package com.treblemaker.weighters.midiweighter;

import com.treblemaker.Application;
import com.treblemaker.loadbalance.LoadBalancer;
import com.treblemaker.model.RhythmicAccents;
import com.treblemaker.model.arpeggio.Arpeggio;
import com.treblemaker.model.bassline.Bassline;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.utils.Http.HttpUtils;
import com.treblemaker.utils.arpeggio.BassAndArpeggioUtil;
import com.treblemaker.utils.rhythmic.RhythmicAccentUtil;
import com.treblemaker.weighters.basslineweighter.ArpeggioHelpers;
import com.treblemaker.weighters.enums.WeightClass;
import com.treblemaker.weighters.interfaces.IMidiWeighter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MidiWeighter implements IMidiWeighter {

    private boolean bypassArpeggioVerticalRating;
    private ArpeggioHelpers arpeggioHelpers;

    @Autowired
    public MidiWeighter(@Value("${bypass_arpeggio_vertical_rating}") boolean bypassArpeggioVerticalRating){

        this.bypassArpeggioVerticalRating = bypassArpeggioVerticalRating;
        this.arpeggioHelpers = new ArpeggioHelpers();
    }

    public double getTotalWeight(double[] bassIntervals, int[] harmonicLoopAccents, int[] beatLoopAccents, int i){
        int tmpWeight = 0;

        if (bassIntervals[i] > 0.0) {
            tmpWeight++;
        }

        if (harmonicLoopAccents[i] > 0.0) {
            tmpWeight++;
        }

        if (beatLoopAccents[i] > 0.0) {
            tmpWeight++;
        }

        double totalWeight;

        switch (tmpWeight) {
            case 1:
                totalWeight = 0.3;
                break;
            case 2:
                totalWeight = 0.6;
                break;
            case 3:
                totalWeight = 1.0;
                break;
            default:
                totalWeight = 0.0;
        }

        return totalWeight;
    }

    @Override
    public WeightClass getWeight(ProgressionUnitBar barOne, ProgressionUnitBar barTwo, Bassline bassline, Arpeggio arpeggio) {

        if(bypassArpeggioVerticalRating){
            return arpeggioHelpers.generateRandomWeight();
        }

        //GET 2bars of rhythmic accents for BEATS ..
        List<RhythmicAccents> beatAccents = barOne.getBeatLoop().getRhythmicAccents();
        int[] beatLoopAccents = RhythmicAccentUtil.extractAccentsAsArray(beatAccents);

        //GET 2bars of rhythmic accents for HARMONICS ..
        List<RhythmicAccents> harmonicAccents = barOne.getHarmonicLoop().getRhythmicAccents();
        int[] harmonicLoopAccents = RhythmicAccentUtil.extractAccentsAsArray(harmonicAccents);


        double[] arpeggioIntervals = new double[32];
        for(int i=0; i<32; i++){
            arpeggioIntervals[i] = BassAndArpeggioUtil.convertIntervalsToDouble(Integer.toString(arpeggio.getArpeggioJson().getArpeggio()[i]));
        }

        double[] bassIntervals = new double[32];
        for (int j=0; j<32;j++){
            bassIntervals[j] = BassAndArpeggioUtil.convertIntervalsToDouble(bassline.getBassline().getIntervalByPosition(j));
        }

        double[] totalWeights = new double[32];
        for (int k=0; k<32;k++){
            totalWeights[k] = getTotalWeight( bassIntervals,  harmonicLoopAccents,  beatLoopAccents, k);
        }

        //TODO THIS SHOULD BE A METHOD
        String arpeggioInputs = "";

        int arrayLength = 32;
        for (int i = 0; i < arrayLength; i++) {

            arpeggioInputs += arpeggioIntervals[i];

            arpeggioInputs += ",";

            arpeggioInputs += totalWeights[i];

            arpeggioInputs += ",";

            arpeggioInputs += bassIntervals[i];

            if(i < 31) {
                arpeggioInputs += ",";
            }
        }

        String url = LoadBalancer.getInstance().getUrl();
        Application.logger.debug("LOG: " + url + "/classify/arpeggio?arpeggio=" + arpeggioInputs);

        HttpUtils httpUtils = new HttpUtils();
        String response = httpUtils.sendGet(url + "/classify/arpeggio?arpeggio=" + arpeggioInputs);

        WeightClass weightClass = arpeggioHelpers.stringToWeight(response);
        Application.logger.debug("LOG: Arpeggio_WeightClass=" + weightClass);

        return weightClass;
    }


}
