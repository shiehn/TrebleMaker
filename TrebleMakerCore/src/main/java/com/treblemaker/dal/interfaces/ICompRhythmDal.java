package com.treblemaker.dal.interfaces;

import com.treblemaker.model.comp.CompRhythm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ICompRhythmDal extends CrudRepository<CompRhythm, Integer> {
    List<CompRhythm> findAll();
}
