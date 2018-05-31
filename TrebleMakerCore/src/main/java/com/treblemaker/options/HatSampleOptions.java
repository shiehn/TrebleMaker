package com.treblemaker.options;

import com.treblemaker.model.SourceData;
import com.treblemaker.model.hat.HatSample;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HatSampleOptions {

    public List<HatSample> getHatSampleOptions(int stationId, SourceData sourceData) {
        return sourceData.getHatSamples().stream().filter(hatSample -> hatSample.getStationId().equalsIgnoreCase(Integer.toString(stationId))).collect(Collectors.toList());
    }
}
