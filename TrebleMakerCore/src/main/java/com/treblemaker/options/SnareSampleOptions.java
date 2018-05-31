package com.treblemaker.options;

import com.treblemaker.model.SourceData;
import com.treblemaker.model.snare.SnareSample;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SnareSampleOptions {

    public List<SnareSample> getSnareSampleOptions(int stationId, SourceData sourceData) {
        return sourceData.getSnareSamples().stream().filter(snareSample -> snareSample.getStationId().equalsIgnoreCase(Integer.toString(stationId))).collect(Collectors.toList());
    }
}
