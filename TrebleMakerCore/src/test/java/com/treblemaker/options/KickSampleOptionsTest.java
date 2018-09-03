package com.treblemaker.options;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.SourceData;
import com.treblemaker.model.kick.KickSample;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class KickSampleOptionsTest extends TestCase {

    @Autowired
    KickSampleOptions kickSampleOptions;

    @Test
    public void shouldFilterPatternsByStation(){

        KickSample kickSampleA = new KickSample();
        kickSampleA.setId(1);
        kickSampleA.setStationId("1");

        KickSample kickSampleB = new KickSample();
        kickSampleB.setId(2);
        kickSampleB.setStationId("2");

        SourceData sourceData = new SourceData();
        sourceData.setKickSamples(Arrays.asList(kickSampleA, kickSampleB));

        List<KickSample> filteredSamples = this.kickSampleOptions.getKickSampleOptions(2, sourceData);

        assertThat(filteredSamples).hasSize(1);
        assertThat(filteredSamples.get(0).getId()).isEqualTo(2);
    }
}