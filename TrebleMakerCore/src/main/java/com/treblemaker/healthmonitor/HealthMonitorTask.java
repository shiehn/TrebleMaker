package com.treblemaker.healthmonitor;

import com.treblemaker.Application;
import com.treblemaker.utils.Http.HttpUtils;

import java.util.concurrent.Callable;

    public class HealthMonitorTask implements Callable<Boolean> {

        private final int HTTP_TIMEOUT = 600000;
        private String url;

        public HealthMonitorTask(String url) {
            this.url = url;
        }

        @Override
        public Boolean call() throws Exception {

            long threadId = Thread.currentThread().getId();
            Application.logger.debug("LOG: Thread # " + threadId + " is doing this task");

            try {
                HttpUtils httpUtils = new HttpUtils();
                String pingResponse = httpUtils.sendGet(url, HTTP_TIMEOUT);
                httpUtils = null;


                if(pingResponse.toLowerCase().contains("0") || pingResponse.toLowerCase().contains("1") || pingResponse.toLowerCase().contains("2")){
                    Application.logger.debug("LOG: SERVER: " + url + " IS HEALTHY");
                    url = null;
                    return true;
                }else{
                    logError();
                    url = null;
                    return false;
                }

            }catch (Exception e){
                logError();
                return false;
            }
        }

        private void logError(){
            Application.logger.debug("LOG: SERVER ERROR: " + url + " IS DOWN!!!!");
        }
    }
