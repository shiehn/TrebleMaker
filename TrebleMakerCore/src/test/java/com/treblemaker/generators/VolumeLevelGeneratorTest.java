package com.treblemaker.generators;

import com.treblemaker.SpringConfiguration;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"return_queue_early_for_tests=true","queue_scheduled_interval=8999999","queue_scheduled_start_delay=8999999","spring.datasource.url=jdbc:mysql://localhost:3306/hivecomposedb","spring.datasource.username=root","spring.datasource.password=redrobes79D"})
public class VolumeLevelGeneratorTest extends TestCase {

    @Autowired
    private VolumeLevelGenerator volumeLevelGenerator;

    @Test
    public void generatePotentialMixes_shouldbeCorrectLength() {
        final List<Map<String, Double>> maps = volumeLevelGenerator.generatePotentialMixes();
        assertThat(maps.size()).isEqualTo(volumeLevelGenerator.NUM_OF_MIXES);
    }

    @Test
    public void generatePotentialMix_shouldHave17Keys() {
        final Map<String, Double> maps = volumeLevelGenerator.generatePotentialMix();
        assertThat(maps.size()).isEqualTo(17);
    }

    @Test
    public void randomNumber_shouldBeEqualToOrGreaterThanMin() {
        int numberOfTests = 100;
        for (int i = 0; i < numberOfTests; i++) {
            double randVolume = volumeLevelGenerator.generateRandomDecibleLevel(-9999, 5);
            assertThat(randVolume).isGreaterThanOrEqualTo(1);
            assertThat(randVolume).isLessThanOrEqualTo(15);
        }
    }

    @Test
    public void randomNumber_shouldBeLessThanOrEqualToMax() {
        int numberOfTests = 100;
        for (int i = 0; i < numberOfTests; i++) {
            double randVolume = volumeLevelGenerator.generateRandomDecibleLevel(1, 9999);
            assertThat(randVolume).isGreaterThanOrEqualTo(1);
            assertThat(randVolume).isLessThanOrEqualTo(15);
        }
    }

    @Test
    public void randomNumber_shouldEqualTarget(){
        double randVolume = volumeLevelGenerator.generateRandomDecibleLevel(5, 5);
        assertThat(randVolume).isEqualTo(5);
    }
}