package com.treblemaker.renderers;

import com.treblemaker.Application;
import com.treblemaker.generators.interfaces.IShimGenerator;
import com.treblemaker.model.AmbienceLoop;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.renderers.interfaces.IAmbienceLoopRenderer;
import com.treblemaker.utils.FileStructure;
import com.treblemaker.utils.interfaces.IAudioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.treblemaker.configs.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class AmbienceLoopRenderer extends BaseRenderer implements IAmbienceLoopRenderer {

    @Autowired
    private IShimGenerator shimGenerator;

    @Autowired
    private IAudioUtils audioUtils;

    @Autowired
    private AppConfigs appConfigs;

    @Autowired
    private FileStructure fileStructure;

    public QueueItem renderAmbienceLoops(String ambienceLoopsTargetPath, String ambienceLoopFileName, QueueItem queueItem) throws Exception {

        //generate the shims for each loopType ..
        queueItem.getProgression().getStructure().forEach(pUnit -> {

            for (int i = 0; i < pUnit.getProgressionUnitBars().size(); i++) {

                //TODO CURRENTLY JUST RENDER BAR ONE !!!
                //TODO THIS IS A TEMPURARY IMPLEMENTATION ...

                if (i == 0) {

                    ProgressionUnitBar pBar = pUnit.getProgressionUnitBars().get(i);

                    //get the shim file name ..
                    String filePath = fileStructure.createShimsDirectory(queueItem.getQueueItemId());

                    String shimName = createShimNameForAmbienceLoops(pBar.getAmbienceLoop());

                    String shimPath = filePath + "/" + shimName;

                    if (!new File(shimPath).exists()) {
                        shimGenerator.generateSilence(pBar.getAmbienceLoop().getShimLength(), shimPath, audioUtils.isMonoOrStereo(appConfigs.getAmbienceFullPath(pBar.getAmbienceLoop())));
                    }
                }
            }
        });

        List<AmbienceLoop> ambienceLoops = new ArrayList<AmbienceLoop>();

        queueItem.getProgression().getStructure().forEach(pUnit -> {

            for (int i = 0; i < pUnit.getProgressionUnitBars().size(); i++) {
                if (i == 0) {
                    //TODO THIS IS JUST TO GET THINGS WORKING ..
                    //TODO ABIENCE LOOPS ONLY SUppoRT BAR ONE ..
                    ambienceLoops.add(pUnit.getProgressionUnitBars().get(0).getAmbienceLoop());
                }
            }
        });

        Application.logger.debug("LOG: Start Concatinating Ambience looops ..");

        List<String> sourceFilesList = new ArrayList<String>();

        for (AmbienceLoop ambienceLoop : ambienceLoops) {
            sourceFilesList.add(appConfigs.getAmbienceFullPath(ambienceLoop));
            sourceFilesList.add(appConfigs.getCompositionOutput() + "/audioshims/" + queueItem.getQueueItemId() + "/" + createShimNameForAmbienceLoops(ambienceLoop));
        }

        concatenateFiles(sourceFilesList, ambienceLoopsTargetPath + "/" + ambienceLoopFileName);

        return queueItem;
    }

    public String createShimNameForAmbienceLoops(AmbienceLoop ambienceLoop) {

        //TODO IS THE NAME UNIQUE ENOUGH?? WHAT IF MULTIPLE SHIMS HAVE THE SAME LENGTH??

        String shimLength = Float.toString(ambienceLoop.getShimLength());
        String shimLengthString = shimLength.replaceAll("\\.", "");
        String shimName = shimLengthString + ".wav";

        return shimName;
    }
}
