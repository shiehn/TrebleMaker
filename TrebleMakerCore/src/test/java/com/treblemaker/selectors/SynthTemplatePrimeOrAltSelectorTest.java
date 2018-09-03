package com.treblemaker.selectors;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.SynthTemplateOption;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.selectors.interfaces.ISynthTemplatePrimeOrAltSelector;
import com.treblemaker.weighters.enums.WeightClass;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SynthTemplatePrimeOrAltSelectorTest extends TestCase {

    @Autowired
    private ISynthTemplatePrimeOrAltSelector synthTemplatePrimeOrAltSelector;

    @Test
    public void ShouldSetCorrectHiSynthA() {

        SynthTemplateOption hiSynth = new SynthTemplateOption(1, "one", SynthTemplateOption.SynthRoll.SYNTH_HI);
        hiSynth.setWeightClass(WeightClass.BAD);

        SynthTemplateOption hiAltSynth = new SynthTemplateOption(2, "two", SynthTemplateOption.SynthRoll.SYNTH_HI);
        hiAltSynth.setWeightClass(WeightClass.OK);

        List<SynthTemplateOption> synthOptions = new ArrayList<>();
        synthOptions.add(hiSynth);
        synthOptions.add(hiAltSynth);

        ProgressionUnitBar pBar = new ProgressionUnitBar();
        pBar.setSynthTemplateHiOptions(Arrays.asList(synthOptions, synthOptions));

        synthTemplatePrimeOrAltSelector.setHiSynths(pBar);

        assertThat(pBar.getHiSynthId()).hasSize(2);
        assertThat(pBar.getHiSynthId().get(1)).isEqualTo(2);
    }

    @Test
    public void ShouldSetCorrectHiSynthB() {

        SynthTemplateOption hiSynth = new SynthTemplateOption(1, "one", SynthTemplateOption.SynthRoll.SYNTH_HI);
        hiSynth.setWeightClass(WeightClass.GOOD);

        SynthTemplateOption hiAltSynth = new SynthTemplateOption(2, "two", SynthTemplateOption.SynthRoll.SYNTH_HI);
        hiAltSynth.setWeightClass(WeightClass.OK);

        List<SynthTemplateOption> synthOptions = new ArrayList<>();
        synthOptions.add(hiSynth);
        synthOptions.add(hiAltSynth);

        ProgressionUnitBar pBar = new ProgressionUnitBar();
        pBar.setSynthTemplateHiOptions(Arrays.asList(synthOptions, synthOptions));

        synthTemplatePrimeOrAltSelector.setHiSynths(pBar);

        assertThat(pBar.getHiSynthId()).hasSize(2);
        assertThat(pBar.getHiSynthId().get(1)).isEqualTo(1);
    }

    @Test
    public void ShouldSetCorrectHiSynthC() {

        SynthTemplateOption hiSynth = new SynthTemplateOption(1, "one", SynthTemplateOption.SynthRoll.SYNTH_HI);
        hiSynth.setWeightClass(WeightClass.GOOD);

        SynthTemplateOption hiAltSynth = new SynthTemplateOption(2, "two", SynthTemplateOption.SynthRoll.SYNTH_HI);
        hiAltSynth.setWeightClass(WeightClass.GOOD);

        List<SynthTemplateOption> synthOptions = new ArrayList<>();
        synthOptions.add(hiSynth);
        synthOptions.add(hiAltSynth);

        ProgressionUnitBar pBar = new ProgressionUnitBar();
        pBar.setSynthTemplateHiOptions(Arrays.asList(synthOptions, synthOptions));

        synthTemplatePrimeOrAltSelector.setHiSynths(pBar);

        boolean isEither = (pBar.getHiSynthId().get(1) == 1 || pBar.getHiSynthId().get(1) == 2);

        Assert.assertTrue(isEither);
    }


    //MID SYNTHS

    @Test
    public void ShouldSetCorrectMidSynthA() {

        SynthTemplateOption midSynth = new SynthTemplateOption(1, "one", SynthTemplateOption.SynthRoll.SYNTH_MID);
        midSynth.setWeightClass(WeightClass.BAD);

        SynthTemplateOption midAltSynth = new SynthTemplateOption(2, "two", SynthTemplateOption.SynthRoll.SYNTH_MID);
        midAltSynth.setWeightClass(WeightClass.OK);

        List<SynthTemplateOption> synthOptions = new ArrayList<>();
        synthOptions.add(midSynth);
        synthOptions.add(midAltSynth);

        ProgressionUnitBar pBar = new ProgressionUnitBar();
        pBar.setSynthTemplateMidOptions(Arrays.asList(synthOptions, synthOptions));

        synthTemplatePrimeOrAltSelector.setMidSynths(pBar);

        assertThat(pBar.getMidSynthId()).hasSize(2);
        assertThat(pBar.getMidSynthId().get(1)).isEqualTo(2);
    }

    @Test
    public void ShouldSetCorrectMidSynthB() {

        SynthTemplateOption midSynth = new SynthTemplateOption(1, "one", SynthTemplateOption.SynthRoll.SYNTH_MID);
        midSynth.setWeightClass(WeightClass.GOOD);

        SynthTemplateOption midAltSynth = new SynthTemplateOption(2, "two", SynthTemplateOption.SynthRoll.SYNTH_MID);
        midAltSynth.setWeightClass(WeightClass.OK);

        List<SynthTemplateOption> synthOptions = new ArrayList<>();
        synthOptions.add(midSynth);
        synthOptions.add(midAltSynth);

        ProgressionUnitBar pBar = new ProgressionUnitBar();
        pBar.setSynthTemplateMidOptions(Arrays.asList(synthOptions, synthOptions));

        synthTemplatePrimeOrAltSelector.setMidSynths(pBar);

        assertThat(pBar.getMidSynthId()).hasSize(2);
        assertThat(pBar.getMidSynthId().get(1)).isEqualTo(1);
    }

    @Test
    public void ShouldSetCorrectMidSynthC() {

        SynthTemplateOption midSynth = new SynthTemplateOption(1, "one", SynthTemplateOption.SynthRoll.SYNTH_HI);
        midSynth.setWeightClass(WeightClass.GOOD);

        SynthTemplateOption midAltSynth = new SynthTemplateOption(2, "two", SynthTemplateOption.SynthRoll.SYNTH_HI);
        midAltSynth.setWeightClass(WeightClass.GOOD);

        List<SynthTemplateOption> synthOptions = new ArrayList<>();
        synthOptions.add(midSynth);
        synthOptions.add(midAltSynth);

        ProgressionUnitBar pBar = new ProgressionUnitBar();
        pBar.setSynthTemplateMidOptions(Arrays.asList(synthOptions, synthOptions));

        synthTemplatePrimeOrAltSelector.setMidSynths(pBar);

        boolean isEither = (pBar.getMidSynthId().get(1) == 1 || pBar.getMidSynthId().get(1) == 2);

        Assert.assertTrue(isEither);
    }

    //LOW SYNTHS

    @Test
    public void ShouldSetCorrectLowSynthA() {

        SynthTemplateOption lowSynth = new SynthTemplateOption(1, "one", SynthTemplateOption.SynthRoll.SYNTH_LOW);
        lowSynth.setWeightClass(WeightClass.BAD);

        SynthTemplateOption lowAltSynth = new SynthTemplateOption(2, "two", SynthTemplateOption.SynthRoll.SYNTH_LOW);
        lowAltSynth.setWeightClass(WeightClass.OK);

        List<SynthTemplateOption> synthOptions = new ArrayList<>();
        synthOptions.add(lowSynth);
        synthOptions.add(lowAltSynth);

        ProgressionUnitBar pBar = new ProgressionUnitBar();
        pBar.setSynthTemplateLowOptions(Arrays.asList(synthOptions, synthOptions));

        synthTemplatePrimeOrAltSelector.setLowSynths(pBar);

        assertThat(pBar.getLowSynthId()).hasSize(2);
        assertThat(pBar.getLowSynthId().get(1)).isEqualTo(2);
    }

    @Test
    public void ShouldSetCorrectLowSynthB() {

        SynthTemplateOption lowSynth = new SynthTemplateOption(1, "one", SynthTemplateOption.SynthRoll.SYNTH_LOW);
        lowSynth.setWeightClass(WeightClass.GOOD);

        SynthTemplateOption lowAltSynth = new SynthTemplateOption(2, "two", SynthTemplateOption.SynthRoll.SYNTH_LOW);
        lowAltSynth.setWeightClass(WeightClass.OK);

        List<SynthTemplateOption> synthOptions = new ArrayList<>();
        synthOptions.add(lowSynth);
        synthOptions.add(lowAltSynth);

        ProgressionUnitBar pBar = new ProgressionUnitBar();
        pBar.setSynthTemplateLowOptions(Arrays.asList(synthOptions, synthOptions));

        synthTemplatePrimeOrAltSelector.setLowSynths(pBar);

        assertThat(pBar.getLowSynthId()).hasSize(2);
        assertThat(pBar.getLowSynthId().get(1)).isEqualTo(1);
    }

    @Test
    public void ShouldSetCorrectLowSynthC() {

        SynthTemplateOption lowSynth = new SynthTemplateOption(1, "one", SynthTemplateOption.SynthRoll.SYNTH_LOW);
        lowSynth.setWeightClass(WeightClass.GOOD);

        SynthTemplateOption lowAltSynth = new SynthTemplateOption(2, "two", SynthTemplateOption.SynthRoll.SYNTH_LOW);
        lowAltSynth.setWeightClass(WeightClass.GOOD);

        List<SynthTemplateOption> synthOptions = new ArrayList<>();
        synthOptions.add(lowSynth);
        synthOptions.add(lowAltSynth);

        ProgressionUnitBar pBar = new ProgressionUnitBar();
        pBar.setSynthTemplateLowOptions(Arrays.asList(synthOptions, synthOptions));

        synthTemplatePrimeOrAltSelector.setLowSynths(pBar);

        boolean isEither = (pBar.getLowSynthId().get(1) == 1 || pBar.getLowSynthId().get(1) == 2);

        Assert.assertTrue(isEither);
    }
}
