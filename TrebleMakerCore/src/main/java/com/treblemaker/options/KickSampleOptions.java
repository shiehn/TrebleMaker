package com.treblemaker.options;

import com.treblemaker.model.SourceData;
import com.treblemaker.model.kick.KickSample;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class KickSampleOptions {

    public List<KickSample> getKickSampleOptions(int stationId, SourceData sourceData) {
        return sourceData.getKickSamples().stream().filter(kickSample -> kickSample.getStationId().equalsIgnoreCase(Integer.toString(stationId))).collect(Collectors.toList());
    }
}
