package com.treblemaker.weighters;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.weighters.helper.CurrentPriorBar;
import com.treblemaker.weighters.helper.WeightHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static com.treblemaker.model.progressions.ProgressionUnit.BarCount.FOUR;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class WeightHelperTests {

    @Test
    public void shouldSetCorrectCurrentPriorBarA(){

        ProgressionUnit priorUnit = new ProgressionUnit();
        priorUnit.initBars(FOUR.getValue());
        priorUnit.getProgressionUnitBars().get(0).setHiSynthId(Arrays.asList(11));
        priorUnit.getProgressionUnitBars().get(1).setHiSynthId(Arrays.asList(12));
        priorUnit.getProgressionUnitBars().get(2).setHiSynthId(Arrays.asList(13));
        priorUnit.getProgressionUnitBars().get(3).setHiSynthId(Arrays.asList(14));

        ProgressionUnit currentUnit = new ProgressionUnit();
        currentUnit.initBars(FOUR.getValue());
        currentUnit.getProgressionUnitBars().get(0).setHiSynthId(Arrays.asList(21));
        currentUnit.getProgressionUnitBars().get(1).setHiSynthId(Arrays.asList(22));
        currentUnit.getProgressionUnitBars().get(2).setHiSynthId(Arrays.asList(23));
        currentUnit.getProgressionUnitBars().get(3).setHiSynthId(Arrays.asList(24));

        int index = 0;

        CurrentPriorBar currentPriorBar = WeightHelper.createCurrentPriorBar(index, currentUnit, priorUnit);

        assertThat(Arrays.asList(21)).isEqualTo(currentPriorBar.getCurrentBar().getHiSynthId());
        assertThat(Arrays.asList(14)).isEqualTo(currentPriorBar.getOneBarPrior().getHiSynthId());
        assertThat(Arrays.asList(13)).isEqualTo(currentPriorBar.getSecondBarPrior().getHiSynthId());
    }

    @Test
    public void shouldSetCorrectCurrentPriorBarB(){

        ProgressionUnit priorUnit = new ProgressionUnit();
        priorUnit.initBars(FOUR.getValue());
        priorUnit.getProgressionUnitBars().get(0).setHiSynthId(Arrays.asList(11));
        priorUnit.getProgressionUnitBars().get(1).setHiSynthId(Arrays.asList(12));
        priorUnit.getProgressionUnitBars().get(2).setHiSynthId(Arrays.asList(13));
        priorUnit.getProgressionUnitBars().get(3).setHiSynthId(Arrays.asList(14));

        ProgressionUnit currentUnit = new ProgressionUnit();
        currentUnit.initBars(FOUR.getValue());
        currentUnit.getProgressionUnitBars().get(0).setHiSynthId(Arrays.asList(21));
        currentUnit.getProgressionUnitBars().get(1).setHiSynthId(Arrays.asList(22));
        currentUnit.getProgressionUnitBars().get(2).setHiSynthId(Arrays.asList(23));
        currentUnit.getProgressionUnitBars().get(3).setHiSynthId(Arrays.asList(24));

        int index = 1;

        CurrentPriorBar currentPriorBar = WeightHelper.createCurrentPriorBar(index, currentUnit, priorUnit);

        assertThat(Arrays.asList(22)).isEqualTo(currentPriorBar.getCurrentBar().getHiSynthId());
        assertThat(Arrays.asList(21)).isEqualTo(currentPriorBar.getOneBarPrior().getHiSynthId());
        assertThat(Arrays.asList(14)).isEqualTo(currentPriorBar.getSecondBarPrior().getHiSynthId());
    }

    @Test
    public void shouldSetCorrectCurrentPriorBarC(){

        ProgressionUnit priorUnit = new ProgressionUnit();
        priorUnit.initBars(FOUR.getValue());
        priorUnit.getProgressionUnitBars().get(0).setHiSynthId(Arrays.asList(11));
        priorUnit.getProgressionUnitBars().get(1).setHiSynthId(Arrays.asList(12));
        priorUnit.getProgressionUnitBars().get(2).setHiSynthId(Arrays.asList(13));
        priorUnit.getProgressionUnitBars().get(3).setHiSynthId(Arrays.asList(14));

        ProgressionUnit currentUnit = new ProgressionUnit();
        currentUnit.initBars(FOUR.getValue());
        currentUnit.getProgressionUnitBars().get(0).setHiSynthId(Arrays.asList(21));
        currentUnit.getProgressionUnitBars().get(1).setHiSynthId(Arrays.asList(22));
        currentUnit.getProgressionUnitBars().get(2).setHiSynthId(Arrays.asList(23));
        currentUnit.getProgressionUnitBars().get(3).setHiSynthId(Arrays.asList(24));

        int index = 2;

        CurrentPriorBar currentPriorBar = WeightHelper.createCurrentPriorBar(index, currentUnit, priorUnit);

        assertThat(Arrays.asList(23)).isEqualTo(currentPriorBar.getCurrentBar().getHiSynthId());
        assertThat(Arrays.asList(22)).isEqualTo(currentPriorBar.getOneBarPrior().getHiSynthId());
        assertThat(Arrays.asList(21)).isEqualTo(currentPriorBar.getSecondBarPrior().getHiSynthId());
    }

    @Test
    public void shouldSetCorrectCurrentPriorBarD(){

        ProgressionUnit priorUnit = new ProgressionUnit();
        priorUnit.initBars(FOUR.getValue());
        priorUnit.getProgressionUnitBars().get(0).setHiSynthId(Arrays.asList(11));
        priorUnit.getProgressionUnitBars().get(1).setHiSynthId(Arrays.asList(12));
        priorUnit.getProgressionUnitBars().get(2).setHiSynthId(Arrays.asList(13));
        priorUnit.getProgressionUnitBars().get(3).setHiSynthId(Arrays.asList(14));

        ProgressionUnit currentUnit = new ProgressionUnit();
        currentUnit.initBars(FOUR.getValue());
        currentUnit.getProgressionUnitBars().get(0).setHiSynthId(Arrays.asList(21));
        currentUnit.getProgressionUnitBars().get(1).setHiSynthId(Arrays.asList(22));
        currentUnit.getProgressionUnitBars().get(2).setHiSynthId(Arrays.asList(23));
        currentUnit.getProgressionUnitBars().get(3).setHiSynthId(Arrays.asList(24));

        int index = 3;

        CurrentPriorBar currentPriorBar = WeightHelper.createCurrentPriorBar(index, currentUnit, priorUnit);

        assertThat(Arrays.asList(24)).isEqualTo(currentPriorBar.getCurrentBar().getHiSynthId());
        assertThat(Arrays.asList(23)).isEqualTo(currentPriorBar.getOneBarPrior().getHiSynthId());
        assertThat(Arrays.asList(22)).isEqualTo(currentPriorBar.getSecondBarPrior().getHiSynthId());
    }


    @Test
    public void shouldSetCorrectCurrentPriorBarAWithNullPrior(){

        ProgressionUnit priorUnit = null;

        ProgressionUnit currentUnit = new ProgressionUnit();
        currentUnit.initBars(FOUR.getValue());
        currentUnit.getProgressionUnitBars().get(0).setHiSynthId(Arrays.asList(21));
        currentUnit.getProgressionUnitBars().get(1).setHiSynthId(Arrays.asList(22));
        currentUnit.getProgressionUnitBars().get(2).setHiSynthId(Arrays.asList(23));
        currentUnit.getProgressionUnitBars().get(3).setHiSynthId(Arrays.asList(24));

        int index = 0;

        CurrentPriorBar currentPriorBar = WeightHelper.createCurrentPriorBar(index, currentUnit, priorUnit);

        assertThat(Arrays.asList(21)).isEqualTo(currentPriorBar.getCurrentBar().getHiSynthId());
        assertThat(currentPriorBar.getOneBarPrior()).isNull();
        assertThat(currentPriorBar.getSecondBarPrior()).isNull();
    }

    @Test
    public void shouldSetCorrectCurrentPriorBarBWithNullPrior(){

        ProgressionUnit priorUnit = null;

        ProgressionUnit currentUnit = new ProgressionUnit();
        currentUnit.initBars(FOUR.getValue());
        currentUnit.getProgressionUnitBars().get(0).setHiSynthId(Arrays.asList(21));
        currentUnit.getProgressionUnitBars().get(1).setHiSynthId(Arrays.asList(22));
        currentUnit.getProgressionUnitBars().get(2).setHiSynthId(Arrays.asList(23));
        currentUnit.getProgressionUnitBars().get(3).setHiSynthId(Arrays.asList(24));

        int index = 1;

        CurrentPriorBar currentPriorBar = WeightHelper.createCurrentPriorBar(index, currentUnit, priorUnit);

        assertThat(Arrays.asList(22)).isEqualTo(currentPriorBar.getCurrentBar().getHiSynthId());
        assertThat(Arrays.asList(21)).isEqualTo(currentPriorBar.getOneBarPrior().getHiSynthId());
        assertThat(currentPriorBar.getSecondBarPrior()).isNull();
    }

    @Test
    public void shouldSetCorrectCurrentPriorBarCWithNullPrior(){

        ProgressionUnit priorUnit = null;

        ProgressionUnit currentUnit = new ProgressionUnit();
        currentUnit.initBars(FOUR.getValue());
        currentUnit.getProgressionUnitBars().get(0).setHiSynthId(Arrays.asList(21));
        currentUnit.getProgressionUnitBars().get(1).setHiSynthId(Arrays.asList(22));
        currentUnit.getProgressionUnitBars().get(2).setHiSynthId(Arrays.asList(23));
        currentUnit.getProgressionUnitBars().get(3).setHiSynthId(Arrays.asList(24));

        int index = 2;

        CurrentPriorBar currentPriorBar = WeightHelper.createCurrentPriorBar(index, currentUnit, priorUnit);

        assertThat(Arrays.asList(23)).isEqualTo(currentPriorBar.getCurrentBar().getHiSynthId());
        assertThat(Arrays.asList(22)).isEqualTo(currentPriorBar.getOneBarPrior().getHiSynthId());
        assertThat(Arrays.asList(21)).isEqualTo(currentPriorBar.getSecondBarPrior().getHiSynthId());
    }

    @Test
    public void shouldSetCorrectCurrentPriorBarDWithNullPrior(){

        ProgressionUnit priorUnit = null;

        ProgressionUnit currentUnit = new ProgressionUnit();
        currentUnit.initBars(FOUR.getValue());
        currentUnit.getProgressionUnitBars().get(0).setHiSynthId(Arrays.asList(21));
        currentUnit.getProgressionUnitBars().get(1).setHiSynthId(Arrays.asList(22));
        currentUnit.getProgressionUnitBars().get(2).setHiSynthId(Arrays.asList(23));
        currentUnit.getProgressionUnitBars().get(3).setHiSynthId(Arrays.asList(24));

        int index = 3;

        CurrentPriorBar currentPriorBar = WeightHelper.createCurrentPriorBar(index, currentUnit, priorUnit);

        assertThat(Arrays.asList(24)).isEqualTo(currentPriorBar.getCurrentBar().getHiSynthId());
        assertThat(Arrays.asList(23)).isEqualTo(currentPriorBar.getOneBarPrior().getHiSynthId());
        assertThat(Arrays.asList(22)).isEqualTo(currentPriorBar.getSecondBarPrior().getHiSynthId());
    }
}