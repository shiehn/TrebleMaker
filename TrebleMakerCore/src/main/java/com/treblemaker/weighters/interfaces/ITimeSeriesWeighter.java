package com.treblemaker.weighters.interfaces;

import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.weighters.enums.WeightClass;

import java.util.List;

public interface ITimeSeriesWeighter {

    WeightClass getTimeSeriesWeight(List<ProgressionUnit> progressionUnits);
}
