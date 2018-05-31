package com.treblemaker.eventchain;

import com.treblemaker.Application;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.ProcessingState;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.types.ProcessingPattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetChordStructureAndHarmonicLoops implements IEventChain {

    @Autowired
    private IEventChain setChordStructureEvent;

    @Autowired
    private IEventChain setHarmonicLoopsEvent;

    @Override
    public QueueState set(QueueState queueState) {

        int scheduleCount = 0;

        while (!queueState.getProcessingState().getCurrentState().equals(ProcessingState.ProcessingStates.COMPLETE)) {

            queueState = setChordStructureEvent.set(queueState);

            if (!queueState.shouldProceed()) {
                if(queueState.getProcessingState().getProcessingPattern().equals(ProcessingPattern.TWO_BAR)){
                    queueState.getProcessingState().setCurrentState(ProcessingState.ProcessingStates.C1_L3);
                }else if(queueState.getProcessingState().getProcessingPattern().equals(ProcessingPattern.FOUR_BAR)){
                    queueState.getProcessingState().setCurrentState(ProcessingState.ProcessingStates.C1_L1);
                }
                queueState.setProceed(true);
                scheduleCount++;
                Application.logger.debug("LOG: ATTEMPT : " + scheduleCount * 4);
            }

            queueState = setHarmonicLoopsEvent.set(queueState);

            if (!queueState.shouldProceed()) {
                if(queueState.getProcessingState().getProcessingPattern().equals(ProcessingPattern.TWO_BAR)) {
                    queueState.getProcessingState().setCurrentState(ProcessingState.ProcessingStates.C1_L3);
                }else if(queueState.getProcessingState().getProcessingPattern().equals(ProcessingPattern.FOUR_BAR)){
                    queueState.getProcessingState().setCurrentState(ProcessingState.ProcessingStates.C1_L1);
                }
                queueState.setProceed(true);
                scheduleCount++;
                Application.logger.debug("LOG: ATTEMPT : " + scheduleCount * 4);
            }
        }

        return queueState;
    }
}
