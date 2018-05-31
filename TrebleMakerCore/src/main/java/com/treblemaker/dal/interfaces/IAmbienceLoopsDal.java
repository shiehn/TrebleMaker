package com.treblemaker.dal.interfaces;

import com.treblemaker.model.*;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableAutoConfiguration
@Repository
public interface IAmbienceLoopsDal extends CrudRepository<AmbienceLoop, Integer> {

	List<AmbienceLoop> findAll();

	List<AmbienceLoop> findByAudioLengthLessThanEqual(float maxSampleLength);
}
