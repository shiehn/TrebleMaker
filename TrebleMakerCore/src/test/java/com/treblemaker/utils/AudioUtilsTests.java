package com.treblemaker.utils;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.utils.interfaces.IAudioUtils;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
		locations = "classpath:application-test.properties")
public class AudioUtilsTests extends TestCase {

	@Autowired
	private IAudioUtils audioUtils;

	@Autowired
	public AppConfigs appConfigs;

	@Test
	public void ShouldGetAudioLength() {

		try {

			String audoFile = Paths.get(appConfigs.getApplicationRoot(), appConfigs.MOCK_AUDIO_PATH,"9_24.wav").toString();

			float audioLength = audioUtils.getAudioLength(audoFile);

			assertEquals((float) 9.24, audioLength, 0.01f);

			audoFile = Paths.get(appConfigs.getApplicationRoot(),appConfigs.MOCK_AUDIO_PATH, "3_898.aif").toString();

			audioLength = audioUtils.getAudioLength(audoFile);

			assertEquals((float) 3.898, (float) audioLength, 0.01f);

		} catch (Exception e) {
			assertEquals(false, true);
		}
	}

	@Test
	public void BeatShouldBeCorrectLength() {
		try {
			float secondsInBar = LoopUtils.getBeatsInSeconds(80, 4);

			String audoFile = Paths.get(appConfigs.getApplicationRoot(),appConfigs.MOCK_AUDIO_PATH, "080_too-much-pot-groove.aif").toString();

			float audioLength;

			audioLength = audioUtils.getAudioLength(audoFile);

			assertEquals(secondsInBar, audioLength, 0.01f);
			
		} catch (Exception e) {
			assertEquals(false, true);
		}
	}

}
