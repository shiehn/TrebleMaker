package com.treblemaker.dal.interfaces;

import com.treblemaker.model.hitsandfills.FillRating;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@EnableAutoConfiguration
@Repository
public interface IFillRatingDal extends CrudRepository<FillRating, Integer> {
}