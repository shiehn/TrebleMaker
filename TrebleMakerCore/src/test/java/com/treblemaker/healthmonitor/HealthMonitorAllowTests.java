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
        locations = "classpath:application-test.properties", properties = {"bypass_healthmonitor=false"})
public class HealthMonitorAllowTests {

    @Autowired
    private HealthMonitor healthMonitor;

    @Test
    public void should_allowHealthCheckToOccur() {
        assertThat(healthMonitor.getShouldBypassHealthmonitor()).isFalse();
    }
}
