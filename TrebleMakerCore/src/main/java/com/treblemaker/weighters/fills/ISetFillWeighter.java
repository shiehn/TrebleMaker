package com.treblemaker.weighters.fills;

import com.treblemaker.model.queues.QueueState;

public interface ISetFillWeighter {

    QueueState setFillWeights(QueueState queueState);
}
