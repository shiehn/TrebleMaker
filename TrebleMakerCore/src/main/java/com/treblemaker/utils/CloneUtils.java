package com.treblemaker.utils;

import com.treblemaker.Application;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.SynthTemplateOption;

import java.util.ArrayList;
import java.util.List;

public class CloneUtils {

    public static List<HarmonicLoop> cloneHarmonicLoops(List<HarmonicLoop> harmonicLoops){

        List<HarmonicLoop> cloneLoops = new ArrayList<>();

        for(HarmonicLoop harmonicLoop : harmonicLoops){
            try {
                cloneLoops.add(harmonicLoop.clone());
            } catch (CloneNotSupportedException e) {
                Application.logger.debug("LOG:", e);
            }
        }

        return cloneLoops;
    }

    public static List<BeatLoop> cloneBeatLoops(List<BeatLoop> beatLoops){

        List<BeatLoop> cloneLoops = new ArrayList<>();

        for(BeatLoop beatLoop : beatLoops){
            try {
                cloneLoops.add(beatLoop.clone());
            } catch (CloneNotSupportedException e) {
                Application.logger.debug("LOG:", e);
            }
        }

        return cloneLoops;
    }

    public static List<SynthTemplateOption> cloneSynthTemplateOptions(List<SynthTemplateOption> synthTemplates){

        List<SynthTemplateOption> cloneLoops = new ArrayList<>();

        for(SynthTemplateOption synthTemplate : synthTemplates){
            try {
                cloneLoops.add(synthTemplate.clone());
            } catch (CloneNotSupportedException e) {
                Application.logger.debug("LOG:", e);
            }
        }

        return cloneLoops;
    }
}
