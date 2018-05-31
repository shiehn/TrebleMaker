package com.treblemaker.dal.interfaces;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.HiveChord;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={
        "return_queue_early_for_tests=true",
        "queue_scheduled_interval=8999999",
        "queue_scheduled_start_delay=8999999",
        "spring.datasource.url=jdbc:mysql://localhost:3306/hivecomposedb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
        "spring.datasource.username=root",
        "spring.datasource.password=redrobes79D"})
public class IHarmonicLoopsDalTest extends TestCase {
    @Autowired
    private IHarmonicLoopsDal iHarmonicLoopsDal;

    @Transactional
    @Test
    public void ShouldRetrieveChords() {

        Iterable<HarmonicLoop> loops = iHarmonicLoopsDal.findAll();

        for (HarmonicLoop item : loops) {
            List<HiveChord> hiveChordList =  item.getChords();

            if(hiveChordList.size() > 0){
                Assert.assertTrue(true);
                return;
            }
        }
    }
}
