package com.treblemaker.generators.interfaces;

import com.treblemaker.model.HiveChord;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.progressions.ProgressionUnit;

import java.util.HashMap;
import java.util.List;

public interface IChordStructureGenerator {

    List<ProgressionUnit.ProgressionType> getProgressionTypes(QueueItem queueItem);

    QueueState set(QueueState queueState);

    boolean setChordThree(List<ProgressionUnit> progressionUnits, List<HiveChord> hiveChordInDatabase);

    boolean setChordTwo(List<HiveChord> hiveChordInDatabase, List<ProgressionUnit> progressionUnits);

    boolean setChordFour(List<HiveChord> hiveChordInDatabase, List<ProgressionUnit> progressionUnits);

    boolean setFirstChordInProgressions(List<ProgressionUnit> progressionUnits, HashMap<ProgressionUnit.ProgressionType, HiveChord> typeAndFirstChord);

    boolean set2nd3rd4thChordsFor4BarLoops(List<ProgressionUnit> progressionUnits, HashMap<ProgressionUnit.ProgressionType, List<HiveChord>> progressionTypeListHashMap);
}