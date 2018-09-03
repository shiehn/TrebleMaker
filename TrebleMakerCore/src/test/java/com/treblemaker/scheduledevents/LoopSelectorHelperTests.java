package com.treblemaker.scheduledevents;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.HiveChord;
import com.treblemaker.selectors.helper.LoopSelectorHelper;
import com.treblemaker.weighters.enums.WeightClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class LoopSelectorHelperTests {

    @Test
    public void should_filterByChordCompatible(){

        HiveChord chordMatch = new HiveChord("gmaj");
        HiveChord chordMissMatch = new HiveChord("dmin");

        HarmonicLoop loopA = new HarmonicLoop();
        loopA.setId(11);
        loopA.setChords(new ArrayList<HiveChord>(){{
           add(chordMatch);
        }});

        HarmonicLoop loopB = new HarmonicLoop();
        loopB.setId(22);
        loopB.setChords(new ArrayList<HiveChord>(){{
            add(chordMissMatch);
        }});

        HarmonicLoop loopC = new HarmonicLoop();
        loopC.setId(33);
        loopC.setChords(new ArrayList<HiveChord>(){{
            add(chordMatch);
        }});

        List<HarmonicLoop> unOrderedLoops = new ArrayList<>();
        unOrderedLoops.add(loopA);
        unOrderedLoops.add(loopB);
        unOrderedLoops.add(loopC);

        List<HarmonicLoop> outputLoops = LoopSelectorHelper.filterChordCompatibleHarmonicLoops(unOrderedLoops, chordMatch, 999);

        List<HarmonicLoop> orderedLoops = new ArrayList<>();
        orderedLoops.add(loopA);
        orderedLoops.add(loopC);

        Assert.assertEquals(2,orderedLoops.size());

        for(int i=0; i<orderedLoops.size(); i++){
            Assert.assertEquals(orderedLoops.get(i).getId(), outputLoops.get(i).getId());
        }
    }

    @Test
    public void should_orderHarmonicLoopsByWeight(){

        HiveChord chordMatch = new HiveChord("gmaj");
        HiveChord chordMissMatch = new HiveChord("dmin");

        HarmonicLoop loopA = new HarmonicLoop();
        loopA.setId(11);
        loopA.setRhythmicWeight(WeightClass.GOOD);
        loopA.setEqWeight(WeightClass.BAD);
        loopA.setChords(new ArrayList<HiveChord>(){{
            add(chordMatch);
        }});

        HarmonicLoop loopB = new HarmonicLoop();
        loopB.setId(22);
        loopB.setRhythmicWeight(WeightClass.BAD);
        loopB.setEqWeight(WeightClass.BAD);
        loopB.setChords(new ArrayList<HiveChord>(){{
            add(chordMissMatch);
        }});

        HarmonicLoop loopC = new HarmonicLoop();
        loopC.setId(33);
        loopC.setRhythmicWeight(WeightClass.GOOD);
        loopC.setEqWeight(WeightClass.OK);
        loopC.setChords(new ArrayList<HiveChord>(){{
            add(chordMatch);
        }});

        List<HarmonicLoop> unOrderedLoops = new ArrayList<>();
        unOrderedLoops.add(loopA);
        unOrderedLoops.add(loopB);
        unOrderedLoops.add(loopC);

        List<HarmonicLoop> correctlyOrderedLoops = new ArrayList<>();
        correctlyOrderedLoops.add(loopC);
        correctlyOrderedLoops.add(loopA);
        correctlyOrderedLoops.add(loopB);

        List<HarmonicLoop> orderedLoops = LoopSelectorHelper.orderHarmonicLoopsByWeight(unOrderedLoops);

        Assert.assertEquals(unOrderedLoops.size(), orderedLoops.size());

        for(int i=0; i<correctlyOrderedLoops.size(); i++){
            Assert.assertEquals(correctlyOrderedLoops.get(i).getId(), orderedLoops.get(i).getId());
        }
    }
}
