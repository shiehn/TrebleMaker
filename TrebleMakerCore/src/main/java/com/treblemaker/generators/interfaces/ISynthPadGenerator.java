package com.treblemaker.generators.interfaces;


import com.treblemaker.model.comp.CompRhythm;
import com.treblemaker.model.progressions.ProgressionUnit;
import org.jfugue.theory.Note;

import java.util.Map;

public interface ISynthPadGenerator {

	ProgressionUnit generateAndSetSynthPad(ProgressionUnit progressionUnit, Map<ProgressionUnit.ProgressionType, CompRhythm> selectedCompOptions);

	String selectChordNote(Note[] notes);
	
	int randomIndex();
}
