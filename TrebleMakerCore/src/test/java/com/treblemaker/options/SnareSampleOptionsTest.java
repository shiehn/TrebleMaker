package com.treblemaker.options;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.SourceData;
import com.treblemaker.model.snare.SnareSample;
import junit.framework.TestCase;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class SnareSampleOptionsTest extends TestCase {

    @Autowired
    SnareSampleOptions snareSampleOptions;

    @Test
    public void shouldFilterPatternsByStation(){

        SnareSample snareSampleA = new SnareSample();
        snareSampleA.setId(1);
        snareSampleA.setStationId("1");

        SnareSample snareSampleB = new SnareSample();
        snareSampleB.setId(2);
        snareSampleB.setStationId("2");

        SourceData sourceData = new SourceData();
        sourceData.setSnareSamples(Arrays.asList(snareSampleA, snareSampleB));

        List<SnareSample> filteredSamples = this.snareSampleOptions.getSnareSampleOptions(2, sourceData);

        assertThat(filteredSamples).hasSize(1);
        AssertionsForClassTypes.assertThat(filteredSamples.get(0).getId()).isEqualTo(2);
    }
}