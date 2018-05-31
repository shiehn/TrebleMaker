package com.treblemaker.healthmonitor;

import com.treblemaker.SpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"bypass_healthmonitor=true","return_queue_early_for_tests=true","queue_scheduled_interval=8999999","queue_scheduled_start_delay=8999999","spring.datasource.url=jdbc:mysql://localhost:3306/hivecomposedb","spring.datasource.username=root","spring.datasource.password=redrobes79D"})
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
