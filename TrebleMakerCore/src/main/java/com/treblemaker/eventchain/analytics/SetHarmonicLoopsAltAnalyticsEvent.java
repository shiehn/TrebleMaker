package com.treblemaker.eventchain.analytics;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.queues.QueueState;
import org.springframework.stereotype.Component;

@Component
public class SetHarmonicLoopsAltAnalyticsEvent implements IEventChain {

    @Override
    public QueueState  set(QueueState queueState ) {

        queueState.getStructure().forEach(progressionUnit -> {

            progressionUnit.getProgressionUnitBars().forEach(pBar -> {

                if(pBar.getHarmonicLoopAlt() == null){
                    pBar.getCompositionTimeSlot().setHarmonicLoopAltId(null);
                }else{
                    pBar.getCompositionTimeSlot().setHarmonicLoopAltId(pBar.getHarmonicLoopAlt().getId());
                }
            });
        });

        return queueState;
    }
}