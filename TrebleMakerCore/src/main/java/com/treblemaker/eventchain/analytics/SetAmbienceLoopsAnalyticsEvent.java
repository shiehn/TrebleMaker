package com.treblemaker.eventchain.analytics;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.progressions.ProgressionUnit;

public class SetAmbienceLoopsAnalyticsEvent implements IEventChain {

    @Override
    public QueueState set(QueueState queueState ) {
        for (ProgressionUnit progressionUnit : queueState.getStructure()) {

            for (int i = 0; i < progressionUnit.getProgressionUnitBars().size(); i++) {

                if (i < progressionUnit.getProgressionUnitBars().size()) {
                    if (progressionUnit.getProgressionUnitBars().get(i).getAmbienceLoop() != null) {
                        progressionUnit.getProgressionUnitBars().get(i).getCompositionTimeSlot().setAmbientLoopId(progressionUnit.getProgressionUnitBars().get(i).getAmbienceLoop().getId());
                    } else if (progressionUnit.getProgressionUnitBars().get(i - 1).getAmbienceLoop() != null) {
                        progressionUnit.getProgressionUnitBars().get(i).getCompositionTimeSlot().setAmbientLoopId(progressionUnit.getProgressionUnitBars().get(i - 1).getAmbienceLoop().getId());
                    } else if (progressionUnit.getProgressionUnitBars().get(i - 2).getAmbienceLoop() != null) {
                        progressionUnit.getProgressionUnitBars().get(i).getCompositionTimeSlot().setAmbientLoopId(progressionUnit.getProgressionUnitBars().get(i - 2).getAmbienceLoop().getId());
                    } else if (progressionUnit.getProgressionUnitBars().get(i - 3).getAmbienceLoop() != null) {
                        progressionUnit.getProgressionUnitBars().get(i).getCompositionTimeSlot().setAmbientLoopId(progressionUnit.getProgressionUnitBars().get(i - 3).getAmbienceLoop().getId());
                    }
                }
            }
        }

        return queueState;
    }
}