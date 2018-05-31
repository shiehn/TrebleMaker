package com.treblemaker.model.fx;

import com.treblemaker.weighters.enums.WeightClass;

public class FXArpeggioWithRating {
    private int totalWeight = 0;
    private FXArpeggioDelay fxArpeggioDelay;

    public FXArpeggioWithRating() {
    }

    public FXArpeggioWithRating(FXArpeggioDelay fxArpeggioDelay){
        this.fxArpeggioDelay = fxArpeggioDelay;
    }

    public void incrementWeight(WeightClass weightClass){
        totalWeight = totalWeight + weightClass.getValue();
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public FXArpeggioDelay getFxArpeggioDelay() {
        return fxArpeggioDelay;
    }

    public void setFxArpeggioDelay(FXArpeggioDelay fxArpeggioDelay) {
        this.fxArpeggioDelay = fxArpeggioDelay;
    }
}
