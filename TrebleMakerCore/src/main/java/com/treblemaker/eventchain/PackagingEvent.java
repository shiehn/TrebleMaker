package com.treblemaker.eventchain;

import com.treblemaker.configs.AppConfigs;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.services.PackagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PackagingEvent implements IEventChain {
    @Autowired
    public AppConfigs appConfigs;

    @Value("${num_of_alt_melodies}")
    int numOfAltMelodies;

    @Override
    public QueueState set(QueueState queueState) {
        QueueItem queueItem = queueState.getQueueItem();

        PackagingService packagingService = new PackagingService(appConfigs, queueItem, numOfAltMelodies);
        packagingService.tar();

        return queueState;
    }
}
