package com.treblemaker.generators;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.comp.CompRhythm;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.weighters.enums.WeightClass;
import junit.framework.TestCase;
import org.jfugue.theory.Chord;
import org.jfugue.theory.Note;
import org.junit.Assert;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.treblemaker.model.progressions.ProgressionUnit.BarCount.FOUR;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class SynthPadGeneratorTest extends TestCase {

    private ProgressionUnit progressionUnitVerse;
    private ProgressionUnit progressionUnitChorus;

    @Autowired
    private SynthPadGenerator synthPadGenerator;

    private Map<ProgressionUnit.ProgressionType, CompRhythm> selectedCompOptions;

    @Before
    public void setup(){

        //VERSE
        progressionUnitVerse = new ProgressionUnit();
        progressionUnitVerse.initBars(FOUR.getValue());
        progressionUnitVerse.setType(ProgressionUnit.ProgressionType.VERSE);

        progressionUnitVerse.getProgressionUnitBars().get(0).setjChord(new Chord("cmaj"));
        progressionUnitVerse.getProgressionUnitBars().get(0).setHiSynthId(Arrays.asList(11));
        progressionUnitVerse.getProgressionUnitBars().get(0).setMidSynthId(Arrays.asList(2));
        progressionUnitVerse.getProgressionUnitBars().get(0).setLowSynthId(Arrays.asList(3));

        progressionUnitVerse.getProgressionUnitBars().get(1).setjChord(new Chord("dmin"));
        progressionUnitVerse.getProgressionUnitBars().get(1).setHiSynthId(Arrays.asList(11));
        progressionUnitVerse.getProgressionUnitBars().get(1).setMidSynthId(Arrays.asList(22));
        progressionUnitVerse.getProgressionUnitBars().get(1).setLowSynthId(Arrays.asList(33));

        progressionUnitVerse.getProgressionUnitBars().get(2).setjChord(new Chord("f#maj"));
        progressionUnitVerse.getProgressionUnitBars().get(2).setHiSynthId(Arrays.asList(11));
        progressionUnitVerse.getProgressionUnitBars().get(2).setMidSynthId(Arrays.asList(2));
        progressionUnitVerse.getProgressionUnitBars().get(2).setLowSynthId(Arrays.asList(3));

        progressionUnitVerse.getProgressionUnitBars().get(3).setjChord(new Chord("g#maj"));
        progressionUnitVerse.getProgressionUnitBars().get(3).setHiSynthId(Arrays.asList(11));
        progressionUnitVerse.getProgressionUnitBars().get(3).setMidSynthId(Arrays.asList(22));
        progressionUnitVerse.getProgressionUnitBars().get(3).setLowSynthId(Arrays.asList(33));

        //CHORUS
        progressionUnitChorus = new ProgressionUnit();
        progressionUnitChorus.initBars(FOUR.getValue());
        progressionUnitChorus.setType(ProgressionUnit.ProgressionType.CHORUS);

        progressionUnitChorus.getProgressionUnitBars().get(0).setjChord(new Chord("amaj"));
        progressionUnitChorus.getProgressionUnitBars().get(0).setHiSynthId(Arrays.asList(111));
        progressionUnitChorus.getProgressionUnitBars().get(0).setMidSynthId(Arrays.asList(112));
        progressionUnitChorus.getProgressionUnitBars().get(0).setLowSynthId(Arrays.asList(113));

        progressionUnitChorus.getProgressionUnitBars().get(1).setjChord(new Chord("c#min"));
        progressionUnitChorus.getProgressionUnitBars().get(1).setHiSynthId(Arrays.asList(111));
        progressionUnitChorus.getProgressionUnitBars().get(1).setMidSynthId(Arrays.asList(122));
        progressionUnitChorus.getProgressionUnitBars().get(1).setLowSynthId(Arrays.asList(133));

        progressionUnitChorus.getProgressionUnitBars().get(2).setjChord(new Chord("bmaj"));
        progressionUnitChorus.getProgressionUnitBars().get(2).setHiSynthId(Arrays.asList(111));
        progressionUnitChorus.getProgressionUnitBars().get(2).setMidSynthId(Arrays.asList(112));
        progressionUnitChorus.getProgressionUnitBars().get(2).setLowSynthId(Arrays.asList(113));

        progressionUnitChorus.getProgressionUnitBars().get(3).setjChord(new Chord("d#maj"));
        progressionUnitChorus.getProgressionUnitBars().get(3).setHiSynthId(Arrays.asList(111));
        progressionUnitChorus.getProgressionUnitBars().get(3).setMidSynthId(Arrays.asList(122));
        progressionUnitChorus.getProgressionUnitBars().get(3).setLowSynthId(Arrays.asList(133));


        //COMP_OPTIONS:
        CompRhythm compRhythmVerse = new CompRhythm();
        compRhythmVerse.setWeight(WeightClass.GOOD);
        compRhythmVerse.setComp("i,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,i,d,x,x,s,x,x,s,x,x,x,x");
        compRhythmVerse.setId(999);

        CompRhythm compRhythmChorus = new CompRhythm();
        compRhythmChorus.setWeight(WeightClass.OK);
        compRhythmChorus.setComp("i,x,x,x,x,x,x,x,x,q.,d,d,d,d,d,x,x,x,x,x,x,x,q,d,d,d,x,s,x,x,x,x");
        compRhythmChorus.setId(777);

        selectedCompOptions = new HashMap<>();
        selectedCompOptions.put(ProgressionUnit.ProgressionType.CHORUS, compRhythmChorus);
        selectedCompOptions.put(ProgressionUnit.ProgressionType.VERSE, compRhythmVerse);
    }

    @Test
    public void shouldSetCorrectCompOptionForProgressionType(){

        ProgressionUnit pUnitVerse = synthPadGenerator.generateAndSetSynthPad(progressionUnitVerse, selectedCompOptions);
        ProgressionUnit pUnitChorus = synthPadGenerator.generateAndSetSynthPad(progressionUnitChorus, selectedCompOptions);

        assertThat(pUnitVerse.getProgressionUnitBars().get(0).getSelectedCompRhythm().getId()).isEqualTo(999);
        assertThat(pUnitVerse.getProgressionUnitBars().get(1).getSelectedCompRhythm()).isNull();

        assertThat(pUnitChorus.getProgressionUnitBars().get(2).getSelectedCompRhythm().getId()).isEqualTo(777);
        assertThat(pUnitChorus.getProgressionUnitBars().get(3).getSelectedCompRhythm()).isNull();
    }

    @Test
    public void shouldSetAlternatingWholeNotesForMidPattern(){

        synthPadGenerator.generateAndSetSynthPad(progressionUnitVerse, selectedCompOptions);

        Assert.assertNotNull(progressionUnitVerse.getProgressionUnitBars());
        Assert.assertTrue(progressionUnitVerse.getProgressionUnitBars().size() > 0);

        boolean foundMatch = false;
        for (String noteName : getNotes("cmaj")) {
            if(!foundMatch) {
                foundMatch = progressionUnitVerse.getProgressionUnitBars().get(0).getPatternMid().toString().contains(noteName);
            }
        }
        assertThat(foundMatch).isTrue();

        foundMatch = false;
        for (String noteName : getNotes("dmin")) {
            if(!foundMatch) {
                foundMatch = progressionUnitVerse.getProgressionUnitBars().get(0).getPatternMid().toString().contains(noteName);
            }
        }
        assertThat(foundMatch).isTrue();

        assertThat(progressionUnitVerse.getProgressionUnitBars().get(1).getPatternMid().toString()).isEmpty();

        foundMatch = false;
        for (String noteName : getNotes("f#maj")) {
            if(!foundMatch) {
                foundMatch = progressionUnitVerse.getProgressionUnitBars().get(2).getPatternMid().toString().contains(noteName);
            }
        }
        assertThat(foundMatch).isTrue();

        foundMatch = false;
        for (String noteName : getNotes("g#maj")) {
            if(!foundMatch) {
                foundMatch = progressionUnitVerse.getProgressionUnitBars().get(2).getPatternMid().toString().contains(noteName);
            }
        }
        assertThat(foundMatch).isTrue();

        assertThat(progressionUnitVerse.getProgressionUnitBars().get(3).getPatternMid().toString()).isEmpty();
    }

    private List<String> getNotes(String chordName){

        Note[] notes = new Chord(chordName).getNotes();
        List<String> stringNotes = Arrays.asList(notes).stream().map(n ->  n.toString()).collect(Collectors.toList());

        return stringNotes;
    }

    @Test
    public void shouldSetAlternatingWholeNotesForMidAltPattern(){

        synthPadGenerator.generateAndSetSynthPad(progressionUnitVerse, selectedCompOptions);

        Assert.assertNotNull(progressionUnitVerse.getProgressionUnitBars());
        Assert.assertTrue(progressionUnitVerse.getProgressionUnitBars().size() > 0);

        boolean foundMatch = false;
        for (String noteName : getNotes("cmaj")) {
            if(!foundMatch) {
                foundMatch = progressionUnitVerse.getProgressionUnitBars().get(0).getPatternMidAlt().toString().contains(noteName);
            }
        }
        assertThat(foundMatch).isTrue();

        foundMatch = false;
        for (String noteName : getNotes("dmin")) {
            if(!foundMatch) {
                foundMatch = progressionUnitVerse.getProgressionUnitBars().get(0).getPatternMidAlt().toString().contains(noteName);
            }
        }
        assertThat(foundMatch).isTrue();

        assertThat(progressionUnitVerse.getProgressionUnitBars().get(1).getPatternMidAlt().toString()).isEmpty();

        foundMatch = false;
        for (String noteName : getNotes("f#maj")) {
            if(!foundMatch) {
                foundMatch = progressionUnitVerse.getProgressionUnitBars().get(2).getPatternMidAlt().toString().contains(noteName);
            }
        }
        assertThat(foundMatch).isTrue();

        foundMatch = false;
        for (String noteName : getNotes("g#maj")) {
            if(!foundMatch) {
                foundMatch = progressionUnitVerse.getProgressionUnitBars().get(2).getPatternMidAlt().toString().contains(noteName);
            }
        }
        assertThat(foundMatch).isTrue();

        assertThat(progressionUnitVerse.getProgressionUnitBars().get(3).getPatternMidAlt().toString()).isEmpty();
    }
}
