package com.treblemaker.generators.beats;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.generators.interfaces.IBeatLoopGenerator;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.RhythmicAccents;
import com.treblemaker.model.SourceData;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.types.Composition;
import com.treblemaker.utils.shims.ShimGenerationUtil;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.*;

import static com.treblemaker.model.progressions.ProgressionUnit.BarCount.FOUR;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class BeatLoopGeneratorTest {

    private QueueState queueStateA;
    private QueueState queueStateB;

    @Autowired
    private IBeatLoopGenerator beatLoopGenerator;

    @Autowired
    public AppConfigs appConfigs;


    private QueueItem createQueueItem(boolean reRender) {

        ArrayList<ProgressionUnit.ProgressionType> progressionTypes = new ArrayList<ProgressionUnit.ProgressionType>() {{
            add(ProgressionUnit.ProgressionType.VERSE);
            add(ProgressionUnit.ProgressionType.VERSE);
            add(ProgressionUnit.ProgressionType.CHORUS);
            add(ProgressionUnit.ProgressionType.VERSE);
            add(ProgressionUnit.ProgressionType.VERSE);
            add(ProgressionUnit.ProgressionType.CHORUS);
            add(ProgressionUnit.ProgressionType.BRIDGE);
            add(ProgressionUnit.ProgressionType.CHORUS);
        }};

        HashMap<ProgressionUnit.ProgressionType, List<HiveChord>> chordsByType = new HashMap<ProgressionUnit.ProgressionType, List<HiveChord>>() {{
            put(ProgressionUnit.ProgressionType.VERSE, Arrays.asList(new HiveChord("d_min7"), new HiveChord("b_dom7"), new HiveChord("a#_maj7"), new HiveChord("c_maj7")));
            put(ProgressionUnit.ProgressionType.CHORUS, Arrays.asList(new HiveChord("a_min7"), new HiveChord("b_dom7"), new HiveChord("a_maj7"), new HiveChord("e_maj7")));
            put(ProgressionUnit.ProgressionType.BRIDGE, Arrays.asList(new HiveChord("d_maj7"), new HiveChord("b_maj7"), new HiveChord("c_maj7"), new HiveChord("a_maj7")));
        }};

        return getQueueItemWithProgressionsAndChordsAndChangeRequest(progressionTypes, chordsByType);
    }

    public QueueItem getQueueItemWithProgressionsAndChordsAndChangeRequest(List<ProgressionUnit.ProgressionType> progressionTypes, HashMap<ProgressionUnit.ProgressionType, List<HiveChord>> chordsByType){

        QueueItem queueItem = new QueueItem();

        ProgressionDTO progressionDTO = new ProgressionDTO();

        for(ProgressionUnit.ProgressionType progressionType : progressionTypes){

            ProgressionUnit pUnit = new ProgressionUnit();
            pUnit.setType(progressionType);

            for(Integer i=0; i<chordsByType.get(progressionType).size(); i++){
                pUnit.getProgressionUnitBars().get(i).setChord(chordsByType.get(progressionType).get(i));
            }

            progressionDTO.getStructure().add(pUnit);
        }

        //ADD DEFAULT BEATLOOPS ..
        for(ProgressionUnit pUnit : progressionDTO.getStructure()){
            pUnit.getProgressionUnitBars().forEach(pBar -> {
                BeatLoop testLoop = new BeatLoop();
                testLoop.setFileName("empty");
                testLoop.setRhythmicAccents(Arrays.asList(new RhythmicAccents()));
                pBar.setBeatLoop(testLoop);
            });
        };

        queueItem.setProgression(progressionDTO);

        return queueItem;
    }

    @Before
    public void initialSetup() {
        ShimGenerationUtil.generateShims(appConfigs);
    }

    private void setup(boolean reRender, boolean isRefactor) {

        SourceData sourceData = new SourceData() {{
            setBeatLoops(new ArrayList<BeatLoop>() {{
                add(new BeatLoop() {{
                    setFileName("beatLoopA");
                    setBarCount(1);
                    setRhythmicAccents(Arrays.asList(new RhythmicAccents()));
                }});
                add(new BeatLoop() {{
                    setFileName("beatLoopB");
                    setBarCount(1);
                    setRhythmicAccents(Arrays.asList(new RhythmicAccents()));
                }});
                add(new BeatLoop() {{
                    setFileName("beatLoopC");
                    setBarCount(1);
                    setRhythmicAccents(Arrays.asList(new RhythmicAccents()));
                }});
            }});
        }};

        queueStateA = new QueueState();
        queueStateA.setDataSource(sourceData);
        queueStateB = new QueueState();
        queueStateB.setDataSource(sourceData);

        queueStateA.setQueueItem(createQueueItem(reRender));
        queueStateB.setQueueItem(createQueueItem(reRender));

        queueStateA.getQueueItem().setIsRefactor(isRefactor);
        queueStateB.getQueueItem().setIsRefactor(isRefactor);
    }

    @Test
    public void ShouldFindBeatShims() {

        for (int supportedBpm : appConfigs.SUPPORTED_BPM) {

            String beatShimsDir = String.format(appConfigs.getBeatShimsDir() + "" + appConfigs.BEAT_SHIM_FILE_NAME, supportedBpm);
            File shimFile = new File(beatShimsDir);
            assertThat(shimFile.exists()).isTrue();
        }
    }

    @Test
    public void ShouldUserBaseShimForBeatLoops() {

        QueueItem queueItem = new QueueItem();
        queueItem.setBpm(80);

        ProgressionUnit unitA = new ProgressionUnit();
        unitA.setComplexity(1);
        unitA.initBars(FOUR.getValue());
        unitA.setType(ProgressionUnit.ProgressionType.CHORUS);
        HashMap<Composition.Layer, Boolean> gateA = new HashMap<>();
        gateA.put(Composition.Layer.BEAT_LOOP, true);
        unitA.setGatedLayers(gateA);

        ProgressionUnit unitB = new ProgressionUnit();
        unitB.setComplexity(90);
        unitB.initBars(FOUR.getValue());
        unitB.setType(ProgressionUnit.ProgressionType.VERSE);
        HashMap<Composition.Layer, Boolean> gateB = new HashMap<>();
        gateB.put(Composition.Layer.BEAT_LOOP, false);
        unitB.setGatedLayers(gateB);

        ProgressionUnit unitC = new ProgressionUnit();
        unitC.setComplexity(1);
        unitC.initBars(FOUR.getValue());
        unitC.setType(ProgressionUnit.ProgressionType.BRIDGE);
        HashMap<Composition.Layer, Boolean> gateC = new HashMap<>();
        gateC.put(Composition.Layer.BEAT_LOOP, true);
        unitC.setGatedLayers(gateC);

        List<ProgressionUnit> pUnits = new ArrayList<>();
        pUnits.add(unitA);
        pUnits.add(unitB);
        pUnits.add(unitC);

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(pUnits);

        QueueState queueState = new QueueState();
        queueState.setDataSource(new SourceData() {{
            setBeatLoops(new ArrayList<BeatLoop>() {{
                add(new BeatLoop() {{
                    setFileName("beatLoopA");
                    setBarCount(1);
                    setRhythmicAccents(Arrays.asList(new RhythmicAccents()));
                }});
                add(new BeatLoop() {{
                    setFileName("beatLoopB");
                    setBarCount(1);
                    setRhythmicAccents(Arrays.asList(new RhythmicAccents()));
                }});
                add(new BeatLoop() {{
                    setFileName("beatLoopC");
                    setBarCount(1);
                    setRhythmicAccents(Arrays.asList(new RhythmicAccents()));
                }});
            }});
        }});
        queueItem.setProgression(progressionDTO);
        queueState.setQueueItem(queueItem);

        queueState = beatLoopGenerator.generateAndSetBeatLoops(queueState, Composition.Layer.BEAT_LOOP);

        for (int i = 0; i < queueState.getStructure().size(); i++) {
            ProgressionUnit unit = queueState.getStructure().get(i);

            if (i == 0) {
                Assert.assertTrue(unit.getProgressionUnitBars().get(0).getBeatLoop().getFileName().contains("beat_shim_2_bars"));
            } else if (i == 1) {
                Assert.assertFalse(unit.getProgressionUnitBars().get(0).getBeatLoop().getFileName().contains("beat_shim_2_bars"));
            } else if (i == 2) {
                Assert.assertTrue(unit.getProgressionUnitBars().get(0).getBeatLoop().getFileName().contains("beat_shim_2_bars"));
            }

//			else if(i == 3){
//				Assert.assertFalse(unit.getAmbienceLoops().get(0).getFileName().contains("base_shim"));
//			}
        }
    }

    @Test
    public void ShouldUserBaseShimForBeatLoopsAlt() {

        QueueItem queueItem = new QueueItem();
        queueItem.setBpm(80);

        ProgressionUnit unitA = new ProgressionUnit();
        unitA.setComplexity(1);
        unitA.initBars(FOUR.getValue());
        unitA.setType(ProgressionUnit.ProgressionType.CHORUS);
        HashMap<Composition.Layer, Boolean> gateA = new HashMap<>();
        gateA.put(Composition.Layer.BEAT_LOOP_ALT, true);
        unitA.setGatedLayers(gateA);

        ProgressionUnit unitB = new ProgressionUnit();
        unitB.setComplexity(90);
        unitB.initBars(FOUR.getValue());
        unitB.setType(ProgressionUnit.ProgressionType.VERSE);
        HashMap<Composition.Layer, Boolean> gateB = new HashMap<>();
        gateB.put(Composition.Layer.BEAT_LOOP_ALT, false);
        unitB.setGatedLayers(gateB);

        ProgressionUnit unitC = new ProgressionUnit();
        unitC.setComplexity(1);
        unitC.initBars(FOUR.getValue());
        unitC.setType(ProgressionUnit.ProgressionType.BRIDGE);
        HashMap<Composition.Layer, Boolean> gateC = new HashMap<>();
        gateC.put(Composition.Layer.BEAT_LOOP_ALT, true);
        unitC.setGatedLayers(gateC);

        List<ProgressionUnit> pUnits = new ArrayList<>();
        pUnits.add(unitA);
        pUnits.add(unitB);
        pUnits.add(unitC);

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(pUnits);

        QueueState queueState = new QueueState();

        SourceData sourceData = new SourceData();

        BeatLoop beatLoopA = new BeatLoop();
        beatLoopA.setFileName("beatLoopA");
        beatLoopA.setBarCount(1);
        beatLoopA.setRhythmicAccents(Arrays.asList(new RhythmicAccents()));

        BeatLoop beatLoopB = new BeatLoop();
        beatLoopB.setFileName("beatLoopB");
        beatLoopB.setBarCount(1);
        beatLoopB.setRhythmicAccents(Arrays.asList(new RhythmicAccents()));

        BeatLoop beatLoopC = new BeatLoop();
        beatLoopC.setFileName("beatLoopC");
        beatLoopC.setBarCount(1);
        beatLoopC.setRhythmicAccents(Arrays.asList(new RhythmicAccents()));

        sourceData.setBeatLoops(Arrays.asList(beatLoopA, beatLoopB, beatLoopC));
        queueState.setDataSource(sourceData);

        queueItem.setProgression(progressionDTO);
        queueState.setQueueItem(queueItem);

        queueState = beatLoopGenerator.generateAndSetBeatLoops(queueState, Composition.Layer.BEAT_LOOP_ALT);

        for (int i = 0; i < queueState.getStructure().size(); i++) {
            ProgressionUnit unit = queueState.getStructure().get(i);

            if (i == 0) {
                Assert.assertTrue(unit.getProgressionUnitBars().get(0).getBeatLoopAlt().getFileName().contains("beat_shim_2_bars"));
            } else if (i == 1) {
                Assert.assertFalse(unit.getProgressionUnitBars().get(0).getBeatLoopAlt().getFileName().contains("beat_shim_2_bars"));
            } else if (i == 2) {
                Assert.assertTrue(unit.getProgressionUnitBars().get(0).getBeatLoopAlt().getFileName().contains("beat_shim_2_bars"));
            }

//			else if(i == 3){
//				Assert.assertFalse(unit.getAmbienceLoops().get(0).getFileName().contains("base_shim"));
//			}
        }
    }

    @Test
    public void ShouldSetCorrectBeatLoopList() {

        QueueItem queueItem = new QueueItem();
        queueItem.setBpm(80);

        ProgressionUnit unitA = new ProgressionUnit();
        unitA.setComplexity(1);
        unitA.initBars(FOUR.getValue());
        unitA.setType(ProgressionUnit.ProgressionType.CHORUS);
        HashMap<Composition.Layer, Boolean> gateA = new HashMap<>();
        gateA.put(Composition.Layer.BEAT_LOOP_ALT, true);
        unitA.setGatedLayers(gateA);

        List<ProgressionUnit> pUnits = new ArrayList<>();
        pUnits.add(unitA);

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(pUnits);


        QueueState queueState = new QueueState();
        queueState.setDataSource(new SourceData() {{
            setBeatLoops(new ArrayList<BeatLoop>() {{
                add(new BeatLoop() {{
                    setFileName("beatLoopA");
                    setBarCount(1);
                }});
                add(new BeatLoop() {{
                    setFileName("beatLoopB");
                    setBarCount(1);
                }});
                add(new BeatLoop() {{
                    setFileName("beatLoopC");
                    setBarCount(1);
                }});
            }});
        }});
        queueItem.setProgression(progressionDTO);
        queueState.setQueueItem(queueItem);

        queueState = beatLoopGenerator.generateAndSetBeatLoops(queueState, Composition.Layer.BEAT_LOOP_ALT);

        for (int i = 0; i < queueState.getStructure().size(); i++) {
            ProgressionUnit unit = queueState.getStructure().get(i);

            Assert.assertTrue(unit.getProgressionUnitBars().get(0).getBeatLoop() == null);
            Assert.assertTrue(unit.getProgressionUnitBars().get(0).getBeatLoopAlt() != null);
            Assert.assertTrue(unit.getProgressionUnitBars().get(1).getBeatLoopAlt() != null);
        }
    }

    @Test
    public void ShouldSetCorrectBeatLoopCurrentPositions() {

        QueueItem queueItem = new QueueItem();
        queueItem.setBpm(80);

        ProgressionUnit unitA = new ProgressionUnit();
        unitA.initBars(4);
        unitA.setComplexity(1);
        unitA.setBarCount(FOUR.getValue());
        unitA.setType(ProgressionUnit.ProgressionType.VERSE);
        HashMap<Composition.Layer, Boolean> gateA = new HashMap<>();
        gateA.put(Composition.Layer.BEAT_LOOP, true);
        unitA.setGatedLayers(gateA);

        ProgressionUnit unitB = new ProgressionUnit();
        unitB.initBars(4);
        unitB.setComplexity(1);
        unitB.setBarCount(FOUR.getValue());
        unitB.setType(ProgressionUnit.ProgressionType.CHORUS);
        HashMap<Composition.Layer, Boolean> gateB = new HashMap<>();
        gateB.put(Composition.Layer.BEAT_LOOP, true);
        unitB.setGatedLayers(gateB);

        ProgressionUnit unitC = new ProgressionUnit();
        unitC.initBars(4);
        unitC.setComplexity(1);
        unitC.setBarCount(FOUR.getValue());
        unitC.setType(ProgressionUnit.ProgressionType.BRIDGE);
        HashMap<Composition.Layer, Boolean> gateC = new HashMap<>();
        gateC.put(Composition.Layer.BEAT_LOOP, true);
        unitC.setGatedLayers(gateC);

        List<ProgressionUnit> pUnits = new ArrayList<>();
        pUnits.add(unitA);
        pUnits.add(unitB);
        pUnits.add(unitC);

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(pUnits);

        BeatLoop loopA = new BeatLoop() {{
            setFileName("beatLoopA");
            setBarCount(1);
        }};

        BeatLoop loopB = new BeatLoop() {{
            setFileName("beatLoopB");
            setBarCount(2);
        }};

        BeatLoop loopC = new BeatLoop() {{
            setFileName("beatLoopC");
            setBarCount(4);
        }};

        QueueState queueState = new QueueState();
        queueState.setDataSource(new SourceData() {{
            setBeatLoops(new ArrayList<BeatLoop>() {{
                add(loopA);
                add(loopB);
                add(loopC);
            }});
        }});
        queueItem.setProgression(progressionDTO);
        queueState.setQueueItem(queueItem);

        Map<ProgressionUnit.ProgressionType, BeatLoop> beatLoopByType = new HashMap<>();
        beatLoopByType.put(ProgressionUnit.ProgressionType.VERSE, loopA);
        beatLoopByType.put(ProgressionUnit.ProgressionType.CHORUS, loopB);
        beatLoopByType.put(ProgressionUnit.ProgressionType.BRIDGE, loopC);

        beatLoopGenerator.setBeatLoops(beatLoopByType, Composition.Layer.BEAT_LOOP, queueState.getStructure());

        queueState.getStructure().forEach(pUnit -> {

            if (pUnit.getType() == ProgressionUnit.ProgressionType.VERSE) {
                for (int i = 0; i < pUnit.getProgressionUnitBars().size(); i++) {

                    ProgressionUnitBar pUnitBar = pUnit.getProgressionUnitBars().get(i);

                    if (i == 0) {
                        Assert.assertTrue(pUnitBar.getBeatLoop().getFileName().equalsIgnoreCase("beatLoopA"));
                        Assert.assertEquals(1, pUnitBar.getBeatLoop().getCurrentBar());
                    }

                    if (i == 1) {
                        Assert.assertTrue(pUnitBar.getBeatLoop().getFileName().equalsIgnoreCase("beatLoopA"));
                        Assert.assertEquals(1, pUnitBar.getBeatLoop().getCurrentBar());
                    }

                    if (i == 2) {
                        Assert.assertTrue(pUnitBar.getBeatLoop().getFileName().equalsIgnoreCase("beatLoopA"));
                        Assert.assertEquals(1, pUnitBar.getBeatLoop().getCurrentBar());
                    }

                    if (i == 3) {
                        Assert.assertTrue(pUnitBar.getBeatLoop().getFileName().equalsIgnoreCase("beatLoopA"));
                        Assert.assertEquals(1, pUnitBar.getBeatLoop().getCurrentBar());
                    }
                }
            }

            if (pUnit.getType() == ProgressionUnit.ProgressionType.CHORUS) {
                for (int i = 0; i < pUnit.getProgressionUnitBars().size(); i++) {

                    ProgressionUnitBar pUnitBar = pUnit.getProgressionUnitBars().get(i);

                    if (i == 0) {
                        Assert.assertTrue(pUnitBar.getBeatLoop().getFileName().equalsIgnoreCase("beatLoopB"));
                        Assert.assertEquals(1, pUnitBar.getBeatLoop().getCurrentBar());
                    }

                    if (i == 1) {
                        Assert.assertTrue(pUnitBar.getBeatLoop().getFileName().equalsIgnoreCase("beatLoopB"));
                        Assert.assertEquals(2, pUnitBar.getBeatLoop().getCurrentBar());
                    }

                    if (i == 2) {
                        Assert.assertTrue(pUnitBar.getBeatLoop().getFileName().equalsIgnoreCase("beatLoopB"));
                        Assert.assertEquals(1, pUnitBar.getBeatLoop().getCurrentBar());
                    }

                    if (i == 3) {
                        Assert.assertTrue(pUnitBar.getBeatLoop().getFileName().equalsIgnoreCase("beatLoopB"));
                        Assert.assertEquals(2, pUnitBar.getBeatLoop().getCurrentBar());
                    }
                }
            }


            if (pUnit.getType() == ProgressionUnit.ProgressionType.BRIDGE) {
                for (int i = 0; i < pUnit.getProgressionUnitBars().size(); i++) {

                    ProgressionUnitBar pUnitBar = pUnit.getProgressionUnitBars().get(i);

                    if (i == 0) {
                        Assert.assertTrue(pUnitBar.getBeatLoop().getFileName().equalsIgnoreCase("beatLoopC"));
                        Assert.assertEquals(1, pUnitBar.getBeatLoop().getCurrentBar());
                    }

                    if (i == 1) {
                        Assert.assertTrue(pUnitBar.getBeatLoop().getFileName().equalsIgnoreCase("beatLoopC"));
                        Assert.assertEquals(2, pUnitBar.getBeatLoop().getCurrentBar());
                    }

                    if (i == 2) {
                        Assert.assertTrue(pUnitBar.getBeatLoop().getFileName().equalsIgnoreCase("beatLoopC"));
                        Assert.assertEquals(3, pUnitBar.getBeatLoop().getCurrentBar());
                    }

                    if (i == 3) {
                        Assert.assertTrue(pUnitBar.getBeatLoop().getFileName().equalsIgnoreCase("beatLoopC"));
                        Assert.assertEquals(4, pUnitBar.getBeatLoop().getCurrentBar());
                    }
                }
            }
        });
    }
}
