package com.treblemaker.selectors.interfaces;

import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.HiveChord;

import java.util.List;

public interface IHarmonicLoopSelector {

	HarmonicLoop selectLoopFromChord(HiveChord currentChord, List<HarmonicLoop> harmonicLoops);

	HarmonicLoop selectLoopFromChord(HiveChord currentChord, List<HarmonicLoop> harmonicLoops, int maxBarCount);

    HarmonicLoop selectLoopWithChordFromLoops(HiveChord currentChord, List<HarmonicLoop> harmonicLoops, int maxBarCount);

	//HarmonicLoop selectLoopFromChords(List<HiveChord> chordCandidates, HiveChord bestChord, List<HarmonicLoop> harmonicLoops, int bpm, int maxBarCount);

	List<HarmonicLoop> selectLoopsFromChords(List<HiveChord> chordCandidates, HiveChord bestChord, List<HarmonicLoop> harmonicLoops, int bpm, int maxBarCount);

	List<HarmonicLoop> getSecondaryCandidatesForMiddleChord(List<HiveChord> chordCandidates, List<HarmonicLoop> harmonicLoops, float maxLength);

	List<HarmonicLoop> getPrimaryCandidatesForMiddleChord(HiveChord bestChord, List<HarmonicLoop> harmonicLoops, float maxLength);

}
