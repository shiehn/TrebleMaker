package com.treblemaker.renderers;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.hitsandfills.Fill;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.renderers.interfaces.IFillsRenderer;
import com.treblemaker.utils.FileStructure;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class FillsRendererTest extends TestCase {

    @Autowired
    private IFillsRenderer fillsRenderer;

    @Test
    public void shouldCreateFillsRenderArray(){
        Fill oneBarFill = new Fill();
        oneBarFill.setBarCount(1);

        Fill twoBarFill = new Fill();
        twoBarFill.setBarCount(2);

        ProgressionUnitBar aa = new ProgressionUnitBar();
        ProgressionUnitBar ab = new ProgressionUnitBar();
        ProgressionUnitBar ac = new ProgressionUnitBar();
        ac.setFill(oneBarFill);
        ProgressionUnitBar ad = new ProgressionUnitBar();

        ProgressionUnitBar ba = new ProgressionUnitBar();
        ProgressionUnitBar bb = new ProgressionUnitBar();
        ProgressionUnitBar bc = new ProgressionUnitBar();
        bc.setFill(twoBarFill);
        ProgressionUnitBar bd = new ProgressionUnitBar();


        ProgressionUnit progressionUnitOne = new ProgressionUnit();
        progressionUnitOne.setBarCount(4);
        progressionUnitOne.setProgressionUnitBars(Arrays.asList(aa,ab,ac,ad));

        ProgressionUnit progressionUnitTwo = new ProgressionUnit();
        progressionUnitTwo.setBarCount(4);
        progressionUnitTwo.setProgressionUnitBars(Arrays.asList(ba,bb,bc,bd));


        List<Fill> fills = fillsRenderer.createFillsToRender(Arrays.asList(progressionUnitOne, progressionUnitTwo));

        assertThat(fills.size()).isEqualTo(7);

        assertThat(fills.get(0)).isNull();
        assertThat(fills.get(1)).isNull();
        assertThat(fills.get(2)).isNotNull();
        assertThat(fills.get(3)).isNull();

        assertThat(fills.get(4)).isNull();
        assertThat(fills.get(5)).isNull();
        assertThat(fills.get(6)).isNotNull();
    }

    @Test
    public void shouldCreateFillsShim(){

        QueueState queueState = new QueueState();
        QueueItem queueItem = new QueueItem();
        queueItem.setBpm(80);
        queueItem.setQueueItemId("itemIdFillzz");
        queueState.setQueueItem(queueItem);

        File hitShim = fillsRenderer.createFillShim(queueState);

        assertThat(hitShim.exists()).isTrue();

        try {
            FileStructure.deleteSingleFile(hitShim.getPath());
        }catch (Exception exception){
            System.out.println("FAILED TO CLEANUP");
        }

        assertThat(hitShim.exists()).isFalse();
    }
}