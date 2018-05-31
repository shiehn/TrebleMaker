package com.treblemaker.renderers.interfaces;

import com.treblemaker.model.AmbienceLoop;
import com.treblemaker.model.queues.QueueItem;

public interface IAmbienceLoopRenderer {

    QueueItem renderAmbienceLoops(String ambienceLoopsTargetPath, String ambienceLoopFileName, QueueItem queueItem) throws Exception;

    String createShimNameForAmbienceLoops(AmbienceLoop ambienceLoop);
}
