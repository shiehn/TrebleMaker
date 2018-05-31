package com.treblemaker.dal.interfaces;

import com.treblemaker.model.analytics.AnalyticsVertical;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAnalyticsVerticalDal extends CrudRepository<AnalyticsVertical, Integer> {
}
