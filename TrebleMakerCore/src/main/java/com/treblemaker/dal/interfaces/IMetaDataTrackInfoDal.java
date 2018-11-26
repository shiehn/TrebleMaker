package com.treblemaker.dal.interfaces;

import com.treblemaker.model.metadata.MetaDataTrackInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IMetaDataTrackInfoDal extends CrudRepository<MetaDataTrackInfo, Integer> {
    List<MetaDataTrackInfo> findAll();
}