package com.treblemaker.selectors;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class HatPatternSelectorTest extends TestCase {

    @Autowired
    HatPatternSelector hatPatternSelector;

    Map<ProgressionUnit.ProgressionType, HatPattern> typeToSelectedPatten = new HashMap<>();
    QueueState queueState = new QueueState();

    HatPattern hatPatternOne;
    HatPattern hatPatternTwo;
    HatPattern hatPatternThree;
    @Before
    public void setUp() {

        //KICK SETUP ..
        hatPatternOne = new HatPattern();
        hatPatternOne.setId(1);
        hatPatternOne.setFromArray(new int[]{0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1});

        hatPatternTwo = new HatPattern();
        hatPatternTwo.setId(2);
        hatPatternTwo.setFromArray(new int[]{1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0});

        hatPatternThree = new HatPattern();
        hatPatternThree.setId(3);
        hatPatternThree.setFromArray(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});

        //STRUCTURE SETUP .
        typeToSelectedPatten.put(ProgressionUnit.ProgressionType.VERSE,  hatPatternOne);
        typeToSelectedPatten.put(ProgressionUnit.ProgressionType.BRIDGE, hatPatternTwo);
        typeToSelectedPatten.put(ProgressionUnit.ProgressionType.CHORUS, hatPatternThree);

        ProgressionUnit pUnitOne = new ProgressionUnit();
        pUnitOne.setType(ProgressionUnit.ProgressionType.CHORUS);
        pUnitOne.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar()));

        ProgressionUnit pUnitTwo = new ProgressionUnit();
        pUnitTwo.setType(ProgressionUnit.ProgressionType.BRIDGE);
        pUnitTwo.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar()));

        ProgressionUnit pUnitThree = new ProgressionUnit();
        pUnitThree.setType(ProgressionUnit.ProgressionType.VERSE);
        pUnitThree.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar()));

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(Arrays.asList(pUnitOne, pUnitTwo, pUnitThree));

        QueueItem queueItem = new QueueItem();
        queueItem.setProgression(progressionDTO);

        queueState.setQueueItem(queueItem);
    }

    @Test
    public void shouldSetSelectedPattern() {

        queueState = hatPatternSelector.setSelectedPatterns(typeToSelectedPatten, queueState);

        List<ProgressionUnit> structure = queueState.getStructure();

        assertThat(structure.get(0).getProgressionUnitBars().get(0).getHatPattern().getId()).isEqualTo(3);
        assertThat(structure.get(0).getProgressionUnitBars().get(1).getHatPattern().getId()).isEqualTo(3);
        assertThat(structure.get(0).getProgressionUnitBars().get(2).getHatPattern().getId()).isEqualTo(3);
        assertThat(structure.get(0).getProgressionUnitBars().get(3).getHatPattern().getId()).isEqualTo(3);

        assertThat(structure.get(1).getProgressionUnitBars().get(0).getHatPattern().getId()).isEqualTo(2);
        assertThat(structure.get(1).getProgressionUnitBars().get(1).getHatPattern().getId()).isEqualTo(2);
        assertThat(structure.get(1).getProgressionUnitBars().get(2).getHatPattern().getId()).isEqualTo(2);
        assertThat(structure.get(1).getProgressionUnitBars().get(3).getHatPattern().getId()).isEqualTo(2);

        assertThat(structure.get(2).getProgressionUnitBars().get(0).getHatPattern().getId()).isEqualTo(1);
        assertThat(structure.get(2).getProgressionUnitBars().get(1).getHatPattern().getId()).isEqualTo(1);
        assertThat(structure.get(2).getProgressionUnitBars().get(2).getHatPattern().getId()).isEqualTo(1);
        assertThat(structure.get(2).getProgressionUnitBars().get(3).getHatPattern().getId()).isEqualTo(1);
    }


}