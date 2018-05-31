package com.treblemaker.eventchain.analytics;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.queues.QueueState;

public class SetHarmonicLoopsAnalyticsEvent implements IEventChain {

    @Override
    public QueueState set(QueueState queueState ) {
        queueState.getStructure().forEach(progressionUnit -> {

            progressionUnit.getProgressionUnitBars().forEach(pBar -> {

                if(pBar.getHarmonicLoop() == null){
                    pBar.getCompositionTimeSlot().setHarmonicLoopId(null);
                }else{
                    pBar.getCompositionTimeSlot().setHarmonicLoopId(pBar.getHarmonicLoop().getId());
                }
            });
        });

        return queueState;
    }
}
