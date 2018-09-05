package com.treblemaker.extractors;

import com.treblemaker.Application;
import com.treblemaker.utils.interfaces.IAudioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class VolumeExtractor {
    private IAudioUtils audioUtils;

    @Autowired
    public VolumeExtractor(IAudioUtils audioUtils){
        this.audioUtils = audioUtils;
    }

    public List<Map<String, Double>> getVolumeMeans(List<Map<String, File>> roleToFileMaps) {
        List<Map<String, Double>> roleToMeanMapList = new ArrayList<>();

        for(Map<String, File> roleToFileMap : roleToFileMaps) {
            Map<String, Double> roleToMeanMap = new HashMap<>();

            for (Map.Entry<String, File> entry : roleToFileMap.entrySet()) {
                try {
                    double mean = audioUtils.getMeanVolume(entry.getValue());
                    roleToMeanMap.put(entry.getKey(), mean);
                } catch (InterruptedException e) {
                    Application.logger.debug("LOG:", e);
                }
            }
            roleToMeanMapList.add(roleToMeanMap);
        }

        return roleToMeanMapList;
    }
}
