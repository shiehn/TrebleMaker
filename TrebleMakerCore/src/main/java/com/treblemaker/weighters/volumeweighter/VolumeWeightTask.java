package com.treblemaker.weighters.volumeweighter;

import com.treblemaker.machinelearning.VolumeLevelClassifier;
import com.treblemaker.weighters.enums.WeightClass;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.concurrent.Callable;

public class VolumeWeightTask implements Callable<VolumeWeighterTaskResponse> {

    private Map<String, Double> potentialMix;
    private boolean bypassRatings;
    String apiUser;
    String apiPassword;

    public VolumeWeightTask(Map<String, Double> potentialMix, boolean bypassRatings, String apiUser, String apiPassword) {
        this.potentialMix = potentialMix;
        this.bypassRatings = bypassRatings;
        this.apiUser = apiUser;
        this.apiPassword = apiPassword;
    }

    @Override
    public VolumeWeighterTaskResponse call() throws Exception {

        VolumeLevelClassifier volumeLevelClassifier = new VolumeLevelClassifier(bypassRatings, apiUser, apiPassword);
        WeightClass weightClass = volumeLevelClassifier.classify(potentialMix);

        VolumeWeighterTaskResponse volumeWeighterTaskResponse = new VolumeWeighterTaskResponse();
        volumeWeighterTaskResponse.setPotentialMix(potentialMix);
        volumeWeighterTaskResponse.setWeightClass(weightClass);

        return volumeWeighterTaskResponse;
    }
}
