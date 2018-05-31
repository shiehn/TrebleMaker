package com.treblemaker.dal.interfaces;

import com.treblemaker.model.queues.QueueItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQueueItemsDal extends CrudRepository<QueueItem, Integer> {
}
