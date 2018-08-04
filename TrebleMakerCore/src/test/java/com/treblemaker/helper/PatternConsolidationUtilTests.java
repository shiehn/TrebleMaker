package com.treblemaker.helper;

import com.treblemaker.model.SynthTemplate;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.renderers.helper.PatternConsolidationUtil;
import org.jfugue.pattern.Pattern;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PatternConsolidationUtilTests {

    private List<ProgressionUnit> progressionUnits;

    private final int BPM = 80;

    @Before
    public void setup(){
        List<SynthTemplate> synthTemplates = Arrays.asList(new SynthTemplate());

        ProgressionUnitBar pBarOne = new ProgressionUnitBar();
        pBarOne.addPatternMid(new Pattern("A1"));
        pBarOne.addPatternMidAlt(new Pattern("B1"));
        pBarOne.setKickMidiPattern(new Pattern("k1"));
        pBarOne.setSnareMidiPattern(new Pattern("s1"));
        pBarOne.setHatMidiPattern(new Pattern("h1"));
        pBarOne.setSynthTemplates(synthTemplates);

        ProgressionUnitBar pBarTwo = new ProgressionUnitBar();
        pBarTwo.addPatternMid(new Pattern("A2"));
        pBarTwo.addPatternMidAlt(new Pattern("B2"));
        pBarTwo.setKickMidiPattern(new Pattern("k2"));
        pBarTwo.setSnareMidiPattern(new Pattern("s2"));
        pBarTwo.setHatMidiPattern(new Pattern("h2"));
        pBarTwo.setSynthTemplates(synthTemplates);

        ProgressionUnitBar pBarThree = new ProgressionUnitBar();
        pBarThree.addPatternMid(new Pattern("A3"));
        pBarThree.addPatternMidAlt(new Pattern("B3"));
        pBarThree.setKickMidiPattern(new Pattern("k3"));
        pBarThree.setSnareMidiPattern(new Pattern("s3"));
        pBarThree.setHatMidiPattern(new Pattern("h3"));
        pBarThree.setSynthTemplates(synthTemplates);

        ProgressionUnitBar pBarFour = new ProgressionUnitBar();
        pBarFour.addPatternMid(new Pattern("A4"));
        pBarFour.addPatternMidAlt(new Pattern("B4"));
        pBarFour.setKickMidiPattern(new Pattern("k4"));
        pBarFour.setSnareMidiPattern(new Pattern("s4"));
        pBarFour.setHatMidiPattern(new Pattern("h4"));
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

    @Test
    public void shouldConsolidateMidiKickPatterns(){
        List<Pattern> pattern = PatternConsolidationUtil.consolidateKickMidiPatterns(progressionUnits, BPM);

        assertThat(pattern).hasSize(1);
        assertThat(pattern.get(0).toString()).isEqualTo("T80 k1 k2 k3 k4");
    }

    @Test
    public void shouldConsolidateMidiSnarePatterns(){
        List<Pattern> pattern = PatternConsolidationUtil.consolidateSnareMidiPatterns(progressionUnits, BPM);

        assertThat(pattern).hasSize(1);
        assertThat(pattern.get(0).toString()).isEqualTo("T80 s1 s2 s3 s4");
    }

    @Test
    public void shouldConsolidateMidiHatPatterns(){
        List<Pattern> pattern = PatternConsolidationUtil.consolidateHatMidiPatterns(progressionUnits, BPM);

        assertThat(pattern).hasSize(1);
        assertThat(pattern.get(0).toString()).isEqualTo("T80 h1 h2 h3 h4");
    }
}
