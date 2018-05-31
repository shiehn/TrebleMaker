package com.treblemaker.renderers;

import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.model.hat.HatPattern;
import com.treblemaker.model.hat.HatSample;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class HatPatternRenderer extends BaseRenderer {

    @Autowired
    public AppConfigs appConfigs;

    private static String PATH_TO_SHIM = "";

    public void render(QueueState queueState) {

        // create a list of all the patterns ...
        List<HatPattern> patternsToRender = getPatternsToRender(queueState);

        // create a list of all the sounds ...
        List<HatSample> hatSamples = queueState.getDataSource().getHatSamples().stream().filter(h -> Integer.parseInt(h.getStationId()) == queueState.getQueueItem().getStationId()).collect(Collectors.toList());

        HatSample hatSample = hatSamples.get(new Random().nextInt(hatSamples.size()));

        //CREATE SHIM ..
        // create a list of all the beat sample paths
        List<String> hatPatternFilePaths = getHatPatternFilePaths(patternsToRender, hatSample, queueState.getQueueItem().getBpm());

        //targetPath + fileName
        String targetPath = queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.HAT_FILENAME;

        try {
            concatenateFiles(hatPatternFilePaths, targetPath);
        } catch (Exception e) {
            Application.logger.debug("LOG:",e);
        }

        return;
    }

    public List<HatPattern> getPatternsToRender(QueueState queueState) {

        List<HatPattern> patternsToRender = new ArrayList<>();

        for (ProgressionUnit progressionUnit : queueState.getStructure()) {
            for (int i = 0; i < progressionUnit.getProgressionUnitBars().size(); i++) {
                patternsToRender.add(progressionUnit.getProgressionUnitBars().get(i).getHatPattern());
            }
        }

        return patternsToRender;
    }

    public List<String> getHatPatternFilePaths(List<HatPattern> patternsToRender, HatSample hatSample, int bpm) {

        List<String> hatPatternFilePaths = new ArrayList<>();

        for (HatPattern hatPatternFilePath : patternsToRender) {
            for (int hatHit : hatPatternFilePath.getAsIntegerArray()) {
                if (hatHit == 0) {
                    hatPatternFilePaths.add(appConfigs.getHatShimFullPath(hatSample, bpm));
                } else if (hatHit == 1) {
                    hatPatternFilePaths.add(appConfigs.getHatFullPath(hatSample));
                } else {
                    throw new RuntimeException("Unexpected Hat Value");
                }
            }
        }

        return hatPatternFilePaths;
    }
}