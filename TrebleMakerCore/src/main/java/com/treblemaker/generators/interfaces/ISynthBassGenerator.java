package com.treblemaker.generators.interfaces;

import com.treblemaker.model.bassline.Bassline;
import com.treblemaker.model.bassline.BasslineWithRating;
import com.treblemaker.model.bassline.Intervals;
import com.treblemaker.model.progressions.ProgressionUnit;

import java.util.HashMap;
import java.util.List;

public interface ISynthBassGenerator {

    void generateSynthBassOptions(ProgressionUnit progressionUnit, List<Bassline> basslines, HashMap<Integer, List<BasslineWithRating>> basslineMap);

    void setSynthBassOptions(ProgressionUnit progressionUnit, List<Bassline> basslines, HashMap<Integer, List<BasslineWithRating>> basslineMap, Integer bpm);

    List<BasslineWithRating> distributeByWeights(List<BasslineWithRating> basslineWithRatings);

    List<String> convertToNoteMap(int[] arpeggioList, Intervals intervalsA, Intervals intervalsB);

    String transposeChord(String originalChord, int bassOctave);
}
