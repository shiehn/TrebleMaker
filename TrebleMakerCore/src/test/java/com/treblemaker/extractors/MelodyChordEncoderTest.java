package com.treblemaker.extractors;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MelodyChordEncoderTest {

    MelodyChordEncoder melodyChordEncoder;

    @Before
    public void setup(){
        melodyChordEncoder = new MelodyChordEncoder();
    }

    @Test
    public void shouldEncodeA(){
        String chord = "cmaj7";
        String encoded = "313";
        assertEquals(encoded, melodyChordEncoder.encode(chord));
    }
    @Test
    public void shouldEncodeB(){
        String chord = "c#maj7";
        String encoded = "323";
        assertEquals(encoded, melodyChordEncoder.encode(chord));
    }
    @Test
    public void shouldEncodeC(){
        String chord = "c#4maj7";
        String encoded = "323";
        assertEquals(encoded, melodyChordEncoder.encode(chord));
    }
    @Test
    public void shouldEncodeD(){
        String chord = "d#maj";
        String encoded = "420";
        assertEquals(encoded, melodyChordEncoder.encode(chord));
    }
    @Test
    public void shouldEncodeE(){
        String chord = "g4dom7";
        String encoded = "715";
        assertEquals(encoded, melodyChordEncoder.encode(chord));
    }
    @Test
    public void shouldEncodeF(){
        String chord = "a#dim";
        String encoded = "122";
        assertEquals(encoded, melodyChordEncoder.encode(chord));
    }


    /*
        NOTES:
        0 | Rest
        1 | a
        2 | b
        3 | c
        4 | d
        5 | e
        6 | f
        7 | g

        FLAT/NATURAL/SHARP
        0 - FLAT | 1 - NATURAL | 2 - SHARP

        CHORD TYPES
        0 | maj
        1 | min
        2 | dim
        3 | maj7
        4 | min7
        5 | dom7
        6 | min7b5
     */
}