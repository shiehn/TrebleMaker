package com.treblemaker.options;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.SynthTemplate;
import com.treblemaker.model.SynthTemplateOption;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.options.interfaces.ISynthTemplateOptions;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class SynthTemplateOptionsTest extends TestCase {

    @Autowired
    ISynthTemplateOptions synthTemplateOptions;

    private ProgressionUnitBar progressionUnitBar;
    private SynthTemplate synthTemplate;

    @Before
    public void setup(){

        synthTemplate = new SynthTemplate();

        synthTemplate.setHiSynthId(11);
        synthTemplate.setHiSynthName("hisynth");

        synthTemplate.setHiSynthIdAlt(12);
        synthTemplate.setHiSynthNameAlt("hisynthalt");

        synthTemplate.setMidSynthId(21);
        synthTemplate.setMidSynthName("midsynth");

        synthTemplate.setMidSynthIdAlt(22);
        synthTemplate.setMidSynthNameAlt("midsynthalt");

        synthTemplate.setLowSynthId(31);
        synthTemplate.setLowSynthName("lowsynth");

        synthTemplate.setLowSynthIdAlt(32);
        synthTemplate.setLowSynthNameAlt("lowsynthalt");

        List<SynthTemplate> synthTemplates = new ArrayList<>();
        synthTemplates.add(synthTemplate);
        synthTemplates.add(synthTemplate);
        synthTemplates.add(synthTemplate);

        progressionUnitBar = new ProgressionUnitBar();
        progressionUnitBar.setSynthTemplates(synthTemplates);
    }

    @Test
    public void shouldSetCorrectNumberOfSynthOptions(){

        synthTemplateOptions.setHiSynthOptions(progressionUnitBar);
        synthTemplateOptions.setMidSynthOptions(progressionUnitBar);
        synthTemplateOptions.setLowSynthOptions(progressionUnitBar);

        Assert.assertEquals(3, progressionUnitBar.getSynthTemplateHiOptions().size());

        Assert.assertEquals(2, progressionUnitBar.getSynthTemplateHiOptions().get(0).size());
        Assert.assertEquals(2, progressionUnitBar.getSynthTemplateMidOptions().get(0).size());
        Assert.assertEquals(2, progressionUnitBar.getSynthTemplateLowOptions().get(0).size());
    }

    @Test
    public void shouldSetCorrectHiSynthOptionIds(){

        synthTemplateOptions.setHiSynthOptions(progressionUnitBar);

        SynthTemplateOption option = progressionUnitBar.getSynthTemplateHiOptions().get(0).stream().filter(s -> s.getId() == 11
                && s.getName().equalsIgnoreCase("hisynth")
                && s.getSynthRoll() == SynthTemplateOption.SynthRoll.SYNTH_HI).findFirst().get();
        Assert.assertNotNull(option);

        option = progressionUnitBar.getSynthTemplateHiOptions().get(0).stream().filter(s -> s.getId() == 12
                && s.getName().equalsIgnoreCase("hisynthalt")
                && s.getSynthRoll() == SynthTemplateOption.SynthRoll.SYNTH_HI).findFirst().get();
        Assert.assertNotNull(option);
    }

    @Test
    public void shouldSetCorrectMidSynthOptionIds() {

        synthTemplateOptions.setMidSynthOptions(progressionUnitBar);

        SynthTemplateOption option = progressionUnitBar.getSynthTemplateMidOptions().get(0).stream().filter(s -> s.getId() == 21
                && s.getName().equalsIgnoreCase("midsynth")
                && s.getSynthRoll() == SynthTemplateOption.SynthRoll.SYNTH_MID).findFirst().get();
        Assert.assertNotNull(option);

        option = progressionUnitBar.getSynthTemplateMidOptions().get(0).stream().filter(s -> s.getId() == 22
                && s.getName().equalsIgnoreCase("midsynthalt")
                && s.getSynthRoll() == SynthTemplateOption.SynthRoll.SYNTH_MID).findFirst().get();
        Assert.assertNotNull(option);
    }

    @Test
    public void shouldSetCorrectLowSynthOptionIds() {

        synthTemplateOptions.setLowSynthOptions(progressionUnitBar);

        SynthTemplateOption option = progressionUnitBar.getSynthTemplateLowOptions().get(0).stream().filter(s -> s.getId() == 31
                && s.getName().equalsIgnoreCase("lowsynth")
                && s.getSynthRoll() == SynthTemplateOption.SynthRoll.SYNTH_LOW).findFirst().get();
        Assert.assertNotNull(option);

        option = progressionUnitBar.getSynthTemplateLowOptions().get(0).stream().filter(s -> s.getId() == 32
                && s.getName().equalsIgnoreCase("lowsynthalt")
                && s.getSynthRoll() == SynthTemplateOption.SynthRoll.SYNTH_LOW).findFirst().get();
        Assert.assertNotNull(option);
    }
}