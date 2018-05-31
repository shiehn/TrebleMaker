package com.treblemaker.eventchain.analytics;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.queues.QueueState;
import org.springframework.stereotype.Component;

@Component
public class SetHitsAnalyticsEvent implements IEventChain {

    @Override
    public QueueState set(QueueState queueState) {
        queueState.getStructure().forEach(progressionUnit -> {

            progressionUnit.getProgressionUnitBars().forEach(pBar -> {

                if (pBar.getHit() == null) {
                    pBar.getCompositionTimeSlot().setHitId(null);
                } else {
                    pBar.getCompositionTimeSlot().setHitId(pBar.getHit().getId());
                }
            });
        });

        return queueState;
    }
}
