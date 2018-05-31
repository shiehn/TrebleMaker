package com.treblemaker.extractors;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class KeyExtractorTest {

    private KeyExtractor keyExtractor;

    @Before
    public void setup(){
        keyExtractor = new KeyExtractor();
    }

    @Test
    public void shouldExtractKey(){
        //setup
        List<String> chords = new ArrayList<>();
        chords.add("D4MIN7");
        chords.add("D4MIN");
        chords.add("EMIN");
        chords.add("DbMIN7");

        //test
        String actual = keyExtractor.extract(chords);

        //expect
        String expected = "c";
        assertThat(actual).isEqualToIgnoringCase(expected);
    }

    @Test
    public void shouldChooseKeyWhereRootIsFirstChordC(){
        //setup
        List<String> chords = new ArrayList<>();
        chords.add("Cmaj");
        chords.add("Gmaj");
        chords.add("amin");
        chords.add("amin");

        //test
        String actual = keyExtractor.extract(chords);

        //expect
        String expected = "c";
        assertThat(actual).isEqualToIgnoringCase(expected);
    }

    @Test
    public void shouldChooseKeyWhereRootIsFirstChordForEquallyBadOptions(){
        //setup
        List<String> chords = new ArrayList<>();
        chords.add("amaj");
        chords.add("bmin");
        chords.add("d#maj");
        chords.add("fmin");

        //test
        String actual = keyExtractor.extract(chords);

        //expect
        String expected = "a";
        assertThat(actual).isEqualToIgnoringCase(expected);
    }

    @Test
    public void shouldChooseKeyFlatsArePresent(){
        //setup
        List<String> chords = new ArrayList<>();
        chords.add("ab4maj");
        chords.add("bbmin7");
        chords.add("db5maj7");
        chords.add("d#dom7");

        //test
        String actual = keyExtractor.extract(chords);

        //expect
        String expected = "g#";
        assertThat(actual).isEqualToIgnoringCase(expected);
    }
}