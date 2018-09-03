package com.treblemaker.generators;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.generators.interfaces.ISynthBassGenerator;
import com.treblemaker.model.bassline.BasslineWithRating;
import com.treblemaker.model.bassline.Intervals;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.weighters.enums.WeightClass;
import junit.framework.TestCase;
import org.jfugue.theory.Chord;
import org.jfugue.theory.Note;
import org.junit.Before;
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
import java.util.stream.Collectors;

import static com.treblemaker.model.progressions.ProgressionUnit.BarCount.FOUR;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class SynthBassOptionsTest extends TestCase {

    @Autowired
    private ISynthBassGenerator synthBassGenerator;

    private ProgressionUnit progressionUnit;

    @Before
    public void setup(){

        progressionUnit = new ProgressionUnit();
        progressionUnit.initBars(FOUR.getValue());
        progressionUnit.setType(ProgressionUnit.ProgressionType.VERSE);

        progressionUnit.getProgressionUnitBars().get(0).setjChord(new Chord("cmaj7"));
        progressionUnit.getProgressionUnitBars().get(0).setHiSynthId(Arrays.asList(11));
        progressionUnit.getProgressionUnitBars().get(0).setMidSynthId(Arrays.asList(2));
        progressionUnit.getProgressionUnitBars().get(0).setLowSynthId(Arrays.asList(3));

        progressionUnit.getProgressionUnitBars().get(1).setjChord(new Chord("cmaj"));
        progressionUnit.getProgressionUnitBars().get(1).setHiSynthId(Arrays.asList(11));
        progressionUnit.getProgressionUnitBars().get(1).setMidSynthId(Arrays.asList(22));
        progressionUnit.getProgressionUnitBars().get(1).setLowSynthId(Arrays.asList(33));

        progressionUnit.getProgressionUnitBars().get(2).setjChord(new Chord("gmin"));
        progressionUnit.getProgressionUnitBars().get(2).setHiSynthId(Arrays.asList(11));
        progressionUnit.getProgressionUnitBars().get(2).setMidSynthId(Arrays.asList(2));
        progressionUnit.getProgressionUnitBars().get(2).setLowSynthId(Arrays.asList(3));

        progressionUnit.getProgressionUnitBars().get(3).setjChord(new Chord("gmin7"));
        progressionUnit.getProgressionUnitBars().get(3).setHiSynthId(Arrays.asList(11));
        progressionUnit.getProgressionUnitBars().get(3).setMidSynthId(Arrays.asList(22));
        progressionUnit.getProgressionUnitBars().get(3).setLowSynthId(Arrays.asList(33));
    }

    @Test
    public void should_distribute_basslines(){

        BasslineWithRating bassA = new BasslineWithRating();
        bassA.incrementWeight(WeightClass.BAD);

        BasslineWithRating bassB = new BasslineWithRating();
        bassB.incrementWeight(WeightClass.OK);

        BasslineWithRating bassC = new BasslineWithRating();
        bassC.incrementWeight(WeightClass.GOOD);

        List<BasslineWithRating> inputOptions = Arrays.asList(bassA, bassB, bassC);

        List<BasslineWithRating> distributedOptions = synthBassGenerator.distributeByWeights(inputOptions);

        List<BasslineWithRating> oneRated = distributedOptions.stream().filter(options -> options.getTotalWeight() == 1).collect(Collectors.toList());
        List<BasslineWithRating> twoRated = distributedOptions.stream().filter(options -> options.getTotalWeight() == 2).collect(Collectors.toList());

        assertThat(distributedOptions.size()).isEqualTo(3);
        assertThat(oneRated.size()).isEqualTo(1);
        assertThat(twoRated.size()).isEqualTo(2);
    }

    @Test
    public void shouldConvertToNoteMap() {

        Intervals intervalsA = new Intervals();
        intervalsA.setNoteOne(new Note("DB"));
        intervalsA.setNoteTwo(new Note("E4"));
        intervalsA.setNoteThree(new Note("G#4"));
        intervalsA.setNoteFour(new Note("G#4"));
        intervalsA.setNoteFive(new Note("DB"));

        Intervals intervalsB = new Intervals();
        intervalsB.setNoteOne(new Note("B"));
        intervalsB.setNoteTwo(new Note("D5"));
        intervalsB.setNoteThree(new Note("F#5"));
        intervalsB.setNoteFour(new Note("A5"));
        intervalsB.setNoteFive(new Note("B"));

        int[] arrpegio = {3, 0, 0, 0,
                0, 5, 0, 3,
                0, 1, 0, 3,
                3, 0, 0, 0,
                3, 0, 0, 0,
                0, 5, 0, 3,
                0, 1, 0, 3,
                3, 0, 0, 0};

        List<String> output = synthBassGenerator.convertToNoteMap(arrpegio, intervalsA, intervalsB);

        assertThat(output).hasSize(32);
        assertThat(output.get(0)).isNotEqualToIgnoringCase("R");
        assertThat(output.get(1)).isEqualToIgnoringCase("R");

        assertThat(output.get(11)).isNotEqualToIgnoringCase("R");
        assertThat(output.get(12)).isEqualToIgnoringCase("R");

        assertThat(output.get(27)).isNotEqualToIgnoringCase("R");
        assertThat(output.get(28)).isEqualToIgnoringCase("R");
    }

    @Test
    public void shouldTransposeToCorrectOctave(){

        int bassOctave = 2;

        List<String> chords = Arrays.asList("gmaj7", "f#dom7", "dbmin7", "c5maj7", "c#5maj", "cb4min");

        assertThat(synthBassGenerator.transposeChord(chords.get(0), bassOctave)).isEqualTo("g2maj7");
        assertThat(synthBassGenerator.transposeChord(chords.get(1), bassOctave)).isEqualTo("f#2dom7");
        assertThat(synthBassGenerator.transposeChord(chords.get(2), bassOctave)).isEqualTo("db2min7");
        assertThat(synthBassGenerator.transposeChord(chords.get(3), bassOctave)).isEqualTo("c2maj7");
        assertThat(synthBassGenerator.transposeChord(chords.get(4), bassOctave)).isEqualTo( "c#2maj");
        assertThat(synthBassGenerator.transposeChord(chords.get(5), bassOctave)).isEqualTo("cb2min");
    }
}