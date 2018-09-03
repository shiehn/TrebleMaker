package com.treblemaker.selectors;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.HiveChord;
import com.treblemaker.selectors.helper.LoopSelectorHelper;
import com.treblemaker.selectors.interfaces.IHarmonicLoopSelector;
import com.treblemaker.utils.LoopUtils;
import com.treblemaker.weighters.enums.WeightClass;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class HarmonicLoopSelectorTest extends TestCase {
    @Autowired
    @Qualifier(value = "harmonicLoopSelector")
    private IHarmonicLoopSelector harmonicLoopSelector;

    private HarmonicLoop loopA;
    private HarmonicLoop loopB;
    private HarmonicLoop loopC;

    private List<HarmonicLoop> setUpTestLoops(float targetBarLength) {
        /* BEFORE

        loopA = new HarmonicLoop();
        loopA.setAudioLength(targetBarLength);

        List<HiveChord> chordListA = new ArrayList<>();
        chordListA.add(new HiveChord("Cmaj"));
        chordListA.add(new HiveChord("D#min"));
        chordListA.add(new HiveChord("Abmaj"));
        chordListA.add(new HiveChord("Abmaj"));
        loopA.setChords(chordListA);

        loopB = new HarmonicLoop();
        loopB.setAudioLength(targetBarLength);

        List<HiveChord> chordListB = new ArrayList<>();
        chordListB.add(new HiveChord("Cmaj"));
        chordListB.add(new HiveChord("Dmin"));
        chordListB.add(new HiveChord("Gmaj"));
        chordListB.add(new HiveChord("Abmaj"));
        loopB.setChords(chordListB);

        loopC = new HarmonicLoop();
        loopC.setAudioLength(targetBarLength + 0.3f);

        List<HiveChord> chordListC = new ArrayList<>();
        chordListC.add(new HiveChord("Cmaj"));
        chordListC.add(new HiveChord("Dmin"));
        chordListC.add(new HiveChord("Gmaj"));
        chordListC.add(new HiveChord("Abmaj"));
        loopC.setChords(chordListB);
        */

        /* AFTER */

        loopA = new HarmonicLoop();
        loopA.setAudioLength(targetBarLength);


        loopA.setChords(new ArrayList<>(Arrays.asList(new HiveChord("Cmaj"), new HiveChord("D#min"), new HiveChord("Abmaj"), new HiveChord("Abmaj"))));

        loopB = new HarmonicLoop();
        loopB.setAudioLength(targetBarLength);
        loopB.setChords(new ArrayList<>(Arrays.asList(new HiveChord("Cmaj"), new HiveChord("Dmin"), new HiveChord("Gmaj"), new HiveChord("Abmaj"))));

        loopC = new HarmonicLoop();
        loopC.setAudioLength(targetBarLength + 0.3f);
        loopC.setChords(new ArrayList<>(Arrays.asList(new HiveChord("Cmaj"), new HiveChord("Dmin"), new HiveChord("Gmaj"), new HiveChord("Abmaj"))));





        List<HarmonicLoop> testLoops = Arrays.asList(loopA, loopB);

        return testLoops;
    }

    private List<List<String>> setUpProgressionsA() {

        List<List<String>> progressions = new ArrayList<>();
        List<String> progressionA = Arrays.asList("Cmaj", "Dmin", "Gmaj", "Gmaj");
        List<String> progressionB = Arrays.asList("Cmaj", "Dmin", "Gmaj", "Dmin");

        progressions.add(progressionA);
        progressions.add(progressionB);

        return progressions;
    }

    @Test
    public void testSelectMiddleChord() throws Exception {

        int bpm = 80;
        int maxBeatsInLoop = 8;
        float barLengthInSec = LoopUtils.getBeatsInSeconds(bpm, maxBeatsInLoop);

        List<HarmonicLoop> testLoops = setUpTestLoops(barLengthInSec);

        HiveChord primaryCandidate = new HiveChord("d#min");
        List<HiveChord> secondaryCandidates = Arrays.asList(new HiveChord("abmaj"), new HiveChord("gmaj"), new HiveChord("dmin"), new HiveChord("bbdom7"), new HiveChord("abmin7"));

        List<HarmonicLoop> primaryLoops = harmonicLoopSelector.getPrimaryCandidatesForMiddleChord(primaryCandidate, testLoops, barLengthInSec);

        Assert.assertEquals(1, primaryLoops.size());

        List<HarmonicLoop> secondaryLoops = harmonicLoopSelector.getSecondaryCandidatesForMiddleChord(secondaryCandidates, testLoops, barLengthInSec);

        Assert.assertEquals(4, secondaryLoops.size());
    }

    @Test
    public void shouldSelectHighestRythmicWeight(){

        HiveChord chordA = new HiveChord("amaj7");

        HarmonicLoop loopA = new HarmonicLoop();
        loopA.setChords(new ArrayList<HiveChord>(){{
            add(new HiveChord("c#min7"));
            add(new HiveChord("amin7"));
        }});
        loopA.setRhythmicWeight(WeightClass.OK);
        loopA.setFileName("loopA");

        HarmonicLoop loopB = new HarmonicLoop();
        loopB.setChords(new ArrayList<HiveChord>(){{
            add(new HiveChord("d#min7"));
            add(new HiveChord("gmin7"));
        }});
        loopB.setFileName("loopB");

        HarmonicLoop loopC = new HarmonicLoop();
        loopC.setChords(new ArrayList<HiveChord>() {{
            add(new HiveChord("cmin7"));
            add(new HiveChord("amaj7"));
        }});
        loopC.setFileName("loopC");

        HarmonicLoop loopD = new HarmonicLoop();
        loopD.setChords(new ArrayList<HiveChord>(){{
            add(new HiveChord("f#min7"));
            add(new HiveChord("amaj7"));
        }});
        loopD.setRhythmicWeight(WeightClass.GOOD);
        loopD.setFileName("loopD");

        List<HarmonicLoop> harmonicLoops = new ArrayList<HarmonicLoop>(){{
            add(loopA);
            add(loopB);
            add(loopC);
            add(loopD);
        }};

        int iterations = 5000;

        int loopACount = 0;
        int loopBCount = 0;
        int loopCCount = 0;
        int loopDCount = 0;

        for (Integer i=0; i<iterations; i++){
            HarmonicLoop selectedLoop = LoopSelectorHelper.makeWeightedSelectionFromHarmonicLoops(harmonicLoops, null, null);

            if(selectedLoop.getFileName().equalsIgnoreCase("loopA")){
                loopACount++;
            }else if(selectedLoop.getFileName().equalsIgnoreCase("loopB")){
                loopBCount++;
            }else if(selectedLoop.getFileName().equalsIgnoreCase("loopC")){
                loopCCount++;
            }else if(selectedLoop.getFileName().equalsIgnoreCase("loopD")){
                loopDCount++;
            }
        }

        System.out.println("loopACount : " + loopACount);
        System.out.println("loopBCount : " + loopBCount);
        System.out.println("loopCCount : " + loopCCount);
        System.out.println("loopDCount : " + loopDCount);

        //LOOPD SHOULD BE SELECTED MOST OFTEN ..

        boolean result = (loopDCount > loopACount) && (loopDCount > loopBCount) && (loopDCount > loopCCount);

        Assert.assertTrue(result);
    }

    @Test
    public void shouldSelectHighestEqWeight(){
        HarmonicLoop loopA = new HarmonicLoop();
        loopA.setChords(new ArrayList<>(Arrays.asList(new HiveChord("c#min7"),new HiveChord("amin7"))));
        loopA.setRhythmicWeight(WeightClass.OK);
        loopA.setEqWeight(WeightClass.OK);
        loopA.setFileName("loopA");

        HarmonicLoop loopB = new HarmonicLoop();
        loopB.setChords(new ArrayList<>(Arrays.asList(new HiveChord("d#min7"),new HiveChord("gmin7"))));
        loopB.setFileName("loopB");

        HarmonicLoop loopC = new HarmonicLoop();
        loopC.setChords(new ArrayList<>(Arrays.asList(new HiveChord("cmin7"), new HiveChord("amaj7"))));
        loopA.setRhythmicWeight(WeightClass.BAD);
        loopA.setEqWeight(WeightClass.GOOD);
        loopC.setFileName("loopC");

        HarmonicLoop loopD = new HarmonicLoop();
        loopD.setChords(new ArrayList<>(Arrays.asList(new HiveChord("f#min7"), new HiveChord("amaj7"))));
        loopD.setEqWeight(WeightClass.GOOD);
        loopD.setFileName("loopD");

        List<HarmonicLoop> harmonicLoops = new ArrayList<>(Arrays.asList(loopA, loopB, loopC, loopD));

        int iterations = 5000;
        int loopACount = 0;
        int loopBCount = 0;
        int loopCCount = 0;
        int loopDCount = 0;

        for (Integer i=0; i<iterations; i++){
            HarmonicLoop selectedLoop = LoopSelectorHelper.makeWeightedSelectionFromHarmonicLoops(harmonicLoops, null, null);

            if(selectedLoop.getFileName().equalsIgnoreCase("loopA")){
                loopACount++;
            }else if(selectedLoop.getFileName().equalsIgnoreCase("loopB")){
                loopBCount++;
            }else if(selectedLoop.getFileName().equalsIgnoreCase("loopC")){
                loopCCount++;
            }else if(selectedLoop.getFileName().equalsIgnoreCase("loopD")){
                loopDCount++;
            }
        }

        System.out.println("loopACount : " + loopACount);
        System.out.println("loopBCount : " + loopBCount);
        System.out.println("loopCCount : " + loopCCount);
        System.out.println("loopDCount : " + loopDCount);

        //LOOPD SHOULD BE SELECTED MOST OFTEN ..

        boolean result = (loopDCount > loopACount) && (loopDCount > loopBCount) && (loopDCount > loopCCount);

        Assert.assertTrue(result);
    }

    @Test
    public void CheckForMatchingTriadBase(){

        boolean hasMatch = false;
        HiveChord harmonicLoop;
        HiveChord chord;

        harmonicLoop = new HiveChord("c#min7");
        chord = new HiveChord("dbmin");
        hasMatch = chord.isEqualOrTriadMatch(harmonicLoop);
        Assert.assertTrue(hasMatch);

        harmonicLoop = new HiveChord("dbmin");
        chord = new HiveChord("c#min7");
        hasMatch = chord.isEqualOrTriadMatch(harmonicLoop);
        Assert.assertTrue(hasMatch);

        harmonicLoop = new HiveChord("c#min6");
        chord = new HiveChord("dbmin");
        hasMatch = chord.isEqualOrTriadMatch(harmonicLoop);
        Assert.assertTrue(hasMatch);

        harmonicLoop = new HiveChord("dbmin");
        chord = new HiveChord("c#min6");
        hasMatch = chord.isEqualOrTriadMatch(harmonicLoop);
        Assert.assertTrue(hasMatch);

        harmonicLoop = new HiveChord("c#dom7");
        chord = new HiveChord("c#maj");
        hasMatch = chord.isEqualOrTriadMatch(harmonicLoop);
        Assert.assertTrue(hasMatch);

        harmonicLoop = new HiveChord("c#maj");
        chord = new HiveChord("c#dom7");
        hasMatch = chord.isEqualOrTriadMatch(harmonicLoop);
        Assert.assertTrue(hasMatch);

        harmonicLoop = new HiveChord("cmaj");
        chord = new HiveChord("c#dom7");
        hasMatch = chord.isEqualOrTriadMatch(harmonicLoop);
        Assert.assertFalse(hasMatch);

        harmonicLoop = new HiveChord("cmaj");
        chord = new HiveChord("cmaj");
        hasMatch = chord.isEqualOrTriadMatch(harmonicLoop);
        Assert.assertTrue(hasMatch);

        harmonicLoop = new HiveChord("cmaj");
        chord = new HiveChord("c#dom7");
        hasMatch = chord.isEqualOrTriadMatch(harmonicLoop);
        Assert.assertFalse(hasMatch);

        harmonicLoop = new HiveChord("c#min7b5");
        chord = new HiveChord("dbdim");
        hasMatch = chord.isEqualOrTriadMatch(harmonicLoop);
        Assert.assertTrue(hasMatch);

        harmonicLoop = new HiveChord("dbdim");
        chord = new HiveChord("c#min7b5");
        hasMatch = chord.isEqualOrTriadMatch(harmonicLoop);
        Assert.assertTrue(hasMatch);


        harmonicLoop = new HiveChord("c#maj7");
        chord = new HiveChord("dbmaj");
        hasMatch = chord.isEqualOrTriadMatch(harmonicLoop);
        Assert.assertTrue(hasMatch);

        harmonicLoop = new HiveChord("dbmaj");
        chord = new HiveChord("c#maj7");
        hasMatch = chord.isEqualOrTriadMatch(harmonicLoop);
        Assert.assertTrue(hasMatch);

        harmonicLoop = new HiveChord("c#maj6");
        chord = new HiveChord("dbmaj");
        hasMatch = chord.isEqualOrTriadMatch(harmonicLoop);
        Assert.assertTrue(hasMatch);

        harmonicLoop = new HiveChord("dbmaj");
        chord = new HiveChord("c#maj6");
        hasMatch = chord.isEqualOrTriadMatch(harmonicLoop);
        Assert.assertTrue(hasMatch);
    }

    @Test
    public void shouldInitCorrectChord(){

        HiveChord dmaj = new HiveChord("dmaj");
        String chordName = dmaj.getChordName();
        String chordNameRaw = dmaj.getRawChordName();
        assertThat(chordName).isEqualTo("dmaj");
        assertThat(chordNameRaw).isEqualTo("dmaj");

        HiveChord dmaj7 = new HiveChord("dmaj7");
        String chordName7 = dmaj7.getChordName();
        String chordNameRaw7 = dmaj7.getRawChordName();
        assertThat(chordName7).isEqualTo("dmaj7");
        assertThat(chordNameRaw7).isEqualTo("dmaj7");
    }

    @Test
    public void triadDominantMatchTest(){
        HiveChord ddom7 = new HiveChord("ddom7");
        HiveChord dmaj = new HiveChord("dmaj");

        assertThat(ddom7.isEqualOrTriadMatch(dmaj)).isTrue();
        assertThat(dmaj.isEqualOrTriadMatch(ddom7)).isTrue();
    }

    @Test
    public void triadMajorMatchTest(){
        HiveChord dmaj7 = new HiveChord("dmaj7");
        HiveChord dmaj = new HiveChord("dmaj");

        assertThat(dmaj7.isEqualOrTriadMatch(dmaj)).isTrue();
        assertThat(dmaj.isEqualOrTriadMatch(dmaj7)).isTrue();
    }

    @Test
    public void triadDiminishedMatchTest(){
        HiveChord gdim = new HiveChord("gdim");
        HiveChord gdim2 = new HiveChord("gdim");

        assertThat(gdim.isEqualOrTriadMatch(gdim2)).isTrue();
        assertThat(gdim2.isEqualOrTriadMatch(gdim)).isTrue();
    }

    @Test
    public void ShouldNormalizeLoopsByWeight(){

        List<HarmonicLoop> initialCollection = new ArrayList<>();

        HarmonicLoop harmonicLoopA = new HarmonicLoop();
        harmonicLoopA.setRhythmicWeight(WeightClass.GOOD);
        harmonicLoopA.setTimeseriesWeight(WeightClass.GOOD);
        harmonicLoopA.setHarmonicWeight(WeightClass.OK);

        HarmonicLoop harmonicLoopB = new HarmonicLoop();
        harmonicLoopB.setRhythmicWeight(WeightClass.BAD);
        harmonicLoopB.setTimeseriesWeight(WeightClass.OK);
        harmonicLoopB.setHarmonicWeight(WeightClass.OK);

        initialCollection.add(harmonicLoopA);
        initialCollection.add(harmonicLoopB);

        List<HarmonicLoop> normalizedCollection = LoopSelectorHelper.normalizeHarmonicLoopsByWeight(initialCollection);

        Assert.assertEquals(4,normalizedCollection.size());
    }
}