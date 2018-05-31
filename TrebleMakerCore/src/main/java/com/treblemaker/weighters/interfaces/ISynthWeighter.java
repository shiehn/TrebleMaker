package com.treblemaker.weighters.interfaces;

import com.treblemaker.model.progressions.ProgressionUnitBar;

import static com.treblemaker.model.SynthTemplateOption.*;

public interface ISynthWeighter {

    void setWeights(ProgressionUnitBar progressionUnitBar, SynthRoll synthRoll);
}
