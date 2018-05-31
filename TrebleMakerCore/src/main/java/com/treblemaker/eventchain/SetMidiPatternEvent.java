package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.extractors.PatternSetter;
import com.treblemaker.model.queues.QueueState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetMidiPatternEvent implements IEventChain {

    @Autowired
    private PatternSetter patternSetter;

    @Override
    public QueueState set(QueueState queueState) {

        queueState.getStructure().forEach(progressionUnit -> {

            boolean skip = false;

            for (int i = 0; i < progressionUnit.getProgressionUnitBars().size(); i++) {

                if (!skip) {
                    patternSetter.setHiPattern(progressionUnit.getProgressionUnitBars().get(i), queueState.getQueueItem().getBpm());
                    patternSetter.setAltHiPattern(progressionUnit.getProgressionUnitBars().get(i), queueState.getQueueItem().getBpm());
                }

                if (!skip) {
                    patternSetter.setMidPattern(progressionUnit.getProgressionUnitBars().get(i), queueState.getQueueItem().getBpm());
                    patternSetter.setAltMidPattern(progressionUnit.getProgressionUnitBars().get(i), queueState.getQueueItem().getBpm());
                }

                if (!skip) {
                    patternSetter.setLowPattern(progressionUnit.getProgressionUnitBars().get(i), queueState.getQueueItem().getBpm());
                    patternSetter.setAltLowPattern(progressionUnit.getProgressionUnitBars().get(i), queueState.getQueueItem().getBpm());
                }
                skip = !skip;
            }
        });

        return queueState;
    }
}
