package com.treblemaker.options.interfaces;

import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.progressions.ProgressionUnitBar;

import java.util.List;

public interface IBeatLoopOptions {

    void setBeatLoopOptions(ProgressionUnitBar pUnitBar, List<BeatLoop> beatLoops);
}
