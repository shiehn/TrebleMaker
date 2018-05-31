package com.treblemaker.dal.interfaces;

import com.treblemaker.model.characterisic.HarmonicLoopCharacteristic;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableAutoConfiguration
@Repository
public interface IHarmonicLoopCharacteristicDal extends CrudRepository<HarmonicLoopCharacteristic, Integer> {

    List<HarmonicLoopCharacteristic> findAll();
}
