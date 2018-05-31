package com.treblemaker.dal.interfaces;

import com.treblemaker.model.hitsandfills.Hit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IHitDal extends CrudRepository<Hit, Integer> {

    List<Hit> findAll();
}
