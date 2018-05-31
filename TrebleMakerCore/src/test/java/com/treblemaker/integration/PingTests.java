package com.treblemaker.integration;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.utils.Http.HttpUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class PingTests {

    public final String pythonEndpoint = "/api/ping";
    public final String javaEndpoint = "/api/softsynths/get";

    @Value("${machinelearning_endpoints}") String[] mlEndpoints;

    @Test
    public void shouldFindEnpoints(){

        assertThat(mlEndpoints).isNotNull();
        assertThat(mlEndpoints.length).isGreaterThan(0);
    }

    @Test
    public void shouldPingAllMlPythonApps(){

        for (String endpoint : mlEndpoints) {
            HttpUtils httpUtils = new HttpUtils();
            String eqReponseString = httpUtils.sendGet(endpoint + pythonEndpoint);
            assertThat(eqReponseString).contains("ok");
        }
    }

    @Test
    public void shouldPingAllMlJavaApps(){

        for (String endpoint : mlEndpoints) {
            HttpUtils httpUtils = new HttpUtils();
            String eqReponseString = httpUtils.sendGet(endpoint.replace(":5000",":8080" + javaEndpoint), "treble","maker");
            assertThat(eqReponseString).contains("tubular_bell_chord_serrano_");
        }
    }
}
