package com.treblemaker.eventchain.analytics;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.queues.QueueState;
import org.springframework.stereotype.Component;

@Component
public class SetBeatLoopAltAnalyticsEvent implements IEventChain {

    @Override
    public QueueState set(QueueState queueState ) {
        queueState.getStructure().forEach(pUnit -> {

            pUnit.getProgressionUnitBars().forEach(pBar -> {

                if(pBar.getBeatLoopAlt() == null){
                    pBar.getCompositionTimeSlot().setBeatLoopAltId(null);
                }else {
                    pBar.getCompositionTimeSlot().setBeatLoopAltId(pBar.getBeatLoopAlt().getId());
                }
            });
        });

        return queueState;
    }
}