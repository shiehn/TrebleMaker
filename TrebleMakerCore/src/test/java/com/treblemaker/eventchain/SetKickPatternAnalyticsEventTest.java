package com.treblemaker.eventchain;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.kick.KickPattern;
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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class SetKickPatternAnalyticsEventTest extends TestCase {

    QueueState queueState;
    SetKickPatternAnalyticsEvent setKickPatternAnalyticsEvent;

    @Before
    public void setup(){
        setKickPatternAnalyticsEvent = new SetKickPatternAnalyticsEvent();

        KickPattern kickPatternOne = new KickPattern();
        kickPatternOne.setId(1);

        KickPattern kickPatternTwo = new KickPattern();
        kickPatternTwo.setId(2);

        ProgressionUnitBar barOne = new ProgressionUnitBar();
        barOne.setKickPattern(kickPatternOne);

        ProgressionUnitBar barTwo = new ProgressionUnitBar();
        barTwo.setKickPattern(kickPatternTwo);

        ProgressionUnitBar barThree = new ProgressionUnitBar();
        barThree.setKickPattern(kickPatternOne);

        ProgressionUnitBar barFour = new ProgressionUnitBar();
        barFour.setKickPattern(kickPatternTwo);

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
    public void shouldSetKickPatternInCompositionTimeSlot(){

        QueueState result = setKickPatternAnalyticsEvent.set(queueState);

        List<ProgressionUnitBar> pBars = result.getStructure().get(0).getProgressionUnitBars();

        assertThat(pBars.get(0).getCompositionTimeSlot().getKickPatternId()).isEqualTo(1);
        assertThat(pBars.get(1).getCompositionTimeSlot().getKickPatternId()).isEqualTo(2);
        assertThat(pBars.get(2).getCompositionTimeSlot().getKickPatternId()).isEqualTo(1);
        assertThat(pBars.get(3).getCompositionTimeSlot().getKickPatternId()).isEqualTo(2);
    }
}