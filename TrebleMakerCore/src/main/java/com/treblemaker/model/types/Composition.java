package com.treblemaker.model.types;

public class Composition {

    public enum LayerGroup {

        AMIENCE_LOOPS, //ambi & ambi alt
        HARMONIC_LOOPS, //harmonic & harmonic alt
        SYNTH_HI, //synthHi
        SYNTH_MED, //synthMed
        BASS, //synth low / bass loops
        BEAT_LOOPS, //beat loops
        FILL //fill loops
    }

    public enum Layer {
        AMBIENCE_LOOP,
        AMBIENCE_LOOP_ALT,
        HARMONIC_LOOP,
        HARMONIC_LOOP_ALT,
        SYNTH_HIGH,
        SYNTH_HIGH_ALT,
        SYNTH_MID,
        SYNTH_MID_ALT,
        SYNTH_LOW,
        SYNTH_LOW_ALT,
        BASS_LOOP,
        BEAT_LOOP,
        BEAT_LOOP_ALT,
        FILL_LOOP
    }
}