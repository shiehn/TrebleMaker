package com.treblemaker.machinelearning;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.dal.interfaces.IAnalyticsHorizontalDal;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.analytics.AnalyticsHorizontal;
import com.treblemaker.model.composition.CompositionTimeSlot;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class BeatLoopAltTimeseriesClassifierTest extends TestCase {

    private BeatLoopAltTimeseriesClassifier beatLoopAltTimeseriesClassifier;

    private IAnalyticsHorizontalDal mockDal;

    @Test
    public void calculateRatingBigramShouldBe2() {

        mockDal = Mockito.mock(IAnalyticsHorizontalDal.class);

        AnalyticsHorizontal analyticsHorizontal = new AnalyticsHorizontal();
        analyticsHorizontal.setId(1);
        analyticsHorizontal.setRating(2);

        CompositionTimeSlot TS1 = new CompositionTimeSlot(){{setBeatLoopAltId(4);}};
        TS1.setId(1);
        CompositionTimeSlot TS2 = new CompositionTimeSlot(){{setBeatLoopAltId(1);}};
        TS2.setId(2);
        CompositionTimeSlot TS3 = new CompositionTimeSlot(){{setBeatLoopAltId(2);}};
        TS3.setId(3);
        CompositionTimeSlot TS4 = new CompositionTimeSlot(){{setBeatLoopAltId(3);}};
        TS4.setId(4);

        List<CompositionTimeSlot> timeSlots = new ArrayList<>();
        timeSlots.add(TS1);
        timeSlots.add(TS2);
        timeSlots.add(TS3);
        timeSlots.add(TS4);

        analyticsHorizontal.setComposition_time_slots(timeSlots);

        List<AnalyticsHorizontal> mockList = new ArrayList<>();
        mockList.add(analyticsHorizontal);

        Iterable<AnalyticsHorizontal> mockIterable = mockList;

        Mockito.when(mockDal.findAll()).thenReturn(mockIterable);

        beatLoopAltTimeseriesClassifier = new BeatLoopAltTimeseriesClassifier(mockDal);

        BeatLoop pbWeightable = new BeatLoop();
        pbWeightable.setId(3);

        ProgressionUnitBar pbCurrent = new ProgressionUnitBar();
        pbCurrent.setBeatLoopAlt(new BeatLoop(){{setId(3);}});

        ProgressionUnitBar pbPrior = new ProgressionUnitBar();
        pbPrior.setBeatLoopAlt(new BeatLoop(){{setId(2);}});

        ProgressionUnitBar pbTwoPrior = new ProgressionUnitBar();
        pbTwoPrior.setBeatLoopAlt(new BeatLoop(){{setId(1);}});

        //TODO STOP PASSING NULL ...
        int rating = beatLoopAltTimeseriesClassifier.calculateRating(pbWeightable, pbCurrent, pbPrior, pbTwoPrior, mockList);

        Assert.assertEquals(2, rating);
    }

    @Test
    public void calculateRatingBigramShouldAlsoBe2() {

        mockDal = Mockito.mock(IAnalyticsHorizontalDal.class);

        AnalyticsHorizontal analyticsHorizontal = new AnalyticsHorizontal();
        analyticsHorizontal.setId(1);
        analyticsHorizontal.setRating(2);

        CompositionTimeSlot TS1 = new CompositionTimeSlot(){{setBeatLoopAltId(1);}};
        TS1.setId(1);
        CompositionTimeSlot TS2 = new CompositionTimeSlot(){{setBeatLoopAltId(2);}};
        TS2.setId(2);
        CompositionTimeSlot TS3 = new CompositionTimeSlot(){{setBeatLoopAltId(3);}};
        TS3.setId(3);
        CompositionTimeSlot TS4 = new CompositionTimeSlot(){{setBeatLoopAltId(4);}};
        TS4.setId(4);

        List<CompositionTimeSlot> timeSlots = Arrays.asList(TS1, TS2, TS3, TS4);

        analyticsHorizontal.setComposition_time_slots(timeSlots);

        Iterable<AnalyticsHorizontal> mockList = Arrays.asList(analyticsHorizontal);

        beatLoopAltTimeseriesClassifier = new BeatLoopAltTimeseriesClassifier(null);

        BeatLoop pbWeightable = new BeatLoop();
        pbWeightable.setId(3);

        ProgressionUnitBar pbCurrent = new ProgressionUnitBar();
        pbCurrent.setBeatLoopAlt(new BeatLoop(){{setId(3);}});

        ProgressionUnitBar pbPrior = new ProgressionUnitBar();
        pbPrior.setBeatLoopAlt(new BeatLoop(){{setId(2);}});

        ProgressionUnitBar pbTwoPrior = new ProgressionUnitBar();
        pbTwoPrior.setBeatLoopAlt(new BeatLoop(){{setId(1);}});

        int rating = beatLoopAltTimeseriesClassifier.calculateRating(pbWeightable, pbCurrent, pbPrior, pbTwoPrior, mockList);

        Assert.assertEquals(2, rating);
    }

    @Test
    public void calculateRatingUnigramShouldBe1() {

        mockDal = Mockito.mock(IAnalyticsHorizontalDal.class);

        AnalyticsHorizontal analyticsHorizontal = new AnalyticsHorizontal();
        analyticsHorizontal.setId(1);
        analyticsHorizontal.setRating(2);

        CompositionTimeSlot TS1 = new CompositionTimeSlot(){{setBeatLoopAltId(2);}};
        TS1.setId(1);
        CompositionTimeSlot TS2 = new CompositionTimeSlot(){{setBeatLoopAltId(3);}};
        TS2.setId(2);
        CompositionTimeSlot TS3 = new CompositionTimeSlot(){{setBeatLoopAltId(4);}};
        TS3.setId(3);
        CompositionTimeSlot TS4 = new CompositionTimeSlot(){{setBeatLoopAltId(4);}};
        TS4.setId(4);

        List<CompositionTimeSlot> timeSlots = new ArrayList<>();
        timeSlots.add(TS1);
        timeSlots.add(TS2);
        timeSlots.add(TS3);
        timeSlots.add(TS4);

        analyticsHorizontal.setComposition_time_slots(timeSlots);

        List<AnalyticsHorizontal> mockList = new ArrayList<>();
        mockList.add(analyticsHorizontal);

        Iterable<AnalyticsHorizontal> mockIterable = mockList;

        Mockito.when(mockDal.findAll()).thenReturn(mockIterable);

        beatLoopAltTimeseriesClassifier = new BeatLoopAltTimeseriesClassifier(mockDal);

        BeatLoop pbWeightable = new BeatLoop();
        pbWeightable.setId(3);

        ProgressionUnitBar pbCurrent = new ProgressionUnitBar();
        pbCurrent.setBeatLoopAlt(new BeatLoop(){{setId(3);}});

        ProgressionUnitBar pbPrior = new ProgressionUnitBar();
        pbPrior.setBeatLoopAlt(new BeatLoop(){{setId(2);}});

        ProgressionUnitBar pbTwoPrior = null;

        //TODO ISN"T PASSING NULL A PRObLEM
        int rating = beatLoopAltTimeseriesClassifier.calculateRating(pbWeightable, pbCurrent, pbPrior, pbTwoPrior, mockIterable);

        Assert.assertEquals(1, rating);
    }

    @Test
    public void calculateRatingUnigramPlusBigramShouldBe3() {

        mockDal = Mockito.mock(IAnalyticsHorizontalDal.class);

        AnalyticsHorizontal analyticsHorizontal = new AnalyticsHorizontal();
        analyticsHorizontal.setId(1);
        analyticsHorizontal.setRating(2);

        CompositionTimeSlot TS1 = new CompositionTimeSlot(){{setBeatLoopAltId(4);}};
        TS1.setId(1);
        CompositionTimeSlot TS2 = new CompositionTimeSlot(){{setBeatLoopAltId(2);}};
        TS2.setId(2);
        CompositionTimeSlot TS3 = new CompositionTimeSlot(){{setBeatLoopAltId(3);}};
        TS3.setId(3);
        CompositionTimeSlot TS4 = new CompositionTimeSlot(){{setBeatLoopAltId(4);}};
        TS4.setId(4);

        List<CompositionTimeSlot> timeSlots = new ArrayList<>();
        timeSlots.add(TS1);
        timeSlots.add(TS2);
        timeSlots.add(TS3);
        timeSlots.add(TS4);



        AnalyticsHorizontal analyticsHorizontal2 = new AnalyticsHorizontal();
        analyticsHorizontal2.setId(1);
        analyticsHorizontal2.setRating(2);

        CompositionTimeSlot TS1B = new CompositionTimeSlot(){{setBeatLoopAltId(1);}};
        TS1B.setId(1);
        CompositionTimeSlot TS2B = new CompositionTimeSlot(){{setBeatLoopAltId(2);}};
        TS2B.setId(2);
        CompositionTimeSlot TS3B = new CompositionTimeSlot(){{setBeatLoopAltId(3);}};
        TS3B.setId(3);
        CompositionTimeSlot TS4B = new CompositionTimeSlot(){{setBeatLoopAltId(4);}};
        TS4B.setId(4);

        List<CompositionTimeSlot> timeSlotsB = new ArrayList<>();
        timeSlotsB.add(TS1B);
        timeSlotsB.add(TS2B);
        timeSlotsB.add(TS3B);
        timeSlotsB.add(TS4B);

        analyticsHorizontal.setComposition_time_slots(timeSlots);
        analyticsHorizontal2.setComposition_time_slots(timeSlotsB);

        List<AnalyticsHorizontal> mockList = new ArrayList<>();
        mockList.add(analyticsHorizontal);
        mockList.add(analyticsHorizontal2);


        Iterable<AnalyticsHorizontal> mockIterable = mockList;

        Mockito.when(mockDal.findAll()).thenReturn(mockIterable);

        beatLoopAltTimeseriesClassifier = new BeatLoopAltTimeseriesClassifier(mockDal);

        BeatLoop pbWeightable = new BeatLoop();
        pbWeightable.setId(3);

        ProgressionUnitBar pbCurrent = new ProgressionUnitBar();
        pbCurrent.setBeatLoopAlt(new BeatLoop(){{setId(3);}});

        ProgressionUnitBar pbPrior = new ProgressionUnitBar();
        pbPrior.setBeatLoopAlt(new BeatLoop(){{setId(2);}});

        ProgressionUnitBar pbTwoPrior = new ProgressionUnitBar();
        pbTwoPrior.setBeatLoopAlt(new BeatLoop(){{setId(1);}});

        int rating = beatLoopAltTimeseriesClassifier.calculateRating(pbWeightable, pbCurrent, pbPrior, pbTwoPrior, mockIterable);

        Assert.assertEquals(3, rating);
    }
}

