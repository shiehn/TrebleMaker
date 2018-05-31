package com.treblemaker.weighters.hits;

import com.treblemaker.model.queues.QueueState;

public interface IHitWeighter {

    QueueState setHitWeights(QueueState queueState);
}
