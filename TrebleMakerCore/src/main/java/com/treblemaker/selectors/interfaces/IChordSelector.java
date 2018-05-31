package com.treblemaker.selectors.interfaces;

import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.progressions.ProgressionUnit;

import java.util.HashMap;
import java.util.List;

public interface IChordSelector {

    HashMap<ProgressionUnit.ProgressionType, HiveChord> selectFirstChordForType(List<ProgressionUnit.ProgressionType> progressionTypes, List<HiveChord> hiveChordInDatabase);

    HashMap<ProgressionUnit.ProgressionType, List<HiveChord>> selectChordsFrom4BarLoop(List<ProgressionUnit> progressionUnits);

    HiveChord selectMiddleChord(HarmonicLoop selectedLoop, HiveChord chordTwoBarsPrior, HiveChord chordTwoBarsForward, List<HiveChord> hiveChordInDatabase);

    HiveChord selectChordBetweenChords(HiveChord chordOne, HiveChord chordTwo, List<HiveChord> hiveChordInDatabase);

    HiveChord selectChordBetweenChordsAndLoop(HiveChord chordOne, HiveChord chordTwo, HarmonicLoop harmonicLoop, List<HiveChord> hiveChordInDatabase);

    HiveChord selectFourthChord(HarmonicLoop selectedLoop, HiveChord thirdChord, HiveChord nextChord, List<HiveChord> hiveChordInDatabase);

    HiveChord getChordTwoBarsPrior(HiveChord currentChord, List<HiveChord> hiveChordInDatabase);

    List<HiveChord> getChordCandidatesTwoBarsPrior(HiveChord currentChord, List<HiveChord> hiveChordInDatabase);

    List<HiveChord> getChordCandidatesBetweenChords(HiveChord chordOne, HiveChord chordTwo, List<HiveChord> hiveChordInDatabase);

}