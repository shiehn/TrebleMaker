package com.treblemaker.generators.fills;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.treblemaker.SpringConfiguration;
import com.treblemaker.generators.interfaces.IFillsGenerator;
import com.treblemaker.model.SourceData;
import com.treblemaker.model.hitsandfills.Fill;
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
public class FillsGeneratorTest extends TestCase {
    @Autowired
    private IFillsGenerator fillsGenerator;

    QueueState queueState = null;

    @Before
    public void setUp() throws JsonProcessingException {

        ProgressionUnit pOne = new ProgressionUnit();
        pOne.setType(ProgressionUnit.ProgressionType.VERSE);
        pOne.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar()));

        ProgressionUnit pTwo = new ProgressionUnit();
        pTwo.setType(ProgressionUnit.ProgressionType.CHORUS);
        pTwo.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar()));

        ProgressionUnit pThree = new ProgressionUnit();
        pThree.setType(ProgressionUnit.ProgressionType.VERSE);
        pThree.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar()));

        ProgressionUnit pFour = new ProgressionUnit();
        pFour.setType(ProgressionUnit.ProgressionType.VERSE);
        pFour.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar()));

        ProgressionUnit pFive = new ProgressionUnit();
        pFive.setType(ProgressionUnit.ProgressionType.VERSE);
        pFive.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar()));

        ProgressionUnit pSix = new ProgressionUnit();
        pSix.setType(ProgressionUnit.ProgressionType.BRIDGE);
        pSix.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar()));

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(Arrays.asList(pOne, pTwo, pThree, pFour, pFive, pSix));

        QueueItem queueItem = new QueueItem();
        queueItem.setProgression(progressionDTO);

        SourceData sourceData = new SourceData();
        sourceData.setFills(generateFills());

        queueState = new QueueState();
        queueState.setQueueItem(queueItem);
        queueState.setDataSource(sourceData);
    }

    private List<Fill> generateFills() {

        Fill fillOne = new Fill();
        fillOne.setBarCount(2);

        Fill fillTwo = new Fill();
        fillTwo.setBarCount(2);

        Fill fillThree = new Fill();
        fillThree.setBarCount(2);

        Fill fillFour = new Fill();
        fillFour.setBarCount(1);

        return Arrays.asList(fillOne,fillTwo,fillThree,fillFour);
    }

    @Test
    public void shouldSetThirdBarWithFillOptions() {

        QueueState state = fillsGenerator.setFillsOptions(queueState);
        List<ProgressionUnit> progressionUnits = state.getStructure();

        assertThat(progressionUnits.get(0).getProgressionUnitBars().get(2).getFillOptions().size()).isEqualTo(3);
        assertThat(progressionUnits.get(1).getProgressionUnitBars().get(2).getFillOptions().size()).isEqualTo(3);
        assertThat(progressionUnits.get(2).getProgressionUnitBars().get(2).getFillOptions().size()).isEqualTo(0);
        assertThat(progressionUnits.get(3).getProgressionUnitBars().get(2).getFillOptions().size()).isEqualTo(0);
        assertThat(progressionUnits.get(4).getProgressionUnitBars().get(2).getFillOptions().size()).isEqualTo(3);
        assertThat(progressionUnits.get(5).getProgressionUnitBars().get(2).getFillOptions().size()).isEqualTo(3);
    }
}