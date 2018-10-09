package com.treblemaker.weighters.harmonicloopweighters;

import com.treblemaker.Application;
import com.treblemaker.loadbalance.LoadBalancer;
import com.treblemaker.utils.Http.HttpUtils;
import com.treblemaker.weighters.WeightClassificationUtils;
import com.treblemaker.weighters.WeightTaskResponse;
import com.treblemaker.weighters.enums.WeightClass;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.Callable;

public class HarmonicLoopEqWeightTask implements Callable<WeightTaskResponse> {

    private int beatLoopId;
    private int harmonicLoopId;
    private boolean bypassEqRatings;
    String apiUser;
    String apiPassword;

    public HarmonicLoopEqWeightTask(int beatLoopId, int harmonicLoopId,  boolean bypassEqRatings, String apiUser, String apiPassword) {
        this.beatLoopId = beatLoopId;
        this.harmonicLoopId = harmonicLoopId;
        this.bypassEqRatings = bypassEqRatings;
        this.apiUser = apiUser;
        this.apiPassword =apiPassword;
    }

    @Override
    public WeightTaskResponse call() throws Exception {

        long threadId = Thread.currentThread().getId();
//        Application.logger.debug("LOG:","\nThread # " + threadId + " is doing this task");

        if(bypassEqRatings){
//            Application.logger.debug("LOG:","EQ RATE # " + TestCounter.getInstance().count.getAndIncrement());
            return new WeightTaskResponse(harmonicLoopId, WeightClass.OK);
        }

        try {
            String url = LoadBalancer.getInstance().getUrl() + "/classify/eq/" + harmonicLoopId + "/" + beatLoopId;
            WeightClass weightClass;

            HttpUtils httpUtils = new HttpUtils();
            String classResult = httpUtils.sendGet(url, apiUser, apiPassword);

            weightClass = WeightClassificationUtils.verticalWeightToClass(classResult);

            Application.logger.debug("LOG: + RATING_HARM_EQ as : " + weightClass);

            WeightTaskResponse weightTaskResponse = new WeightTaskResponse(harmonicLoopId, weightClass);
            return weightTaskResponse;


        }catch (Exception e){
            Application.logger.debug("LOG:", e);
            return new WeightTaskResponse(harmonicLoopId, WeightClass.BAD);
        }
    }
}
