package com.treblemaker.weighters.helper;

import com.treblemaker.model.progressions.ProgressionUnitBar;

public class CurrentPriorBar {

    private ProgressionUnitBar currentBar;
    private ProgressionUnitBar oneBarPrior;
    private ProgressionUnitBar secondBarPrior;

    public ProgressionUnitBar getCurrentBar() {
        return currentBar;
    }

    public void setCurrentBar(ProgressionUnitBar currentBar) {
        this.currentBar = currentBar;
    }

    public ProgressionUnitBar getOneBarPrior() {
        return oneBarPrior;
    }

    public void setOneBarPrior(ProgressionUnitBar oneBarPrior) {
        this.oneBarPrior = oneBarPrior;
    }

    public ProgressionUnitBar getSecondBarPrior() {
        return secondBarPrior;
    }

    public void setSecondBarPrior(ProgressionUnitBar secondBarPrior) {
        this.secondBarPrior = secondBarPrior;
    }
}
