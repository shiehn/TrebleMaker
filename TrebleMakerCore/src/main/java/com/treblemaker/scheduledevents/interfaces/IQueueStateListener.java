package com.treblemaker.scheduledevents.interfaces;

import com.treblemaker.model.queues.QueueState;

public interface IQueueStateListener {
    void updateQueueState(QueueState queueState);
}
