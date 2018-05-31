package com.treblemaker.renderers.interfaces;

import com.treblemaker.model.SynthTemplate;

import java.io.IOException;
import java.io.InputStream;

public interface IAudioRender {

    enum Spectrum {
        MELODIC, HI, ALT_HI, MID, ALT_MID, LOW, ALT_LOW
    }

    String output(InputStream inputStream) throws IOException;

    void renderPart(String midiFilePath, String audioTargetPath, IAudioRender.Spectrum spectrum, SynthTemplate template, Integer bpm);
}
