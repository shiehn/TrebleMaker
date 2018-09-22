package com.treblemaker.cachetestasset;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Queue;

@SpringBootApplication
public class CachetestassetApplication {

	@PostConstruct
	public void LocalCache(){
		Config cfg = new Config();
		HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
		Map<Integer, String> mapCustomers = instance.getMap("customers");
		mapCustomers.put(1, "Joe");
		mapCustomers.put(2, "Ali");
		mapCustomers.put(3, "Avi");

		System.out.println("LOG: Customer with key 1: "+ mapCustomers.get(1));
		System.out.println("LOG: Map Size:" + mapCustomers.size());

		Queue<String> queueCustomers = instance.getQueue("customers");
		queueCustomers.offer("Tom");
		queueCustomers.offer("Mary");
		queueCustomers.offer("Jane");
		System.out.println("LOG: First customer: " + queueCustomers.poll());
		System.out.println("LOG: Second customer: "+ queueCustomers.peek());
		System.out.println("LOG: Queue size: " + queueCustomers.size());
	}

	public static void main(String[] args) {
		SpringApplication.run(CachetestassetApplication.class, args);
	}
}
