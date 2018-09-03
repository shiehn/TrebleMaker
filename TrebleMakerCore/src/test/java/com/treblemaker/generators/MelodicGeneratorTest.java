package com.treblemaker.generators;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.RhythmicAccents;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class MelodicGeneratorTest extends TestCase {

    @Autowired
    private MelodicGenerator melodicGenerator;

    @Test
    public void shouldExtract_Position0_2BarBeatLoop (){
        BeatLoop beatLoop = new BeatLoop();
        beatLoop.setBarCount(2);
        beatLoop.setRhythmicAccents(Arrays.asList(createAccentsByPosition(0), createAccentsByPosition(1)));

        ProgressionUnitBar progressionUnitBar = new ProgressionUnitBar();
        progressionUnitBar.setBeatLoop(beatLoop);

        RhythmicAccents result = melodicGenerator.getBeatLoopBarAccents(progressionUnitBar, 0);

        assertThat(result.getOneOne()).isEqualToIgnoringCase("1");
    }

    @Test
    public void shouldExtract_Position2_4BarBeatLoop (){
        BeatLoop beatLoop = new BeatLoop();
        beatLoop.setBarCount(4);
        beatLoop.setRhythmicAccents(Arrays.asList(createAccentsByPosition(0), createAccentsByPosition(1),
                createAccentsByPosition(2), createAccentsByPosition(3)));

        ProgressionUnitBar progressionUnitBar = new ProgressionUnitBar();
        progressionUnitBar.setBeatLoop(beatLoop);

        RhythmicAccents result = melodicGenerator.getBeatLoopBarAccents(progressionUnitBar, 2);

        assertThat(result.getThreeOne()).isEqualToIgnoringCase("1");
    }

    @Test
    public void shouldExtract_Position3_2BarBeatLoopAlt (){
        BeatLoop beatLoop = new BeatLoop();
        beatLoop.setBarCount(2);
        beatLoop.setRhythmicAccents(Arrays.asList(createAccentsByPosition(0), createAccentsByPosition(1)));

        ProgressionUnitBar progressionUnitBar = new ProgressionUnitBar();
        progressionUnitBar.setBeatLoopAlt(beatLoop);

        RhythmicAccents result = melodicGenerator.getBeatLoopAltBarAccents(progressionUnitBar, 3);

        assertThat(result.getTwoOne()).isEqualToIgnoringCase("1");
    }

    @Test
    public void shouldExtract_Position3_4BarBeatLoopAlt (){
        BeatLoop beatLoop = new BeatLoop();
        beatLoop.setBarCount(4);
        beatLoop.setRhythmicAccents(Arrays.asList(createAccentsByPosition(0), createAccentsByPosition(1),
                createAccentsByPosition(2), createAccentsByPosition(3)));

        ProgressionUnitBar progressionUnitBar = new ProgressionUnitBar();
        progressionUnitBar.setBeatLoopAlt(beatLoop);

        RhythmicAccents result = melodicGenerator.getBeatLoopAltBarAccents(progressionUnitBar, 3);

        assertThat(result.getFourOne()).isEqualToIgnoringCase("1");
    }

    @Test
    public void shouldExtract_Position1_2BarHarmonicLoop(){
        HarmonicLoop harmLoop = new HarmonicLoop();
        harmLoop.setBarCount(2);
        harmLoop.setRhythmicAccents(Arrays.asList(createAccentsByPosition(0), createAccentsByPosition(1)));

        ProgressionUnitBar progressionUnitBar = new ProgressionUnitBar();
        progressionUnitBar.setHarmonicLoop(harmLoop);

        RhythmicAccents result = melodicGenerator.getHarmonicLoopBarAccents(progressionUnitBar, 1);

        assertThat(result.getTwoOne()).isEqualToIgnoringCase("1");
    }

    @Test
    public void shouldExtract_Position2_4BarHarmonicLoop (){
        HarmonicLoop harmLoop = new HarmonicLoop();
        harmLoop.setBarCount(4);
        harmLoop.setRhythmicAccents(Arrays.asList(createAccentsByPosition(0), createAccentsByPosition(1),
                createAccentsByPosition(2), createAccentsByPosition(3)));

        ProgressionUnitBar progressionUnitBar = new ProgressionUnitBar();
        progressionUnitBar.setHarmonicLoop(harmLoop);

        RhythmicAccents result = melodicGenerator.getHarmonicLoopBarAccents(progressionUnitBar, 2);

        assertThat(result.getThreeOne()).isEqualToIgnoringCase("1");
    }

    @Test
    public void shouldExtract_Position0_1BarHarmonicLoopAlt (){
        HarmonicLoop harmLoop = new HarmonicLoop();
        harmLoop.setBarCount(1);
        harmLoop.setRhythmicAccents(Arrays.asList(createAccentsByPosition(0)));

        ProgressionUnitBar progressionUnitBar = new ProgressionUnitBar();
        progressionUnitBar.setHarmonicLoopAlt(harmLoop);

        RhythmicAccents result = melodicGenerator.getHarmonicLoopAltBarAccents(progressionUnitBar, 0);

        assertThat(result.getOneOne()).isEqualToIgnoringCase("1");
    }

    @Test
    public void shouldExtract_Position2_2BarHarmonicLoopAlt (){
        HarmonicLoop harmLoop = new HarmonicLoop();
        harmLoop.setBarCount(2);
        harmLoop.setRhythmicAccents(Arrays.asList(createAccentsByPosition(0),createAccentsByPosition(1)));

        ProgressionUnitBar progressionUnitBar = new ProgressionUnitBar();
        progressionUnitBar.setHarmonicLoopAlt(harmLoop);

        RhythmicAccents result = melodicGenerator.getHarmonicLoopAltBarAccents(progressionUnitBar, 2);

        assertThat(result.getOneOne()).isEqualToIgnoringCase("1");
    }

    @Test
    public void shouldSumAccentsCorrectly() {

        RhythmicAccents rhythmicAccentsOne = new RhythmicAccents();
        rhythmicAccentsOne.setOneOne("1");
        rhythmicAccentsOne.setOneTwo("1");
        rhythmicAccentsOne.setOneThree("1");
        rhythmicAccentsOne.setOneFour("1");

        rhythmicAccentsOne.setTwoOne("0");
        rhythmicAccentsOne.setTwoTwo("0");
        rhythmicAccentsOne.setTwoThree("0");
        rhythmicAccentsOne.setTwoFour("0");

        rhythmicAccentsOne.setThreeOne("0");
        rhythmicAccentsOne.setThreeTwo("0");
        rhythmicAccentsOne.setThreeThree("0");
        rhythmicAccentsOne.setThreeFour("0");

        rhythmicAccentsOne.setFourOne("0");
        rhythmicAccentsOne.setFourTwo("0");
        rhythmicAccentsOne.setFourThree("0");
        rhythmicAccentsOne.setFourFour("0");

        RhythmicAccents rhythmicAccentsTwo = new RhythmicAccents();
        rhythmicAccentsTwo.setOneOne("1");
        rhythmicAccentsTwo.setOneTwo("1");
        rhythmicAccentsTwo.setOneThree("1");
        rhythmicAccentsTwo.setOneFour("1");

        rhythmicAccentsTwo.setTwoOne("1");
        rhythmicAccentsTwo.setTwoTwo("1");
        rhythmicAccentsTwo.setTwoThree("1");
        rhythmicAccentsTwo.setTwoFour("1");

        rhythmicAccentsTwo.setThreeOne("0");
        rhythmicAccentsTwo.setThreeTwo("0");
        rhythmicAccentsTwo.setThreeThree("0");
        rhythmicAccentsTwo.setThreeFour("0");

        rhythmicAccentsTwo.setFourOne("0");
        rhythmicAccentsTwo.setFourTwo("0");
        rhythmicAccentsTwo.setFourThree("0");
        rhythmicAccentsTwo.setFourFour("0");

        RhythmicAccents rhythmicAccentsThree = new RhythmicAccents();
        rhythmicAccentsThree.setOneOne("1");
        rhythmicAccentsThree.setOneTwo("1");
        rhythmicAccentsThree.setOneThree("1");
        rhythmicAccentsThree.setOneFour("1");

        rhythmicAccentsThree.setTwoOne("1");
        rhythmicAccentsThree.setTwoTwo("1");
        rhythmicAccentsThree.setTwoThree("1");
        rhythmicAccentsThree.setTwoFour("1");

        rhythmicAccentsThree.setThreeOne("1");
        rhythmicAccentsThree.setThreeTwo("1");
        rhythmicAccentsThree.setThreeThree("1");
        rhythmicAccentsThree.setThreeFour("1");

        rhythmicAccentsThree.setFourOne("0");
        rhythmicAccentsThree.setFourTwo("0");
        rhythmicAccentsThree.setFourThree("0");
        rhythmicAccentsThree.setFourFour("0");

        RhythmicAccents rhythmicAccentsFour = new RhythmicAccents();
        rhythmicAccentsFour.setOneOne("1");
        rhythmicAccentsFour.setOneTwo("1");
        rhythmicAccentsFour.setOneThree("1");
        rhythmicAccentsFour.setOneFour("1");

        rhythmicAccentsFour.setTwoOne("1");
        rhythmicAccentsFour.setTwoTwo("1");
        rhythmicAccentsFour.setTwoThree("1");
        rhythmicAccentsFour.setTwoFour("1");

        rhythmicAccentsFour.setThreeOne("1");
        rhythmicAccentsFour.setThreeTwo("1");
        rhythmicAccentsFour.setThreeThree("1");
        rhythmicAccentsFour.setThreeFour("1");

        rhythmicAccentsFour.setFourOne("1");
        rhythmicAccentsFour.setFourTwo("1");
        rhythmicAccentsFour.setFourThree("1");
        rhythmicAccentsFour.setFourFour("1");

        RhythmicAccents summedAccents = melodicGenerator.sumRhythmicAccents(Arrays.asList(rhythmicAccentsOne,
                rhythmicAccentsTwo,
                rhythmicAccentsThree,
                rhythmicAccentsFour));

        assertThat(summedAccents.getOneOne()).isEqualToIgnoringCase("4");
        assertThat(summedAccents.getOneTwo()).isEqualToIgnoringCase("4");
        assertThat(summedAccents.getOneThree()).isEqualToIgnoringCase("4");
        assertThat(summedAccents.getOneFour()).isEqualToIgnoringCase("4");

        assertThat(summedAccents.getTwoOne()).isEqualToIgnoringCase("3");
        assertThat(summedAccents.getTwoTwo()).isEqualToIgnoringCase("3");
        assertThat(summedAccents.getTwoThree()).isEqualToIgnoringCase("3");
        assertThat(summedAccents.getTwoFour()).isEqualToIgnoringCase("3");

        assertThat(summedAccents.getThreeOne()).isEqualToIgnoringCase("2");
        assertThat(summedAccents.getThreeTwo()).isEqualToIgnoringCase("2");
        assertThat(summedAccents.getThreeThree()).isEqualToIgnoringCase("2");
        assertThat(summedAccents.getThreeFour()).isEqualToIgnoringCase("2");

        assertThat(summedAccents.getFourOne()).isEqualToIgnoringCase("1");
        assertThat(summedAccents.getFourTwo()).isEqualToIgnoringCase("1");
        assertThat(summedAccents.getFourThree()).isEqualToIgnoringCase("1");
        assertThat(summedAccents.getFourFour()).isEqualToIgnoringCase("1");
    }

    @Test
    public void shouldSelectCorectMean() {
        RhythmicAccents rhythmicAccentsOne = new RhythmicAccents();
        rhythmicAccentsOne.setOneOne("10");
        rhythmicAccentsOne.setOneTwo("8");
        rhythmicAccentsOne.setOneThree("1");
        rhythmicAccentsOne.setOneFour("0");

        rhythmicAccentsOne.setTwoOne("0");
        rhythmicAccentsOne.setTwoTwo("5");
        rhythmicAccentsOne.setTwoThree("3");
        rhythmicAccentsOne.setTwoFour("2");

        rhythmicAccentsOne.setThreeOne("4");
        rhythmicAccentsOne.setThreeTwo("6");
        rhythmicAccentsOne.setThreeThree("1");
        rhythmicAccentsOne.setThreeFour("0");

        rhythmicAccentsOne.setFourOne("0");
        rhythmicAccentsOne.setFourTwo("0");
        rhythmicAccentsOne.setFourThree("3");
        rhythmicAccentsOne.setFourFour("5");

        assertThat(melodicGenerator.extractMeanfromAccents(rhythmicAccentsOne)).isEqualTo(3);
    }

    @Test
    public void shouldSelectHigherThatMeanAsAccents() {
        RhythmicAccents rhythmicAccentsOne = new RhythmicAccents();
        rhythmicAccentsOne.setOneOne("10");
        rhythmicAccentsOne.setOneTwo("8");
        rhythmicAccentsOne.setOneThree("1");
        rhythmicAccentsOne.setOneFour("0");

        rhythmicAccentsOne.setTwoOne("0");
        rhythmicAccentsOne.setTwoTwo("5");
        rhythmicAccentsOne.setTwoThree("3");
        rhythmicAccentsOne.setTwoFour("2");

        rhythmicAccentsOne.setThreeOne("4");
        rhythmicAccentsOne.setThreeTwo("6");
        rhythmicAccentsOne.setThreeThree("1");
        rhythmicAccentsOne.setThreeFour("0");

        rhythmicAccentsOne.setFourOne("0");
        rhythmicAccentsOne.setFourTwo("0");
        rhythmicAccentsOne.setFourThree("3");
        rhythmicAccentsOne.setFourFour("5");

        //average 3
        int mean = 3;

        RhythmicAccents accents = melodicGenerator.setAccentsAboveMean(rhythmicAccentsOne, 3);

        assertThat(accents.getOneOne()).isEqualTo("1");
        assertThat(accents.getOneTwo()).isEqualTo("1");
        assertThat(accents.getOneThree()).isEqualTo("0");
        assertThat(accents.getOneFour()).isEqualTo("0");

        assertThat(accents.getTwoOne()).isEqualTo("0");
        assertThat(accents.getTwoTwo()).isEqualTo("1");
        assertThat(accents.getTwoThree()).isEqualTo("0");
        assertThat(accents.getTwoFour()).isEqualTo("0");

        assertThat(accents.getThreeOne()).isEqualTo("1");
        assertThat(accents.getThreeTwo()).isEqualTo("1");
        assertThat(accents.getThreeThree()).isEqualTo("0");
        assertThat(accents.getThreeFour()).isEqualTo("0");

        assertThat(accents.getFourOne()).isEqualTo("0");
        assertThat(accents.getFourTwo()).isEqualTo("0");
        assertThat(accents.getFourThree()).isEqualTo("0");
        assertThat(accents.getFourFour()).isEqualTo("1");
    }

    private RhythmicAccents createAccentsByPosition(int position){
        RhythmicAccents accents = new RhythmicAccents();

        switch (position){
            case 0:
                accents.setOneOne("1");
                return accents;
            case 1:
                accents.setTwoOne("1");
                return accents;
            case 2:
                accents.setThreeOne("1");
                return accents;
            case 3:
                accents.setFourOne("1");
                return accents;
            default:
                throw new RuntimeException("OUT OF BOUNDS");
        }
    }
}