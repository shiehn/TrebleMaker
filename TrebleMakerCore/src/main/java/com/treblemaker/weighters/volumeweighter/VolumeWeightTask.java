package com.treblemaker.weighters.volumeweighter;

import com.treblemaker.machinelearning.VolumeLevelClassifier;
import com.treblemaker.weighters.enums.WeightClass;

import java.util.Map;
import java.util.concurrent.Callable;

public class VolumeWeightTask implements Callable<VolumeWeighterTaskResponse> {

    private Map<String, Double> potentialMix;
    private boolean bypassRatings;

    public VolumeWeightTask(Map<String, Double> potentialMix, boolean bypassRatings) {
        this.potentialMix = potentialMix;
        this.bypassRatings = bypassRatings;
    }

    @Override
    public VolumeWeighterTaskResponse call() throws Exception {

        VolumeLevelClassifier volumeLevelClassifier = new VolumeLevelClassifier(bypassRatings);
        WeightClass weightClass = volumeLevelClassifier.classify(potentialMix);

        VolumeWeighterTaskResponse volumeWeighterTaskResponse = new VolumeWeighterTaskResponse();
        volumeWeighterTaskResponse.setPotentialMix(potentialMix);
        volumeWeighterTaskResponse.setWeightClass(weightClass);

        return volumeWeighterTaskResponse;
    }
}
