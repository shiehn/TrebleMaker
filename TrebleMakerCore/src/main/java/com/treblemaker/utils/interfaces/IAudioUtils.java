package com.treblemaker.utils.interfaces;

import java.io.File;
import java.io.IOException;

public interface IAudioUtils {

    float getAudioLength(String pathToAudioFile);

    int isMonoOrStereo(String pathToAudioFile);

    void trimAudio(String sourceFileName, String destinationFileName, int startSecond, float secondsToCopy);

    void concatenateAudioShim(String sourceFile, String targetFilePath, float loopLength, float targetLength) throws Exception;

    void copyFile(File sourceFile, File destFile) throws IOException;

    double getMaxVolume(File sourceFile) throws InterruptedException;

    double getMeanVolume(File sourceFile) throws InterruptedException;

    void resampleWithFFMPEGTo16Bit44100Mono(String sourcePath, String targetPath);

    void resampleToMono16Bit(String filePath) throws InterruptedException;
}
