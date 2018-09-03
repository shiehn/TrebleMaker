package com.treblemaker.eventchain;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.snare.SnarePattern;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class SetSnarePatternAnalyticsEventTest extends TestCase {

    QueueState queueState;
    SetSnarePatternAnalyticsEvent setSnarePatternAnalyticsEvent;

    @Before
    public void setup(){
        setSnarePatternAnalyticsEvent = new SetSnarePatternAnalyticsEvent();

        SnarePattern snarePatternOne = new SnarePattern();
        snarePatternOne.setId(1);

        SnarePattern snarePatternTwo = new SnarePattern();
        snarePatternTwo.setId(2);

        ProgressionUnitBar barOne = new ProgressionUnitBar();
        barOne.setSnarePattern(snarePatternOne);

        ProgressionUnitBar barTwo = new ProgressionUnitBar();
        barTwo.setSnarePattern(snarePatternTwo);

        ProgressionUnitBar barThree = new ProgressionUnitBar();
        barThree.setSnarePattern(snarePatternOne);

        ProgressionUnitBar barFour = new ProgressionUnitBar();
        barFour.setSnarePattern(snarePatternTwo);

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
    public void shouldSetSnarePatternInCompositionTimeSlot(){

        QueueState result = setSnarePatternAnalyticsEvent.set(queueState);

        List<ProgressionUnitBar> pBars = result.getStructure().get(0).getProgressionUnitBars();

        assertThat(pBars.get(0).getCompositionTimeSlot().getSnarePatternId()).isEqualTo(1);
        assertThat(pBars.get(1).getCompositionTimeSlot().getSnarePatternId()).isEqualTo(2);
        assertThat(pBars.get(2).getCompositionTimeSlot().getSnarePatternId()).isEqualTo(1);
        assertThat(pBars.get(3).getCompositionTimeSlot().getSnarePatternId()).isEqualTo(2);
    }
}