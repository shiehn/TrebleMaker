package com.treblemaker.dal.interfaces;

import com.treblemaker.model.composition.Composition;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableAutoConfiguration
@Repository
public interface ICompositionDal extends CrudRepository<Composition, Integer> {

    Composition findByCompositionUid(String compositionUid);

    List<Composition> findAll();
}
