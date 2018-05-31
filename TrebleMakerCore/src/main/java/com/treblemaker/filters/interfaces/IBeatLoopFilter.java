package com.treblemaker.filters.interfaces;

import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.HiveChord;

import java.util.List;

public interface IBeatLoopFilter {

    List<BeatLoop> filterByChords(List<BeatLoop> beatOptions, List<HiveChord> chords);
}
