package com.treblemaker.factory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorPoolFactory {

    public static ExecutorService getPool(){

        ExecutorService executorPool = Executors.newFixedThreadPool(6);
        return executorPool;
    }
}
