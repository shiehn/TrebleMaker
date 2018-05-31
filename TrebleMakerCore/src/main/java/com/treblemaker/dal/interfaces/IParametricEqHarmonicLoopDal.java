package com.treblemaker.dal.interfaces;

import com.treblemaker.services.spectrumanalysis.model.ParametricEqHarmonicLoop;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@EnableAutoConfiguration
@Repository
public interface IParametricEqHarmonicLoopDal extends CrudRepository<ParametricEqHarmonicLoop, Integer> {

    ParametricEqHarmonicLoop findOneByHarmonicLoopId(int harmonicLoopId);
}