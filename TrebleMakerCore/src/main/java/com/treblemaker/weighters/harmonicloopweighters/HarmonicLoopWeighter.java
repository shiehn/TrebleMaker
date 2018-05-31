package com.treblemaker.weighters.harmonicloopweighters;

import com.treblemaker.Application;
import com.treblemaker.factory.ExecutorPoolFactory;
import com.treblemaker.machinelearning.HarmonicLoopTimeseriesClassifier;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.analytics.AnalyticsHorizontal;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.weighters.WeightTaskResponse;
import com.treblemaker.weighters.enums.WeightClass;
import com.treblemaker.weighters.interfaces.IRhythmWeighter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.treblemaker.constants.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class HarmonicLoopWeighter {

    private HarmonicLoopTimeseriesClassifier harmonicLoopTimeseriesClassifier;
    private IRhythmWeighter rhythmWeighter;
    private boolean bypassEqRatings;

    private final int THREAD_TIMEOUT = 60;

    @Autowired
    public HarmonicLoopWeighter(IRhythmWeighter rhythmWeighter, HarmonicLoopTimeseriesClassifier harmonicLoopTimeseriesClassifier, @Value("${bypass_eq_ratings}") boolean bypassEqRatings) {

        this.rhythmWeighter = rhythmWeighter;
        this.harmonicLoopTimeseriesClassifier = harmonicLoopTimeseriesClassifier;
        this.bypassEqRatings = bypassEqRatings;
    }

    public boolean setHarmonicLoopWeight(Integer indexToWeight, ProgressionUnit currentProgressionUnit, ProgressionUnit priorProgressionUnit, Iterable<AnalyticsHorizontal> analyticsHorizontals) {

        ProgressionUnitBar progressionUnitBar = currentProgressionUnit.getProgressionUnitBars().get(indexToWeight);

        //ASSIGN RYTHMIC WEIGHTS
        //ULTIMATELY IT SHOULD FOLLOW SUIT WITH BELOW THREADED CODE FOR HARMONIC WEIGHTS..
        for (int i = 0; i < progressionUnitBar.getHarmonicLoopOptions().size(); i++) {
            WeightClass rWeight = rhythmWeighter.calculateRhythmicWeight(progressionUnitBar.getHarmonicLoop(), progressionUnitBar.getHarmonicLoopOptions().get(i));
            progressionUnitBar.getHarmonicLoopOptions().get(i).setRhythmicWeight(rWeight);
//            Application.logger.debug("LOG:","HL WEIGHT RHYTHM: " + rWeight.toString());
        }
        //END ASSIGN RYTHMIC WEIGHTS


        //position one

        //position two

        //position three

        //position four


        //ASSIGN TIME-SERIES WEIGHTS
        assignTimeSeriesWeight(indexToWeight, currentProgressionUnit, priorProgressionUnit);
        //END TIMESERIES WEIGHTS


        //Assign THEME weight
        assignThemeWeight(indexToWeight, currentProgressionUnit, priorProgressionUnit);
        //End assign THEME weight


        Collection<HarmonicLoopEqWeightTask> taskList = new ArrayList<>();

        //SET EQ & rhythmical weights ..
        for (int i = 0; i < progressionUnitBar.getHarmonicLoopOptions().size(); i++) {
            HarmonicLoopEqWeightTask task = new HarmonicLoopEqWeightTask(progressionUnitBar.getBeatLoop().getId(), progressionUnitBar.getHarmonicLoopOptions().get(i).getId(), bypassEqRatings);
            taskList.add(task);
        }

        Application.logger.debug("LOG: Harmonic TaskList SIZE: " + taskList.size());
        ExecutorService executorPool = ExecutorPoolFactory.getPool();
        Application.logger.debug("LOG: EXECUTOR : CREATING");
        Application.logger.debug("LOG: TIME: " + DateTime.now());
        try {
            List<Future<WeightTaskResponse>> invokedTasks = executorPool.invokeAll(taskList);
            List<WeightTaskResponse> weightTaskResponses = new ArrayList<>();

            for (Future<WeightTaskResponse> task : invokedTasks) {
                try {
                    weightTaskResponses.add(task.get());
                    //TODO do i acctually assign the weight class!!!!!!!!@@@@@@@@&?????????????????
                    task.get(THREAD_TIMEOUT, TimeUnit.SECONDS).getWeightClass();
                } catch (TimeoutException tException) {
                    Application.logger.debug("LOG: ERROR!! : TIMEOUT EXCEPTION : HL WEIGHTING!!");
                } catch (InterruptedException ie) {
                    Application.logger.debug("LOG: ERROR!! : INTERRUPT EXCEPTION : HL WEIGHTING!!");
                }
            }

            assignWeights(progressionUnitBar, weightTaskResponses);
        } catch (Exception e) {
            Application.logger.debug("LOG: ERROR!! : HL WEIGHTING!! ", e);
        }
        executorPool.shutdownNow();

        try {
            while (!executorPool.awaitTermination(10, TimeUnit.SECONDS)) {
                Application.logger.debug("LOG: Awaiting completion of threads.");
            }

            Application.logger.debug("LOG: EXECUTOR : SHUTDOWN SUCCESS");
        } catch (InterruptedException e) {
            Application.logger.debug("LOG: ERROR AWAITING THREAD SHUTDOWN : ", e);
        }

        return true;
    }

    public void assignThemeWeight(Integer indexToWeight, ProgressionUnit currentProgressionUnit, ProgressionUnit priorProgressionUnit) {

        ProgressionUnitBar progressionUnitBar = currentProgressionUnit.getProgressionUnitBars().get(indexToWeight);

        for (int i = 0; i < progressionUnitBar.getHarmonicLoopOptions().size(); i++) {

//            Integer currentLoopId = progressionUnitBar.getHarmonicLoop().getId();
            Integer currentLoopOptionId = progressionUnitBar.getHarmonicLoopOptions().get(i).getId();

            //ONE
            if (indexToWeight.equals(LoopPositions.POSITION_ONE) && priorProgressionUnit != null) {
                if (priorProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getHarmonicLoop() != null) {
                    Integer previousLoopId = priorProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getHarmonicLoop().getId();

                    if (currentLoopOptionId.equals(previousLoopId)) {
                        progressionUnitBar.getHarmonicLoopOptions().get(i).setThemeWeight(WeightClass.GOOD);
                        continue;
                    }
                }
            }

            //TWO
            if (indexToWeight.equals(LoopPositions.POSITION_TWO)) {
                if (currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE).getHarmonicLoop() != null) {
                    HarmonicLoop harmonicLoopOne = currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE).getHarmonicLoop();
                    if (harmonicLoopOne != null && harmonicLoopOne.getId().equals(currentLoopOptionId)) {
                        progressionUnitBar.getHarmonicLoopOptions().get(i).setThemeWeight(WeightClass.GOOD);
                        continue;
                    }
                }
            }

            //THREE
            if (indexToWeight.equals(LoopPositions.POSITION_THREE)) {
                if (currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).getHarmonicLoop() != null) {
                    HarmonicLoop harmonicLoopTwo = currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).getHarmonicLoop();
                    if (harmonicLoopTwo != null && harmonicLoopTwo.getId().equals(currentLoopOptionId)) {
                        progressionUnitBar.getHarmonicLoopOptions().get(i).setThemeWeight(WeightClass.GOOD);
                        continue;
                    }
                }
            }

            //FOUR
            if (indexToWeight.equals(LoopPositions.POSITION_FOUR)) {
                if (currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).getHarmonicLoop() != null) {
                    HarmonicLoop harmonicLoopThree = currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).getHarmonicLoop();
                    if (harmonicLoopThree != null && harmonicLoopThree.getId().equals(currentLoopOptionId)) {
                        progressionUnitBar.getHarmonicLoopOptions().get(i).setThemeWeight(WeightClass.GOOD);
                        continue;
                    }
                }
            }
        }
    }

    //assignTimeSeriesWeight
    //assignTimeSeriesWeight
    //assignTimeSeriesWeight
    //assignTimeSeriesWeight
    //assignTimeSeriesWeight
    //assignTimeSeriesWeight
    //assignTimeSeriesWeight
    //assignTimeSeriesWeight
    public void assignTimeSeriesWeight(Integer indexToWeight, ProgressionUnit currentProgressionUnit, ProgressionUnit priorProgressionUnit) {

        ProgressionUnitBar progressionUnitBar = currentProgressionUnit.getProgressionUnitBars().get(indexToWeight);

        for (int i = 0; i < progressionUnitBar.getHarmonicLoopOptions().size(); i++) {

            HarmonicLoop currentLoopOption = progressionUnitBar.getHarmonicLoopOptions().get(i);

            //ONE
            if (indexToWeight.equals(LoopPositions.POSITION_ONE) && priorProgressionUnit != null) {
                if (priorProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getHarmonicLoop() != null) {
                    Integer previousLoopId = priorProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getHarmonicLoop().getId();

                    WeightClass weightClass = harmonicLoopTimeseriesClassifier.classify(currentLoopOption, progressionUnitBar, priorProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR), null);
                    currentLoopOption.setTimeseriesWeight(weightClass);
                    continue;
                }
            }

            //TWO
            if (indexToWeight.equals(LoopPositions.POSITION_TWO)) {
                if (currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE).getHarmonicLoop() != null) {
                    HarmonicLoop harmonicLoopOne = currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE).getHarmonicLoop();
                    if (harmonicLoopOne != null) {
                        WeightClass weightClass = harmonicLoopTimeseriesClassifier.classify(currentLoopOption,
                                currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO),
                                currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE), null);
                        currentLoopOption.setTimeseriesWeight(weightClass);
                        continue;
                    }
                }
            }

            //THREE
            if (indexToWeight.equals(LoopPositions.POSITION_THREE)) {
                if (currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).getHarmonicLoop() != null) {
                    HarmonicLoop harmonicLoop = currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).getHarmonicLoop();
                    if (harmonicLoop != null) {
                        WeightClass weightClass = harmonicLoopTimeseriesClassifier.classify(currentLoopOption,
                                currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE),
                                currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO), null);
                        currentLoopOption.setTimeseriesWeight(weightClass);
                        continue;
                    }
                }
            }

            //FOUR
            if (indexToWeight.equals(LoopPositions.POSITION_FOUR)) {
                if (currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).getHarmonicLoop() != null) {
                    HarmonicLoop harmonicLoop = currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).getHarmonicLoop();
                    if (harmonicLoop != null) {
                        WeightClass weightClass = harmonicLoopTimeseriesClassifier.classify(currentLoopOption,
                                currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR),
                                currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE), null);
                        currentLoopOption.setTimeseriesWeight(weightClass);
                        continue;
                    }
                }
            }
        }
    }

    public void assignWeights(ProgressionUnitBar progressionUnitBar, List<WeightTaskResponse> weightTaskResponses) {

        for (int i = 0; i < progressionUnitBar.getHarmonicLoopOptions().size(); i++) {
            for (WeightTaskResponse response : weightTaskResponses) {
                if (progressionUnitBar.getHarmonicLoopOptions().get(i).getId() == response.getLoopId()) {
                    progressionUnitBar.getHarmonicLoopOptions().get(i).setEqWeight(response.getWeightClass());
                }
            }
        }
    }
}