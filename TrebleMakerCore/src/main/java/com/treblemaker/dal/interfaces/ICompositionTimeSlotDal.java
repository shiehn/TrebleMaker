package com.treblemaker.dal.interfaces;

import com.treblemaker.model.composition.CompositionTimeSlot;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableAutoConfiguration
@Repository
public interface ICompositionTimeSlotDal extends CrudRepository<CompositionTimeSlot, Integer> {

    List<CompositionTimeSlot> findAll();

    List<CompositionTimeSlot> findByCompositionId(int CompositionId);
}