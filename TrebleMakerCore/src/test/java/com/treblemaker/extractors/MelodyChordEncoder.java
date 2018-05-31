package com.treblemaker.extractors;

import com.treblemaker.model.HiveChord;

public class MelodyChordEncoder {
    public String encode(String chordName){

        HiveChord hiveChord = new HiveChord(chordName);

        String root = hiveChord.extractRoot(chordName);

        String type = hiveChord.getChordName().replaceFirst(root, "");

        return encodeNoteName(root) + encodeChordType(type);
    }

    public String encodeNoteName(String encoded) {
        switch (encoded) {
            case "a" :
                return "11";
            case "a#" :
                return "12";
            case "b":
                return "21";
            case "c" :
                return "31";
            case "c#" :
                return "32";
            case "d" :
                return "41";
            case "d#" :
                return "42";
            case "e" :
                return "51";
            case "f" :
                return "61";
            case "f#" :
                return "62";
            case "g" :
                return "71";
            case "g#" :
                return "72";
        }

        throw new RuntimeException("cannot encode: " + encoded);
    }

    public String encodeChordType(String chordType) {
        switch (chordType) {
            case "maj" :
                return "0";
            case "min" :
                return "1";
            case "dim":
                return "2";
            case "maj7" :
                return "3";
            case "min7" :
                return "4";
            case "dom7" :
                return "5";
            case "min7b5" :
                return "6";
        }

        throw new RuntimeException("cannot encode: " + chordType);
    }
}