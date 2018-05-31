package com.treblemaker.renderers.interfaces;

import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.rendertransports.HarmonicLoopRenderTransport;

public interface IHarmonicLoopRenderer {

    void renderHarmonicLoops(String loopsTargetPath, String loopFileName, HarmonicLoopRenderTransport harmonicLoopRenderTransport) throws Exception;

    HarmonicLoopRenderTransport extractLoopsToRender(QueueItem queueItem);
}
