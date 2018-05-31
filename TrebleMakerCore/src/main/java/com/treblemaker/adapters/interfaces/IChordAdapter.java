package com.treblemaker.adapters.interfaces;

import com.treblemaker.model.HiveChord;
import org.jfugue.theory.Chord;

public interface IChordAdapter {

	  Chord DbChordToJFugueChord(HiveChord dbChord);
}
