package com.treblemaker.services.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StationIdService {

    @Autowired
    @Value("${station_ids}") int[] stationIds;

    private Integer previousStationIndex;

    public int getStationId(){

        if(previousStationIndex == null || isLastIndex()){
            previousStationIndex = 0;
        }else{
            previousStationIndex = previousStationIndex + 1;
        }

        return stationIds[previousStationIndex];
    }

    private boolean isLastIndex(){
        return (previousStationIndex >= stationIds.length-1);
    }
}
