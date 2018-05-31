package com.treblemaker.mixer;

import com.treblemaker.Application;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.configs.AppConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

@Component
public class StereoMixer {

    @Autowired
    AppConfigs appConfigs;

    public StereoMixer(AppConfigs appConfigs){
        appConfigs = appConfigs;
    }

    public boolean createStereoMixes(QueueItem queueItem, Integer numOfGeneratedMixes) {

        List<String> monoPartsSource = new ArrayList<>();
        monoPartsSource.add(appConfigs.COMP_RHYTHM_FILENAME);
        monoPartsSource.add(appConfigs.COMP_RHYTHM_ALT_FILENAME);
        monoPartsSource.add(appConfigs.COMP_AMBIENCE_FILENAME);
        monoPartsSource.add(appConfigs.COMP_HARMONIC_FILENAME);
        monoPartsSource.add(appConfigs.COMP_HARMONIC_ALT_FILENAME);
        monoPartsSource.add(appConfigs.HITS_FILENAME);
        monoPartsSource.add(appConfigs.FILLS_FILENAME);
        monoPartsSource.add(appConfigs.KICK_FILENAME);
        monoPartsSource.add(appConfigs.HAT_FILENAME);
        monoPartsSource.add(appConfigs.SNARE_FILENAME);

        for (int i = 0; i < numOfGeneratedMixes; i++) {
            monoPartsSource.add(i + appConfigs.COMP_MELODIC_AUDIO_FILENAME_FX);
            monoPartsSource.add(i + appConfigs.COMP_HI_FX_AUDIO_FILENAME);
            monoPartsSource.add(i + appConfigs.COMP_ALT_HI_FX_AUDIO_FILENAME);
            monoPartsSource.add(i + appConfigs.COMP_MID_AUDIO_FILENAME);
            monoPartsSource.add(i + appConfigs.COMP_ALT_MID_AUDIO_FILENAME);
            monoPartsSource.add(i + appConfigs.COMP_LOW_AUDIO_FILENAME);
            monoPartsSource.add(i + appConfigs.COMP_ALT_LOW_AUDIO_FILENAME);
        }

        for(String source : monoPartsSource){

            String fullSource = queueItem.getMonoPartsFilePath() + "/" + source;
            String sourceLeft = fullSource.replace(".wav", "_l.wav");
            String sourceRight = fullSource.replace(".wav", "_r.wav");

            if( !(new File(sourceLeft)).exists() || !(new File(sourceRight)).exists() ) {
                Application.logger.debug("ERROR: MONO MIX SOURCE l or r DOES NOT EXIST:" + queueItem.getMonoPartsFilePath() + "/" + source);
                throw new RuntimeException("ERROR: MONO MIX SOURCE l or r DOES NOT EXIST:" + queueItem.getMonoPartsFilePath() + "/" + source);
            }else{
                Application.logger.debug("LOG: stereo mix left exisits: " + queueItem.getMonoPartsFilePath() + "/" + sourceLeft);
                Application.logger.debug("LOG: stereo mix right exisits: " + queueItem.getMonoPartsFilePath() + "/" + sourceRight);
            }
        }

        HashMap<String, List<String>> monoPartsLAndR = new HashMap<>();

        for (String monoPart : monoPartsSource) {
            monoPartsLAndR.put(monoPart, Arrays.asList(queueItem.getMonoPartsFilePath() + "/" + monoPart.replace(".wav", "_l.wav"), queueItem.getMonoPartsFilePath() + "/" + monoPart.replace(".wav", "_r.wav")));
        }

        for (Map.Entry<String, List<String>> entry : monoPartsLAndR.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            renderStereoFile(key, queueItem.getStereoPartsFilePath(), value);
        }

        for(String output : monoPartsSource){
            if(!(new File(queueItem.getStereoPartsFilePath() + "/" + output)).exists()){
                Application.logger.debug("ERROR: STEREO MIX OUTPUT DOES NOT EXIST:" + queueItem.getStereoPartsFilePath() + "/" + output);
                throw new RuntimeException("ERROR: STEREO MIX OUPUT DOES NOT EXIST:" + queueItem.getStereoPartsFilePath() + "/" + output);
            }else{
                Application.logger.debug("ERROR: s:" + queueItem.getStereoPartsFilePath() + "/" + output);
            }
        }

        return true;
    }

    private boolean renderStereoFile(String fileName, String stereoFilePath, List<String> sourceFiles) {

        StringBuilder strBuild = new StringBuilder();

        Process processDuration = null;

        try {

            List<String> ffmpegCmd = new ArrayList<String>() {
            };
            ffmpegCmd.add(appConfigs.getFFMPEGLocation());
            ffmpegCmd.add("-i");
            ffmpegCmd.add(sourceFiles.get(0));
            ffmpegCmd.add("-i");
            ffmpegCmd.add(sourceFiles.get(1));
            ffmpegCmd.add("-filter_complex");
            ffmpegCmd.add("[0:a][1:a] amerge=inputs=" + sourceFiles.size());
            ffmpegCmd.add("-ac");
            ffmpegCmd.add(Integer.toString(sourceFiles.size()));
            ffmpegCmd.add("-c:a");
            ffmpegCmd.add("pcm_s16le");
            ffmpegCmd.add(stereoFilePath + "/" + fileName);

            processDuration = new ProcessBuilder(ffmpegCmd).redirectErrorStream(true).start();

            try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(processDuration.getInputStream(), Charset.defaultCharset()))) {
                String line;
                while ((line = processOutputReader.readLine()) != null) {

                    strBuild.append(line + System.lineSeparator());
                }
                processDuration.waitFor();

                Thread.sleep(4000);
            }
        } catch (IOException e) {
            Application.logger.debug("LOG:", e);
        } catch (InterruptedException e) {
            Application.logger.debug("LOG:", e);
        }

        String outputJson = strBuild.toString().trim();
        Application.logger.debug("LOG:" + outputJson);

        if((new File(stereoFilePath + "/" + fileName)).exists()){
            Application.logger.debug("LOG: StereoMix Success: " + stereoFilePath + "/" + fileName);
        }else{
            throw new RuntimeException("ERROR: FAILED TO RENDER STEREO MIX: " + stereoFilePath + "/" + fileName);
        }

        return true;
    }
}
