package com.treblemaker.weighters.interfaces;

import com.treblemaker.model.analytics.AnalyticsHorizontal;
import com.treblemaker.model.progressions.ProgressionUnit;

import java.util.List;

public interface IWeighter {

    boolean setWeights(List<ProgressionUnit> progressionUnit, Iterable<AnalyticsHorizontal> analyticsHorizontals);

    boolean setWeight(Integer indexToWeight, ProgressionUnit currentProgressionUnit, ProgressionUnit priorProgressionUnit, Iterable<AnalyticsHorizontal> analyticsHorizontals);
}
