//package com.treblemaker.weighters.fx;
//
//import com.treblemaker.SpringConfiguration;
//import com.treblemaker.model.fx.FXArpeggioDelay;
//import com.treblemaker.model.fx.FXArpeggioWithRating;
//import com.treblemaker.model.progressions.ProgressionDTO;
//import com.treblemaker.model.progressions.ProgressionUnit;
//import com.treblemaker.model.progressions.ProgressionUnitBar;
//import com.treblemaker.model.queues.QueueItem;
//import com.treblemaker.model.queues.QueueState;
//import com.treblemaker.weighters.interfaces.ISynthFXWeighter;
//import junit.framework.TestCase;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.IntegrationTest;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.*;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = SpringConfiguration.class)
//@IntegrationTest({"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
//public class SynthFXWeighterTest extends TestCase {
//
//    @Autowired
//    private ISynthFXWeighter synthFXWeighter;
//
//    private QueueState queueState;
//
//    @Before
//    public void setup(){
//
//        queueState = new QueueState();
//
//        List<FXArpeggioDelay> synthFXOptions = new ArrayList<>();
//        synthFXOptions.add(new FXArpeggioDelay());
//        synthFXOptions.add(new FXArpeggioDelay());
//        synthFXOptions.add(new FXArpeggioDelay());
//        synthFXOptions.add(new FXArpeggioDelay());
//
//        ProgressionUnitBar barA = new ProgressionUnitBar();
//        barA.setHiSynthId(111);
//        ProgressionUnitBar barB = new ProgressionUnitBar();
//        barB.setHiSynthId(222);
//
//        ProgressionUnit progressionUnitA = new ProgressionUnit();
//        progressionUnitA.setProgressionUnitBars(Arrays.asList(barA,barA,barB,barA));
//
//        ProgressionUnit progressionUnitB = new ProgressionUnit();
//        progressionUnitB.setProgressionUnitBars(Arrays.asList(barB,barA,barB,barA));
//
//        ProgressionUnit progressionUnitC = new ProgressionUnit();
//        progressionUnitC.setProgressionUnitBars(Arrays.asList(barB,barB,barB,barA));
//
//        ProgressionUnit progressionUnitD = new ProgressionUnit();
//        progressionUnitD.setProgressionUnitBars(Arrays.asList(barB,barB,barA,barA));
//
//        List<ProgressionUnit> progressionUnits = Arrays.asList(progressionUnitA, progressionUnitB, progressionUnitC, progressionUnitD);
//
//        ProgressionDTO progression = new ProgressionDTO();
//        progression.setStructure(progressionUnits);
//
//        QueueItem queueItem = new QueueItem();
//        queueItem.setProgression(progression);
//
//        queueState.setQueueItem(queueItem);
//    }
//
//    @Test
//    public void shouldSetSynthFXWeights(){
//
//
//        FXArpeggioWithRating one = new FXArpeggioWithRating();
//        FXArpeggioWithRating two = new FXArpeggioWithRating();
//        FXArpeggioWithRating three = new FXArpeggioWithRating();
//        FXArpeggioWithRating four = new FXArpeggioWithRating();
//        FXArpeggioWithRating five = new FXArpeggioWithRating();
//        FXArpeggioWithRating six = new FXArpeggioWithRating();
//
//        List<FXArpeggioWithRating> listOne = Arrays.asList(one,two,three);
//        List<FXArpeggioWithRating> listTwo = Arrays.asList(four,five,six);
//
//        Map<Integer, List<FXArpeggioWithRating>> synthIdToFXOptions = new HashMap<>();
//        synthIdToFXOptions.put(111, listOne);
//        synthIdToFXOptions.put(222, listTwo);
//
//        for (Map.Entry<Integer, List<FXArpeggioWithRating>> entry : synthIdToFXOptions.entrySet())
//        {
//            for(FXArpeggioWithRating value : entry.getValue()){
//                assertThat(value.getTotalWeight()).isEqualTo(0);
//            }
//        }
//
//        Map<Integer, List<FXArpeggioWithRating>> map = synthFXWeighter.setWeights(queueState, synthIdToFXOptions);
//
//        for (Map.Entry<Integer, List<FXArpeggioWithRating>> entry : map.entrySet())
//        {
//            for(FXArpeggioWithRating value : entry.getValue()){
//                assertThat(value.getTotalWeight()).isGreaterThan(0);
//            }
//        }
//    }
//}