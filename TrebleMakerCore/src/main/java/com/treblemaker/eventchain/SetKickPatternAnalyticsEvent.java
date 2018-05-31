package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;

public class SetKickPatternAnalyticsEvent implements IEventChain {

    @Override
    public QueueState set(QueueState queueState) {

        for(ProgressionUnit progressionUnit : queueState.getStructure()){
            for(ProgressionUnitBar progressionUnitBar : progressionUnit.getProgressionUnitBars()){
                progressionUnitBar.getCompositionTimeSlot().setKickPatternId(progressionUnitBar.getKickPattern().getId());
            }
        }

        return queueState;
    }
}
