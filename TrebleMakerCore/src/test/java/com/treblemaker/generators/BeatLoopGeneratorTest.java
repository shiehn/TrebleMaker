package com.treblemaker.generators;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.constants.LoopPositions;
import com.treblemaker.generators.interfaces.IBeatLoopGenerator;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.RhythmicAccents;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.weighters.interfaces.IWeighter;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static com.treblemaker.model.progressions.ProgressionUnit.BarCount.FOUR;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class BeatLoopGeneratorTest extends TestCase {

    @Autowired
    private IBeatLoopGenerator beatLoopAltGenerator;

    @Autowired
    private IWeighter beatLoopAltWeighter;

    @Test
    public void ShouldSetBeatLoopAltWeights() {

        RhythmicAccents rhythmicAccentsOne = new RhythmicAccents();
        rhythmicAccentsOne.setOneOne("0");
        rhythmicAccentsOne.setTwoOne("1");
        List<RhythmicAccents> rhythmicAccentsListOne = new ArrayList<>();
        rhythmicAccentsListOne.add(rhythmicAccentsOne);

        RhythmicAccents rhythmicAccentsTwo = new RhythmicAccents();
        rhythmicAccentsTwo.setTwoOne("1");
        rhythmicAccentsTwo.setTwoTwo("1");
        List<RhythmicAccents> rhythmicAccentsListTwo = new ArrayList<>();
        rhythmicAccentsListTwo.add(rhythmicAccentsTwo);

        BeatLoop beatLoopOne = new BeatLoop();
        beatLoopOne.setRhythmicAccents(rhythmicAccentsListOne);

        BeatLoop beatLoopAltOneOption = new BeatLoop();
        beatLoopAltOneOption.setRhythmicAccents(rhythmicAccentsListTwo);

        BeatLoop beatLoopTwo = new BeatLoop();
        beatLoopTwo.setRhythmicAccents(rhythmicAccentsListOne);

        BeatLoop beatLoopAltTwoOption = new BeatLoop();
        beatLoopAltTwoOption.setRhythmicAccents(rhythmicAccentsListTwo);

        BeatLoop beatLoopThree = new BeatLoop();
        beatLoopThree.setRhythmicAccents(rhythmicAccentsListOne);

        BeatLoop beatLoopAltThreeOption = new BeatLoop();
        beatLoopAltThreeOption.setRhythmicAccents(rhythmicAccentsListTwo);

        BeatLoop beatLoopFour = new BeatLoop();
        beatLoopFour.setRhythmicAccents(rhythmicAccentsListOne);

        BeatLoop beatLoopAltFourOption = new BeatLoop();
        beatLoopAltFourOption.setRhythmicAccents(rhythmicAccentsListTwo);

        List<ProgressionUnit> progressionUnits = new ArrayList<>();
        ProgressionUnit progressionUnit = new ProgressionUnit();
        progressionUnit.initBars(FOUR.getValue());

        progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE).setBeatLoop(beatLoopOne);
        progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE).setBeatLoopAltOptions(new ArrayList<BeatLoop>() {
            {
                add(beatLoopAltOneOption);
            }
        });

        progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).setBeatLoop(beatLoopTwo);
        progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).setBeatLoopAltOptions(new ArrayList<BeatLoop>() {
            {
                add(beatLoopAltTwoOption);
            }
        });


        progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).setBeatLoop(beatLoopThree);
        progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).setBeatLoopAltOptions(new ArrayList<BeatLoop>() {
            {
                add(beatLoopAltThreeOption);
            }
        });

        progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).setBeatLoop(beatLoopFour);
        progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).setBeatLoopAltOptions(new ArrayList<BeatLoop>() {
            {
                add(beatLoopAltFourOption);
            }
        });

        progressionUnits.add(progressionUnit);

        beatLoopAltWeighter.setWeights(progressionUnits, null );

        progressionUnits.forEach(pUnit -> {
            pUnit.getProgressionUnitBars().forEach(pUnitBar -> {
                pUnitBar.getBeatLoopAltOptions().forEach(option -> {
                    Assert.assertNotSame(0, option.getTotalWeight());
                });
            });
        });
    }

    @Test
    public void ShouldGetMaxBarCount() {

        BeatLoop beatLoopAlt = new BeatLoop();
        ProgressionUnit progressionUnit = new ProgressionUnit();
        progressionUnit.initBars(FOUR.getValue());
        progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).setBeatLoopAlt(beatLoopAlt);

        int maxBarCount = beatLoopAltGenerator.getMaxBarCount(progressionUnit, LoopPositions.POSITION_TWO);
        Assert.assertEquals(3, maxBarCount);

        beatLoopAlt = new BeatLoop();
        progressionUnit = new ProgressionUnit();
        progressionUnit.initBars(FOUR.getValue());
        progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE).setBeatLoopAlt(beatLoopAlt);
        progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).setBeatLoopAlt(beatLoopAlt);

        maxBarCount = beatLoopAltGenerator.getMaxBarCount(progressionUnit, LoopPositions.POSITION_ONE);
        Assert.assertEquals(2, maxBarCount);

        beatLoopAlt = new BeatLoop();
        progressionUnit = new ProgressionUnit();
        progressionUnit.initBars(FOUR.getValue());
        progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).setBeatLoopAlt(beatLoopAlt);
        progressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).setBeatLoopAlt(beatLoopAlt);

        maxBarCount = beatLoopAltGenerator.getMaxBarCount(progressionUnit, LoopPositions.POSITION_FOUR);
        Assert.assertEquals(1, maxBarCount);
    }

    @Test
    public void ShouldSetBeatLoopAltTwoBars() {

        BeatLoop beatLoopAlt = new BeatLoop();
        beatLoopAlt.setFileName("twoBars");
        beatLoopAlt.setBarCount(2);

        ProgressionUnit progressionUnit = new ProgressionUnit();
        progressionUnit.initBars(FOUR.getValue());

        beatLoopAltGenerator.setBeatLoop(beatLoopAlt, 0, progressionUnit.getProgressionUnitBars());

        Assert.assertTrue(progressionUnit.getProgressionUnitBars().get(0).getBeatLoopAlt().getFileName().equalsIgnoreCase("twoBars"));
        Assert.assertEquals(1, progressionUnit.getProgressionUnitBars().get(0).getBeatLoopAlt().getCurrentBar());

        Assert.assertTrue(progressionUnit.getProgressionUnitBars().get(1).getBeatLoopAlt().getFileName().equalsIgnoreCase("twoBars"));
        Assert.assertEquals(2, progressionUnit.getProgressionUnitBars().get(1).getBeatLoopAlt().getCurrentBar());

        Assert.assertEquals(null, progressionUnit.getProgressionUnitBars().get(2).getBeatLoopAlt());
        Assert.assertEquals(null, progressionUnit.getProgressionUnitBars().get(3).getBeatLoopAlt());
    }

    @Test
    public void ShouldSetBeatLoopAltThreeBars() {

        BeatLoop beatLoopAlt = new BeatLoop();
        beatLoopAlt.setFileName("threeBars");
        beatLoopAlt.setBarCount(3);

        ProgressionUnit progressionUnit = new ProgressionUnit();
        progressionUnit.initBars(FOUR.getValue());

        beatLoopAltGenerator.setBeatLoop(beatLoopAlt, 1, progressionUnit.getProgressionUnitBars());

        Assert.assertEquals(null, progressionUnit.getProgressionUnitBars().get(0).getBeatLoopAlt());

        Assert.assertTrue(progressionUnit.getProgressionUnitBars().get(1).getBeatLoopAlt().getFileName().equalsIgnoreCase("threeBars"));
        Assert.assertEquals(1, progressionUnit.getProgressionUnitBars().get(1).getBeatLoopAlt().getCurrentBar());

        Assert.assertTrue(progressionUnit.getProgressionUnitBars().get(2).getBeatLoopAlt().getFileName().equalsIgnoreCase("threeBars"));
        Assert.assertEquals(2, progressionUnit.getProgressionUnitBars().get(2).getBeatLoopAlt().getCurrentBar());

        Assert.assertTrue(progressionUnit.getProgressionUnitBars().get(3).getBeatLoopAlt().getFileName().equalsIgnoreCase("threeBars"));
        Assert.assertEquals(3, progressionUnit.getProgressionUnitBars().get(3).getBeatLoopAlt().getCurrentBar());
    }
}