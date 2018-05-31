package com.treblemaker.dal.interfaces;

import com.treblemaker.model.bassline.Bassline;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IBasslineDal extends CrudRepository<Bassline, Integer> {

    List<Bassline> findAll();

    Bassline findById(int id);

    List<Bassline> findByArpeggioPerBar(int arpeggio_per_bar);
}
