package com.treblemaker.renderers.interfaces;

import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.rendertransports.BeatLoopRenderTransport;

import java.util.List;

public interface IBeatLoopRenderer {

    void renderRhythm(String queueItemId, String rhythmTargetPath, String rhythmFileName, BeatLoopRenderTransport beatLoopRenderTransport);

    BeatLoopRenderTransport extractLoopsToRender(QueueItem queueItem);

    void concatenateAndFinalizeRendering(String queueItemId, String targetPath, String fileName, List<BeatLoop> beatLoops);
}
