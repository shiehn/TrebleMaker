package com.treblemaker.fx;

import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;

import java.util.ArrayList;
import java.util.List;

public class FXHelper {

    /*
        bpm/60000 = quaterNoteDelay
        bpm/60000/2 = eighthNoteDelay
    */

    private static int MS_IN_MIN = 60000;

    public static int getSixteenNoteInMs(int bpm) {

        return MS_IN_MIN / bpm / 4;
    }

    public static int getEigthNoteInMs(int bpm) {

        return MS_IN_MIN / bpm / 2;
    }

    public static int getQuarterNoteInMs(int bpm) {

        return MS_IN_MIN / bpm;
    }

    public static int getHalfNoteInMs(int bpm) {

        return MS_IN_MIN / bpm * 2;
    }

    public static int getEighthTripletsInMs(int bpm) {

        return MS_IN_MIN / bpm / 3;
    }

    public static int getTripletsInMs(int bpm) {

        return MS_IN_MIN / bpm * 2 / 3;
    }

    public static List<List<Integer>> extractHiSynthIds(List<ProgressionUnit> progressionUnits) {

        List<List<Integer>> hiSynthIds = new ArrayList<>();
        for(int i=0; i<progressionUnits.get(0).getProgressionUnitBars().get(0).getSynthTemplates().size(); i++){
            hiSynthIds.add(new ArrayList<>());
        }

        for (ProgressionUnit progressionUnit : progressionUnits) {
            for (ProgressionUnitBar progressionUnitBar : progressionUnit.getProgressionUnitBars()) {

                for (int i = 0; i < progressionUnitBar.getSynthTemplates().size(); i++) {
                    if (hiSynthIds.get(i).size() < 2) {
                        if (!hiSynthIds.get(i).contains(progressionUnitBar.getHiSynthId().get(i))) {
                            hiSynthIds.get(i).add(progressionUnitBar.getHiSynthId().get(i));
                        }
                    }
                }
            }
        }

        return hiSynthIds;
    }
}
