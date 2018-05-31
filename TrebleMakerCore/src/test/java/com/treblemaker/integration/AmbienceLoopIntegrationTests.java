package com.treblemaker.integration;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.dal.interfaces.IAmbienceLoopsDal;
import com.treblemaker.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true","return_queue_early_for_tests=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999", "spring.datasource.url=jdbc:mysql://localhost:3306/hivecomposedb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "spring.datasource.username=root", "spring.datasource.password=redrobes79D"})
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
