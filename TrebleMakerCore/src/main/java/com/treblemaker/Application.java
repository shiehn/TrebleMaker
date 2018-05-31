package com.treblemaker;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import javax.annotation.PostConstruct;

@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
public class Application {

    public static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(Application.class).web(false).run(args);
    }

    public static HazelcastInstance client;

    @Value("${connect_to_cache}")
    private boolean connectToCache;

    @Value("${cache_connection}")
    private String CACHE_CONN;

    @PostConstruct
    public void cacheConfig() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.addAddress(CACHE_CONN);
        client = HazelcastClient.newHazelcastClient(clientConfig);
    }
}
