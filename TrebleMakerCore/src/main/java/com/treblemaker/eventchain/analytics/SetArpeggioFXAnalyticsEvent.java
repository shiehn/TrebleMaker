package com.treblemaker.eventchain.analytics;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.queues.QueueState;

public class SetArpeggioFXAnalyticsEvent implements IEventChain {

    @Override
    public QueueState set(QueueState queueState) {

        queueState.getStructure().forEach(
                progressionUnit -> progressionUnit.getProgressionUnitBars().forEach(
                        progressionUnitBar -> progressionUnitBar.getCompositionTimeSlot()
                                .setFxArpeggioDelayId(progressionUnitBar.getSelectedFXId(0)))); //TODO THIS IS HAD CODED TO THE FIRST INDEX ... SO ANALYTICS WILL ONLY WORK FOR THE FIRST INDEX

        return queueState;
    }
}