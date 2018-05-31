package com.treblemaker.helper;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.SynthTemplate;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.renderers.helper.PatternConsolidationUtil;
import org.jfugue.pattern.Pattern;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"return_queue_early_for_tests=true","queue_scheduled_interval=8999999","queue_scheduled_start_delay=8999999","spring.datasource.url=jdbc:mysql://localhost:3306/hivecomposedb","spring.datasource.username=root","spring.datasource.password=redrobes79D"})
public class PatternConsolidationUtilTests {

    private List<ProgressionUnit> progressionUnits;

    private final int BPM = 80;

    @Before
    public void setup(){

        List<SynthTemplate> synthTemplates = Arrays.asList(new SynthTemplate());

        ProgressionUnitBar pBarOne = new ProgressionUnitBar();
        pBarOne.addPatternMid(new Pattern("A1"));
        pBarOne.addPatternMidAlt(new Pattern("B1"));
        pBarOne.setSynthTemplates(synthTemplates);

        ProgressionUnitBar pBarTwo = new ProgressionUnitBar();
        pBarTwo.addPatternMid(new Pattern("A2"));
        pBarTwo.addPatternMidAlt(new Pattern("B2"));
        pBarTwo.setSynthTemplates(synthTemplates);

        ProgressionUnitBar pBarThree = new ProgressionUnitBar();
        pBarThree.addPatternMid(new Pattern("A3"));
        pBarThree.addPatternMidAlt(new Pattern("B3"));
        pBarThree.setSynthTemplates(synthTemplates);

        ProgressionUnitBar pBarFour = new ProgressionUnitBar();
        pBarFour.addPatternMid(new Pattern("A4"));
        pBarFour.addPatternMidAlt(new Pattern("B4"));
        pBarFour.setSynthTemplates(synthTemplates);

        List<ProgressionUnitBar> progressionUnitBars = Arrays.asList(pBarOne, pBarTwo, pBarThree, pBarFour);

        ProgressionUnit progressionUnit = new ProgressionUnit();
        progressionUnit.setProgressionUnitBars(progressionUnitBars);

        progressionUnits = new ArrayList<>();
        progressionUnits.add(progressionUnit);
    }

    @Test
    public void shouldConsolidateAlternatingMidPatterns(){

        List<Pattern> pattern = PatternConsolidationUtil.consolidateMidPatterns(progressionUnits, BPM);

        assertThat(pattern.get(0).toString()).contains("A1");
        assertThat(pattern.get(0).toString()).contains("A3");
        assertThat(pattern.get(0).toString()).doesNotContain("A2");
        assertThat(pattern.get(0).toString()).doesNotContain("A4");
    }

    @Test
    public void shouldConsolidateAlternatingMidAltPatterns(){

        List<Pattern> pattern = PatternConsolidationUtil.consolidateMidAltPatterns(progressionUnits, BPM);

        assertThat(pattern.get(0).toString()).contains("B1");
        assertThat(pattern.get(0).toString()).contains("B3");
        assertThat(pattern.get(0).toString()).doesNotContain("B2");
        assertThat(pattern.get(0).toString()).doesNotContain("B4");
    }
}
