package com.treblemaker.dal.interfaces;

import com.treblemaker.model.kick.KickPattern;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableAutoConfiguration
@Repository
public interface IKickPatternDal extends CrudRepository<KickPattern, Integer> {
    List<KickPattern> findAll();
}
