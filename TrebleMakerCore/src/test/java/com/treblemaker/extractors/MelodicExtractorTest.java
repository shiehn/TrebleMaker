package com.treblemaker.extractors;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.extractors.model.HarmonyExtraction;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.PitchExtractions;
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
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class MelodicExtractorTest extends TestCase {

    @Autowired
    private MelodicExtractor melodicExtractor;

    ProgressionUnitBar progressionUnitBar;

    public void setupFourBarExample() {

        PitchExtractions peOne = new PitchExtractions();
        PitchExtractions peTwo = new PitchExtractions();
        peTwo.setTwoOne("A-B");
        PitchExtractions peThree = new PitchExtractions();
        PitchExtractions peFour = new PitchExtractions();

        HarmonicLoop harmonicLoop = new HarmonicLoop();
        harmonicLoop.setBarCount(4);
        harmonicLoop.setCurrentBar(3);
        harmonicLoop.setPitchExtractions(Arrays.asList(peOne, peTwo, peThree, peFour));

        PitchExtractions peOneB = new PitchExtractions();
        peOneB.setTwoOne("C-D");
        HarmonicLoop harmonicLoopAlt = new HarmonicLoop();
        harmonicLoopAlt.setBarCount(1);
        harmonicLoopAlt.setCurrentBar(1);
        harmonicLoopAlt.setPitchExtractions(Arrays.asList(peOneB));

        List<String> arpeggioHiPositions = Arrays.asList("0", "0", "0", "0", "H", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
        List<String> arpeggioLowPositions = Arrays.asList("0", "0", "0", "0", "L", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");

        progressionUnitBar = new ProgressionUnitBar();
        progressionUnitBar.setChord(new HiveChord("cmaj7"));
        progressionUnitBar.setArpeggioHiPositions(arpeggioHiPositions);
        progressionUnitBar.setArpeggioLowPositions(arpeggioLowPositions);
        progressionUnitBar.setHarmonicLoop(harmonicLoop);
        progressionUnitBar.setHarmonicLoopAlt(harmonicLoopAlt);
    }

    public void setupTwoBarExample() {

        PitchExtractions peOne = new PitchExtractions();
        peOne.setTwoOne("A-B");
        PitchExtractions peTwo = new PitchExtractions();

        HarmonicLoop harmonicLoop = new HarmonicLoop();
        harmonicLoop.setBarCount(2);
        harmonicLoop.setCurrentBar(3);
        harmonicLoop.setPitchExtractions(Arrays.asList(peOne, peTwo));

        PitchExtractions peOneB = new PitchExtractions();
        peOneB.setTwoOne("C-D");
        HarmonicLoop harmonicLoopAlt = new HarmonicLoop();
        harmonicLoopAlt.setBarCount(1);
        harmonicLoopAlt.setCurrentBar(3);
        harmonicLoopAlt.setPitchExtractions(Arrays.asList(peOneB));

        List<String> arpeggioHiPositions = Arrays.asList("0", "0", "0", "0", "H", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
        List<String> arpeggioLowPositions = Arrays.asList("0", "0", "0", "0", "L", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");

        progressionUnitBar = new ProgressionUnitBar();
        progressionUnitBar.setChord(new HiveChord("cmaj7"));
        progressionUnitBar.setArpeggioHiPositions(arpeggioHiPositions);
        progressionUnitBar.setArpeggioLowPositions(arpeggioLowPositions);
        progressionUnitBar.setHarmonicLoop(harmonicLoop);
        progressionUnitBar.setHarmonicLoopAlt(harmonicLoopAlt);
    }

    @Test
    public void shouldExtractHarmoniesAtIndexFromFourBarLoop() {
        setupFourBarExample();

        int noteIndex = 4;
        HarmonyExtraction extraction = melodicExtractor.extractedAndSetHarmony(progressionUnitBar, noteIndex);
        assertThat(extraction.getChordname()).isEqualToIgnoringCase("cmaj7");
        assertThat(extraction.getHarmonies()).isEqualTo(Arrays.asList("L", "H", "A","B","C","D"));
    }

    @Test
    public void shouldExtractHarmoniesAtIndexFromTwoBarLoop() {
        setupTwoBarExample();

        int noteIndex = 4;
        HarmonyExtraction extraction = melodicExtractor.extractedAndSetHarmony(progressionUnitBar, noteIndex);
        assertThat(extraction.getChordname()).isEqualToIgnoringCase("cmaj7");
        assertThat(extraction.getHarmonies()).isEqualTo(Arrays.asList("L", "H", "C","D"));
    }

    @Test
    public void shouldGetCurrentBarFor2BarLoops(){
        int resultOne = melodicExtractor.getCurrentBarIndex(2, 1, 3);
        int resultTwo = melodicExtractor.getCurrentBarIndex(2, 2, 7);
        int resultThree = melodicExtractor.getCurrentBarIndex(2, 3, 11);
        int resultFour = melodicExtractor.getCurrentBarIndex(2, 4, 15);

        assertThat(resultOne).isEqualTo(0);
        assertThat(resultTwo).isEqualTo(1);
        assertThat(resultThree).isEqualTo(0);
        assertThat(resultFour).isEqualTo(1);
    }

    @Test
    public void shouldGetCurrentBarFor1BarLoops(){
        int resultOne = melodicExtractor.getCurrentBarIndex(1, 1, 1);
        assertThat(resultOne).isEqualTo(0);
    }

    @Test
    public void shouldGetCurrentBarFor4BarLoops() {
        int resultOne = melodicExtractor.getCurrentBarIndex(4, 4, 13);
        assertThat(resultOne).isEqualTo(3);
    }
}