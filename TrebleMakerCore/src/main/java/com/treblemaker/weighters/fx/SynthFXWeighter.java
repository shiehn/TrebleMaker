package com.treblemaker.weighters.fx;

import com.treblemaker.Application;
import com.treblemaker.factory.ExecutorPoolFactory;
import com.treblemaker.fx.util.DurationAnalysis;
import com.treblemaker.model.arpeggio.Arpeggio;
import com.treblemaker.model.fx.FXArpeggioWithRating;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.weighters.interfaces.ISynthFXWeighter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Component
public class SynthFXWeighter implements ISynthFXWeighter {

    private DurationAnalysis durationAnalysis;
    private boolean bypassSynthFXRating;

    @Value("${api.user}")
    String apiUser;

    @Value("${api.password}")
    String apiPassword;

    @Autowired
    public SynthFXWeighter(DurationAnalysis durationAnalysis, boolean bypassSynthFXRating) {
        this.durationAnalysis = durationAnalysis;
        this.bypassSynthFXRating = bypassSynthFXRating;
    }

    @Override
    public Map<Integer, List<FXArpeggioWithRating>> setWeights(QueueState queueState, Map<Integer, List<FXArpeggioWithRating>> synthIdToFXOptions, int variationIndex) {

        for (ProgressionUnit progressionUnit : queueState.getStructure()) {
            for (ProgressionUnitBar progressionUnitBar : progressionUnit.getProgressionUnitBars()) {

                Collection<SynthFXWeightTask> taskCollection = new ArrayList<>();

                for (FXArpeggioWithRating fxArpeggioWithRating : synthIdToFXOptions.get(progressionUnitBar.getHiSynthId().get(variationIndex))) {

                    final Arpeggio arpeggio = queueState.getDataSource().getArpeggios().stream().filter(arp -> arp.getId() == progressionUnitBar.getArpeggioId()).findFirst().get();

                    //TODO RATE EVERY OPTION AGAINST THIS BAR
                    SynthFXWeightTask synthFXWeightTask = new SynthFXWeightTask(
                            fxArpeggioWithRating,
                            arpeggio,
                            progressionUnitBar.getHiSynthId().get(variationIndex),
                            progressionUnitBar,
                            durationAnalysis,
                            bypassSynthFXRating, apiUser, apiPassword);

                    taskCollection.add(synthFXWeightTask);
                }

                ExecutorService executorPool = ExecutorPoolFactory.getPool();
                try {
                    List<Future<SynthFXWeightResponse>> taskList = executorPool.invokeAll(taskCollection);

                    for (Future<SynthFXWeightResponse> task : taskList) {
//                        Application.logger.debug("LOG:","SynthFX weight status : " + task.get().getWeight());

                        //update map with result!!!
                        updateMapWithTaskResult(synthIdToFXOptions, progressionUnitBar, task);
                    }
                } catch (Exception e) {
                    Application.logger.debug("LOG:", e);
                }
                executorPool.shutdown();
            }
        }

        return synthIdToFXOptions;
    }

    private void updateMapWithTaskResult(Map<Integer, List<FXArpeggioWithRating>> synthIdToFXOptions, ProgressionUnitBar progressionUnitBar, Future<SynthFXWeightResponse> task) throws InterruptedException, java.util.concurrent.ExecutionException {
        for (FXArpeggioWithRating fxArpeggioWithRating : synthIdToFXOptions.get(progressionUnitBar.getHiSynthId().get(0))) {//TODO getHiSynthId().get(0) is totally WRONG MUST CORRECT
            if (task.get().getFxArpeggioWithRatingId() == fxArpeggioWithRating.getFxArpeggioDelay().getId()
                    && task.get().getFxArpeggioWithRatingId() == progressionUnitBar.getHiSynthId().get(0)) { //TODO getHiSynthId().get(0) is totally WRONG MUST CORRECT
                fxArpeggioWithRating.incrementWeight(task.get().getWeight());
            }
        }
    }
}