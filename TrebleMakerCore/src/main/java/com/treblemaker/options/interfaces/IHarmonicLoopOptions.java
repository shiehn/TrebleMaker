package com.treblemaker.options.interfaces;

import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.ProcessingState;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IHarmonicLoopOptions {

    void setHarmonicLoopAltOptions(ProgressionUnitBar progressionUnitBar, List<HarmonicLoop> harmonicLoopOptions);

    void setHarmonicLoopOptions(ProgressionUnitBar progressionUnitBar, List<HarmonicLoop> harmonicLoopOptions, ProcessingState processingState);

    List<HarmonicLoop> getLoopOptionsFromHarmonicLoopAndChord(HarmonicLoop harmonicLoop, HiveChord chord, List<HarmonicLoop> harmonicLoopOptions);

    List<HarmonicLoop> getLoopOptionsFromHarmonicLoop(HarmonicLoop harmonicLoop, List<HarmonicLoop> harmonicLoopOptions);

    List<HarmonicLoop> getLoopOptionsFromAChord(HiveChord chord, List<HarmonicLoop> harmonicLoopOptions);
}
