package com.treblemaker.extractors;

import com.treblemaker.extractors.model.HarmonyExtraction;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MelodicExtractor {

    public QueueState extractedAndSetHarmony(QueueState queueState) {
        for (ProgressionUnit pUnit : queueState.getStructure()) {
            for (int i = 0; i < pUnit.getProgressionUnitBars().size(); i++) {
                List<HarmonyExtraction> extractions = createHarmonicExtractionForBar(pUnit.getProgressionUnitBars().get(i));
                pUnit.getProgressionUnitBars().get(i).setHarmonicExtractions(extractions);
            }

        }

        return queueState;
    }

    public List<HarmonyExtraction> createHarmonicExtractionForBar(ProgressionUnitBar progressionUnitBar) {

        List<HarmonyExtraction> harmonyExtractions = new ArrayList<>();

        for (int noteIndex = 0; noteIndex < 16; noteIndex++) {
            harmonyExtractions.add(extractedAndSetHarmony(progressionUnitBar, noteIndex));
        }

        return harmonyExtractions;
    }

    public HarmonyExtraction extractedAndSetHarmony(ProgressionUnitBar progressionUnitBar, int noteIndex) {
        HarmonyExtraction harmonicExtraction = new HarmonyExtraction();
        harmonicExtraction.setChordname(progressionUnitBar.getChord().getChordName());

        List<String> harmonies = new ArrayList<>();
        if (!progressionUnitBar.getArpeggioLowPositions().get(noteIndex).equalsIgnoreCase("R")) {
            harmonies.add(progressionUnitBar.getArpeggioLowPositions().get(noteIndex));
        }

        if (!progressionUnitBar.getArpeggioHiPositions().get(noteIndex).equalsIgnoreCase("R")) {
            harmonies.add(progressionUnitBar.getArpeggioHiPositions().get(noteIndex));
        }

        //EXTRACT HARMONIC LOOPS ..
        HarmonicLoop hLoop = progressionUnitBar.getHarmonicLoop();
        String[] harmonicNotesAtIndex = hLoop.getPitchExtractions().get(getCurrentBarIndex(hLoop.getBarCount(), hLoop.getCurrentBar(), noteIndex)).getAsList().get(noteIndex);

        if(harmonicNotesAtIndex != null && arrayNotEmpty(harmonicNotesAtIndex)){
            harmonies.addAll(Arrays.asList(harmonicNotesAtIndex));
        }

        //EXTRAC HARMONIC LOOP ALT ..
        HarmonicLoop hAltLoop = progressionUnitBar.getHarmonicLoopAlt();
        String[] harmonicAltNotesAtIndex = hAltLoop.getPitchExtractions().get(getCurrentBarIndex(hAltLoop.getBarCount(),hAltLoop.getCurrentBar(), noteIndex)).getAsList().get(noteIndex);

        if(harmonicAltNotesAtIndex != null && arrayNotEmpty(harmonicAltNotesAtIndex)){
            harmonies.addAll(Arrays.asList(harmonicAltNotesAtIndex));
        }

        harmonicExtraction.setHarmonies(harmonies);

        return harmonicExtraction;
    }

    public int getCurrentBarIndex(int barCount, int currentBar, int index){
        switch (barCount){
            case 1:
                return 0;
            case 2:
                if(index < 4){
                    return 0;
                }else if(index >=4 && index <8){
                    return 1;
                }else if(index >=8 && index <12){
                    return 0;
                }else if(index >= 12 && index <16){
                    return 1;
                }else{
                    throw new RuntimeException("ERROR getCurrentBarIndex out of bound index : " + index);
                }
            case 4:
                if(index < 4){
                    return 0;
                }else if(index >=4 && index <8){
                    return 1;
                }else if(index >=8 && index <12){
                    return 2;
                }else if(index >= 12 && index <16){
                    return 3;
                }else{
                    throw new RuntimeException("ERROR getCurrentBarIndex out of bound index : " + index);
                }
            default:
                throw new RuntimeException("ERROR getCurrentBarIndex bar count - " + barCount);
        }
    }

    private boolean arrayNotEmpty(String[] extractions){
        return Arrays.asList(extractions).stream().filter(e -> e != "").findFirst().isPresent();
    }
}
