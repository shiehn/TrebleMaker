package com.treblemaker.scheduledevents;

import com.treblemaker.Application;

import java.util.concurrent.atomic.AtomicBoolean;

public class GlobalState {

    private static GlobalState instance = null;

    private AtomicBoolean queueItemInprogress = null;
    private AtomicBoolean healthMonitorInProgress = null;
    private AtomicBoolean loopCorrectionInProgress = null;

    protected GlobalState(){
        queueItemInprogress = new AtomicBoolean();
        queueItemInprogress.set(false);
        healthMonitorInProgress = new AtomicBoolean();
        healthMonitorInProgress.set(false);
        loopCorrectionInProgress = new AtomicBoolean();
        loopCorrectionInProgress.set(false);
    }

    public static GlobalState getInstance(){
        if(instance == null){
            synchronized (GlobalState.class){
                if (instance == null){
                    instance = new GlobalState();
                }
            }
        }
        return instance;
    }



    public boolean isQueueItemInprogress() {
        return queueItemInprogress.get();
    }

    public void setQueueItemInprogress(boolean value) {
        this.queueItemInprogress.set(value);
    }

    public boolean isHealthMonitorInProgress(){
        return this.healthMonitorInProgress.get();
    }

    public void setHealthMonitorInProgress(boolean value) {
        this.healthMonitorInProgress.set(value);
        Application.logger.debug("LOG: ------------------- ");
        Application.logger.debug("LOG: HEALTH MONITOR ACTIVE : " + value);
        Application.logger.debug("LOG: ---------------------" );
    }

    public boolean isLoopCorrectionInProgress() {
        return loopCorrectionInProgress.get();
    }

    public void setLoopCorrectionInProgress(boolean loopCorrectionInProgress) {
        this.loopCorrectionInProgress.set(loopCorrectionInProgress);
    }
}