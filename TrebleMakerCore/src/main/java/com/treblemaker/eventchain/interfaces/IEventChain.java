package com.treblemaker.eventchain.interfaces;

import com.treblemaker.model.queues.QueueState;

public interface IEventChain {
	QueueState set(QueueState queueState);
}
