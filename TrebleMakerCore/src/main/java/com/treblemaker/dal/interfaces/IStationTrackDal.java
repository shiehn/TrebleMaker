package com.treblemaker.dal.interfaces;

import com.treblemaker.model.stations.StationTrack;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IStationTrackDal extends CrudRepository<StationTrack, Integer> {

    List<StationTrack> findAll();
}
