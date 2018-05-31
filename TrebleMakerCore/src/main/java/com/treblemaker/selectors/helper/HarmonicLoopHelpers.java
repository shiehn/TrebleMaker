package com.treblemaker.selectors.helper;

import com.treblemaker.selectors.models.ChordSet;

import java.util.ArrayList;
import java.util.List;

public class HarmonicLoopHelpers {

    public static List<ChordSet> createListOfChords(List<List<String>> progressions){

        List<ChordSet> chordSets = new ArrayList<>();

        int j = 0;

        for(List<String> progressionList : progressions){

            boolean setChordOne = true;

            for(String chord : progressionList){

                if(setChordOne){
                    ChordSet chordSet = new ChordSet();
                    chordSet.setChordOne(chord);
                    chordSets.add(chordSet);
                    setChordOne = false;
                }else{
                    chordSets.get(j).setChordTwo(chord);
                    setChordOne = true;
                    j++;
                }
            }
        }

        return chordSets;
    }

    public static List<ChordSet> createFirstAndThirdSets(List<List<String>> progressions){

        List<ChordSet> chordSets = new ArrayList<>();

        for(List<String> progressionList : progressions){

            ChordSet chordSet = new ChordSet();

            int i = 0;
            boolean chordOne = true;

            for(String chord : progressionList){

                if(i%2 == 0){

                    if(chordOne){
                        chordSet.setChordOne(chord);
                    }else{
                        chordSet.setChordTwo(chord);

                        if(chordSet.getChordOne() != null && chordSet.getChordTwo() != null){
                            chordSets.add(chordSet);
                        }
                    }

                    chordOne = !chordOne;
                }

                i++;
            }
        }

        return chordSets;
    }

    public static List<ChordSet> getFirstSets(List<ChordSet> chordSets){
        List<ChordSet> returnSets = new ArrayList<>();

        int i = 0;
        for(ChordSet set : chordSets) {
            if (i % 2 == 0) {
                if(!returnSets.contains(set)){
                    returnSets.add(set);
                }
            }
            i++;
        }

        return returnSets;
    }

    public static List<ChordSet> getSecondSets(List<ChordSet> chordSets){
        List<ChordSet> returnSets = new ArrayList<>();

        int i = 0;
        for(ChordSet set : chordSets) {
            if (i % 2 != 0) {
                if(!returnSets.contains(set)){
                    returnSets.add(set);
                }
            }
            i++;
        }

        return returnSets;
    }
}
