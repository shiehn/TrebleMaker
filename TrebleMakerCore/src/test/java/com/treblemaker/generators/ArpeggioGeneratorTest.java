package com.treblemaker.generators;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.generators.interfaces.IArpeggioGenerator;
import com.treblemaker.model.SourceData;
import com.treblemaker.model.arpeggio.Arpeggio;
import com.treblemaker.model.bassline.Intervals;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.weighters.enums.WeightClass;
import junit.framework.TestCase;
import org.jfugue.theory.Chord;
import org.jfugue.theory.Note;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class ArpeggioGeneratorTest extends TestCase {

    @Autowired
    private IArpeggioGenerator arpeggioGenerator;

    private List<ProgressionUnit> getProgressionUnits() {

        List<ProgressionUnitBar> cBars = new ArrayList<>();

        cBars.add(new ProgressionUnitBar() {{
            setjChord(new Chord("emin"));
        }});

        cBars.add(new ProgressionUnitBar() {{
            setjChord(new Chord("emin"));
        }});

        cBars.add(new ProgressionUnitBar() {{
            setjChord(new Chord("dmaj"));
        }});

        cBars.add(new ProgressionUnitBar() {{
            setjChord(new Chord("dmaj"));
        }});

        List<ProgressionUnitBar> vBars = new ArrayList<>();

        vBars.add(new ProgressionUnitBar() {{
            setjChord(new Chord("F#maj7"));
        }});

        vBars.add(new ProgressionUnitBar() {{
            setjChord(new Chord("dmin7"));
        }});

        vBars.add(new ProgressionUnitBar() {{
            setjChord(new Chord("F#maj7"));
        }});

        vBars.add(new ProgressionUnitBar() {{
            setjChord(new Chord("gmaj7"));
        }});

        ProgressionUnit v1 = new ProgressionUnit();
        v1.setType(ProgressionUnit.ProgressionType.VERSE);
        v1.setProgressionUnitBars(vBars);

        ProgressionUnit v2 = new ProgressionUnit();
        v2.setType(ProgressionUnit.ProgressionType.VERSE);
        v2.setProgressionUnitBars(vBars);

        ProgressionUnit c1 = new ProgressionUnit();
        c1.setType(ProgressionUnit.ProgressionType.CHORUS);
        c1.setProgressionUnitBars(cBars);

        ProgressionUnit c2 = new ProgressionUnit();
        c2.setType(ProgressionUnit.ProgressionType.CHORUS);
        c2.setProgressionUnitBars(cBars);

        List<ProgressionUnit> progressionUnits = new ArrayList<>();
        progressionUnits.add(v1);
        progressionUnits.add(v2);
        progressionUnits.add(c1);
        progressionUnits.add(v1);
        progressionUnits.add(v2);
        progressionUnits.add(c2);

        return progressionUnits;
    }

    private List<Arpeggio> getArpeggios() {

        List<Arpeggio> arpeggios = new ArrayList<>();
        Arpeggio arpeggio1 = new Arpeggio();
        arpeggio1.setArpeggioPerBar(33);
        arpeggio1.setChordTypeOne(5);
        arpeggio1.setChordTypeTwo(12);
        arpeggio1.setArpeggioJson("{\"arpeggio\":[0,2,3,1,1,2,0,0,0,0,0,0,0,0,0,0,0,2,3,1,1,2,0,0,0,0,0,0,0,0,0,0]}");

        Arpeggio arpeggio2 = new Arpeggio();
        arpeggio2.setArpeggioPerBar(44);
        arpeggio2.setChordTypeOne(9);
        arpeggio2.setChordTypeTwo(4);
        arpeggio2.setArpeggioJson("{\"arpeggio\":[0,4,5,0,0,5,4,0,3,5,0,0,0,0,0,0,0,4,5,0,0,5,4,0,3,5,0,0,0,0,0,0]}");

        Arpeggio arpeggio3 = new Arpeggio();
        arpeggio3.setArpeggioPerBar(34);
        arpeggio3.setChordTypeOne(14);
        arpeggio3.setChordTypeTwo(3);
        arpeggio3.setArpeggioJson("{\"arpeggio\":[1,1,0,5,2,0,0,2,0,0,0,0,0,0,0,0,1,1,0,5,2,0,0,2,0,0,0,0,0,0,0,0]}");

        Arpeggio arpeggio4 = new Arpeggio();
        arpeggio4.setArpeggioPerBar(43);
        arpeggio4.setChordTypeOne(2);
        arpeggio4.setChordTypeTwo(1);
        arpeggio4.setArpeggioJson("{\"arpeggio\":[0,0,2,1,2,5,2,0,4,0,3,0,2,3,0,0,0,0,2,1,2,5,2,0,1,0,3,0,2,3,0,0]}");

        Arpeggio arpeggio5 = new Arpeggio();
        arpeggio5.setArpeggioPerBar(33);
        arpeggio5.setChordTypeOne(2);
        arpeggio5.setChordTypeTwo(1);
        arpeggio5.setArpeggioJson("{\"arpeggio\":[0,0,2,1,2,5,2,0,4,0,3,0,2,3,0,0,0,0,2,1,2,5,2,0,1,0,3,0,2,3,0,0]}");

        arpeggios.add(arpeggio1);
        arpeggios.add(arpeggio2);
        arpeggios.add(arpeggio3);
        arpeggios.add(arpeggio4);
        arpeggios.add(arpeggio5);

        return arpeggios;
    }

    @Test
    public void shouldGetArpeggioChorus() {

        SourceData sourceData = new SourceData();
        sourceData.setArpeggios(getArpeggios());

        List<ProgressionUnit> progressionUnits = getProgressionUnits();

        Map<Integer, List<Arpeggio>> arpeggioOptions = arpeggioGenerator.getArpeggioOptions(progressionUnits, ProgressionUnit.ProgressionType.CHORUS, sourceData);

        assertEquals(2, arpeggioOptions.get(33).size());
    }

    @Test
    public void shouldGetArpeggioVerse() {

        SourceData sourceData = new SourceData();
        sourceData.setArpeggios(getArpeggios());

        List<ProgressionUnit> progressionUnits = getProgressionUnits();

        Map<Integer, List<Arpeggio>> arpeggioOptions = arpeggioGenerator.getArpeggioOptions(progressionUnits, ProgressionUnit.ProgressionType.VERSE, sourceData);

        assertEquals(1, arpeggioOptions.get(44).size());
    }

    @Test
    public void shouldSelectHigestRated() {

        Arpeggio arpeggioOne = new Arpeggio();
        arpeggioOne.setId(1);
        arpeggioOne.incrementWeight(WeightClass.OK);
        arpeggioOne.incrementWeight(WeightClass.OK);
        arpeggioOne.incrementWeight(WeightClass.GOOD);

        Arpeggio arpeggioTwo = new Arpeggio();
        arpeggioTwo.setId(2);
        arpeggioTwo.incrementWeight(WeightClass.GOOD);
        arpeggioTwo.incrementWeight(WeightClass.GOOD);
        arpeggioTwo.incrementWeight(WeightClass.GOOD);

        Arpeggio arpeggioThree = new Arpeggio();
        arpeggioThree.setId(3);
        arpeggioThree.incrementWeight(WeightClass.GOOD);

        List<Arpeggio> listOne = new ArrayList<>();
        listOne.add(arpeggioOne);

        List<Arpeggio> listTwo = new ArrayList<>();
        listTwo.add(arpeggioOne);
        listTwo.add(arpeggioTwo);

        List<Arpeggio> listThree = new ArrayList<>();
        listThree.add(arpeggioOne);
        listThree.add(arpeggioTwo);
        listThree.add(arpeggioThree);

        Map<Integer, List<Arpeggio>> ratedArpeggioInput = new HashMap<>();
        ratedArpeggioInput.put(1, listOne);
        ratedArpeggioInput.put(2, listTwo);
        ratedArpeggioInput.put(3, listThree);

        Map<Integer, Arpeggio> highestRated = arpeggioGenerator.selectFromARateDistributedList(ratedArpeggioInput);

        assertThat(highestRated.size()).isEqualTo(3); 
    }

    @Test
    public void should_distribute_basslines() {

        Arpeggio arpeggioA = new Arpeggio();
        arpeggioA.incrementWeight(WeightClass.BAD);

        Arpeggio arpeggioB = new Arpeggio();
        arpeggioB.incrementWeight(WeightClass.OK);

        Arpeggio arpeggioC = new Arpeggio();
        arpeggioC.incrementWeight(WeightClass.GOOD);

        List<Arpeggio> inputOptions = Arrays.asList(arpeggioA, arpeggioB, arpeggioC);

        List<Arpeggio> distributedOptions = arpeggioGenerator.distributeWeightedArpeggios(inputOptions);

        List<Arpeggio> oneRated = distributedOptions.stream().filter(options -> options.getTotalWeight() == 1).collect(Collectors.toList());
        List<Arpeggio> twoRated = distributedOptions.stream().filter(options -> options.getTotalWeight() == 2).collect(Collectors.toList());

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

        List<String> output = arpeggioGenerator.convertToNoteMap(arrpegio, intervalsA, intervalsB);

        assertThat(output).hasSize(32);
        assertThat(output.get(0)).isNotEqualToIgnoringCase("R");
        assertThat(output.get(1)).isEqualToIgnoringCase("R");

        assertThat(output.get(11)).isNotEqualToIgnoringCase("R");
        assertThat(output.get(12)).isEqualToIgnoringCase("R");

        assertThat(output.get(27)).isNotEqualToIgnoringCase("R");
        assertThat(output.get(28)).isEqualToIgnoringCase("R");
    }
}
