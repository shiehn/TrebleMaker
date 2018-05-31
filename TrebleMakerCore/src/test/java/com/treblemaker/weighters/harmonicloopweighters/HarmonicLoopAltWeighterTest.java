package com.treblemaker.weighters.harmonicloopweighters;

import com.hazelcast.core.IMap;
import com.treblemaker.Application;
import com.treblemaker.SpringConfiguration;
import com.treblemaker.constants.LoopPositions;
import com.treblemaker.model.analytics.AnalyticsHorizontal;
import com.treblemaker.model.*;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class HarmonicLoopAltWeighterTest extends TestCase {

    @Autowired
    private HarmonicLoopAltWeighter harmonicLoopAltWeighter;

    @Value("${cache_key_hive_cache}")
    private String cacheKeyHiveCache;

    @Value("${cache_key_timeseries}")
    private String cacheKeyTimeseries;

    private ProgressionUnit priorProgressionUnit;
    private ProgressionUnit currentProgressionUnit;

    @Test
    public void shouldSetHarmonicLoopAltWeights() {

        IMap hiveCache = Application.client.getMap(cacheKeyHiveCache);
        Object cachedData = hiveCache.get(cacheKeyTimeseries);

        Iterable<AnalyticsHorizontal> analyticsHorizontals = (Iterable<AnalyticsHorizontal>) cachedData;

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
    public void test() {

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

        //SET BAR OPTIONS
        bar1.setHarmonicLoopAltOptions(asList(hAltLoopOption1, hAltLoopOption11));
        bar2.setHarmonicLoopAltOptions(asList(hAltLoopOption2, hAltLoopOption22));
        bar3.setHarmonicLoopAltOptions(asList(hAltLoopOption3, hAltLoopOption33));
        bar4.setHarmonicLoopAltOptions(asList(hAltLoopOption4, hAltLoopOption44));

        List<HarmonicLoop> harmonicAltOptions = asList(hAltLoopOption1, hAltLoopOption2, hAltLoopOption3, hAltLoopOption4);

        priorProgressionUnit = new ProgressionUnit();
        priorProgressionUnit.initBars(4);
        priorProgressionUnit.setProgressionUnitBars(asList(bar1, bar2, bar3, bar4));

        currentProgressionUnit = new ProgressionUnit();
        currentProgressionUnit.initBars(4);
        currentProgressionUnit.setProgressionUnitBars(asList(bar1, bar2, bar3, bar4));
    }
}