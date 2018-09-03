package com.treblemaker.generators;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.TestBase;
import com.treblemaker.generators.harmonic.HarmonicLoopGenerator;
import com.treblemaker.generators.interfaces.IChordStructureGenerator;
import com.treblemaker.generators.interfaces.IHarmonicLoopGenerator;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.ProcessingState;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.types.Composition;
import com.treblemaker.model.types.ProcessingPattern;
import com.treblemaker.options.OptionsFilter;
import com.treblemaker.options.interfaces.IHarmonicLoopOptions;
import com.treblemaker.selectors.interfaces.IChordSelector;
import com.treblemaker.selectors.interfaces.IHarmonicLoopSelector;
import com.treblemaker.weighters.harmonicloopweighters.HarmonicLoopWeighter;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.treblemaker.constants.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.treblemaker.model.progressions.ProgressionUnit.BarCount.FOUR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class ChordStructureGeneratorTest extends TestBase {

    @Autowired
    IChordStructureGenerator chordStructureGenerator;

    @Autowired
    IHarmonicLoopSelector harmonicLoopSelector;

    @Autowired
    @Qualifier(value = "chordSelector")
    IChordSelector chordSelector;

    @Autowired
    IHarmonicLoopOptions harmonicLoopOptions;

    @Autowired
    OptionsFilter optionsFilter;

    IHarmonicLoopGenerator harmonicLoopGenerator;

    private QueueItem mQueueItem;

    @Before
    public void setup(){

        HarmonicLoopWeighter mockHarmonicLoopWeighter = mock(HarmonicLoopWeighter.class);
        when(mockHarmonicLoopWeighter.setHarmonicLoopWeight(any(), any(), any(), any())).thenReturn(true);

        harmonicLoopGenerator = new HarmonicLoopGenerator(harmonicLoopSelector, chordSelector, mockHarmonicLoopWeighter, harmonicLoopOptions, optionsFilter);
    }

    private void setupTest() {

        List<ProgressionUnit.ProgressionType> progressionTypes = new ArrayList<>();
        progressionTypes.add(ProgressionUnit.ProgressionType.VERSE);
        progressionTypes.add(ProgressionUnit.ProgressionType.CHORUS);
        progressionTypes.add(ProgressionUnit.ProgressionType.BRIDGE);

        mQueueItem = getQueueItem(progressionTypes);
    }

    @Test
    public void ShouldGetProgressionTypes() {

        setupTest();

        List<ProgressionUnit.ProgressionType> progressionTypes = chordStructureGenerator.getProgressionTypes(mQueueItem);

        List<ProgressionUnit.ProgressionType> expected = new ArrayList<ProgressionUnit.ProgressionType>();
        expected.add(ProgressionUnit.ProgressionType.VERSE);
        expected.add(ProgressionUnit.ProgressionType.CHORUS);
        expected.add(ProgressionUnit.ProgressionType.BRIDGE);

        assertEquals(expected, progressionTypes);
    }

    @Test
    public void ShouldSetMiddleChords() {

        List<ProgressionUnit> progressionUnits = new ArrayList<>();

        ProgressionUnit pUnit = new ProgressionUnit();
        pUnit.initBars(FOUR.getValue());

        HarmonicLoop loop = new HarmonicLoop();
        loop.setChords(new ArrayList<HiveChord>() {{
            add(new HiveChord("e_dom7"));
            add(new HiveChord("e_min7"));
            add(new HiveChord("d_dom7"));
            // add(new HiveChord("g_maj7"));
        }});

        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).setHarmonicLoop(loop);

        //SET CHORDS ..
        //pUnit.getProgressionUnitBars().get(ChordPositions.CHORD_ONE).setChord(new HiveChord("f_maj7"));

        progressionUnits.add(pUnit);

        //fill shit out here!!!!

        List<HiveChord> hiveChordInDatabase = new ArrayList<HiveChord>();
        hiveChordInDatabase.add(new HiveChord("c_maj7"));
        hiveChordInDatabase.add(new HiveChord("d_min7"));
        hiveChordInDatabase.add(new HiveChord("f_maj7"));
        hiveChordInDatabase.add(new HiveChord("d_dom7"));
        hiveChordInDatabase.add(new HiveChord("g_maj7"));
        hiveChordInDatabase.add(new HiveChord("g#_maj"));

        chordStructureGenerator.setChordThree(progressionUnits, hiveChordInDatabase);

        System.out.println("NAME : " + progressionUnits.get(0).getProgressionUnitBars().get(2).getChord().getChordName());

        HiveChord selectedChord = progressionUnits.get(0).getProgressionUnitBars().get(2).getChord();

        assertTrue(selectedChord.isEqual("d_dom7") || selectedChord.isEqual("e_dom7") || selectedChord.isEqual("e_min7"));

        //TEST2
        //TEST2
        //TEST2
        //TEST2
        //TEST2

        progressionUnits = new ArrayList<>();
        pUnit = new ProgressionUnit();
        pUnit.initBars(FOUR.getValue());
        loop = new HarmonicLoop();
        loop.setChords(new ArrayList<HiveChord>() {{
            add(new HiveChord("e_dom7"));
            add(new HiveChord("e_min7"));
            add(new HiveChord("d_dom7"));
            add(new HiveChord("g_maj7"));
        }});

        pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).setHarmonicLoop(loop);

        //SET CHORDS ..
        pUnit.getProgressionUnitBars().get(ChordPositions.CHORD_ONE).setChord(new HiveChord("d_dom7"));

        progressionUnits.add(pUnit);

        //fill shit out here!!!!

        hiveChordInDatabase = new ArrayList<HiveChord>();
        hiveChordInDatabase.add(new HiveChord("c_maj7"));
        hiveChordInDatabase.add(new HiveChord("d_min7"));
        hiveChordInDatabase.add(new HiveChord("f_maj7"));
        hiveChordInDatabase.add(new HiveChord("d_dom7"));
        hiveChordInDatabase.add(new HiveChord("g_maj7"));
        hiveChordInDatabase.add(new HiveChord("g#_maj"));

        chordStructureGenerator.setChordThree(progressionUnits, hiveChordInDatabase);

        System.out.println("NAME : " + progressionUnits.get(0).getProgressionUnitBars().get(2).getChord().getChordName());

        selectedChord = progressionUnits.get(0).getProgressionUnitBars().get(2).getChord();

        assertTrue(selectedChord.isEqual("g_maj7"));
    }

    @Test
    public void ShouldSetLastChord() {

        List<ProgressionUnit> progressionUnits = new ArrayList<ProgressionUnit>();
        ProgressionUnit a = new ProgressionUnit();
        a.initBars(FOUR.getValue());
        ProgressionUnit b = new ProgressionUnit();
        b.initBars(FOUR.getValue());
        progressionUnits.add(a);
        progressionUnits.add(b);

        progressionUnits.forEach(pUnit -> {
            pUnit.getProgressionUnitBars().get(ChordPositions.CHORD_ONE).setChord(new HiveChord("f_maj"));
            pUnit.getProgressionUnitBars().get(ChordPositions.CHORD_TWO).setChord(new HiveChord(""));
            pUnit.getProgressionUnitBars().get(ChordPositions.CHORD_THREE).setChord(new HiveChord("g_maj"));
            pUnit.getProgressionUnitBars().get(ChordPositions.CHORD_FOUR).setChord(new HiveChord(""));
        });

        List<HiveChord> hiveChordInDatabase = new ArrayList<HiveChord>();
        hiveChordInDatabase.add(new HiveChord("c_maj7"));
        hiveChordInDatabase.add(new HiveChord("d_min7"));
        hiveChordInDatabase.add(new HiveChord("f_maj7"));
        hiveChordInDatabase.add(new HiveChord("d_dom7"));
        hiveChordInDatabase.add(new HiveChord("g_maj7"));
        hiveChordInDatabase.add(new HiveChord("ab_maj"));

        chordStructureGenerator.setChordFour(hiveChordInDatabase, progressionUnits);

        assertFalse(progressionUnits.get(0).getProgressionUnitBars().get(ChordPositions.CHORD_FOUR).getChord().isEqual("ab_maj"));
    }

    @Test
    public void ShouldSetFirstChordInChangePair() {

        List<ProgressionUnit> progressionUnits = new ArrayList<>();
        ProgressionUnit verse = new ProgressionUnit();
        verse.initBars(FOUR.getValue());
        verse.setType(ProgressionUnit.ProgressionType.VERSE);

        ProgressionUnit bridge = new ProgressionUnit();
        bridge.initBars(FOUR.getValue());
        bridge.setType(ProgressionUnit.ProgressionType.BRIDGE);

        ProgressionUnit chorus = new ProgressionUnit();
        chorus.initBars(FOUR.getValue());
        chorus.setType(ProgressionUnit.ProgressionType.CHORUS);

        progressionUnits.add(verse);
        progressionUnits.add(chorus);
        progressionUnits.add(verse);
        progressionUnits.add(bridge);

        HashMap<ProgressionUnit.ProgressionType, HiveChord> typeAndFirstChord = new HashMap<ProgressionUnit.ProgressionType, HiveChord>();
        typeAndFirstChord.put(ProgressionUnit.ProgressionType.BRIDGE, new HiveChord("c#_dom7"));
        typeAndFirstChord.put(ProgressionUnit.ProgressionType.VERSE, new HiveChord("a#_dom7"));
        typeAndFirstChord.put(ProgressionUnit.ProgressionType.CHORUS, new HiveChord("f#_maj7"));

        chordStructureGenerator.setFirstChordInProgressions(progressionUnits, typeAndFirstChord);

        assertTrue(progressionUnits.get(0).getProgressionUnitBars().get(0).getChord().isEqual("a#_dom7"));
        assertTrue(progressionUnits.get(1).getProgressionUnitBars().get(0).getChord().isEqual("f#_maj7"));
        assertTrue(progressionUnits.get(2).getProgressionUnitBars().get(0).getChord().isEqual("a#_dom7"));
        assertTrue(progressionUnits.get(3).getProgressionUnitBars().get(0).getChord().isEqual("c#_dom7"));
    }

    @Test
    public void ShouldSetPhase_C1_L3_C3_L1_C2_L4_C4_L2() {

        QueueState queueState = new QueueState();
        queueState.setProcessingState(new ProcessingState(ProcessingState.ProcessingStates.C1_L3));
        queueState.getProcessingState().setProcessingPattern(ProcessingPattern.TWO_BAR);
        queueState.setDataSource(createSourceData());

        queueState.setQueueItem(getQueueItem(new ArrayList<ProgressionUnit.ProgressionType>() {{
            add(ProgressionUnit.ProgressionType.VERSE);
            add(ProgressionUnit.ProgressionType.CHORUS);
            add(ProgressionUnit.ProgressionType.BRIDGE);
        }}));

        //TEST SHOULD SET C1
        //TEST SHOULD SET C1
        //TEST SHOULD SET C1
        queueState = chordStructureGenerator.set(queueState);

        queueState.getStructure().forEach(pUnit -> {
            HiveChord chordOne = pUnit.getProgressionUnitBars().get(0).getChord();

            Boolean chordIsMatch = chordOne.isEqual("amin7");

            assertThat(chordIsMatch).isTrue();
        });

        //TEST SHOULD SET L3
        //TEST SHOULD SET L3
        //TEST SHOULD SET L3
        queueState = harmonicLoopGenerator.setHarmonicLoops(queueState, Composition.Layer.HARMONIC_LOOP);
        queueState.getProcessingState().setProcessingPattern(ProcessingPattern.TWO_BAR);

        queueState.getStructure().forEach(pUnit -> {
            HarmonicLoop currentLoop = pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).getHarmonicLoop();

            boolean isEqual = "loopOneBar".equalsIgnoreCase(currentLoop.getFileName()) || "loopTwoBar".equalsIgnoreCase(currentLoop.getFileName());
            assertThat(isEqual).isTrue();
        });

        //TEST SHOULD SET C3
        //TEST SHOULD SET C3
        //TEST SHOULD SET C3
        queueState.setProcessingState(new ProcessingState(ProcessingState.ProcessingStates.C3_L1));
        queueState.getProcessingState().setProcessingPattern(ProcessingPattern.TWO_BAR);

        queueState = chordStructureGenerator.set(queueState);

        queueState.getStructure().forEach(pUnit -> {

            HiveChord chord = pUnit.getProgressionUnitBars().get(ChordPositions.CHORD_THREE).getChord();

            Boolean chordIsMatch = chord.isEqual("amin7") || chord.isEqual("amin");

            assertThat(chordIsMatch).isTrue();
        });

        //TEST SHOULD SET L1
        //TEST SHOULD SET L1
        //TEST SHOULD SET L1
        queueState.setProcessingState(new ProcessingState(ProcessingState.ProcessingStates.C3_L1));
        queueState.getProcessingState().setProcessingPattern(ProcessingPattern.TWO_BAR);

        queueState = harmonicLoopGenerator.setHarmonicLoops(queueState, Composition.Layer.HARMONIC_LOOP);

        String bb = "";

        queueState.getStructure().forEach(pUnit -> {
            HarmonicLoop currentLoop = pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE).getHarmonicLoop();

            boolean isEqual = false;

            if(!isEqual){
                isEqual = "loopOneBar".equalsIgnoreCase(currentLoop.getFileName()) && currentLoop.getCurrentBar() == 1;
            }

            if(!isEqual){
                isEqual = "loopTwoBar".equalsIgnoreCase(currentLoop.getFileName()) && currentLoop.getCurrentBar() == 1;
            }

            if(!isEqual){
                isEqual = "loopFourBar".equalsIgnoreCase(currentLoop.getFileName()) && currentLoop.getCurrentBar() == 1;
            }

            assertThat(isEqual).isTrue();
        });

        //TEST SHOULD SET C2
        //TEST SHOULD SET C2
        //TEST SHOULD SET C2
        queueState.setProcessingState(new ProcessingState(ProcessingState.ProcessingStates.C2_L4));
        queueState.getProcessingState().setProcessingPattern(ProcessingPattern.TWO_BAR);

        queueState = chordStructureGenerator.set(queueState);

        queueState.getStructure().forEach(pUnit -> {

            HiveChord chord = pUnit.getProgressionUnitBars().get(ChordPositions.CHORD_TWO).getChord();

            Boolean chordIsMatch = chord.isEqual("amin7") || chord.isEqual("amin");

            assertThat(chordIsMatch).isTrue();
        });

        //TEST SHOULD SET L4
        //TEST SHOULD SET L4
        //TEST SHOULD SET L4
        queueState.setProcessingState(new ProcessingState(ProcessingState.ProcessingStates.C2_L4));
        queueState.getProcessingState().setProcessingPattern(ProcessingPattern.TWO_BAR);

        queueState = harmonicLoopGenerator.setHarmonicLoops(queueState, Composition.Layer.HARMONIC_LOOP);

        queueState.getStructure().forEach(pUnit -> {
            HarmonicLoop currentLoop = pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getHarmonicLoop();

            boolean isEqual = false;

            if(!isEqual){
                isEqual = "loopOneBar".equalsIgnoreCase(currentLoop.getFileName()) && currentLoop.getCurrentBar() == 4;
            }

            if(!isEqual){
                isEqual = "loopTwoBar".equalsIgnoreCase(currentLoop.getFileName()) && currentLoop.getCurrentBar() == 4;
            }

            if(!isEqual){
                isEqual = "loopFourBar".equalsIgnoreCase(currentLoop.getFileName()) && currentLoop.getCurrentBar() == 4;
            }

             assertThat(isEqual).isTrue();
        });

        //TEST SHOULD SET C4
        //TEST SHOULD SET C4
        //TEST SHOULD SET C4
        queueState.setProcessingState(new ProcessingState(ProcessingState.ProcessingStates.C4_L2));
        queueState.getProcessingState().setProcessingPattern(ProcessingPattern.TWO_BAR);

        queueState = chordStructureGenerator.set(queueState);

        queueState.getStructure().forEach(pUnit -> {
            HiveChord currentChord = pUnit.getProgressionUnitBars().get(ChordPositions.CHORD_FOUR).getChord();

            HiveChord expectedChord = new HiveChord("amin7");

            assertThat(currentChord.hasMatchingRoot(expectedChord)).isTrue();
        });

        //TEST SHOULD SET L2
        //TEST SHOULD SET L2
        //TEST SHOULD SET L2
        queueState.setProcessingState(new ProcessingState(ProcessingState.ProcessingStates.C4_L2));
        queueState.getProcessingState().setProcessingPattern(ProcessingPattern.TWO_BAR);

        queueState = harmonicLoopGenerator.setHarmonicLoops(queueState, Composition.Layer.HARMONIC_LOOP);

        queueState.getStructure().forEach(pUnit -> {
            HarmonicLoop currentLoop = pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).getHarmonicLoop();

            boolean isEqual = false;

            if(!isEqual){
                isEqual = "loopOneBar".equalsIgnoreCase(currentLoop.getFileName()) && currentLoop.getCurrentBar() == 2;
            }

            if(!isEqual){
                isEqual = "loopTwoBar".equalsIgnoreCase(currentLoop.getFileName()) && currentLoop.getCurrentBar() == 2;
            }

            if(!isEqual){
                isEqual = "loopFourBar".equalsIgnoreCase(currentLoop.getFileName()) && currentLoop.getCurrentBar() == 2;
            }

            if(!isEqual){
                String debugy = "";
            }

            assertThat(isEqual).isTrue();
        });
    }


    @Test
    public void ShouldSetPhaseWithVariedLoopLengths_C1_L3_C3_L1_C2_L4_C4_L2() {

        QueueState queueState = new QueueState();

        queueState.setProcessingState(new ProcessingState(ProcessingState.ProcessingStates.C1_L3));
        queueState.getProcessingState().setProcessingPattern(ProcessingPattern.TWO_BAR);
        queueState.setDataSource(createSourceData());

        queueState.setQueueItem(getQueueItem(new ArrayList<ProgressionUnit.ProgressionType>() {{
            add(ProgressionUnit.ProgressionType.VERSE);
            add(ProgressionUnit.ProgressionType.CHORUS);
            add(ProgressionUnit.ProgressionType.BRIDGE);
        }}));

        //TEST SHOULD SET C1
        //TEST SHOULD SET C1
        //TEST SHOULD SET C1
        queueState = chordStructureGenerator.set(queueState);

        queueState.getStructure().forEach(pUnit -> {
            HiveChord chordOne = pUnit.getProgressionUnitBars().get(0).getChord();

            Boolean chordIsMatch = chordOne.isEqual("amin7");

            Assert.assertTrue(chordIsMatch);
        });

        //TEST SHOULD SET L3
        //TEST SHOULD SET L3
        //TEST SHOULD SET L3
        queueState = harmonicLoopGenerator.setHarmonicLoops(queueState, Composition.Layer.HARMONIC_LOOP);

        queueState.getStructure().forEach(pUnit -> {

            HarmonicLoop loopThree = pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).getHarmonicLoop();
//            HarmonicLoop loopFour = pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getHarmonicLoop();

            boolean isEqual = false;

            if(!isEqual){
                isEqual = "loopOneBar".equalsIgnoreCase(loopThree.getFileName()) && loopThree.getCurrentBar() == 3;
            }

            if(!isEqual){
                isEqual = "loopTwoBar".equalsIgnoreCase(loopThree.getFileName()) && loopThree.getCurrentBar() == 3;
            }

            assertThat(isEqual).isTrue();
        });

        //TEST SHOULD SET C3
        //TEST SHOULD SET C3
        //TEST SHOULD SET C3

        queueState.setProcessingState(new ProcessingState(ProcessingState.ProcessingStates.C3_L1));
        queueState.getProcessingState().setProcessingPattern(ProcessingPattern.TWO_BAR);

        queueState = chordStructureGenerator.set(queueState);

        queueState.getStructure().forEach(pUnit -> {

            HiveChord chord = pUnit.getProgressionUnitBars().get(ChordPositions.CHORD_THREE).getChord();

            Boolean chordIsMatch = chord.isEqual("amin7") || chord.isEqual("amin");

            Assert.assertTrue(chordIsMatch);
        });

        //TEST SHOULD SET L1
        //TEST SHOULD SET L1
        //TEST SHOULD SET L1
        queueState.setProcessingState(new ProcessingState(ProcessingState.ProcessingStates.C3_L1));
        queueState.getProcessingState().setProcessingPattern(ProcessingPattern.TWO_BAR);

        queueState = harmonicLoopGenerator.setHarmonicLoops(queueState, Composition.Layer.HARMONIC_LOOP);

        queueState.getStructure().forEach(pUnit -> {
            HarmonicLoop loopOne = pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE).getHarmonicLoop();
//            HarmonicLoop loopTwo = pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).getHarmonicLoop();

//            HarmonicLoop loopThree = pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).getHarmonicLoop();
//            HarmonicLoop loopFour = pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getHarmonicLoop();

            boolean isEqual = false;

            if(!isEqual){
                isEqual = "loopOneBar".equalsIgnoreCase(loopOne.getFileName()) && loopOne.getCurrentBar() == 1;
            }

            if(!isEqual){
                isEqual = "loopTwoBar".equalsIgnoreCase(loopOne.getFileName()) && loopOne.getCurrentBar() == 1;
            }

            if(!isEqual){
                isEqual = "loopFourBar".equalsIgnoreCase(loopOne.getFileName()) && loopOne.getCurrentBar() == 1;
            }

            assertThat(isEqual).isTrue();
        });
    }

    @Test
    public void shouldSetC1WhenPatternIsFourBar() {
        QueueState queueState = new QueueState();
        ProcessingState processingState = new ProcessingState(ProcessingState.ProcessingStates.C1_L1);
        processingState.setProcessingPattern(ProcessingPattern.FOUR_BAR);
        queueState.setProcessingState(processingState);

        queueState.setDataSource(createSourceData());

        queueState.setQueueItem(getQueueItem(new ArrayList<ProgressionUnit.ProgressionType>() {{
            add(ProgressionUnit.ProgressionType.VERSE);
            add(ProgressionUnit.ProgressionType.CHORUS);
            add(ProgressionUnit.ProgressionType.BRIDGE);
        }}));

        //TEST SHOULD SET C1
        //TEST SHOULD SET C1
        //TEST SHOULD SET C1
        queueState = chordStructureGenerator.set(queueState);

        queueState.getStructure().forEach(pUnit -> {
            HiveChord chordOne = pUnit.getProgressionUnitBars().get(0).getChord();
            assertThat(chordOne.isEqual("amin7")).isTrue();
        });
    }

    @Test
    public void shouldSetChordOne_whenPatternIsC1L1() {

        QueueState queueState = new QueueState();
        queueState.setProcessingState(new ProcessingState(ProcessingState.ProcessingStates.C1_L1));
        queueState.getProcessingState().setProcessingPattern(ProcessingPattern.FOUR_BAR);
        queueState.setDataSource(createSourceData());

        queueState.setQueueItem(getQueueItem(new ArrayList<ProgressionUnit.ProgressionType>() {{
            add(ProgressionUnit.ProgressionType.VERSE);
            add(ProgressionUnit.ProgressionType.CHORUS);
            add(ProgressionUnit.ProgressionType.BRIDGE);
        }}));

        //TEST SHOULD SET C1
        //TEST SHOULD SET C1
        //TEST SHOULD SET C1
        queueState = chordStructureGenerator.set(queueState);

        queueState.getStructure().forEach(pUnit -> {
            HiveChord chordOne = pUnit.getProgressionUnitBars().get(0).getChord();

            Boolean chordIsMatch = chordOne.isEqual("amin7");
            assertThat(chordIsMatch).isTrue();
        });

        //TEST SHOULD SET L1
        //TEST SHOULD SET L1
        //TEST SHOULD SET L1
        queueState = harmonicLoopGenerator.setHarmonicLoops(queueState, Composition.Layer.HARMONIC_LOOP);

        queueState.getStructure().forEach(pUnit -> {
            HarmonicLoop loopOne = pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_ONE).getHarmonicLoop();
            HarmonicLoop loopTwo = pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).getHarmonicLoop();
            HarmonicLoop loopThree = pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).getHarmonicLoop();
            HarmonicLoop loopFour = pUnit.getProgressionUnitBars().get(LoopPositions.POSITION_FOUR).getHarmonicLoop();

            assertThat("loopFourBar").isEqualToIgnoringCase(loopOne.getFileName());
            assertThat(loopOne.getCurrentBar()).isEqualTo(1);
            assertThat("loopFourBar").isEqualToIgnoringCase(loopTwo.getFileName());
            assertThat(loopTwo.getCurrentBar()).isEqualTo(2);
            assertThat("loopFourBar").isEqualToIgnoringCase(loopThree.getFileName());
            assertThat(loopThree.getCurrentBar()).isEqualTo(3);
            assertThat("loopFourBar").isEqualToIgnoringCase(loopFour.getFileName());
            assertThat(loopFour.getCurrentBar()).isEqualTo(4);
        });
    }

    @Test
    public void shouldSet2nd3rd4thChords(){

        ProgressionUnit progressionUnitOne = new ProgressionUnit();
        progressionUnitOne.setType(ProgressionUnit.ProgressionType.VERSE);
        progressionUnitOne.initBars(4);

        ProgressionUnit progressionUnitTwo = new ProgressionUnit();
        progressionUnitTwo.setType(ProgressionUnit.ProgressionType.BRIDGE);
        progressionUnitTwo.initBars(4);

        ProgressionUnit progressionUnitThree = new ProgressionUnit();
        progressionUnitThree.setType(ProgressionUnit.ProgressionType.CHORUS);
        progressionUnitThree.initBars(4);

        List<ProgressionUnit> progressionUnits = Arrays.asList(progressionUnitOne,progressionUnitTwo,progressionUnitThree);

        HashMap<ProgressionUnit.ProgressionType, List<HiveChord>> typeToChordsMap = new HashMap<>();

        typeToChordsMap.put(ProgressionUnit.ProgressionType.VERSE, Arrays.asList(new HiveChord("cmin"),new HiveChord("dmin"),new HiveChord("emin"),new HiveChord("fmin")));
        typeToChordsMap.put(ProgressionUnit.ProgressionType.BRIDGE, Arrays.asList(new HiveChord("amaj"),new HiveChord("bmaj"),new HiveChord("cmaj"),new HiveChord("dmaj")));
        typeToChordsMap.put(ProgressionUnit.ProgressionType.CHORUS, Arrays.asList(new HiveChord("ddom7"),new HiveChord("edom7"),new HiveChord("fdom7"),new HiveChord("gdom7")));

        chordStructureGenerator.set2nd3rd4thChordsFor4BarLoops(progressionUnits, typeToChordsMap);

        assertThat(progressionUnits.size()).isEqualTo(3);

        for(ProgressionUnit progressionUnit : progressionUnits){
            List<ProgressionUnitBar> bars = progressionUnit.getProgressionUnitBars();

            if(progressionUnit.getType().equals(ProgressionUnit.ProgressionType.VERSE)){
                assertThat(bars.get(0).getChord().isEqual(new HiveChord("cmin"))).isTrue();
                assertThat(bars.get(1).getChord().isEqual(new HiveChord("dmin"))).isTrue();
                assertThat(bars.get(2).getChord().isEqual(new HiveChord("emin"))).isTrue();
                assertThat(bars.get(3).getChord().isEqual(new HiveChord("fmin"))).isTrue();
            }

            if(progressionUnit.getType().equals(ProgressionUnit.ProgressionType.BRIDGE)){
                assertThat(bars.get(0).getChord().isEqual(new HiveChord("amaj"))).isTrue();
                assertThat(bars.get(1).getChord().isEqual(new HiveChord("bmaj"))).isTrue();
                assertThat(bars.get(2).getChord().isEqual(new HiveChord("cmaj"))).isTrue();
                assertThat(bars.get(3).getChord().isEqual(new HiveChord("dmaj"))).isTrue();
            }

            if(progressionUnit.getType().equals(ProgressionUnit.ProgressionType.CHORUS)){
                assertThat(bars.get(0).getChord().isEqual(new HiveChord("ddom7"))).isTrue();
                assertThat(bars.get(1).getChord().isEqual(new HiveChord("edom7"))).isTrue();
                assertThat(bars.get(2).getChord().isEqual(new HiveChord("fdom7"))).isTrue();
                assertThat(bars.get(3).getChord().isEqual(new HiveChord("gdom7"))).isTrue();
            }
        }
    }
}
