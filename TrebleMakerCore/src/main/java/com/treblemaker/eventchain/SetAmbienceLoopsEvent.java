package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.generators.interfaces.IAmbienceGenerator;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.types.Composition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SetAmbienceLoopsEvent implements IEventChain {

	@Autowired
	@Qualifier(value = "ambienceGenerator")
	private IAmbienceGenerator ambienceGenerator;

	@Override
	public QueueState set(QueueState queueState ) {
		return ambienceGenerator.generateAndSetAmbienceLoops(queueState, Composition.Layer.AMBIENCE_LOOP);
	}
}
