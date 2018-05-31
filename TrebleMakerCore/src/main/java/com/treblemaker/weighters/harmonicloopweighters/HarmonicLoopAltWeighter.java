package com.treblemaker.weighters.harmonicloopweighters;

import com.treblemaker.Application;
import com.treblemaker.factory.ExecutorPoolFactory;
import com.treblemaker.machinelearning.HarmonicLoopTimeseriesClassifier;
import com.treblemaker.machinelearning.interfaces.IVerticalClassifier;
import com.treblemaker.model.analytics.AnalyticsHorizontal;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.weighters.helper.CurrentPriorBar;
import com.treblemaker.weighters.helper.WeightHelper;
import com.treblemaker.weighters.interfaces.IRhythmWeighter;
import com.treblemaker.weighters.themeweighters.HarmonicAltThemeWeighter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Component
public class HarmonicLoopAltWeighter {

    private IRhythmWeighter rhythmWeighter;
    private IVerticalClassifier harmonicVerticalClassifier;
    private HarmonicLoopTimeseriesClassifier harmonicLoopTimeseriesClassifier;
    private HarmonicAltThemeWeighter harmonicAltThemeWeighter;

    @Autowired
    public void HarmonicLoopAltWeighter(IRhythmWeighter rhythmWeighter, IVerticalClassifier harmonicVerticalClassifier, HarmonicLoopTimeseriesClassifier harmonicLoopTimeseriesClassifier, HarmonicAltThemeWeighter harmonicAltThemeWeighter) {

        this.rhythmWeighter = rhythmWeighter;
        this.harmonicVerticalClassifier = harmonicVerticalClassifier;
        this.harmonicLoopTimeseriesClassifier = harmonicLoopTimeseriesClassifier;
        this.harmonicAltThemeWeighter = harmonicAltThemeWeighter;
    }

    public boolean setHarmonicLoopAltWeight(Integer indexToWeight, ProgressionUnit currentProgressionUnit, ProgressionUnit priorProgressionUnit, Iterable<AnalyticsHorizontal> analyticsHorizontals) {

        Application.logger.debug("LOG: HARMONIC SETWEIGHT");

        //TODO WRITE TEST
        CurrentPriorBar bars = WeightHelper.createCurrentPriorBar(indexToWeight, currentProgressionUnit, priorProgressionUnit);

        Collection<HarmonicLoopWeightTask> collection = new ArrayList<>();

        bars.getCurrentBar().getHarmonicLoopAltOptions().forEach(optionToWeight -> {

            HarmonicLoopWeightTask loop = new HarmonicLoopWeightTask(optionToWeight, bars,
                    harmonicVerticalClassifier,
                    rhythmWeighter,
                    harmonicLoopTimeseriesClassifier,
                    harmonicAltThemeWeighter
            );
            collection.add(loop);
        });

        //TODO DO THE WEIGHTS EVEN GET SET???  RETURNS BOOL ...
        //TODO DO THE WEIGHTS EVEN GET SET???  RETURNS BOOL ...
        //TODO DO THE WEIGHTS EVEN GET SET???  RETURNS BOOL ...
        //TODO DO THE WEIGHTS EVEN GET SET???  RETURNS BOOL ...
        //TODO DO THE WEIGHTS EVEN GET SET???  RETURNS BOOL ...
        //TODO DO THE WEIGHTS EVEN GET SET???  RETURNS BOOL ...
        //TODO DO THE WEIGHTS EVEN GET SET???  RETURNS BOOL ...

        ExecutorService executorPool = ExecutorPoolFactory.getPool();
        try {
            List<Future<Boolean>> taskList = executorPool.invokeAll(collection);

            for (Future<Boolean> task : taskList) {
                Application.logger.debug("LOG: Harmonic Alt weight status : " + task.get());
            }
        } catch (Exception e) {
            Application.logger.debug("LOG:", e);
        }
        executorPool.shutdown();

        return true;
    }
}
