package com.treblemaker.renderers;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.TestBase;
import com.treblemaker.model.*;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.dal.interfaces.IBeatLoopsDal;
import com.treblemaker.dal.interfaces.IHarmonicLoopsDal;
import com.treblemaker.generators.interfaces.IShimGenerator;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.utils.interfaces.IAudioUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class BaseRendererTest extends TestBase {

    @Autowired
    private IShimGenerator shimGenerator;

    @Autowired
    private IAudioUtils audioUtils;

    @Autowired
    private IHarmonicLoopsDal harmonicLoopsDal;

    @Autowired
    private IBeatLoopsDal beatLoopsDal;

    @Autowired
    public AppConfigs appConfigs;

    @Test
    public void ShouldConcatinateToCorrectLength() {
        try {
            int numOfFiles = 15;
            float concateItemLength = 7.53f;
            String concateItem = appConfigs.MOCK_AUDIO_PATH + "concateTestItem.wav";
            String concateOutput = appConfigs.MOCK_AUDIO_PATH + "concateTestOutput.wav";

            shimGenerator.generateSilence(concateItemLength, concateItem, 0);

            BaseRenderer baseRenderer = new BaseRenderer();

            List<String> sourceFilesList = new ArrayList<String>();

            for (int i = 0; i < numOfFiles; i++) {
                sourceFilesList.add(concateItem);
            }

            baseRenderer.concatenateFiles(sourceFilesList, concateOutput);

            float expectedLength = concateItemLength * 15.0f;

            float audioLength = audioUtils.getAudioLength(concateOutput);

            assertEquals(expectedLength, audioLength, 0.01f);

        } catch (Exception e) {
            fail("FAILED");
        }
    }

    @Test
    public void ShouldConcatinateMultipleLoopsAndShims() throws Exception {

        double shimLengthOne = 15.4;
        double shimLengthTwo = 29.7;

        String shimToGenerateOne = appConfigs.MOCK_AUDIO_PATH + "shimConcateTestOne.wav";
        String shimToGenerateTwo = appConfigs.MOCK_AUDIO_PATH + "shimConcateTestTwo.wav";

        String targetOutput = appConfigs.MOCK_AUDIO_PATH + "concateTest.wav";

        File shimFileOne = new File(shimToGenerateOne);
        if(shimFileOne != null || shimFileOne.exists()){
            deleteFolderAndContents(shimFileOne);
        }

        File shimFileTwo = new File(shimToGenerateTwo);
        if(shimFileTwo != null ||  shimFileTwo.exists()){
            deleteFolderAndContents(shimFileTwo);
        }

        List<BeatLoop> beatLoops = beatLoopsDal.findByNormalizedLength(1);
        List<HarmonicLoop> harmonicLoops = harmonicLoopsDal.findByNormalizedLength(1);

        BeatLoop beatLoop = beatLoops.get((new Random()).nextInt(beatLoops.size()));
        HarmonicLoop harmonicLoop = harmonicLoops.get((new Random()).nextInt(harmonicLoops.size()));

        shimGenerator.generateSilence(shimLengthOne, shimToGenerateOne, 1);
        shimGenerator.generateSilence(shimLengthTwo, shimToGenerateTwo, 2);

        System.out.println("MonoStereo = " + audioUtils.isMonoOrStereo(shimToGenerateTwo));

        List<String> sourceFilesList = new ArrayList<String>();
        sourceFilesList.add(shimToGenerateOne);
        sourceFilesList.add(shimToGenerateTwo);
        sourceFilesList.add(beatLoop.getFilePath(appConfigs) + beatLoop.getFileName());
        sourceFilesList.add(harmonicLoop.getFilePath(appConfigs) + harmonicLoop.getFileName());

        BaseRenderer baseRenderer = new BaseRenderer();
        baseRenderer.concatenateFiles(sourceFilesList, targetOutput);

        float targetLength = (float)shimLengthOne + (float)shimLengthTwo + beatLoop.getAudioLength() + harmonicLoop.getAudioLength();
        float actualLength = audioUtils.getAudioLength(targetOutput);

        shimFileOne = new File(shimToGenerateOne);
        if(shimFileOne != null || shimFileOne.exists()){
            deleteFolderAndContents(shimFileOne);
        }

        shimFileTwo = new File(shimToGenerateTwo);
        if(shimFileTwo != null ||  shimFileTwo.exists()){
            deleteFolderAndContents(shimFileTwo);
        }

        File target = new File(targetOutput);
        if(target != null ||  target.exists()){
            deleteFolderAndContents(target);
        }

        Assert.assertEquals(targetLength,actualLength,0.01f);
    }
}
