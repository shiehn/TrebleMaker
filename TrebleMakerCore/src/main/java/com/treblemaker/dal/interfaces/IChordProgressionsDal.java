package com.treblemaker.dal.interfaces;

import com.treblemaker.model.ChordProgression;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableAutoConfiguration
@Repository
public interface IChordProgressionsDal extends CrudRepository<ChordProgression, Integer> {

    List<ChordProgression> findAll();
}
