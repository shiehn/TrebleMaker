package com.treblemaker.dal.interfaces;

import com.treblemaker.model.stations.Station;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IStationDal extends CrudRepository<Station, Integer> {

    List<Station> findAll();
}
