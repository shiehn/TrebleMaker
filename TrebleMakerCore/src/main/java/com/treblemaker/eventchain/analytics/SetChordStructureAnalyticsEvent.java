package com.treblemaker.eventchain.analytics;

import com.treblemaker.Application;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;

public class SetChordStructureAnalyticsEvent implements IEventChain {

    @Override
    public QueueState set(QueueState queueState ) {
        for(ProgressionUnit progressionUnit : queueState.getStructure()){

            for(int i=0; i<progressionUnit.getProgressionUnitBars().size(); i++) {

                Application.logger.debug("LOG: *PRE_CHORD_ID*");
                Application.logger.debug("LOG: *CHORD_ID* : " + progressionUnit.getProgressionUnitBars().get(i).getChord().getId());
                progressionUnit.getProgressionUnitBars().get(i).getCompositionTimeSlot().setChordId(progressionUnit.getProgressionUnitBars().get(i).getChord().getId());
            }
        }

        return queueState;
    }
}
