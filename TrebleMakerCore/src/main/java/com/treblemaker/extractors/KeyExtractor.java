package com.treblemaker.extractors;

import com.treblemaker.model.HiveChord;

import java.util.*;

class KeyScore {
    private String key = "";
    private int score = 0;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        this.score = score + 1;
    }
}

public class KeyExtractor {
    public String extract(List<String> chords){

        List<String> potentialKeys = Arrays.asList("a","a#","b", "c","c#", "d","d#", "e", "f","f#", "g", "g#");
        Map<String, Integer> keyScore = new HashMap<>();

        for (String key : potentialKeys) {
            keyScore.put(key, 0);
        }

        for(String key: potentialKeys){

            List<HiveChord> chordsInKey = getChordsInKey(key);

            for(String chord: chords){
                HiveChord hiveChord = new HiveChord(chord);

                for (HiveChord hChordInKey : chordsInKey) {
                    if(hiveChord.getChordName().equalsIgnoreCase(hChordInKey.getChordName())){
                        keyScore.put(key, keyScore.get(key)+1);
                    }
                }
            }
        }

        System.out.println(keyScore);

        String keyWithHighestScore = "";
        int currentScore = -1;

        for(String key: potentialKeys){
            if(keyScore.get(key) > currentScore){
                keyWithHighestScore = key;
                currentScore = keyScore.get(key);
            }
        }

        List<String> highestScores = new ArrayList<>();
        for(String key: potentialKeys){
            if(keyScore.get(key) == currentScore){
                highestScores.add(key);
            }
        }

        if(highestScores.size() == 1){
            return highestScores.get(0);
        }

        //if there are multiple keys match the one where the tonic is first chord
        for (String key : highestScores) {
            if(chords.get(0).equalsIgnoreCase(key+"maj") ||
                    chords.get(0).equalsIgnoreCase(key + "maj7")){
                return key;
            }
        }

        return keyWithHighestScore;
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

    public List<HiveChord> getChordsInKey(String key){
        switch (key.toLowerCase()){
            case "a":
                return createChordsInKey(getNotesInKey(key.toLowerCase()));
            case "a#":
                return createChordsInKey(getNotesInKey(key.toLowerCase()));
            case "b":
                return createChordsInKey(getNotesInKey(key.toLowerCase()));
            case "c":
                return createChordsInKey(getNotesInKey(key.toLowerCase()));
            case "c#":
                return createChordsInKey(getNotesInKey(key.toLowerCase()));
            case "d":
                return createChordsInKey(getNotesInKey(key.toLowerCase()));
            case "d#":
                return createChordsInKey(getNotesInKey(key.toLowerCase()));
            case "e":
                return createChordsInKey(getNotesInKey(key.toLowerCase()));
            case "f":
                return createChordsInKey(getNotesInKey(key.toLowerCase()));
            case "f#":
                return createChordsInKey(getNotesInKey(key.toLowerCase()));
            case "g":
                return createChordsInKey(getNotesInKey(key.toLowerCase()));
            case "g#":
                return createChordsInKey(getNotesInKey(key.toLowerCase()));
            default:
                throw new RuntimeException("unknown key!!! = " + key);
        }
    }

    private List<HiveChord> createChordsInKey(List<String> notesInChord){

        List<String> triads = new ArrayList<>();
        triads.add("maj");
        triads.add("min");
        triads.add("min");
        triads.add("maj");
        triads.add("maj");
        triads.add("min");
        triads.add("dim");

        List<String> sevenths = new ArrayList<>();
        sevenths.add("maj7");
        sevenths.add("min7");
        sevenths.add("min7");
        sevenths.add("maj7");
        sevenths.add("dom7");
        sevenths.add("min7");
        sevenths.add("min7b5");

        List<HiveChord> chords = new ArrayList<>();
        for(int i=0; i<notesInChord.size(); i++){
            chords.add(new HiveChord(notesInChord.get(i) + triads.get(i)));
            chords.add(new HiveChord(notesInChord.get(i) + sevenths.get(i)));
        }

        return chords;
    }
}


