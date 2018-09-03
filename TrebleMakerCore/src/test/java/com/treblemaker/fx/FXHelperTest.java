package com.treblemaker.fx;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.SynthTemplate;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class FXHelperTest extends TestCase {

    List<ProgressionUnit> progressionUnits;

    @Before
    public void setup() {

        final List<SynthTemplate> synthTemplates = Arrays.asList(new SynthTemplate(), new SynthTemplate(), new SynthTemplate());

        ProgressionUnitBar pBarOne = new ProgressionUnitBar();
        pBarOne.setHiSynthId(Arrays.asList(1, 5, 9, 13));
        pBarOne.setSynthTemplates(synthTemplates);

        ProgressionUnitBar pBarTwo = new ProgressionUnitBar();
        pBarTwo.setHiSynthId(Arrays.asList(2, 6, 10, 14));
        pBarTwo.setSynthTemplates(synthTemplates);

        ProgressionUnitBar pBarThree = new ProgressionUnitBar();
        pBarThree.setHiSynthId(Arrays.asList(3, 7, 11, 15));
        pBarThree.setSynthTemplates(synthTemplates);

        ProgressionUnitBar pBarFour = new ProgressionUnitBar();
        pBarFour.setHiSynthId(Arrays.asList(4, 8, 12, 16));
        pBarFour.setSynthTemplates(synthTemplates);

        ProgressionUnit pUnit = new ProgressionUnit();
        pUnit.setBarCount(4);
        pUnit.setProgressionUnitBars(Arrays.asList(pBarOne, pBarTwo, pBarThree, pBarFour));

        progressionUnits = new ArrayList<>();
        progressionUnits.add(pUnit);
    }
    
    @Test
    public void shouldSelectCorrectNumberOfSynthIds() {

        List<List<Integer>> lists = FXHelper.extractHiSynthIds(progressionUnits);

        assertThat(lists.size()).isEqualTo(3);

        assertThat(lists.get(0).size()).isEqualTo(2);
        assertThat(lists.get(0).get(0)).isEqualTo(1);
        assertThat(lists.get(0).get(1)).isEqualTo(2);

        assertThat(lists.get(1).size()).isEqualTo(2);
        assertThat(lists.get(1).get(0)).isEqualTo(5);
        assertThat(lists.get(1).get(1)).isEqualTo(6);

        assertThat(lists.get(2).size()).isEqualTo(2);
        assertThat(lists.get(2).get(0)).isEqualTo(9);
        assertThat(lists.get(2).get(1)).isEqualTo(10);
    }
}