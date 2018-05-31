package com.treblemaker.weighters;

import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.interfaces.IWeightableLoop;
import com.treblemaker.weighters.enums.WeightClass;
import com.treblemaker.weighters.helper.WeightDistributor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

public class WeightDistrubutorTests {

    @Test
    public void shouldSetOneInstanceForNonWeightLoop(){

        List<BeatLoop> iWeightableList = new ArrayList<>();
        BeatLoop beatLoop = new BeatLoop();
        iWeightableList.add(beatLoop);

        List<IWeightableLoop> results = WeightDistributor.distributeIWeightableListByWeights(iWeightableList);

        assertEquals(1, results.size());
    }

    @Test
    public void shouldContainInstanceCountThatMatchesTotal(){

        List<HarmonicLoop> iWeightableList = new ArrayList<>();
        HarmonicLoop harmonicLoop = new HarmonicLoop();
        harmonicLoop.setEqWeight(WeightClass.GOOD);
        harmonicLoop.setRhythmicWeight(WeightClass.OK);
        harmonicLoop.setHarmonicWeight(WeightClass.GOOD);
        iWeightableList.add(harmonicLoop);

        List<IWeightableLoop> results = WeightDistributor.distributeIWeightableListByWeights(iWeightableList);

        assertEquals((int)harmonicLoop.getTotalWeight(), results.size());
    }

    @Test
    public void shouldContainInstanceCountThatMatchesSumTotal(){

        List<IWeightableLoop> iWeightableList = new ArrayList<>();

        HarmonicLoop harmonicLoop = new HarmonicLoop();
        harmonicLoop.setEqWeight(WeightClass.GOOD);
        harmonicLoop.setRhythmicWeight(WeightClass.OK);
        harmonicLoop.setHarmonicWeight(WeightClass.GOOD);

        BeatLoop beatLoop = new BeatLoop();
        beatLoop.setRhythmicWeight(WeightClass.BAD);

        iWeightableList.add(harmonicLoop);
        iWeightableList.add(beatLoop);

        List<IWeightableLoop> results = WeightDistributor.distributeIWeightableListByWeights(iWeightableList);

        int totalWeight = harmonicLoop.getTotalWeight() + beatLoop.getTotalWeight();
        assertEquals(totalWeight, results.size());
    }
}