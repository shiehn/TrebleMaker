package com.treblemaker.generators.melody;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class PitchCorrectionTest {

    PitchCorrection pitchCorrection;

    @Before
    public void setup(){
        pitchCorrection = new PitchCorrection();
    }
    //note=e in key=[a#, c, d, d#, f, g, a]
    @Test
    public void shouldCorrectPitchesToSharpKey(){
        String[] notesToCorrect = new String[]{"ab","a", "a#","bb","b","b#", "cb","c","c#","db","d","d#","eb","e","e#","fb","f","f#", "gb", "g", "g#"};
        String[] correctedNotes = pitchCorrection.forceCorrectPitches(notesToCorrect, "a#");

        System.out.println(pitchCorrection.getNotesInKey("a#"));

        List<String> expectedNotes = Arrays.asList("a#", "c", "d", "d#", "f", "g", "a");

        for (String note: correctedNotes) {
            assertThat(expectedNotes).contains(note.toLowerCase());
        }
    }

    @Test
    public void shouldCorrectPitchesToNaturalKey(){
        String[] notesToCorrect = new String[]{"ab","a", "a#","bb","b","b#", "cb","c","c#","db","d","d#","eb","e","e#","fb","f","f#", "gb", "g", "g#"};
        String[] correctedNotes = pitchCorrection.forceCorrectPitches(notesToCorrect, "c");

        System.out.println(pitchCorrection.getNotesInKey("c"));

        List<String> expectedNotes = Arrays.asList("c", "d", "e", "f", "g", "a", "b");


        for (String note: correctedNotes) {
            assertThat(expectedNotes).contains(note.toLowerCase());
        }
    }
}