package com.treblemaker.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class HiveChordTest {

    @Test
    public void shouldNotHaveMatchingRoots(){
       HiveChord chordC = new HiveChord("cmaj7");
       HiveChord chordCSharp = new HiveChord("c#maj7");

       HiveChord chordC4 = new HiveChord("c4maj7");
       HiveChord chordCSharp4 = new HiveChord("c#4maj7");

       assertFalse(chordC.hasMatchingRoot(chordCSharp));
       assertFalse(chordC4.hasMatchingRoot(chordCSharp4));
    }

    @Test
    public void shouldHaveMatchingRoots(){
        HiveChord chordC = new HiveChord("cmaj7");
        HiveChord chordCSharp = new HiveChord("cmaj7");

        HiveChord chordC4 = new HiveChord("c4maj7");
        HiveChord chordCSharp4 = new HiveChord("c4maj7");

        assertTrue(chordC.hasMatchingRoot(chordCSharp));
        assertTrue(chordC4.hasMatchingRoot(chordCSharp4));
    }

    @Test
    public void shouldNotExtractSameRoot(){
        HiveChord hiveChord = new HiveChord();

        String chordCRoot = hiveChord.extractRoot("cmaj7");
        String chordCSharpRoot = hiveChord.extractRoot("c#maj7");

        assertNotEquals(chordCRoot, chordCSharpRoot);
    }

    @Test
    public void shouldExtractSameRoot(){
        HiveChord hiveChord = new HiveChord();

        String chordCRoot = hiveChord.extractRoot("cmaj7");
        String chordCSharpRoot = hiveChord.extractRoot("c4maj7");

        assertEquals(chordCRoot, chordCSharpRoot);
    }

    @Test
    public void shouldExtractCorrectRoot(){
        HiveChord hiveChord = new HiveChord();
        String chordCRoot = hiveChord.extractRoot("cmaj7");
        String chordCSharpRoot = hiveChord.extractRoot("c#maj7");
        String chordCRoot4 = hiveChord.extractRoot("c4maj7");
        String chordCSharpRoot4 = hiveChord.extractRoot("c#4maj7");

        assertEquals(chordCRoot, "c");
        assertEquals(chordCSharpRoot, "c#");
        assertEquals(chordCRoot4, "c");
        assertEquals(chordCSharpRoot4, "c#");
    }

    @Test
    public void shouldRemoveOctaveFromChordName(){
        HiveChord chord = new HiveChord("c#4maj7");
        assertEquals("c#maj7", chord.getChordName());
    }
}