package com.treblemaker.generators;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.TestBase;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.dal.interfaces.IBeatLoopsDal;
import com.treblemaker.generators.interfaces.IShimGenerator;
import com.treblemaker.utils.interfaces.IAudioUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class ShimGeneratorTest extends TestBase {

    @Autowired
    private IBeatLoopsDal iBeatLoopsDal;

    @Autowired
    private IShimGenerator shimGenerator;

    @Autowired
    private IAudioUtils audioUtils;

    @Autowired
    public AppConfigs appConfigs;

    @Test
    public void ShouldCreateSampleOfCoorectLengthMono() {
        try {
            float shimLength = 23.53f;
            String shimPath = appConfigs.getMockDataDir() + "mockAudio\\shimTest.wav";

            File file = new File(shimPath);
            if(file != null && file.exists()){
                deleteFolderAndContents(file);
            }

            shimGenerator.generateSilence(shimLength, shimPath, 1);

            float audioLength;

            audioLength = audioUtils.getAudioLength(shimPath);

            file = new File(shimPath);
            if(file != null && file.exists()){
                deleteFolderAndContents(file);
            }

            assertEquals(shimLength, audioLength, 0.01f);

        } catch (Exception e) {
            assertEquals(false, true);
        }
    }

    @Test
    public void ShouldCreateSampleOfCoorectLengthStereo() {
        try {
            float shimLength = 23.53f;
            String shimPath = appConfigs.getMockDataDir() + "mockAudio\\shimTest.wav";

            File file = new File(shimPath);
            if(file != null && file.exists()){
                deleteFolderAndContents(file);
            }

            shimGenerator.generateSilence(shimLength, shimPath, 2);

            float audioLength;

            audioLength = audioUtils.getAudioLength(shimPath);

            file = new File(shimPath);
            if(file != null && file.exists()){
                deleteFolderAndContents(file);
            }

            assertEquals(shimLength, audioLength, 0.01f);

        } catch (Exception e) {
            assertEquals(false, true);
        }
    }

}
