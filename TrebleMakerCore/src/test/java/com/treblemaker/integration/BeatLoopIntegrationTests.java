package com.treblemaker.integration;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.TestBase;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.dal.interfaces.IBeatLoopsDal;
import com.treblemaker.dal.interfaces.IHarmonicLoopsDal;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.utils.LoopUtils;
import com.treblemaker.utils.interfaces.IAudioUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.treblemaker.model.*;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class BeatLoopIntegrationTests extends TestBase {

    @Autowired
    private IBeatLoopsDal beatLoopsDal;

    @Autowired
    private IHarmonicLoopsDal harmonicLoopsDal;

    @Autowired
    private IAudioUtils audioUtils;

    @Autowired
    public AppConfigs appConfigs;

    @Test
    public void AllLoopsShouldExisitOnDisk(){

        List<BeatLoop> beatLoops = beatLoopsDal.findByNormalizedLength(HarmonicLoop.ALREADY_NORMALIZED);

        beatLoops.forEach(beatLoop -> {

            File file = new File(appConfigs.getBeatLoopfullPath(beatLoop));
            boolean loopExisits = (file != null && file.exists());

            if(!loopExisits){
                System.out.println("@@@@@!!!!! BEAT LOOP MISSING : " + appConfigs.getBeatLoopfullPath(beatLoop) + " !!!!!@@@@@");
            }

            assertThat(loopExisits).isTrue();
        });
    }

    @Test
    public void AllLoopsShouldBeCorrectLength(){

        List<BeatLoop> beatLoops = beatLoopsDal.findByNormalizedLength(HarmonicLoop.ALREADY_NORMALIZED);

        beatLoops.forEach(beatLoop -> {

            File file = new File(appConfigs.getBeatLoopfullPath(beatLoop));
            boolean loopExisits = (file != null && file.exists());

            if(loopExisits){

                float actualBeatLength = audioUtils.getAudioLength(appConfigs.getBeatLoopfullPath(beatLoop));

                float targetBeatLength = beatLoop.getBarCount() * LoopUtils.getSecondsInBar(beatLoop.getBpm());

                Assert.assertEquals(beatLoop.getAudioLength(), targetBeatLength, 0.02);

                Assert.assertEquals(targetBeatLength, actualBeatLength, 0.05);
            }
        });
    }

    @Test
    public void LOG_BEATLOOP_MONO_OR_STEREO() {

        List<BeatLoop> beatLoops = beatLoopsDal.findByNormalizedLength(HarmonicLoop.ALREADY_NORMALIZED);

        beatLoops.forEach(beatLoop -> {

            File file = new File(appConfigs.getBeatLoopfullPath(beatLoop));
            boolean loopExisits = (file != null && file.exists());

            if(loopExisits){

                String monoOrStereo = (audioUtils.isMonoOrStereo(appConfigs.getBeatLoopfullPath(beatLoop)) == 1) ? "MONO" : "STEREO";
                System.out.println(audioUtils.isMonoOrStereo(appConfigs.getBeatLoopfullPath(beatLoop)));
                System.out.println(beatLoop.getFileName() + " : " + monoOrStereo);
            }
        });
    }
}
