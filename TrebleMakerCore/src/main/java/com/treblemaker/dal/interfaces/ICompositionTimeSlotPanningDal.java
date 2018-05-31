package com.treblemaker.dal.interfaces;

import com.treblemaker.model.composition.CompositionTimeSlotPanning;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICompositionTimeSlotPanningDal extends CrudRepository<CompositionTimeSlotPanning, Integer> {

    List<CompositionTimeSlotPanning> findAll();
}
