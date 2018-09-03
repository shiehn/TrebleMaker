package com.treblemaker.renderers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.treblemaker.SpringConfiguration;
import com.treblemaker.TestBase;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.model.rendertransports.HarmonicLoopRenderTransport;
import com.treblemaker.renderers.interfaces.IHarmonicLoopRenderer;
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

@Ignore
@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class HarmonicLoopRendererTest extends TestBase {

    @Autowired
    private IHarmonicLoopRenderer harmonicLoopRenderer;

    @Autowired
    private IHarmonicLoopRenderer harmonicLoopAltRenderer;

    @Autowired
    private IAudioUtils audioUtils;

    @Autowired
    public AppConfigs appConfigs;

    @Test
    public void ShouldRenderHarmonicLoopsToCorrectLength() throws Exception {

        String targetPath = appConfigs.getApplicationRoot();
        String fileName = "harmonicLoopRenderTest.wav";

        File targetFile = new File(targetPath + fileName);
        if(targetFile != null && targetFile.exists()){
            deleteFolderAndContents(targetFile);
        }

        ObjectMapper mapper = new ObjectMapper();
        HarmonicLoopRenderTransport harmonicLoopRenderTransport = mapper.readValue(new File(appConfigs.getMockDataDir() + "render\\harmonicLoopsRenderMock.json"), HarmonicLoopRenderTransport.class);

        harmonicLoopRenderer.renderHarmonicLoops(targetPath, fileName, harmonicLoopRenderTransport);

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
    public void ShouldRenderHarmonicLoopsAltToCorrectLength() throws Exception {

        String targetPath = appConfigs.getApplicationRoot();
        String fileName = "harmonicLoopAltRenderTest.wav";

        File targetFile = new File(targetPath + fileName);
        if(targetFile != null && targetFile.exists()){
            deleteFolderAndContents(targetFile);
        }

        ObjectMapper mapper = new ObjectMapper();
        HarmonicLoopRenderTransport harmonicLoopRenderTransport = mapper.readValue(new File(appConfigs.getMockDataDir() + "render\\harmonicLoopsAltRenderMock.json"), HarmonicLoopRenderTransport.class);

        harmonicLoopAltRenderer.renderHarmonicLoops(targetPath, fileName, harmonicLoopRenderTransport);

        File outPutFile = new File(targetPath + fileName);

        int bpm = 80;
        int barCount = 32;
        float audioLengthInSeconds = audioUtils.getAudioLength(targetPath + fileName);
        float targetTrackLengthInSeconds = LoopUtils.getSecondsInBar(bpm) * barCount;

        //CLEAN_UP
        if(outPutFile != null && outPutFile.exists()){
            deleteFolderAndContents(targetFile);
        }

        Assert.assertEquals(targetTrackLengthInSeconds, audioLengthInSeconds, 0.5f);
    }
}