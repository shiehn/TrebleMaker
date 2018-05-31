package com.treblemaker.eventchain;

import com.treblemaker.adapters.interfaces.IChordAdapter;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.queues.QueueState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class JChordAppenderEvent implements IEventChain {
  
	@Autowired
	@Qualifier(value = "chordAdapter")
	private IChordAdapter chordAdapter;

	@Override
	public QueueState set(QueueState queueState ) {

		queueState.getStructure().forEach(pUnit -> {
			pUnit.getProgressionUnitBars().forEach(pBar -> {

				pBar.setjChord(chordAdapter.DbChordToJFugueChord(pBar.getChord()));
			});
		});

		return queueState;
	}

}
