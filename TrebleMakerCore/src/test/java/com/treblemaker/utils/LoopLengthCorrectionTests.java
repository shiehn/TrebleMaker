package com.treblemaker.utils;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.utils.interfaces.IAudioUtils;
import com.treblemaker.utils.loopcorrection.LoopCorrectionBase;
import junit.framework.Assert;
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
public class LoopLengthCorrectionTests extends TestCase {

    @Autowired
    private IAudioUtils audioUtils;

    @Autowired
    private LoopCorrectionBase loopCorrectionBase;

    @Autowired
    public AppConfigs appConfigs;

    @Test
    public void shouldTrimBeatLoop() throws Exception {

        String sourcePath = Paths.get(appConfigs.MOCK_AUDIO_PATH,"96BpmLengthTrimTest.wav").toString();
        String targetPath = Paths.get(appConfigs.MOCK_AUDIO_PATH,"96BpmLengthTest_TrimResult.wav").toString();
        int bpm = 100;

        BeatLoop beatLoop = new BeatLoop();
        beatLoop.setBpm(bpm);
        beatLoop.setFilePath(sourcePath);

        float sourceLength = audioUtils.getAudioLength(sourcePath);

        try {
            loopCorrectionBase.correctLoopLength(sourcePath, targetPath, beatLoop);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }

        float targetLength = audioUtils.getAudioLength(targetPath);

        Assert.assertTrue(targetLength < sourceLength);
        Assert.assertEquals(4.8002496f, targetLength);

        Assert.assertEquals(4.8f, beatLoop.getAudioLength());
        Assert.assertEquals(2,beatLoop.getBarCount().intValue());
    }

    @Test
    public void shouldConcateShimToBeatLoop() throws Exception {

        String sourcePath = Paths.get(appConfigs.MOCK_AUDIO_PATH,"96BpmLengthShimTest.wav").toString();
        String targetPath = Paths.get(appConfigs.MOCK_AUDIO_PATH,"96BpmLengthShimTest_ShimResult.wav").toString();
        int bpm = 75;

        BeatLoop beatLoop = new BeatLoop();
        beatLoop.setBpm(bpm);
        beatLoop.setFilePath(sourcePath);

        float sourceLength = audioUtils.getAudioLength(sourcePath);

        try {
            loopCorrectionBase.correctLoopLength(sourcePath, targetPath, beatLoop);
        } catch (Exception e) {
            Assert.assertTrue(false);
        }

        //float sourceLength = audioUtils.getAudioLength(sourcePath);
        //float targetLength = audioUtils.getAudioLength(sourcePath);

//        Assert.assertTrue(targetLength > sourceLength);
//        Assert.assertEquals(6.3995237f, targetLength);

        Assert.assertEquals(6.4f, beatLoop.getAudioLength());
        Assert.assertEquals(2,beatLoop.getBarCount().intValue());
    }
}
