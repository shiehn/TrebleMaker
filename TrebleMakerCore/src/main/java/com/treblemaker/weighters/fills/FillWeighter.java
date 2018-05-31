package com.treblemaker.weighters.fills;

import com.treblemaker.model.hitsandfills.Fill;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.weighters.enums.WeightClass;

public class FillWeighter implements ISetFillWeighter{

    @Override
    public QueueState setFillWeights(QueueState queueState) {
        for (ProgressionUnit progressionUnit : queueState.getStructure()) {
            for (ProgressionUnitBar progressionUnitBar : progressionUnit.getProgressionUnitBars()) {
                for (Fill option : progressionUnitBar.getFillOptions()) {

                    //TODO THIS IS BULLSHIT AND NEEDS TO BE UN_HARDCODED
                    option.setVerticalWeight(WeightClass.GOOD);
                }
            }
        }

        return queueState;
    }
}
