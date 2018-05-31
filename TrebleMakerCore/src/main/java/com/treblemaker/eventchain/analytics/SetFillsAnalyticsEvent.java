package com.treblemaker.eventchain.analytics;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.queues.QueueState;
import org.springframework.stereotype.Component;

@Component
public class SetFillsAnalyticsEvent implements IEventChain {

    @Override
    public QueueState set(QueueState queueState) {

        queueState.getStructure().forEach(progressionUnit -> {

            progressionUnit.getProgressionUnitBars().forEach(pBar -> {

                if(pBar.getFill() == null){
                    pBar.getCompositionTimeSlot().setFillId(null);
                }else{
                    pBar.getCompositionTimeSlot().setFillId(pBar.getFill().getId());
                }
            });
        });

        return queueState;
    }
}
