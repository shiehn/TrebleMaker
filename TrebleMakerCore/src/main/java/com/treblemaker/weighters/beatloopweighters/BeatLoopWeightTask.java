package com.treblemaker.weighters.beatloopweighters;

import com.treblemaker.Application;
import com.treblemaker.machinelearning.interfaces.IBeatSequenceClassifier;
import com.treblemaker.machinelearning.interfaces.IVerticalClassifier;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.analytics.AnalyticsHorizontal;
import com.treblemaker.weighters.helper.CurrentPriorBar;
import com.treblemaker.weighters.interfaces.IRhythmWeighter;
import com.treblemaker.weighters.themeweighters.BeatLoopAltThemeWeighter;

import java.util.concurrent.Callable;

public class BeatLoopWeightTask implements Callable<Boolean> {

    private BeatLoop optionToWeight;
    private CurrentPriorBar bars;
    private Iterable<AnalyticsHorizontal> analyticsHorizontals;
    private IRhythmWeighter rhythmWeighter;
    private IVerticalClassifier beatLoopVerticalClassifier;
    private IBeatSequenceClassifier beatLoopSequenceClassifier;
    private BeatLoopAltThemeWeighter beatLoopAltThemeWeighter;

    public BeatLoopWeightTask(BeatLoop optionToWeight, CurrentPriorBar bars, Iterable<AnalyticsHorizontal> analyticsHorizontals, IRhythmWeighter rhythmWeighter, IVerticalClassifier beatLoopVerticalClassifier, IBeatSequenceClassifier beatLoopSequenceClassifier, BeatLoopAltThemeWeighter beatLoopAltThemeWeighter) {
        this.optionToWeight = optionToWeight;
        this.bars = bars;
        this.analyticsHorizontals = analyticsHorizontals;
        this.rhythmWeighter = rhythmWeighter;
        this.beatLoopVerticalClassifier = beatLoopVerticalClassifier;
        this.beatLoopSequenceClassifier = beatLoopSequenceClassifier;
        this.beatLoopAltThemeWeighter = beatLoopAltThemeWeighter;
    }

    @Override
    public Boolean call() throws Exception {

        Application.logger.debug("LOG: CURRENT THREAD : " + Thread.currentThread().getName());

        try {

            optionToWeight.setVerticalWeight(beatLoopVerticalClassifier.classify(bars.getCurrentBar(), optionToWeight));

            optionToWeight.setRhythmicWeight(rhythmWeighter.calculateRhythmicWeight(bars.getCurrentBar().getBeatLoop(), optionToWeight));

            optionToWeight.setTimeseriesWeight(beatLoopSequenceClassifier.classify(optionToWeight, bars.getCurrentBar(), bars.getOneBarPrior(), bars.getSecondBarPrior(), analyticsHorizontals));

            optionToWeight.setThemeWeight(beatLoopAltThemeWeighter.calculateThemeWeight(optionToWeight, bars.getOneBarPrior(), bars.getSecondBarPrior()));

        } catch (Exception e) {
            return false;
        }

        return true;
    }
}