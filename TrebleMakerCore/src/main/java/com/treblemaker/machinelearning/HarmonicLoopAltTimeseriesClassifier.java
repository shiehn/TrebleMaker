package com.treblemaker.machinelearning;

import com.treblemaker.Application;
import com.treblemaker.dal.interfaces.IAnalyticsHorizontalDal;
import com.treblemaker.machinelearning.helper.SequenceClassiferUtils;
import com.treblemaker.model.analytics.AnalyticsHorizontal;
import com.treblemaker.model.interfaces.IWeightable;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.weighters.enums.WeightClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HarmonicLoopAltTimeseriesClassifier {

    private IAnalyticsHorizontalDal analyticsHorizontalDal;

    private boolean bypassSeqenceRatings;

    private String cacheKeyHiveCache;

    private String cacheKeyTimeseries;

    private Iterable<AnalyticsHorizontal> analyticsHorizontals;

    @Autowired
    public HarmonicLoopAltTimeseriesClassifier(@Value("${bypass_seqence_ratings}") boolean bypassSeqenceRatings,
                                               @Value("${cache_key_hive_cache}") String cacheKeyHiveCache,
                                               @Value("${cache_key_timeseries}") String cacheKeyTimeseries,
                                               IAnalyticsHorizontalDal analyticsHorizontalDal,
                                               Iterable<AnalyticsHorizontal> analyticsHorizontals) {
        this.bypassSeqenceRatings = bypassSeqenceRatings;
        this.cacheKeyHiveCache = cacheKeyHiveCache;
        this.cacheKeyTimeseries = cacheKeyTimeseries;
        this.analyticsHorizontalDal = analyticsHorizontalDal;
        this.analyticsHorizontals = analyticsHorizontals;
    }

    public WeightClass classify(IWeightable weightable, ProgressionUnitBar currentBar, ProgressionUnitBar oneBarPrior, ProgressionUnitBar secondBarPrior) {

        if (bypassSeqenceRatings) {
            return WeightClass.OK;
        }

        int rating = calculateRating(weightable, currentBar, oneBarPrior, secondBarPrior);

        return SequenceClassiferUtils.ratingToWeightClass(rating);
    }

    private Iterable<AnalyticsHorizontal> getAnalyticsHorizontals() {

//        if(Application.client != null){
//            hiveCache = Application.client.getMap(cacheKeyHiveCache);
//            cachedData = hiveCache.get(cacheKeyTimeseries);
//        }

//        Iterable<AnalyticsHorizontal> analyticsHorizontals = null;

//        if(cachedData != null){
//            analyticsHorizontals = (Iterable<AnalyticsHorizontal>)cachedData;
        if(analyticsHorizontals == null){
            analyticsHorizontals = analyticsHorizontalDal.findAll();
            if(Application.client != null) {
                Application.client.getMap(cacheKeyHiveCache).put(cacheKeyHiveCache, analyticsHorizontals);
            }
        }

        return analyticsHorizontals;
    }

    public int calculateRating(IWeightable weightable, ProgressionUnitBar currentBar, ProgressionUnitBar oneBarPrior, ProgressionUnitBar secondBarPrior) {

        Application.logger.debug("LOG: HarmonicAlt Sequence Weighting ...");

        int rating = 0;

        Iterable<AnalyticsHorizontal> analyticsHorizontals = getAnalyticsHorizontals();

        for (AnalyticsHorizontal analyticsHorizontal : analyticsHorizontals) {

            boolean hasBigram = false;

            for (Integer i = 0; i < analyticsHorizontal.getComposition_time_slots().size(); i++) {

                if (i > 1 && bigramIsNotNull(oneBarPrior, secondBarPrior, analyticsHorizontal, i)) {

                    if (weightable.getId().equals(analyticsHorizontal.getComposition_time_slots().get(i).getHarmonicLoopAltId())
                            && oneBarPrior.getHarmonicLoopAlt().getId().equals(analyticsHorizontal.getComposition_time_slots().get(i - 1).getHarmonicLoopAltId())
                            && secondBarPrior.getHarmonicLoopAlt().getId().equals(analyticsHorizontal.getComposition_time_slots().get(i - 2).getHarmonicLoopAltId())) {

                        rating = SequenceClassiferUtils.incrementBigramRating(analyticsHorizontal.getRating(), rating);

                        hasBigram = true;
                    }
                }
            }

            if (!hasBigram) {

                for (Integer i = 0; i < analyticsHorizontal.getComposition_time_slots().size(); i++) {

                    if (i > 0 && unigramIsNotNull(oneBarPrior, analyticsHorizontal, i)) {

                        if (weightable.getId().equals(analyticsHorizontal.getComposition_time_slots().get(i).getHarmonicLoopAltId())
                                && oneBarPrior.getHarmonicLoopAlt().getId().equals(analyticsHorizontal.getComposition_time_slots().get(i - 1).getHarmonicLoopAltId())) {

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
                analyticsHorizontal.getComposition_time_slots().get(i).getHarmonicLoopAltId() != null &&
                oneBarPrior != null &&
                oneBarPrior.getHarmonicLoopAlt() != null &&
                oneBarPrior.getHarmonicLoopAlt().getId() != null &&
                analyticsHorizontal.getComposition_time_slots().get(i - 1) != null &&
                analyticsHorizontal.getComposition_time_slots().get(i - 1).getHarmonicLoopAltId() != null;
    }

    private boolean bigramIsNotNull(ProgressionUnitBar oneBarPrior, ProgressionUnitBar secondBarPrior, AnalyticsHorizontal analyticsHorizontal, Integer i) {
        return analyticsHorizontal != null &&
                analyticsHorizontal.getComposition_time_slots() != null &&
                analyticsHorizontal.getComposition_time_slots().get(i) != null &&
                analyticsHorizontal.getComposition_time_slots().get(i).getHarmonicLoopAltId() != null &&

                oneBarPrior != null &&
                oneBarPrior.getHarmonicLoopAlt() != null &&
                oneBarPrior.getHarmonicLoopAlt().getId() != null &&

                analyticsHorizontal.getComposition_time_slots().get(i - 1) != null &&
                analyticsHorizontal.getComposition_time_slots().get(i - 1).getHarmonicLoopAltId() != null &&

                secondBarPrior != null &&
                secondBarPrior.getHarmonicLoopAlt() != null &&
                secondBarPrior.getHarmonicLoopAlt().getId() != null &&

                analyticsHorizontal.getComposition_time_slots().get(i - 2) != null &&
                analyticsHorizontal.getComposition_time_slots().get(i - 2).getHarmonicLoopAltId() != null;
    }
}
