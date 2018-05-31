package com.treblemaker.generators.melody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PitchCorrection {

    public String convertToNaturalOrSharp(String note){

        if(note.length() > 1 && (note.charAt(1)+"").equalsIgnoreCase("b")){
            switch (note.toLowerCase()){
                case "ab":
                    return "g#";
                case "bb":
                    return "a#";
                case "cb":
                    return "b";
                case "db":
                    return "c#";
                case "eb":
                    return "d#";
                case "fb":
                    return "d";
                case "gb":
                    return "f#";

            }

            throw new RuntimeException("COULD NOT CORRECT FLAT TO SHARP: note=" + note);
        }

        return note;
    }

    public String[] forceCorrectPitches(String[] notes, String key){

        key = convertToNaturalOrSharp(key);

        for (int i=0; i<notes.length; i++) {
            notes[i] = convertToNaturalOrSharp(notes[i]);
        }

        /*
        String expected = "b4i a#4i a#4s a#4s a#4s a#4s a#4i c5i a#4i a4i b4s c5s b4i rs rs rs rs rs rs rs rs rs rs rs rs " +
                "f5s e5s b4s b4i. b4s b4s " +
                "b4s b4s b4i " +
                "b4i. b4s";
         */

        //collect not names
        List<String> correctedNotes = new ArrayList<>();
        for (String note : notes){

            System.out.println("A");

            if(note.toLowerCase().contains("r")){
                System.out.println("B");
                correctedNotes.add(note);
            }else{
                String extractedNote = note.split("[0-9]")[0];
                System.out.println("C");
                System.out.println("EXTRACTED NOTE:" + extractedNote);
                System.out.println("NOTE: " + note);
                if(!keyContainsNote(extractedNote, getNotesInKey(key))){
                    System.out.println("D");
                    String replacementNote = findNearestNoteInKey(extractedNote.replace("#", ""),getNotesInKey(key));
                    note = note.replace(extractedNote, replacementNote);
                    System.out.println(String.format("REPLACING %s WITH %s", note, replacementNote));
                }

                System.out.println("E");
                correctedNotes.add(note);
            }
        }

        String[] corrected = new String[notes.length];
        return correctedNotes.toArray(corrected);
    }

    private boolean keyContainsNote(String note, List<String> keyNotes){
        return keyNotes.contains(note);
    }

    public String findNearestNoteInKey(String noteName, List<String> keyNotes){
        for(String keyNote: keyNotes){
            if(keyNote.contains(noteName)){
                return keyNote;
            }else{
                System.out.println("not able to find nearby not in key for note=" + noteName + " in key=" + keyNotes);
                return keyNotes.get(new Random().nextInt(keyNotes.size()));
            }
        }

        throw new RuntimeException("not able to find nearby not in key for note=" + noteName + " in key=" + keyNotes);
    }

    public List<String> getNotesInKey(String key){
        switch (key){
            case "a":
                return Arrays.asList("a", "b", "c#", "d", "e", "f#", "g#");
            case "a#":
                return Arrays.asList("a#", "c", "d", "d#", "f", "g", "a");
            case "b":
                return Arrays.asList("b", "c#", "d#", "e", "f#", "g#", "a#");
            case "c":
                return Arrays.asList("c", "d", "e", "f", "g", "a", "b");
            case "c#":
                return Arrays.asList("c#", "d#", "e#", "f#", "g#", "a#", "b#");
            case "d":
                return Arrays.asList("d", "e", "f#", "g", "a", "b", "c#");
            case "d#":
                return Arrays.asList("d#", "e#", "g", "g#", "a#", "b#", "d");
            case "e":
                return Arrays.asList("e", "f#", "g#", "a", "b", "c#", "d#");
            case "f":
                return Arrays.asList("f", "g", "a", "a#", "c", "d", "e");
            case "f#":
                return Arrays.asList("f#", "g#", "a#", "b", "c#", "d#", "f");
            case "g":
                return Arrays.asList("g", "a", "b", "c", "d", "e", "f#");
            case "g#":
                return Arrays.asList("g#", "a#", "b#", "c#", "d#", "e#", "g");
                default:
                    throw new RuntimeException("unknown key!!! = " + key);
        }
    }
}