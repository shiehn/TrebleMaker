package com.treblemaker.weighters.helper;

import com.treblemaker.model.fx.FXArpeggioWithRating;
import com.treblemaker.model.interfaces.IWeightableLoop;

import java.util.ArrayList;
import java.util.List;

public class WeightDistributor {

    public static List<IWeightableLoop> distributeIWeightableListByWeights(List<? extends IWeightableLoop> weightables) {

        List<IWeightableLoop> distributedWeightables = new ArrayList<>();

        weightables.forEach(weightable -> {

            if (weightable.getTotalWeight() < 1) {
                distributedWeightables.add(weightable);
            } else {
                for (int i = 0; i < weightable.getTotalWeight(); i++) {
                    distributedWeightables.add(weightable);
                }
            }
        });

        return distributedWeightables;
    }

    public static List<FXArpeggioWithRating> distributeFXListByWeights(List<FXArpeggioWithRating> weightables) {

        List<FXArpeggioWithRating> distributedWeightables = new ArrayList<>();

        weightables.forEach(weightable -> {

            if (weightable.getTotalWeight() < 1) {
                distributedWeightables.add(weightable);
            } else {
                for (int i = 0; i < weightable.getTotalWeight(); i++) {
                    distributedWeightables.add(weightable);
                }
            }
        });

        return distributedWeightables;
    }
}
