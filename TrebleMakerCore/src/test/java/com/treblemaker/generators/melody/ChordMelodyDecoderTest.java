package com.treblemaker.generators.melody;

import org.junit.Assert;
import org.junit.Test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ChordMelodyDecoderTest {

    String validA = "11^113*113*113*415*713*713*713*315*6250-6251-6251-6251-6251-6251-6251-6251-6251-6251-6251-6251-1150-1151-6250-6251+5150-5151-5151-5151-5151-5151-5151-5151-4250-4251-3250-3251-2140-2141-2141-2141+6250-6251-6251-6251-6251-6251-6251-6251-6251-6251-6251-6251-6251-6251-6251-6251+6250-6251-6251-6251-6251-6251-6251-6251-6251-6251-6251-6251-6251-6251-6251-6251#";
    String validB = "11^113*113*113*415*713*713*713*315*3250-3251-3251-3251-4250-4251-5150-5151-7250-7251-6250-6251-6251-6251-2140-2141+2140-2141-2141-2141-2141-2141-2141-2141-2141-2141-2141-2141-2141-2141-2141-2141+6250-6251-7250-7251-2150-2151-2151-2151-2151-2151-2151-2151-2151-2151-2151-2151+0000-0000-0000-0000-1150-1151-1151-1151-1150-1151-1151-1151-1151-1151-1150-1151#";

    @Test
    public void shouldDetermineIfValidMelody(){

        String invalidA = "11^113*113*113*415*713*713*713*315*0000-0000-0000-0000-4260-4261-4261-4261-4260-4261-5160-5161-3160-3161-3161-3161+3260-3261-3261-3261-3261-3261-3261-3261-1250-1251-3260-3261-1250-1251-2150-2151+0000-0000-0000-0000-0000-0000-0000-0000-0000-0000-0000-0000-0000-0000-0000-0000#4000000000000000000000000000#00##0#000#00000000000000000000000000000000000000009";
        String invalidB = "11^113*113*113*415*713*713*713*315*0000-0000-0000-0000-7250-7251-7251-7251-7251-7251-7251-7251-1150-1151-1151-1151+7250-7251-7251-7251-7251-7251-7250-7251-6250-6251-6251-6251-5150-5151-5151-5151+5150-5151-5151-5151-5151-5151-5151-5151-7150-7151-7151-7151-7151-7151-2150-2151##000000000-0-0-0#000000000000000000000000000000000000000000000000000000000000000";
        String invalidC = "11^113*113*113*415*713*713*713*315*5140-5141-5141-5141-5141-5141-5141-5141-0000-0000-0000-0000-0000-0000-0000-0000+0000-0000-0000-0000+0000-0000-0000-0000-0000-0000-0000-0000-0000-0000-0000-0000-0000-0000-0000-0000#100000-000-0000000000000000000000000000000000000000000000000000##000000000000000000000000000000000000000000000000000000000000000000000000000";

        ChordMelodyDecoder chordMelodyDecoder = new ChordMelodyDecoder(new Config(false));
        Assert.assertFalse(chordMelodyDecoder.INVALID.equalsIgnoreCase(chordMelodyDecoder.extractValidMelody(validA)));
        Assert.assertFalse(chordMelodyDecoder.INVALID.equalsIgnoreCase(chordMelodyDecoder.extractValidMelody(validB)));

        Assert.assertTrue(chordMelodyDecoder.INVALID.equalsIgnoreCase(chordMelodyDecoder.extractValidMelody(invalidA)));
        Assert.assertTrue(chordMelodyDecoder.INVALID.equalsIgnoreCase(chordMelodyDecoder.extractValidMelody(invalidB)));
        Assert.assertTrue(chordMelodyDecoder.INVALID.equalsIgnoreCase(chordMelodyDecoder.extractValidMelody(invalidC)));
    }

    @Test
    public void shouldDetermineIfValidChords(){

        String validA = "#61:310-610%310*610*310*610";
        String validB = "#31:110-610%310*610*310*510";

        String invalidA = "#31:310-610%310:610*310*610";
        String invalidB = "#31:310-610%310%610*310*610";
        String invalidC = "#31:310-610%310*610*310-610";

        ChordMelodyDecoder chordMelodyDecoder = new ChordMelodyDecoder(new Config(false));
        Assert.assertFalse(chordMelodyDecoder.INVALID.equalsIgnoreCase(chordMelodyDecoder.extractValidChords(validA)));
        Assert.assertFalse(chordMelodyDecoder.INVALID.equalsIgnoreCase(chordMelodyDecoder.extractValidChords(validB)));

        Assert.assertTrue(chordMelodyDecoder.INVALID.equalsIgnoreCase(chordMelodyDecoder.extractValidChords(invalidA)));
        Assert.assertTrue(chordMelodyDecoder.INVALID.equalsIgnoreCase(chordMelodyDecoder.extractValidChords(invalidB)));
        Assert.assertTrue(chordMelodyDecoder.INVALID.equalsIgnoreCase(chordMelodyDecoder.extractValidChords(invalidC)));
    }

    @Test
    public void shouldProduceJfugeString(){
        String expectedA = "f#5h. a5i f#5i e5h d#5i c#5i b4q f#5w f#5w";
        String expectedB = "c#5q d#5i e5i g#5i f#5q b4i b4w f#5i g#5i b5h. rs rs rs rs a5q a5q. a5i";
        ChordMelodyDecoder decoder = new ChordMelodyDecoder(new Config(false));

        System.out.println("ACTUAL:   " + decoder.decodeMelody(validA,decoder.getKey(validA)));
        System.out.println("EXPECTED: " + expectedA);
        Assert.assertTrue(expectedA.trim().equalsIgnoreCase(decoder.decodeMelody(validA,decoder.getKey(validA)).trim()));

        System.out.println("ACTUAL:   " + decoder.decodeMelody(validB,decoder.getKey(validB)));
        System.out.println("EXPECTED: " + expectedB);
        Assert.assertTrue(expectedB.trim().equalsIgnoreCase(decoder.decodeMelody(validB,decoder.getKey(validB)).trim()));
    }

    @Test
    public void shouldExtractKey(){
        ChordMelodyDecoder chordMelodyDecoder = new ChordMelodyDecoder(new Config(false));

        String key = chordMelodyDecoder.getKey(validA);
        Assert.assertEquals("a", key);
    }

    @Test
    public void shouldProduceCorrectJFugeString(){
        String input = "31^313*313*313*313*613*613*715*715*" +
                "2140-2141-2040-2041-" +
                "2040-2040-2040-2040-" +
                "2040-2041-3150-3151-" +
                "2040-2041-1140-1141+" +
                "2140-3150-2140-2141-" +
                "0000-0000-0000-0000-" +
                "0000-0000-0000-0000-" +
                "0000-0000-0000-0000+" +

                "6150-5150-2140-2140-" +
                "2141-2141-2140-2140-" +
                "2140-2140-2140-2141-" +
                "2140-2141-2141-2140+";


        String expected = "b4i a#4i a#4s a#4s a#4s a#4s a#4i c5i a#4i a4i b4s c5s b4i rs rs rs rs rs rs rs rs rs rs rs rs " +
                "f5s e5s b4s b4i. b4s b4s " +
                "b4s b4s b4i " +
                "b4i. b4s";
        ChordMelodyDecoder decoder = new ChordMelodyDecoder(new Config(false));
        String actual = decoder.decodeMelody(input,decoder.getKey(input));
        System.out.println("ACTUAL:   " + actual);
        System.out.println("EXPECTED: " + expected);

        Assert.assertEquals(expected.trim(), decoder.decodeMelody(input,decoder.getKey(input)).trim());
    }

    @Test
    public void shouldPitchCorrectedJFugeString(){
        String input = "31^313*313*313*313*613*613*715*715*" +
                "2140-2141-2040-2041-" +
                "2040-2040-2040-2040-" +
                "2040-2041-3150-3151-" +
                "2040-2041-1140-1141+" +
                "2140-3150-2140-2141-" +
                "0000-0000-0000-0000-" +
                "0000-0000-0000-0000-" +
                "0000-0000-0000-0000+" +

                "6150-5150-2140-2140-" +
                "2141-2141-2140-2140-" +
                "2140-2140-2140-2141-" +
                "2140-2141-2141-2140+";


        String expected = "b4i a#4i a#4s a#4s a#4s a#4s a#4i c5i a#4i a4i b4s c5s b4i rs rs rs rs rs rs rs rs rs rs rs rs " +
                "f5s e5s b4s b4i. b4s b4s " +
                "b4s b4s b4i " +
                "b4i. b4s";
        boolean forceCorrectPitches = true;
        ChordMelodyDecoder decoder = new ChordMelodyDecoder(new Config(true));
        String actual = decoder.decodeMelody(input, decoder.getKey(input));
        System.out.println("ACTUAL:   " + actual);

        List<String> badPitches = new ArrayList<>();
        for(String note : actual.split(" ")){
            if(note.contains("#")){
                badPitches.add(note);
            }
        }

        System.out.println("BAD NOTES: " + badPitches);

        Assert.assertEquals(0, badPitches.size());
    }

    @Test
    public void shouldExtractChordsToArray(){
        ChordMelodyDecoder decoder = new ChordMelodyDecoder(null);

        String chordPrediction = "#31:313-313%125*415*713*713";

        List<String> chords = decoder.extractChordPredictionToArray(chordPrediction);

        List<String> expected = Arrays.asList("125","415","713","713");

        assertThat(chords).isEqualTo(expected);
    }
}