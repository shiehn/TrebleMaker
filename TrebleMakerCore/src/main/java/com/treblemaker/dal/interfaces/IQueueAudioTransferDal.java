package com.treblemaker.dal.interfaces;

import com.treblemaker.model.queues.QueueAudioTransfer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IQueueAudioTransferDal extends CrudRepository<QueueAudioTransfer, Integer> {

    List<QueueAudioTransfer> findAll();
}
