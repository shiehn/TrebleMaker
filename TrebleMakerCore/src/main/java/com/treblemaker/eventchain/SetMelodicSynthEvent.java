package com.treblemaker.eventchain;

import com.treblemaker.dal.interfaces.IBachChoraleDal;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.extractors.MelodicExtractor;
import com.treblemaker.extractors.model.HarmonyExtraction;
import com.treblemaker.generators.melody.MelodyGenerator;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.melodic.BachChorale;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnit.ProgressionType;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SetMelodicSynthEvent implements IEventChain {

    private MelodyGenerator melodydGenerator;

    private MelodicExtractor melodicExtractor;

    private IBachChoraleDal bachChoraleDal;

    @Autowired
    public SetMelodicSynthEvent(MelodyGenerator melodydGenerator, MelodicExtractor melodicExtractor, IBachChoraleDal bachChoraleDal) {
        this.melodydGenerator = melodydGenerator;
        this.melodicExtractor = melodicExtractor;
        this.bachChoraleDal = bachChoraleDal;
    }

    @Override
    public QueueState set(QueueState queueState) {
        Map<ProgressionType, List<HiveChord>> pTypeToChords = new HashMap<>();
        for (ProgressionUnit progressionUnit : queueState.getStructure()) {
            List<HiveChord> chords = new ArrayList<>();
            for(ProgressionUnitBar pBar : progressionUnit.getProgressionUnitBars()){
                chords.add(pBar.getChord());
            }
            pTypeToChords.put(progressionUnit.getType(), chords);
        }

        Map<ProgressionType, String> pTypeToMelody = new HashMap<>();
        for (Map.Entry<ProgressionType, List<HiveChord>> entry : pTypeToChords.entrySet())
        {
            String jFugueMelody = melodydGenerator.generate(entry.getValue());
            pTypeToMelody.put(entry.getKey(), jFugueMelody);
        }

        for (ProgressionUnit progressionUnit : queueState.getStructure()) {
            progressionUnit.setMelody(pTypeToMelody.get(progressionUnit.getType()));
        }

        return queueState;
    }

    public Map<ProgressionType, List<BachChorale>> createTypeToBachChorales(List<BachChorale> choralesDb, Map<ProgressionType, List<HarmonyExtraction>> typeToHarmonicExtractionMap){

        Map<ProgressionType, List<BachChorale>> typeToChorales = new HashMap<>();

        for(Map.Entry<ProgressionType, List<HarmonyExtraction>> mapEntry : typeToHarmonicExtractionMap.entrySet()){
            if(typeToChorales.get(mapEntry.getKey()) == null){
                typeToChorales.put(mapEntry.getKey(), createMatchingChorales(choralesDb, mapEntry.getValue()));
            }
        }

        return typeToChorales ;
    }

    public List<String> findUnusedNotes(List<String> usedNotes, List<String> availableNotes) {

        List<String> unUsedNotes = new ArrayList<>();

        for (String availableNote : availableNotes) {
            boolean isAvailable = true;
            for (String usedNote : usedNotes) {
                if (usedNote.equalsIgnoreCase(availableNote)) {
                    isAvailable = false;
                }
            }

            if (isAvailable) {
                unUsedNotes.add(availableNote);
            }
        }

        return unUsedNotes;
    }

    public Map<ProgressionType, List<String>> createTypeToChordSequenceMap(Map<ProgressionType, int[]> typeToMatchLengthAndOffset, List<BachChorale> chorales) {

        Map<ProgressionType, List<String>> typeToChordSequence = new HashMap<>();

        for (Map.Entry<ProgressionType, int[]> map : typeToMatchLengthAndOffset.entrySet()) {

            int startIndex = map.getValue()[1];
            int endIndex = map.getValue()[1] + map.getValue()[0];
            List<String> chordSequence = chorales.subList(startIndex, endIndex).stream().map(c -> c.getChord()).collect(Collectors.toList());
            typeToChordSequence.put(map.getKey(), chordSequence);
        }

        return typeToChordSequence;
    }

    public List<BachChorale> createMatchingChorales(List<BachChorale> bachChorales, List<HarmonyExtraction> harmonyExtractions) {

        List<BachChorale> matchingChorales = new ArrayList<>();
        List<HarmonyExtraction> remainingExtractions = harmonyExtractions;

        while (matchingChorales.size() < harmonyExtractions.size()){

            LongestMatchResponse longestMatch = getLongestMatch(bachChorales, remainingExtractions);
            if(longestMatch.getMatch().isEmpty()){
                longestMatch.addMatchingChord(null);
                matchingChorales.add(null);
            }else {
                matchingChorales.addAll(longestMatch.getMatch());
            }

            remainingExtractions = remainingExtractions.subList(longestMatch.getMatch().size(), remainingExtractions.size());
        }

        return matchingChorales;
    }

    class LongestMatchResponse {
        List<BachChorale> match = new ArrayList<>();

        public List<BachChorale> getMatch() {
            return match;
        }

        public void setMatch(List<BachChorale> match) {
            this.match = match;
        }

        public void addMatchingChord(BachChorale bachChorale) {
            match.add(bachChorale);
        }
    }

    public LongestMatchResponse getLongestMatch(List<BachChorale> bachChorales, List<HarmonyExtraction> harmonyExtractions) {

        LongestMatchResponse longestMatchResponse = new LongestMatchResponse();

        for (int currentLength = 0; currentLength < harmonyExtractions.size(); currentLength++) {

            List<HarmonyExtraction> extractionSubset = harmonyExtractions.subList(0, currentLength + 1);

            for (int i = 0; i < bachChorales.size() - extractionSubset.size(); i++) {

                LongestMatchResponse currentMatchResponse = new LongestMatchResponse();

                for (int subSetIndex = 0; subSetIndex < extractionSubset.size(); subSetIndex++) {
                    //compare
                    if(extractionSubset.get(subSetIndex) != null){
                        if (bachChorales.get(i + subSetIndex).getChord().equalsIgnoreCase(extractionSubset.get(subSetIndex).getChordname())) {
                            currentMatchResponse.addMatchingChord(bachChorales.get(i + subSetIndex));
                        }
                    }
                }

                if (longestMatchResponse == null || currentMatchResponse.getMatch().size() > longestMatchResponse.getMatch().size()) {
                    if(matchesListPrefix(currentMatchResponse.getMatch(), extractionSubset)){
                        longestMatchResponse = currentMatchResponse;
                    }
                }
            }

            String test = "";
        }

        return longestMatchResponse;
    }

    private boolean matchesListPrefix(List<BachChorale> currentMatchResponse, List<HarmonyExtraction> harmonyExtractions){

        for(int i=0; i<currentMatchResponse.size(); i++){
            if(currentMatchResponse.get(i) == null || harmonyExtractions.get(i) == null){
                return false;
            }

            if(!currentMatchResponse.get(i).getChord().equalsIgnoreCase(harmonyExtractions.get(i).getChordname())){
                return false;
            }
        }

        return true;
    }


//    public Map<ProgressionType, List<BachChorale>> createTypeToBachChorales(List<BachChorale> bachChorales, Map<ProgressionType, List<HarmonyExtraction>> typeToHarmonicExtractionMap) {
//
//        Map<ProgressionType, List<BachChorale>> typeToMatchBachChorales = new HashMap<>();
//
//        for (Map.Entry<ProgressionType, List<HarmonyExtraction>> typeMap : typeToHarmonicExtractionMap.entrySet()) {
//            ProgressionType type = typeMap.getKey();
//
//            int[] longestMatchAndStartIndex = findLongestMatchAndOffset(bachChorales, typeMap.getValue());
//
//
////            typeToMatchLengthAndOffset.put(type, longestMatchAndStartIndex);
//        }
//
//        return null;
//    }


//    //bachIndexToMatchLength
//    public Map<Integer, Integer> findLongestMatchAndOffset(List<BachChorale> bachChorales, List<HarmonyExtraction> extractions) {
//
//        int currentStartIndex=0;
//        int currentMatchLength=0;
//
//        //create extractions list starting at size 1
//        List<HarmonyExtraction> extractions = new ArrayList<>();
//    }

    /*
    public int[] findLongestMatchAndOffset(List<BachChorale> bachChorales, List<HarmonyExtraction> extractions) {
        int longestMatch = 0;
        int longestMatchIndexOffset = 0;

        for (int indexOffset = 0; indexOffset < extractions.size(); indexOffset++) {
            int currentLengthOfMatch = interateCollection(indexOffset, bachChorales, extractions);
            if (currentLengthOfMatch > longestMatch) {
                longestMatch = currentLengthOfMatch;
                longestMatchIndexOffset = indexOffset;
            }
        }

        return new int[]{longestMatch, longestMatchIndexOffset};
    }
    */

    public int interateCollection(int indexOffset, List<BachChorale> bachChorales, List<HarmonyExtraction> extractions) {

        int maxLength = 0;
        for (int i = 0; i < bachChorales.size(); i++) {
            if (extractions.size() > i + indexOffset) {
                if (extractions.get(i + indexOffset) != null) {
                    HiveChord bachChord = new HiveChord(bachChorales.get(i).getChord());
                    HiveChord extractionChord = new HiveChord(extractions.get(i + indexOffset).getChordname());

                    if (bachChord.isEqualOrTriadMatch(extractionChord)) {
                        maxLength++;
                    }
                }
            }
        }

        return maxLength;
    }
}
