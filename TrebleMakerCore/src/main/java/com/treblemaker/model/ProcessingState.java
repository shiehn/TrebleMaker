package com.treblemaker.model;

import com.treblemaker.model.types.ProcessingPattern;

public class ProcessingState {

    private ProcessingPattern processingPattern;

    public enum ProcessingStates {
        C1_L3, C3_L1, C2_L4, C4_L2, COMPLETE, START_OVER, C1_L1, C2_C3_C4
    }

    public ProcessingState(){}

    public ProcessingState(ProcessingStates currentState){
        setCurrentState(currentState);
    }

    private ProcessingStates currentState;

    public ProcessingStates getCurrentState() {

        if(currentState == null){
            currentState = ProcessingStates.C1_L3;
        }

        return currentState;
    }

    public void setCurrentState(ProcessingStates currentState) {
        this.currentState = currentState;
    }

    public ProcessingPattern getProcessingPattern() {
        return processingPattern;
    }

    public void setProcessingPattern(ProcessingPattern processingPattern) {
        this.processingPattern = processingPattern;
    }
}
