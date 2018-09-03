package com.treblemaker.renderers;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.hitsandfills.Hit;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.renderers.interfaces.IHitRenderer;
import com.treblemaker.utils.FileStructure;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.IntegrationTest;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Ignore
@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class HitRendererTest extends TestCase {

    @Autowired
    private IHitRenderer hitRenderer;

    @Test
    public void shouldCreateHitRenderArray(){

        Hit oneBarHit = new Hit();
        oneBarHit.setBarCount(1);

        Hit twoBarHit = new Hit();
        twoBarHit.setBarCount(2);

        Hit threeBarHit = new Hit();
        threeBarHit.setBarCount(3);

        ProgressionUnitBar aa = new ProgressionUnitBar();
        aa.setHit(oneBarHit);
        ProgressionUnitBar ab = new ProgressionUnitBar();
        ProgressionUnitBar ac = new ProgressionUnitBar();
        ProgressionUnitBar ad = new ProgressionUnitBar();

        ProgressionUnitBar ba = new ProgressionUnitBar();
        ba.setHit(twoBarHit);
        ProgressionUnitBar bb = new ProgressionUnitBar();
        ProgressionUnitBar bc = new ProgressionUnitBar();
        ProgressionUnitBar bd = new ProgressionUnitBar();

        ProgressionUnitBar ca = new ProgressionUnitBar();
        ca.setHit(threeBarHit);
        ProgressionUnitBar cb = new ProgressionUnitBar();
        ProgressionUnitBar cc = new ProgressionUnitBar();
        ProgressionUnitBar cd = new ProgressionUnitBar();

        ProgressionUnit progressionUnitOne = new ProgressionUnit();
        progressionUnitOne.setBarCount(4);
        progressionUnitOne.setProgressionUnitBars(Arrays.asList(aa,ab,ac,ad));

        ProgressionUnit progressionUnitTwo = new ProgressionUnit();
        progressionUnitTwo.setBarCount(4);
        progressionUnitTwo.setProgressionUnitBars(Arrays.asList(ba,bb,bc,bd));

        ProgressionUnit progressionUnitThree = new ProgressionUnit();
        progressionUnitThree.setBarCount(4);
        progressionUnitThree.setProgressionUnitBars(Arrays.asList(ca,cb,cc,cd));

        List<Hit> hits = hitRenderer.createHitsToRender(Arrays.asList(progressionUnitOne, progressionUnitTwo, progressionUnitThree));

        assertThat(hits.size()).isEqualTo(9);

        assertThat(hits.get(0)).isNotNull();
        assertThat(hits.get(1)).isNull();
        assertThat(hits.get(2)).isNull();
        assertThat(hits.get(3)).isNull();

        assertThat(hits.get(4)).isNotNull();
        assertThat(hits.get(5)).isNull();
        assertThat(hits.get(6)).isNull();

        assertThat(hits.get(7)).isNotNull();
        assertThat(hits.get(8)).isNull();
    }

    @Test
    public void shouldCreateHitShim(){

        QueueState queueState = new QueueState();
        QueueItem queueItem = new QueueItem();
        queueItem.setBpm(80);
        queueItem.setQueueItemId("itemIdZZZZ");
        queueState.setQueueItem(queueItem);

        File hitShim = hitRenderer.createHitShim(queueState);

        assertThat(hitShim.exists()).isTrue();

        try {
            FileStructure.deleteSingleFile(hitShim.getPath());
        }catch (Exception exception){
            System.out.println("FAILED TO CLEANUP");
        }

        assertThat(hitShim.exists()).isFalse();
    }
}
