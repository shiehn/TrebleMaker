package com.treblemaker.integration;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.dal.interfaces.IHarmonicLoopsDal;
import com.treblemaker.model.*;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class HarmonicLoopIntegrationTests {

    @Autowired
    private IHarmonicLoopsDal harmonicLoopsDal;

    @Autowired
    private IAudioUtils audioUtils;

    @Autowired
    public AppConfigs appConfigs;

    @Test
    public void AllLoopsShouldExisitOnDisk(){
        List<HarmonicLoop> harmonicLoops = harmonicLoopsDal.findByNormalizedLength(HarmonicLoop.ALREADY_NORMALIZED);
        harmonicLoops.forEach(harmonicLoop -> {
            File file = new File(appConfigs.getHarmonicLoopsFullPath(harmonicLoop));
            boolean loopExisits = (file != null && file.exists());

            if(!loopExisits){
                System.out.println("@@@@@!!!!! HARMONIC LOOP MISSING : " + appConfigs.getHarmonicLoopsFullPath(harmonicLoop) + " !!!!!@@@@@");
            }

            Assert.assertTrue(loopExisits);
        });
    }

    @Test
    public void LOG_HARMONICLOOP_MONO_OR_STEREO() {
        List<HarmonicLoop> harmonicLoops = harmonicLoopsDal.findByNormalizedLength(HarmonicLoop.ALREADY_NORMALIZED);
        harmonicLoops.forEach(harmonicLoop -> {
            File file = new File(appConfigs.getHarmonicLoopsFullPath(harmonicLoop));
            boolean loopExisits = (file != null && file.exists());

            if(loopExisits){
                String monoOrStereo = (audioUtils.isMonoOrStereo(appConfigs.getHarmonicLoopsFullPath(harmonicLoop)) == 1) ? "MONO" : "STEREO";
                System.out.println(audioUtils.isMonoOrStereo(appConfigs.getHarmonicLoopsFullPath(harmonicLoop)));
                System.out.println(harmonicLoop.getFileName() + " : " + monoOrStereo);
            }
        });
    }
}
