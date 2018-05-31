package com.treblemaker.model.bassline;

import com.treblemaker.weighters.enums.WeightClass;

public class BasslineWithRating {

    private int totalWeight = 0;
    private Bassline bassline;

    public void incrementWeight(WeightClass weightClass){
        totalWeight = totalWeight + weightClass.getValue();
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public Bassline getBassline() {
        return bassline;
    }

    public void setBassline(Bassline bassline) {
        this.bassline = bassline;
    }
}
