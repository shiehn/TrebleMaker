package com.treblemaker.generators;

import com.treblemaker.model.kick.KickPattern;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueItem;
import org.jfugue.pattern.Pattern;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static com.treblemaker.model.progressions.ProgressionUnit.BarCount.FOUR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class KickMidiGeneratorTest {

    QueueItem queueItem;

    @Before
    public void setup(){
        ProgressionUnit pUnitOne = new ProgressionUnit();
        pUnitOne.initBars(FOUR.getValue());
        pUnitOne.getProgressionUnitBars().get(0).setKickPattern(getKickPatternOnOFF());
        pUnitOne.getProgressionUnitBars().get(1).setKickPattern(getKickPatternOnOFF());
        pUnitOne.getProgressionUnitBars().get(2).setKickPattern(getKickPatternOnOFF());
        pUnitOne.getProgressionUnitBars().get(3).setKickPattern(getKickPatternOnOFF());

        ProgressionUnit pUnitTwo = new ProgressionUnit();
        pUnitTwo.initBars(FOUR.getValue());
        pUnitTwo.getProgressionUnitBars().get(0).setKickPattern(getKickPatternOffOffOnOn());
        pUnitTwo.getProgressionUnitBars().get(1).setKickPattern(getKickPatternOffOffOnOn());
        pUnitTwo.getProgressionUnitBars().get(2).setKickPattern(getKickPatternOffOffOnOn());
        pUnitTwo.getProgressionUnitBars().get(3).setKickPattern(getKickPatternOffOffOnOn());

        queueItem = new QueueItem();
        queueItem.setBpm(77);

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(Arrays.asList(pUnitOne, pUnitTwo));

        queueItem.setProgression(progressionDTO);
    }

    @Test
    public void shouldSetMidiPattern(){
        KickMidiGenerator kickMidiGenerator = new KickMidiGenerator();

        queueItem = kickMidiGenerator.setMidi(queueItem);

        Pattern p1b1 = queueItem.getProgression().getStructure().get(0).getProgressionUnitBars().get(0).getKickMidiPattern();

        assertThat(p1b1.toString()).isEqualToIgnoringCase("T77 a4s rs a4s rs a4s rs a4s rs a4s rs a4s rs");
        assertThat(true).isFalse();
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