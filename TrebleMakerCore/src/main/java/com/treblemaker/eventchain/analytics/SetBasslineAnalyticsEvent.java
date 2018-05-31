package com.treblemaker.eventchain.analytics;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.queues.QueueState;

public class SetBasslineAnalyticsEvent implements IEventChain {

    @Override
    public QueueState set(QueueState queueState ) {

        queueState.getStructure().forEach(progressionUnit -> {
            progressionUnit.getProgressionUnitBars().forEach(progressionUnitBar -> {
                progressionUnitBar.getCompositionTimeSlot().setBasslineId(progressionUnitBar.getBasslineId());
            });
        });

        return queueState;
    }
}
