package com.treblemaker.dal.interfaces;

import com.treblemaker.model.BeatLoop;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableAutoConfiguration
@Repository
public interface IBeatLoopsDal extends CrudRepository<BeatLoop, Integer> {

	List<BeatLoop> findAll();

	List<BeatLoop> findByNormalizedLength(int normalizedLength);
}
