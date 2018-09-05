package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.extractors.VolumeExtractor;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.renderers.VolumeAdjustmentRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.treblemaker.configs.*;
import com.treblemaker.constants.*;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderAndSetFinalVolumeMixEvent implements IEventChain {

    @Autowired
    private VolumeExtractor volumeExtractor;

    @Autowired
    private VolumeAdjustmentRenderer volumeAdjustmentRenderer;

    @Autowired
    private AppConfigs appConfigs;

    @Value("${num_of_generated_mixes}")
    Integer numOfGeneratedMixes;

    @Override
    public QueueState set(QueueState queueState) {

        List<Map<String, File>> roleToFileMap = getRoleToFileMap(queueState, numOfGeneratedMixes);

        List<Map<Path, Double>> pathToOffsetMapList = convertPathsToOffsets(queueState.getQueueItem().getSelectedVolumeTargets(), roleToFileMap);

        //RENDER THE MixWithTheOffsets ...
        volumeAdjustmentRenderer.renderMixVolumesWithTargetOffsets(pathToOffsetMapList, appConfigs.getCompositionOutput() + "/stereoparts/" + queueState.getQueueItem().getId());


        //AFTER THE MIX IS COMPLETE ADJUST THE OVERALL LEVEL TO DESIRED MAX DB ...
        //AFTER THE MIX IS COMPLETE ADJUST THE OVERALL LEVEL TO DESIRED MAX DB ...
        //AFTER THE MIX IS COMPLETE ADJUST THE OVERALL LEVEL TO DESIRED MAX DB ...
        //AFTER THE MIX IS COMPLETE ADJUST THE OVERALL LEVEL TO DESIRED MAX DB ...
        //AFTER THE MIX IS COMPLETE ADJUST THE OVERALL LEVEL TO DESIRED MAX DB ...
        //AFTER THE MIX IS COMPLETE ADJUST THE OVERALL LEVEL TO DESIRED MAX DB ...
        // COPY pathToOffsetMap and set each value to the same value ex. 20db


        List<Map<Path, Double>> pathToDbTargetList = new ArrayList<>();

        for(Map<Path, Double> pathToOffsetMap: pathToOffsetMapList) {
            Map<Path, Double> pathToDbTarget = new HashMap<>();

            for (Map.Entry<Path, Double> entry : pathToOffsetMap.entrySet()) {
                pathToDbTarget.put(entry.getKey(), 12.0);
            }

            pathToDbTargetList.add(pathToDbTarget);
        }

        volumeAdjustmentRenderer.renderMixVolumesWithTargetOffsets(pathToDbTargetList, appConfigs.getCompositionOutput() + "/stereoparts/" + queueState.getQueueItem().getId());

        //AFTER THE MIX IS COMPLETE ADJUST THE OVERALL LEVEL TO DESIRED MAX DB ...
        //AFTER THE MIX IS COMPLETE ADJUST THE OVERALL LEVEL TO DESIRED MAX DB ...
        //AFTER THE MIX IS COMPLETE ADJUST THE OVERALL LEVEL TO DESIRED MAX DB ...
        //AFTER THE MIX IS COMPLETE ADJUST THE OVERALL LEVEL TO DESIRED MAX DB ...
        //AFTER THE MIX IS COMPLETE ADJUST THE OVERALL LEVEL TO DESIRED MAX DB ...
        //AFTER THE MIX IS COMPLETE ADJUST THE OVERALL LEVEL TO DESIRED MAX DB ...
        //AFTER THE MIX IS COMPLETE ADJUST THE OVERALL LEVEL TO DESIRED MAX DB ...

        List<Map<String, Double>> roleToCurrentMeanMap = volumeExtractor.getVolumeMeans(roleToFileMap);
        queueState.getQueueItem().setFinalVolumeLevels(roleToCurrentMeanMap);

        return queueState;
    }

    public List<Map<Path, Double>> convertPathsToOffsets(Map<String, Double> roleToOffsetMap, List<Map<String, File>> roleToFileMap) {

        List<Map<Path, Double>> pathsToOffsetsList = new ArrayList<>();

        for (int i = 0; i < roleToFileMap.size(); i++) {
            Map<Path, Double> pathsToOffsets = new HashMap<>();

            for (Map.Entry<String, Double> roleToOffset : roleToOffsetMap.entrySet()) {
                pathsToOffsets.put(roleToFileMap.get(i).get(roleToOffset.getKey()).toPath(), roleToOffset.getValue());
            }

            pathsToOffsetsList.add(pathsToOffsets);
        }

        return pathsToOffsetsList;
    }

    public List<Map<String, File>> getRoleToFileMap(QueueState queueState, int numOfVariations) {

        List<Map<String, File>> roleToFileMaps = new ArrayList<>();

        for (int i = 0; i < numOfVariations; i++) {
            Map<String, File> roleToFileMap = new HashMap<>();
            roleToFileMap.put(MixRoles.COMP_HI_FX, new File(queueState.getQueueItem().getStereoPartsFilePath() + "/" + i + appConfigs.COMP_HI_FX_AUDIO_FILENAME));
            roleToFileMap.put(MixRoles.COMP_HI_ALT_FX, new File(queueState.getQueueItem().getStereoPartsFilePath() + "/" + i + appConfigs.COMP_ALT_HI_FX_AUDIO_FILENAME));
            roleToFileMap.put(MixRoles.COMP_MID, new File(queueState.getQueueItem().getStereoPartsFilePath() + "/" + i + appConfigs.COMP_MID_AUDIO_FILENAME));
            roleToFileMap.put(MixRoles.COMP_MID_ALT, new File(queueState.getQueueItem().getStereoPartsFilePath() + "/" + i + appConfigs.COMP_ALT_MID_AUDIO_FILENAME));
            roleToFileMap.put(MixRoles.COMP_LOW, new File(queueState.getQueueItem().getStereoPartsFilePath() + "/" + i + appConfigs.COMP_LOW_AUDIO_FILENAME));
            roleToFileMap.put(MixRoles.COMP_LOW_ALT, new File(queueState.getQueueItem().getStereoPartsFilePath() + "/" + i + appConfigs.COMP_ALT_LOW_AUDIO_FILENAME));
            roleToFileMap.put(MixRoles.COMP_RHYTHM, new File(queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.COMP_RHYTHM_FILENAME));
            roleToFileMap.put(MixRoles.COMP_RHYTHM_ALT, new File(queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.COMP_RHYTHM_ALT_FILENAME));
            roleToFileMap.put(MixRoles.COMP_AMBIENCE, new File(queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.COMP_AMBIENCE_FILENAME));
            roleToFileMap.put(MixRoles.COMP_HARMONIC, new File(queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.COMP_HARMONIC_FILENAME));
            roleToFileMap.put(MixRoles.COMP_HARMONIC_ALT, new File(queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.COMP_HARMONIC_ALT_FILENAME));
            roleToFileMap.put(MixRoles.HITS, new File(queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.HITS_FILENAME));
            roleToFileMap.put(MixRoles.FILLS, new File(queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.FILLS_FILENAME));
            roleToFileMap.put(MixRoles.KICK, new File(queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.KICK_FILENAME));
            roleToFileMap.put(MixRoles.SNARE, new File(queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.SNARE_FILENAME));
            roleToFileMap.put(MixRoles.HAT, new File(queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.HAT_FILENAME));

            for(int j=0; j<numOfGeneratedMixes; j++){
                roleToFileMap.put(MixRoles.COMP_MELODY + j, new File(queueState.getQueueItem().getStereoPartsFilePath() + "/" + i + appConfigs.COMP_MELODIC_AUDIO_FILENAME_FX.replace(".wav", "_"+j+".wav")));
            }

            roleToFileMaps.add(roleToFileMap);
        }

        return roleToFileMaps;
    }
}
