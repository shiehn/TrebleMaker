package com.treblemaker.model;

import java.io.Serializable;

/**
 * Created by Steve on 2015-09-08.
 */
public class ChordBase extends Object implements Serializable {

    public String normalizeNotesToSharps(String chordType) {

        String chordName = "";

        if (chordType != null && chordType.length() > 3) {

            String secondCharacter = Character.toString(chordType.charAt(1));

            if (secondCharacter.equalsIgnoreCase("b") || secondCharacter.equalsIgnoreCase("#")) {

                //get the first to characters ..
                String prefix = chordType.substring(0, 2);
                String suffix = chordType.substring(2, chordType.length());

                //update the charaters ..
                chordName = transposeChordName(prefix.toLowerCase());

                //replace the characters
                return (chordName + suffix).toLowerCase();
            } else {
                return chordType.toLowerCase();
            }
        }

        return chordType.toLowerCase();
    }

    public String transposeChordName(String chordName) {

        switch (chordName.toLowerCase()) {
            case "ab":
                return "g#";
            case "a":
                return "a";
            case "a#":
                return "a#";
            case "bb":
                return "a#";
            case "b":
                return "b";
            case "cb":
                return "b";
            case "c":
                return "c";
            case "c#":
                return "c#";
            case "db":
                return "c#";
            case "d":
                return "d";
            case "d#":
                return "d#";
            case "eb":
                return "d#";
            case "e":
                return "e";
            case "fb":
                return "e";
            case "f":
                return "f";
            case "f#":
                return "f#";
            case "gb":
                return "f#";
            case "g":
                return "g";
            case "g#":
                return "g#";
        }

        return chordName;
    }
}
