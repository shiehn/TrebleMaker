package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.generators.interfaces.IFillsGenerator;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.selectors.interfaces.IFillSelector;
import com.treblemaker.weighters.fills.ISetFillWeighter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetFills implements IEventChain {

    @Autowired
    private IFillsGenerator fillsGenerator;

    @Autowired
    private ISetFillWeighter fillWeighter;

    @Autowired
    private IFillSelector fillSelector;

    @Override
    public QueueState set(QueueState queueState) {

        queueState = fillsGenerator.setFillsOptions(queueState);

        queueState = fillWeighter.setFillWeights(queueState);

        queueState = fillSelector.selectFills(queueState);

        return queueState;
    }
}
