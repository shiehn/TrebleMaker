package com.treblemaker.scheduledevents.interfaces;

import com.treblemaker.model.queues.QueueItem;

public interface IQueueDigester {

	QueueItem digest(QueueItem queueItem) throws Exception;
}
