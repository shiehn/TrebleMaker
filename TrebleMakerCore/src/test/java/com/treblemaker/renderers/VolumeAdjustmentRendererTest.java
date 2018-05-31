package com.treblemaker.renderers;

import com.treblemaker.SpringConfiguration;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class VolumeAdjustmentRendererTest extends TestCase {

    Map<String, Double> currentMeans;
    Map<String, Double> targetMeans;

    @Before
    public void setup(){

        //populateCurrentMeans
        currentMeans = new HashMap<>();
        currentMeans.put("One", 0.0);
        currentMeans.put("Two", 1.1);
        currentMeans.put("Three", -5.5);

        //populate targetMeans
        targetMeans = new HashMap<>();
        targetMeans.put("One", 5.5);
        targetMeans.put("Two", -15.4);
        targetMeans.put("Three", -4.5);
    }

    @Test
    public void calculateMixTargetOffsets_shouldGenerateCorrectOffsets(){

        VolumeAdjustmentRenderer volumeAdjustmentRenderer = new VolumeAdjustmentRenderer();
        Map<String, Double> offsets = volumeAdjustmentRenderer.calculateMixTargetOffsets(currentMeans, targetMeans);

        assertThat(offsets.size()).isEqualTo(3);
        assertThat(offsets.get("One")).isEqualTo(5.5);
        assertThat(offsets.get("Two")).isEqualTo(-16.5);
        assertThat(offsets.get("Three")).isEqualTo(1.0);
    }
}