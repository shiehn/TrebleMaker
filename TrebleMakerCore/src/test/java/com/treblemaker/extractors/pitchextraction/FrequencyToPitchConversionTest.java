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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class FrequencyToPitchConversionTest extends TestCase {

    FrequencyToPitchConversion frequencyToPitchConversion;

    @Before
    public void setup(){
        frequencyToPitchConversion = new FrequencyToPitchConversion();
    }

    @Test
    public void shouldReturnCNote(){

        String pitchOne = frequencyToPitchConversion.getPitchName(16.35f);
        String pitchTwo = frequencyToPitchConversion.getPitchName(31.96f);
        String pitchThree = frequencyToPitchConversion.getPitchName(4185.01f);

        assertThat(pitchOne).isEqualTo("c");
        assertThat(pitchTwo).isEqualTo("c");
        assertThat(pitchThree).isEqualTo("c");
    }

    @Test
    public void shouldReturnBNote(){

        String pitchOne = frequencyToPitchConversion.getPitchName(31f);
        String pitchTwo = frequencyToPitchConversion.getPitchName(122f);
        String pitchThree = frequencyToPitchConversion.getPitchName(7901.13f);

        assertThat(pitchOne).isEqualTo("b");
        assertThat(pitchTwo).isEqualTo("b");
        assertThat(pitchThree).isEqualTo("b");
    }

    @Test
    public void shouldReturnFSharpNote(){

        String pitchOne = frequencyToPitchConversion.getPitchName(23f);
        String pitchTwo = frequencyToPitchConversion.getPitchName(186.1f);
        String pitchThree = frequencyToPitchConversion.getPitchName(5920f);

        assertThat(pitchOne).isEqualTo("fs");
        assertThat(pitchTwo).isEqualTo("fs");
        assertThat(pitchThree).isEqualTo("fs");
    }
}