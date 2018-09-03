package com.treblemaker.eventchain.analytics;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.sentiment.SentimentComposition;
import com.treblemaker.model.sentiment.SentimentSection;
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

import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties", properties = {"num_of_generated_mixes=2", "num_of_generated_mix_variations=3"})
public class SentimentEventTest extends TestCase {

    @Autowired
    private SentimentEvent sentimentEvent;

    QueueState queueState;

    @Before
    public void setup() {

        Map<String, List<String>> bOneSentiment = new HashMap<>();
        bOneSentiment.put("11", Arrays.asList("1"));

        Map<String, List<String>> bTwoSentiment = new HashMap<>();
        bTwoSentiment.put("21", Arrays.asList("2"));
        bTwoSentiment.put("22", Arrays.asList("2", "2"));

        Map<String, List<String>> bThreeSentiment = new HashMap<>();
        bThreeSentiment.put("31", Arrays.asList("3"));
        bThreeSentiment.put("32", Arrays.asList("3", "3"));
        bThreeSentiment.put("33", Arrays.asList("3", "3", "3"));

        Map<String, List<String>> bFourSentiment = new HashMap<>();
        bFourSentiment.put("41", Arrays.asList("4"));
        bFourSentiment.put("42", Arrays.asList("4", "4"));
        bFourSentiment.put("43", Arrays.asList("4", "4", "4"));
        bFourSentiment.put("44", Arrays.asList("4", "4", "4", "4"));


        ProgressionUnitBar barOne = new ProgressionUnitBar();
        barOne.setSentimentLabels(bOneSentiment);

        ProgressionUnitBar barTwo = new ProgressionUnitBar();
        barTwo.setSentimentLabels(bTwoSentiment);

        ProgressionUnitBar barThree = new ProgressionUnitBar();
        barThree.setSentimentLabels(bThreeSentiment);

        ProgressionUnitBar barFour = new ProgressionUnitBar();
        barFour.setSentimentLabels(bFourSentiment);

        ProgressionUnit progressionUnit = new ProgressionUnit();
        progressionUnit.setProgressionUnitBars(Arrays.asList(barOne, barTwo, barThree, barFour));

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(Arrays.asList(progressionUnit));

        QueueItem queueItem = new QueueItem();
        queueItem.setProgression(progressionDTO);

        queueState = new QueueState();
        queueState.setQueueItem(queueItem);
    }

    @Test
    public void shouldCreateSentimentComposition() {

        SentimentComposition sentimentComposition = sentimentEvent.createSentimentComposition(queueState);
        sentimentComposition.setCompositionId("TEST_" + new Date());
        List<SentimentSection> sentimentSections = sentimentComposition.getSentimentSections().getSentimentSectionList();
        for (int i = 0; i < sentimentSections.size(); i++) {
            Map<String, List<String>> sentimentLabels = sentimentSections.get(i).getSentimentLabels();
            switch (i) {
                case 0:
                    assertThat(sentimentLabels.get("11")).hasSize(1);
                    break;
                case 1:
                    assertThat(sentimentLabels.get("31")).hasSize(1);
                    assertThat(sentimentLabels.get("32")).hasSize(2);
                    assertThat(sentimentLabels.get("33")).hasSize(3);
                    break;
                default:
                    fail("THERE SHOULD ONLY BE TWO SENTIMENT MAPS FOR THIS TEST!");
            }
        }
    }

    @Test
    public void shouldCreateSentimentSectionsHalfTheLengthOfTheNumberOfBars() {

        SentimentComposition sentimentComposition = sentimentEvent.createSentimentComposition(queueState);
        List<SentimentSection> sentimentSections = sentimentComposition.getSentimentSections().getSentimentSectionList();

        assertThat(sentimentSections).hasSize(2);
    }
}