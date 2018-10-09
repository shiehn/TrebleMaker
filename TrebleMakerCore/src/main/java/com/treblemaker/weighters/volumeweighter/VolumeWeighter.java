package com.treblemaker.weighters.volumeweighter;

import com.treblemaker.Application;
import com.treblemaker.factory.ExecutorPoolFactory;
import com.treblemaker.weighters.enums.WeightClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Component
public class VolumeWeighter {

    private boolean bypassRatings;

    @Value("${api.user}")
    String apiUser;

    @Value("${api.password}")
    String apiPassword;

    @Autowired
    public VolumeWeighter(@Value("${bypass_volume_level_ratings}") boolean bypassRatings) {
        this.bypassRatings = bypassRatings;
    }

    public List<VolumeWeighterTaskResponse> weightVolumes(List<Map<String, Double>> potentialMixes) {

        List<VolumeWeighterTaskResponse> weightedResponses = new ArrayList<>();

        Collection<VolumeWeightTask> taskCollection = new ArrayList<>();

        for (Map<String, Double> potentialMix : potentialMixes) {
            taskCollection.add(new VolumeWeightTask(potentialMix, bypassRatings, apiUser, apiPassword));
        }

        ExecutorService executorPool = ExecutorPoolFactory.getPool();
        try {
            List<Future<VolumeWeighterTaskResponse>> taskList = executorPool.invokeAll(taskCollection);

            for (Future<VolumeWeighterTaskResponse> task : taskList) {
                Application.logger.debug("LOG: Volume weight rating : " + task.get().getWeightClass());
                weightedResponses.add(task.get());
            }
        } catch (Exception e) {
            Application.logger.debug("LOG: ", e);
        }
        executorPool.shutdown();

        return weightedResponses;
    }

    private WeightClass getRandomWeight() {

        //TODO fucking fix this!!!!!!!!!
        //TODO fucking fix this!!!!!!!!!
        //TODO fucking fix this!!!!!!!!!

        List<WeightClass> weightClasses = Arrays.asList(WeightClass.BAD, WeightClass.OK, WeightClass.GOOD);

        return weightClasses.get(new Random().nextInt(weightClasses.size()));
    }
}
