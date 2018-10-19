package com.treblemaker.mixer;

import com.treblemaker.Application;
import com.treblemaker.mixer.interfaces.IAudioMixer;
import com.treblemaker.renderers.BaseRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import com.treblemaker.configs.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.treblemaker.mixer.MixPartsAndVariations.*;
import static com.treblemaker.mixer.MixPartsAndVariations.MixTypes.*;

@Component
public class AudioMixer extends BaseRenderer implements IAudioMixer {

    @Autowired
    private Environment environment;

    @Autowired
    private AppConfigs appConfigs;

    public void createMixes(String audioPartMelodic1,
                            String audioPartMelodic2,
                            String audioPartMelodic3,
                            String audioPartMelodic4,
                            String audioPartMelodic5,
                            String audioPartHi,
                            String audioPartAltHi, String audioPartMid,
                            String audioPartAltMid, String audioPartLow,
                            String audioPartAltLow, String audioPartRhythm,
                            String audioPartRhythmAlt, String audioPartAmbience,
                            String audioPartHarmonic, String audioPartHarmonicAlt,
                            String audioPartHits, String audioPartFills,
                            String audioPartKick, String audioPartSnare,
                            String audioPartHat, int index, String targetPath) throws Exception {

        Map<String, String> MixPartToPath = new HashMap<>();
        MixPartToPath.put(MixParts.audioPartMelodic1.toString(), audioPartMelodic1.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartMelodic2.toString(), audioPartMelodic2.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartMelodic3.toString(), audioPartMelodic3.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartMelodic4.toString(), audioPartMelodic4.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartMelodic5.toString(), audioPartMelodic5.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartHi.toString(), audioPartHi.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartAltHi.toString(), audioPartAltHi.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartMid.toString(),audioPartMid.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartAltMid.toString(), audioPartAltMid.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartLow.toString(), audioPartLow.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartAltLow.toString(), audioPartAltLow.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartRhythm.toString(), audioPartRhythm.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartRhythmAlt.toString(), audioPartRhythmAlt.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartAmbience.toString(), audioPartAmbience.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartHarmonic.toString(), audioPartHarmonic.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartHarmonicAlt.toString(), audioPartHarmonicAlt.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartHits.toString(), audioPartHits.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartFills.toString(), audioPartFills.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartKick.toString(), audioPartKick.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartSnare.toString(), audioPartSnare.replace("//", "\\"));
        MixPartToPath.put(MixParts.audioPartHat.toString(), audioPartHat.replace("//", "\\"));

        targetPath = targetPath.replace("\"", "");

        createMix(getPartPathsForMix(mixVariations.get(fullMix1.toString()), MixPartToPath),
                targetPath.replace(".wav", "_" + index + mixTypeExtentions.get(fullMix1)));
        createMix(getPartPathsForMix(mixVariations.get(fullMix2.toString()), MixPartToPath),
                targetPath.replace(".wav", "_" + index + mixTypeExtentions.get(fullMix2)));
        createMix(getPartPathsForMix(mixVariations.get(fullMix3.toString()), MixPartToPath),
                targetPath.replace(".wav", "_" + index + mixTypeExtentions.get(fullMix3)));
        createMix(getPartPathsForMix(mixVariations.get(fullMix4.toString()), MixPartToPath),
                targetPath.replace(".wav", "_" + index + mixTypeExtentions.get(fullMix4)));
        createMix(getPartPathsForMix(mixVariations.get(fullMix5.toString()), MixPartToPath),
                targetPath.replace(".wav", "_" + index + mixTypeExtentions.get(fullMix5)));
    }

    private List<String> getPartPathsForMix(List<String> mixDefinitions, Map<String, String> paths){

        List<String> outputPaths = new ArrayList<>();

        for(String mixPart: mixDefinitions){
            outputPaths.add(paths.get(mixPart));
        }

        return outputPaths;
    }

    private void createMix(List<String> inputs, String targetPath) throws Exception {

        StringBuilder strBuild = new StringBuilder();

        Process processDuration;

        String[] pBuilderParams = new String[(inputs.size() * 2) + 8];
        pBuilderParams[0] = appConfigs.getFFMPEGLocation();

        for (int i = 1; i < inputs.size() + 1; i++) {
            pBuilderParams[i * 2 - 1] = "-i";
            pBuilderParams[i * 2] = inputs.get(i - 1);
        }

        pBuilderParams[inputs.size() * 2 + 1] = "-filter_complex";
        pBuilderParams[inputs.size() * 2 + 2] = "amix=inputs=" + inputs.size() + ":duration=longest:dropout_transition=0";
        pBuilderParams[inputs.size() * 2 + 3] = "-codec:a";
        pBuilderParams[inputs.size() * 2 + 4] = "libmp3lame";
        pBuilderParams[inputs.size() * 2 + 5] = "-qscale:a";
        pBuilderParams[inputs.size() * 2 + 6] = "2";
        pBuilderParams[inputs.size() * 2 + 7] = targetPath.replace(".wav", ".mp3");

        try {
            processDuration = new ProcessBuilder(pBuilderParams).redirectErrorStream(true).start();

            try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(processDuration.getInputStream(), Charset.defaultCharset()))) {
                String line;
                while ((line = processOutputReader.readLine()) != null) {

                    strBuild.append(line + System.lineSeparator());
                }
                processDuration.waitFor();
            }
        } catch (IOException e) {
            Application.logger.debug("LOG:", e);
            Application.logger.debug("LOG:", e);
        } catch (InterruptedException e) {
            Application.logger.debug("LOG:", e);
            Application.logger.debug("LOG:", e);
        } catch (Exception e) {
            Application.logger.debug("LOG:", e);
            Application.logger.debug("LOG:", e);
        }

        String outputJson = strBuild.toString().trim();

        Application.logger.debug("LOG: FINAL MIX OUT >>> ");
        Application.logger.debug("LOG: FINAL MIX OUT >>> ");
        Application.logger.debug("LOG: FINAL MIX OUT >>> ");
        Application.logger.debug("LOG: FINAL MIX OUT >>> ");
        Application.logger.debug("LOG: FINAL MIX OUT >>> ");
        Application.logger.debug("LOG: FINAL MIX OUT >>> ");

        Application.logger.debug("LOG:" + outputJson);
    }

    public boolean changeVolume(double newGain, String inputPath, String outputPath) {

        StringBuilder strBuild = new StringBuilder();
        Process processDuration = null;

        try {

            List<String> ffmpegCmd = new ArrayList<String>() {
            };
            ffmpegCmd.add(appConfigs.getFFMPEGLocation());
            ffmpegCmd.add("-i");
            ffmpegCmd.add(inputPath);
            ffmpegCmd.add("-af");
            ffmpegCmd.add("volume=" + newGain);
            ffmpegCmd.add(outputPath);

            processDuration = new ProcessBuilder(ffmpegCmd).redirectErrorStream(true).start();

            try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(processDuration.getInputStream(), Charset.defaultCharset()))) {
                String line;
                while ((line = processOutputReader.readLine()) != null) {

                    strBuild.append(line + System.lineSeparator());
                }
                processDuration.waitFor();
            }
        } catch (IOException e) {
            Application.logger.debug("LOG:","EXCEPTION ", e);
        } catch (InterruptedException e) {
            Application.logger.debug("LOG:","EXCEPTION ", e);
        }

        String outputJson = strBuild.toString().trim();
        Application.logger.debug("LOG:" + outputJson);

        return true;
    }
}
