package com.treblemaker.mixer.interfaces;

public interface IAudioMixer {

    void createMixes(String audioPartMelodic, String audioPartHi, String audioPartAltHi, String audioPartMid, String audioPartAltMid, String audioPartLow, String audioPartAltLow, String audioPartRhythm, String audioPartRhythmAlt, String audioPartAmbience, String audioPartHarmonic, String audioPartHarmonicAlt, String audioPartHits, String audioPartFills, String audioPartKick, String audioPartSnare, String audioPartHat, int index, String targetPath) throws Exception;

    boolean changeVolume(double newGain, String inputPath, String outputPath);
}


