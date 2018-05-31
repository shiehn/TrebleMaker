package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.generators.interfaces.IChordStructureGenerator;
import com.treblemaker.model.queues.QueueState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetChordStructureEvent implements IEventChain {

	@Autowired
	private IChordStructureGenerator chordStructureGenerator;

	@Override
	public QueueState set(QueueState queueState) {

		return chordStructureGenerator.set(queueState);
	}
}
