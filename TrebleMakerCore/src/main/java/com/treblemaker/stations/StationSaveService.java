package com.treblemaker.stations;

import com.treblemaker.dal.interfaces.IStationTrackDal;
import com.treblemaker.model.stations.StationTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StationSaveService {

    @Autowired
    private IStationTrackDal stationTrackDal;

    public boolean save(String compositionId){
        List<StationTrack> stationTracks = stationTrackDal.findAll();

        for(StationTrack stationTrack : stationTracks){
            if(stationTrack.getFile().equalsIgnoreCase(compositionId)){
                return false;
            }
        }

        StationTrack stationTrack = new StationTrack();
        stationTrack.setName(compositionId);
        stationTrack.setUploaded(0);
        stationTrack.setStationId(1);
        stationTrack.setFile(compositionId);
        stationTrack.setAddToStation(0);
        stationTrack.setNumOfVersions(1);
        stationTrack.setNumOfVersionVariations(1);

        stationTrackDal.save(stationTrack);
        return true;
    }
}
