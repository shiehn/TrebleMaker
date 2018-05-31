package com.treblemaker.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.treblemaker.Application;

import java.util.Map;
import java.util.Queue;

public class LocalCache {
    public static void main(String[] args) {
        Config cfg = new Config();
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
        Map<Integer, String> mapCustomers = instance.getMap("customers");
        mapCustomers.put(1, "Joe");
        mapCustomers.put(2, "Ali");
        mapCustomers.put(3, "Avi");

        Application.logger.debug("LOG: Customer with key 1: "+ mapCustomers.get(1));
        Application.logger.debug("LOG: Map Size:" + mapCustomers.size());

        Queue<String> queueCustomers = instance.getQueue("customers");
        queueCustomers.offer("Tom");
        queueCustomers.offer("Mary");
        queueCustomers.offer("Jane");
        Application.logger.debug("LOG: First customer: " + queueCustomers.poll());
        Application.logger.debug("LOG: Second customer: "+ queueCustomers.peek());
        Application.logger.debug("LOG: Queue size: " + queueCustomers.size());
    }
}