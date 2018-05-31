package com.treblemaker.selectors;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.generators.interfaces.IHitGenerator;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.SourceData;
import com.treblemaker.model.hitsandfills.Hit;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.selectors.interfaces.IHitSelector;
import com.treblemaker.weighters.enums.WeightClass;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class HitSelectorTest extends TestCase {

    QueueState queueState;

    @Autowired
    private IHitGenerator hitGenerator;

    @Autowired
    private IHitSelector hitSelector;

    @Before
    public void setUp() {

        List<ProgressionUnitBar> progressionUnitBars = createProgressionBars();

        ProgressionUnit progressionUnit = new ProgressionUnit();
        progressionUnit.setBarCount(4);

        progressionUnit.setProgressionUnitBars(progressionUnitBars);


        List<ProgressionUnitBar> progressionUnitBars2 = createProgressionBars();

        ProgressionUnit progressionUnit2 = new ProgressionUnit();
        progressionUnit2.setBarCount(4);

        progressionUnit2.setProgressionUnitBars(progressionUnitBars2);

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(Arrays.asList(progressionUnit, progressionUnit2));

        Hit optionOne = new Hit();
        optionOne.setId(1);
        optionOne.setVerticalWeight(WeightClass.BAD);

        SourceData sourceData = new SourceData();
        sourceData.setHits(Arrays.asList(optionOne));


        QueueItem queueItem = new QueueItem();
        queueItem.setProgression(progressionDTO);
        queueItem.setStationId(0);


        queueState = new QueueState();
        queueState.setQueueItem(queueItem);
        queueState.setDataSource(sourceData);
    }

    protected List<ProgressionUnitBar> createProgressionBars() {
        Hit optionOne = new Hit();
        optionOne.setId(1);
        optionOne.setVerticalWeight(WeightClass.BAD);

        Hit optionTwo = new Hit();
        optionTwo.setId(2);
        optionTwo.setVerticalWeight(WeightClass.GOOD);

        Hit optionThree = new Hit();
        optionThree.setId(3);
        optionThree.setVerticalWeight(WeightClass.OK);

        HiveChord chordOne = new HiveChord();
        chordOne.setId(1);
        chordOne.setChordName("amin7");

        HiveChord chordTwo = new HiveChord();
        chordTwo.setId(2);
        chordTwo.setChordName("cmaj");

        HiveChord chordThree = new HiveChord();
        chordThree.setId(3);
        chordThree.setChordName("fdom7");

        ProgressionUnitBar barOne = new ProgressionUnitBar();
        barOne.setChord(chordOne);
        barOne.setHitOptions(Arrays.asList(optionOne, optionTwo, optionThree));

        ProgressionUnitBar barTwo = new ProgressionUnitBar();
        barTwo.setChord(chordTwo);
        barTwo.setHitOptions(Arrays.asList(optionOne, optionTwo, optionThree));

        ProgressionUnitBar barThree = new ProgressionUnitBar();
        barThree.setChord(chordThree);
        barThree.setHitOptions(Arrays.asList(optionOne, optionTwo, optionThree));

        ProgressionUnitBar barFour = new ProgressionUnitBar();
        barFour.setChord(chordThree);
        barFour.setHitOptions(Arrays.asList(optionOne, optionTwo, optionThree));

        return Arrays.asList(barOne, barTwo, barThree, barFour);
    }

    @Test
    public void shouldSetHighestRated() {

        queueState = hitSelector.selectHits(queueState);

        Hit progressionUnitOneHit = queueState.getStructure().get(0).getProgressionUnitBars().get(0).getHit();
        Hit progressionUnitTwoHit = queueState.getStructure().get(1).getProgressionUnitBars().get(0).getHit();

        assertThat(progressionUnitOneHit).isNotNull();
        assertThat(progressionUnitOneHit.getId()).isEqualTo(1);

        assertThat(progressionUnitTwoHit).isNotNull();
        assertThat(progressionUnitTwoHit.getId()).isEqualTo(1);
    }
}
