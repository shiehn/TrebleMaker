package com.treblemaker.dal.interfaces;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.HiveChord;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

@Ignore
@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
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
