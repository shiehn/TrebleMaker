package com.treblemaker.generators.beats;

import com.hazelcast.core.IMap;
import com.treblemaker.Application;
import com.treblemaker.dal.interfaces.IAnalyticsHorizontalDal;
import com.treblemaker.generators.interfaces.IBeatLoopGenerator;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.analytics.AnalyticsHorizontal;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.types.Composition;
import com.treblemaker.options.interfaces.IBeatLoopOptions;
import com.treblemaker.selectors.interfaces.IBeatLoopSelector;
import com.treblemaker.weighters.interfaces.IWeighter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.treblemaker.constants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class BeatLoopAltGenerator implements IBeatLoopGenerator {

    @Autowired
    private IBeatLoopOptions beatLoopOptions;

    @Autowired
    private IWeighter beatLoopAltWeighter;

    @Autowired
    private IBeatLoopSelector beatLoopSelector;

    @Autowired
    private IAnalyticsHorizontalDal analyticsHorizontalDal;

    private boolean bypassSeqenceRatings;

    private String cacheKeyHiveCache;

    private String cacheKeyTimeseries;

    @Autowired
    public BeatLoopAltGenerator(@Value("${bypass_seqence_ratings}") boolean bypassSeqenceRatings,
                                @Value("${cache_key_hive_cache}") String cacheKeyHiveCache,
                                @Value("${cache_key_timeseries}") String cacheKeyTimeseries) {

        this.bypassSeqenceRatings = bypassSeqenceRatings;
        this.cacheKeyHiveCache = cacheKeyHiveCache;
        this.cacheKeyTimeseries = cacheKeyTimeseries;
    }

    @Override
    public QueueState generateAndSetBeatLoops(QueueState queueState, Composition.Layer layerType) {
        //TODO WRITE TEST ..

        Application.logger.debug("LOG: ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        Application.logger.debug("LOG: about to iterate over queueState.getStructure()");
        Application.logger.debug("LOG: about to iterate over queueState.getStructure()");
        Application.logger.debug("LOG: about to iterate over queueState.getStructure()");
        Application.logger.debug("LOG: ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        IMap hiveCache = null;
        Object cachedData = null;

        if(Application.client != null) {
            hiveCache = Application.client.getMap(cacheKeyHiveCache);
            cachedData = hiveCache.get(cacheKeyTimeseries);
        }

        Iterable<AnalyticsHorizontal> analyticsHorizontals = null;

        if (cachedData != null) {
            analyticsHorizontals = (Iterable<AnalyticsHorizontal>) cachedData;
        } else {
            analyticsHorizontals = analyticsHorizontalDal.findAll();
            if(Application.client != null) {
                hiveCache.put(cacheKeyTimeseries, analyticsHorizontals);
            }
        }

        //check the threshold for the given bar(s) to see if should add layer ..
        for (Integer pIndex = 0; pIndex < queueState.getStructure().size(); pIndex++) {

            ProgressionUnit currentPUnit = queueState.getStructure().get(pIndex);
            ProgressionUnit priorPUnit = null;
            if (pIndex > 0) {
                priorPUnit = queueState.getStructure().get(pIndex - 1);
            }

            //queueState.getStructure().forEach(pUnit -> {
//            BeatLoop beatLoop;
//            if (currentPUnit.isLayerGated(layerType)) {
//                beatLoop = ShimHelper.getShimAsBeatLoop(queueState.getQueueItem().getBpm());
//            } else {

            for (int i = 0; i < currentPUnit.getProgressionUnitBars().size(); i++) {

                ProgressionUnitBar pUnitBar = currentPUnit.getProgressionUnitBars().get(i);

                //ProgressionUnitBar pUnitBar = pUnit.getProgressionUnitBars().get(i-1);

                //MAKE SURE ITS NOT ALREADY SET
                if (pUnitBar.getBeatLoopAlt() == null) {

                    //setBeatLoopOptions all options with matching beatloop lengths ...
                    beatLoopOptions.setBeatLoopOptions(pUnitBar, queueState.getDataSource().getBeatLoops(queueState.getQueueItem().getStationId()));

                    Application.logger.debug("LOG: ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    Application.logger.debug("LOG: beatLoopAltWeighter.setWeight(i, currentPUnit, priorPUnit);");
                    Application.logger.debug("LOG: beatLoopAltWeighter.setWeight(i, currentPUnit, priorPUnit);");
                    Application.logger.debug("LOG: beatLoopAltWeighter.setWeight(i, currentPUnit, priorPUnit);)");
                    Application.logger.debug("LOG: ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

                    beatLoopAltWeighter.setWeight(i, currentPUnit, priorPUnit, analyticsHorizontals);

                    //if its beat 0 find out if beat 2/3/4 is setHarmonicLoops to determine length //

                    //if its beat 1 find out if beat /3/4 is setHarmonicLoops to determine length //

                    int maxBarCount = getMaxBarCount(currentPUnit, i);

                    //TODO SELECT beats .. v
                    //ALSO IF A BAR IS SAY 2 bar durations
                    BeatLoop beatSelection = (BeatLoop) beatLoopSelector.selectByWeightAndBarCount(pUnitBar.getBeatLoopAltOptions(), maxBarCount);

                    setBeatLoop(beatSelection, i, currentPUnit.getProgressionUnitBars());
                }
            }
        }
        //});
//        }

        return queueState;
    }

    //TODO put this in a helper service or something ...
    public int getMaxBarCount(ProgressionUnit pUnit, int currentBarIndex) {

        switch (currentBarIndex) {
            case LoopPositions.POSITION_ONE:

                int maxBarCount = 1;
                if (pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).getBeatLoopAlt() != null) {
                    return maxBarCount;
                }

                maxBarCount = maxBarCount + 1;

                if (pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).getBeatLoopAlt() != null) {
                    return maxBarCount;
                }

                maxBarCount = maxBarCount + 1;

                if (pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getBeatLoopAlt() != null) {
                    return maxBarCount;
                }

                return maxBarCount + 1;
            case LoopPositions.POSITION_TWO:
                maxBarCount = 1;

                if (pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).getBeatLoopAlt() != null) {
                    return maxBarCount;
                }

                maxBarCount = maxBarCount + 1;

                if (pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getBeatLoopAlt() != null) {
                    return maxBarCount;
                }

                return maxBarCount + 1;

            case LoopPositions.POSITION_THREE:
                maxBarCount = 1;

                if (pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getBeatLoopAlt() != null) {
                    return maxBarCount;
                }

                return maxBarCount + 1;

            case LoopPositions.POSITION_FOUR:

                return 1;
        }

        return -999;
    }

    @Override
    public void setBeatLoops(Map<ProgressionUnit.ProgressionType, BeatLoop> beatLoopByType, Composition.Layer layerType, List<ProgressionUnit> progressionUnits) {

    }

    @Override
    public void setBeatLoop(BeatLoop beatLoop, int barIndex, List<ProgressionUnitBar> progressionUnitBars) {

        List<Integer> positionsToSet = new ArrayList<Integer>();

        try {

            switch (barIndex) {
                case LoopPositions.POSITION_ONE:

                    switch (beatLoop.getBarCount()) {
                        case 1:
                            BeatLoop loopOne = beatLoop.clone();
                            loopOne.setCurrentBar(1);

                            progressionUnitBars.get(LoopPositions.POSITION_ONE).setBeatLoopAlt(loopOne);
                            break;
                        case 2:
                            loopOne = beatLoop.clone();
                            loopOne.setCurrentBar(1);

                            BeatLoop loopTwo = beatLoop.clone();
                            loopTwo.setCurrentBar(2);

                            progressionUnitBars.get(LoopPositions.POSITION_ONE).setBeatLoopAlt(loopOne);
                            progressionUnitBars.get(LoopPositions.POSITION_TWO).setBeatLoopAlt(loopTwo);
                            break;
                        case 3:
                            loopOne = beatLoop.clone();
                            loopOne.setCurrentBar(1);

                            loopTwo = beatLoop.clone();
                            loopTwo.setCurrentBar(2);

                            BeatLoop loopThree = beatLoop.clone();
                            loopThree.setCurrentBar(3);

                            progressionUnitBars.get(LoopPositions.POSITION_ONE).setBeatLoopAlt(loopOne);
                            progressionUnitBars.get(LoopPositions.POSITION_TWO).setBeatLoopAlt(loopTwo);
                            progressionUnitBars.get(LoopPositions.POSITION_THREE).setBeatLoopAlt(loopThree);
                            break;
                        case 4:
                            loopOne = beatLoop.clone();
                            loopOne.setCurrentBar(1);

                            loopTwo = beatLoop.clone();
                            loopTwo.setCurrentBar(2);

                            loopThree = beatLoop.clone();
                            loopThree.setCurrentBar(3);

                            BeatLoop loopFour = beatLoop.clone();
                            loopFour.setCurrentBar(4);

                            progressionUnitBars.get(LoopPositions.POSITION_ONE).setBeatLoopAlt(loopOne);
                            progressionUnitBars.get(LoopPositions.POSITION_TWO).setBeatLoopAlt(loopTwo);
                            progressionUnitBars.get(LoopPositions.POSITION_THREE).setBeatLoopAlt(loopThree);
                            progressionUnitBars.get(LoopPositions.POSITION_FOUR).setBeatLoopAlt(loopFour);
                            break;
                    }

                    break;
                case LoopPositions.POSITION_TWO:

                    switch (beatLoop.getBarCount()) {
                        case 1:
                            BeatLoop loopOne = beatLoop.clone();
                            loopOne.setCurrentBar(1);

                            progressionUnitBars.get(LoopPositions.POSITION_TWO).setBeatLoopAlt(loopOne);
                            break;
                        case 2:
                            loopOne = beatLoop.clone();
                            loopOne.setCurrentBar(1);

                            BeatLoop loopTwo = beatLoop.clone();
                            loopTwo.setCurrentBar(2);

                            progressionUnitBars.get(LoopPositions.POSITION_TWO).setBeatLoopAlt(loopOne);
                            progressionUnitBars.get(LoopPositions.POSITION_THREE).setBeatLoopAlt(loopTwo);
                            break;
                        case 3:
                            loopOne = beatLoop.clone();
                            loopOne.setCurrentBar(1);

                            loopTwo = beatLoop.clone();
                            loopTwo.setCurrentBar(2);

                            BeatLoop loopThree = beatLoop.clone();
                            loopThree.setCurrentBar(3);

                            progressionUnitBars.get(LoopPositions.POSITION_TWO).setBeatLoopAlt(loopOne);
                            progressionUnitBars.get(LoopPositions.POSITION_THREE).setBeatLoopAlt(loopTwo);
                            progressionUnitBars.get(LoopPositions.POSITION_FOUR).setBeatLoopAlt(loopThree);
                            break;
                        case 4:
                            //TODO LOG ERROR ..
                            break;
                    }

                    break;
                case LoopPositions.POSITION_THREE:

                    switch (beatLoop.getBarCount()) {
                        case 1:
                            BeatLoop loopOne = beatLoop.clone();
                            loopOne.setCurrentBar(1);

                            progressionUnitBars.get(LoopPositions.POSITION_THREE).setBeatLoopAlt(loopOne);
                            break;
                        case 2:
                            loopOne = beatLoop.clone();
                            loopOne.setCurrentBar(1);

                            BeatLoop loopTwo = beatLoop.clone();
                            loopTwo.setCurrentBar(2);

                            progressionUnitBars.get(LoopPositions.POSITION_THREE).setBeatLoopAlt(loopOne);
                            progressionUnitBars.get(LoopPositions.POSITION_FOUR).setBeatLoopAlt(loopTwo);
                            break;
                        case 3:

                            //TODO LOG ERROR ..
                            break;
                        case 4:

                            //TODO LOG ERROR ..
                            break;
                    }

                    break;
                case LoopPositions.POSITION_FOUR:

                    switch (beatLoop.getBarCount()) {
                        case 1:
                            BeatLoop loopOne = beatLoop.clone();
                            loopOne.setCurrentBar(1);

                            progressionUnitBars.get(LoopPositions.POSITION_THREE).setBeatLoopAlt(loopOne);
                            break;
                        case 2:

                            //TODO LOG ERROR ..
                            break;
                        case 3:

                            //TODO LOG ERROR ..
                            break;
                        case 4:

                            //TODO LOG ERROR ..
                            break;
                    }

                    break;
            }
        } catch (CloneNotSupportedException e) {
            Application.logger.debug("LOG:", e);
        }
    }
}
