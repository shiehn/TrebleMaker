package com.treblemaker.adapters;

import com.treblemaker.Application;
import com.treblemaker.adapters.interfaces.IChordAdapter;
import com.treblemaker.model.HiveChord;
import org.jfugue.theory.Chord;
import org.springframework.stereotype.Component;

@Component
public class ChordAdapter implements IChordAdapter {

	public Chord DbChordToJFugueChord(HiveChord dbChord){

		//TODO SHOULD NOT HAVE TO DO THIS!!!
		//THE DATABASE SHOULD ON HAVE UNDERSCORES ..

		//dbChord = dbChord.replaceAll("-", "_");

//		String[] chordArray = dbChord.split("_");
//		String chordString = "";
//
//		if(chordArray.length > 2){
//			for(int i=0; i<3; i++){
//				chordString += chordArray[i];
//			}
//		}

        Application.logger.debug("LOG: CHORD NAME PRE : " + dbChord);
        Application.logger.debug("LOG: CHORD NAME : " + dbChord.getChordName());

		String chordName = dbChord.getChordName().replaceAll("_", "");

		Chord jfugueChord = new Chord(chordName);

		Application.logger.debug("LOG: J-CHORD NAME :" + jfugueChord.toString());

		return jfugueChord;
	}
}
