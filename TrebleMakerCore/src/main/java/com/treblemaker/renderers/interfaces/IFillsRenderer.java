package com.treblemaker.renderers.interfaces;

import com.treblemaker.model.hitsandfills.Fill;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;

import java.io.File;
import java.util.List;

public interface IFillsRenderer {

    File createFillShim(QueueState queueState);

    void render(QueueState queueState) throws Exception;

    List<Fill> createFillsToRender(List<ProgressionUnit> progressionUnits);
}
