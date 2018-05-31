package com.treblemaker.selectors.interfaces;

import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;

public interface ISynthTemplatePrimeOrAltSelector {

	void setHiSynths(ProgressionUnitBar progressionUnitBar);

	void setMidSynths(ProgressionUnitBar progressionUnitBar);

	void setLowSynths(ProgressionUnitBar progressionUnitBar);

	QueueState setRhythm(QueueState queueState);

	boolean randomSelection();
}
