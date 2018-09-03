package com.treblemaker.healthmonitor;

import com.treblemaker.SpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class HealthMonitorBypassTests {

    @Autowired
    private HealthMonitor healthMonitor;

    @Test
    public void should_allowHealthCheckByPass(){

        assertThat(healthMonitor.getShouldBypassHealthmonitor()).isTrue();

//        final long startTime = System.currentTimeMillis();
//        boolean checkResult = healthMonitor.mLServicesHealthCheckOk();
//        final long endTime = System.currentTimeMillis();
//        long executionTime = endTime - startTime;
//
//        assertThat(checkResult).isTrue();
//        assertThat(executionTime).isLessThan(5000);
    }
}
