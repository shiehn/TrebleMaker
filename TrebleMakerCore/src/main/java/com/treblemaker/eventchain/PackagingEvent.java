package com.treblemaker.eventchain;

import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.services.PackagingService;
import com.treblemaker.utils.TAR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Component
public class PackagingEvent implements IEventChain {

    @Autowired
    public AppConfigs appConfigs;

    private PackagingService packagingService;

    @Override
    public QueueState set(QueueState queueState) {
        QueueItem queueItem = queueState.getQueueItem();

        if(packagingService == null){
            packagingService = new PackagingService(appConfigs, queueItem);
        }

        packagingService.tar();

        return queueState;
    }
}
