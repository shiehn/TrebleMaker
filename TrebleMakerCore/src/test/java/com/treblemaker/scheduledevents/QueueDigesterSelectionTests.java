package com.treblemaker.scheduledevents;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.dal.interfaces.IQueueItemCustomDal;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.scheduledevents.interfaces.IQueueDigester;
import com.treblemaker.services.queue.QueueService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"return_queue_early_for_tests=true","queue_scheduled_interval=8999999","queue_scheduled_start_delay=8999999","spring.datasource.url=jdbc:mysql://localhost:3306/hivecomposedb","spring.datasource.username=root","spring.datasource.password=redrobes79D","bypass_rhythm_ratings=true","bypass_bassline_vertical_rating=true","bypass_arpeggio_vertical_rating=true","bypass_harmonic_loop_vertical_ratings=true","bypass_vertical_beat_ratings=true","bypass_seqence_ratings=true","bypass_eq_ratings=true","bypass_eq_ratings=true","bypass_analytics=true","bypass_eqanalytics=true"})
public class QueueDigesterSelectionTests {

    @Autowired
    private IQueueItemCustomDal queueItemCustomDal;

    @Autowired
    private IQueueDigester queueDigester;

    @Autowired
    private QueueService queueService;

    private QueueItem queueItem;

    @Before
    public void addQueueItem() throws Exception {

        queueService.addQueueItem();
        queueItem = queueItemCustomDal.getQueueItem();
        queueItemCustomDal.setQueueItemProcessing(queueItem.getQueueItemId());
    }

    @Test
    public void should_useOneBeatPerProgressionType() throws Exception {

        GlobalState.getInstance().setQueueItemInprogress(false);
        QueueItem digestedItem = queueDigester.digest(queueItem);

        Map<ProgressionUnit.ProgressionType, Integer> typeAndBeatMap = new HashMap<>();

        for(ProgressionUnit progressionUnit : digestedItem.getProgression().getStructure()){
            for(ProgressionUnitBar progressionUnitBar : progressionUnit.getProgressionUnitBars()){
                Integer beatId = typeAndBeatMap.get(progressionUnit.getType());

                if(beatId == null){
                    typeAndBeatMap.put(progressionUnit.getType(), progressionUnitBar.getBeatLoop().getId());
                }else{
                    assertThat(progressionUnitBar.getBeatLoop().getId()).isEqualTo(beatId);
                }
            }
        }
    }

    @Test
    public void should_haveArpeggioAndBasslineAndMidiSetForEachBar() throws Exception {
        GlobalState.getInstance().setQueueItemInprogress(false);
        QueueItem digestedItem = queueDigester.digest(queueItem);

        boolean skip = false;

        for (ProgressionUnit progressionUnit : digestedItem.getProgression().getStructure()) {
            for(ProgressionUnitBar progressionUnitBar : progressionUnit.getProgressionUnitBars()){

                if(!skip){
                    System.out.println("testing bar from : " + progressionUnit.getType().toString());
                    assertThat(progressionUnitBar.getArpeggioId()).isGreaterThan(0);
                    assertThat(progressionUnitBar.getBasslineId()).isGreaterThan(0);
                    assertThat(progressionUnitBar.getSelectedBassline()).isNotNull();
                    assertThat(progressionUnitBar.getSelectedBassline().getId()).isGreaterThan(0);

                    String highPattern = progressionUnitBar.getPatternHi().toString().replace("T80", "").replace("RW","").replace("Rw","").replace("Rs","").replace("Ri","").replace("Rq","").replace(" ","");
                    String highAltPattern = progressionUnitBar.getPatternHiAlt().toString().replace("T80", "").replace("RW","").replace("Rw","").replace("Rs","").replace("Ri","").replace("Rq","").replace(" ","");

                    String hightPatternTotal= highPattern + highAltPattern;
                    assertThat(hightPatternTotal.length()).isGreaterThan(0);

                    String midPattern = progressionUnitBar.getPatternMid().toString().replace("T80", "").replace("RW","").replace("Rw","").replace("Rs","").replace("Ri","").replace("Rq","").replace(" ","");
                    String midAltPattern = progressionUnitBar.getPatternMidAlt().toString().replace("T80", "").replace("RW","").replace("Rw","").replace("Rs","").replace("Ri","").replace("Rq","").replace(" ","");

                    String midPatternTotal = midPattern + midAltPattern;
                    assertThat(midPatternTotal.length()).isGreaterThan(0);

                    String lowPattern = progressionUnitBar.getPatternLow().toString().replace("T80", "").replace("RW","").replace("Rw","").replace("Rs","").replace("Ri","").replace("Rq","").replace(" ","");
                    String lowAltPattern = progressionUnitBar.getPatternLowAlt().toString().replace("T80", "").replace("RW","").replace("Rw","").replace("Rs","").replace("Ri","").replace("Rq","").replace(" ","");

                    String lowPatternTotal = lowPattern + lowAltPattern;
                    assertThat(lowPatternTotal.length()).isGreaterThan(0);
                }

                skip = !skip;
            }
        }
    }

//    @Test
//    public void should_useHaveMatchingLoopsInProgressionUnits() throws Exception {
//
//    }
}
