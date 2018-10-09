package com.treblemaker.weighters.fx;

import com.treblemaker.Application;
import com.treblemaker.fx.util.DurationAnalysis;
import com.treblemaker.loadbalance.LoadBalancer;
import com.treblemaker.model.arpeggio.Arpeggio;
import com.treblemaker.model.fx.FXArpeggioWithRating;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.utils.Http.HttpUtils;
import com.treblemaker.weighters.basslineweighter.ArpeggioHelpers;
import com.treblemaker.weighters.enums.WeightClass;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.Callable;

public class SynthFXWeightTask implements Callable<SynthFXWeightResponse> {

    private FXArpeggioWithRating fxArpeggioWithRating;
    private Arpeggio arpeggio;
    private int synthId;
    private ProgressionUnitBar progressionUnitBar;
    private DurationAnalysis durationAnalysis;
    private boolean bypassSynthFXRating;
    private ArpeggioHelpers arpeggioHelpers;
    String apiUser;
    String apiPassword;

    public SynthFXWeightTask(FXArpeggioWithRating fxArpeggioWithRating, Arpeggio arpeggio, int synthId, ProgressionUnitBar progressionUnitBar, DurationAnalysis durationAnalysis, boolean bypassSynthFXRating, String apiUser, String apiPassword) {
        this.fxArpeggioWithRating = fxArpeggioWithRating;
        this.arpeggio = arpeggio;
        this.synthId = synthId;
        this.progressionUnitBar = progressionUnitBar;
        this.durationAnalysis = durationAnalysis;
        this.bypassSynthFXRating = bypassSynthFXRating;
        this.apiUser = apiUser;
        this.apiPassword = apiPassword;
        arpeggioHelpers = new ArpeggioHelpers();
    }

    @Override
    public SynthFXWeightResponse call() throws Exception {

        long threadId = Thread.currentThread().getId();
//        Application.logger.debug("LOG:","\nThread # " + threadId + " is doing this task");

        if(bypassSynthFXRating){
//            Application.logger.debug("LOG:","EQ RATE # " + TestCounter.getInstance().count.getAndIncrement());
            SynthFXWeightResponse synthFXWeightBypassResponse = new SynthFXWeightResponse(fxArpeggioWithRating.getFxArpeggioDelay().getId(),synthId,WeightClass.OK);
            return synthFXWeightBypassResponse;
        }

        //PUT TOGETHER ALL THE PREDICTORS!!!
        //PUT TOGETHER ALL THE PREDICTORS!!!
        //PUT TOGETHER ALL THE PREDICTORS!!!
        //PUT TOGETHER ALL THE PREDICTORS!!!
        //PUT TOGETHER ALL THE PREDICTORS!!!
        //PUT TOGETHER ALL THE PREDICTORS!!!
        //PUT TOGETHER ALL THE PREDICTORS!!!
        //PUT TOGETHER ALL THE PREDICTORS!!!
        //PUT TOGETHER ALL THE PREDICTORS!!!
        //PUT TOGETHER ALL THE PREDICTORS!!!
        //PUT TOGETHER ALL THE PREDICTORS!!!

        //SynthId,

        double sixteenthFreq = durationAnalysis.extractSixteenthFrequency(arpeggio.getArpeggioJson().getArpeggio());
        double eigthFreq = durationAnalysis.extractEightFrequency(arpeggio.getArpeggioJson().getArpeggio());
        double quarterFreq = durationAnalysis.extractQuarterFrequency(arpeggio.getArpeggioJson().getArpeggio());
        double dottedQuarterFreq = durationAnalysis.extractDottedQuarterFrequency(arpeggio.getArpeggioJson().getArpeggio());
        double halfFreq = durationAnalysis.extractHalfFrequency(arpeggio.getArpeggioJson().getArpeggio());

        double fxvol = fxArpeggioWithRating.getFxArpeggioDelay().getDelayVolume();
        double fxtype = fxArpeggioWithRating.getFxArpeggioDelay().getDelayType();
        double fxmastervol = fxArpeggioWithRating.getFxArpeggioDelay().getMasterVolume();

        //PUT TOGETHER ALL THE PREDICTORS!!!
        //PUT TOGETHER ALL THE PREDICTORS!!!

        SynthFXWeightResponse synthFXWeightResponse = null;

        try {
            StringBuilder params = new StringBuilder();
            params.append("?");

            params.append("synthid");
            params.append("=");
            params.append(synthId);
            params.append("&");

            params.append("sixteenthfreq");
            params.append("=");
            params.append(sixteenthFreq);
            params.append("&");

            params.append("eigthfreq");
            params.append("=");
            params.append(eigthFreq);
            params.append("&");

            params.append("quarterfreq");
            params.append("=");
            params.append(quarterFreq);
            params.append("&");

            params.append("dottedquarterfreq");
            params.append("=");
            params.append(dottedQuarterFreq);
            params.append("&");

            params.append("halffreq");
            params.append("=");
            params.append(halfFreq);
            params.append("&");

            params.append("fxvol");
            params.append("=");
            params.append(fxvol);
            params.append("&");

            params.append("fxtype");
            params.append("=");
            params.append(fxtype);

            String url = LoadBalancer.getInstance().getUrl() + "/classify/synthfx" + params.toString();

            HttpUtils httpUtils = new HttpUtils();
            String response = httpUtils.sendGet(url, apiUser, apiPassword);
            WeightClass weightClass = arpeggioHelpers.stringToWeight(response);
            synthFXWeightResponse = new SynthFXWeightResponse(fxArpeggioWithRating.getFxArpeggioDelay().getId(), synthId, weightClass);

            return synthFXWeightResponse;
        }catch (Exception e){
            Application.logger.debug("LOG:", e);
            return new SynthFXWeightResponse(fxArpeggioWithRating.getFxArpeggioDelay().getId(), synthId, WeightClass.BAD);
        }
    }
}
