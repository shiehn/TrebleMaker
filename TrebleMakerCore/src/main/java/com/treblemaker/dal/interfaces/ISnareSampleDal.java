package com.treblemaker.dal.interfaces;

import com.treblemaker.model.snare.SnareSample;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableAutoConfiguration
@Repository
public interface ISnareSampleDal extends CrudRepository<SnareSample, Integer> {
    List<SnareSample> findAll();
}