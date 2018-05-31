package com.treblemaker.dal.interfaces;

import com.treblemaker.model.sentiment.SentimentTypes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISentimentTypeDal extends CrudRepository<SentimentTypes, Integer> {
    List<SentimentTypes> findAll();
}
