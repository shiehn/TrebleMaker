package com.treblemaker.weighters;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.TestBase;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.weighters.enums.WeightClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class WeightableLoopTests extends TestBase {

    @Test
    public void ShouldReturnTotalWeightOf3(){

        HarmonicLoop harmonicLoop = new HarmonicLoop();
        harmonicLoop.setVerticalWeight(WeightClass.GOOD);
        harmonicLoop.setRhythmicWeight(WeightClass.OK);
        harmonicLoop.setTimeseriesWeight(WeightClass.GOOD);

        int totalWeight = harmonicLoop.getTotalWeight();
        Assert.assertEquals (3, totalWeight);
    }

    @Test
    public void ShouldReturnTotalWeightOf2(){

        HarmonicLoop harmonicLoop = new HarmonicLoop();
        harmonicLoop.setVerticalWeight(WeightClass.GOOD);
        harmonicLoop.setRhythmicWeight(WeightClass.BAD);
        harmonicLoop.setTimeseriesWeight(WeightClass.GOOD);

        int totalWeight = harmonicLoop.getTotalWeight();
        Assert.assertEquals (2, totalWeight);
    }

    @Test
    public void ShouldReturnTotalWeightOf1(){

        HarmonicLoop harmonicLoop = new HarmonicLoop();
        harmonicLoop.setVerticalWeight(WeightClass.BAD);
        harmonicLoop.setRhythmicWeight(WeightClass.BAD);
        harmonicLoop.setTimeseriesWeight(WeightClass.GOOD);

        int totalWeight = harmonicLoop.getTotalWeight();
        Assert.assertEquals (1, totalWeight);
    }

    @Test
    public void ShouldReturnDefaultWeightOf1(){

        HarmonicLoop harmonicLoop = new HarmonicLoop();
        harmonicLoop.setVerticalWeight(WeightClass.BAD);
        harmonicLoop.setRhythmicWeight(WeightClass.BAD);
        harmonicLoop.setTimeseriesWeight(WeightClass.BAD);

        int totalWeight = harmonicLoop.getTotalWeight();
        Assert.assertEquals (1, totalWeight);
    }
}
