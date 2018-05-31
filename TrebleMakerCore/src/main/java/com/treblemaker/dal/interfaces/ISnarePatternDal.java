package com.treblemaker.dal.interfaces;

import com.treblemaker.model.snare.SnarePattern;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableAutoConfiguration
@Repository
public interface ISnarePatternDal extends CrudRepository<SnarePattern, Integer> {
    List<SnarePattern> findAll();
}
