package com.treblemaker.machinelearning.interfaces;

import com.treblemaker.model.interfaces.IWeightable;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.weighters.enums.WeightClass;


public interface IVerticalClassifier {

    WeightClass classify(ProgressionUnitBar progressionUnitBar, IWeightable weightable);

    WeightClass classify(String neuralRequest);
}
