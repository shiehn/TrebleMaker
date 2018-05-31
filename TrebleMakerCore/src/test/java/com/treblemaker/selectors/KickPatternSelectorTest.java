package com.treblemaker.selectors;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.kick.KickPattern;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnit.ProgressionType;
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

import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class KickPatternSelectorTest extends TestCase {

    @Autowired
    KickPatternSelector kickPatternSelector;

    Map<ProgressionType, KickPattern> typeToSelectedPatten = new HashMap<>();
    QueueState queueState = new QueueState();

    KickPattern kickPatternOne;
    KickPattern kickPatternTwo;
    KickPattern kickPatternThree;

    @Before
    public void setUp() {

        //KICK SETUP ..
        kickPatternOne = new KickPattern();
        kickPatternOne.setId(1);
        kickPatternOne.setFromArray(new int[]{0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1});

        kickPatternTwo = new KickPattern();
        kickPatternTwo.setId(2);
        kickPatternTwo.setFromArray(new int[]{1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0});

        kickPatternThree = new KickPattern();
        kickPatternThree.setId(3);
        kickPatternThree.setFromArray(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});

        //STRUCTURE SETUP .
        typeToSelectedPatten.put(ProgressionType.VERSE, kickPatternOne);
        typeToSelectedPatten.put(ProgressionType.BRIDGE, kickPatternTwo);
        typeToSelectedPatten.put(ProgressionType.CHORUS, kickPatternThree);

        ProgressionUnit pUnitOne = new ProgressionUnit();
        pUnitOne.setType(ProgressionType.CHORUS);
        pUnitOne.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar()));

        ProgressionUnit pUnitTwo = new ProgressionUnit();
        pUnitTwo.setType(ProgressionType.BRIDGE);
        pUnitTwo.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar()));

        ProgressionUnit pUnitThree = new ProgressionUnit();
        pUnitThree.setType(ProgressionType.VERSE);
        pUnitThree.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar(), new ProgressionUnitBar()));

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(Arrays.asList(pUnitOne, pUnitTwo, pUnitThree));

        QueueItem queueItem = new QueueItem();
        queueItem.setProgression(progressionDTO);

        queueState.setQueueItem(queueItem);
    }

    @Test
    public void shouldSetSelectedPattern() {

        queueState = kickPatternSelector.setSelectedPatterns(typeToSelectedPatten, queueState);

        List<ProgressionUnit> structure = queueState.getStructure();

        assertThat(structure.get(0).getProgressionUnitBars().get(0).getKickPattern().getId()).isEqualTo(3);
        assertThat(structure.get(0).getProgressionUnitBars().get(1).getKickPattern().getId()).isEqualTo(3);
        assertThat(structure.get(0).getProgressionUnitBars().get(2).getKickPattern().getId()).isEqualTo(3);
        assertThat(structure.get(0).getProgressionUnitBars().get(3).getKickPattern().getId()).isEqualTo(3);

        assertThat(structure.get(1).getProgressionUnitBars().get(0).getKickPattern().getId()).isEqualTo(2);
        assertThat(structure.get(1).getProgressionUnitBars().get(1).getKickPattern().getId()).isEqualTo(2);
        assertThat(structure.get(1).getProgressionUnitBars().get(2).getKickPattern().getId()).isEqualTo(2);
        assertThat(structure.get(1).getProgressionUnitBars().get(3).getKickPattern().getId()).isEqualTo(2);

        assertThat(structure.get(2).getProgressionUnitBars().get(0).getKickPattern().getId()).isEqualTo(1);
        assertThat(structure.get(2).getProgressionUnitBars().get(1).getKickPattern().getId()).isEqualTo(1);
        assertThat(structure.get(2).getProgressionUnitBars().get(2).getKickPattern().getId()).isEqualTo(1);
        assertThat(structure.get(2).getProgressionUnitBars().get(3).getKickPattern().getId()).isEqualTo(1);
    }


}