package com.treblemaker.renderers;

import com.treblemaker.Application;
import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.kick.KickPattern;
import com.treblemaker.model.kick.KickSample;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class KickPatternRendererTest extends TestCase {

    @Autowired
    private KickPatternRenderer kickPatternRenderer;

    private QueueState queueState;
    private KickPattern kickPatternOne;
    private KickPattern kickPatternTwo;
    private KickPattern kickPatternThree;

    @Before
    public void setup() {
        //BAR ONE
        KickPattern kickOneA = new KickPattern();
        kickOneA.setId(11);

        KickPattern kickOneB = new KickPattern();
        kickOneB.setId(12);

        KickPattern kickOneC = new KickPattern();
        kickOneC.setId(13);

        KickPattern kickOneD = new KickPattern();
        kickOneD.setId(14);

        //BAR TWO
        KickPattern kickTwoA = new KickPattern();
        kickTwoA.setId(21);

        KickPattern kickTwoB = new KickPattern();
        kickTwoB.setId(22);

        KickPattern kickTwoC = new KickPattern();
        kickTwoC.setId(23);

        KickPattern kickTwoD = new KickPattern();
        kickTwoD.setId(24);

        //BAR THREE
        KickPattern kickThreeA = new KickPattern();
        kickThreeA.setId(31);

        KickPattern kickThreeB = new KickPattern();
        kickThreeB.setId(32);

        KickPattern kickThreeC = new KickPattern();
        kickThreeC.setId(33);

        KickPattern kickThreeD = new KickPattern();
        kickThreeD.setId(34);

        //PROGRESSIONS
        ProgressionUnit pUnitOne = new ProgressionUnit();
        pUnitOne.setType(ProgressionUnit.ProgressionType.CHORUS);
        pUnitOne.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar()));
        pUnitOne.getProgressionUnitBars().get(0).setKickPattern(kickOneA);
        pUnitOne.getProgressionUnitBars().get(1).setKickPattern(kickOneB);
        pUnitOne.getProgressionUnitBars().get(2).setKickPattern(kickOneC);
        pUnitOne.getProgressionUnitBars().get(3).setKickPattern(kickOneD);

        ProgressionUnit pUnitTwo = new ProgressionUnit();
        pUnitTwo.setType(ProgressionUnit.ProgressionType.BRIDGE);
        pUnitTwo.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar()));
        pUnitTwo.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar()));
        pUnitTwo.getProgressionUnitBars().get(0).setKickPattern(kickTwoA);
        pUnitTwo.getProgressionUnitBars().get(1).setKickPattern(kickTwoB);
        pUnitTwo.getProgressionUnitBars().get(2).setKickPattern(kickTwoC);
        pUnitTwo.getProgressionUnitBars().get(3).setKickPattern(kickTwoD);

        ProgressionUnit pUnitThree = new ProgressionUnit();
        pUnitThree.setType(ProgressionUnit.ProgressionType.VERSE);
        pUnitThree.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar()));
        pUnitThree.getProgressionUnitBars().get(0).setKickPattern(kickThreeA);
        pUnitThree.getProgressionUnitBars().get(1).setKickPattern(kickThreeB);
        pUnitThree.getProgressionUnitBars().get(2).setKickPattern(kickThreeC);
        pUnitThree.getProgressionUnitBars().get(3).setKickPattern(kickThreeD);

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(Arrays.asList(pUnitOne, pUnitTwo, pUnitThree));

        QueueItem queueItem = new QueueItem();
        queueItem.setProgression(progressionDTO);

        queueState = new QueueState();
        queueState.setQueueItem(queueItem);

        KickPattern kickPatternOne;
        KickPattern kickPatternTwo;
    }

    @Test
    public void shouldCreateKickPatternRenderList() {

        List<KickPattern> patternsToRender = kickPatternRenderer.getPatternsToRender(queueState);

        assertThat(patternsToRender).hasSize(12);
        assertThat(patternsToRender.get(0).getId()).isEqualTo(11);
        assertThat(patternsToRender.get(1).getId()).isEqualTo(12);
        assertThat(patternsToRender.get(2).getId()).isEqualTo(13);
        assertThat(patternsToRender.get(3).getId()).isEqualTo(14);
        assertThat(patternsToRender.get(4).getId()).isEqualTo(21);
        assertThat(patternsToRender.get(5).getId()).isEqualTo(22);
        assertThat(patternsToRender.get(6).getId()).isEqualTo(23);
        assertThat(patternsToRender.get(7).getId()).isEqualTo(24);
        assertThat(patternsToRender.get(8).getId()).isEqualTo(31);
        assertThat(patternsToRender.get(9).getId()).isEqualTo(32);
        assertThat(patternsToRender.get(10).getId()).isEqualTo(33);
        assertThat(patternsToRender.get(11).getId()).isEqualTo(34);
    }


    @Test
    public void shouldCreateStringArray() {
        int BPM = 80;

        //KICK SETUP ..
        kickPatternOne = new KickPattern();
        kickPatternOne.setId(1);
        //0
        kickPatternOne.setFromArray(new int[]{0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1});

        kickPatternTwo = new KickPattern();
        kickPatternTwo.setId(2);
        //16
        kickPatternTwo.setFromArray(new int[]{1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0});

        kickPatternThree = new KickPattern();
        kickPatternThree.setId(3);
        //32
        kickPatternThree.setFromArray(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});

        List<KickPattern> patternsToRender = new ArrayList<>();
        patternsToRender.add(kickPatternOne);
        patternsToRender.add(kickPatternTwo);
        patternsToRender.add(kickPatternThree);

        String shimpath = Paths.get("/","signalsandsorcery", "TrebleMaker","Loops","2","Kicks","shim_kick_80.wav").toString();
        String kickpath = Paths.get("/","signalsandsorcery", "TrebleMaker","Loops","2","Kicks","kick17.wav").toString();

        KickSample kickSample = new KickSample();
        kickSample.setStationId("2");
        kickSample.setFileName("kick17.wav");
        List<String> kickPatternFilePaths = kickPatternRenderer.getKickPatternFilePaths(patternsToRender, kickSample, BPM);

        assertThat(kickPatternFilePaths).hasSize(48);

        assertThat(kickPatternFilePaths.get(0)).isEqualToIgnoringCase(shimpath);
        assertThat(kickPatternFilePaths.get(4)).isEqualToIgnoringCase(kickpath);

        assertThat(kickPatternFilePaths.get(19)).isEqualToIgnoringCase(shimpath);
        assertThat(kickPatternFilePaths.get(31)).isEqualToIgnoringCase(shimpath);

        assertThat(kickPatternFilePaths.get(37)).isEqualToIgnoringCase(kickpath);
        assertThat(kickPatternFilePaths.get(46)).isEqualToIgnoringCase(kickpath);

        BaseRenderer baseRenderer = new BaseRenderer();

        try {
            baseRenderer.concatenateFiles(kickPatternFilePaths, Paths.get("/","signalsandsorcery","TrebleMaker","Loops","2","SUNDAY_KICK.wav").toString());
        } catch (Exception e) {
            Application.logger.debug("LOG:", e);
        }
    }
}