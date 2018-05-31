package com.treblemaker.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class TestCounter {

    private static TestCounter instance = null;

    public AtomicInteger count = new AtomicInteger();

    protected TestCounter(){}

    public static TestCounter getInstance(){
        if(instance == null){
            synchronized (TestCounter.class){
                if(instance == null){
                    instance = new TestCounter();
                }
            }
        }
        return instance;
    }
}
