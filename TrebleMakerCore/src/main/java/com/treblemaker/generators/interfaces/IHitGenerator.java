package com.treblemaker.generators.interfaces;

import com.treblemaker.model.queues.QueueState;

public interface IHitGenerator {

    QueueState setHitOptions(QueueState queueState);

    QueueState weightHitOptions(QueueState queueState);

    QueueState selectHits(QueueState queueState);
}
