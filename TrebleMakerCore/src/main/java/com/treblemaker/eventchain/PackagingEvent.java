package com.treblemaker.eventchain;

import com.treblemaker.configs.AppConfigs;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.services.PackagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PackagingEvent implements IEventChain {
    @Autowired
    public AppConfigs appConfigs;

    @Override
    public QueueState set(QueueState queueState) {
        QueueItem queueItem = queueState.getQueueItem();

        PackagingService packagingService = new PackagingService(appConfigs, queueItem);
        packagingService.tar();

        return queueState;
    }
}
