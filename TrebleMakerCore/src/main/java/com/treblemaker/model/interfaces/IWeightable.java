package com.treblemaker.model.interfaces;


import com.treblemaker.weighters.enums.WeightClass;

public interface IWeightable {

    Integer getId();

    void setId(Integer id);

    void setFileName(String fileName);

    String getFileName();

    WeightClass getVerticalWeight();

    void setVerticalWeight(WeightClass weight);

    WeightClass getTimeseriesWeight();

    void setTimeseriesWeight(WeightClass weight);

    WeightClass getThemeWeight();

    void setThemeWeight(WeightClass weight);

    void setRhythmicWeight(WeightClass weight);

    WeightClass getRhythmicWeight();

    WeightClass getEqWeight();

    void setEqWeight(WeightClass weight);

    Integer getTotalWeight();
}
