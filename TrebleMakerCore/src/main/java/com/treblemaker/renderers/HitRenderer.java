package com.treblemaker.renderers;

import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.generators.interfaces.IShimGenerator;
import com.treblemaker.model.hitsandfills.Hit;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.renderers.interfaces.IHitRenderer;
import com.treblemaker.utils.FileStructure;
import com.treblemaker.utils.LoopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class HitRenderer implements IHitRenderer {

    @Autowired
    private IShimGenerator shimGenerator;

    @Autowired
    BaseRenderer baseRenderer;

    @Autowired
    public AppConfigs appConfigs;

    @Autowired
    private FileStructure fileStructure;

    @Override
    public void render(QueueState queueState) throws Exception {

        List<Hit> hitsToRender = createHitsToRender(queueState.getStructure());

        List<String> hitFilesToRender = new ArrayList<>();

        for (Hit hit : hitsToRender) {
            if(hit == null){
                hitFilesToRender.add(createHitShim(queueState).getAbsolutePath());
            }else{
                hitFilesToRender.add(appConfigs.getHitsFullPath(hit));
            }
        }

        if(!hitFilesToRender.isEmpty()) {
            baseRenderer.concatenateFiles(hitFilesToRender, queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.HITS_FILENAME);
        }
    }

    @Override
    public List<Hit> createHitsToRender(List<ProgressionUnit> progressionUnits) {

        List<Hit> hits = new ArrayList<>();

        for(ProgressionUnit progressionUnit : progressionUnits){

            int shimCount = 4;

            if(progressionUnit.getProgressionUnitBars().get(0).getHit() != null){
                hits.add(progressionUnit.getProgressionUnitBars().get(0).getHit());
                shimCount = shimCount - progressionUnit.getProgressionUnitBars().get(0).getHit().getBarCount();
            }

            for(int i=0; i<shimCount; i++){
                hits.add(null);
            }
        }

        return hits;
    }

    @Override
    public File createHitShim(QueueState queueState) {

        String filePath = fileStructure.createShimsDirectory(queueState.getQueueItem().getQueueItemId());

        String shimName = createNameForHitShim();

        String shimPath = filePath + "/" + shimName;

        if (new File(shimPath).exists()) {
            try {
                FileStructure.deleteFile(new File(shimPath));
            } catch (IOException e) {
                Application.logger.debug("LOG:",e);
            }
        }

        shimGenerator.generateSilence(LoopUtils.getBeatsInSeconds(queueState.getQueueItem().getBpm(), 4), shimPath, 1);

        return new File(shimPath);
    }

    public String createNameForHitShim() {

        //TODO IS THE NAME UNIQUE ENOUGH?? WHAT IF MULTIPLE SHIMS HAVE THE SAME LENGTH??

//        String shimLength = Float.toString(ambienceLoop.getShimLength());
//        String shimLengthString = shimLength.replaceAll("\\.", "");
        String shimName = "onebar.wav";

        return shimName;
    }
}
