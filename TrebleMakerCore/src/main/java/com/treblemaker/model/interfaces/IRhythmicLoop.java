package com.treblemaker.model.interfaces;

import com.treblemaker.configs.AppConfigs;
import com.treblemaker.model.RhythmicAccents;

import java.util.List;

public interface IRhythmicLoop {

    float getAudioLength();

    void setAudioLength(float audioLength);

    Integer getBarCount();

    void setBarCount(Integer barCount);

    int getBpm();

    void setBpm(int bpm);

    String getFilePath(AppConfigs appConfigs);

    void setFilePath(String filePath);

    int getNormalizedLength();

    void setNormalizedLength(int normalizedLength);

    List<RhythmicAccents> getRhythmicAccents();

    void setRhythmicAccents(List<RhythmicAccents> rhythmicAccents);

    boolean isShim();

    void setIsShim(boolean isShim);
}
