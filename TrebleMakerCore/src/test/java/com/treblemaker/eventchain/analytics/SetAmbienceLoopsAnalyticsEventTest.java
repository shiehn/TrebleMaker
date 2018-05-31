package com.treblemaker.eventchain.analytics;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.fx.FXArpeggioDelay;
import com.treblemaker.model.fx.FXArpeggioWithRating;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"return_queue_early_for_tests=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999", "spring.datasource.url=jdbc:mysql://localhost:3306/hivecomposedb", "spring.datasource.username=root", "spring.datasource.password=redrobes79D"})
public class SetAmbienceLoopsAnalyticsEventTest extends TestCase {
    private QueueState queueState;

    @Autowired
    private IEventChain setArpeggioFXAnalyticsEvent;

    @Before
    public void setup(){

        queueState = new QueueState();

        ProgressionUnitBar barA = new ProgressionUnitBar();
        barA.setHiSynthId(Arrays.asList(111));
        ProgressionUnitBar barB = new ProgressionUnitBar();
        barB.setHiSynthId(Arrays.asList(222));

        ProgressionUnit progressionUnitA = new ProgressionUnit();
        progressionUnitA.setProgressionUnitBars(Arrays.asList(barA,barA,barB,barA));

        ProgressionUnit progressionUnitB = new ProgressionUnit();
        progressionUnitB.setProgressionUnitBars(Arrays.asList(barB,barA,barB,barA));

        ProgressionUnit progressionUnitC = new ProgressionUnit();
        progressionUnitC.setProgressionUnitBars(Arrays.asList(barB,barB,barB,barB));

        ProgressionUnit progressionUnitD = new ProgressionUnit();
        progressionUnitD.setProgressionUnitBars(Arrays.asList(barB,barB,barB,barA));

        List<ProgressionUnit> progressionUnits = Arrays.asList(progressionUnitA, progressionUnitB, progressionUnitC, progressionUnitD);

        ProgressionDTO progression = new ProgressionDTO();
        progression.setStructure(progressionUnits);

        QueueItem queueItem = new QueueItem();
        queueItem.setProgression(progression);

        FXArpeggioDelay delayA = new FXArpeggioDelay();
        delayA.setId(888);

        FXArpeggioDelay delayB = new FXArpeggioDelay();
        delayB.setId(999);

        FXArpeggioWithRating fxArpeggioWithRatingA = new FXArpeggioWithRating(delayA);
        FXArpeggioWithRating fxArpeggioWithRatingB = new FXArpeggioWithRating(delayB);

        Map<Integer, FXArpeggioWithRating> selectedFXMap = new HashMap<>();
        selectedFXMap.put(111,fxArpeggioWithRatingA);
        selectedFXMap.put(222, fxArpeggioWithRatingB);

        queueItem.getProgression().getStructure().forEach(progressionUnit -> progressionUnit.getProgressionUnitBars().forEach(progressionUnitBar -> progressionUnitBar.setSelectedFXMap(Arrays.asList(selectedFXMap))));

        queueState.setQueueItem(queueItem);
    }

    @Test
    public void shouldSetCorrectAnalytics() throws IOException, InterruptedException {

        assertThat(queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlot().getFxArpeggioDelayId()).isNull();
        assertThat(queueState.getStructure().get(1).getProgressionUnitBars().get(1).getCompositionTimeSlot().getFxArpeggioDelayId()).isNull();
        assertThat(queueState.getStructure().get(2).getProgressionUnitBars().get(3).getCompositionTimeSlot().getFxArpeggioDelayId()).isNull();
        assertThat(queueState.getStructure().get(3).getProgressionUnitBars().get(0).getCompositionTimeSlot().getFxArpeggioDelayId()).isNull();

        queueState = setArpeggioFXAnalyticsEvent.set(queueState);

        assertThat(queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlot().getFxArpeggioDelayId()).isEqualTo(888);
        assertThat(queueState.getStructure().get(1).getProgressionUnitBars().get(1).getCompositionTimeSlot().getFxArpeggioDelayId()).isEqualTo(888);
        assertThat(queueState.getStructure().get(2).getProgressionUnitBars().get(3).getCompositionTimeSlot().getFxArpeggioDelayId()).isEqualTo(999);
        assertThat(queueState.getStructure().get(3).getProgressionUnitBars().get(0).getCompositionTimeSlot().getFxArpeggioDelayId()).isEqualTo(999);
    }
}