package com.treblemaker.renderers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.treblemaker.SpringConfiguration;
import com.treblemaker.TestBase;
import com.treblemaker.model.rendertransports.BeatLoopRenderTransport;
import com.treblemaker.renderers.interfaces.IBeatLoopRenderer;
import com.treblemaker.utils.LoopUtils;
import com.treblemaker.utils.interfaces.IAudioUtils;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

@Ignore
@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class BeatLoopRendererTest extends TestBase {

    @Autowired
    private IBeatLoopRenderer beatLoopRenderer;

    @Autowired
    private IBeatLoopRenderer beatLoopAltRenderer;

    @Autowired
    private IAudioUtils audioUtils;

    @Test
    public void ShouldRenderBeatLoopsToCorrectLength() throws IOException {

        String targetPath = appConfigs.getApplicationRoot();
        String fileName = "beatLoopRenderTest.wav";

        File targetFile = new File(targetPath + fileName);
        if(targetFile != null && targetFile.exists()){
            deleteFolderAndContents(targetFile);
        }

        ObjectMapper mapper = new ObjectMapper();
        BeatLoopRenderTransport beatLoopRenderTransport = mapper.readValue(new File(appConfigs.getMockDataDir() + "render\\beatLoopsRenderMock.json"), BeatLoopRenderTransport.class);

        beatLoopRenderer.renderRhythm("FIX THIS!!!!", targetPath, fileName, beatLoopRenderTransport);

        File outPutFile = new File(targetPath + fileName);

        int bpm = 80;
        int barCount = 32;
        float audioLengthInSeconds = audioUtils.getAudioLength(targetPath + fileName);
        float targetTrackLengthInSeconds = LoopUtils.getSecondsInBar(bpm) * barCount;

        //CLEAN_UP
        if(outPutFile != null && outPutFile.exists()){
            deleteFolderAndContents(targetFile);
        }

        Assert.assertEquals(targetTrackLengthInSeconds, audioLengthInSeconds, 0.02f);
    }

    @Test
    public void ShouldRenderBeatLoopsAltToCorrectLength() throws IOException {

        String targetPath = appConfigs.getApplicationRoot();
        String fileName = "beatLoopAltRenderTest.wav";

        File targetFile = new File(targetPath + fileName);
        if(targetFile != null && targetFile.exists()){
            deleteFolderAndContents(targetFile);
        }

        ObjectMapper mapper = new ObjectMapper();
        BeatLoopRenderTransport beatLoopRenderTransport = mapper.readValue(new File(appConfigs.getMockDataDir() + "render\\beatLoopsAltRenderMock.json"), BeatLoopRenderTransport.class);

        beatLoopAltRenderer.renderRhythm("FIX THIS!!!", targetPath, fileName, beatLoopRenderTransport);

        File outPutFile = new File(targetPath + fileName);

        int bpm = 80;
        int barCount = 32;
        float audioLengthInSeconds = audioUtils.getAudioLength(targetPath + fileName);
        float targetTrackLengthInSeconds = LoopUtils.getSecondsInBar(bpm) * barCount;

        //CLEAN_UP
        if(outPutFile != null && outPutFile.exists()){
            deleteFolderAndContents(targetFile);
        }

        Assert.assertEquals(targetTrackLengthInSeconds, audioLengthInSeconds, 0.02f);
    }
}
