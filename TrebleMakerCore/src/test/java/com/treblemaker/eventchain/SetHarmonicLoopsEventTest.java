package com.treblemaker.eventchain;

import com.treblemaker.SpringConfiguration;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"return_queue_early_for_tests=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999", "spring.datasource.url=jdbc:mysql://localhost:3306/hivecomposedb", "spring.datasource.username=root", "spring.datasource.password=redrobes79D"})
public class SetHarmonicLoopsEventTest extends TestCase {

    @Ignore
    @Test
    public void ShouldSetHarmonicLoops(){
        Assert.fail("Not yet implemented");
    }
}