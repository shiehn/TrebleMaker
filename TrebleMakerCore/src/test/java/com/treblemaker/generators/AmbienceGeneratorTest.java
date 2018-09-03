package com.treblemaker.generators;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.dal.interfaces.IAmbienceLoopsDal;
import com.treblemaker.generators.interfaces.IAmbienceGenerator;
import com.treblemaker.generators.interfaces.IShimGenerator;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.types.Composition;
import com.treblemaker.utils.LoopUtils;
import com.treblemaker.utils.interfaces.IAudioUtils;
import junit.framework.TestCase;
import com.treblemaker.model.AmbienceLoop;
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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.treblemaker.model.progressions.ProgressionUnit.BarCount.FOUR;
import static com.treblemaker.model.progressions.ProgressionUnit.ProgressionType.CHORUS;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class AmbienceGeneratorTest extends TestCase {

    @Autowired
    private IShimGenerator shimGenerator;

    @Autowired
    private IAudioUtils audioUtils;

    @Autowired
    public AppConfigs appConfigs;

    private IAmbienceGenerator ambienceGenerator;
    private IAmbienceGenerator ambienceGeneratorNoMock;

    @Before
    public void setup() {
        //Atonal
        AmbienceLoop ambienceLoopOne = new AmbienceLoop();
        ambienceLoopOne.setId(1);
        ambienceLoopOne.setFileName("illusion.wav");
        ambienceLoopOne.setAudioLength(12.0f);

        //Tonal
        AmbienceLoop ambienceLoopTwo = new AmbienceLoop();
        ambienceLoopTwo.setId(2);
        ambienceLoopTwo.setFileName("illusion.wav");
        ambienceLoopTwo.setAudioLength(12.0f);
        ambienceLoopTwo.setChords(Arrays.asList(new HiveChord("cmaj7"), new HiveChord("bbdom7")));

        //Tonal
        AmbienceLoop ambienceLoopThree = new AmbienceLoop();
        ambienceLoopThree.setId(3);
        ambienceLoopThree.setFileName("illusion.wav");
        ambienceLoopThree.setAudioLength(12.0f);
        ambienceLoopThree.setChords(Arrays.asList(new HiveChord("dmin7"), new HiveChord("amin7")));

        IAmbienceLoopsDal mockAmbienceDal = mock(IAmbienceLoopsDal.class);
        when(mockAmbienceDal.findByAudioLengthLessThanEqual(anyFloat())).thenReturn(Arrays.asList(ambienceLoopOne, ambienceLoopTwo, ambienceLoopThree));

        IAudioUtils mockAudioUtils = mock(IAudioUtils.class);
        when(mockAudioUtils.getAudioLength(anyString())).thenReturn(12.0f);

        ambienceGenerator = new AmbienceGenerator(shimGenerator, mockAmbienceDal, mockAudioUtils, appConfigs);

        ambienceGeneratorNoMock = new AmbienceGenerator(shimGenerator, mockAmbienceDal, audioUtils, appConfigs);
    }

    @Test
    public void ShouldFindBaseShim() {
        final String shimPath = Paths.get(appConfigs.getAmbienceLocation(1),"base_shim.wav").toString();
        File shimFile = new File(shimPath);
        assertThat(shimFile).exists();
    }

    @Test
    public void ShouldBarCountForLoop() {
        float barCount = LoopUtils.getBarCountForLoop(3.0f, 6.1f);
        assertEquals(2.0f, barCount, 0.0f);

        barCount = LoopUtils.getBarCountForLoop(12.3f, 6.1f);
        assertEquals(4.0f, barCount, 0.0f);
    }

    @Test
    public void ShouldSetCorrectAmbienceList() {
        ProgressionUnit unitA = new ProgressionUnit();
        unitA.setComplexity(1);
        unitA.initBars(FOUR.getValue());
        unitA.setType(CHORUS);
        HashMap<Composition.Layer, Boolean> gateA = new HashMap<>();
        gateA.put(Composition.Layer.AMBIENCE_LOOP, true);
        unitA.setGatedLayers(gateA);

        List<ProgressionUnit> pUnits = new ArrayList<>();
        pUnits.add(unitA);

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(pUnits);

        QueueState queueState = new QueueState();
        QueueItem queueItem = new QueueItem();
        queueItem.setBpm(80);
        queueItem.setProgression(progressionDTO);
        queueState.setQueueItem(queueItem);

        queueState = ambienceGenerator.generateAndSetAmbienceLoops(queueState, Composition.Layer.AMBIENCE_LOOP);

        for (int i = 0; i < queueState.getStructure().size(); i++) {
            ProgressionUnit unit = queueState.getStructure().get(i);

            assertThat(unit.getProgressionUnitBars().get(0).getBeatLoop()).isNull();
            assertThat(unit.getProgressionUnitBars().get(0).getAmbienceLoop()).isNotNull();
        }
    }




    @Test
    public void shouldSelectLoopWithMatchingTriad() {

        ProgressionUnitBar barOne = new ProgressionUnitBar();
        barOne.setChord(new HiveChord("amin7"));

        ProgressionUnitBar barTwo = new ProgressionUnitBar();
        barTwo.setChord(new HiveChord("amin"));

        ProgressionUnitBar barThree = new ProgressionUnitBar();
        barThree.setChord(new HiveChord("dmin7"));

        ProgressionUnitBar barFour = new ProgressionUnitBar();
        barFour.setChord(new HiveChord("dmin"));

        ProgressionUnit unitA = new ProgressionUnit();
        unitA.setComplexity(1);
        unitA.initBars(FOUR.getValue());
        unitA.setType(CHORUS);
        unitA.setProgressionUnitBars(Arrays.asList(barOne, barTwo, barThree, barFour));

        List<ProgressionUnit> pUnits = new ArrayList<>();
        pUnits.add(unitA);

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(pUnits);

        QueueState queueState = new QueueState();
        QueueItem queueItem = new QueueItem();
        queueItem.setBpm(80);
        queueItem.setProgression(progressionDTO);
        queueState.setQueueItem(queueItem);


        queueState = ambienceGenerator.generateAndSetAmbienceLoops(queueState, Composition.Layer.AMBIENCE_LOOP);

        assertThat(queueState.getStructure().get(0).getProgressionUnitBars().get(0).getAmbienceLoop().getId()).isEqualTo(3);
    }

    @Test
    public void shouldSelectAtonalLoop() {

        ProgressionUnitBar barOne = new ProgressionUnitBar();
        barOne.setChord(new HiveChord("a#min7"));

        ProgressionUnitBar barTwo = new ProgressionUnitBar();
        barTwo.setChord(new HiveChord("a#min"));

        ProgressionUnitBar barThree = new ProgressionUnitBar();
        barThree.setChord(new HiveChord("d#min7"));

        ProgressionUnitBar barFour = new ProgressionUnitBar();
        barFour.setChord(new HiveChord("d#min"));

        ProgressionUnit unitA = new ProgressionUnit();
        unitA.setComplexity(1);
        unitA.initBars(FOUR.getValue());
        unitA.setType(CHORUS);
        unitA.setProgressionUnitBars(Arrays.asList(barOne, barTwo, barThree, barFour));

        List<ProgressionUnit> pUnits = new ArrayList<>();
        pUnits.add(unitA);

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(pUnits);

        QueueState queueState = new QueueState();
        QueueItem queueItem = new QueueItem();
        queueItem.setBpm(80);
        queueItem.setProgression(progressionDTO);
        queueState.setQueueItem(queueItem);

        queueState = ambienceGenerator.generateAndSetAmbienceLoops(queueState, Composition.Layer.AMBIENCE_LOOP);

        assertThat(queueState.getStructure().get(0).getProgressionUnitBars().get(0).getAmbienceLoop().getId()).isEqualTo(1);
    }
}