package com.treblemaker.weighters.basslineweighter;

import com.treblemaker.Application;
import com.treblemaker.loadbalance.LoadBalancer;
import com.treblemaker.model.RhythmicAccents;
import com.treblemaker.model.bassline.BasslineWithRating;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.utils.Http.HttpUtils;
import com.treblemaker.utils.arpeggio.BassAndArpeggioUtil;
import com.treblemaker.utils.rhythmic.RhythmicAccentUtil;
import com.treblemaker.weighters.enums.WeightClass;
import java.util.List;

public class BasslineWeightTask {

    private boolean bypassBasslineVerticalRating;
    private ProgressionUnitBar barOne;
    private ProgressionUnitBar barTwo;
    private BasslineWithRating basslineWithRating;
    private ArpeggioHelpers arpeggioHelpers;
    private String apiUser;
    private String apiPassword;

    public BasslineWeightTask(boolean bypassBasslineVerticalRating, ProgressionUnitBar barOne, ProgressionUnitBar barTwo, BasslineWithRating basslineWithRating, String apiUser, String apiPassword){
        this.bypassBasslineVerticalRating = bypassBasslineVerticalRating;
        this.barOne = barOne;
        this.barTwo = barTwo;
        this.basslineWithRating = basslineWithRating;
        this.arpeggioHelpers = new ArpeggioHelpers();
        this.apiUser = apiUser;
        this.apiPassword = apiPassword;
    }

    public BasslineWithRating call() throws Exception {

        if(bypassBasslineVerticalRating){
            basslineWithRating.incrementWeight(arpeggioHelpers.generateRandomWeight());
            return basslineWithRating;
        }

        //GET 2bars of rhythmic accents for BEATS ..
        List<RhythmicAccents> beatAccents = barOne.getBeatLoop().getRhythmicAccents();
        int[] beatLoopAccents = RhythmicAccentUtil.extractAccentsAsArray(beatAccents);

        //GET 2bars of rhythmic accents for HARMONICS ..
        List<RhythmicAccents> harmonicAccents = barOne.getHarmonicLoop().getRhythmicAccents();
        int[] harmonicLoopAccents = RhythmicAccentUtil.extractAccentsAsArray(harmonicAccents);

        double[] durations = new double[32];
        int length=32;
        for (int j=0; j<length;j++){
            durations[j] = BassAndArpeggioUtil.convertDurationToInt(basslineWithRating.getBassline().getBassline().getDurationByPosition(j));
        }

        double[] intervals = new double[32];
        length=32;
        for (int j=0; j<length;j++){
            intervals[j] = BassAndArpeggioUtil.convertIntervalsToDouble(basslineWithRating.getBassline().getBassline().getIntervalByPosition(j));
        }

        Application.logger.debug("LOG: ******** RATING BASSLINE ********");
        String neuralInput = arpeggioHelpers.createNeuralInput(intervals, durations,  beatLoopAccents, harmonicLoopAccents);

        String url = LoadBalancer.getInstance().getUrl();
        Application.logger.debug("LOG: " + url + "/classify/bass?bass=" + neuralInput);
        HttpUtils httpUtils = new HttpUtils();
        String response = httpUtils.sendGet(url + "/classify/bass?bass=" + neuralInput,   apiUser,   apiPassword);

        WeightClass weightClass = arpeggioHelpers.stringToWeight(response);
        Application.logger.debug("LOG: Bassline_WeightClass=" + weightClass);

        basslineWithRating.incrementWeight(weightClass);

        return basslineWithRating;
    }
}
