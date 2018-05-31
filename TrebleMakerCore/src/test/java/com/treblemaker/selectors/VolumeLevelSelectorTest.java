package com.treblemaker.selectors;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.weighters.enums.WeightClass;
import com.treblemaker.weighters.volumeweighter.VolumeWeighterTaskResponse;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class VolumeLevelSelectorTest extends TestCase {

    @Test
    public void distributeWeights_shouldDistribteCorrectly(){

        VolumeLevelSelector volumeLevelSelector = new VolumeLevelSelector();

        VolumeWeighterTaskResponse volumeOne = new VolumeWeighterTaskResponse();
        volumeOne.setWeightClass(WeightClass.BAD);

        VolumeWeighterTaskResponse volumeTwo = new VolumeWeighterTaskResponse();
        volumeTwo.setWeightClass(WeightClass.OK);

        VolumeWeighterTaskResponse volumeThree = new VolumeWeighterTaskResponse();
        volumeThree.setWeightClass(WeightClass.GOOD);

        List<VolumeWeighterTaskResponse> weightedMixes = Arrays.asList(volumeOne,volumeTwo,volumeThree);

        List<Map<String, Double>> distributedMixes = volumeLevelSelector.distributeWeights(weightedMixes);

        assertThat(distributedMixes).hasSize(1+2+3);
    }
}
