package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.queues.QueueState;
import org.springframework.stereotype.Component;

@Component
public class SetKeyEvent implements IEventChain {

    @Override
    public QueueState set(QueueState queueState) {

        return lakjsdflk

        //return ambienceGenerator.generateAndSetAmbienceLoops(queueState, Composition.Layer.AMBIENCE_LOOP);
    }
}
