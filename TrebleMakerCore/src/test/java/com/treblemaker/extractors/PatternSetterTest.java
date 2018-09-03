package com.treblemaker.extractors;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.extractors.interfaces.IPatternSetter;
import com.treblemaker.model.SynthTemplate;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import junit.framework.TestCase;
import org.jfugue.pattern.Pattern;
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
public class PatternSetterTest extends TestCase {
    @Autowired
    private IPatternSetter patternSetter;

    private ProgressionUnitBar pBar;

    private final int BPM = 80;

    @Before
    public void setup(){

        SynthTemplate synthTemplate = new SynthTemplate();
        synthTemplate.setHiSynthId(1);
        synthTemplate.setHiSynthIdAlt(11);
        synthTemplate.setMidSynthId(2);
        synthTemplate.setMidSynthIdAlt(22);
        synthTemplate.setLowSynthId(3);
        synthTemplate.setLowSynthIdAlt(33);

        List<SynthTemplate> synthTemplates = new ArrayList<>();
        synthTemplates.add(synthTemplate);

        pBar = new ProgressionUnitBar();
        pBar.setSynthTemplates(synthTemplates);

        pBar.addPatternHi(new Pattern("Cmajw"));
        pBar.addPatternHiAlt(new Pattern("Cmajw"));
        pBar.addPatternMid(new Pattern("Cmajw"));
        pBar.addPatternMidAlt(new Pattern("Cmajw"));
        pBar.addPatternLow(new Pattern("Cmajw"));
        pBar.addPatternLowAlt(new Pattern("Cmajw"));

        pBar.addHiSynthId(11);
        pBar.addMidSynthId(2);
        pBar.addLowSynthId(33);
    }

    @Test
    public void ShouldSetHiPattern() {

        patternSetter.setHiPattern(pBar,BPM);
        Assert.assertTrue(pBar.getPatternHi().toString().contains("T80 Rw"));
    }

    @Test
    public void ShouldSetHiAltPattern() {

        patternSetter.setHiPattern(pBar,BPM);
        Assert.assertTrue(pBar.getPatternHiAlt().get(0).toString().equalsIgnoreCase("Cmajw"));
    }

    @Test
    public void ShouldSetMidPattern(){

        patternSetter.setMidPattern(pBar,BPM);
        Assert.assertTrue(pBar.getPatternMid().get(0).toString().equalsIgnoreCase("Cmajw"));
    }

    @Test
    public void ShouldSetMidAltPattern() {

        patternSetter.setAltMidPattern(pBar,BPM);
        Assert.assertTrue(pBar.getPatternMidAlt().get(0).toString().contains("T80 Rw"));
    }

    @Test
    public void ShouldSetLowPattern() {

        patternSetter.setLowPattern(pBar,BPM);
        Assert.assertTrue(pBar.getPatternLow().toString().contains("T80 Rw"));
    }

    @Test
    public void ShouldSetLowAltPattern() {

        patternSetter.setLowPattern(pBar,BPM);
        Assert.assertTrue(pBar.getPatternLowAlt().toString().contains("Cmajw"));
    }
}