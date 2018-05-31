package com.treblemaker.utils;

import com.treblemaker.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread {
    InputStream is;
    String type;

    public StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null)
            	
            	if(line.contains("Press [q] to stop, [?] for help")){
            		
            		//THIS MEANS RENDER IS COMPLETE !!!
//            		if(compose != null){
//            			compose.mixRenderComplete();
//            		}
            	}
            	
                Application.logger.debug("LOG:" + type + "> " + line);
        }
        catch (IOException e) {
            Application.logger.debug("LOG:", e);
        }
    }
}
