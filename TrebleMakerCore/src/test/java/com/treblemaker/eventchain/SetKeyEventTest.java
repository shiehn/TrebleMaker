package com.treblemaker.eventchain;

import com.treblemaker.model.HiveChord;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static com.treblemaker.model.progressions.ProgressionUnit.BarCount.FOUR;
import static org.junit.Assert.*;

public class SetKeyEventTest {

    private QueueState queueState;

    @Before
    public void setup(){
        ProgressionUnit pUnitOne = new ProgressionUnit();
        pUnitOne.initBars(FOUR.getValue());
        pUnitOne.getProgressionUnitBars().get(0).setChord(new HiveChord("Cmaj"));
        pUnitOne.getProgressionUnitBars().get(1).setChord(new HiveChord("Dmin"));
        pUnitOne.getProgressionUnitBars().get(2).setChord(new HiveChord("Dmin"));
        pUnitOne.getProgressionUnitBars().get(3).setChord(new HiveChord("Emin"));

        ProgressionUnit pUnitTwo = new ProgressionUnit();
        pUnitTwo.initBars(FOUR.getValue());
        pUnitTwo.getProgressionUnitBars().get(0).setChord(new HiveChord("Amin"));
        pUnitTwo.getProgressionUnitBars().get(1).setChord(new HiveChord("Bmin"));
        pUnitTwo.getProgressionUnitBars().get(2).setChord(new HiveChord("Bbmin"));
        pUnitTwo.getProgressionUnitBars().get(3).setChord(new HiveChord("Gmaj"));

        QueueItem queueItem = new QueueItem();
        queueItem.setBpm(77);

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(Arrays.asList(pUnitOne, pUnitTwo));

        queueItem.setProgression(progressionDTO);
        queueState = new QueueState();
        queueState.setQueueItem(queueItem);
    }

    @Test
    public void shouldSetCorrectKey(){

        SetKeyEvent setKeyEvent = new SetKeyEvent();

        queueState = setKeyEvent.set(queueState);
        assert(queueState.getStructure().get(0).getKey()).equalsIgnoreCase("c");
        assert(queueState.getStructure().get(1).getKey()).equalsIgnoreCase("g");
    }
}