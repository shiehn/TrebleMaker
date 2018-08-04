package com.treblemaker.renderers.helper;

import com.treblemaker.model.progressions.ProgressionUnit;
import org.jfugue.pattern.Pattern;

import java.util.ArrayList;
import java.util.List;

public class PatternConsolidationUtil {

    public static List<Pattern> consolidateHiPatterns(List<ProgressionUnit> progressionUnits, Integer bpm) {

        List<Pattern> consolidatedPatterns = new ArrayList<>();

        for (int t = 0; t < progressionUnits.get(0).getProgressionUnitBars().get(0).getSynthTemplates().size(); t++) {
            Pattern consolidatedPattern = new Pattern();

            for (int k = 0; k < progressionUnits.size(); k++) {

                boolean skip = false;

                for (int i = 0; i < progressionUnits.get(k).getProgressionUnitBars().size(); i++) {

                    if (!skip) {
                        consolidatedPattern.add(progressionUnits.get(k).getProgressionUnitBars().get(i).getPatternHi().get(t).setTempo(bpm));
                    }

                    skip = !skip;
                }
            }

            consolidatedPatterns.add(consolidatedPattern);
        }

        return consolidatedPatterns;
    }

    public static List<Pattern> consolidateHiAltPatterns(List<ProgressionUnit> progressionUnits, Integer bpm) {

        List<Pattern> consolidatedPatterns = new ArrayList<>();

        for (int t = 0; t < progressionUnits.get(0).getProgressionUnitBars().get(0).getSynthTemplates().size(); t++) {
            Pattern consolidatedPattern = new Pattern();

            for (int k = 0; k < progressionUnits.size(); k++) {

                boolean skip = false;

                for (int i = 0; i < progressionUnits.get(k).getProgressionUnitBars().size(); i++) {

                    if (!skip) {
                        consolidatedPattern.add(progressionUnits.get(k).getProgressionUnitBars().get(i).getPatternHiAlt().get(t).setTempo(bpm));
                    }

                    skip = !skip;
                }
            }

            consolidatedPatterns.add(consolidatedPattern);
        }

        return consolidatedPatterns;
    }

    public static List<Pattern> consolidateMidPatterns(List<ProgressionUnit> progressionUnits, Integer bpm) {

        List<Pattern> consolidatedPatterns = new ArrayList<>();

        for (int t = 0; t < progressionUnits.get(0).getProgressionUnitBars().get(0).getSynthTemplates().size(); t++) {
            Pattern consolidatedPattern = new Pattern();

            for (int k = 0; k < progressionUnits.size(); k++) {

                boolean skip = false;

                for (int i = 0; i < progressionUnits.get(k).getProgressionUnitBars().size(); i++) {

                    if (!skip) {
                        consolidatedPattern.add(progressionUnits.get(k).getProgressionUnitBars().get(i).getPatternMid().get(t).setTempo(bpm));
                    }

                    skip = !skip;
                }
            }

            consolidatedPatterns.add(consolidatedPattern);
        }

        return consolidatedPatterns;
    }

    public static List<Pattern> consolidateMidAltPatterns(List<ProgressionUnit> progressionUnits, Integer bpm) {

        List<Pattern> consolidatedPatterns = new ArrayList<>();

        for (int t = 0; t < progressionUnits.get(0).getProgressionUnitBars().get(0).getSynthTemplates().size(); t++) {
            Pattern consolidatedPattern = new Pattern();

            for (int k = 0; k < progressionUnits.size(); k++) {

                boolean skip = false;

                for (int i = 0; i < progressionUnits.get(k).getProgressionUnitBars().size(); i++) {

                    if (!skip) {
                        consolidatedPattern.add(progressionUnits.get(k).getProgressionUnitBars().get(i).getPatternMidAlt().get(t).setTempo(bpm));
                    }

                    skip = !skip;
                }
            }

            consolidatedPatterns.add(consolidatedPattern);
        }

        return consolidatedPatterns;
    }

    public static List<Pattern> consolidateLowPatterns(List<ProgressionUnit> progressionUnits, Integer bpm) {

        List<Pattern> consolidatedPatterns = new ArrayList<>();

        for (int t = 0; t < progressionUnits.get(0).getProgressionUnitBars().get(0).getSynthTemplates().size(); t++) {
            Pattern consolidatedPattern = new Pattern();

            for (int k = 0; k < progressionUnits.size(); k++) {

                boolean skip = false;

                for (int i = 0; i < progressionUnits.get(k).getProgressionUnitBars().size(); i++) {
                    if (!skip) {
                        consolidatedPattern.add(progressionUnits.get(k).getProgressionUnitBars().get(i).getPatternLow().get(t).setTempo(bpm));
                    }

                    skip = !skip;
                }
            }

            consolidatedPatterns.add(consolidatedPattern);
        }

        return consolidatedPatterns;
    }

    public static List<Pattern> consolidateLowAltPatterns(List<ProgressionUnit> progressionUnits, Integer bpm) {

        List<Pattern> consolidatedPatterns = new ArrayList<>();

        for (int t = 0; t < progressionUnits.get(0).getProgressionUnitBars().get(0).getSynthTemplates().size(); t++) {
            Pattern consolidatedPattern = new Pattern();

            for (int k = 0; k < progressionUnits.size(); k++) {

                boolean skip = false;

                for (int i = 0; i < progressionUnits.get(k).getProgressionUnitBars().size(); i++) {
                    if (!skip) {
                        consolidatedPattern.add(progressionUnits.get(k).getProgressionUnitBars().get(i).getPatternLowAlt().get(t).setTempo(bpm));
                    }

                    skip = !skip;
                }
            }

            consolidatedPatterns.add(consolidatedPattern);
        }

        return consolidatedPatterns;
    }

    public static List<Pattern> consolidateKickMidiPatterns(List<ProgressionUnit> progressionUnits, Integer bpm) {
        List<Pattern> consolidatedPatterns = new ArrayList<>();

            Pattern consolidatedPattern = new Pattern();

            for (int k = 0; k < progressionUnits.size(); k++) {
                for (int i = 0; i < progressionUnits.get(k).getProgressionUnitBars().size(); i++) {
                    Pattern pattern = progressionUnits.get(k).getProgressionUnitBars().get(i).getKickMidiPattern();
                    if(i==0){
                        pattern.setTempo(bpm);
                    }

                    consolidatedPattern.add(pattern);
                }
            }

            consolidatedPatterns.add(consolidatedPattern);

        return consolidatedPatterns;
    }

    public static List<Pattern> consolidateSnareMidiPatterns(List<ProgressionUnit> progressionUnits, Integer bpm) {
        List<Pattern> consolidatedPatterns = new ArrayList<>();

        Pattern consolidatedPattern = new Pattern();

        for (int k = 0; k < progressionUnits.size(); k++) {
            for (int i = 0; i < progressionUnits.get(k).getProgressionUnitBars().size(); i++) {
                Pattern pattern = progressionUnits.get(k).getProgressionUnitBars().get(i).getSnareMidiPattern();
                if(i==0){
                    pattern.setTempo(bpm);
                }

                consolidatedPattern.add(pattern);
            }
        }

        consolidatedPatterns.add(consolidatedPattern);

        return consolidatedPatterns;
    }

    public static List<Pattern> consolidateHatMidiPatterns(List<ProgressionUnit> progressionUnits, Integer bpm) {
        List<Pattern> consolidatedPatterns = new ArrayList<>();

        Pattern consolidatedPattern = new Pattern();

        for (int k = 0; k < progressionUnits.size(); k++) {
            for (int i = 0; i < progressionUnits.get(k).getProgressionUnitBars().size(); i++) {
                Pattern pattern = progressionUnits.get(k).getProgressionUnitBars().get(i).getHatMidiPattern();
                if(i==0){
                    pattern.setTempo(bpm);
                }

                consolidatedPattern.add(pattern);
            }
        }

        consolidatedPatterns.add(consolidatedPattern);

        return consolidatedPatterns;
    }
}
