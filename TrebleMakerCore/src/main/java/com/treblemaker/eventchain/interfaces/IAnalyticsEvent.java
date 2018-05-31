package com.treblemaker.eventchain.interfaces;


import com.treblemaker.model.queues.QueueState;

public interface IAnalyticsEvent {

    QueueState initAnalytics(QueueState queueState);

    Integer saveAnalytics(QueueState queueState);
}
