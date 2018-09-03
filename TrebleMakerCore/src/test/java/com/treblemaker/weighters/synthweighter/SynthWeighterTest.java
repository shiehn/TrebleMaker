package com.treblemaker.weighters.synthweighter;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.SynthTemplateOption;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.weighters.interfaces.ISynthWeighter;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class SynthWeighterTest extends TestCase {

    @Autowired
    private ISynthWeighter synthWeighter;

    @Test
    public void ShouldSetSynthWeights() {

        ProgressionUnitBar progressionUnitBar = new ProgressionUnitBar();
        progressionUnitBar.setBeatLoop(new BeatLoop());
        progressionUnitBar.setHarmonicLoop(new HarmonicLoop());

        SynthTemplateOption synthTemplateOptionHi = new SynthTemplateOption(1, "hi", SynthTemplateOption.SynthRoll.SYNTH_HI);
        SynthTemplateOption synthTemplateOptionHiAlt = new SynthTemplateOption(11, "hialt", SynthTemplateOption.SynthRoll.SYNTH_HI);
        SynthTemplateOption synthTemplateOptionMid = new SynthTemplateOption(2, "mid", SynthTemplateOption.SynthRoll.SYNTH_MID);
        SynthTemplateOption synthTemplateOptionMidAlt = new SynthTemplateOption(2, "midalt", SynthTemplateOption.SynthRoll.SYNTH_MID);
        SynthTemplateOption synthTemplateOptionLow = new SynthTemplateOption(3, "low", SynthTemplateOption.SynthRoll.SYNTH_LOW);
        SynthTemplateOption synthTemplateOptionLowAlt = new SynthTemplateOption(3, "lowalt", SynthTemplateOption.SynthRoll.SYNTH_LOW);

        progressionUnitBar.addSynthTemplateHiOption(Arrays.asList(synthTemplateOptionHi, synthTemplateOptionHi));
        progressionUnitBar.addSynthTemplateHiOption(Arrays.asList(synthTemplateOptionHiAlt, synthTemplateOptionHiAlt));
        progressionUnitBar.addSynthTemplateMidOption(Arrays.asList(synthTemplateOptionMid, synthTemplateOptionMid));
        progressionUnitBar.addSynthTemplateMidOption(Arrays.asList(synthTemplateOptionMidAlt, synthTemplateOptionMidAlt));
        progressionUnitBar.addSynthTemplateLowOption(Arrays.asList(synthTemplateOptionLow, synthTemplateOptionLow));
        progressionUnitBar.addSynthTemplateLowOption(Arrays.asList(synthTemplateOptionLowAlt, synthTemplateOptionLowAlt));

        synthWeighter.setWeights(progressionUnitBar, SynthTemplateOption.SynthRoll.SYNTH_HI);

        List<SynthTemplateOption> hiOptions = progressionUnitBar.getSynthTemplateHiOptions().get(1).stream().filter(s -> s.getWeightClass() != null).collect(Collectors.toList());
        Assert.assertEquals(2, hiOptions.size());

        synthWeighter.setWeights(progressionUnitBar, SynthTemplateOption.SynthRoll.SYNTH_MID);

        List<SynthTemplateOption> midOptions = progressionUnitBar.getSynthTemplateMidOptions().get(1).stream().filter(s -> s.getWeightClass() != null).collect(Collectors.toList());
        Assert.assertEquals(2, midOptions.size());

        synthWeighter.setWeights(progressionUnitBar, SynthTemplateOption.SynthRoll.SYNTH_LOW);

        List<SynthTemplateOption> lowOptions = progressionUnitBar.getSynthTemplateLowOptions().get(1).stream().filter(s -> s.getWeightClass() != null).collect(Collectors.toList());
        Assert.assertEquals(2, lowOptions.size());
    }
}