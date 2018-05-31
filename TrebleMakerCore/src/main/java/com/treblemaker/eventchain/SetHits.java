package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.generators.interfaces.IHitGenerator;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.selectors.interfaces.IHitSelector;
import com.treblemaker.weighters.hits.IHitWeighter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetHits implements IEventChain {

    @Autowired
    private IHitGenerator hitGenerator;

    @Autowired
    private IHitWeighter hitWeighter;

    @Autowired
    private IHitSelector hitSelector;

    @Override
    public QueueState set(QueueState queueState) {

        //set hit options
        queueState = hitGenerator.setHitOptions(queueState);

        //weight hit options
        queueState = hitWeighter.setHitWeights(queueState);

        //select hit options
        queueState = hitSelector.selectHits(queueState);

        return queueState;
    }
}
