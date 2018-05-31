package com.treblemaker.selectors;

import com.treblemaker.model.hitsandfills.Hit;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.selectors.interfaces.IHitSelector;

import java.util.Random;

public class HitSelector implements IHitSelector {

    @Override
    public QueueState selectHits(QueueState queueState) {

        //TODO THIS IS TOTAL RANDOM ... FIX THIS ...

        //FOR NOW JUST SELECT ONE HIT ...
        Hit selectedHit = queueState.getDataSource().getHits(queueState.getQueueItem().getStationId()).get(new Random().nextInt(queueState.getDataSource().getHits(queueState.getQueueItem().getStationId()).size()));

        for(ProgressionUnit progressionUnit:queueState.getStructure()){
            for(ProgressionUnitBar progressionUnitBar : progressionUnit.getProgressionUnitBars()){
                progressionUnitBar.setHit(selectedHit);
            }
        }

        /*
        for(ProgressionUnit progressionUnit:queueState.getStructure()){
            for(ProgressionUnitBar progressionUnitBar : progressionUnit.getProgressionUnitBars()){
                progressionUnitBar.setHit(selectHit(progressionUnitBar));
            }
        }
        */

        return queueState;
    }

    private Hit selectHit(ProgressionUnitBar progressionUnitBar){

        Hit highestRated = null;
        for(Hit hit : progressionUnitBar.getHitOptions()){
            if(highestRated == null || hit.getTotalWeight() >  highestRated.getTotalWeight()){
                highestRated = hit;
            }
        }

        return highestRated;
    }
}
