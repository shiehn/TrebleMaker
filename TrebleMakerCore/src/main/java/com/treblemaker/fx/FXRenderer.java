package com.treblemaker.fx;

import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.model.fx.FXArpeggioDelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

@Component
public class FXRenderer implements IFXRenderer {

    @Autowired
    public AppConfigs appConfigs;

    @Override
    public String createFfmpegEchoCommand(FXArpeggioDelay fxArpeggioDelay, int bpm) {

        FXDelayParameters.DelayType delayType = FXDelayParameters.getDelayTypeByNormalizedData(fxArpeggioDelay.getDelayType());
        int[] echoIntervals = FXDelayParameters.getEchoIntervals(delayType, bpm);

        FXDelayParameters.EchoVolume echoVolume = FXDelayParameters.getEchoVolumeFromNormalizedData(fxArpeggioDelay.getDelayVolume());
        double[] echoVolumes = FXDelayParameters.getEchoVolumes(echoVolume);

        //CREATE THE COMMAND HERE  .. asldfj                                         //"1.0:1.0:250|500|750|1000:0.8|0.6|0.3|0.1"
        String ffmpegCommand = "aecho=1.0:"
                + fxArpeggioDelay.getMasterVolume() + ":"
                + echoIntervals[0] + "|"
                + echoIntervals[1] + "|"
                + echoIntervals[2] + "|"
                + echoIntervals[3] + ":"
                + echoVolumes[0] + "|"
                + echoVolumes[1] + "|"
                + echoVolumes[2] + "|"
                + echoVolumes[3];

        return ffmpegCommand;
    }

    @Override
    public void renderSfx(String sourceFile, String targetFile, FXArpeggioDelay fxArpeggioDelay, int bpm) {

        String ffmpegEchoCommand = createFfmpegEchoCommand(fxArpeggioDelay, bpm);
        StringBuilder strBuild = new StringBuilder();

        Application.logger.debug("LOG: RENDER FX ATTEMPT : " + targetFile);
        String debugCommand = appConfigs.getFFMPEGLocation() + " -i " + sourceFile + " -af " +  ffmpegEchoCommand +
                " -y " + targetFile;
        Application.logger.debug("LOG: RENDER FX COMMAND : " + debugCommand);

        Process processDuration = null;
        try {
            processDuration = new ProcessBuilder(appConfigs.getFFMPEGLocation(),
                    "-i",
                    sourceFile,
                    "-af",
                    ffmpegEchoCommand,
                    "-y",
                    targetFile).redirectErrorStream(true).start();

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

        Application.logger.debug("LOG: FX RENDER OUTPUT >>> ");
        Application.logger.debug("LOG:",outputJson);
    }

    @Override
    public void renderReverbFx(String sourceFile, String targetFile) {

        Process processDuration;
        StringBuilder strBuild = new StringBuilder();
        try {
            processDuration = new ProcessBuilder(appConfigs.getSoxLocation(),
                    sourceFile,
                    targetFile,
                    "reverb").redirectErrorStream(true).start();

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

        if((new File(targetFile)).exists()){
            Application.logger.debug("LOG: FX RENDER SUCCESS: " + targetFile);
            new File(sourceFile).delete();
            return;
        }

        throw new RuntimeException("ERROR: FAILED TO RENDER FX FOR :" + sourceFile);
    }
}
