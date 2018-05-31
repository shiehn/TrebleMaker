package com.treblemaker.loadbalance;

import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalancer {

    private static LoadBalancer instance = null;

    protected LoadBalancer(){}

    public static LoadBalancer getInstance(){
        if(instance == null){
            synchronized (LoadBalancer.class){
                if (instance == null){
                    instance = new LoadBalancer();
                }
            }
        }
        return instance;
    }

    private String[] mlEndpoints;
    private boolean useOnlyFirstMachinelearnEndpoint;
    private volatile AtomicInteger currentIndex = null;

    public void initLoadBalancer(boolean useOnlyFirstMachinelearnEndpoint, String[] mlEndpoints){
        this.currentIndex = new AtomicInteger(0);
        this.mlEndpoints = mlEndpoints;
        this.useOnlyFirstMachinelearnEndpoint = useOnlyFirstMachinelearnEndpoint;
    }

    public String getUrl() {

        if(useOnlyFirstMachinelearnEndpoint){
            return mlEndpoints[0];
        }

        int value = currentIndex.intValue();
        if(value > (mlEndpoints.length -  1)){

            String mlEndpoint = mlEndpoints[0];
            currentIndex.set(1);
            return mlEndpoint;
        }else{

            String mlEndpoint = mlEndpoints[value];
            currentIndex.incrementAndGet();
            return mlEndpoint;
        }
    }
}
