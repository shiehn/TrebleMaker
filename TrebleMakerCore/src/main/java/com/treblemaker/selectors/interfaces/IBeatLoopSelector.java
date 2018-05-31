package com.treblemaker.selectors.interfaces;

import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.interfaces.IWeightableLoop;

import java.util.List;

public interface IBeatLoopSelector {

    IWeightableLoop selectBeatLoop(List<BeatLoop> beatLoops);

    IWeightableLoop selectByWeight(List<? extends IWeightableLoop> weightables);

    IWeightableLoop selectByWeightAndBarCount(List<? extends IWeightableLoop> weightables, int maxBarCount);
}
