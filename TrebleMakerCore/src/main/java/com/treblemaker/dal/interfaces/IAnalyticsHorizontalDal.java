package com.treblemaker.dal.interfaces;

import com.treblemaker.model.analytics.AnalyticsHorizontal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAnalyticsHorizontalDal extends CrudRepository<AnalyticsHorizontal, Integer> {
}
