package com.treblemaker.helpers;

import com.treblemaker.configs.*;
import com.treblemaker.utils.LoopUtils;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.*;
import scala.App;

/**
 * Created by Steve on 2015-09-10.
 */
public class ShimHelper {

    static String baseShimFile = "base_shim.wav";
    static String baseShimPath = "Loops\\AmbienceLoops\\";

    public static AmbienceLoop getShimAsAmbienceLoop(){

        AmbienceLoop ambienceLoop = new AmbienceLoop();
        ambienceLoop.setAudioLength(2f);
        ambienceLoop.setFileName(baseShimFile);
        ambienceLoop.setFilePath(baseShimPath);

        return ambienceLoop;
    }

    public static BeatLoop getShimAsBeatLoop(int bpm, AppConfigs appConfigs){

        //TODO WHY 2 BARS .. shouldn't this be configurable
        String beatLoopShimFile = String.format(appConfigs.BEAT_SHIM_FILE_NAME, bpm);
        BeatLoop beatLoop = new BeatLoop();
        beatLoop.setAudioLength(LoopUtils.getSecondsInBar(bpm)*2);
        beatLoop.setBarCount(2);
        beatLoop.setFileName(beatLoopShimFile);
        beatLoop.setFilePath(String.format(appConfigs.getBeatShimsDir()));

        return beatLoop;
    }

    public static HarmonicLoop getShimAsHarmonicLoop(int bpm, AppConfigs appConfigs){

        //TODO WHY 2 BARS .. shouldn't this be configurable
        String beatLoopShimFile = String.format(appConfigs.HARMONIC_SHIM_FILE_NAME, bpm);
        HarmonicLoop harmonicLoop = new HarmonicLoop();
        harmonicLoop.setAudioLength(LoopUtils.getSecondsInBar(bpm) * 2);
        harmonicLoop.setBarCount(2);
        harmonicLoop.setFileName(beatLoopShimFile);
        harmonicLoop.setFilePath(String.format(appConfigs.getHarmonicShimsDir()));

        return harmonicLoop;
    }
}
