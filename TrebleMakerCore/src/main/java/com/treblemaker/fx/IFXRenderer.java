package com.treblemaker.fx;

import com.treblemaker.model.fx.FXArpeggioDelay;

public interface IFXRenderer {

    String createFfmpegEchoCommand(FXArpeggioDelay fxArpeggioDelay, int bpm);

    void renderSfx(String sourceFile, String targetFile, FXArpeggioDelay fxArpeggioDelay, int bpm);

    void renderReverbFx(String sourceFile, String targetFile);
}
