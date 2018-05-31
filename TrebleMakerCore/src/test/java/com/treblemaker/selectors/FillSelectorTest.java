package com.treblemaker.selectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.SourceData;
import com.treblemaker.model.hitsandfills.Fill;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.selectors.interfaces.IFillSelector;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class FillSelectorTest extends TestCase {

    QueueState queueState = null;

    @Autowired
    private IFillSelector fillSelector;

    private List<Fill> generateFills() {

        Fill fillOne = new Fill();
        fillOne.setBarCount(2);

        Fill fillTwo = new Fill();
        fillTwo.setBarCount(2);

        Fill fillThree = new Fill();
        fillThree.setBarCount(2);

        Fill fillFour = new Fill();
        fillFour.setBarCount(1);

        return Arrays.asList(fillOne, fillTwo, fillThree, fillFour);
    }

    @Before
    public void setUp() throws JsonProcessingException {

        ProgressionUnit pOne = new ProgressionUnit();
        pOne.setType(ProgressionUnit.ProgressionType.VERSE);
        pOne.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar()));
        pOne.getProgressionUnitBars().get(2).setFillOptions(generateFills());

        ProgressionUnit pTwo = new ProgressionUnit();
        pTwo.setType(ProgressionUnit.ProgressionType.CHORUS);
        pTwo.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar()));
        pTwo.getProgressionUnitBars().get(2).setFillOptions(generateFills());

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(Arrays.asList(pOne, pTwo));

        QueueItem queueItem = new QueueItem();
        queueItem.setProgression(progressionDTO);

        SourceData sourceData = new SourceData();
        sourceData.setFills(generateFills());

        queueState = new QueueState();
        queueState.setQueueItem(queueItem);
        queueState.setDataSource(sourceData);
    }

    @Test
    public void shouldSelectOneFill() {

        fillSelector.selectFills(queueState);

        for (ProgressionUnit pUnit : queueState.getStructure()) {
            assertThat(pUnit.getProgressionUnitBars().get(0).getFill()).isNull();
            assertThat(pUnit.getProgressionUnitBars().get(2).getFill()).isNotNull();
        }
    }
}