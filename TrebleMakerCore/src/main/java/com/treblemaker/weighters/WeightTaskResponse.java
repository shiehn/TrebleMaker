package com.treblemaker.weighters;

import com.treblemaker.weighters.enums.WeightClass;

public class WeightTaskResponse {

    private Integer loopId;
    private WeightClass weightClass;

    public WeightTaskResponse(Integer loopId, WeightClass weightClass ) {
        this.loopId = loopId;
        this.weightClass = weightClass;
    }

    public Integer getLoopId() {
        return loopId;
    }

    public void setLoopId(Integer loopId) {
        this.loopId = loopId;
    }

    public WeightClass getWeightClass() {
        return weightClass;
    }

    public void setWeightClass(WeightClass weightClass) {
        this.weightClass = weightClass;
    }

}
