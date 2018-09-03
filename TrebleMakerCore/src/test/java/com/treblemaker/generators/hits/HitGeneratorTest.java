package com.treblemaker.generators.hits;

import com.fasterxml.jackson.core.JsonProcessingException;
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
public class HitGeneratorTest extends TestCase {
    @Autowired
    IHitGenerator hitGenerator;

    QueueState queueState;

    @Before
    public void setUp() throws JsonProcessingException {

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
        ProgressionUnitBar barTwo = new ProgressionUnitBar();
        barTwo.setChord(chordTwo);
        ProgressionUnitBar barThree = new ProgressionUnitBar();
        barThree.setChord(chordThree);

        ProgressionUnitBar barFour = new ProgressionUnitBar();
        barFour.setChord(chordThree);

        List<ProgressionUnitBar> progressionUnitBars = Arrays.asList(barOne,barTwo,barThree,barFour);

        ProgressionUnit progressionUnit = new ProgressionUnit();
        progressionUnit.setBarCount(4);

        progressionUnit.setProgressionUnitBars(progressionUnitBars);

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(Arrays.asList(progressionUnit));

        QueueItem queueItem = new QueueItem();
        queueItem.setProgression(progressionDTO);

        Hit optionOne = new Hit();
        optionOne.setId(1);
        optionOne.setCompatibleChord(chordOne);

        Hit optionTwo = new Hit();
        optionTwo.setId(2);
        optionTwo.setCompatibleChord(chordTwo);

        Hit optionThree = new Hit();
        optionThree.setId(3);

        SourceData dataSource = new SourceData();
        dataSource.setHits(Arrays.asList(optionOne, optionTwo, optionThree));

        queueState = new QueueState();
        queueState.setDataSource(dataSource);
        queueState.setQueueItem(queueItem);
    }

    @Test
    public void shouldSetHitOptionsWithCompatibleChords(){

        queueState = hitGenerator.setHitOptions(queueState);

        List<ProgressionUnitBar> bars = queueState.getQueueItem().getProgression().getStructure().get(0).getProgressionUnitBars();

        assertThat(bars.get(0).getHitOptions().size()).isEqualTo(2);
        assertThat(bars.get(1).getHitOptions().size()).isEqualTo(2);
        assertThat(bars.get(2).getHitOptions().size()).isEqualTo(1);
    }
}
