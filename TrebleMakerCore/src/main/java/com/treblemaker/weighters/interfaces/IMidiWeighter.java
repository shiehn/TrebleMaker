package com.treblemaker.weighters.interfaces;

import com.treblemaker.model.arpeggio.Arpeggio;
import com.treblemaker.model.bassline.Bassline;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.weighters.enums.WeightClass;

public interface IMidiWeighter {

    WeightClass getWeight(ProgressionUnitBar barOne, ProgressionUnitBar barTwo, Bassline bassline, Arpeggio arpeggio);
}
