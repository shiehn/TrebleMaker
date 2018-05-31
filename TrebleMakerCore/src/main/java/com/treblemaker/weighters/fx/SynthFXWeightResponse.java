package com.treblemaker.weighters.fx;

import com.treblemaker.weighters.enums.WeightClass;

public class SynthFXWeightResponse {

    private int synthId;
    private int fxArpeggioWithRatingId;
    private WeightClass weight;

    public SynthFXWeightResponse(int fxArpeggioWithRatingId, int synthId, WeightClass weight){
        this.synthId = synthId;
        this.fxArpeggioWithRatingId = fxArpeggioWithRatingId;
        this.weight = weight;
    }

    public int getSynthId() {
        return synthId;
    }

    public void setSynthId(int synthId) {
        this.synthId = synthId;
    }

    public int getFxArpeggioWithRatingId() {
        return fxArpeggioWithRatingId;
    }

    public void setFxArpeggioWithRatingId(int fxArpeggioWithRatingId) {
        this.fxArpeggioWithRatingId = fxArpeggioWithRatingId;
    }

    public WeightClass getWeight() {
        return weight;
    }

    public void setWeight(WeightClass weight) {
        this.weight = weight;
    }
}