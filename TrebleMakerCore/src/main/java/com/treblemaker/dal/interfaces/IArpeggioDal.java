package com.treblemaker.dal.interfaces;

import com.treblemaker.model.arpeggio.Arpeggio;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IArpeggioDal extends CrudRepository<Arpeggio, Integer> {

    List<Arpeggio> findAll();
}





