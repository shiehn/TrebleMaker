package com.treblemaker.dal.interfaces;

import com.treblemaker.model.queues.QueueItem;

import java.sql.SQLException;


public interface IQueueItemCustomDal {

	QueueItem getQueueItem() throws Exception;

	QueueItem getQueueItemById(String queueItemId) throws SQLException, ClassNotFoundException;

	QueueItem getRefactoredQueueItem(String refactoredQueueId) throws SQLException, ClassNotFoundException;

	void setQueueItemProcessing(String queueItemId) throws ClassNotFoundException;

	void setQueueItemComplete(String queueItemId, String outputPath);
}
