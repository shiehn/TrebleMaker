package com.treblemaker.selectors.helper;

import com.treblemaker.Application;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.ProcessingState;
import com.treblemaker.options.OptionsFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoopSelectorHelper {

    public static List<HarmonicLoop> filterChordCompatibleHarmonicLoops(List<HarmonicLoop> harmonicLoops, HiveChord currentChord, int maxBarCount) {

        List<HarmonicLoop> chordCompatibleOptions = new ArrayList<>();

        harmonicLoops.forEach(loop -> {
            if (currentChord.hasMatchingRoot(loop)) {
                if (loop.getBarCount() <= maxBarCount) {
                    chordCompatibleOptions.add(loop);
                }
            }
        });

        return chordCompatibleOptions;
    }

    public static List<HarmonicLoop> orderHarmonicLoopsByWeight(List<HarmonicLoop> harmonicLoops) {

        int maxRating = 0;
        List<HarmonicLoop> loopsOrderedByWeight = new ArrayList<>();

        //find highest number and iterate down
        for (HarmonicLoop loop : harmonicLoops) {
            if (loop.getTotalWeight() > maxRating) {
                maxRating = loop.getTotalWeight();
            }
        }

        //order loops
        for(int i=maxRating; i>=0; i--){
            for (HarmonicLoop loop : harmonicLoops) {
                if (loop.getTotalWeight().equals(i)) {
                    loopsOrderedByWeight.add(loop);
                }
            }
        }

        return loopsOrderedByWeight;
    }

    public static List<HarmonicLoop> normalizeHarmonicLoopsByWeight(List<HarmonicLoop> weightables) {

        List<HarmonicLoop> normalizedSelection = new ArrayList<>();

        //FOR EACH LOOP ADD AND INSTANCE FOR EACH TOTAL WEIGHT
        //i.e. if the total weight is 9 add 9 instances to the list
        weightables.forEach(weightable -> {

            int instanceCount = weightable.getTotalWeight();

            for (Integer i=0; i<instanceCount; i++){
                normalizedSelection.add(weightable);
            }
        });

        return normalizedSelection;
    }

    public static HarmonicLoop makeWeightedSelectionFromHarmonicLoops(List<HarmonicLoop> harmonicLoops, OptionsFilter optionsFilter, ProcessingState processingState) {

        List<HarmonicLoop> loopSelections = normalizeHarmonicLoopsByWeight(harmonicLoops);

        if(processingState != null) {
            loopSelections = optionsFilter.filterByProcessingState(loopSelections, processingState);
        }

        if (loopSelections.isEmpty()) {
            Application.logger.debug("LOG: !! makeWeightedSelection OPTIONS ARE EMPTY !!!!");
            return null;
        }

        int index = new Random().nextInt(loopSelections.size());
        return loopSelections.get(index);
    }
}
