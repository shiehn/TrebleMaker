package com.treblemaker.generators;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.HiveChord;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class StationTrackGeneratorTest extends TestCase {

    @Autowired
    StationTrackGenerator stationTrackGenerator;

    QueueState queueState;

    @Before
    public void setup(){

        HiveChord chordOne = new HiveChord();
        chordOne.setId(1);
        chordOne.setChordName("amin7");

        HiveChord chordTwo = new HiveChord();
        chordTwo.setId(2);
        chordTwo.setChordName("cmaj");

        HiveChord chordThree = new HiveChord();
        chordThree.setId(3);
        chordThree.setChordName("fdom7");

        HiveChord chordFour = new HiveChord();
        chordFour.setId(3);
        chordFour.setChordName("gdim");

        ProgressionUnitBar barOne = new ProgressionUnitBar();
        barOne.setChord(chordOne);
        ProgressionUnitBar barTwo = new ProgressionUnitBar();
        barTwo.setChord(chordTwo);
        ProgressionUnitBar barThree = new ProgressionUnitBar();
        barThree.setChord(chordThree);
        ProgressionUnitBar barFour = new ProgressionUnitBar();
        barFour.setChord(chordFour);

        List<ProgressionUnitBar> progressionUnitBars = Arrays.asList(barOne,barTwo,barThree,barFour);

        ProgressionUnit progressionUnit = new ProgressionUnit();
        progressionUnit.setBarCount(4);

        progressionUnit.setProgressionUnitBars(progressionUnitBars);

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(Arrays.asList(progressionUnit, progressionUnit));

        QueueItem queueItem = new QueueItem();
        queueItem.setProgression(progressionDTO);

        queueItem = new QueueItem();
        queueItem.setBpm(80);
        queueItem.setProgression(progressionDTO);

        queueState = new QueueState();
        queueState.setQueueItem(queueItem);
    }

    @Test
    public void shouldExtractCorrectTrackName(){

        String expectedPrefix = "amin7_cmaj_fdom7_gdim_80_null_";
        String expectedSuffix = ".wav";

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String expectedDate = dateFormat.format(date).replace("/", "-");

        expectedPrefix = expectedPrefix + expectedDate + "_";

        String trackName = stationTrackGenerator.extractTrackName(queueState);

        assertThat(trackName).contains(expectedPrefix);
        assertThat(trackName).contains(expectedSuffix);
    }
}