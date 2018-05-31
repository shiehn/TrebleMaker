package com.treblemaker.generators.interfaces;

import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.types.Composition;

import java.util.List;
import java.util.Map;

public interface IBeatLoopGenerator {

    QueueState generateAndSetBeatLoops(QueueState queueState, Composition.Layer layerType);

    void setBeatLoops(Map<ProgressionUnit.ProgressionType, BeatLoop> beatLoopByType, Composition.Layer layerType, List<ProgressionUnit> progressionUnits);

    void setBeatLoop(BeatLoop beatLoop, int barIndex, List<ProgressionUnitBar> progressionUnitBars);

    int getMaxBarCount(ProgressionUnit pUnit, int currentBarIndex);
}
