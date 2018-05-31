package com.treblemaker.eventchain;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.eventchain.interfaces.IAnalyticsEvent;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.SynthTemplate;
import com.treblemaker.model.composition.CompositionTimeSlot;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.treblemaker.model.progressions.ProgressionUnit.BarCount.FOUR;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"return_queue_early_for_tests=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999", "spring.datasource.url=jdbc:mysql://localhost:3306/hivecomposedb", "spring.datasource.username=root", "spring.datasource.password=redrobes79D"})
public class AnalyticsTest extends TestCase {

    @Autowired
    private IAnalyticsEvent analyticsEvent;

    @Autowired
    @Qualifier(value = "setChordStructureAnalyticsEvent")
    private IEventChain setChordStructureAnalyticsEvent;

    @Autowired
    @Qualifier("setBeatLoopAnalyticsEvent")
    private IEventChain setBeatLoopAnalyticsEvent;

    @Autowired
    @Qualifier("setSynthTemplateAnalytics")
    private IEventChain setSynthTemplateAnalytics;

    private QueueState createTestQueue() {

        ProgressionUnit unit1 = new ProgressionUnit();
        unit1.initBars(FOUR.getValue());

        //BEAT LOOPS .....
        BeatLoop loop1 = new BeatLoop();
        loop1.setId(11);
        loop1.setBarCount(1);

        BeatLoop loop2 = new BeatLoop();
        loop2.setId(12);
        loop2.setBarCount(1);

        BeatLoop loop3 = new BeatLoop();
        loop3.setId(13);
        loop3.setBarCount(2);

        ProgressionUnitBar barA = new ProgressionUnitBar();
        barA.setBeatLoop(loop1);

        ProgressionUnitBar barB =  new ProgressionUnitBar();
        barB.setBeatLoop(loop2);

        ProgressionUnitBar barC = new ProgressionUnitBar();
        barC.setBeatLoop(loop3);

        //List<ProgressionUnitBar> bars = new ArrayList<>();
        unit1.getProgressionUnitBars().set(0, barA);
        unit1.getProgressionUnitBars().set(1, barB);
        unit1.getProgressionUnitBars().set(2, barC);
        unit1.getProgressionUnitBars().set(3, barC);

        //SET SYNTHS ..
        unit1.getProgressionUnitBars().forEach(pBar -> {
            pBar.setHiSynthId(Arrays.asList(1));
            pBar.setMidSynthId(Arrays.asList(2));
            pBar.setLowSynthId(Arrays.asList(3));
        });

        unit1.getProgressionUnitBars().forEach(pBar -> {
            pBar.setCompositionTimeSlot(new CompositionTimeSlot());
        });

        List<ProgressionUnit> units = new ArrayList<>();
        units.add(unit1);

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(units);

        SynthTemplate template = new SynthTemplate();
        template.setHiSynthId(12);
        template.setHiSynthIdAlt(13);
        template.setMidSynthId(21);
        template.setMidSynthIdAlt(22);
        template.setLowSynthId(32);
        template.setLowSynthIdAlt(33);

        List<SynthTemplate> templates = new ArrayList<>();
        templates.add(template);

        progressionDTO.getStructure().forEach(pUnit -> {
            pUnit.getProgressionUnitBars().forEach(pBar ->  {
                pBar.setSynthTemplates(templates);
                pBar.setHiSynthId(Arrays.asList(templates.get(0).getHiSynthId()));
                pBar.setMidSynthId(Arrays.asList(templates.get(0).getMidSynthId()));
                pBar.setLowSynthId(Arrays.asList(templates.get(0).getLowSynthId()));
            });
        });

        QueueItem queueItem = new QueueItem();
        queueItem.setBpm(80);
        queueItem.setProgression(progressionDTO);

        QueueState queueState = new QueueState();
        queueState.setQueueItem(queueItem);

        return queueState;
    }

    @Test
    public void shouldInitTimeSlots() {

        QueueState queueState = createTestQueue();
        queueState = analyticsEvent.initAnalytics(queueState);

        //SHOULD CONTAIN 4 TIME SLOTS

        int timeSlotCount = 0;

        for (ProgressionUnit unit : queueState.getStructure()) {
            timeSlotCount += unit.getProgressionUnitBars().size();
        }

        Assert.assertEquals(4, timeSlotCount);
    }

    @Test
    public void shouldTrackBeatLoops() {

        QueueState queueState = createTestQueue();
        queueState = analyticsEvent.initAnalytics(queueState);
        queueState = setBeatLoopAnalyticsEvent.set(queueState);

        //CURRENTLY WE ARE USING ON BEAT TYPE PER UNIT ..

        Assert.assertEquals(11, queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlot().getBeatLoopId().intValue());
        Assert.assertEquals(12, queueState.getStructure().get(0).getProgressionUnitBars().get(1).getCompositionTimeSlot().getBeatLoopId().intValue());
        Assert.assertEquals(13, queueState.getStructure().get(0).getProgressionUnitBars().get(2).getCompositionTimeSlot().getBeatLoopId().intValue());
        Assert.assertEquals(13, queueState.getStructure().get(0).getProgressionUnitBars().get(3).getCompositionTimeSlot().getBeatLoopId().intValue());
    }

    @Test
    public void shouldTrackSynthTemplates() {

        QueueState queueState = createTestQueue();
        queueState = analyticsEvent.initAnalytics(queueState);
        queueState = setSynthTemplateAnalytics.set(queueState );

        //CURRENTLY WE ARE USING ON BEAT TYPE PER UNIT ..

        Assert.assertSame(12, queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlot().getSynthTemplateHiId());
        Assert.assertSame(12, queueState.getStructure().get(0).getProgressionUnitBars().get(1).getCompositionTimeSlot().getSynthTemplateHiId());
        Assert.assertSame(12, queueState.getStructure().get(0).getProgressionUnitBars().get(2).getCompositionTimeSlot().getSynthTemplateHiId());
        Assert.assertSame(12, queueState.getStructure().get(0).getProgressionUnitBars().get(3).getCompositionTimeSlot().getSynthTemplateHiId());

        Assert.assertSame(21, queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlot().getSynthTemplateMidId());
        Assert.assertSame(21, queueState.getStructure().get(0).getProgressionUnitBars().get(1).getCompositionTimeSlot().getSynthTemplateMidId());
        Assert.assertSame(21, queueState.getStructure().get(0).getProgressionUnitBars().get(2).getCompositionTimeSlot().getSynthTemplateMidId());
        Assert.assertSame(21, queueState.getStructure().get(0).getProgressionUnitBars().get(3).getCompositionTimeSlot().getSynthTemplateMidId());

        Assert.assertSame(32, queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlot().getSynthTemplateLowId());
        Assert.assertSame(32, queueState.getStructure().get(0).getProgressionUnitBars().get(1).getCompositionTimeSlot().getSynthTemplateLowId());
        Assert.assertSame(32, queueState.getStructure().get(0).getProgressionUnitBars().get(2).getCompositionTimeSlot().getSynthTemplateLowId());
        Assert.assertSame(32, queueState.getStructure().get(0).getProgressionUnitBars().get(3).getCompositionTimeSlot().getSynthTemplateLowId());
    }
}