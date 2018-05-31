package com.treblemaker.generators;

import com.treblemaker.Application;
import com.treblemaker.dal.interfaces.IChordSequencesDal;
import com.treblemaker.generators.interfaces.IChordStructureGenerator;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnit.ProgressionType;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.selectors.interfaces.IChordSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.treblemaker.constants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.treblemaker.logger.Log.HarmonicStructureGenerator;
import static com.treblemaker.logger.Log.LogProcedureState;

@Component
public class ChordStructureGenerator implements IChordStructureGenerator {

    @Autowired
    private IChordSelector chordSelector;

    @Autowired
    private IChordSequencesDal chordSequencesDal;

    public List<ProgressionType> getProgressionTypes(QueueItem queueItem) {

        List<ProgressionType> progressionTypes = new ArrayList<ProgressionType>();

        for (ProgressionUnit progressionUnit : queueItem.getProgression().getStructure()) {

            if (!progressionTypes.contains(progressionUnit.getType())) {
                progressionTypes.add(progressionUnit.getType());
            }
        }

        return progressionTypes;
    }


    public QueueState set(QueueState queueState) {

        Application.logger.debug("LOG: $$$ THREAD ID chord generate : " + Thread.currentThread().getId());

        List<ProgressionType> progressionTypes = getProgressionTypes(queueState.getQueueItem());
        LogProcedureState(queueState.getProcessingState().getCurrentState(), HarmonicStructureGenerator);

        switch (queueState.getProcessingState().getProcessingPattern()){
            case TWO_BAR:
                switch (queueState.getProcessingState().getCurrentState()) {
                    case C1_L3:
                        HashMap<ProgressionType, HiveChord> typeAndFirstChord = chordSelector.selectFirstChordForType(progressionTypes, queueState.getDataSource().getHiveChordInDatabase());
                        queueState.setProceed(setFirstChordInProgressions(queueState.getStructure(), typeAndFirstChord));
                        break;
                    case C3_L1:
                        // Now Set The Middle (3)Chord for each Set ..
                        queueState.setProceed(setChordThree(queueState.getStructure(), queueState.getDataSource().getHiveChordInDatabase()));
                        break;
                    case C2_L4:
                        // 7) Now Set the 2nd chords ..
                        queueState.setProceed(setChordTwo(queueState.getDataSource().getHiveChordInDatabase(), queueState.getStructure()));
                        break;
                    case C4_L2:
                        // 8) Now Set the Last chords ..
                        queueState.setProceed(setChordFour(queueState.getDataSource().getHiveChordInDatabase(), queueState.getStructure()));
                        break;
                }
                break;
            case FOUR_BAR:
                switch (queueState.getProcessingState().getCurrentState()) {
                    case C1_L1:
                        HashMap<ProgressionType, HiveChord> typeAndFirstChord = chordSelector.selectFirstChordForType(progressionTypes, queueState.getDataSource().getHiveChordInDatabase());
                        queueState.setProceed(setFirstChordInProgressions(queueState.getStructure(), typeAndFirstChord));
                        break;
                    case C2_C3_C4:
                        HashMap<ProgressionType, List<HiveChord>> progressionTypeListHashMap = chordSelector.selectChordsFrom4BarLoop(queueState.getStructure());
                        set2nd3rd4thChordsFor4BarLoops(queueState.getStructure(), progressionTypeListHashMap);
                    default:
                        break;
                }
                break;
            default:
                break;
        }

        return queueState;
    }

    public boolean setChordThree(List<ProgressionUnit> progressionUnits, List<HiveChord> hiveChordInDatabase) {

        for (int i = 0; i < progressionUnits.size(); i++) {

            //HarmonicLoop selectedLoop, String chordTwoBarsPrior, String chordTwoBarsForward

            HiveChord nextProgressionChord;

            if (i + 1 == progressionUnits.size()) {
                nextProgressionChord = progressionUnits.get(i).getProgressionUnitBars().get(ChordPositions.CHORD_ONE).getChord();
            } else {
                nextProgressionChord = progressionUnits.get(i + 1).getProgressionUnitBars().get(ChordPositions.CHORD_ONE).getChord();
            }

            HiveChord middleChord = chordSelector.selectMiddleChord(progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_THREE).getHarmonicLoop(), progressionUnits.get(i).getProgressionUnitBars().get(ChordPositions.CHORD_ONE).getChord(), nextProgressionChord, hiveChordInDatabase);

            if(middleChord == null || middleChord.getRawChordName().equalsIgnoreCase("_")){
                return false;
            }

            progressionUnits.get(i).getProgressionUnitBars().get(ChordPositions.CHORD_THREE).setChord(middleChord);
        }

        return true;
    }

    public boolean setChordTwo(List<HiveChord> hiveChordInDatabase, List<ProgressionUnit> progressionUnits) {

        HashMap<ProgressionType, HiveChord> typeToChordMap = new HashMap<>();

        for (ProgressionUnit progression : progressionUnits) {

            if (typeToChordMap.get(progression.getType()) == null) {

                if (typeToChordMap.get(progression.getType()) == null || typeToChordMap.get(progression.getType()).isEqual("")) {

                    HiveChord secondChord;
                    //IF LOOP TWO IS SET ENSURE ITS COMPATIBLE
                    if(progression.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).getHarmonicLoop() != null){
                        secondChord = chordSelector.selectChordBetweenChordsAndLoop(progression.getProgressionUnitBars().get(ChordPositions.CHORD_ONE).getChord(), progression.getProgressionUnitBars().get(ChordPositions.CHORD_THREE).getChord(), progression.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).getHarmonicLoop(), hiveChordInDatabase);
                    }else {
                        secondChord = chordSelector.selectChordBetweenChords(progression.getProgressionUnitBars().get(ChordPositions.CHORD_ONE).getChord(), progression.getProgressionUnitBars().get(ChordPositions.CHORD_THREE).getChord(), hiveChordInDatabase);
                    }

                    if(secondChord == null || secondChord.getRawChordName().equalsIgnoreCase("_")){
                        return false;
                    }

                    typeToChordMap.put(progression.getType(), secondChord);
                }
            }

            progression.getProgressionUnitBars().get(ChordPositions.CHORD_TWO).setChord(typeToChordMap.get(progression.getType()));
        }

        return true;
    }

    public boolean setChordFour(List<HiveChord> hiveChordInDatabase, List<ProgressionUnit> progressionUnits) {

        HashMap<ProgressionType, HiveChord> typeToLoopMap = new HashMap<>();

        for (int i=0; i<progressionUnits.size(); i++) {

            ProgressionUnit progression = progressionUnits.get(i);

            if (typeToLoopMap.get(progression.getType()) == null || typeToLoopMap.get(progression.getType()).isEqual("")) {
                HarmonicLoop fourthLoop = progression.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getHarmonicLoop();

                //IF THE 3rd loop covers 2 bars then harminize with it ..
                if (fourthLoop == null) {
                    fourthLoop = progression.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).getHarmonicLoop();
                }

                HiveChord chordThree = progression.getProgressionUnitBars().get(ChordPositions.CHORD_THREE).getChord();
                HiveChord chordFromNextProgression;

                if(i + 1 < progressionUnits.size()){
                    chordFromNextProgression = progressionUnits.get(i+1).getProgressionUnitBars().get(ChordPositions.CHORD_ONE).getChord();
                }else{
                    chordFromNextProgression = progression.getProgressionUnitBars().get(ChordPositions.CHORD_THREE).getChord();
                }

                HiveChord fourthChord = chordSelector.selectFourthChord(fourthLoop, chordThree, chordFromNextProgression, hiveChordInDatabase);

                if(fourthChord == null || fourthChord.getRawChordName().equalsIgnoreCase("_")){
                    return false;
                }

                typeToLoopMap.put(progression.getType(), fourthChord);
            }

            progression.getProgressionUnitBars().get(ChordPositions.CHORD_FOUR).setChord(typeToLoopMap.get(progression.getType()));
        }

        return true;
    }

    public boolean set2nd3rd4thChordsFor4BarLoops(List<ProgressionUnit> progressionUnits, HashMap<ProgressionType, List<HiveChord>> progressionTypeListHashMap) {

        for (ProgressionUnit progressionUnit : progressionUnits) {

            List<HiveChord> hiveChords = progressionTypeListHashMap.get(progressionUnit.getType());

            progressionUnit.getProgressionUnitBars().get(0).setChord(hiveChords.get(0));
            progressionUnit.getProgressionUnitBars().get(1).setChord(hiveChords.get(1));
            progressionUnit.getProgressionUnitBars().get(2).setChord(hiveChords.get(2));
            progressionUnit.getProgressionUnitBars().get(3).setChord(hiveChords.get(3));
        }

        return true;
    }


    public boolean setFirstChordInProgressions(List<ProgressionUnit> progressionUnits, HashMap<ProgressionType, HiveChord> typeAndFirstChord) {

        String attemptString = "";

        for (ProgressionUnit progression : progressionUnits) {

            //SET CHORD
            HiveChord chord = typeAndFirstChord.get(progression.getType());

            if(chord == null){
                return false;
            }

            progression.getProgressionUnitBars().get(ChordPositions.CHORD_ONE).setChord(chord);

            attemptString += chord.getChordName();

            //SET ANALYTICS
            //progression.getCompositionTimeSlots().get(ChordPositions.CHORD_ONE).setChordId(typeAndFirstChord.get(progression.getType()));
        }

        Application.logger.debug("LOG: attemptString :" + attemptString);

        return true;
    }
}