package com.treblemaker.weighters.themeweighters;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.weighters.enums.WeightClass;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class HarmonicAltThemeWeighterTest extends TestCase {
    private HarmonicAltThemeWeighter harmonicAltThemeWeighter;

    ProgressionUnitBar onePriorProgBar;
    ProgressionUnitBar twoPrioProgBar;

    HarmonicLoop oneBarLoop;
    HarmonicLoop twoBarsLoop;

    @Before
    public void setup(){

        onePriorProgBar = new ProgressionUnitBar();
        twoPrioProgBar = new ProgressionUnitBar();

        harmonicAltThemeWeighter = new HarmonicAltThemeWeighter();
    }

    @Test
    public void shouldWeightGood_forOneBarPriorMatch(){

        HarmonicLoop barTooWeight = new HarmonicLoop();
        barTooWeight.setId(1);

        oneBarLoop = new HarmonicLoop();
        oneBarLoop.setId(1);
        onePriorProgBar.setHarmonicLoopAlt(oneBarLoop);

        twoBarsLoop = new HarmonicLoop();
        twoBarsLoop.setId(2);
        twoPrioProgBar.setHarmonicLoopAlt(twoBarsLoop);

        WeightClass result = harmonicAltThemeWeighter.calculateThemeWeight(barTooWeight, onePriorProgBar, twoPrioProgBar);

        assertThat(result).isEqualTo(WeightClass.GOOD);
    }

    @Test
    public void shouldWeightOk_forTwoBarsPriorMatch(){

        HarmonicLoop barTooWeight = new HarmonicLoop();
        barTooWeight.setId(2);

        oneBarLoop = new HarmonicLoop();
        oneBarLoop.setId(1);
        onePriorProgBar.setHarmonicLoopAlt(oneBarLoop);

        twoBarsLoop = new HarmonicLoop();
        twoBarsLoop.setId(2);
        twoPrioProgBar.setHarmonicLoopAlt(twoBarsLoop);

        WeightClass result = harmonicAltThemeWeighter.calculateThemeWeight(barTooWeight, onePriorProgBar, twoPrioProgBar);

        assertThat(result).isEqualTo(WeightClass.OK);
    }

    @Test
    public void shouldWeightBad_forNonMatch(){

        HarmonicLoop barTooWeight = new HarmonicLoop();
        barTooWeight.setId(0);

        oneBarLoop = new HarmonicLoop();
        oneBarLoop.setId(1);
        onePriorProgBar.setHarmonicLoopAlt(oneBarLoop);

        twoBarsLoop = new HarmonicLoop();
        twoBarsLoop.setId(2);
        twoPrioProgBar.setHarmonicLoopAlt(twoBarsLoop);

        WeightClass result = harmonicAltThemeWeighter.calculateThemeWeight(barTooWeight, onePriorProgBar, twoPrioProgBar);

        assertThat(result).isEqualTo(WeightClass.BAD);
    }

    @Test
    public void shouldHandleNulls(){

        HarmonicLoop barTooWeight = new HarmonicLoop();
        barTooWeight.setId(0);

        oneBarLoop = null;
        onePriorProgBar.setHarmonicLoopAlt(oneBarLoop);

        twoPrioProgBar = null;

        WeightClass result = harmonicAltThemeWeighter.calculateThemeWeight(barTooWeight, onePriorProgBar, twoPrioProgBar);

        assertThat(result).isEqualTo(WeightClass.BAD);
    }
}
