package com.treblemaker.weighters.volumeweighter;

import com.treblemaker.weighters.enums.WeightClass;
import java.util.Map;

public class VolumeWeighterTaskResponse {

    private Map<String, Double> potentialMix;
    private WeightClass  weightClass;

    public Map<String, Double> getPotentialMix() {
        return potentialMix;
    }

    public void setPotentialMix(Map<String, Double> potentialMix) {
        this.potentialMix = potentialMix;
    }

    public WeightClass getWeightClass() {
        return weightClass;
    }

    public void setWeightClass(WeightClass weightClass) {
        this.weightClass = weightClass;
    }
}
