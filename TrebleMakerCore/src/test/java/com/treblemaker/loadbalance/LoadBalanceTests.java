package com.treblemaker.loadbalance;

import com.treblemaker.SpringConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
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
        locations = "classpath:application-test.properties", properties = {"machinelearning_endpoints=a,b,c,d"})
public class LoadBalanceTests {

    @Value("${machinelearning_endpoints}")
    private String[] mlEndpoints;

    @Before
    public void setUp(){
        LoadBalancer.getInstance().initLoadBalancer(false,mlEndpoints);
    }

    @Test
    public void should_cycleThroughUrlList(){
        assertThat(mlEndpoints).isNotEmpty();
    }
}
