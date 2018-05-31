package com.treblemaker.generators.interfaces;

import com.treblemaker.model.comp.CompRhythm;
import com.treblemaker.model.progressions.ProgressionUnit;

import java.util.List;
import java.util.Map;

public interface ICompGenerator {

    Map<ProgressionUnit.ProgressionType, List<CompRhythm>> weightCompOptions(List<ProgressionUnit> progressionUnits, Map<ProgressionUnit.ProgressionType, List<CompRhythm>> typeAndCompOptions);

    Map<ProgressionUnit.ProgressionType, CompRhythm> selectCompOptionsByWeight(Map<ProgressionUnit.ProgressionType, List<CompRhythm>> typeAndCompOptions);
}
