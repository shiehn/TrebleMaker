package com.treblemaker.selectors;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class SnarePatternSelectorTest extends TestCase {

    @Autowired
    SnarePatternSelector snarePatternSelector;

    Map<ProgressionUnit.ProgressionType, SnarePattern> typeToSelectedPatten = new HashMap<>();
    QueueState queueState = new QueueState();

    SnarePattern snarePatternOne;
    SnarePattern snarePatternTwo;
    SnarePattern snarePatternThree;
    @Before
    public void setUp() {

        //KICK SETUP ..
        snarePatternOne = new SnarePattern();
        snarePatternOne.setId(1);
        snarePatternOne.setFromArray(new int[]{0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1});

        snarePatternTwo = new SnarePattern();
        snarePatternTwo.setId(2);
        snarePatternTwo.setFromArray(new int[]{1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0});

        snarePatternThree = new SnarePattern();
        snarePatternThree.setId(3);
        snarePatternThree.setFromArray(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});

        //STRUCTURE SETUP .
        typeToSelectedPatten.put(ProgressionUnit.ProgressionType.VERSE,  snarePatternOne);
        typeToSelectedPatten.put(ProgressionUnit.ProgressionType.BRIDGE, snarePatternTwo);
        typeToSelectedPatten.put(ProgressionUnit.ProgressionType.CHORUS, snarePatternThree);

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

        queueState = snarePatternSelector.setSelectedPatterns(typeToSelectedPatten, queueState);

        List<ProgressionUnit> structure = queueState.getStructure();

        assertThat(structure.get(0).getProgressionUnitBars().get(0).getSnarePattern().getId()).isEqualTo(3);
        assertThat(structure.get(0).getProgressionUnitBars().get(1).getSnarePattern().getId()).isEqualTo(3);
        assertThat(structure.get(0).getProgressionUnitBars().get(2).getSnarePattern().getId()).isEqualTo(3);
        assertThat(structure.get(0).getProgressionUnitBars().get(3).getSnarePattern().getId()).isEqualTo(3);

        assertThat(structure.get(1).getProgressionUnitBars().get(0).getSnarePattern().getId()).isEqualTo(2);
        assertThat(structure.get(1).getProgressionUnitBars().get(1).getSnarePattern().getId()).isEqualTo(2);
        assertThat(structure.get(1).getProgressionUnitBars().get(2).getSnarePattern().getId()).isEqualTo(2);
        assertThat(structure.get(1).getProgressionUnitBars().get(3).getSnarePattern().getId()).isEqualTo(2);

        assertThat(structure.get(2).getProgressionUnitBars().get(0).getSnarePattern().getId()).isEqualTo(1);
        assertThat(structure.get(2).getProgressionUnitBars().get(1).getSnarePattern().getId()).isEqualTo(1);
        assertThat(structure.get(2).getProgressionUnitBars().get(2).getSnarePattern().getId()).isEqualTo(1);
        assertThat(structure.get(2).getProgressionUnitBars().get(3).getSnarePattern().getId()).isEqualTo(1);
    }


}