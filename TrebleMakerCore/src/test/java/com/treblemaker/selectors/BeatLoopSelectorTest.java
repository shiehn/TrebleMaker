package com.treblemaker.selectors;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.selectors.interfaces.IBeatLoopSelector;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class BeatLoopSelectorTest extends TestCase {

    @Autowired
    private IBeatLoopSelector beatLoopAltSelector;

    @Test
    public void ShouldSelectByWeightAndBarCount(){

        BeatLoop beatLoopAltOptionOne = new BeatLoop();
        beatLoopAltOptionOne.setBarCount(4);
        beatLoopAltOptionOne.setFileName("beatLoopAltOptionOne");

        BeatLoop beatLoopAltOptionTwo = new BeatLoop();
        beatLoopAltOptionTwo.setBarCount(2);
        beatLoopAltOptionTwo.setFileName("beatLoopAltOptionTwo");

        BeatLoop beatLoopAltOptionThree = new BeatLoop();
        beatLoopAltOptionThree.setBarCount(3);
        beatLoopAltOptionThree.setFileName("beatLoopAltOptionThree");

        ProgressionUnitBar progressionUnitBar = new ProgressionUnitBar();
        progressionUnitBar.getBeatLoopAltOptions().add(beatLoopAltOptionOne);
        progressionUnitBar.getBeatLoopAltOptions().add(beatLoopAltOptionTwo);
        progressionUnitBar.getBeatLoopAltOptions().add(beatLoopAltOptionThree);

        BeatLoop beatSelection = (BeatLoop) beatLoopAltSelector.selectByWeightAndBarCount(progressionUnitBar.getBeatLoopAltOptions(), 2);

        Assert.assertTrue(beatSelection.getFileName().equalsIgnoreCase(beatLoopAltOptionTwo.getFileName()));
    }

}
