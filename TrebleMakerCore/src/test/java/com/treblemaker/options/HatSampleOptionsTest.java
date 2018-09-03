package com.treblemaker.options;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.SourceData;
import com.treblemaker.model.hat.HatSample;
import junit.framework.TestCase;
import org.assertj.core.api.AssertionsForClassTypes;
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
public class HatSampleOptionsTest extends TestCase {

    @Autowired
    HatSampleOptions hatSampleOptions;

    @Test
    public void shouldFilterPatternsByStation(){

        HatSample hatSampleA = new HatSample();
        hatSampleA.setId(1);
        hatSampleA.setStationId("1");

        HatSample hatSampleB = new HatSample();
        hatSampleB.setId(2);
        hatSampleB.setStationId("2");

        SourceData sourceData = new SourceData();
        sourceData.setHatSamples(Arrays.asList(hatSampleA, hatSampleB));

        List<HatSample> filteredSamples = this.hatSampleOptions.getHatSampleOptions(2, sourceData);

        assertThat(filteredSamples).hasSize(1);
        AssertionsForClassTypes.assertThat(filteredSamples.get(0).getId()).isEqualTo(2);
    }
}