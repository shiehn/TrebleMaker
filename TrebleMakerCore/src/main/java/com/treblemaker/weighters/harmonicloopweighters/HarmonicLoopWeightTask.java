package com.treblemaker.weighters.harmonicloopweighters;

import com.treblemaker.Application;
import com.treblemaker.machinelearning.HarmonicLoopTimeseriesClassifier;
import com.treblemaker.machinelearning.interfaces.IVerticalClassifier;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.weighters.helper.CurrentPriorBar;
import com.treblemaker.weighters.interfaces.IRhythmWeighter;
import com.treblemaker.weighters.themeweighters.HarmonicAltThemeWeighter;

public class HarmonicLoopWeightTask {

    private HarmonicLoop optionToWeight;
    private CurrentPriorBar bars;

    private IRhythmWeighter rhythmWeighter;
    private IVerticalClassifier harmonicVerticalClassifier;
    private HarmonicLoopTimeseriesClassifier harmonicLoopTimeseriesClassifier;
    private HarmonicAltThemeWeighter harmonicAltThemeWeighter;

    public HarmonicLoopWeightTask(HarmonicLoop optionToWeight, CurrentPriorBar bars,
                                  IVerticalClassifier harmonicVerticalClassifier,
                                  IRhythmWeighter rhythmWeighter,
                                  HarmonicLoopTimeseriesClassifier harmonicLoopTimeseriesClassifier,
                                  HarmonicAltThemeWeighter harmonicAltThemeWeighter) {

        this.optionToWeight = optionToWeight;
        this.bars = bars;
        this.harmonicVerticalClassifier = harmonicVerticalClassifier;
        this.rhythmWeighter = rhythmWeighter;
        this.harmonicLoopTimeseriesClassifier = harmonicLoopTimeseriesClassifier;
        this.harmonicAltThemeWeighter = harmonicAltThemeWeighter;
    }

    public Boolean call() throws Exception {

        Application.logger.debug("LOG: CURRENT THREAD : " + Thread.currentThread().getName());

        try {
            Application.logger.debug("LOG: HARMONIC Alt weighting : vertical, rhythmic, timeseries, theme");

            optionToWeight.setVerticalWeight(harmonicVerticalClassifier.classify(bars.getCurrentBar(), optionToWeight));

            optionToWeight.setRhythmicWeight(rhythmWeighter.calculateRhythmicWeight(bars.getCurrentBar().getBeatLoop(), optionToWeight));

            optionToWeight.setTimeseriesWeight(harmonicLoopTimeseriesClassifier.classify(optionToWeight, bars.getCurrentBar(), bars.getOneBarPrior(), bars.getSecondBarPrior()));

            optionToWeight.setThemeWeight(harmonicAltThemeWeighter.calculateThemeWeight(optionToWeight, bars.getOneBarPrior(), bars.getSecondBarPrior()));
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}