package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.generators.interfaces.IHarmonicLoopGenerator;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.types.Composition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SetHarmonicLoopsEvent implements IEventChain {

	@Autowired
	@Qualifier(value = "harmonicLoopGenerator")
	private IHarmonicLoopGenerator harmonicLoopGenerator; 

	@Override
	public QueueState set(QueueState queueState ) {
		return harmonicLoopGenerator.setHarmonicLoops(queueState, Composition.Layer.HARMONIC_LOOP);
	}
}
