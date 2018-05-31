package com.treblemaker.generators.interfaces;

import com.treblemaker.model.AmbienceLoop;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.types.Composition;

import java.util.List;
import java.util.Map;

public interface IAmbienceGenerator {

    QueueState generateAndSetAmbienceLoops(QueueState queueState, Composition.Layer layerType);

    void setAmbienceLoops(Map<ProgressionUnit.ProgressionType, AmbienceLoop> ambienceLoopsByType, Composition.Layer layerType, List<ProgressionUnit> progressionUnits);

    AmbienceLoop getLoop(float maxSampleLength, ProgressionUnit progressionUnit);
}
