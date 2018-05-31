package com.treblemaker.dal.interfaces;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;
import com.treblemaker.model.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableAutoConfiguration
@Repository
public interface IHarmonicLoopsDal extends CrudRepository<HarmonicLoop, Integer> {

    List<HarmonicLoop> findAll();

    List<HarmonicLoop> findByNormalizedLength(int normalized_length);
}
