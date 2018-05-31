package com.treblemaker.utils.loopcorrection;

import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.generators.interfaces.IShimGenerator;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.interfaces.IRhythmicLoop;
import com.treblemaker.utils.LoopUtils;
import com.treblemaker.utils.interfaces.IAudioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

@Component
public class LoopCorrectionBase {

    @Autowired
    @Qualifier(value = "shimGenerator")
    protected IShimGenerator shimGenerator;

    @Autowired
    private IAudioUtils audioUtils;

    @Autowired
    public AppConfigs appConfigs;

    public static void copyFile(File sourceFile, File targetFile) throws IOException {

        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }

        try (
                FileChannel in = new FileInputStream(sourceFile).getChannel();
                FileChannel out = new FileOutputStream(targetFile).getChannel()) {

            out.transferFrom(in, 0, in.size());
        }
    }

    public void correctLoopLength(String sourceFilePath, String targetFilePath, IRhythmicLoop iRhythmicLoop) throws Exception {

        float loopLength = audioUtils.getAudioLength(sourceFilePath);

        float secondsInBar = LoopUtils.getSecondsInBar(iRhythmicLoop.getBpm());

        //THRESHHOLD IS CURRENTLY SET TO 25%
        float bleedThreshold = secondsInBar / 4;

        int numOfBarsInLoop = (int) Math.floor(loopLength / secondsInBar);

        boolean pastThreshold = LoopUtils.isPastBleedThreshold(loopLength, secondsInBar, bleedThreshold);

        int targetBarCount = LoopUtils.getTargetBarCount(numOfBarsInLoop, pastThreshold);

        float targetLength = targetBarCount * secondsInBar;

        Application.logger.debug("LOG: ************* " + iRhythmicLoop.getFilePath(appConfigs) + " *******************");
        Application.logger.debug("LOG: ************* " + iRhythmicLoop.getFilePath(appConfigs) + " *******************");
        Application.logger.debug("LOG: ************* " + iRhythmicLoop.getFilePath(appConfigs) + " *******************");

        if (targetLength < loopLength) {
            //TRIM THE FUCKER ..
            audioUtils.trimAudio(sourceFilePath, targetFilePath, 0, targetLength);
        } else {
            //APPEND THE SHIM ..
            audioUtils.concatenateAudioShim(sourceFilePath, targetFilePath, loopLength, targetLength);
        }

        //update loop in database ..
        iRhythmicLoop.setAudioLength(targetLength);
        iRhythmicLoop.setBarCount(targetBarCount);
        iRhythmicLoop.setNormalizedLength(BeatLoop.ALREADY_NORMALIZED);
    }
}
