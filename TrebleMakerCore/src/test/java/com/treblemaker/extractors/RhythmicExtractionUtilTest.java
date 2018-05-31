package com.treblemaker.extractors;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.dal.interfaces.IBeatLoopsDal;
import com.treblemaker.dal.interfaces.IHarmonicLoopsDal;
import com.treblemaker.utils.LoopUtils;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class RhythmicExtractionUtilTest extends TestCase {

    RhythmicExtractionUtil rhythmApp;

    @Autowired
    private IBeatLoopsDal beatLoopsDal;

    @Autowired
    private IHarmonicLoopsDal harmonicLoopsDal;

    @Before
    public void setup() {
        rhythmApp = new RhythmicExtractionUtil(beatLoopsDal, harmonicLoopsDal);
    }

    @Test
    public void performRhythmicExtraction() throws Exception {
        rhythmApp.performHarmonicExtraction();

        assertThat(true).isTrue();
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