package com.treblemaker.eventchain;

import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SetChordProgressionEventTest {

    QueueState queueState;
    SetChordProgressionEvent chordProgressionEvent;

    @Before
    public void setup(){
        QueueItem queueItem = new QueueItem();

        ProgressionUnit progressionUnitA = new ProgressionUnit();
        progressionUnitA.setType(ProgressionUnit.ProgressionType.VERSE);
        progressionUnitA.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(),
                new ProgressionUnitBar(),
                new ProgressionUnitBar(),
                new ProgressionUnitBar()));

        ProgressionUnit progressionUnitB = new ProgressionUnit();
        progressionUnitB.setType(ProgressionUnit.ProgressionType.CHORUS);
        progressionUnitB.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(),
                new ProgressionUnitBar(),
                new ProgressionUnitBar(),
                new ProgressionUnitBar()));

        ProgressionUnit progressionUnitC = new ProgressionUnit();
        progressionUnitC.setType(ProgressionUnit.ProgressionType.BRIDGE);
        progressionUnitC.setProgressionUnitBars(Arrays.asList(new ProgressionUnitBar(),
                new ProgressionUnitBar(),
                new ProgressionUnitBar(),
                new ProgressionUnitBar()));

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(Arrays.asList(progressionUnitA,progressionUnitB,progressionUnitC));

        queueItem.setProgression(progressionDTO);

        queueState = new QueueState();
        queueState.setQueueItem(queueItem);
        chordProgressionEvent = new SetChordProgressionEvent();
    }

    @Ignore
    @Test
    public void shouldSetAllChords(){

        queueState = chordProgressionEvent.set(queueState);

        assertThat(queueState.getStructure().size()).isGreaterThan(0);

        for (ProgressionUnit progressionUnit : queueState.getStructure()) {
            assertThat(progressionUnit.getProgressionUnitBars().size()).isGreaterThan(0);

            for (ProgressionUnitBar progressionUnitBar: progressionUnit.getProgressionUnitBars()) {
                assertThat(progressionUnitBar.getChord()).isNotNull();
                assertThat(progressionUnitBar.getChord().getRawChordName()).isNotNull();
                assertThat(progressionUnitBar.getChord().getRawChordName()).isNotEmpty();
            }
        }
    }
}