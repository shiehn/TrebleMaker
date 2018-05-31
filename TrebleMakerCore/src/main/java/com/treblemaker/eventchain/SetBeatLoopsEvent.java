package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.generators.interfaces.IBeatLoopGenerator;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.types.Composition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetBeatLoopsEvent implements IEventChain {

	@Autowired
	private IBeatLoopGenerator beatLoopGenerator;

	@Override
	public QueueState set(QueueState queueState ) {
		return beatLoopGenerator.generateAndSetBeatLoops(queueState, Composition.Layer.BEAT_LOOP);
	}
}
