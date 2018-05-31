package com.treblemaker.dal.interfaces;

import com.treblemaker.model.fx.FXArpeggioDelay;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFXArpeggioDelayDal extends CrudRepository<FXArpeggioDelay, Integer> {

    List<FXArpeggioDelay> findAll();
}
