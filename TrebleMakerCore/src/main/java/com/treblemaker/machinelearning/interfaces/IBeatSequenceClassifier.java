package com.treblemaker.machinelearning.interfaces;

import com.treblemaker.model.analytics.AnalyticsHorizontal;
import com.treblemaker.model.interfaces.IWeightable;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.weighters.enums.WeightClass;

public interface IBeatSequenceClassifier {

    WeightClass classify(IWeightable weightable, ProgressionUnitBar currentBar, ProgressionUnitBar oneBarPrior, ProgressionUnitBar secondBarPrior, Iterable<AnalyticsHorizontal> analyticsHorizontals);

    int calculateRating(IWeightable weightable, ProgressionUnitBar currentBar, ProgressionUnitBar oneBarPrior, ProgressionUnitBar secondBarPrior, Iterable<AnalyticsHorizontal> analyticsHorizontals);
}
