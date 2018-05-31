package com.treblemaker.weighters.timeseriesweighters;

import com.treblemaker.weighters.enums.WeightClass;
import com.treblemaker.weighters.interfaces.ITimeSeriesWeighter;
import com.treblemaker.model.progressions.ProgressionUnit;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HarmonicTimeSeriesWeighter implements ITimeSeriesWeighter {

    @Override
    public WeightClass getTimeSeriesWeight(List<ProgressionUnit> progressionUnits) {
        return null;
    }
}
