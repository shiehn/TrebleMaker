package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.generators.interfaces.IHarmonicLoopGenerator;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.types.Composition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetHarmonicLoopsAltEvent implements IEventChain {

    @Autowired
    private IHarmonicLoopGenerator harmonicLoopAltGenerator;

    @Override
    public QueueState set(QueueState queueState ){

        queueState = harmonicLoopAltGenerator.setHarmonicLoops(queueState, Composition.Layer.HARMONIC_LOOP);

        return queueState;
    }
}