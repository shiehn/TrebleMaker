package com.treblemaker.weighters.harmonicloopweighters;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.constants.LoopPositions;
import com.treblemaker.dal.interfaces.IAnalyticsHorizontalDal;
import com.treblemaker.model.analytics.AnalyticsHorizontal;
import com.treblemaker.model.*;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.selectors.interfaces.ITemplateSelector;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


    /*
    bypass_rhythm_ratings=${TM_BYPASS_RHYTHM_RATING}
bypass_bassline_vertical_rating=${TM_BYPASS_BASSLINE_RATING}
bypass_arpeggio_vertical_rating=${TM_BYPASS_ARPEGGIO_RATING}
bypass_harmonic_loop_vertical_ratings=${TM_BYPASS_HARMONIC_LOOP_RATING}
bypass_vertical_beat_ratings=${TM_BYPASS_BEAT_LOOP_RATING}
bypass_seqence_ratings=${TM_BYPASS_SEQUENCE_RATING}
bypass_eq_ratings=${TM_BYPASS_EQ_RATING}
#CANNOT RATE TRACK IF BYPASS ANALYTICS IS SET
bypass_analytics=${TM_BYPASS_ANALYTICS}
bypass_eqanalytics=${TM_BYPASS_EQ_ANALYTICS}
bypass_synthfx_rating=true
bypass_volume_level_ratings=true
bypass_healthmonitor=true
num_of_generated_mixes=${TM_NUM_OF_MIXES}
num_of_generated_mix_variations=${TM_NUM_OF_MIX_VARIATIONS}
connect_to_cache = true
machinelearning_endpoints=${TM_API_URL}
use_only_first_machinelearn_endpoint = true
     */

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker", "com.treblemaker.dal"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties", properties = {"bypass_rhythm_ratings=false", "bypass_harmonic_loop_vertical_ratings=false", "bypass_seqence_ratings=false"})
public class HarmonicLoopAltWeighterTest extends TestCase {

    @Autowired
    private HarmonicLoopAltWeighter harmonicLoopAltWeighter;

    @Autowired
    private IAnalyticsHorizontalDal analyticsHorizontalDal;

    @Autowired
    private ITemplateSelector templateSelector;

    private ProgressionUnit priorProgressionUnit;
    private ProgressionUnit currentProgressionUnit;

    @Test
    public void shouldSetHarmonicLoopAltWeights() {
        Iterable<AnalyticsHorizontal> analyticsHorizontals = analyticsHorizontalDal.findAll();

        harmonicLoopAltWeighter.setHarmonicLoopAltWeight(LoopPositions.POSITION_THREE, currentProgressionUnit, priorProgressionUnit, analyticsHorizontals);

        for(HarmonicLoop hAltOption : currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_THREE).getHarmonicLoopAltOptions()){
            assertThat(hAltOption.getVerticalWeight()).isNotNull();
            assertThat(hAltOption.getRhythmicWeight()).isNotNull();
            assertThat(hAltOption.getTimeseriesWeight()).isNotNull();
            assertThat(hAltOption.getThemeWeight()).isNotNull();
        }

        for(HarmonicLoop hAltOption : currentProgressionUnit.getProgressionUnitBars().get(LoopPositions.POSITION_TWO).getHarmonicLoopAltOptions()){
            assertThat(hAltOption.getVerticalWeight()).isNull();
            assertThat(hAltOption.getRhythmicWeight()).isNull();
            assertThat(hAltOption.getTimeseriesWeight()).isNull();
            assertThat(hAltOption.getEqWeight()).isNull();
            assertThat(hAltOption.getThemeWeight()).isNull();
        }
    }





    @Before
    public void test() throws Exception {

        // START BAR

        ProgressionUnitBar bar1 = new ProgressionUnitBar();

        HarmonicLoop hLoop1 = new HarmonicLoop();
        hLoop1.setId(1);
        bar1.setHarmonicLoop(hLoop1);

        HarmonicLoop hAltLoop1 = new HarmonicLoop();
        hAltLoop1.setId(1);
        bar1.setHarmonicLoopAlt(hAltLoop1);

        // END BAR
        // START BAR

        ProgressionUnitBar bar2 = new ProgressionUnitBar();

        HarmonicLoop hLoop2 = new HarmonicLoop();
        hLoop2.setId(2);
        bar2.setHarmonicLoop(hLoop2);

        HarmonicLoop hAltLoop2 = new HarmonicLoop();
        hAltLoop2.setId(2);
        bar2.setHarmonicLoopAlt(hAltLoop2);

        // END BAR
        // START BAR

        ProgressionUnitBar bar3 = new ProgressionUnitBar();

        HarmonicLoop hLoop3 = new HarmonicLoop();
        hLoop3.setId(3);
        bar3.setHarmonicLoop(hLoop3);

        HarmonicLoop hAltLoop3 = new HarmonicLoop();
        hAltLoop3.setId(3);
        bar3.setHarmonicLoopAlt(hAltLoop3);

        // END BAR
        // START BAR

        ProgressionUnitBar bar4 = new ProgressionUnitBar();

        HarmonicLoop hLoop4 = new HarmonicLoop();
        hLoop4.setId(4);
        bar4.setHarmonicLoop(hLoop4);

        HarmonicLoop hAltLoop4 = new HarmonicLoop();
        hAltLoop4.setId(4);
        bar4.setHarmonicLoopAlt(hAltLoop4);

        // END BAR

        //OPTIONS

        HarmonicLoop hAltLoopOption1 = new HarmonicLoop();
        hAltLoopOption1.setId(11);

        HarmonicLoop hAltLoopOption11 = new HarmonicLoop();
        hAltLoopOption11.setId(111);

        HarmonicLoop hAltLoopOption2 = new HarmonicLoop();
        hAltLoopOption2.setId(22);

        HarmonicLoop hAltLoopOption22 = new HarmonicLoop();
        hAltLoopOption22.setId(222);

        HarmonicLoop hAltLoopOption3 = new HarmonicLoop();
        hAltLoopOption3.setId(33);

        HarmonicLoop hAltLoopOption33 = new HarmonicLoop();
        hAltLoopOption33.setId(333);

        HarmonicLoop hAltLoopOption4 = new HarmonicLoop();
        hAltLoopOption4.setId(44);

        HarmonicLoop hAltLoopOption44 = new HarmonicLoop();
        hAltLoopOption44.setId(444);

        BeatLoop beatLoop = new BeatLoop();
        beatLoop.setId(0);

        HarmonicLoop harmonicLoop = new HarmonicLoop();
        harmonicLoop.setId(0);

        //SET BAR OPTIONS
        bar1.setHarmonicLoopAltOptions(asList(hAltLoopOption1, hAltLoopOption11));
        bar1.setBeatLoop(beatLoop);
        bar1.setHarmonicLoop(harmonicLoop);
        bar2.setHarmonicLoopAltOptions(asList(hAltLoopOption2, hAltLoopOption22));
        bar2.setBeatLoop(beatLoop);
        bar2.setHarmonicLoop(harmonicLoop);
        bar3.setHarmonicLoopAltOptions(asList(hAltLoopOption3, hAltLoopOption33));
        bar3.setBeatLoop(beatLoop);
        bar3.setHarmonicLoop(harmonicLoop);
        bar4.setHarmonicLoopAltOptions(asList(hAltLoopOption4, hAltLoopOption44));
        bar4.setBeatLoop(beatLoop);
        bar4.setHarmonicLoop(harmonicLoop);

        List<HarmonicLoop> harmonicAltOptions = asList(hAltLoopOption1, hAltLoopOption2, hAltLoopOption3, hAltLoopOption4);

        priorProgressionUnit = new ProgressionUnit();
        priorProgressionUnit.initBars(4);
        priorProgressionUnit.setProgressionUnitBars(asList(bar1, bar2, bar3, bar4));

        currentProgressionUnit = new ProgressionUnit();
        currentProgressionUnit.initBars(4);
        currentProgressionUnit.setProgressionUnitBars(asList(bar1, bar2, bar3, bar4));

        setSynthTemplates();
    }

    private void setSynthTemplates() throws Exception {
        SynthTemplate synthTemplate = templateSelector.chooseSpecific(11);
        priorProgressionUnit.getProgressionUnitBars().forEach(pBar -> pBar.setSynthTemplates(Arrays.asList(synthTemplate)));
        currentProgressionUnit.getProgressionUnitBars().forEach(pBar -> pBar.setSynthTemplates(Arrays.asList(synthTemplate)));
    }
}