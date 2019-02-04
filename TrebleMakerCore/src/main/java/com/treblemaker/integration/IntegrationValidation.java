package com.treblemaker.integration;

import com.treblemaker.Application;
import com.treblemaker.loadbalance.LoadBalancer;
import com.treblemaker.utils.Http.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class IntegrationValidation {

    @Value("${api.user}")
    String apiUser;

    @Value("${api.password}")
    String apiPassword;

    String PING_ENDPOINT = "PING_ENDPOINT";
    String CLASSIFY_BASS = "CLASSIFY_BASS_ENDPOINT";
    String CLASSIFY_ARPEGGIO = "CLASSIFY_ARPEGGIO_ENDPOINT";
    String CLASSIFY_BEATLOOP = "CLASSIFY_BEATLOOP_ENDPOINT";
    String CLASSIFY_HARMONIC_LOOP = "CLASSIFY_HARMONIC_LOOP_ENDPOINT";

    String pingEndpoint = "/api/track";
    String classifyBass = "/classify/bass?bass=0.2,0.33,0,0,0.4,0.33,0,0,0.4,1.0,1,0,0.0,0.0,0,0,0.4,0.33,1,0,0.4,0.66,0,0,0.4,0.66,1,0,0.4,1.0,0,0,0.0,0.0,1,0,0.0,0.0,0,0,0.0,0.0,1,0,0.0,0.0,0,0,0.0,0.0,1,0,0.0,0.0,0,0,0.0,0.0,1,0,0.0,0.0,0,0,1.0,0.33,1,0,1.0,0.66,0,0,1.0,0.66,1,0,1.0,1.0,0,0,0.2,0.33,1,0,0.2,0.66,0,0,0.2,1.0,1,0,0.0,0.0,0,0,0.0,0.0,1,0,0.0,0.0,0,0,0.0,0.0,1,0,0.0,0.0,0,0,0.2,0.33,1,0,0.2,0.66,0,0,0.2,0.66,1,0,0.2,1.0,0,0";
    String classifyArpeggio = "/classify/arpeggio?arpeggio=0.0,0.3,0.2,0.0,0.3,0.2,0.4,0.3,0.0,0.0,0.0,0.0,0.0,0.3,0.0,0.0,0.0,0.0,0.0,0.6,0.2,0.0,0.0,0.0,0.2,0.3,0.0,0.6,0.0,0.0,1.0,0.6,1.0,1.0,0.3,1.0,1.0,0.3,0.0,0.0,0.3,0.2,0.0,0.6,0.2,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.3,1.0,0.4,0.6,1.0,0.0,0.0,0.0,0.0,0.3,0.0,0.0,0.3,0.2,0.0,0.3,0.0,0.0,0.0,0.0,0.2,0.3,0.0,0.6,0.3,1.0,1.0,0.6,1.0,1.0,0.0,0.0,1.0,0.3,0.0,0.0,0.3,0.2,0.0,0.3,0.0,0.0,0.0,0.0\n";
    String classifyBeatLoop = "/classify/beatalt/79/92/527/107/53/53/148";
    String classifyHarmonicLoop = "/classify/harmalt/73/527/95/53/53/148";

    public void validateEndpoints(){
        Map<String, String> nameToRequest = new HashMap<>();
        nameToRequest.put(PING_ENDPOINT, pingEndpoint);
        nameToRequest.put(CLASSIFY_BASS, classifyBass);
        nameToRequest.put(CLASSIFY_ARPEGGIO, classifyArpeggio);
        nameToRequest.put(CLASSIFY_BEATLOOP, classifyBeatLoop);
        nameToRequest.put(CLASSIFY_HARMONIC_LOOP, classifyHarmonicLoop);

        for (Map.Entry<String, String> entry : nameToRequest.entrySet())
        {
            boolean ENDPOINT_UP = false;
            while(!ENDPOINT_UP) {
                Application.logger.debug("*******************************************************************");
                Application.logger.debug("LOG: TESTING ENDPOINT INTEGRATION FOR: " + entry.getKey());
                Application.logger.debug("*******************************************************************");

                System.out.println(entry.getKey() + "/" + entry.getValue());

                String url = LoadBalancer.getInstance().getUrl();

                HttpUtils httpUtils = new HttpUtils();
                String response = httpUtils.sendGet(url + entry.getValue(), apiUser, apiPassword);

                Application.logger.debug("*******************************************************************");
                if (response.equalsIgnoreCase("error")) {
                    Application.logger.debug("LOG: ENDPOINT INTEGRATION FOR: " + entry.getKey() + " ERROR!!!!");
                } else {
                    Application.logger.debug("LOG: ENDPOINT INTEGRATION FOR: " + entry.getKey() + " SUCCESS!");
                    ENDPOINT_UP = true;
                }
                Application.logger.debug("*******************************************************************");

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
