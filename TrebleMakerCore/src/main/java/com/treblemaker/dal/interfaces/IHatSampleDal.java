package com.treblemaker.dal.interfaces;

import com.treblemaker.model.hat.HatSample;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableAutoConfiguration
@Repository
public interface IHatSampleDal extends CrudRepository<HatSample, Integer> {
    List<HatSample> findAll();
}