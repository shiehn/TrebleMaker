package com.treblemaker.extractors.interfaces;

import com.treblemaker.model.progressions.ProgressionUnitBar;
import org.jfugue.pattern.Pattern;

public interface IPatternSetter {

    void setHiPattern(ProgressionUnitBar progressionUnitBar, Integer bpm);

    void setAltHiPattern(ProgressionUnitBar progressionUnitBar, Integer bpm);

    void setMidPattern(ProgressionUnitBar progressionUnitBar, Integer bpm);

    void setAltMidPattern(ProgressionUnitBar progressionUnitBar, Integer bpm);

    void setLowPattern(ProgressionUnitBar progressionUnitBar, Integer bpm);

    void setAltLowPattern(ProgressionUnitBar progressionUnitBar, Integer bpm);

    Pattern generateBarRest(Integer bpm);

    Pattern generateRests(int barCount, Integer bpm);
}
