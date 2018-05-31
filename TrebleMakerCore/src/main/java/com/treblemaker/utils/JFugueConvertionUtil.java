package com.treblemaker.utils;

import com.treblemaker.model.bassline.Intervals;
import org.jfugue.pattern.Pattern;
import org.jfugue.theory.Chord;
import org.jfugue.theory.Note;

import java.util.ArrayList;
import java.util.List;

public class JFugueConvertionUtil {

    public static List<String> getDurations(int[] intervals){

        List<Integer> lengths = new ArrayList<>();
        int prev = 999;
        int count = 0;
        for (int t:intervals) {
            if(prev != t && prev != 999 || count == 4){
                lengths.add(count);
                prev = t;
                count = 1;
            } else {
                prev = t;
                count++;
            }
        }
        lengths.add(count);

        List<String> durations = new ArrayList<>();

        for(int l : lengths){
            durations.add(durationToRests(l));
        }

        return durations;
    }

    private static String durationToRests(int duration){

        switch (duration){
            case 1:
                return "s";
            case 2:
                return "i";
            case 3:
                return "i.";
            case 4:
                return "q";
        }

        throw new RuntimeException();
    }

    public static List<String> getNotes(int[] beats, Intervals intervalsA, Intervals intervalsB){

        List<String> notes = new ArrayList<>();

        int prev = 999;
        int count = 0;
        for (int i=0; i<beats.length; i++) {
            int t = beats[i];
            if(prev != t || count == 4){
                count = 0;
                if(t == 0){
                    notes.add("R");
                }else{
                    if(i < 15){
                        notes.add(intervalsA.getByPosition(t).toString());
                    }else{
                        notes.add(intervalsB.getByPosition(t).toString());
                    }
                }
                prev = t;
            }
            count++;
        }

        return notes;
    }

    public static Pattern generatePattern(String note, String duration){

        Pattern pattern = new Pattern();
        pattern.add(note + duration);

        return pattern;
    }

    public static Intervals populateIntervals(Chord chord, int bassOctave){

        Intervals intervals = new Intervals();

        if(chord.getNotes().length == 3){
            intervals.setIntervalClass(ChordClass.Triad);
        }else if(chord.getNotes().length == 4){
            intervals.setIntervalClass(ChordClass.Seventh);
        }else {


            intervals.setIntervalClass(ChordClass.Seventh);
        }

        String rootOctave = Integer.toString(bassOctave);
        String rootOctaveTransposedUp = Integer.toString(bassOctave + 1);

        if(intervals.getIntervalClass() == ChordClass.Triad){
            intervals.setNoteOne(chord.getNotes()[0]);
            intervals.setNoteTwo(chord.getNotes()[1]);
            intervals.setNoteThree(chord.getNotes()[2]);

            String transposedNote = chord.getNotes()[0].toString().replace(rootOctave,rootOctaveTransposedUp);
            intervals.setNoteFive(new Note(transposedNote));
        }else if(intervals.getIntervalClass() == ChordClass.Seventh){
            intervals.setNoteOne(chord.getNotes()[0]);
            intervals.setNoteTwo(chord.getNotes()[1]);
            intervals.setNoteThree(chord.getNotes()[2]);
            intervals.setNoteFour(chord.getNotes()[3]);
            String transposedNote = chord.getNotes()[0].toString().replace(rootOctave,rootOctaveTransposedUp);
            intervals.setNoteFive(new Note(transposedNote));
        } else {
            throw new RuntimeException("fuck - SOME JFUGUE ISSUE");
        }

        return intervals;
    }

    public static int getBasslineChordsCode(Intervals intervalsOne, Intervals intervalsTwo){

        if(intervalsOne.getIntervalClass() == ChordClass.Triad && intervalsTwo.getIntervalClass() == ChordClass.Triad){
            return 33;
        }else if(intervalsOne.getIntervalClass() == ChordClass.Triad && intervalsTwo.getIntervalClass() == ChordClass.Seventh){
            return 34;
        }else if(intervalsOne.getIntervalClass() == ChordClass.Seventh && intervalsTwo.getIntervalClass() == ChordClass.Seventh){
            return 44;
        }else if(intervalsOne.getIntervalClass() == ChordClass.Seventh && intervalsTwo.getIntervalClass() == ChordClass.Triad){
            return 43;
        }

        throw new RuntimeException("bassline code not found");
    }
}
