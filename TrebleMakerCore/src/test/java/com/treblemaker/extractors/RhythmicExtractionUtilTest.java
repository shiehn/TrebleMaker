package com.treblemaker.extractors;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.dal.interfaces.IBeatLoopsDal;
import com.treblemaker.dal.interfaces.IHarmonicLoopsDal;
import com.treblemaker.utils.LoopUtils;
import com.treblemaker.utils.interfaces.IAudioUtils;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker", "com.treblemaker.dal.interfaces"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class RhythmicExtractionUtilTest extends TestCase {

    @Autowired
    AppConfigs appConfigs;

    RhythmicExtractionUtil rhythmApp;

    @Autowired
    private IBeatLoopsDal beatLoopsDal;

    @Autowired
    private IHarmonicLoopsDal harmonicLoopsDal;

    @Autowired
    private IAudioUtils audioUtils;

    @Before
    public void setup() {
        rhythmApp = new RhythmicExtractionUtil(beatLoopsDal, harmonicLoopsDal, appConfigs, audioUtils);
    }

    @Test
    public void divideBarIntoSixteenths() {

        Double barLength = 3.00;
        Double expected = barLength / 16;

        double secondsInSixteethNote = LoopUtils.getSecondsInSixteethNote(80);

        assertThat(secondsInSixteethNote).isEqualTo(expected);
    }

    @Test
    public void setFirstPosition() {

        int barCount = 1;
        Integer[] expected = new Integer[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        Double[] inputs = new Double[]{0.07};
        List<Integer[]> actual = rhythmApp.getOnsetIndexes(inputs, barCount, 3.0f);

        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0)).isEqualTo(expected);
    }

    @Test
    public void setFirstAndLastPosition() {

        int barCount = 1;
        Integer[] expected = new Integer[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};

        Double[] inputs = new Double[]{2.7325, 0.08};
        List<Integer[]> actual = rhythmApp.getOnsetIndexes(inputs, barCount, 3.0f);

        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0)).isEqualTo(expected);
        System.out.println("IS WORKING .. ");
    }

    @Test
    public void shouldReturnTwoArraysPosition() {

        int barCount = 2;
        Integer[] expectedOne = new Integer[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
        Integer[] expectedTwo = new Integer[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        Double[] inputs = new Double[]{2.7325, 0.08, 3.08};
        List<Integer[]> actual = rhythmApp.getOnsetIndexes(inputs, barCount, 3.0f);

        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0)).isEqualTo(expectedOne);
        assertThat(actual.get(1)).isEqualTo(expectedTwo);
    }

    @Test
    public void shouldReturnTwoArraysPositionV2() {

        int barCount = 2;
        Integer[] expectedOne = new Integer[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
        Integer[] expectedTwo = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};

        Double[] inputs = new Double[]{2.7325, 0.08, 5.7325};
        List<Integer[]> actual = rhythmApp.getOnsetIndexes(inputs, barCount, 3.0f);

        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0)).isEqualTo(expectedOne);
        assertThat(actual.get(1)).isEqualTo(expectedTwo);
    }
}