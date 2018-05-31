package com.treblemaker.dal.interfaces;

import com.treblemaker.model.SoftSynths;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISoftSynthsDal extends CrudRepository<SoftSynths, Integer> {
}
