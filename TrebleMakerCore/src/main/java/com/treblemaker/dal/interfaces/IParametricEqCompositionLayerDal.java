package com.treblemaker.dal.interfaces;

import com.treblemaker.services.spectrumanalysis.model.ParametricEqCompositionLayer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@EnableAutoConfiguration
@Repository
public interface IParametricEqCompositionLayerDal extends CrudRepository<ParametricEqCompositionLayer, Integer> {

}