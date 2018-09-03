package com.treblemaker.eventchain;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.dal.interfaces.IFXArpeggioDelayDal;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.fx.FXArpeggioDelay;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.selectors.interfaces.ISynthFXSelector;
import com.treblemaker.weighters.interfaces.ISynthFXWeighter;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Ignore
@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class SetFXEventTest extends TestCase {
    private QueueState queueState;
    private IEventChain setFXEvent;
    private IFXArpeggioDelayDal fxArpeggioDelayDal;
    private ISynthFXWeighter synthFXWeighter;
    private ISynthFXSelector synthFXSelector;

    private void mockDal(){

        FXArpeggioDelay fxArpeggioDelayA = new FXArpeggioDelay();
        fxArpeggioDelayA.setId(555);
        FXArpeggioDelay fxArpeggioDelayB = new FXArpeggioDelay();
        fxArpeggioDelayB.setId(666);

//        fxArpeggioDelayDal = mock(IFXArpeggioDelayDal.class);
//        when(fxArpeggioDelayDal.findAll()).thenReturn(Arrays.asList(fxArpeggioDelayA, fxArpeggioDelayB));
//
//        synthFXWeighter = mock(ISynthFXWeighter.class);
//        when(synthFXWeighter.setWeights(any())).thenReturn(queueState);
//
//        synthFXSelector = mock(ISynthFXSelector.class);
//        when(synthFXSelector.selectSynthFX(anyListOf(ProgressionUnit.class))).thenReturn(Arrays.asList(fxArpeggioDelayA, fxArpeggioDelayB));
    }

    @Before
    public void setup(){

        queueState = new QueueState();

        ProgressionUnitBar barA = new ProgressionUnitBar();
        barA.setHiSynthId(Arrays.asList(111));
        ProgressionUnitBar barB = new ProgressionUnitBar();
        barB.setHiSynthId(Arrays.asList(222));
        ProgressionUnitBar barC = new ProgressionUnitBar();
        barC.setHiSynthId(Arrays.asList(333));

        ProgressionUnit progressionUnitA = new ProgressionUnit();
        progressionUnitA.setProgressionUnitBars(Arrays.asList(barA,barA,barC,barA));

        ProgressionUnit progressionUnitB = new ProgressionUnit();
        progressionUnitB.setProgressionUnitBars(Arrays.asList(barB,barA,barB,barA));

        ProgressionUnit progressionUnitC = new ProgressionUnit();
        progressionUnitC.setProgressionUnitBars(Arrays.asList(barB,barB,barB,barA));

        ProgressionUnit progressionUnitD = new ProgressionUnit();
        progressionUnitD.setProgressionUnitBars(Arrays.asList(barC,barC,barC,barA));

        List<ProgressionUnit> progressionUnits = Arrays.asList(progressionUnitA, progressionUnitB, progressionUnitC, progressionUnitD);

        ProgressionDTO progression = new ProgressionDTO();
        progression.setStructure(progressionUnits);

        QueueItem queueItem = new QueueItem();
        queueItem.setProgression(progression);

        queueState.setQueueItem(queueItem);

        mockDal();

        setFXEvent = new SetFXEvent(fxArpeggioDelayDal, synthFXWeighter, synthFXSelector);
    }

    @Test
    public void setFX() throws IOException, InterruptedException {

        //111:555
        //333:666

//        queueState = setFXEvent.set(queueState);

        assertThat(true).isFalse();

//        assertThat(queueState.getStructure().get(0).getProgressionUnitBars().get(0).getHiSynthId()).isEqualTo(111);
//        assertThat(queueState.getStructure().get(0).getProgressionUnitBars().get(0).getSelectedFXArpeggioDelay().getId()).isEqualTo(555);
//
//        assertThat(queueState.getStructure().get(0).getProgressionUnitBars().get(1).getHiSynthId()).isEqualTo(111);
//        assertThat(queueState.getStructure().get(0).getProgressionUnitBars().get(1).getSelectedFXArpeggioDelay().getId()).isEqualTo(555);
//
//        assertThat(queueState.getStructure().get(3).getProgressionUnitBars().get(2).getHiSynthId()).isEqualTo(333);
//        assertThat(queueState.getStructure().get(3).getProgressionUnitBars().get(2).getSelectedFXArpeggioDelay().getId()).isEqualTo(666);
//
//        assertThat(queueState.getStructure().get(3).getProgressionUnitBars().get(3).getHiSynthId()).isEqualTo(111);
//        assertThat(queueState.getStructure().get(3).getProgressionUnitBars().get(3).getSelectedFXArpeggioDelay().getId()).isEqualTo(555);
    }
}
