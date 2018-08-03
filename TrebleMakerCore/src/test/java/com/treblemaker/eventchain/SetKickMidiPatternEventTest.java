package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.kick.KickPattern;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import org.jfugue.pattern.Pattern;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static com.treblemaker.model.progressions.ProgressionUnit.BarCount.FOUR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SetKickMidiPatternEventTest {

    private IEventChain setKickPatternEvent;

    private QueueState queueState;

    private final String KEY = "f#";

    @Before
    public void setup(){
        ProgressionUnit pUnitOne = new ProgressionUnit();
        pUnitOne.setKey(KEY);
        pUnitOne.initBars(FOUR.getValue());
        pUnitOne.getProgressionUnitBars().get(0).setKickPattern(getKickPatternOnOFF());
        pUnitOne.getProgressionUnitBars().get(1).setKickPattern(getKickPatternOnOFF());
        pUnitOne.getProgressionUnitBars().get(2).setKickPattern(getKickPatternOnOFF());
        pUnitOne.getProgressionUnitBars().get(3).setKickPattern(getKickPatternOnOFF());

        ProgressionUnit pUnitTwo = new ProgressionUnit();
        pUnitTwo.setKey(KEY);
        pUnitTwo.initBars(FOUR.getValue());
        pUnitTwo.getProgressionUnitBars().get(0).setKickPattern(getKickPatternOffOffOnOn());
        pUnitTwo.getProgressionUnitBars().get(1).setKickPattern(getKickPatternOffOffOnOn());
        pUnitTwo.getProgressionUnitBars().get(2).setKickPattern(getKickPatternOffOffOnOn());
        pUnitTwo.getProgressionUnitBars().get(3).setKickPattern(getKickPatternOffOffOnOn());

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
        setKickPatternEvent = new SetKickMidiPatternEvent();

        queueState = setKickPatternEvent.set(queueState);

        Pattern p1b1 = queueState.getQueueItem().getProgression().getStructure().get(0).getProgressionUnitBars().get(0).getKickMidiPattern();
        Pattern p2b1 = queueState.getQueueItem().getProgression().getStructure().get(1).getProgressionUnitBars().get(0).getKickMidiPattern();

        assertThat(p1b1.toString()).isEqualToIgnoringCase("T77 f#s rs f#s rs f#s rs f#s rs f#s rs f#s rs f#s rs f#s rs ");
        assertThat(p2b1.toString()).isEqualToIgnoringCase("T77 rs rs f#s f#s rs rs f#s f#s rs rs f#s f#s rs rs f#s f#s ");
    }

    public KickPattern getKickPatternOnOFF(){
        KickPattern kickPattern = new KickPattern();

        kickPattern.setOneOne(1);
        kickPattern.setOneTwo(0);
        kickPattern.setOneThree(1);
        kickPattern.setOneFour(0);

        kickPattern.setTwoOne(1);
        kickPattern.setTwoTwo(0);
        kickPattern.setTwoThree(1);
        kickPattern.setTwoFour(0);

        kickPattern.setThreeOne(1);
        kickPattern.setThreeTwo(0);
        kickPattern.setThreeThree(1);
        kickPattern.setThreeFour(0);

        kickPattern.setFourOne(1);
        kickPattern.setFourTwo(0);
        kickPattern.setFourThree(1);
        kickPattern.setFourFour(0);

        return kickPattern;
    }

    public KickPattern getKickPatternOffOffOnOn(){
        KickPattern kickPattern = new KickPattern();

        kickPattern.setOneOne(0);
        kickPattern.setOneTwo(0);
        kickPattern.setOneThree(1);
        kickPattern.setOneFour(1);

        kickPattern.setTwoOne(0);
        kickPattern.setTwoTwo(0);
        kickPattern.setTwoThree(1);
        kickPattern.setTwoFour(1);

        kickPattern.setThreeOne(0);
        kickPattern.setThreeTwo(0);
        kickPattern.setThreeThree(1);
        kickPattern.setThreeFour(1);

        kickPattern.setFourOne(0);
        kickPattern.setFourTwo(0);
        kickPattern.setFourThree(1);
        kickPattern.setFourFour(1);

        return kickPattern;
    }
}