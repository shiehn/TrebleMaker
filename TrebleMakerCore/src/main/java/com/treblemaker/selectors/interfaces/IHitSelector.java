package com.treblemaker.selectors.interfaces;

import com.treblemaker.model.queues.QueueState;

public interface IHitSelector {

    QueueState selectHits(QueueState queueState);
}
