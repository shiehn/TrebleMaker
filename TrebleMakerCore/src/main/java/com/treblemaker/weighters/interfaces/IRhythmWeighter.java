package com.treblemaker.weighters.interfaces;

import com.treblemaker.weighters.enums.WeightClass;
import com.treblemaker.weighters.models.NormalizedAccents;
import com.treblemaker.model.interfaces.IRhythmicLoop;

import java.util.List;

public interface IRhythmWeighter {

    int ACCENT_MATCH_WEIGHT = 2;
    int ACCENT_NON_MATCH_NO_NEIGHBOUR = 1;
    int ACCENT_NON_MATCH_NEIGHBOUR_MATCHING = 0;
    int ACCENT_NON_MATCH_NEIGHBOUR_NON_MATCHING = -1;

    //NON MATCHES ...
    int weightNonMatchNoNeighbours(List<String> referenceAccents, List<String> accents);
    //   0000 0000 0000 0000
    //   0000 0010 0000 1000
    //static int ACCENT_NON_MATCH_NO_NEIGHBOUR = 1;

    int weightNonMatchNonMatchingNeighour(List<String> referenceAccents, List<String> accents);
    //   0000 0001 0000 0100
    //   0000 0010 0000 1000
    //static int ACCENT_NON_MATCH_NEIGHBOUR_NON_MATCHING = -1;

    int weightNonMatchWithMatchingNeighour(List<String> referenceAccents, List<String> accents);
    //   0000 0001 0000 0100
    //   0000 0011 0000 1100
    //static int ACCENT_NON_MATCH_NEIGHBOUR_MATCHING = 0;


    int weightMatchNoNeighbours(List<String> referenceAccents, List<String> accents);
    //   0000 0010 0000 1000
    //   0000 0010 0000 1000
    //static int ACCENT_MATCH_WEIGHT = 2;

    //START HERE here
    int weightMatchNonMatchingNeighour(List<String> referenceAccents, List<String> accents);
    //   0000 0011 0000 1100
    //   0000 0010 0000 1000
    //static int ACCENT_MATCH_WEIGHT = 2;

    int weightMatchWithMatchingNeighour(List<String> referenceAccents, List<String> accents);
    //   0000 0011 0000 1100
    //   0000 0011 0000 1100
    //static int ACCENT_MATCH_WEIGHT = 2;

    WeightClass calculateRhythmicWeight(IRhythmicLoop referenceLoop, IRhythmicLoop loop);

    NormalizedAccents normalizeAccentLengths(IRhythmicLoop referenceLoop, IRhythmicLoop loop);
}
