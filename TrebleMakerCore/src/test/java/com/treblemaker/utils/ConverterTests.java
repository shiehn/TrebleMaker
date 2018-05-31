package com.treblemaker.utils;

import com.treblemaker.SpringConfiguration;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class ConverterTests extends TestCase {

	@Test
	public void ShouldCalculateCorrectSecondsInBar() {

		float secondsInBeats = LoopUtils.getBeatsInSeconds(60, 60);
				
		assertEquals((float)60.0, secondsInBeats, 0.0f);
	} 
}


