package com.treblemaker.integration;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.utils.Http.HttpUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class PingTests {

    public final String pythonEndpoint = "/api/ping";
    public final String javaEndpoint = "/api/softsynths/get";

    @Value("${machinelearning_endpoints}") String[] mlEndpoints;

    @Value("${api.user}")
    String apiUser;

    @Value("${api.password}")
    String apiPassword;

    @Test
    public void shouldFindEnpoints(){

        assertThat(mlEndpoints).isNotNull();
        assertThat(mlEndpoints.length).isGreaterThan(0);
    }

    @Test
    public void shouldPingAllMlPythonApps(){

        for (String endpoint : mlEndpoints) {
            HttpUtils httpUtils = new HttpUtils();
            String eqReponseString = httpUtils.sendGet(endpoint + pythonEndpoint, apiUser, apiPassword);
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
