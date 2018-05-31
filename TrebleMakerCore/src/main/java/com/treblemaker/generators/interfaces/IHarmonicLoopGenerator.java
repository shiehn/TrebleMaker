package com.treblemaker.generators.interfaces;

import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.types.Composition;

import java.util.List;

public interface IHarmonicLoopGenerator {

	QueueState setHarmonicLoops(QueueState queueState, Composition.Layer layerType );

	int getMaxBarCount(ProgressionUnit pUnit, int currentBarIndex);

	void setHarmonicLoop(HarmonicLoop harmonicLoop, int barIndex, List<ProgressionUnitBar> progressionUnitBars);
}
