package com.treblemaker.weighters.themeweighters;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.weighters.enums.WeightClass;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class BeatLoopAltThemeWeighterTest extends TestCase {

    private BeatLoopAltThemeWeighter beatAltThemeWeighter;

    ProgressionUnitBar onePriorProgBar;
    ProgressionUnitBar twoPrioProgBar;

    BeatLoop oneBarLoop;
    BeatLoop twoBarsLoop;

    @Before
    public void setup(){

        onePriorProgBar = new ProgressionUnitBar();
        twoPrioProgBar = new ProgressionUnitBar();

        beatAltThemeWeighter = new BeatLoopAltThemeWeighter();
    }

    @Test
    public void shouldWeightGood_forOneBarPriorMatch(){

        BeatLoop barTooWeight = new BeatLoop();
        barTooWeight.setId(1);

        oneBarLoop = new BeatLoop();
        oneBarLoop.setId(1);
        onePriorProgBar.setBeatLoopAlt(oneBarLoop);

        twoBarsLoop = new BeatLoop();
        twoBarsLoop.setId(2);
        twoPrioProgBar.setBeatLoopAlt(twoBarsLoop);

        WeightClass result = beatAltThemeWeighter.calculateThemeWeight(barTooWeight, onePriorProgBar, twoPrioProgBar);

        assertThat(result).isEqualTo(WeightClass.GOOD);
    }

    @Test
    public void shouldWeightOk_forTwoBarsPriorMatch(){

        BeatLoop barTooWeight = new BeatLoop();
        barTooWeight.setId(2);

        oneBarLoop = new BeatLoop();
        oneBarLoop.setId(1);
        onePriorProgBar.setBeatLoopAlt(oneBarLoop);

        twoBarsLoop = new BeatLoop();
        twoBarsLoop.setId(2);
        twoPrioProgBar.setBeatLoopAlt(twoBarsLoop);

        WeightClass result = beatAltThemeWeighter.calculateThemeWeight(barTooWeight, onePriorProgBar, twoPrioProgBar);

        assertThat(result).isEqualTo(WeightClass.OK);
    }

    @Test
    public void shouldWeightBad_forNonMatch(){

        BeatLoop barTooWeight = new BeatLoop();
        barTooWeight.setId(0);

        oneBarLoop = new BeatLoop();
        oneBarLoop.setId(1);
        onePriorProgBar.setBeatLoopAlt(oneBarLoop);

        twoBarsLoop = new BeatLoop();
        twoBarsLoop.setId(2);
        twoPrioProgBar.setBeatLoopAlt(twoBarsLoop);

        WeightClass result = beatAltThemeWeighter.calculateThemeWeight(barTooWeight, onePriorProgBar, twoPrioProgBar);

        assertThat(result).isEqualTo(WeightClass.BAD);
    }

    @Test
    public void shouldHandleNulls(){

        BeatLoop barTooWeight = new BeatLoop();
        barTooWeight.setId(0);

        oneBarLoop = null;
        onePriorProgBar.setBeatLoopAlt(oneBarLoop);

        twoPrioProgBar = null;

        WeightClass result = beatAltThemeWeighter.calculateThemeWeight(barTooWeight, onePriorProgBar, twoPrioProgBar);

        assertThat(result).isEqualTo(WeightClass.BAD);
    }
}