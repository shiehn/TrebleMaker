package com.treblemaker.dal.interfaces;

import com.treblemaker.model.sentiment.SentimentComposition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISentimentCompositionDal extends CrudRepository<SentimentComposition, Integer> {
    List<SentimentComposition> findAll();
}