package com.treblemaker.generators;

import com.treblemaker.generators.interfaces.ISynthPadGenerator;
import com.treblemaker.model.comp.CompRhythm;
import com.treblemaker.model.progressions.ProgressionUnit;
import org.jfugue.pattern.Pattern;
import org.jfugue.theory.Note;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;

@Component
public class SynthPadGenerator implements ISynthPadGenerator {

    @Value("${num_of_generated_mixes}")
    Integer numOfGeneratedMixes;

    /*
    public ProgressionUnit generateAndSetSynthPad(ProgressionUnit progressionUnit, Map<ProgressionUnit.ProgressionType, CompRhythm> selectedCompOptions) {

        boolean skipBar = false;

        for(int i=0; i<progressionUnit.getProgressionUnitBars().size(); i++){

            if(!skipBar) {

                String chordNameOne = progressionUnit.getProgressionUnitBars().get(i).getjChord().toString();
                String chordNameTwo = progressionUnit.getProgressionUnitBars().get(i+1).getjChord().toString();

                //TODO IS THERE NO BETTER WAY??
                //CHANGE THE OCTAVE ..

                if (chordNameOne.indexOf("4") == 1 || chordNameOne.indexOf("4") == 2) {
                    //TODO IS THERE NO BETTER WAY??
                    //TODO WHAT IF ITS A SUS CORD .. REPLACING 4 COULD BE BAD??
                    chordNameOne = chordNameOne.replace("4", "5");
                }

                if (chordNameTwo.indexOf("4") == 1 || chordNameTwo.indexOf("4") == 2) {
                    //TODO IS THERE NO BETTER WAY??
                    //TODO WHAT IF ITS A SUS CORD .. REPLACING 4 COULD BE BAD??
                    chordNameTwo = chordNameTwo.replace("4", "5");
                }

                String selectedNoteOne = selectChordNote(progressionUnit.getProgressionUnitBars().get(i).getjChord().getNotes());
                String selectedNoteTwo = selectChordNote(progressionUnit.getProgressionUnitBars().get(i+1).getjChord().getNotes());

                CompRhythm compRhythm = selectedCompOptions.get(progressionUnit.getType());
                String[] compPositions = compRhythm.getComp().split(",");

                String jfugueSequence = CompGenerationUtil.compPositionsToJFugueSequence(compPositions, selectedNoteOne, selectedNoteTwo);

                progressionUnit.getProgressionUnitBars().get(i).setSelectedCompRhythm(compRhythm);

                //ITS POSSIBLE THIS LOOP SHOULD WRAP THE ENTIRE FUNCTION .. THIS IS ADDING THE
                //SAME PATTERN TO EACH INDEX
                for(int t=0; t<numOfGeneratedMixes; t++) {

                    if(t >= progressionUnit.getProgressionUnitBars().get(i).getPatternMidAlt().size()){
                        progressionUnit.getProgressionUnitBars().get(i).getPatternMid().add(new Pattern());
                        progressionUnit.getProgressionUnitBars().get(i).getPatternMidAlt().add(new Pattern());
                    }

                    progressionUnit.getProgressionUnitBars().get(i).getPatternMidAlt().get(t).add(jfugueSequence);
                    progressionUnit.getProgressionUnitBars().get(i).getPatternMid().get(t).add(jfugueSequence);
                }
            }

            skipBar = !skipBar;
        }

        return progressionUnit;
    }
    */


    public ProgressionUnit generateAndSetSynthPad(ProgressionUnit progressionUnit, Map<ProgressionUnit.ProgressionType, CompRhythm> selectedCompOptions) {

        boolean skipBar = false;

        for(int i=0; i<progressionUnit.getProgressionUnitBars().size(); i++){

            if(!skipBar) {

                String chordNameOne = progressionUnit.getProgressionUnitBars().get(i).getjChord().toString();
                String chordNameTwo = progressionUnit.getProgressionUnitBars().get(i+1).getjChord().toString();

                //TODO IS THERE NO BETTER WAY??
                //CHANGE THE OCTAVE ..

                if (chordNameOne.indexOf("4") == 1 || chordNameOne.indexOf("4") == 2) {
                    //TODO IS THERE NO BETTER WAY??
                    //TODO WHAT IF ITS A SUS CORD .. REPLACING 4 COULD BE BAD??
                    chordNameOne = chordNameOne.replace("4", "5");
                }

                if (chordNameTwo.indexOf("4") == 1 || chordNameTwo.indexOf("4") == 2) {
                    //TODO IS THERE NO BETTER WAY??
                    //TODO WHAT IF ITS A SUS CORD .. REPLACING 4 COULD BE BAD??
                    chordNameTwo = chordNameTwo.replace("4", "5");
                }

                //String selectedNoteOne = selectChordNote(progressionUnit.getProgressionUnitBars().get(i).getjChord().getNotes());
                //String selectedNoteTwo = selectChordNote(progressionUnit.getProgressionUnitBars().get(i+1).getjChord().getNotes());


                String jFugueString = "";
                jFugueString = chordNameOne + "w " + chordNameTwo + "w ";




                //String jfugueSequence = CompGenerationUtil.compPositionsToJFugueSequence(compPositions, selectedNoteOne, selectedNoteTwo);

                //progressionUnit.getProgressionUnitBars().get(i).setSelectedCompRhythm(compRhythm);

                //ITS POSSIBLE THIS LOOP SHOULD WRAP THE ENTIRE FUNCTION .. THIS IS ADDING THE
                //SAME PATTERN TO EACH INDEX
                for(int t=0; t<numOfGeneratedMixes; t++) {

                    if(t >= progressionUnit.getProgressionUnitBars().get(i).getPatternMidAlt().size()){
                        progressionUnit.getProgressionUnitBars().get(i).getPatternMid().add(new Pattern());
                        progressionUnit.getProgressionUnitBars().get(i).getPatternMidAlt().add(new Pattern());
                    }

                    progressionUnit.getProgressionUnitBars().get(i).getPatternMidAlt().get(t).add(jFugueString);
                    progressionUnit.getProgressionUnitBars().get(i).getPatternMid().get(t).add(jFugueString);
                }
            }

            skipBar = !skipBar;
        }

        return progressionUnit;
    }




    public String selectChordNote(Note[] notes){

        return notes[randomIndex()].toString();
    }

    public int randomIndex() {
        Random rand = new Random();
        int n = rand.nextInt(2);

        if (n == 1) {
            return 2;
        }

        return n;
    }
}
