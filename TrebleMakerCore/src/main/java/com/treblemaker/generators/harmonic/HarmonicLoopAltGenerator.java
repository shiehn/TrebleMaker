package com.treblemaker.generators.harmonic;

import com.hazelcast.core.IMap;
import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.dal.interfaces.IAnalyticsHorizontalDal;
import com.treblemaker.generators.interfaces.IHarmonicLoopGenerator;
import com.treblemaker.helpers.ShimHelper;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.analytics.AnalyticsHorizontal;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.types.Composition;
import com.treblemaker.options.interfaces.IHarmonicLoopOptions;
import com.treblemaker.selectors.helper.LoopSelectorHelper;
import com.treblemaker.selectors.interfaces.IHarmonicLoopSelector;
import com.treblemaker.weighters.harmonicloopweighters.HarmonicLoopAltWeighter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.treblemaker.constants.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class HarmonicLoopAltGenerator implements IHarmonicLoopGenerator {

    private IHarmonicLoopOptions harmonicLoopOptions;

    private HarmonicLoopAltWeighter harmonicLoopAltWeighter;

    private IHarmonicLoopSelector harmonicLoopSelector;

    @Autowired
    private IAnalyticsHorizontalDal analyticsHorizontalDal;

    @Autowired
    public AppConfigs appConfigs;

    private String cacheKeyHiveCache;

    private String cacheKeyTimeseries;

    @Autowired
    public HarmonicLoopAltGenerator(IHarmonicLoopOptions harmonicLoopOptions,
                                    HarmonicLoopAltWeighter harmonicLoopAltWeighter,
                        IHarmonicLoopSelector harmonicLoopSelector,
                                    @Value("${cache_key_hive_cache}") String cacheKeyHiveCache,
                                    @Value("${cache_key_timeseries}") String cacheKeyTimeseries){

        this.harmonicLoopOptions = harmonicLoopOptions;
        this.harmonicLoopAltWeighter = harmonicLoopAltWeighter;
        this.harmonicLoopSelector = harmonicLoopSelector;
        this.cacheKeyHiveCache = cacheKeyHiveCache;
        this.cacheKeyTimeseries = cacheKeyTimeseries;
    }

    @Override
    public QueueState setHarmonicLoops(QueueState queueState, Composition.Layer layerType ) {

        IMap hiveCache = null;
        Object cachedData = null;

        if(Application.client != null) {
            hiveCache = Application.client.getMap(cacheKeyHiveCache);
            cachedData = hiveCache.get(cacheKeyTimeseries);
        }

        Iterable<AnalyticsHorizontal> analyticsHorizontals = null;

        if(cachedData != null){
            analyticsHorizontals = (Iterable<AnalyticsHorizontal>)cachedData;
        }else{
            analyticsHorizontals = analyticsHorizontalDal.findAll();
            if(Application.client != null) {
                hiveCache.put(cacheKeyHiveCache, analyticsHorizontals);
            }
        }

        //check the threshold for the given bar(s) to see if should add layer ..
        for(Integer bIndex=0; bIndex<queueState.getStructure().size(); bIndex++){

            ProgressionUnit currentUnit = queueState.getStructure().get(bIndex);
            ProgressionUnit priorUnit = null;
            if(bIndex > 0){
                priorUnit = queueState.getStructure().get(bIndex-1);
            }

            BeatLoop beatLoop;
            if (currentUnit.isLayerGated(layerType)) {
                //TODO WTF IS THIS!!!! a beat loop in harmonic loops ..
                beatLoop = ShimHelper.getShimAsBeatLoop(queueState.getQueueItem().getBpm(), appConfigs);
            } else {

                for (int i = 0; i < currentUnit.getProgressionUnitBars().size(); i++) {

                    ProgressionUnitBar pBar = currentUnit.getProgressionUnitBars().get(i);

                    //MAKE SURE ITS NOT ALREADY SET
                    if (pBar.getHarmonicLoopAlt() == null) {

                        //setHarmonicLoops all options ..
                        System.out.println("STATION ID : " + queueState.getQueueItem().getStationId());
                        harmonicLoopOptions.setHarmonicLoopAltOptions(pBar, queueState.getDataSource().getHarmonicLoops(queueState.getQueueItem().getStationId()));

                        harmonicLoopAltWeighter.setHarmonicLoopAltWeight(i, currentUnit, priorUnit, analyticsHorizontals);

                        //if its beat 0 find out if beat 2/3/4 is setHarmonicLoops to determine length //

                        //if its beat 1 find out if beat /3/4 is setHarmonicLoops to determine length //

                        int maxBarCount = getMaxBarCount(currentUnit, i);

                        //TODO SELECT beats .. v
                        //ALSO IF A BAR IS SAY 2 bar durations
                        HarmonicLoop harmonicLoopSelection = LoopSelectorHelper.makeWeightedSelectionFromHarmonicLoops(pBar.getHarmonicLoopAltOptions(), null, null);//  (pUnitBar.getBeatLoopAltOptions(), maxBarCount);

                        setHarmonicLoop(harmonicLoopSelection, i, currentUnit.getProgressionUnitBars());
                    }
                }
            }
        }

        return queueState;
    }

    /*

     */

    @Override
    public int getMaxBarCount(ProgressionUnit pUnit, int currentBarIndex) {

        switch (currentBarIndex) {
            case LoopPositions.POSITION_ONE:

                int maxBarCount = 1;
                if (pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).getHarmonicLoopAlt() != null) {
                    return maxBarCount;
                }

                maxBarCount = maxBarCount + 1;

                if (pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).getHarmonicLoopAlt() != null) {
                    return maxBarCount;
                }

                maxBarCount = maxBarCount + 1;

                if (pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getHarmonicLoopAlt() != null) {
                    return maxBarCount;
                }

                return maxBarCount + 1;
            case LoopPositions.POSITION_TWO:
                maxBarCount = 1;

                if (pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).getHarmonicLoopAlt() != null) {
                    return maxBarCount;
                }

                maxBarCount = maxBarCount + 1;

                if (pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getHarmonicLoopAlt() != null) {
                    return maxBarCount;
                }

                return maxBarCount + 1;

            case LoopPositions.POSITION_THREE:
                maxBarCount = 1;

                if (pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getHarmonicLoopAlt() != null) {
                    return maxBarCount;
                }

                return maxBarCount + 1;

            case LoopPositions.POSITION_FOUR:

                return 1;
        }

        return -999;
    }


    @Override
    public void setHarmonicLoop(HarmonicLoop harmonicLoop, int barIndex, List<ProgressionUnitBar> progressionUnitBars ) {

        if(harmonicLoop == null){
            String test = "";
        }

        List<Integer> positionsToSet = new ArrayList<Integer>();

        try {

            switch (barIndex) {
                case LoopPositions.POSITION_ONE:



                    switch (harmonicLoop.getBarCount()) {
                        case 1:
                            HarmonicLoop loopOne = harmonicLoop.clone();
                            loopOne.setCurrentBar(1);

                            progressionUnitBars.get(LoopPositions.POSITION_ONE).setHarmonicLoopAlt(loopOne);
                            break;
                        case 2:
                            loopOne = harmonicLoop.clone();
                            loopOne.setCurrentBar(1);

                            HarmonicLoop loopTwo = harmonicLoop.clone();
                            loopTwo.setCurrentBar(2);

                            progressionUnitBars.get(LoopPositions.POSITION_ONE).setHarmonicLoopAlt(loopOne);
                            progressionUnitBars.get(LoopPositions.POSITION_TWO).setHarmonicLoopAlt(loopTwo);
                            break;
                        case 3:
                            loopOne = harmonicLoop.clone();
                            loopOne.setCurrentBar(1);

                            loopTwo = harmonicLoop.clone();
                            loopTwo.setCurrentBar(2);

                            HarmonicLoop loopThree = harmonicLoop.clone();
                            loopThree.setCurrentBar(3);

                            progressionUnitBars.get(LoopPositions.POSITION_ONE).setHarmonicLoopAlt(loopOne);
                            progressionUnitBars.get(LoopPositions.POSITION_TWO).setHarmonicLoopAlt(loopTwo);
                            progressionUnitBars.get(LoopPositions.POSITION_THREE).setHarmonicLoopAlt(loopThree);
                            break;
                        case 4:
                            loopOne = harmonicLoop.clone();
                            loopOne.setCurrentBar(1);

                            loopTwo = harmonicLoop.clone();
                            loopTwo.setCurrentBar(2);

                            loopThree = harmonicLoop.clone();
                            loopThree.setCurrentBar(3);

                            HarmonicLoop loopFour = harmonicLoop.clone();
                            loopFour.setCurrentBar(4);

                            progressionUnitBars.get(LoopPositions.POSITION_ONE).setHarmonicLoopAlt(loopOne);
                            progressionUnitBars.get(LoopPositions.POSITION_TWO).setHarmonicLoopAlt(loopTwo);
                            progressionUnitBars.get(LoopPositions.POSITION_THREE).setHarmonicLoopAlt(loopThree);
                            progressionUnitBars.get(LoopPositions.POSITION_FOUR).setHarmonicLoopAlt(loopFour);
                            break;
                    }

                    break;
                case LoopPositions.POSITION_TWO:

                    switch (harmonicLoop.getBarCount()) {
                        case 1:
                            HarmonicLoop loopOne = harmonicLoop.clone();
                            loopOne.setCurrentBar(1);

                            progressionUnitBars.get(LoopPositions.POSITION_TWO).setHarmonicLoopAlt(loopOne);
                            break;
                        case 2:
                            loopOne = harmonicLoop.clone();
                            loopOne.setCurrentBar(1);

                            HarmonicLoop loopTwo = harmonicLoop.clone();
                            loopTwo.setCurrentBar(2);

                            progressionUnitBars.get(LoopPositions.POSITION_TWO).setHarmonicLoopAlt(loopOne);
                            progressionUnitBars.get(LoopPositions.POSITION_THREE).setHarmonicLoopAlt(loopTwo);
                            break;
                        case 3:
                            loopOne = harmonicLoop.clone();
                            loopOne.setCurrentBar(1);

                            loopTwo = harmonicLoop.clone();
                            loopTwo.setCurrentBar(2);

                            HarmonicLoop loopThree = harmonicLoop.clone();
                            loopThree.setCurrentBar(3);

                            progressionUnitBars.get(LoopPositions.POSITION_TWO).setHarmonicLoopAlt(loopOne);
                            progressionUnitBars.get(LoopPositions.POSITION_THREE).setHarmonicLoopAlt(loopTwo);
                            progressionUnitBars.get(LoopPositions.POSITION_FOUR).setHarmonicLoopAlt(loopThree);
                            break;
                        case 4:
                            //TODO LOG ERROR ..
                            break;
                    }

                    break;
                case LoopPositions.POSITION_THREE:

                    switch (harmonicLoop.getBarCount()) {
                        case 1:
                            HarmonicLoop loopOne = harmonicLoop.clone();
                            loopOne.setCurrentBar(1);

                            progressionUnitBars.get(LoopPositions.POSITION_THREE).setHarmonicLoopAlt(loopOne);
                            break;
                        case 2:
                            loopOne = harmonicLoop.clone();
                            loopOne.setCurrentBar(1);

                            HarmonicLoop loopTwo = harmonicLoop.clone();
                            loopTwo.setCurrentBar(2);

                            progressionUnitBars.get(LoopPositions.POSITION_THREE).setHarmonicLoopAlt(loopOne);
                            progressionUnitBars.get(LoopPositions.POSITION_FOUR).setHarmonicLoopAlt(loopTwo);
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

                    switch (harmonicLoop.getBarCount()) {
                        case 1:
                            HarmonicLoop loopOne = harmonicLoop.clone();
                            loopOne.setCurrentBar(1);

                            progressionUnitBars.get(LoopPositions.POSITION_THREE).setHarmonicLoopAlt(loopOne);
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
