package com.treblemaker.dal.interfaces;

import com.treblemaker.model.analytics.AnalyticsHorizontal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAnalyticsHorizontalDal extends CrudRepository<AnalyticsHorizontal, Integer> {
}
