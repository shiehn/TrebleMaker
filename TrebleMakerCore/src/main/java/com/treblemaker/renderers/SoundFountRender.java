package com.treblemaker.renderers;

import com.treblemaker.Application;
import com.treblemaker.model.SynthTemplate;
import com.treblemaker.renderers.interfaces.IAudioRender;

import java.io.*;

import com.treblemaker.configs.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Component
public class SoundFountRender implements IAudioRender {

    @Autowired
    private AppConfigs appConfigs;

    public String output(InputStream inputStream) throws IOException {

        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;

        try {

            br = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;

            while ((line = br.readLine()) != null) {
                sb.append(line + System.getProperty("line.separator"));
            }
        } finally {
            br.close();
        }

        return sb.toString();
    }

    @Override
    public void renderPart(String midiFilePath, String audioTargetPath, SoundFountRender.Spectrum spectrum, SynthTemplate template, Integer bpm) {

        midiFilePath = midiFilePath.replace("//", "\\");
        audioTargetPath = audioTargetPath.replace("//", "\\");

        String synthSetting = "";
        if (spectrum == Spectrum.MELODIC) {
            synthSetting = template.getHiSynthName();
        }else if (spectrum == Spectrum.HI) {
            synthSetting = template.getHiSynthNameAlt();
        }else if (spectrum == Spectrum.ALT_HI) {
            synthSetting = template.getHiSynthNameAlt();
        }else if (spectrum == Spectrum.MID) {
            synthSetting = template.getMidSynthName();
        }else if (spectrum == Spectrum.ALT_MID) {
            synthSetting = template.getMidSynthName();
        } else if (spectrum == Spectrum.LOW) {
            synthSetting = template.getLowSynthName();
        }else if (spectrum == Spectrum.ALT_LOW) {
            synthSetting = template.getLowSynthName();
        }

        String debugProcess = appConfigs.getFluidSynth() + "/fluidsynth -F " + audioTargetPath + " " + appConfigs.getSoundFontLocation() + "/" + synthSetting + " " + midiFilePath;

        Application.logger.debug("LOG: RENDER START **********");
        Application.logger.debug("LOG:" + debugProcess);
        Application.logger.debug("LOG: RENDER END **********");

        //ProcessBuilder process = new ProcessBuilder(appConfigs.MrsWatsonLocation, "--midi-file", midiFilePath, "--tempo", Integer.toString(Config.SONG_BPM), "--output", audioTargetPath, "--plugin", appConfigs.VST_PLUGINS_DIR + synthSetting);

        ProcessBuilder process = new ProcessBuilder(appConfigs.getFluidSynth() + "/fluidsynth", "-F", audioTargetPath, appConfigs.getSoundFontLocation() + "/" + synthSetting, midiFilePath);

        try {
            process.start();
            Thread.sleep(2000);
            Application.logger.debug("LOG: fluid renderPart " + spectrum + " COMPLETE");
            Application.logger.debug("LOG: should exist at  " + audioTargetPath);
        } catch (IOException e) {
            Application.logger.debug("LOG:",e);
        } catch (InterruptedException e) {
            Application.logger.debug("LOG:",e);
        }

        if(!(new File(audioTargetPath)).exists()){
            Application.logger.debug("ERROR:", "ERROR FILE DOES NOT EXISIT: " + audioTargetPath);
            throw new RuntimeException("ERROR FILE DOES NOT EXISIT: " + audioTargetPath);
        }
    }
}
