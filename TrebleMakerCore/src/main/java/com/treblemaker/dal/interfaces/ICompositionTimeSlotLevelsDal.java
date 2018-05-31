package com.treblemaker.dal.interfaces;

import com.treblemaker.model.composition.CompositionTimeSlotLevels;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICompositionTimeSlotLevelsDal extends CrudRepository<CompositionTimeSlotLevels, Integer> {

    List<CompositionTimeSlotLevels> findAll();
}