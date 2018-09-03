package com.treblemaker.renderers;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.TestBase;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.rendertransports.BeatLoopRenderTransport;
import com.treblemaker.renderers.interfaces.IBeatLoopRenderer;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.treblemaker.model.progressions.ProgressionUnit.BarCount.FOUR;

@Ignore
@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class BeatLoopAltRendererTest extends TestBase {

    @Autowired
    private IBeatLoopRenderer beatLoopAltRenderer;

    @Autowired
    public AppConfigs appConfigs;

    @Test
    public void ShouldExtractLoopsToRender(){

        ProgressionUnit progressionUnit = new ProgressionUnit();
        progressionUnit.initBars(FOUR.getValue());

        BeatLoop loopAltOneA = new BeatLoop();
        loopAltOneA.setFileName("mock_data_beatloop_one.wav");
        loopAltOneA.setBarCount(2);
        loopAltOneA.setCurrentBar(1);

        BeatLoop loopAltOneB = new BeatLoop();
        loopAltOneB.setFileName("mock_data_beatloop_one.wav");
        loopAltOneB.setBarCount(2);
        loopAltOneB.setCurrentBar(2);

        BeatLoop loopAltTwo = new BeatLoop();
        loopAltTwo.setFileName("mock_data_beatloop_two.wav");
        loopAltTwo.setBarCount(1);
        loopAltTwo.setCurrentBar(1);

        progressionUnit.getProgressionUnitBars().get(0).setBeatLoopAlt(loopAltOneA);
        progressionUnit.getProgressionUnitBars().get(1).setBeatLoopAlt(loopAltOneB);
        progressionUnit.getProgressionUnitBars().get(3).setBeatLoopAlt(loopAltTwo);

        List<ProgressionUnit> progressionUnits = new ArrayList<>();
        progressionUnits.add(progressionUnit);

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(progressionUnits);

        QueueItem queueItem = new QueueItem();
        queueItem.setBpm(80);
        queueItem.setQueueItemId("TEST");
        queueItem.setProgression(progressionDTO);

        BeatLoopRenderTransport beatLoopRenderTransport = beatLoopAltRenderer.extractLoopsToRender(queueItem);
        List<BeatLoop> beatLoops = beatLoopRenderTransport.getBeatLoops();

        //ensure each source file exists ..
//        beatLoops.forEach(bLoop -> {
//
//            File file = new File(appConfigs.BEAT_LOOPS_LOCATION + bLoop.getFileName());
//            Assert.assertTrue(file.exists());
//        });

        Assert.assertEquals(3, beatLoops.size());

        Assert.assertTrue(beatLoops.get(0).getFileName().equalsIgnoreCase("mock_data_beatloop_one.wav"));
        Assert.assertTrue(beatLoops.get(2).getFileName().equalsIgnoreCase("mock_data_beatloop_two.wav"));

        File file = new File("c://Capricious//Compositions//audioshims//TEST//TEST_BeatLoopAlt_80.wav");

        Assert.assertTrue(file.exists());

        //TEST FINAL OUTPUT
        String songName = UUID.randomUUID().toString();
        final String AUDIO_PART_FILE_PATH = appConfigs.getCompositionOutput() + "audioparts//" + songName + "//";

        beatLoopAltRenderer.concatenateAndFinalizeRendering("FIX THIS", AUDIO_PART_FILE_PATH, appConfigs.COMP_HARMONIC_ALT_FILENAME, beatLoops);
    }
}
