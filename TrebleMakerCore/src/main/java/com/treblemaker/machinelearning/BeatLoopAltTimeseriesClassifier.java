package com.treblemaker.machinelearning;

import com.treblemaker.dal.interfaces.IAnalyticsHorizontalDal;
import com.treblemaker.machinelearning.helper.SequenceClassiferUtils;
import com.treblemaker.machinelearning.interfaces.IBeatSequenceClassifier;
import com.treblemaker.model.analytics.AnalyticsHorizontal;
import com.treblemaker.model.interfaces.IWeightable;

import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.weighters.enums.WeightClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeatLoopAltTimeseriesClassifier implements IBeatSequenceClassifier {

    private IAnalyticsHorizontalDal analyticsHorizontalDal;

    @Autowired
    public BeatLoopAltTimeseriesClassifier(IAnalyticsHorizontalDal analyticsHorizontalDal){

        this.analyticsHorizontalDal = analyticsHorizontalDal;
    }

    @Override
    public WeightClass classify(IWeightable weightable, ProgressionUnitBar currentBar, ProgressionUnitBar oneBarPrior, ProgressionUnitBar secondBarPrior, Iterable<AnalyticsHorizontal> analyticsHorizontals) {

        int rating = calculateRating(weightable, currentBar, oneBarPrior, secondBarPrior, analyticsHorizontals);

        return SequenceClassiferUtils.ratingToWeightClass(rating);
    }

    @Override
    public int calculateRating(IWeightable weightable, ProgressionUnitBar currentBar, ProgressionUnitBar oneBarPrior, ProgressionUnitBar secondBarPrior, Iterable<AnalyticsHorizontal> analyticsHorizontals) {

        int rating = 0;

        for(AnalyticsHorizontal analyticsHorizontal : analyticsHorizontals){

            boolean hasBigram = false;

            for(Integer i=0; i<analyticsHorizontal.getComposition_time_slots().size(); i++){

                if(i > 1 && bigramIsNotNull(oneBarPrior, secondBarPrior, analyticsHorizontal, i)){
                    if(weightable.getId().equals(analyticsHorizontal.getComposition_time_slots().get(i).getBeatLoopAltId())
                            && oneBarPrior.getBeatLoopAlt().getId().equals(analyticsHorizontal.getComposition_time_slots().get(i-1).getBeatLoopAltId())
                            && secondBarPrior.getBeatLoopAlt().getId().equals(analyticsHorizontal.getComposition_time_slots().get(i-2).getBeatLoopAltId())){

                        rating = SequenceClassiferUtils.incrementBigramRating(analyticsHorizontal.getRating(), rating);

                        hasBigram = true;
                    }
                }
            }

            if(!hasBigram){

                for(Integer i=0; i<analyticsHorizontal.getComposition_time_slots().size(); i++){

                    if(i > 0 && unigramIsNotNull(oneBarPrior, analyticsHorizontal, i)){
                        if(weightable.getId().equals(analyticsHorizontal.getComposition_time_slots().get(i).getBeatLoopAltId())
                                && oneBarPrior.getBeatLoopAlt().getId().equals(analyticsHorizontal.getComposition_time_slots().get(i-1).getBeatLoopAltId())){

                            rating = SequenceClassiferUtils.incrementUnigramRating(analyticsHorizontal.getRating(), rating);
                        }
                    }
                }
            }
        }

        return rating;
    }

    private boolean unigramIsNotNull(ProgressionUnitBar oneBarPrior, AnalyticsHorizontal analyticsHorizontal, Integer i) {
        return analyticsHorizontal != null &&
                analyticsHorizontal.getComposition_time_slots() != null &&
                analyticsHorizontal.getComposition_time_slots().get(i) != null &&
                analyticsHorizontal.getComposition_time_slots().get(i).getBeatLoopAltId() != null &&
                oneBarPrior != null &&
                oneBarPrior.getBeatLoopAlt() != null &&
                oneBarPrior.getBeatLoopAlt().getId() != null &&
                analyticsHorizontal.getComposition_time_slots().get(i-1) != null &&
                analyticsHorizontal.getComposition_time_slots().get(i-1).getBeatLoopAltId() != null;
    }

    private boolean bigramIsNotNull(ProgressionUnitBar oneBarPrior, ProgressionUnitBar secondBarPrior, AnalyticsHorizontal analyticsHorizontal, Integer i) {
        return analyticsHorizontal != null &&
                analyticsHorizontal.getComposition_time_slots() != null &&
                analyticsHorizontal.getComposition_time_slots().get(i) != null &&
                analyticsHorizontal.getComposition_time_slots().get(i).getBeatLoopAltId() != null &&

                oneBarPrior != null &&
                oneBarPrior.getBeatLoopAlt() != null &&
                oneBarPrior.getBeatLoopAlt().getId() != null &&

                analyticsHorizontal.getComposition_time_slots().get(i-1) != null &&
                analyticsHorizontal.getComposition_time_slots().get(i-1).getBeatLoopAltId() != null &&

                secondBarPrior != null &&
                secondBarPrior.getBeatLoopAlt() != null &&
                secondBarPrior.getBeatLoopAlt().getId() != null &&

                analyticsHorizontal.getComposition_time_slots().get(i-2) != null &&
                analyticsHorizontal.getComposition_time_slots().get(i-2).getBeatLoopAltId() != null;
    }
}

