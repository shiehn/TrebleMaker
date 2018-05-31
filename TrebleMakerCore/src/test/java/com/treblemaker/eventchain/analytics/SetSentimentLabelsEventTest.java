package com.treblemaker.eventchain.analytics;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.kick.KickPattern;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999", "num_of_generated_mixes=2", "num_of_generated_mix_variations=3"})
public class SetSentimentLabelsEventTest extends TestCase {

    QueueState queueState;

    @Autowired
    IEventChain setSentimentLabelsEvent;

    @Before
    public void setup(){

        KickPattern kickPatternOne = new KickPattern();
        kickPatternOne.setId(1);

        KickPattern kickPatternTwo = new KickPattern();
        kickPatternTwo.setId(2);

        ProgressionUnitBar barOne = new ProgressionUnitBar();
        barOne.setKickPattern(kickPatternOne);

        ProgressionUnitBar barTwo = new ProgressionUnitBar();
        barTwo.setKickPattern(kickPatternTwo);

        ProgressionUnitBar barThree = new ProgressionUnitBar();
        barThree.setKickPattern(kickPatternOne);

        ProgressionUnitBar barFour = new ProgressionUnitBar();
        barFour.setKickPattern(kickPatternTwo);

        ProgressionUnit progressionUnit = new ProgressionUnit();
        progressionUnit.setProgressionUnitBars(Arrays.asList(barOne,barTwo, barOne, barTwo));

        ProgressionDTO progressionDTO  = new ProgressionDTO();
        progressionDTO.setStructure(Arrays.asList(progressionUnit));

        QueueItem queueItem = new QueueItem();
        queueItem.setQueueItemId("GUID_ID");
        queueItem.setProgression(progressionDTO);

        queueState = new QueueState();
        queueState.setQueueItem(queueItem);
    }

    @Test
    public void shouldSetSentimentLabelsOnProgressionBars(){

        queueState = setSentimentLabelsEvent.set(queueState);

        for(ProgressionUnit progressionUnit : queueState.getStructure()){

            boolean skipBar = false;

            for(ProgressionUnitBar progressionUnitBar : progressionUnit.getProgressionUnitBars()){
                if(!skipBar){
                    assertThat(countSentimentMixVariations(progressionUnitBar.getSentimentLabels())).isEqualTo(6); //3 variations per mix
                    assertThat(allMixVariationsContainLabels(progressionUnitBar.getSentimentLabels())).isTrue();
                }else{
                    assertThat(progressionUnitBar.getSentimentLabels()).isNull();
                }

                skipBar = !skipBar;
            }
        }
    }

    private int countSentimentMixVariations(Map<String, List<String>> sentimentLabels){

        int count = 0;

        for (Map.Entry<String, List<String>> entry : sentimentLabels.entrySet())
        {
                count++;
        }

        return count;
    }

    private boolean allMixVariationsContainLabels(Map<String, List<String>> sentimentLabels){

        if(sentimentLabels == null){
            return false;
        }

        for (Map.Entry<String, List<String>> entry : sentimentLabels.entrySet())
        {
            if(entry.getValue() == null || entry.getValue().isEmpty()){
                    return false;
                }
        }

        return true;
    }
}