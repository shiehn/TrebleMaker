package com.treblemaker.dal.interfaces;

import com.treblemaker.model.metadata.MetaDataChordInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IMetaDataChordInfoDal extends CrudRepository<MetaDataChordInfo, Integer> {
    List<MetaDataChordInfo> findAll();
}