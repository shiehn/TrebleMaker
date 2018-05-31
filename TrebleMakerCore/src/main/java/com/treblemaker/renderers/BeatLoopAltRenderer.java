package com.treblemaker.renderers;

import com.treblemaker.configs.AppConfigs;
import com.treblemaker.Application;
import com.treblemaker.generators.interfaces.IShimGenerator;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.rendertransports.BeatLoopRenderTransport;
import com.treblemaker.renderers.interfaces.IBeatLoopRenderer;
import com.treblemaker.utils.FileStructure;
import com.treblemaker.utils.LoopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class BeatLoopAltRenderer extends BaseRenderer  implements IBeatLoopRenderer {

    @Autowired
    private IShimGenerator shimGenerator;

    @Autowired
    private AppConfigs appConfigs;

    @Autowired
    private FileStructure fileStructure;

    @Override
    public void renderRhythm(String queueItemId, String rhythmTargetPath, String rhythmFileName, BeatLoopRenderTransport beatLoopRenderTransport) {

        concatenateAndFinalizeRendering(queueItemId, rhythmTargetPath, rhythmFileName, beatLoopRenderTransport.getBeatLoops());
    }

    @Override
    public void concatenateAndFinalizeRendering(String queueItemId, String targetPath, String fileName, List<BeatLoop> beatLoops) {
        try {

            Application.logger.debug("LOG: ******** Start RENDERING ALT BEATS LOOPS *************");

            List<String> sourceFilesList = new ArrayList<String>();

            for (BeatLoop beat : beatLoops) {
                if(beat.isShim()){
                    sourceFilesList.add(appConfigs.getBeatShimLoopfullPath(beat, queueItemId));
                }else{
                    sourceFilesList.add(appConfigs.getBeatLoopfullPath(beat));
                }
            }

            File outPutPath = Paths.get(targetPath).toFile();
            if(!outPutPath.exists()){
                outPutPath.mkdirs();
            }

            concatenateFiles(sourceFilesList, targetPath + "/" + fileName);

        } catch (Exception e) {
            //TODO handle exceptions .
            Application.logger.debug("LOG:", e);
        }
    }

    @Override
    public BeatLoopRenderTransport extractLoopsToRender(QueueItem queueItem) {

        List<BeatLoop> beatLoops = new ArrayList<BeatLoop>();

        queueItem.getProgression().getStructure().forEach(pUnit -> {
            pUnit.getProgressionUnitBars().forEach(pUnitBar ->  {

                if(pUnitBar.getBeatLoopAlt() == null){

                    //get the shim file name ..
                    String shimPath = fileStructure.createShimsDirectory(queueItem.getQueueItemId());

                    String shimName = createShimNameForBeatLoopsAlt(queueItem.getQueueItemId(), queueItem.getBpm());

                    if (!Paths.get(shimPath, shimName).toFile().exists()) {

                        //TODO should this be mono or stereo ???
                        double audioLength = LoopUtils.getSecondsInBar(queueItem.getBpm());
                        String absolutePath = Paths.get(appConfigs.getApplicationRoot(), shimPath).toString();
                        absolutePath = absolutePath.replace("//","\\");
                        shimGenerator.generateSilence(audioLength, Paths.get(shimPath, shimName).toString(), 1);
                    }

                    BeatLoop beatLoopShim = new BeatLoop();
                    beatLoopShim.setIsShim(true);
                    beatLoopShim.setFileName(shimName);
                    beatLoopShim.setFilePath(shimPath);

                    beatLoops.add(beatLoopShim);

                }else if(pUnitBar.getBeatLoopAlt().getCurrentBar() == 1){
                    beatLoops.add(pUnitBar.getBeatLoopAlt());
                }
            });
        });

        BeatLoopRenderTransport beatLoopRenderTransport = new BeatLoopRenderTransport();
        beatLoopRenderTransport.setBeatLoops(beatLoops);
        return beatLoopRenderTransport;
    }

    public String createShimNameForBeatLoopsAlt(String queueItemId, int bpm) {

        String shimName = queueItemId + "_BeatLoopAlt_" + bpm + ".wav";

        return shimName;
    }
}
