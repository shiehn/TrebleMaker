package com.treblemaker.dal.interfaces;

import com.treblemaker.model.melodic.BachChorale;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IBachChoraleDal extends CrudRepository<BachChorale, Integer> {
    List<BachChorale> findAll();
}
