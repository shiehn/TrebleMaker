package com.treblemaker.dal.interfaces;

import com.treblemaker.model.ChordSequences;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IChordSequencesDal extends CrudRepository<ChordSequences, Integer> {

    List<ChordSequences> findAll();
}






