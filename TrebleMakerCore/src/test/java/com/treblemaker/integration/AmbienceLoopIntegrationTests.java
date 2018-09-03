package com.treblemaker.integration;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.dal.interfaces.IAmbienceLoopsDal;
import com.treblemaker.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class AmbienceLoopIntegrationTests {

    @Autowired
    private IAmbienceLoopsDal ambienceLoopsDal;

    @Autowired
    public AppConfigs appConfigs;

    @Test
    public void allLoopsShouldExistOnDisk(){

        List<AmbienceLoop> ambienceLoops = ambienceLoopsDal.findAll();

        ambienceLoops.forEach(ambienceLoop -> {
            File file = new File(appConfigs.getAmbienceFullPath(ambienceLoop));
            boolean loopExisits = (file != null && file.exists());

            if(!loopExisits){
                System.out.println("@@@@@!!!!! AMBI LOOP MISSING : " + appConfigs.getAmbienceFullPath(ambienceLoop) + " !!!!!@@@@@");
            }

            assertThat(loopExisits).isTrue();
        });
    }
}
