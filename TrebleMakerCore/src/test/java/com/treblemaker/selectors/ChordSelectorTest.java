package com.treblemaker.selectors;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnit.ProgressionType;
import com.treblemaker.selectors.interfaces.IChordSelector;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class ChordSelectorTest extends TestCase {

    @Autowired
    @Qualifier(value = "chordSelector")
    private IChordSelector chordSelector;

    private List<ProgressionType> setUpProgressionTypes() {
        List<ProgressionType> progressionTypes = new ArrayList<>();

        progressionTypes.add(ProgressionType.VERSE);
        progressionTypes.add(ProgressionType.BRIDGE);
        progressionTypes.add(ProgressionType.CHORUS);

        return progressionTypes;
    }

    private List<HiveChord> setUpChordsFromDatabase() {
        List<HiveChord> chords = new ArrayList<>();
        chords.add(new HiveChord("eMIN7"));
        chords.add(new HiveChord("aMIN7"));
        chords.add(new HiveChord("bbDOM7"));
        chords.add(new HiveChord("f#DOM7"));
        chords.add(new HiveChord("cMAJ7"));
        chords.add(new HiveChord("gMAJ"));

        return chords;
    }

    @Test
    public void testChordEquality() {
        HiveChord chordA = null;
        HiveChord chordB = null;

        chordA = new HiveChord("eMIN7");
        chordB = new HiveChord("eMIN7");
        assertThat(chordA.getChordName()).isEqualTo(chordB.getChordName());

        chordA = new HiveChord("eMIN7");
        chordB = new HiveChord("EMin7");
        assertThat(chordA.getChordName()).isEqualTo(chordB.getChordName());

        chordA = new HiveChord("eMIN");
        chordB = new HiveChord("EMin7");
        assertThat(chordA.getChordName()).isNotEqualTo(chordB.getChordName());

        chordA = new HiveChord("d#Min7");
        chordB = new HiveChord("EbMIN7");
        assertThat(chordA.getChordName()).isEqualTo(chordB.getChordName());

        chordA = new HiveChord("abDom7");
        chordB = new HiveChord("g#Dom7");
        assertThat(chordA.getChordName()).isEqualTo(chordB.getChordName());

        chordA = new HiveChord("CBDom7");
        chordB = new HiveChord("BDom7");
        assertThat(chordA.getChordName()).isEqualTo(chordB.getChordName());

        chordA = new HiveChord("Gbdom7");
        chordB = new HiveChord("f#DOM7");
        assertThat(chordA.getChordName()).isEqualTo(chordB.getChordName());

        chordA = new HiveChord("bbdom7");
        chordB = new HiveChord("a#DOM7");
        assertThat(chordA.getChordName()).isEqualTo(chordB.getChordName());
    }

    @Test
    public void testCompatibleWithEachChordPair() throws Exception {
        List<ProgressionType> progressionTypes = setUpProgressionTypes();
        List<HiveChord> hiveChordInDatabase = setUpChordsFromDatabase();

        HashMap<ProgressionType, HiveChord> typeAndFirstChord = chordSelector.selectFirstChordForType(progressionTypes, hiveChordInDatabase);

        int total = 0;
        for (HiveChord chord : typeAndFirstChord.values()) {
            total++;
        }

        Assert.assertEquals(3, total);

        Assert.assertTrue(!typeAndFirstChord.get(ProgressionType.VERSE).isEqual(""));
        Assert.assertTrue(typeAndFirstChord.get(ProgressionType.VERSE) != null);
        Assert.assertTrue(typeAndFirstChord.get(ProgressionType.BRIDGE) != null);
        Assert.assertTrue(typeAndFirstChord.get(ProgressionType.CHORUS) != null);
    }


    @Test
    public void selectMiddleChord() throws Exception {
        HarmonicLoop loop = new HarmonicLoop();

        List<HiveChord> hiveChordInDatabase = new ArrayList<>();

        List<HiveChord> hiveChordList = new ArrayList<>();
        hiveChordList.add(new HiveChord("dDOM7"));
        hiveChordList.add(new HiveChord("gMAJ7"));

        loop.setChords(hiveChordList);

        //AT THE POINT YOU SELECT
        //C1_L3

        HiveChord selectedChord = chordSelector.selectMiddleChord(loop, new HiveChord("g#MAJ7"), new HiveChord("dDIM"), hiveChordInDatabase);

        Assert.assertTrue(selectedChord.isEqual(loop.getRootOne()) || selectedChord.isEqual(loop.getRootTwo()));
    }

    @Test
    public void getsSingleChordTwoBarsPrior() {
        List<HiveChord> hiveChordInDatabase = Arrays.asList(new HiveChord("cMAJ7"), new HiveChord("a#MIN7"), new HiveChord("dDom7"), new HiveChord("d#Maj7"),
                new HiveChord("a#Min7"), new HiveChord("a#Min7"), new HiveChord("dDom7"), new HiveChord("d#Maj7"), new HiveChord("a#Min7"),
                new HiveChord("_"), new HiveChord("fDom7"), new HiveChord("cMaj7"), new HiveChord("c#_sus4"), new HiveChord("_"), new HiveChord("cMaj7"),
                new HiveChord("d#Dom7"), new HiveChord("dMin7b5"), new HiveChord("cMaj7"));

        HiveChord selectedChordA = chordSelector.getChordTwoBarsPrior(new HiveChord("a#Min7"), hiveChordInDatabase);
        boolean hasMatch = (selectedChordA.getChordName().equals("ddom7") || selectedChordA.getChordName().equals("d#maj7"));
        assertThat(hasMatch).isTrue();

        HiveChord selectedChordB = chordSelector.getChordTwoBarsPrior(new HiveChord("cMaj7"), hiveChordInDatabase);
        assertThat(selectedChordB.getChordName()).isEqualTo("d#dom7");
    }

    @Test
    public void getsListOfChordsTwoBarsPrior() {
        List<HiveChord> hiveChordInDatabase = Arrays.asList(new HiveChord("dDom7"), new HiveChord("cMAJ7"), new HiveChord("a#Min7"), new HiveChord("dDom7"), new HiveChord("d#Maj7")
                , new HiveChord("f#Min7"), new HiveChord("_"), new HiveChord("dDom7")
                , new HiveChord("_"), new HiveChord("a#Min7"), new HiveChord("dDom7")
                , new HiveChord("a#Min7"), new HiveChord("a#Min7"), new HiveChord("dDom7"), new HiveChord("d#Maj7"), new HiveChord("a#Min7"));

        List<HiveChord> selectedChord = chordSelector.getChordCandidatesTwoBarsPrior(new HiveChord("dDom7"), hiveChordInDatabase);

        assertThat(2).isEqualTo(selectedChord.size());
        assertThat(selectedChord.get(0).isEqual("cMAJ7")).isTrue();
        assertThat(selectedChord.get(1).isEqual("a#Min7")).isTrue();
    }

    @Test
    public void selectsCorrectChordBetweenTwoChords() {
        List<HiveChord> hiveChordInDatabase = Arrays.asList(new HiveChord("cMAJ7"), new HiveChord("a#Min7"), new HiveChord("dDom7"), new HiveChord("d#Maj7"),
                new HiveChord("a#Min7"), new HiveChord("a#Min7"), new HiveChord("_"), new HiveChord("d#Maj7"), new HiveChord("a#Min7"),
                new HiveChord("a#Min7"), new HiveChord("_"), new HiveChord("_"), new HiveChord("d#Maj7"), new HiveChord("a#Min7"),
                new HiveChord("a#Min7"), new HiveChord("a#Min7"), new HiveChord("dDom7"), new HiveChord("d#Maj7"), new HiveChord("a#Min7"));

        HiveChord selectedChord1 = chordSelector.selectChordBetweenChords(new HiveChord("a#Min7"), new HiveChord("d#Maj7"), hiveChordInDatabase);
        HiveChord selectedChord2 = chordSelector.selectChordBetweenChords(new HiveChord("a#Min7"), new HiveChord("d#Maj7"), hiveChordInDatabase);
        HiveChord selectedChord3 = chordSelector.selectChordBetweenChords(new HiveChord("a#Min7"), new HiveChord("d#Maj7"), hiveChordInDatabase);
        HiveChord selectedChord4 = chordSelector.selectChordBetweenChords(new HiveChord("a#Min7"), new HiveChord("d#Maj7"), hiveChordInDatabase);

        assertThat(selectedChord1.getChordName()).isEqualTo("ddom7");
        assertThat(selectedChord2.getChordName()).isEqualTo("ddom7");
        assertThat(selectedChord3.getChordName()).isEqualTo("ddom7");
        assertThat(selectedChord4.getChordName()).isEqualTo("ddom7");
    }

    @Test
    public void selectsCorrectFourthChord() throws Exception {

        List<HiveChord> hiveChordInDatabase = Arrays.asList(new HiveChord("cMAJ7"), new HiveChord("bbMin7"), new HiveChord("dDom7"), new HiveChord("ebMaj7"), new HiveChord("a#Min7"),
                new HiveChord("bbMin7"), new HiveChord("dDom7"), new HiveChord("ebMaj7"), new HiveChord("bbMin7"));

        HiveChord chordOne = new HiveChord("d#Dom7");

        HiveChord chordTwo = new HiveChord("bbMin7");

        List<HiveChord> chordList = new ArrayList<>();
        chordList.add(chordOne);
        chordList.add(chordTwo);

        HarmonicLoop selectedLoop = new HarmonicLoop();
        selectedLoop.setChords(chordList);

//        selectedLoop.setRootOne("d#dom7");
//        selectedLoop.setRootTwo("bbmin7");

        HiveChord selectedChord = chordSelector.selectFourthChord(selectedLoop, new HiveChord("a#Min7"), new HiveChord("dDom7"), hiveChordInDatabase);

        Assert.assertTrue(selectedChord.isEqual("bbMin7"));
    }


    @Test
    public void shouldCreateHashMapWithCminAndEmaj(){

        HiveChord eMinChord = new HiveChord("emin");
        HiveChord cMajChord = new HiveChord("cmaj");
        HiveChord fmin7Chord = new HiveChord("fmin7");

        HarmonicLoop loopEmin = new HarmonicLoop();
        loopEmin.setBarCount(4);
        loopEmin.setChords(Arrays.asList(eMinChord));

        HarmonicLoop loopCmaj7 = new HarmonicLoop();
        loopCmaj7.setBarCount(4);
        loopCmaj7.setChords(Arrays.asList(cMajChord));

        HarmonicLoop loopFmin7Cmaj = new HarmonicLoop();
        loopFmin7Cmaj.setBarCount(4);
        loopFmin7Cmaj.setChords(Arrays.asList(cMajChord,fmin7Chord));

        ProgressionUnit progressionUnitOne = new ProgressionUnit();
        progressionUnitOne.setType(ProgressionType.VERSE);
        progressionUnitOne.initBars(4);
        progressionUnitOne.getProgressionUnitBars().get(0).setChord(eMinChord);
        progressionUnitOne.getProgressionUnitBars().get(0).setHarmonicLoop(loopEmin);

        ProgressionUnit progressionUnitTwo = new ProgressionUnit();
        progressionUnitTwo.setType(ProgressionType.BRIDGE);
        progressionUnitTwo.initBars(4);
        progressionUnitTwo.getProgressionUnitBars().get(0).setChord(cMajChord);
        progressionUnitTwo.getProgressionUnitBars().get(0).setHarmonicLoop(loopCmaj7);

        ProgressionUnit progressionUnitThree = new ProgressionUnit();
        progressionUnitThree.setType(ProgressionType.CHORUS);
        progressionUnitThree.initBars(4);
        progressionUnitThree.getProgressionUnitBars().get(0).setChord(fmin7Chord);
        progressionUnitThree.getProgressionUnitBars().get(0).setHarmonicLoop(loopFmin7Cmaj);

        HashMap<ProgressionType, List<HiveChord>> typeToProgressionMap = chordSelector.selectChordsFrom4BarLoop(Arrays.asList(progressionUnitOne, progressionUnitTwo, progressionUnitThree));

        assertThat(typeToProgressionMap.size()).isEqualTo(3);

        assertThat(typeToProgressionMap.get(ProgressionType.VERSE).size()).isEqualTo(4);
        assertThat(typeToProgressionMap.get(ProgressionType.VERSE).get(0).getChordName()).isEqualToIgnoringCase("emin");
        assertThat(typeToProgressionMap.get(ProgressionType.VERSE).get(1).getChordName()).isEqualToIgnoringCase("emin");
        assertThat(typeToProgressionMap.get(ProgressionType.VERSE).get(2).getChordName()).isEqualToIgnoringCase("emin");
        assertThat(typeToProgressionMap.get(ProgressionType.VERSE).get(3).getChordName()).isEqualToIgnoringCase("emin");

        assertThat(typeToProgressionMap.get(ProgressionType.BRIDGE).size()).isEqualTo(4);
        assertThat(typeToProgressionMap.get(ProgressionType.BRIDGE).get(0).getChordName()).isEqualToIgnoringCase("cmaj");
        assertThat(typeToProgressionMap.get(ProgressionType.BRIDGE).get(1).getChordName()).isEqualToIgnoringCase("cmaj");
        assertThat(typeToProgressionMap.get(ProgressionType.BRIDGE).get(2).getChordName()).isEqualToIgnoringCase("cmaj");
        assertThat(typeToProgressionMap.get(ProgressionType.BRIDGE).get(3).getChordName()).isEqualToIgnoringCase("cmaj");

        assertThat(typeToProgressionMap.get(ProgressionType.CHORUS).size()).isEqualTo(4);
        assertThat(typeToProgressionMap.get(ProgressionType.CHORUS).get(0).getChordName()).isEqualToIgnoringCase("fmin7");
        assertThat(typeToProgressionMap.get(ProgressionType.CHORUS).get(1).getChordName()).isEqualToIgnoringCase("fmin7");
        assertThat(typeToProgressionMap.get(ProgressionType.CHORUS).get(2).getChordName()).isEqualToIgnoringCase("fmin7");
        assertThat(typeToProgressionMap.get(ProgressionType.CHORUS).get(3).getChordName()).isEqualToIgnoringCase("fmin7");

    }
}