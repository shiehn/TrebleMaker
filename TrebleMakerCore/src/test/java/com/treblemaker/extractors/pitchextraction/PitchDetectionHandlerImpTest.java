package com.treblemaker.extractors.pitchextraction;

import com.treblemaker.SpringConfiguration;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class PitchDetectionHandlerImpTest extends TestCase {

    PitchDetectionHandlerImp pitchDetectionHandlerImp;

    @Before
    public void setup(){
        pitchDetectionHandlerImp = new PitchDetectionHandlerImp();
    }

    @Test
    public void shouldCreateCorrectWindowSize(){

        List<List<String>> windowMarixOne = pitchDetectionHandlerImp.getWindowMarix(1);
        List<List<String>> windowMarixTwo = pitchDetectionHandlerImp.getWindowMarix(2);
        List<List<String>> windowMarixThree = pitchDetectionHandlerImp.getWindowMarix(4);

        assertThat(windowMarixOne.size()).isEqualTo(16);
        assertThat(windowMarixTwo.size()).isEqualTo(32);
        assertThat(windowMarixThree.size()).isEqualTo(64);
    }
}