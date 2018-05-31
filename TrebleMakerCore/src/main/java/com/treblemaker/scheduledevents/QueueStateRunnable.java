package com.treblemaker.scheduledevents;

import com.treblemaker.scheduledevents.interfaces.IQueueStateListener;

/**
 * Created by Steve on 2015-08-07.
 */
public class QueueStateRunnable implements Runnable {

    protected IQueueStateListener queueStateCallback;

    public QueueStateRunnable(IQueueStateListener queueStateCallback){
        this.queueStateCallback = queueStateCallback;
    }

    @Override
    public void run() {

    }
}
