package com.treblemaker.selectors;

import com.treblemaker.options.OptionsFilter;
import com.treblemaker.options.interfaces.IHarmonicLoopOptions;
import com.treblemaker.selectors.helper.LoopSelectorHelper;
import com.treblemaker.selectors.interfaces.IHarmonicLoopSelector;
import com.treblemaker.utils.LoopUtils;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.HiveChord;
import com.treblemaker.weighters.enums.WeightClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class HarmonicLoopSelector implements IHarmonicLoopSelector {

    @Autowired
    private IHarmonicLoopOptions harmonicLoopOptions;

    @Autowired
    private OptionsFilter optionsFilter;

    @Override
    public HarmonicLoop selectLoopFromChord(HiveChord currentChord, List<HarmonicLoop> harmonicLoops) {

        int maxBarCount = 2;

        return selectLoopFromChord(currentChord, harmonicLoops, maxBarCount);
    }

    @Override
    public HarmonicLoop selectLoopFromChord(HiveChord currentChord, List<HarmonicLoop> harmonicLoops, int maxBarCount) {

        List<HarmonicLoop> loops = LoopSelectorHelper.filterChordCompatibleHarmonicLoops(harmonicLoops, currentChord, 999);
        return loops.get(new Random().nextInt(loops.size()));
    }


    @Override
    public HarmonicLoop selectLoopWithChordFromLoops(HiveChord currentChord, List<HarmonicLoop> harmonicLoops, int maxBarCount) {

        List<HarmonicLoop> chordCompatibleLoops = LoopSelectorHelper.filterChordCompatibleHarmonicLoops(harmonicLoops, currentChord, 999);

        HarmonicLoop selectedLoop = LoopSelectorHelper.makeWeightedSelectionFromHarmonicLoops(chordCompatibleLoops, optionsFilter, null);

        return selectedLoop;
    }

    @Override
    public List<HarmonicLoop> selectLoopsFromChords(List<HiveChord> chordCandidates, HiveChord bestChord, List<HarmonicLoop> harmonicLoops, int bpm, int maxBarCount) {

        //middle chord can be up to 2 bars in length ..
        //TODO BPM need to be passed
        int maxBeatsInLoop = maxBarCount * 4;
        float barLengthInSec = LoopUtils.getBeatsInSeconds(bpm, maxBeatsInLoop);

        //get primary candidates ..
        List<HarmonicLoop> primaryCandiatesUnfiltered = getPrimaryCandidatesForMiddleChord(bestChord, harmonicLoops, barLengthInSec);

        List<HarmonicLoop> primaryCandiates = new ArrayList<>();
        if (primaryCandiatesUnfiltered != null) {
            primaryCandiatesUnfiltered.forEach(pc -> {
                if (pc.getBarCount() <= maxBarCount) {
                    primaryCandiates.add(pc);
                }
            });
        }

        //get secondary candidates ..
        List<HarmonicLoop> secondaryCandiatesUnfiltered = getSecondaryCandidatesForMiddleChord(chordCandidates, harmonicLoops, barLengthInSec);

        List<HarmonicLoop> secondaryCandiates = new ArrayList<>();
        if (secondaryCandiatesUnfiltered != null) {
            secondaryCandiatesUnfiltered.forEach(sc -> {
                if (sc.getBarCount() <= maxBarCount) {
                    secondaryCandiates.add(sc);
                }
            });
        }

        if ((primaryCandiates == null || primaryCandiates.isEmpty()) && (secondaryCandiates == null || secondaryCandiates.isEmpty())) {
            return null;
        }

        List<HarmonicLoop> harmonicWeightedLoops = new ArrayList<>();

        //TODO REDUE THIS AFTER ADDING WEIGHTING SYSTEM ..
        if (primaryCandiates != null && !primaryCandiates.isEmpty()) {

            for (HarmonicLoop loop : primaryCandiates) {
                loop.setHarmonicWeight(WeightClass.GOOD);
                harmonicWeightedLoops.add(loop);
            }
        }

        if (secondaryCandiates != null && !secondaryCandiates.isEmpty()) {

            for (HarmonicLoop loop : primaryCandiates) {
                loop.setHarmonicWeight(WeightClass.OK);
                harmonicWeightedLoops.add(loop);
            }
        }

        return harmonicWeightedLoops;
    }

    public List<HarmonicLoop> getPrimaryCandidatesForMiddleChord(HiveChord bestChord, List<HarmonicLoop> harmonicLoops, float maxLength) {

        List<HarmonicLoop> primaryCandiates = new ArrayList<>();

        for (HarmonicLoop loop : harmonicLoops) {

            if (bestChord.hasMatchingRoot(loop)) {
                //TODO ADD A WEIGHT SYSTEM !!!!!!!!!!!!
                if (loop.getAudioLength() <= maxLength) {
                    primaryCandiates.add(loop);
                }
            }
        }

        if (primaryCandiates == null || primaryCandiates.size() == 0) {
            return null;
        }

        return primaryCandiates;
    }

    public List<HarmonicLoop> getSecondaryCandidatesForMiddleChord(List<HiveChord> chordCandidates, List<HarmonicLoop> harmonicLoops, float maxLength) {
        List<HarmonicLoop> secondaryCandiates = new ArrayList<>();

        for (HiveChord chord : chordCandidates) {
            for (HarmonicLoop loop : harmonicLoops) {

                //allow for duplicates ..
                if (chord.hasMatchingRoot(loop)) {
                    //TODO ADD A WEIGHT SYSTEM !!!!!!!!!!!!
                    if (loop.getAudioLength() <= maxLength) {
                        secondaryCandiates.add(loop);
                    }
                }
            }
        }

        if (secondaryCandiates == null || secondaryCandiates.size() == 0) {
            return null;
        }

        return secondaryCandiates;
    }
}
