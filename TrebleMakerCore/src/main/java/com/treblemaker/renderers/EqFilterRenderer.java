package com.treblemaker.renderers;

import com.treblemaker.Application;
import org.springframework.stereotype.Component;
import com.treblemaker.configs.AppConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

@Component
public class EqFilterRenderer {

    @Autowired
    public AppConfigs appConfigs;

    public void render(String sourceFile, String targetFile, Integer frequencyCutoff) {

        Process processDuration;
        StringBuilder strBuild = new StringBuilder();
        try {
            processDuration = new ProcessBuilder(appConfigs.getSoxLocation(),
                    sourceFile,
                    targetFile,
                    "highpass",
                    frequencyCutoff.toString()).redirectErrorStream(true).start();

            try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(processDuration.getInputStream(), Charset.defaultCharset()))) {
                String line;
                while ((line = processOutputReader.readLine()) != null) {

                    strBuild.append(line + System.lineSeparator());
                }
                processDuration.waitFor();
            }
        } catch (Exception e) {
            Application.logger.debug("LOG:", e);
        }

        String outputJson = strBuild.toString().trim();

        new File(sourceFile).delete();
    }
}
