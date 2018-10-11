package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.hat.HatPattern;
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

public class SetHatMidiPatternEventTest {

    private IEventChain setHatPatternEvent;

    private QueueState queueState;

    private final String KEY = "f#";

    @Before
    public void setup(){
        ProgressionUnit pUnitOne = new ProgressionUnit();
        pUnitOne.setKey(KEY);
        pUnitOne.initBars(FOUR.getValue());
        pUnitOne.getProgressionUnitBars().get(0).setHatPattern(getHatPatternOnOFF());
        pUnitOne.getProgressionUnitBars().get(1).setHatPattern(getHatPatternOnOFF());
        pUnitOne.getProgressionUnitBars().get(2).setHatPattern(getHatPatternOnOFF());
        pUnitOne.getProgressionUnitBars().get(3).setHatPattern(getHatPatternOnOFF());

        ProgressionUnit pUnitTwo = new ProgressionUnit();
        pUnitTwo.setKey(KEY);
        pUnitTwo.initBars(FOUR.getValue());
        pUnitTwo.getProgressionUnitBars().get(0).setHatPattern(getHatPatternOffOffOnOn());
        pUnitTwo.getProgressionUnitBars().get(1).setHatPattern(getHatPatternOffOffOnOn());
        pUnitTwo.getProgressionUnitBars().get(2).setHatPattern(getHatPatternOffOffOnOn());
        pUnitTwo.getProgressionUnitBars().get(3).setHatPattern(getHatPatternOffOffOnOn());

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
        setHatPatternEvent = new SetHatMidiPatternEvent();

        queueState = setHatPatternEvent.set(queueState);

        Pattern p1b1 = queueState.getQueueItem().getProgression().getStructure().get(0).getProgressionUnitBars().get(0).getHatMidiPattern();
        Pattern p2b1 = queueState.getQueueItem().getProgression().getStructure().get(1).getProgressionUnitBars().get(0).getHatMidiPattern();

        assertThat(p1b1.toString()).isEqualToIgnoringCase("T77 f#5s rs f#5s rs f#5s rs f#5s rs f#5s rs f#5s rs f#5s rs f#5s rs ");
        assertThat(p2b1.toString()).isEqualToIgnoringCase("T77 rs rs f#5s f#5s rs rs f#5s f#5s rs rs f#5s f#5s rs rs f#5s f#5s ");
    }

    public HatPattern getHatPatternOnOFF(){
        HatPattern HatPattern = new HatPattern();

        HatPattern.setOneOne(1);
        HatPattern.setOneTwo(0);
        HatPattern.setOneThree(1);
        HatPattern.setOneFour(0);

        HatPattern.setTwoOne(1);
        HatPattern.setTwoTwo(0);
        HatPattern.setTwoThree(1);
        HatPattern.setTwoFour(0);

        HatPattern.setThreeOne(1);
        HatPattern.setThreeTwo(0);
        HatPattern.setThreeThree(1);
        HatPattern.setThreeFour(0);

        HatPattern.setFourOne(1);
        HatPattern.setFourTwo(0);
        HatPattern.setFourThree(1);
        HatPattern.setFourFour(0);

        return HatPattern;
    }

    public HatPattern getHatPatternOffOffOnOn(){
        HatPattern HatPattern = new HatPattern();

        HatPattern.setOneOne(0);
        HatPattern.setOneTwo(0);
        HatPattern.setOneThree(1);
        HatPattern.setOneFour(1);

        HatPattern.setTwoOne(0);
        HatPattern.setTwoTwo(0);
        HatPattern.setTwoThree(1);
        HatPattern.setTwoFour(1);

        HatPattern.setThreeOne(0);
        HatPattern.setThreeTwo(0);
        HatPattern.setThreeThree(1);
        HatPattern.setThreeFour(1);

        HatPattern.setFourOne(0);
        HatPattern.setFourTwo(0);
        HatPattern.setFourThree(1);
        HatPattern.setFourFour(1);

        return HatPattern;
    }
}