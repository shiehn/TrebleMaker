package com.treblemaker.selectors.interfaces;

import com.treblemaker.model.queues.QueueState;

public interface IFillSelector {

    QueueState selectFills(QueueState queueState);
}
