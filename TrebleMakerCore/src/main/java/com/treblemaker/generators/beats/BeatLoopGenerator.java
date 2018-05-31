package com.treblemaker.generators.beats;

import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.generators.interfaces.IBeatLoopGenerator;
import com.treblemaker.helpers.ShimHelper;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.types.Composition;
import com.treblemaker.selectors.interfaces.IBeatLoopSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.treblemaker.constants.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BeatLoopGenerator implements IBeatLoopGenerator {

    @Autowired
    private IBeatLoopSelector beatLoopSelector;

    @Autowired
    public AppConfigs appConfigs;

    public QueueState generateAndSetBeatLoops(QueueState queueState, Composition.Layer layerType ) {

        // ProgressionType.VERSE, ProgressionType.BRIDGE, ProgressionType.CHORUS
        Map<ProgressionUnit.ProgressionType, BeatLoop> beatLoopByType = new HashMap<>();

        queueState.getStructure().forEach(pUnit -> {

            if (beatLoopByType.get(pUnit.getType()) == null) {

                BeatLoop beatLoop;
                if (pUnit.isLayerGated(layerType)) {
                    beatLoop = ShimHelper.getShimAsBeatLoop(queueState.getQueueItem().getBpm(), appConfigs);
                } else {
                    beatLoop = (BeatLoop)beatLoopSelector.selectBeatLoop(queueState.getDataSource().getBeatLoops(queueState.getQueueItem().getStationId()));
                }

                beatLoopByType.put(pUnit.getType(), beatLoop);
            }
        });

        setBeatLoops(beatLoopByType, layerType, queueState.getStructure());

        //ONLY SELECT A NEW BEAT IF reRender = true
//        if (!queueState.getQueueItem().isRefactor()) {
//            setBeatLoops(beatLoopByType, layerType, queueState.getStructure());
//        } else if (queueState.getQueueItem().isRefactor() && BeatLoopHelper.isBeatLoopReRender(queueState.getQueueItem())) {
//            setBeatLoops(beatLoopByType, layerType, queueState.getStructure());
//        } else if (queueState.getQueueItem().isRefactor() && !BeatLoopHelper.isBeatLoopReRender(queueState.getQueueItem())) {
//            //use the existing beatLoop ..
//            //setBeatLoops(beatLoopsByType, queueItem.getQueueItem().getStructure());
//        }

        return queueState;
    }

    public void setBeatLoops(Map<ProgressionUnit.ProgressionType, BeatLoop> beatLoopByType, Composition.Layer layerType, List<ProgressionUnit> progressionUnits) {

        progressionUnits.forEach(pUnit -> {
            try {
                if (layerType == Composition.Layer.BEAT_LOOP) {

                    BeatLoop beatLoop = beatLoopByType.get(pUnit.getType());

                    if (beatLoop.getBarCount() == 1) {

                        BeatLoop loopOne = beatLoop.clone();
                        loopOne.setCurrentBar(1);

                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE).setBeatLoop(loopOne);
                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).setBeatLoop(loopOne);
                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).setBeatLoop(loopOne);
                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).setBeatLoop(loopOne);

                    } else if (beatLoop.getBarCount() == 2) {

                        BeatLoop loopOne = beatLoop.clone();
                        loopOne.setCurrentBar(1);

                        BeatLoop loopTwo = beatLoop.clone();
                        loopTwo.setCurrentBar(2);

                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE).setBeatLoop(loopOne);
                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).setBeatLoop(loopTwo);
                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).setBeatLoop(loopOne);
                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).setBeatLoop(loopTwo);

                    } else if (beatLoop.getBarCount() == 4) {

                        BeatLoop loopOne = beatLoop.clone();
                        loopOne.setCurrentBar(1);

                        BeatLoop loopTwo = beatLoop.clone();
                        loopTwo.setCurrentBar(2);

                        BeatLoop loopThree = beatLoop.clone();
                        loopThree.setCurrentBar(3);

                        BeatLoop loopFour = beatLoop.clone();
                        loopFour.setCurrentBar(4);

                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE).setBeatLoop(loopOne);
                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).setBeatLoop(loopTwo);
                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).setBeatLoop(loopThree);
                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).setBeatLoop(loopFour);
                    }

                } else if (layerType == Composition.Layer.BEAT_LOOP_ALT) {

                    BeatLoop beatLoop = beatLoopByType.get(pUnit.getType());

                    if (beatLoop.getBarCount() == 1) {

                        BeatLoop loopOne = beatLoop.clone();
                        loopOne.setCurrentBar(1);

                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE).setBeatLoopAlt(loopOne);
                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).setBeatLoopAlt(loopOne);
                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).setBeatLoopAlt(loopOne);
                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).setBeatLoopAlt(loopOne);

                    } else if (beatLoop.getBarCount() == 2) {

                        BeatLoop loopOne = beatLoop.clone();
                        loopOne.setCurrentBar(1);

                        BeatLoop loopTwo = beatLoop.clone();
                        loopTwo.setCurrentBar(2);

                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE).setBeatLoopAlt(loopOne);
                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).setBeatLoopAlt(loopTwo);
                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).setBeatLoopAlt(loopOne);
                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).setBeatLoopAlt(loopTwo);

                    } else if (beatLoop.getBarCount() == 4) {

                        BeatLoop loopOne = beatLoop.clone();
                        loopOne.setCurrentBar(1);

                        BeatLoop loopTwo = beatLoop.clone();
                        loopTwo.setCurrentBar(2);

                        BeatLoop loopThree = beatLoop.clone();
                        loopThree.setCurrentBar(3);

                        BeatLoop loopFour = beatLoop.clone();
                        loopFour.setCurrentBar(4);

                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE).setBeatLoopAlt(loopOne);
                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).setBeatLoopAlt(loopTwo);
                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).setBeatLoopAlt(loopThree);
                        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).setBeatLoopAlt(loopFour);
                    }
                }
            } catch (CloneNotSupportedException e) {
                Application.logger.debug("LOG:", e);
            }
        });
    }


    @Override
    public void setBeatLoop(BeatLoop beatLoop, int barIndex, List<ProgressionUnitBar> progressionUnitBars) {


    }

    @Override
    public int getMaxBarCount(ProgressionUnit pUnit, int currentBarIndex) {
        return 0;
    }
}
