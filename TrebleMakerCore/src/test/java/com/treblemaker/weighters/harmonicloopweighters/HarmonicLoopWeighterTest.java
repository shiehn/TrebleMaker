package com.treblemaker.weighters.harmonicloopweighters;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.constants.LoopPositions;
import com.treblemaker.loadbalance.LoadBalancer;
import com.treblemaker.machinelearning.HarmonicLoopTimeseriesClassifier;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.TestBase;
import com.treblemaker.utils.CloneUtils;
import com.treblemaker.weighters.enums.WeightClass;
import com.treblemaker.weighters.rhythmweighter.RhythmWeighter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.treblemaker.model.progressions.ProgressionUnit.BarCount.FOUR;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"bypass_seqence_ratings=false", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class HarmonicLoopWeighterTest extends TestBase {

    private RhythmWeighter rhythmWeighter;
    private HarmonicLoopWeighter harmonicLoopWeighter;

    @Autowired
    private HarmonicLoopTimeseriesClassifier harmonicLoopTimeseriesClassifier;

    @Value("${machinelearning_endpoints}") String[] mlEndpoints;

    @Before
    public void initTest() throws IOException {

        LoadBalancer.getInstance().initLoadBalancer(false, mlEndpoints);

        rhythmWeighter = Mockito.mock(RhythmWeighter.class);
        Mockito.when(rhythmWeighter.calculateRhythmicWeight(Mockito.any(), Mockito.any())).thenReturn(WeightClass.GOOD);

        harmonicLoopWeighter = new HarmonicLoopWeighter(rhythmWeighter, harmonicLoopTimeseriesClassifier, true);
    }

    private List<ProgressionUnit> progressionUnitSetup() throws CloneNotSupportedException {

        ProgressionUnitBar bar = new ProgressionUnitBar();

        BeatLoop beatLoop = new BeatLoop();
        beatLoop.setId(666);

        HarmonicLoop harmonicLoopA = new HarmonicLoop();
        harmonicLoopA.setId(77);

        HarmonicLoop altOptionA = new HarmonicLoop();
        altOptionA.setId(22);
        HarmonicLoop altOptionB = new HarmonicLoop();
        altOptionB.setId(33);
        HarmonicLoop altOptionC = new HarmonicLoop();
        altOptionC.setId(44);
        HarmonicLoop altOptionD = new HarmonicLoop();
        altOptionD.setId(77);

        List<HarmonicLoop> options = Arrays.asList(altOptionA,altOptionB,altOptionC,altOptionD);

        bar.setHarmonicLoopOptions(CloneUtils.cloneHarmonicLoops(options));
        bar.setHarmonicLoop(harmonicLoopA);
        bar.setBeatLoop(beatLoop);

        ProgressionUnit progressionUnitOne = new ProgressionUnit();
        progressionUnitOne.initBars(FOUR.getValue());
        progressionUnitOne.getProgressionUnitBars().set(0, bar.clone());
        progressionUnitOne.getProgressionUnitBars().set(1, bar.clone());
        progressionUnitOne.getProgressionUnitBars().set(2, bar.clone());
        progressionUnitOne.getProgressionUnitBars().set(3, bar.clone());

        ProgressionUnit progressionUnitTwo = new ProgressionUnit();
        progressionUnitTwo.initBars(FOUR.getValue());
        progressionUnitTwo.getProgressionUnitBars().set(0, bar.clone());
        progressionUnitTwo.getProgressionUnitBars().set(1, bar.clone());
        progressionUnitTwo.getProgressionUnitBars().set(2, bar.clone());
        progressionUnitTwo.getProgressionUnitBars().set(3, bar.clone());

        return Arrays.asList(progressionUnitOne, progressionUnitTwo);
    }

    @Test
    public void should_set_harmonicLoopWeights_for_bar() throws CloneNotSupportedException {

        int barIndex = 1;
        ProgressionUnit currentProgressionUnit = progressionUnitSetup().get(1);
        ProgressionUnit previousProgressionUnit = progressionUnitSetup().get(0);

        ExecutorService executorPool = Executors.newFixedThreadPool(4);

        harmonicLoopWeighter.setHarmonicLoopWeight(LoopPositions.POSITION_TWO, currentProgressionUnit, previousProgressionUnit, null);

        //EXPECT EQ WEIGHTS & RYTHMIC WEIGHT TO BE SET on bars other than the barIndex
        WeightClass rythmicalWeight = currentProgressionUnit.getProgressionUnitBars().get(barIndex).getHarmonicLoopOptions().get(LoopPositions.POSITION_ONE).getRhythmicWeight();
        assertThat(rythmicalWeight).isEqualTo(WeightClass.GOOD);

        WeightClass eqWeight = currentProgressionUnit.getProgressionUnitBars().get(barIndex).getHarmonicLoopOptions().get(LoopPositions.POSITION_ONE).getEqWeight();
        assertThat(eqWeight).isNotNull();

//        WeightClass themeWeight = currentProgressionUnit.getProgressionUnitBars().get(barIndex).getHarmonicLoopOptions().get(LoopPositions.POSITION_ONE).getThemeWeight();
//        assertThat(themeWeight).isNotNull();
//
//        WeightClass timeSeriesWeight = currentProgressionUnit.getProgressionUnitBars().get(barIndex).getHarmonicLoopOptions().get(LoopPositions.POSITION_ONE).getTimeseriesWeight();
//        assertThat(timeSeriesWeight).isNotNull();

        rythmicalWeight = currentProgressionUnit.getProgressionUnitBars().get(barIndex).getHarmonicLoopOptions().get(LoopPositions.POSITION_TWO).getRhythmicWeight();
        assertThat(rythmicalWeight).isEqualTo(WeightClass.GOOD);

        eqWeight = currentProgressionUnit.getProgressionUnitBars().get(barIndex).getHarmonicLoopOptions().get(LoopPositions.POSITION_TWO).getEqWeight();
        assertThat(eqWeight).isNotNull();

//        themeWeight = currentProgressionUnit.getProgressionUnitBars().get(barIndex).getHarmonicLoopOptions().get(LoopPositions.POSITION_TWO).getThemeWeight();
//        assertThat(themeWeight).isNotNull();
//
//        timeSeriesWeight = currentProgressionUnit.getProgressionUnitBars().get(barIndex).getHarmonicLoopOptions().get(LoopPositions.POSITION_TWO).getTimeseriesWeight();
//        assertThat(timeSeriesWeight).isNotNull();

        rythmicalWeight = currentProgressionUnit.getProgressionUnitBars().get(barIndex).getHarmonicLoopOptions().get(LoopPositions.POSITION_THREE).getRhythmicWeight();
        assertThat(rythmicalWeight).isEqualTo(WeightClass.GOOD);

        eqWeight = currentProgressionUnit.getProgressionUnitBars().get(barIndex).getHarmonicLoopOptions().get(LoopPositions.POSITION_THREE).getEqWeight();
        assertThat(eqWeight).isNotNull();

//        themeWeight = currentProgressionUnit.getProgressionUnitBars().get(barIndex).getHarmonicLoopOptions().get(LoopPositions.POSITION_THREE).getThemeWeight();
//        assertThat(themeWeight).isNotNull();
//
//        timeSeriesWeight = currentProgressionUnit.getProgressionUnitBars().get(barIndex).getHarmonicLoopOptions().get(LoopPositions.POSITION_THREE).getTimeseriesWeight();
//        assertThat(timeSeriesWeight).isNotNull();


        //EXPECT THE CURRENT LOOP TO hAVE A TIMESERIES WEIGHTING


        //EXPECT THE PREVIOUS LOOP TO NOT HAVE A TIMESERIES WEIGHTING ..



        //EXPECT EQ WEIGHTS & RYTHMIC WEIGHT TO BE SET only for the target baridex

    }

    @Test
    public void should_setThemeWeights() throws CloneNotSupportedException {

        List<ProgressionUnit> progressionUnits = progressionUnitSetup();

        harmonicLoopWeighter.assignThemeWeight(LoopPositions.POSITION_ONE, progressionUnits.get(1), progressionUnits.get(0));

        assertThat(progressionUnits.get(1).getProgressionUnitBars()
                .get(LoopPositions.POSITION_ONE)
                .getHarmonicLoopOptions()
                .get(2)
                .getThemeWeight()).isNull();

        assertThat(progressionUnits.get(1).getProgressionUnitBars()
                .get(LoopPositions.POSITION_ONE)
                .getHarmonicLoopOptions()
                .get(3)
                .getThemeWeight()).isEqualTo(WeightClass.GOOD);

        progressionUnits = progressionUnitSetup();

        harmonicLoopWeighter.assignThemeWeight(LoopPositions.POSITION_TWO, progressionUnits.get(1), progressionUnits.get(0));

        assertThat(progressionUnits.get(1).getProgressionUnitBars()
                .get(LoopPositions.POSITION_TWO)
                .getHarmonicLoopOptions()
                .get(2)
                .getThemeWeight()).isNull();

        assertThat(progressionUnits.get(1).getProgressionUnitBars()
                .get(LoopPositions.POSITION_TWO)
                .getHarmonicLoopOptions()
                .get(3)
                .getThemeWeight()).isEqualTo(WeightClass.GOOD);

        progressionUnits = progressionUnitSetup();

        harmonicLoopWeighter.assignThemeWeight(LoopPositions.POSITION_THREE, progressionUnits.get(1), progressionUnits.get(0));

        assertThat(progressionUnits.get(1).getProgressionUnitBars()
                .get(LoopPositions.POSITION_THREE)
                .getHarmonicLoopOptions()
                .get(2)
                .getThemeWeight()).isNull();

        assertThat(progressionUnits.get(1).getProgressionUnitBars()
                .get(LoopPositions.POSITION_THREE)
                .getHarmonicLoopOptions()
                .get(3)
                .getThemeWeight()).isEqualTo(WeightClass.GOOD);

        progressionUnits = progressionUnitSetup();

        harmonicLoopWeighter.assignThemeWeight(LoopPositions.POSITION_FOUR, progressionUnits.get(1), progressionUnits.get(0));

        assertThat(progressionUnits.get(1).getProgressionUnitBars()
                .get(LoopPositions.POSITION_FOUR)
                .getHarmonicLoopOptions()
                .get(2)
                .getThemeWeight()).isNull();

        assertThat(progressionUnits.get(1).getProgressionUnitBars()
                .get(LoopPositions.POSITION_FOUR)
                .getHarmonicLoopOptions()
                .get(3)
                .getThemeWeight()).isEqualTo(WeightClass.GOOD);
    }

    @Test
    public void should_setTimeSeriesWeights() throws CloneNotSupportedException {

        List<ProgressionUnit> progressionUnits = progressionUnitSetup();

        harmonicLoopWeighter.assignTimeSeriesWeight(LoopPositions.POSITION_ONE, progressionUnits.get(1), progressionUnits.get(0));

        assertThat(progressionUnits.get(1).getProgressionUnitBars()
                .get(LoopPositions.POSITION_ONE)
                .getHarmonicLoopOptions()
                .get(2)
                .getTimeseriesWeight()).isEqualTo(WeightClass.OK);

        assertThat(progressionUnits.get(1).getProgressionUnitBars()
                .get(LoopPositions.POSITION_ONE)
                .getHarmonicLoopOptions()
                .get(3)
                .getTimeseriesWeight()).isEqualTo(WeightClass.OK);

        progressionUnits = progressionUnitSetup();

        harmonicLoopWeighter.assignTimeSeriesWeight(LoopPositions.POSITION_TWO, progressionUnits.get(1), progressionUnits.get(0));

        assertThat(progressionUnits.get(1).getProgressionUnitBars()
                .get(LoopPositions.POSITION_TWO)
                .getHarmonicLoopOptions()
                .get(2)
                .getTimeseriesWeight()).isEqualTo(WeightClass.OK);

        assertThat(progressionUnits.get(1).getProgressionUnitBars()
                .get(LoopPositions.POSITION_TWO)
                .getHarmonicLoopOptions()
                .get(3)
                .getTimeseriesWeight()).isEqualTo(WeightClass.OK);

        progressionUnits = progressionUnitSetup();

        harmonicLoopWeighter.assignTimeSeriesWeight(LoopPositions.POSITION_THREE, progressionUnits.get(1), progressionUnits.get(0));

        assertThat(progressionUnits.get(1).getProgressionUnitBars()
                .get(LoopPositions.POSITION_THREE)
                .getHarmonicLoopOptions()
                .get(2)
                .getTimeseriesWeight()).isEqualTo(WeightClass.OK);

        assertThat(progressionUnits.get(1).getProgressionUnitBars()
                .get(LoopPositions.POSITION_THREE)
                .getHarmonicLoopOptions()
                .get(3)
                .getTimeseriesWeight()).isEqualTo(WeightClass.OK);

        progressionUnits = progressionUnitSetup();

        harmonicLoopWeighter.assignTimeSeriesWeight(LoopPositions.POSITION_FOUR, progressionUnits.get(1), progressionUnits.get(0));

        assertThat(progressionUnits.get(1).getProgressionUnitBars()
                .get(LoopPositions.POSITION_FOUR)
                .getHarmonicLoopOptions()
                .get(2)
                .getTimeseriesWeight()).isEqualTo(WeightClass.OK);

        assertThat(progressionUnits.get(1).getProgressionUnitBars()
                .get(LoopPositions.POSITION_FOUR)
                .getHarmonicLoopOptions()
                .get(3)
                .getTimeseriesWeight()).isEqualTo(WeightClass.OK);
    }
}
