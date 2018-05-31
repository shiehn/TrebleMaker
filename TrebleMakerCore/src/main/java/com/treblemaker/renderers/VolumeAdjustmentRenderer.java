package com.treblemaker.renderers;

import com.treblemaker.Application;
import com.treblemaker.services.audiofilter.NormalizeAudio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class VolumeAdjustmentRenderer {

    @Autowired
    private NormalizeAudio normalizeAudio;

    public Map<String,Double> calculateMixTargetOffsets(Map<String, Double> currentMeans, Map<String, Double> targetMeans){

        Map<String,Double> offsets = new HashMap<>();

        for(Map.Entry<String, Double> targetMap : targetMeans.entrySet()){

            Double target = targetMap.getValue();
            Double current = currentMeans.get(targetMap.getKey()).doubleValue();

            Double offset = target - current;

            offsets.put(targetMap.getKey(), offset);
        }

        return offsets;
    }

    public void renderMixVolumesWithTargetOffsets(List<Map<Path, Double>> roleToOffsetMapList, String directoryPath) {

        try {

            List<Path> alreadyAdjusted = new ArrayList<>();
            List<Path> alreadyRenamed = new ArrayList<>();

            for(Map<Path, Double> roleToOffsetMap : roleToOffsetMapList) {
                normalizeAudio.normalizeToTarget(roleToOffsetMap, alreadyAdjusted, alreadyRenamed);
            }
        } catch (IOException e) {
            Application.logger.debug("LOG:",e);
        }
    }
}
