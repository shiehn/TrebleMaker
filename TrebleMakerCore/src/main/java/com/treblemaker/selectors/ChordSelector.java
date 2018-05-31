package com.treblemaker.selectors;

import com.treblemaker.adapters.interfaces.IChordAdapter;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.selectors.interfaces.IChordSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.treblemaker.configs.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.treblemaker.model.progressions.ProgressionUnit.ProgressionType;

@Component
public class ChordSelector implements IChordSelector {

    @Autowired
    @Qualifier(value = "chordAdapter")
    private IChordAdapter chordAdapter;

    @Autowired
    private AppConfigs appConfigs;

    public HashMap<ProgressionType, HiveChord> selectFirstChordForType(List<ProgressionType> progressionTypes, List<HiveChord> hiveChordInDatabase) {

        HashMap<ProgressionType, HiveChord> typeAndChord = new HashMap<ProgressionType, HiveChord>();

        for (ProgressionType type : progressionTypes) {
            typeAndChord.put(type, tempRandomChordSelect(hiveChordInDatabase));
        }

        return typeAndChord;
    }

    @Override
    public HashMap<ProgressionType, List<HiveChord>> selectChordsFrom4BarLoop(List<ProgressionUnit> progressionUnits) {

        HashMap<ProgressionType, List<HiveChord>> typeToHiveChords = new HashMap<>();

        for (ProgressionUnit progressionUnit : progressionUnits) {
            if (typeToHiveChords.get(progressionUnit.getType()) == null) {

                HiveChord[] fourBarChordList = new HiveChord[4];

                HarmonicLoop loop = progressionUnit.getProgressionUnitBars().get(0).getHarmonicLoop();

                List<HiveChord> chordsInBar = progressionUnit.getProgressionUnitBars().stream().map(pBar -> pBar.getChord()).filter(chord -> chord != null).collect(Collectors.toList());

                List<HiveChord> compatibleChords = new ArrayList<>();

                for (HiveChord chord : chordsInBar) {
                    for (HiveChord loopChord : loop.getChords()) {
                        if (chord.isEqualOrTriadMatch(loopChord)) {
                            compatibleChords.add(chord);
                        }
                    }
                }

                for (Integer i = 0; i < progressionUnit.getProgressionUnitBars().size(); i++) {
                    HiveChord currentChord = progressionUnit.getProgressionUnitBars().get(i).getChord();

                    if (i.equals(0) || compatibleChords.isEmpty()) {
                        fourBarChordList[i] = currentChord;
                    } else {
                        fourBarChordList[i] = compatibleChords.get(new Random().nextInt(compatibleChords.size()));
                    }
                }

                typeToHiveChords.put(progressionUnit.getType(), Arrays.asList(fourBarChordList));
            }
        }

        return typeToHiveChords;
    }

    public HiveChord tempRandomChordSelect(List<HiveChord> getHiveChordInDatabase) {

        int max = getHiveChordInDatabase.size();
        HiveChord randomChord = null;

        boolean breakLoop = false;
        while (!breakLoop) {
            int randomIndex = new Random().nextInt(max);
            randomChord = getHiveChordInDatabase.get(randomIndex);
            if (!randomChord.getRawChordName().equalsIgnoreCase("_")) {
                breakLoop = true;
            }
        }

        return randomChord;
    }

    public HiveChord getChordTwoBarsPrior(HiveChord currentChord, List<HiveChord> hiveChordInDatabase) {

        List<HiveChord> chordCandidates = getChordCandidatesTwoBarsPrior(currentChord, hiveChordInDatabase);

        int max = chordCandidates.size();
        int randomIndex = new Random().nextInt(max);

        HiveChord chordCandidate = chordCandidates.get(randomIndex);

        if (chordCandidate == null) {
            return null;
        }

        return chordCandidate;
    }

    public List<HiveChord> getChordCandidatesTwoBarsPrior(HiveChord currentChord, List<HiveChord> hiveChordInDatabase) {

        List<HiveChord> chordCandidates = new ArrayList<HiveChord>();

        for (int i = 0; i < hiveChordInDatabase.size(); i++) {

            if (i > 1 && hiveChordInDatabase.get(i).isEqual(currentChord)) {
                // has a chord match make sure none of the chords are "empty slots"
                if (!hiveChordInDatabase.get(i - 2).getChordName().equalsIgnoreCase(appConfigs.CHORD_BREAK) && !hiveChordInDatabase.get(i - 1).getChordName().equalsIgnoreCase(appConfigs.CHORD_BREAK)) {
                    //now get the chord 2 bars prior ..
                    chordCandidates.add(hiveChordInDatabase.get(i - 2));
                }
            }
        }

        if (chordCandidates == null || chordCandidates.size() == 0) {
            return null;
        }

        return chordCandidates;
    }

    public HiveChord selectChordBetweenChords(HiveChord chordOne, HiveChord chordTwo, List<HiveChord> hiveChordInDatabase) {

        List<HiveChord> candidates = getChordCandidatesBetweenChords(chordOne, chordTwo, hiveChordInDatabase);

        if (candidates == null || candidates.size() == 0) {
            return null;
        }

        // TODO handle a better solution ....
        // TODO just match one way ..
        // if there is no matches resort to
        if (candidates.size() == 0) {
            // TODO GARBAGE >> PLEASE REVIST ..
            // TODO CANNOT FIND A CHORD .. CONSIDER RANDOM OR SOMETHING ..
            candidates.add(chordOne);
        }


        int max = candidates.size();
        int randomIndex = new Random().nextInt(max);

        return candidates.get(randomIndex);
    }

    @Override
    public HiveChord selectChordBetweenChordsAndLoop(HiveChord chordOne, HiveChord chordTwo, HarmonicLoop harmonicLoop, List<HiveChord> hiveChordInDatabase) {

        List<HiveChord> candidates = getChordCandidatesBetweenChords(chordOne, chordTwo, hiveChordInDatabase);

        if (candidates == null || candidates.size() == 0) {
            return null;
        }

        List<HiveChord> filteredChords = new ArrayList<>();

        candidates.forEach(candidate -> {

            if (candidate.hasMatchingRoot(harmonicLoop)) {
                filteredChords.add(candidate);
            }
        });

        if (filteredChords == null || filteredChords.isEmpty()) {
            return harmonicLoop.getValidChords().get(0);
        }

        int max = filteredChords.size();
        int randomIndex = new Random().nextInt(max);

        return filteredChords.get(randomIndex);
    }

    public List<HiveChord> getChordCandidatesBetweenChords(HiveChord chordOne, HiveChord chordTwo, List<HiveChord> hiveChordInDatabase) {

        List<HiveChord> candidates = new ArrayList<HiveChord>();
        List<HiveChord> db = hiveChordInDatabase;

        //try to find a middle cord from exact chord match ..
        for (int i = 0; i < db.size(); i++) {

            if (db.get(i).isEqual(chordOne) && ((i + 2 < db.size()) && db.get(i + 2).isEqual(chordTwo))) {

                try {
                    if (!db.get(i+1).getRawChordName().equalsIgnoreCase(appConfigs.CHORD_BREAK)) {
                        candidates.add(db.get(i + 1));
                    }
                } catch (Exception e) {
                    System.out.print("THIS IS NOT A LEGIT *J_FUGUE CHORD* : " + db.get(i).getChordName());
                }
            }
        }

        if (candidates != null && candidates.size() != 0) {
            return candidates;
        }

        //SINCE NO EXACT MATCH WAS FOUND TRY FINDING A EXACT OR MATCHING ROOT ..
        for (int i = 0; i < db.size(); i++) {

            if (db.get(i).hasMatchingRoot(chordOne) && ((i + 2 < db.size()) && db.get(i + 2).hasMatchingRoot(chordTwo))) {
                // TODO check if is legit jfuge chord ..
                if (i + 1 < db.size()) {
                    if (!db.get(i).getChordName().equalsIgnoreCase(appConfigs.CHORD_BREAK)) {
                        candidates.add(db.get(i + 1));
                    }
                }
            }
        }

        if (candidates == null || candidates.size() == 0) {
            return null;
        }

        return candidates;
    }


    @Override
    public HiveChord selectMiddleChord(HarmonicLoop selectedLoop, HiveChord chordOneOfFour, HiveChord chordOneOfNextBar, List<HiveChord> hiveChordInDatabase) {
        //TODO ADD LOGIC TO MAKE A SMART DECISION ...
        //TODO CONSIDER chordOneOfFour & chordOneOfNextBar ..

        List<HiveChord> compatibleChords = selectedLoop.getValidChords();

        if (compatibleChords == null || compatibleChords.size() == 0) {
            return null;
        }


        for (int i = 0; i < compatibleChords.size(); i++) {

            HiveChord compChord = compatibleChords.get(i);

            for (int j = 0; j < hiveChordInDatabase.size(); j++) {

                HiveChord dbChord = hiveChordInDatabase.get(j);

                if ((chordOneOfFour != null && chordOneOfNextBar != null) && (j > 0 && j < hiveChordInDatabase.size() - 1) && dbChord.isEqualOrTriadMatch(compChord) &&
                        hiveChordInDatabase.get(j - 1).isEqualOrTriadMatch(chordOneOfFour) &&
                        hiveChordInDatabase.get(j + 1).isEqualOrTriadMatch(chordOneOfNextBar)) {
                    return compChord;
                } else if ((chordOneOfFour != null) && (j > 0) && dbChord.isEqualOrTriadMatch(compChord) &&
                        hiveChordInDatabase.get(j - 1).isEqualOrTriadMatch(chordOneOfFour)) {
                    return compChord;
                } else if ((chordOneOfNextBar != null) && (j < hiveChordInDatabase.size() - 1) && dbChord.isEqualOrTriadMatch(compChord) &&
                        hiveChordInDatabase.get(j + 1).isEqualOrTriadMatch(chordOneOfNextBar)) {
                    return compChord;
                }
            }
        }

        int index = new Random().nextInt(compatibleChords.size());
        return compatibleChords.get(index);
    }

    @Override
    public HiveChord selectFourthChord(HarmonicLoop selectedLoop, HiveChord thirdChord, HiveChord nextChord, List<HiveChord> hiveChordInDatabase) {

        List<HiveChord> candidates = getChordCandidatesBetweenChords(thirdChord, nextChord, hiveChordInDatabase);

        if (candidates == null || candidates.size() == 0) {
            return null;
        }

        List<HiveChord> compatibleChords = selectedLoop.getValidChords();

        List<HiveChord> perfectMatches = new ArrayList<>();

        for (HiveChord chord : compatibleChords) {
            for (HiveChord dbChord : candidates) {
                if (chord.isEqual(dbChord)) {
                    perfectMatches.add(dbChord);
                }
            }
        }

        if (perfectMatches.size() > 0) {
            int index = new Random().nextInt(perfectMatches.size());
            return perfectMatches.get(index);
        } else {
            int index = new Random().nextInt(compatibleChords.size());
            return compatibleChords.get(index);
        }
    }
}
