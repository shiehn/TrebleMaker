package com.treblemaker.selectors;

import com.treblemaker.model.hitsandfills.Fill;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.selectors.interfaces.IFillSelector;

import java.util.Random;

public class FillSelector implements IFillSelector {

    @Override
    public QueueState selectFills(QueueState queueState) {

        //TODO TOTAL BULL SHIT MUST EVENTRUALL BE REMOVED !!!!!
        //TODO TOTAL BULL SHIT MUST EVENTRUALL BE REMOVED !!!!!
        //TODO TOTAL BULL SHIT MUST EVENTRUALL BE REMOVED !!!!!
        //TODO TOTAL BULL SHIT MUST EVENTRUALL BE REMOVED !!!!!
        Fill selectedFill = queueState.getDataSource().getFills(queueState.getQueueItem().getStationId()).get(new Random().nextInt(queueState.getDataSource().getFills(queueState.getQueueItem().getStationId()).size()));

//        Fill randomFill = progressionUnitBar.getFillOptions().get(new Random().nextInt(progressionUnitBar.getFillOptions().size()));

        for(ProgressionUnit progressionUnit:queueState.getStructure()){
            for(ProgressionUnitBar progressionUnitBar : progressionUnit.getProgressionUnitBars()){
                if(!progressionUnitBar.getFillOptions().isEmpty()){
                    progressionUnitBar.setFill(selectedFill);
                }
            }
        }

        return queueState;
    }
}