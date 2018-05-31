package com.treblemaker.dal.interfaces;

import com.treblemaker.model.kick.KickSample;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@EnableAutoConfiguration
@Repository
public interface IKickSampleDal extends CrudRepository <KickSample, Integer>{
    List<KickSample> findAll();
}
