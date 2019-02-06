package com.treblemaker.weighters.beatloopweighters;

import com.treblemaker.Application;
import com.treblemaker.factory.ExecutorPoolFactory;
import com.treblemaker.machinelearning.interfaces.IBeatSequenceClassifier;
import com.treblemaker.machinelearning.interfaces.IVerticalClassifier;
import com.treblemaker.model.analytics.AnalyticsHorizontal;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.weighters.helper.CurrentPriorBar;
import com.treblemaker.weighters.helper.WeightHelper;
import com.treblemaker.weighters.interfaces.IRhythmWeighter;
import com.treblemaker.weighters.interfaces.IWeighter;
import com.treblemaker.weighters.themeweighters.BeatLoopAltThemeWeighter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Component
public class BeatLoopAltWeighter implements IWeighter {

    private IRhythmWeighter rhythmWeighter;

    private IVerticalClassifier beatLoopVerticalClassifier;

    private IBeatSequenceClassifier beatLoopSequenceClassifier;

    private BeatLoopAltThemeWeighter beatLoopAltThemeWeighter;

    @Autowired
    public void BeatLoopAltWeighter(IRhythmWeighter rhythmWeighter, IVerticalClassifier beatLoopVerticalClassifier, IBeatSequenceClassifier beatLoopSequenceClassifier, BeatLoopAltThemeWeighter beatLoopAltThemeWeighter) {

        this.rhythmWeighter = rhythmWeighter;
        this.beatLoopVerticalClassifier = beatLoopVerticalClassifier;
        this.beatLoopSequenceClassifier = beatLoopSequenceClassifier;
        this.beatLoopAltThemeWeighter = beatLoopAltThemeWeighter;
    }

    @Override
    public boolean setWeights(List<ProgressionUnit> progressionUnit, Iterable<AnalyticsHorizontal> analyticsHorizontals) {

        for (Integer i = 0; i < progressionUnit.size(); i++) {

            ProgressionUnit currentProgressionUnit = progressionUnit.get(i);
            ProgressionUnit priorProgressionUnit = null;

            if (i > 0) {
                priorProgressionUnit = progressionUnit.get(i - 1);
            }

            for (Integer indexToWeight = 0; indexToWeight < currentProgressionUnit.getProgressionUnitBars().size(); indexToWeight++) {
                setWeight(indexToWeight, currentProgressionUnit, priorProgressionUnit, analyticsHorizontals);
            }
        }

        return true;
    }

    @Override
    public boolean setWeight(Integer indexToWeight, ProgressionUnit currentProgressionUnit, ProgressionUnit priorProgressionUnit, Iterable<AnalyticsHorizontal> analyticsHorizontals) {

        //TODO WRITE TEST
        CurrentPriorBar bars = WeightHelper.createCurrentPriorBar(indexToWeight, currentProgressionUnit, priorProgressionUnit);

        Collection<BeatLoopWeightTask> collection = new ArrayList<>();

        bars.getCurrentBar().getBeatLoopAltOptions().forEach(optionToWeight -> {

            BeatLoopWeightTask loop = new BeatLoopWeightTask(optionToWeight, bars, analyticsHorizontals, rhythmWeighter, beatLoopVerticalClassifier, beatLoopSequenceClassifier, beatLoopAltThemeWeighter);
            collection.add(loop);
        });

        try {
            for (BeatLoopWeightTask task : collection) {
                Application.logger.debug("LOG: BeatLoop Alt weight status : " + task.call());
            }
        } catch (Exception e) {
            Application.logger.debug("LOG:", e);
        }

        return true;
    }
}
