package com.treblemaker.generators.interfaces;

import com.treblemaker.model.SourceData;
import com.treblemaker.model.arpeggio.Arpeggio;
import com.treblemaker.model.bassline.Intervals;
import com.treblemaker.model.progressions.ProgressionUnit;

import java.util.List;
import java.util.Map;

public interface IArpeggioGenerator {

	List<Integer> generateTemplate(List<ProgressionUnit> progressionUnits, ProgressionUnit.ProgressionType unitType);

	Map<Integer, List<Arpeggio>> getArpeggioOptions(List<ProgressionUnit> progressionUnits, ProgressionUnit.ProgressionType unitType, SourceData sourceData);

    Map<Integer, Arpeggio> selectFromARateDistributedList(Map<Integer, List<Arpeggio>> weightedArpeggios);

	List<Arpeggio> distributeWeightedArpeggios(List<Arpeggio> weightedArpeggios);

    void weightArpeggioOptions(List<ProgressionUnit> progressionUnits, ProgressionUnit.ProgressionType unitType, Map<Integer, List<Arpeggio>> arpeggioMap);

	void setPatternWithTemplate(List<ProgressionUnit> progressionUnits, ProgressionUnit.ProgressionType unitType, Map<Integer, Arpeggio> selectedArpeggios, SourceData sourceData, Integer bpm);

	List<String> convertToNoteMap(int[] arpeggioList, Intervals intervalsA, Intervals intervalsB);
}
