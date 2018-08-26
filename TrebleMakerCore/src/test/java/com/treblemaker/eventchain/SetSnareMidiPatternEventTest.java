package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.snare.SnarePattern;
import org.jfugue.pattern.Pattern;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static com.treblemaker.model.progressions.ProgressionUnit.BarCount.FOUR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SetSnareMidiPatternEventTest {

    private IEventChain setSnarePatternEvent;

    private QueueState queueState;

    private final String KEY = "f#";

    @Before
    public void setup(){
        ProgressionUnit pUnitOne = new ProgressionUnit();
        pUnitOne.setKey(KEY);
        pUnitOne.initBars(FOUR.getValue());
        pUnitOne.getProgressionUnitBars().get(0).setSnarePattern(getSnarePatternOnOFF());
        pUnitOne.getProgressionUnitBars().get(1).setSnarePattern(getSnarePatternOnOFF());
        pUnitOne.getProgressionUnitBars().get(2).setSnarePattern(getSnarePatternOnOFF());
        pUnitOne.getProgressionUnitBars().get(3).setSnarePattern(getSnarePatternOnOFF());

        ProgressionUnit pUnitTwo = new ProgressionUnit();
        pUnitTwo.setKey(KEY);
        pUnitTwo.initBars(FOUR.getValue());
        pUnitTwo.getProgressionUnitBars().get(0).setSnarePattern(getSnarePatternOffOffOnOn());
        pUnitTwo.getProgressionUnitBars().get(1).setSnarePattern(getSnarePatternOffOffOnOn());
        pUnitTwo.getProgressionUnitBars().get(2).setSnarePattern(getSnarePatternOffOffOnOn());
        pUnitTwo.getProgressionUnitBars().get(3).setSnarePattern(getSnarePatternOffOffOnOn());

        QueueItem queueItem = new QueueItem();
        queueItem.setBpm(77);

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(Arrays.asList(pUnitOne, pUnitTwo));

        queueItem.setProgression(progressionDTO);

        queueState = new QueueState();
        queueState.setQueueItem(queueItem);
    }

    @Test
    public void shouldSetMidiPattern(){
        setSnarePatternEvent = new SetSnareMidiPatternEvent();

        queueState = setSnarePatternEvent.set(queueState);

        Pattern p1b1 = queueState.getQueueItem().getProgression().getStructure().get(0).getProgressionUnitBars().get(0).getSnareMidiPattern();
        Pattern p2b1 = queueState.getQueueItem().getProgression().getStructure().get(1).getProgressionUnitBars().get(0).getSnareMidiPattern();

        assertThat(p1b1.toString()).isEqualToIgnoringCase("T77 f#s rs f#s rs f#s rs f#s rs f#s rs f#s rs f#s rs f#s rs ");
        assertThat(p2b1.toString()).isEqualToIgnoringCase("T77 rs rs f#s f#s rs rs f#s f#s rs rs f#s f#s rs rs f#s f#s ");
    }

    public SnarePattern getSnarePatternOnOFF(){
        SnarePattern snarePattern = new SnarePattern();

        snarePattern.setOneOne(1);
        snarePattern.setOneTwo(0);
        snarePattern.setOneThree(1);
        snarePattern.setOneFour(0);

        snarePattern.setTwoOne(1);
        snarePattern.setTwoTwo(0);
        snarePattern.setTwoThree(1);
        snarePattern.setTwoFour(0);

        snarePattern.setThreeOne(1);
        snarePattern.setThreeTwo(0);
        snarePattern.setThreeThree(1);
        snarePattern.setThreeFour(0);

        snarePattern.setFourOne(1);
        snarePattern.setFourTwo(0);
        snarePattern.setFourThree(1);
        snarePattern.setFourFour(0);

        return snarePattern;
    }

    public SnarePattern getSnarePatternOffOffOnOn(){
        SnarePattern snarePattern = new SnarePattern();

        snarePattern.setOneOne(0);
        snarePattern.setOneTwo(0);
        snarePattern.setOneThree(1);
        snarePattern.setOneFour(1);

        snarePattern.setTwoOne(0);
        snarePattern.setTwoTwo(0);
        snarePattern.setTwoThree(1);
        snarePattern.setTwoFour(1);

        snarePattern.setThreeOne(0);
        snarePattern.setThreeTwo(0);
        snarePattern.setThreeThree(1);
        snarePattern.setThreeFour(1);

        snarePattern.setFourOne(0);
        snarePattern.setFourTwo(0);
        snarePattern.setFourThree(1);
        snarePattern.setFourFour(1);

        return snarePattern;
    }
}