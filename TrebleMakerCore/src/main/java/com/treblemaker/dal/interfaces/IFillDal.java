package com.treblemaker.dal.interfaces;

import com.treblemaker.model.hitsandfills.Fill;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IFillDal extends CrudRepository<Fill, Integer> {

    List<Fill> findAll();
}
