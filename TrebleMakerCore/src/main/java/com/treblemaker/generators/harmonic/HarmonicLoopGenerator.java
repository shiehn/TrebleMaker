package com.treblemaker.generators.harmonic;

import com.treblemaker.Application;
import com.treblemaker.generators.interfaces.IHarmonicLoopGenerator;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.ProcessingState;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.types.ProcessingPattern;
import com.treblemaker.options.OptionsFilter;
import com.treblemaker.options.interfaces.IHarmonicLoopOptions;
import com.treblemaker.selectors.helper.LoopSelectorHelper;
import com.treblemaker.selectors.interfaces.IChordSelector;
import com.treblemaker.selectors.interfaces.IHarmonicLoopSelector;
import com.treblemaker.weighters.harmonicloopweighters.HarmonicLoopWeighter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.treblemaker.constants.*;

import java.util.*;

import static com.treblemaker.logger.Log.LogProcedureState;
import static com.treblemaker.logger.Log.LoopGenerator;
import static com.treblemaker.model.types.Composition.Layer;

@Component
public class HarmonicLoopGenerator implements IHarmonicLoopGenerator {

    public enum Priority {
        ONE_A, ONE_B, ONE_C, TWO_A, TWO_B, TWO_C
    }

    private IHarmonicLoopSelector harmonicLoopSelector;
    private IChordSelector chordSelector;
    private HarmonicLoopWeighter harmonicLoopWeighter;
    private IHarmonicLoopOptions harmonicLoopOptions;
    private OptionsFilter optionsFilter;

    public HarmonicLoopGenerator(@Qualifier(value = "harmonicLoopSelector") IHarmonicLoopSelector harmonicLoopSelector,
                                 @Qualifier(value = "chordSelector") IChordSelector chordSelector,
                                 @Qualifier(value = "harmonicLoopWeighter") HarmonicLoopWeighter harmonicLoopWeighter,
                                 IHarmonicLoopOptions harmonicLoopOptions,
                                 OptionsFilter optionsFilter) {
        this.harmonicLoopSelector = harmonicLoopSelector;
        this.chordSelector = chordSelector;
        this.harmonicLoopWeighter = harmonicLoopWeighter;
        this.harmonicLoopOptions = harmonicLoopOptions;
        this.optionsFilter = optionsFilter;
    }

    @Override
    public QueueState setHarmonicLoops(QueueState queueState, Layer layerType) {

        List<ProgressionUnit> progressionUnits;

        LogProcedureState(queueState.getProcessingState().getCurrentState(), LoopGenerator);

        switch (queueState.getProcessingState().getCurrentState()) {

            case C1_L1:

                int maxBarCount = 4;

                progressionUnits = queueState.getStructure();

                for (int i = 0; i < progressionUnits.size(); i++) {
                    //find loops that match chord one ..
                    List<HiveChord> chordCandidates = Arrays.asList(progressionUnits.get(i).getProgressionUnitBars().get(ChordPositions.CHORD_ONE).getChord());
                    List<HarmonicLoop> loopOptions = harmonicLoopSelector.selectLoopsFromChords(chordCandidates, chordCandidates.get(0), queueState.getDataSource().getHarmonicLoops(queueState.getQueueItem().getStationId()), queueState.getQueueItem().getBpm(), maxBarCount);

                    if (loopOptions == null) {
                        queueState.setProceed(false);
                        break;
                    }

                    //ENSURE LOOP IS 4 BARS
                    List<HarmonicLoop> filteredOptions = optionsFilter.filterByProcessingState(loopOptions, queueState.getProcessingState());

                    if (filteredOptions == null || filteredOptions.isEmpty()) {
                        queueState.setProceed(false);
                        break;
                    }

                    //TODO REMOVE RANDOM SELECTION ..
                    HarmonicLoop harmonicLoop = filteredOptions.get(new Random().nextInt(filteredOptions.size()));

                    //SET LOOP

                    HarmonicLoop loopOne = null;
                    HarmonicLoop loopTwo = null;
                    HarmonicLoop loopThree = null;
                    HarmonicLoop loopFour = null;

                    try {
                        loopOne = harmonicLoop.clone();
                        loopTwo = harmonicLoop.clone();
                        loopThree = harmonicLoop.clone();
                        loopFour = harmonicLoop.clone();
                    } catch (CloneNotSupportedException e) {
                        Application.logger.debug("LOG:", e);
                    }

                    loopOne.setCurrentBar(1);
                    progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_ONE).setHarmonicLoop(loopOne);

                    loopTwo.setCurrentBar(2);
                    progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_TWO).setHarmonicLoop(loopTwo);

                    loopThree.setCurrentBar(3);
                    progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_THREE).setHarmonicLoop(loopThree);

                    loopFour.setCurrentBar(4);
                    progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).setHarmonicLoop(loopFour);
                }

                updateAlgoritmState(queueState);

                break;
            case C2_C3_C4:
                updateAlgoritmState(queueState);

                break;
            case C1_L3:

                maxBarCount = 2;

                progressionUnits = queueState.getStructure();

                for (int i = 0; i < progressionUnits.size(); i++) {

                    ProgressionUnit nextProgression = null;
                    if (i + 1 < progressionUnits.size()) {
                        nextProgression = progressionUnits.get(i + 1);
                    }

                    List<HiveChord> chordCandidates;
                    HiveChord bestChordSelection;

                    if (nextProgression != null) {
                        chordCandidates = chordSelector.getChordCandidatesTwoBarsPrior(nextProgression.getProgressionUnitBars().get(ChordPositions.CHORD_ONE).getChord(), queueState.getDataSource().getHiveChordInDatabase());
                        if (chordCandidates == null) {
                            queueState.setProceed(false);
                            break;
                        }

                        bestChordSelection = chordSelector.getChordTwoBarsPrior(nextProgression.getProgressionUnitBars().get(ChordPositions.CHORD_ONE).getChord(), queueState.getDataSource().getHiveChordInDatabase());
                        if (bestChordSelection == null) {
                            queueState.setProceed(false);
                            break;
                        }

                    } else {
                        //TODO assume you are just progressing to yourself???
                        chordCandidates = chordSelector.getChordCandidatesTwoBarsPrior(progressionUnits.get(i).getProgressionUnitBars().get(ChordPositions.CHORD_ONE).getChord(), queueState.getDataSource().getHiveChordInDatabase());
                        if (chordCandidates == null) {
                            queueState.setProceed(false);
                            break;
                        }
                        bestChordSelection = chordSelector.getChordTwoBarsPrior(progressionUnits.get(i).getProgressionUnitBars().get(ChordPositions.CHORD_ONE).getChord(), queueState.getDataSource().getHiveChordInDatabase());
                        if (bestChordSelection == null) {
                            queueState.setProceed(false);
                            break;
                        }
                    }

                    List<HarmonicLoop> loopOptions = harmonicLoopSelector.selectLoopsFromChords(chordCandidates, bestChordSelection, queueState.getDataSource().getHarmonicLoops(queueState.getQueueItem().getStationId()), queueState.getQueueItem().getBpm(), maxBarCount);
                    if (loopOptions == null || loopOptions.isEmpty()) {
                        queueState.setProceed(false);
                        break;
                    }

                    progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_THREE).setHarmonicLoopOptions(loopOptions);

                    harmonicLoopWeighter.setHarmonicLoopWeight(LoopPositions.POSITION_THREE, progressionUnits.get(i), null, null);

                    HarmonicLoop harmonicLoop = LoopSelectorHelper.makeWeightedSelectionFromHarmonicLoops(progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_THREE).getHarmonicLoopOptions(), optionsFilter, queueState.getProcessingState());

                    //SET LOOP
                    if (harmonicLoop.getBarCount() == 1) {

                        harmonicLoop.setCurrentBar(3);
                        progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_THREE).setHarmonicLoop(harmonicLoop);
                        progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).setHarmonicLoop(null);
                    } else if (harmonicLoop.getBarCount() == 2) {

                        HarmonicLoop loopThree = null;
                        HarmonicLoop loopFour = null;

                        try {

                            loopThree = harmonicLoop.clone();
                            loopThree.setCurrentBar(3);

                            loopFour = harmonicLoop.clone();
                            loopFour.setCurrentBar(4);

                        } catch (CloneNotSupportedException e) {
                            Application.logger.debug("LOG:", e);
                        }

                        progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_THREE).setHarmonicLoop(loopThree);
                        progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).setHarmonicLoop(loopFour);
                    }
                }

                //TODO you need to check if it failed somehow .. its assuming everything is ok ..
                updateAlgoritmState(queueState);

                break;
            case C3_L1:

                //get the first chord

                //get the loop options based on chord one ..

                int count = 1;

                progressionUnits = queueState.getStructure();

                for (int i = 0; i < progressionUnits.size(); i++) {

                    ProgressionUnit currentProgression = progressionUnits.get(i);
                    HiveChord currentChord = currentProgression.getProgressionUnitBars().get(ChordPositions.CHORD_ONE).getChord();

                    ProgressionUnit priorProgression = null;
                    if(i>0){
                        priorProgression = progressionUnits.get(i-1);
                    }

                    harmonicLoopOptions.setHarmonicLoopOptions(currentProgression.getProgressionUnitBars().get(ChordPositions.CHORD_ONE), queueState.getDataSource().getHarmonicLoops(queueState.getQueueItem().getStationId()), queueState.getProcessingState());

                    harmonicLoopWeighter.setHarmonicLoopWeight(LoopPositions.POSITION_ONE, currentProgression, priorProgression, null);

                    HarmonicLoop selectedLoopOption = LoopSelectorHelper.makeWeightedSelectionFromHarmonicLoops(currentProgression.getProgressionUnitBars().get(ChordPositions.CHORD_ONE).getHarmonicLoopOptions(), optionsFilter, queueState.getProcessingState());
                    //HarmonicLoop selectedLoopOption = harmonicLoopSelector.selectLoopFromChord(currentChord, currentProgression.getProgressionUnitBars().get(ChordPositions.CHORD_ONE).getHarmonicLoopOptions());

                    if (selectedLoopOption == null) {
                        queueState.setProceed(false);
                        break;
                    }

                    int barCount = selectedLoopOption.getBarCount();

                    if (barCount == 1) {
                        //TODO HANDLE ONE BAR LOOPS ..
                        selectedLoopOption.setCurrentBar(1);
                        progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_ONE).setHarmonicLoop(selectedLoopOption);
                    } else if (barCount == 2) {
                        //TODO HANDLE TWO BAR LOOPS ..

                        HarmonicLoop loopOne = null;
                        HarmonicLoop loopTwo = null;

                        try {
                            loopOne = selectedLoopOption.clone();
                            loopOne.setCurrentBar(1);

                            loopTwo = selectedLoopOption.clone();
                            loopTwo.setCurrentBar(2);
                        } catch (CloneNotSupportedException e) {
                            Application.logger.debug("LOG:", e);
                        }

                        progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_ONE).setHarmonicLoop(loopOne);
                        progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_TWO).setHarmonicLoop(loopTwo);
                    } else if (barCount == 4)  {
                        //TODO HANDLE FOUR BAR LOOPS ..

                        HarmonicLoop loopOne = null;
                        HarmonicLoop loopTwo = null;
                        HarmonicLoop loopThree = null;
                        HarmonicLoop loopFour = null;

                        try {
                            loopOne = selectedLoopOption.clone();
                            loopOne.setCurrentBar(1);

                            loopTwo = selectedLoopOption.clone();
                            loopTwo.setCurrentBar(2);

                            loopThree = selectedLoopOption.clone();
                            loopThree.setCurrentBar(3);

                            loopFour = selectedLoopOption.clone();
                            loopFour.setCurrentBar(4);
                        } catch (CloneNotSupportedException e) {
                            Application.logger.debug("LOG:", e);
                        }

                        progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_ONE).setHarmonicLoop(loopOne);
                        progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_TWO).setHarmonicLoop(loopTwo);
                        progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_THREE).setHarmonicLoop(loopThree);
                        progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).setHarmonicLoop(loopFour);
                    }

                    Application.logger.debug("LOG:","SelectFirstLoop Success : " + count + "/" + progressionUnits.size());

                    //TODO REMOVE JUST FOR DIAGONSTICS START ..
                    if (progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_ONE).getHarmonicLoop() == null ||
                            !(progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_ONE).getHarmonicLoop().getBarCount() == 1 ||
                                    progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_ONE).getHarmonicLoop().getBarCount() == 2)) {
                        String Error = "HERE!!!";
                    }
                    //TODO REMOVE JUST FOR DIAGONSTICS END ..


                    count++;
                }

                //TODO you need to check if it failed somehow .. its assuming everything is ok ..
                updateAlgoritmState(queueState);
                break;
            case C2_L4:

                Map<ProgressionUnit.ProgressionType, String> typeToFourthChord = new HashMap<>();

                progressionUnits = queueState.getStructure();

                for (int i = 0; i < progressionUnits.size(); i++) {

                    if (progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getHarmonicLoop() == null) {

                        //get 3rd chord ..
                        HiveChord thirdChord = progressionUnits.get(i).getProgressionUnitBars().get(ChordPositions.CHORD_THREE).getChord();

                        HiveChord nextProgressionChord = progressionUnits.get(i).getProgressionUnitBars().get(ChordPositions.CHORD_ONE).getChord();
                        if ((i + 1) < progressionUnits.size()) {
                            nextProgressionChord = progressionUnits.get(i + 1).getProgressionUnitBars().get(ChordPositions.CHORD_ONE).getChord();
                        }

                        //TODO SHOULD't this be a list of possible chords?????

                        List<HiveChord> candidates = chordSelector.getChordCandidatesBetweenChords(thirdChord, nextProgressionChord, queueState.getDataSource().getHiveChordInDatabase());
                        if (candidates == null) {
                            queueState.setProceed(false);
                            break;
                        }

                        HiveChord bestCandidate = chordSelector.selectChordBetweenChords(thirdChord, nextProgressionChord, queueState.getDataSource().getHiveChordInDatabase());
                        if (bestCandidate == null) {
                            queueState.setProceed(false);
                            break;
                        }

                        final int maxBarLength = 1;
                        List<HarmonicLoop> loopOptions = harmonicLoopSelector.selectLoopsFromChords(candidates, bestCandidate, queueState.getDataSource().getHarmonicLoops(queueState.getQueueItem().getStationId()), queueState.getQueueItem().getBpm(), maxBarLength);
                        if (loopOptions == null || loopOptions.isEmpty()) {
                            queueState.setProceed(false);
                            break;
                        }

                        //filter here
                        loopOptions = optionsFilter.filterByProcessingState(loopOptions, queueState.getProcessingState());

                        progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).setHarmonicLoopOptions(loopOptions);

                        harmonicLoopWeighter.setHarmonicLoopWeight(LoopPositions.POSITION_FOUR, progressionUnits.get(i), null, null);

                        HarmonicLoop harmonicLoop = LoopSelectorHelper.makeWeightedSelectionFromHarmonicLoops(progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getHarmonicLoopOptions(), optionsFilter, queueState.getProcessingState());

                        //SET HARMONIC LOOP
                        harmonicLoop.setCurrentBar(4);
                        progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).setHarmonicLoop(harmonicLoop);
                    }
                }

                //TODO you need to check if it failed somehow .. its assuming everything is ok ..
                updateAlgoritmState(queueState);
                break;
            case C4_L2:

                //setHarmonicLoops loop two
                //TODO check if loop one is 1 or 2 bars ..
                progressionUnits = queueState.getStructure();

                for (int i = 0; i < progressionUnits.size(); i++) {

                    ProgressionUnit currentProgression = progressionUnits.get(i);

                    //FIRST FIND OUT IF POSITION TWO IS ALREADY SET ..
                    if (currentProgression.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).getHarmonicLoop() == null) {

                        HiveChord currentChord = currentProgression.getProgressionUnitBars().get(1).getChord();

                        final int maxBarLength = 1;
                        List<HarmonicLoop> loopOptions = LoopSelectorHelper.filterChordCompatibleHarmonicLoops(queueState.getDataSource().getHarmonicLoops(queueState.getQueueItem().getStationId()), currentChord, maxBarLength);

                        currentProgression.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).setHarmonicLoopOptions(loopOptions);

                        harmonicLoopWeighter.setHarmonicLoopWeight(LoopPositions.POSITION_TWO, currentProgression, null, null);

                        HarmonicLoop harmonicLoop = LoopSelectorHelper.makeWeightedSelectionFromHarmonicLoops(currentProgression.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).getHarmonicLoopOptions(), optionsFilter, queueState.getProcessingState());

                        if (harmonicLoop == null) {
                            queueState.setProceed(false);
                            break;
                        }

                        //SET HARMONIC LOOP
                        harmonicLoop.setCurrentBar(2);
                        currentProgression.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).setHarmonicLoop(harmonicLoop);
                    }


                    //TODO REMOVE JUST FOR DIAGONSTICS START ..
                    if (progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_TWO).getHarmonicLoop() == null ||
                            !(progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getHarmonicLoop().getBarCount() == 1 ||
                                    progressionUnits.get(i).getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getHarmonicLoop().getBarCount() == 2)) {
                        String Error = "HERE!!!";
                    }
                    //TODO REMOVE JUST FOR DIAGONSTICS END ..
                }

                //TODO you need to check if it failed somehow .. its assuming everything is ok ..
                updateAlgoritmState(queueState);
                break;
        }

        return queueState;
    }

    private void updateAlgoritmState(QueueState queueState) {

        final boolean[] restartAlgoritm = {false};

        if(queueState.getProcessingState().getProcessingPattern().equals(ProcessingPattern.FOUR_BAR)){
            switch (queueState.getProcessingState().getCurrentState()) {
                case C1_L1:
                    queueState.getStructure().forEach(progressionUnit -> {
                        if (progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE).getHarmonicLoop() == null
                                || progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).getHarmonicLoop() == null
                                || progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).getHarmonicLoop() == null
                                || progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getHarmonicLoop() == null) {
                            restartAlgoritm[0] = true;
                            return;
                        }
                    });
                    break;
                case C2_C3_C4:
                    queueState.getStructure().forEach(progressionUnit -> {
                        if (progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE).getHarmonicLoop() == null) {
                            restartAlgoritm[0] = true;
                            return;
                        }
                    });
                    break;
            }
        }else if(queueState.getProcessingState().getProcessingPattern().equals(ProcessingPattern.TWO_BAR)){
            switch (queueState.getProcessingState().getCurrentState()) {
                case C1_L3:
                    queueState.getStructure().forEach(progressionUnit -> {
                        if (progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).getHarmonicLoop() == null) {
                            restartAlgoritm[0] = true;
                            return;
                        }
                    });
                    break;
                case C3_L1:
                    queueState.getStructure().forEach(progressionUnit -> {
                        if (progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE).getHarmonicLoop() == null) {
                            restartAlgoritm[0] = true;
                            return;
                        }
                    });
                    break;
                case C2_L4:
                    queueState.getStructure().forEach(progressionUnit -> {
                        if (progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getHarmonicLoop() == null) {
                            restartAlgoritm[0] = true;
                            return;
                        }
                    });
                    break;
                case C4_L2:
                    queueState.getStructure().forEach(progressionUnit -> {
                        if (progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).getHarmonicLoop() == null) {
                            restartAlgoritm[0] = true;
                            return;
                        }
                    });
                    break;
            }
        } else {
            new RuntimeException("No ProcessingPatternState set");
        }

        if (restartAlgoritm[0]) {
            //TODO COUNT RESTARTS .. COULD CREATE INFINITE LOOP ..
            if(queueState.getProcessingState().getProcessingPattern().equals(ProcessingPattern.FOUR_BAR)) {
                queueState.getProcessingState().setCurrentState(ProcessingState.ProcessingStates.C1_L3);
                return;
            }else if(queueState.getProcessingState().getProcessingPattern().equals(ProcessingPattern.TWO_BAR)) {
                queueState.getProcessingState().setCurrentState(ProcessingState.ProcessingStates.C1_L1);
                return;
            }else{
                new RuntimeException("No ProcessingPatternState set");
            }
        }

        if(queueState.getProcessingState().getProcessingPattern().equals(ProcessingPattern.FOUR_BAR)){
            switch (queueState.getProcessingState().getCurrentState()) {
                case C1_L1:
                    queueState.getProcessingState().setCurrentState(ProcessingState.ProcessingStates.C2_C3_C4);
                    break;
                case C2_C3_C4:
                    queueState.getProcessingState().setCurrentState(ProcessingState.ProcessingStates.COMPLETE);
                    break;
                default:
                    new RuntimeException("No FOUR_BAR ProcessingState set");
            }
        }else if(queueState.getProcessingState().getProcessingPattern().equals(ProcessingPattern.TWO_BAR)) {
            switch (queueState.getProcessingState().getCurrentState()) {
                case C1_L3:
                    queueState.getProcessingState().setCurrentState(ProcessingState.ProcessingStates.C3_L1);
                    break;
                case C3_L1:
                    queueState.getProcessingState().setCurrentState(ProcessingState.ProcessingStates.C2_L4);
                    break;
                case C2_L4:
                    queueState.getProcessingState().setCurrentState(ProcessingState.ProcessingStates.C4_L2);
                    break;
                case C4_L2:
                    queueState.getProcessingState().setCurrentState(ProcessingState.ProcessingStates.COMPLETE);
                    break;
                default:
                    new RuntimeException("No TWO_BAR ProcessingState set");
            }
        }else{
            new RuntimeException("No ProcessingPatternState set");
        }
    }

    private Map<Priority, List<HarmonicLoop>> distributePriorityWeights(Map<Priority, List<HarmonicLoop>> priorityLoopMap) {

        Map<Priority, List<HarmonicLoop>> distributedLoopMap = new HashMap<>();

        //if one_a x6

        //if one_b x5

        //if one_c x4

        //if two_a x3

        //if two_b x2

        //if two_c x2

        return distributedLoopMap;
    }

    private Map<ProgressionUnit.ProgressionType, List<List<HiveChord>>> getProgressions(QueueItem queueItem) {

        Map<ProgressionUnit.ProgressionType, List<List<HiveChord>>> progressions = new HashMap<>();

        List<ProgressionUnit> progressionUnits = queueItem.getProgression().getStructure();

        //List<ProgressionUnit.ProgressionType> progressionTypes = getProgressionTypes(progressionUnits);

        for (ProgressionUnit progressionUnit : progressionUnits) {

            List<List<HiveChord>> progressionLists = progressions.get(progressionUnit.getType());

            if (progressionLists == null) {
                progressionLists = new ArrayList<>();
            }

            boolean containsList = false;

            List<HiveChord> hiveChordInProgressionUnit = new ArrayList<>();
            progressionUnit.getProgressionUnitBars().forEach(pBar -> {
                hiveChordInProgressionUnit.add(pBar.getChord());
            });

            for (List<HiveChord> progressionList : progressionLists) {

                if (progressionList.equals(hiveChordInProgressionUnit)) {
                    containsList = true;
                }
            }

            if (!containsList) {
                progressionLists.add(hiveChordInProgressionUnit);
            }

            progressions.put(progressionUnit.getType(), progressionLists);
        }

        return progressions;
    }

    public List<ProgressionUnit.ProgressionType> getProgressionTypes(List<ProgressionUnit> progressionUnits) {

        List<ProgressionUnit.ProgressionType> progressionTypes = new ArrayList<>();

        for (ProgressionUnit progressionUnit : progressionUnits) {
            if (!progressionTypes.contains(progressionUnit.getType())) {
                progressionTypes.add(progressionUnit.getType());
            }
        }

        return progressionTypes;
    }

    @Override
    public int getMaxBarCount(ProgressionUnit pUnit, int currentBarIndex) {
        return 0;
    }

    @Override
    public void setHarmonicLoop(HarmonicLoop harmonicLoop, int barIndex, List<ProgressionUnitBar> progressionUnitBars) {

    }
}
