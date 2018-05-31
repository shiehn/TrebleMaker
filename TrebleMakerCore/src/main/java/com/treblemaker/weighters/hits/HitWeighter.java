package com.treblemaker.weighters.hits;

import com.treblemaker.model.hitsandfills.Hit;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.weighters.enums.WeightClass;

public class HitWeighter implements IHitWeighter {

    @Override
    public QueueState setHitWeights(QueueState queueState) {

        //TODO THIS IS TEMP CODE !!!!!
        //TODO THIS IS TEMP CODE !!!!!
        //TODO THIS IS TEMP CODE !!!!!
        //TODO THIS IS TEMP CODE !!!!!
        //TODO MUST SET WEIGHTS !!!!!

        for(ProgressionUnit progressionUnit : queueState.getStructure()){
            for(ProgressionUnitBar progressionUnitBar : progressionUnit.getProgressionUnitBars()){
                for(Hit hitOption : progressionUnitBar.getHitOptions()){
                    hitOption.setVerticalWeight(WeightClass.OK);
                }
            }
        }

        return queueState;
    }
}
