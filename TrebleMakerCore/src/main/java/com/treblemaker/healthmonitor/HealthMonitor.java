package com.treblemaker.healthmonitor;

import com.treblemaker.Application;
import com.treblemaker.factory.ExecutorPoolFactory;
import com.treblemaker.scheduledevents.GlobalState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Component
public class HealthMonitor {

    private final Boolean bypassHealthmonitor;
    private String[] mlEndpoints;

    @Autowired
    public HealthMonitor(@Value("${machinelearning_endpoints}") String[] mlEndpoints, @Value("${bypass_healthmonitor}") Boolean bypassHealthmonitor) {
        this.mlEndpoints = mlEndpoints;
        this.bypassHealthmonitor = bypassHealthmonitor;
    }

    public boolean mLServicesHealthCheckOk(){

        if(getShouldBypassHealthmonitor()){
            return true;
        }

        /*
        THREAD COUNTING JUST FOR DEBUG REASONS ..
         */
        int nbThreads =  Thread.getAllStackTraces().keySet().size();

        Application.logger.debug("LOG: DEBUG : ALL THREADS : " + nbThreads);

        int nbRunning = 0;
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getState()==Thread.State.RUNNABLE) nbRunning++;
        }

        Application.logger.debug("LOG: DEBUG : RUNNING THREADS : " + nbRunning);
        /*
        THREAD COUNTING JUST FOR DEBUG REASONS ..
         */

        GlobalState.getInstance().setHealthMonitorInProgress(true);

        Collection<HealthMonitorTask> taskList = new ArrayList<>();
        List<Boolean> responseList = new ArrayList<>();

        for (String endpoint : mlEndpoints) {
            taskList.add(new HealthMonitorTask(endpoint + "/classify/synthfx?synthid=31&sixteenthfreq=0.2&eigthfreq=0.1&quarterfreq=0.0&dottedquarterfreq=0.0&halffreq=0.0&fxvol=1.0&fxtype=0.3"));
        }

        ExecutorService executorPool = ExecutorPoolFactory.getPool();
        try {
            List<Future<Boolean>> invokedTasks = executorPool.invokeAll(taskList);


            for(Future<Boolean> task : invokedTasks){
                responseList.add(task.get());
            }
        } catch (Exception e) {
            Application.logger.debug("LOG: ERROR DURING HEALTH PING !!!", e);
            Application.logger.debug("LOG: ERROR DURING HEALTH PING !!!", e);
        }
        executorPool.shutdownNow();

        try {
            while (!executorPool.awaitTermination(10, TimeUnit.SECONDS)) {
                Application.logger.debug("LOG: Awaiting completion of threads.");
            }
        } catch (InterruptedException e) {
            Application.logger.debug("LOG: ERROR AWAITING THREAD SHUTDOWN : ", e);
        }

        GlobalState.getInstance().setHealthMonitorInProgress(false);

        if(responseList.size() != mlEndpoints.length){
            return false;
        }

        for(Boolean response  : responseList){
            if(!response){
                return false;
            }
        }

        return true;
    }

    public Boolean getShouldBypassHealthmonitor(){
        return bypassHealthmonitor;
    }
}
