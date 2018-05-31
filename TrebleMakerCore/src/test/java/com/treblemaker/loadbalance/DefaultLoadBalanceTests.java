package com.treblemaker.loadbalance;

import com.treblemaker.SpringConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class DefaultLoadBalanceTests {

    @Value("${machinelearning_endpoints}")
    private String[] mlEndpoints;

    @Before
    public void setUp(){
        LoadBalancer.getInstance().initLoadBalancer(true, mlEndpoints);
    }

    @Test
    public void should_notCycleThroughUrlListWhenFirstEnabled(){

        assertThat(mlEndpoints).isNotEmpty();

        String previousEnpoint = mlEndpoints[0];

        for (int i=0; i<mlEndpoints.length*4; i++) {
            String url = LoadBalancer.getInstance().getUrl();
            assertThat(url).matches(previousEnpoint);
            previousEnpoint = url;
        }
    }
}
