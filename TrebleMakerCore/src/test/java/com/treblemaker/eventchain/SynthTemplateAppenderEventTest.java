package com.treblemaker.eventchain;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
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

import static com.treblemaker.model.progressions.ProgressionUnit.BarCount.FOUR;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties", properties ={"num_of_generated_mixes=3","num_of_generated_mix_variations=3"})
public class SynthTemplateAppenderEventTest extends TestCase {

    @Autowired
    SynthTemplateAppenderEvent synthTemplateAppenderEvent;

    @Test
    public void shouldSetCorrectNumberOfSynthTemplates(){

        QueueState queueState = createTestQueue();

        synthTemplateAppenderEvent.set(queueState);

        queueState.getStructure().forEach(progressionUnit -> progressionUnit.getProgressionUnitBars().forEach(progressionUnitBar -> {
            assertThat(progressionUnitBar.getSynthTemplates()).hasSize(1);
        }));
    }

    private QueueState createTestQueue() {

        ProgressionUnit unit1 = new ProgressionUnit();
        unit1.initBars(FOUR.getValue());

        ProgressionUnitBar barA = new ProgressionUnitBar();
        ProgressionUnitBar barB =  new ProgressionUnitBar();
        ProgressionUnitBar barC = new ProgressionUnitBar();

        //List<ProgressionUnitBar> bars = new ArrayList<>();
        unit1.getProgressionUnitBars().set(0, barA);
        unit1.getProgressionUnitBars().set(1, barB);
        unit1.getProgressionUnitBars().set(2, barC);
        unit1.getProgressionUnitBars().set(3, barC);

        List<ProgressionUnit> units = new ArrayList<>();
        units.add(unit1);

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(units);

        QueueItem queueItem = new QueueItem();
        queueItem.setBpm(80);
        queueItem.setProgression(progressionDTO);

        QueueState queueState = new QueueState();
        queueState.setQueueItem(queueItem);

        return queueState;
    }
}