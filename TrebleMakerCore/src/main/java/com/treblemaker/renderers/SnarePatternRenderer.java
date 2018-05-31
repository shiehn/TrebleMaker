package com.treblemaker.renderers;

import com.treblemaker.Application;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.snare.SnarePattern;
import com.treblemaker.model.snare.SnareSample;
import com.treblemaker.configs.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Component
public class SnarePatternRenderer extends BaseRenderer {

    @Autowired
    private AppConfigs appConfigs;

    private static String PATH_TO_SHIM = "";

    public void render(QueueState queueState) {

        // create a list of all the patterns ...
        List<SnarePattern> patternsToRender = getPatternsToRender(queueState);

        // create a list of all the sounds ...
        List<SnareSample> snareSamples = queueState.getDataSource().getSnareSamples().stream().filter(s -> Integer.parseInt(s.getStationId()) == queueState.getQueueItem().getStationId()).collect(Collectors.toList());
        SnareSample snareSample = snareSamples.get(new Random().nextInt(snareSamples.size()));

        //CREATE SHIM ..
        // create a list of all the beat sample paths
        List<String> snarePatternFilePaths = getSnarePatternFilePaths(patternsToRender, snareSample, queueState.getQueueItem().getBpm());

        //targetPath + fileName
        String targetPath = queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.SNARE_FILENAME;

        try {
            concatenateFiles(snarePatternFilePaths, targetPath);
        } catch (Exception e) {
            Application.logger.debug("LOG:",e);
        }

        return;
    }

    public List<SnarePattern> getPatternsToRender(QueueState queueState) {

        List<SnarePattern> patternsToRender = new ArrayList<>();

        for (ProgressionUnit progressionUnit : queueState.getStructure()) {
            for (int i = 0; i < progressionUnit.getProgressionUnitBars().size(); i++) {
                patternsToRender.add(progressionUnit.getProgressionUnitBars().get(i).getSnarePattern());
            }
        }

        return patternsToRender;
    }

    public List<String> getSnarePatternFilePaths(List<SnarePattern> patternsToRender, SnareSample snareSample, int bpm) {

        List<String> snarePatternFilePaths = new ArrayList<>();

        for (SnarePattern snarePatternFilePath : patternsToRender) {
            for (int snareHit : snarePatternFilePath.getAsIntegerArray()) {
                if (snareHit == 0) {
                    snarePatternFilePaths.add(appConfigs.getSnareShimFullPath(snareSample, bpm));
                } else if (snareHit == 1) {
                    snarePatternFilePaths.add(appConfigs.getSnareFullPath(snareSample));
                } else {
                    throw new RuntimeException("Unexpected Kick Value");
                }
            }
        }

        return snarePatternFilePaths;
    }
}