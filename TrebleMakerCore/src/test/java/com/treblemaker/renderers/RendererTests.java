package com.treblemaker.renderers;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.rendertransports.BeatLoopRenderTransport;
import com.treblemaker.model.rendertransports.HarmonicLoopRenderTransport;
import com.treblemaker.renderers.interfaces.IBeatLoopRenderer;
import com.treblemaker.renderers.interfaces.IHarmonicLoopRenderer;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class RendererTests extends TestCase {

    @Autowired
    private IBeatLoopRenderer beatLoopRenderer;

    @Autowired
    private IHarmonicLoopRenderer harmonicLoopRenderer;

    @Test
    public void ShouldConcatinateBeatLoops() {

        BeatLoop loopA = new BeatLoop();
        loopA.setBarCount(1);
        loopA.setCurrentBar(1);
        loopA.setFileName("loopA");

        BeatLoop loopB1 = new BeatLoop();
        loopB1.setBarCount(2);
        loopB1.setCurrentBar(1);
        loopB1.setFileName("loopB");

        BeatLoop loopB2 = new BeatLoop();
        loopB2.setBarCount(2);
        loopB2.setCurrentBar(2);
        loopB2.setFileName("loopB");

        ProgressionUnit progressionUnit1 = new ProgressionUnit();
        progressionUnit1.initBars(4);
        progressionUnit1.getProgressionUnitBars().get(0).setBeatLoop(loopA);
        progressionUnit1.getProgressionUnitBars().get(1).setBeatLoop(loopA);
        progressionUnit1.getProgressionUnitBars().get(2).setBeatLoop(loopA);
        progressionUnit1.getProgressionUnitBars().get(3).setBeatLoop(loopA);

        ProgressionUnit progressionUnit2 = new ProgressionUnit();
        progressionUnit2.initBars(4);
        progressionUnit2.getProgressionUnitBars().get(0).setBeatLoop(loopB1);
        progressionUnit2.getProgressionUnitBars().get(1).setBeatLoop(loopB2);
        progressionUnit2.getProgressionUnitBars().get(2).setBeatLoop(loopB1);
        progressionUnit2.getProgressionUnitBars().get(3).setBeatLoop(loopB2);

        QueueItem queueItem = new QueueItem();
        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(new ArrayList<ProgressionUnit>() {{
            add(progressionUnit1);
            add(progressionUnit2);
        }});
        queueItem.setProgression(progressionDTO);

        BeatLoopRenderTransport beatLoopRenderTransport = beatLoopRenderer.extractLoopsToRender(queueItem);
        List<BeatLoop> loopsToRender = beatLoopRenderTransport.getBeatLoops();

        Assert.assertEquals(6, loopsToRender.size());

        Assert.assertTrue(loopsToRender.get(0).getFileName().equalsIgnoreCase("loopA"));

        Assert.assertTrue(loopsToRender.get(4).getFileName().equalsIgnoreCase("loopB"));
        Assert.assertEquals(1, loopsToRender.get(4).getCurrentBar());
        Assert.assertTrue(loopsToRender.get(5).getFileName().equalsIgnoreCase("loopB"));
        Assert.assertEquals(1, loopsToRender.get(5).getCurrentBar());
    }

    @Test
    public void ShouldConcatinateHarmonicLoops() {

        HarmonicLoop loopA = new HarmonicLoop();
        loopA.setBarCount(1);
        loopA.setCurrentBar(1);
        loopA.setFileName("loopA");

        HarmonicLoop loopB1 = new HarmonicLoop();
        loopB1.setBarCount(2);
        loopB1.setCurrentBar(1);
        loopB1.setFileName("loopB");

        HarmonicLoop loopB2 = new HarmonicLoop();
        loopB2.setBarCount(2);
        loopB2.setCurrentBar(2);
        loopB2.setFileName("loopB");

        ProgressionUnit progressionUnit1 = new ProgressionUnit();
        progressionUnit1.initBars(4);
        progressionUnit1.getProgressionUnitBars().get(0).setHarmonicLoop(loopA);
        progressionUnit1.getProgressionUnitBars().get(1).setHarmonicLoop(loopA);
        progressionUnit1.getProgressionUnitBars().get(2).setHarmonicLoop(loopA);
        progressionUnit1.getProgressionUnitBars().get(3).setHarmonicLoop(loopA);

        ProgressionUnit progressionUnit2 = new ProgressionUnit();
        progressionUnit2.initBars(4);
        progressionUnit2.getProgressionUnitBars().get(0).setHarmonicLoop(loopB1);
        progressionUnit2.getProgressionUnitBars().get(1).setHarmonicLoop(loopB2);
        progressionUnit2.getProgressionUnitBars().get(2).setHarmonicLoop(loopB1);
        progressionUnit2.getProgressionUnitBars().get(3).setHarmonicLoop(loopB2);

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(new ArrayList<ProgressionUnit>() {{
            add(progressionUnit1);
            add(progressionUnit2);
        }});

        QueueItem queueItem = new QueueItem();
        queueItem.setProgression(progressionDTO);

        HarmonicLoopRenderTransport harmonicLoopRenderTransport = harmonicLoopRenderer.extractLoopsToRender(queueItem);

        List<HarmonicLoop> loopsToRender = harmonicLoopRenderTransport.getHarmonicLoops();

        Assert.assertEquals(6, loopsToRender.size());

        Assert.assertTrue(loopsToRender.get(0).getFileName().equalsIgnoreCase("loopA"));

        Assert.assertTrue(loopsToRender.get(4).getFileName().equalsIgnoreCase("loopB"));
        Assert.assertEquals(1, loopsToRender.get(4).getCurrentBar());
        Assert.assertTrue(loopsToRender.get(5).getFileName().equalsIgnoreCase("loopB"));
        Assert.assertEquals(1, loopsToRender.get(5).getCurrentBar());
    }
}
