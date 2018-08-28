package com.treblemaker.eventchain;

import com.treblemaker.dal.interfaces.IStationDal;
import com.treblemaker.dal.interfaces.IStationTrackDal;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.generators.StationTrackGenerator;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.stations.Station;
import com.treblemaker.model.stations.StationTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SetStationTrackEvent implements IEventChain {

    @Autowired
    private StationTrackGenerator stationTrackGenerator;

    @Autowired
    private IStationDal stationDal;

    @Autowired
    private IStationTrackDal stationTrackDal;

    @Value("${num_of_generated_mixes}")
    int numOfGeneratedMixes;

    @Value("${num_of_generated_mix_variations}")
    int numOfGeneratedMixVariations;

    @Value("${api.version}")
    int apiVersion;

    @Override
    public QueueState set(QueueState queueState) {

        //create track name
        String trackName = stationTrackGenerator.extractTrackName(queueState);

        Station station = stationDal.findOne(queueState.getQueueItem().getStationId());
        StationTrack stationTrack = stationTrackDal.findAll().stream().filter(st -> st.getFile().equalsIgnoreCase(queueState.getQueueItem().getQueueItemId())).collect(Collectors.toList()).get(0);

        stationTrack.setName(trackName);
        stationTrack.setApiVersion(apiVersion);
        stationTrack.setStationId(station.getId());
        stationTrack.setStation(station);
        stationTrackDal.save(stationTrack);

        /*
        Station station = stationDal.findOne(queueState.getQueueItem().getStationId());
        //insertTack

        StationTrack stationTrack = new StationTrack();
        stationTrack.setStationId(station.getId());
        stationTrack.setStation(station);
        stationTrack.setName(trackName);
        stationTrack.setFile(queueState.getQueueItem().getQueueItemId());
        stationTrack.setUploaded(0);
        stationTrack.setNumOfVersions(numOfGeneratedMixes);
        stationTrack.setNumOfVersionVariations(numOfGeneratedMixVariations);
        stationTrackDal.save(stationTrack);
        */


        return queueState;
    }
}
