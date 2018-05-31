package com.treblemaker.eventchain;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.hat.HatPattern;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class SetHatPatternAnalyticsEventTest extends TestCase {

    QueueState queueState;
    SetHatPatternAnalyticsEvent setHatPatternAnalyticsEvent;

    @Before
    public void setup(){
        setHatPatternAnalyticsEvent = new SetHatPatternAnalyticsEvent();

        HatPattern hatPatternOne = new HatPattern();
        hatPatternOne.setId(1);

        HatPattern hatPatternTwo = new HatPattern();
        hatPatternTwo.setId(2);

        ProgressionUnitBar barOne = new ProgressionUnitBar();
        barOne.setHatPattern(hatPatternOne);

        ProgressionUnitBar barTwo = new ProgressionUnitBar();
        barTwo.setHatPattern(hatPatternTwo);

        ProgressionUnitBar barThree = new ProgressionUnitBar();
        barThree.setHatPattern(hatPatternOne);

        ProgressionUnitBar barFour = new ProgressionUnitBar();
        barFour.setHatPattern(hatPatternTwo);

        ProgressionUnit progressionUnit = new ProgressionUnit();
        progressionUnit.setProgressionUnitBars(Arrays.asList(barOne,barTwo, barOne, barTwo));

        ProgressionDTO progressionDTO  = new ProgressionDTO();
        progressionDTO.setStructure(Arrays.asList(progressionUnit));

        QueueItem queueItem = new QueueItem();
        queueItem.setProgression(progressionDTO);

        queueState = new QueueState();
        queueState.setQueueItem(queueItem);
    }

    @Test
    public void shouldSetHatPatternInCompositionTimeSlot(){

        QueueState result = setHatPatternAnalyticsEvent.set(queueState);

        List<ProgressionUnitBar> pBars = result.getStructure().get(0).getProgressionUnitBars();

        assertThat(pBars.get(0).getCompositionTimeSlot().getHatPatternId()).isEqualTo(1);
        assertThat(pBars.get(1).getCompositionTimeSlot().getHatPatternId()).isEqualTo(2);
        assertThat(pBars.get(2).getCompositionTimeSlot().getHatPatternId()).isEqualTo(1);
        assertThat(pBars.get(3).getCompositionTimeSlot().getHatPatternId()).isEqualTo(2);
    }
}