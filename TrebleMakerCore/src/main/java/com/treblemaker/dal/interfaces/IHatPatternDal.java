package com.treblemaker.dal.interfaces;

import com.treblemaker.model.hat.HatPattern;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableAutoConfiguration
@Repository
public interface IHatPatternDal extends CrudRepository<HatPattern, Integer> {
    List<HatPattern> findAll();
}
