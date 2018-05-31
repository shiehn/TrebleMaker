package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.generators.interfaces.IBeatLoopGenerator;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.types.Composition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetBeatLoopsAltEvent implements IEventChain {

    @Autowired
    private IBeatLoopGenerator beatLoopAltGenerator;

    @Override
    public QueueState set(QueueState queueState ) {

        return beatLoopAltGenerator.generateAndSetBeatLoops(queueState, Composition.Layer.BEAT_LOOP_ALT);
    }
}