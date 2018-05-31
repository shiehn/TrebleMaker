package com.treblemaker.eventchain.analytics;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.queues.QueueState;
import org.springframework.stereotype.Component;

@Component
public class SetBeatLoopAnalyticsEvent implements IEventChain {

    @Override
    public QueueState set(QueueState queueState ) {
        queueState.getStructure().forEach(pUnit -> {
            pUnit.getProgressionUnitBars().forEach(pBar -> {

                if(pBar.getBeatLoop() == null){
                    pBar.getCompositionTimeSlot().setBeatLoopId(null);
                }else{
                    pBar.getCompositionTimeSlot().setBeatLoopId(pBar.getBeatLoop().getId());
                }
            });
        });

        return queueState;
    }
}
