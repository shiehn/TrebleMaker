package com.treblemaker.utils;

import com.treblemaker.SpringConfiguration;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
		locations = "classpath:application-test.properties")
public class ConverterTests extends TestCase {

	@Test
	public void ShouldCalculateCorrectSecondsInBar() {

		float secondsInBeats = LoopUtils.getBeatsInSeconds(60, 60);
				
		assertEquals((float)60.0, secondsInBeats, 0.0f);
	} 
}


