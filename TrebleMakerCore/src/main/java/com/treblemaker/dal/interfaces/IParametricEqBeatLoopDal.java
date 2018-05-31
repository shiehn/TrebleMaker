package com.treblemaker.dal.interfaces;

import com.treblemaker.services.spectrumanalysis.model.ParametricEqBeatLoop;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@EnableAutoConfiguration
@Repository
public interface IParametricEqBeatLoopDal extends CrudRepository<ParametricEqBeatLoop, Integer> {

    ParametricEqBeatLoop findOneByBeatLoopId(int beatLoopId);
}
