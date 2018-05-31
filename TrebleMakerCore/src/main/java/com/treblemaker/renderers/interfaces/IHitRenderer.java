package com.treblemaker.renderers.interfaces;

import com.treblemaker.model.hitsandfills.Hit;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;

import java.io.File;
import java.util.List;

public interface IHitRenderer {

    void render(QueueState queueState) throws Exception;

    List<Hit> createHitsToRender(List<ProgressionUnit> progressionUnits);

    File createHitShim(QueueState queueState);
}
