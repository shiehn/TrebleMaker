package com.treblemaker;

import com.treblemaker.utils.LoopUtils;
import com.treblemaker.utils.interfaces.IAudioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
public class SampleTrimUtil {

    @Autowired
    private IAudioUtils audioUtils;

    final int BPM = 120;
    final String SAMPLE_DIRECTORY_PATH = "C:\\Capricious\\resampletest\\";

//    public static void main(String[] args) throws Exception {
//        new SpringApplicationBuilder(SampleTrimUtil.class).web(false).run(args);
//    }

//    @PostConstruct
    public void postConstruct() throws Exception {

        String TARGET_PATH = createTargetFolder(SAMPLE_DIRECTORY_PATH);

        List<String> files = filePathsInDirectory(SAMPLE_DIRECTORY_PATH);

        for (String fileName : files) {
            audioUtils.resampleToMono16Bit(SAMPLE_DIRECTORY_PATH + fileName);
            Application.logger.debug("LOG: FILE: " + SAMPLE_DIRECTORY_PATH + fileName);
            correctLoopLength(SAMPLE_DIRECTORY_PATH + fileName, TARGET_PATH + fileName, BPM);
        }
    }

    public String createTargetFolder(String sampleDirectoryPath) {
        String uuid = UUID.randomUUID().toString();
        String targetDir = sampleDirectoryPath + uuid + "\\";
        new File(sampleDirectoryPath + uuid).mkdirs();

        return targetDir;
    }

    public List<String> filePathsInDirectory(String directoryPath) {
        List<String> results = new ArrayList<String>();

        File[] files = new File(directoryPath).listFiles();

        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }

        return results;
    }

    public void correctLoopLength(String sourceFilePath, String targetFilePath, int bpm) throws Exception {

        float originalLength = audioUtils.getAudioLength(sourceFilePath);

        float secondsInBar = LoopUtils.getSecondsInBar(bpm);

        float sixteenthNoteInSecs = 0.0625f * secondsInBar;

        if (sixteenthNoteInSecs < originalLength) {
            //TRIM THE FUCKER ..
            audioUtils.trimAudio(sourceFilePath, targetFilePath.toLowerCase().replace(" ", ""), 0, sixteenthNoteInSecs);
        } else {
            Application.logger.debug("LOG: ERROR ALREADY TRIMMED");
            Application.logger.debug("LOG: ERROR ALREADY TRIMMED");
            Application.logger.debug("LOG: ERROR ALREADY TRIMMED");
            Application.logger.debug("LOG: ERROR ALREADY TRIMMED");
            //APPEND THE SHIM ..
//            audioUtils.concatenateAudioShim(sourceFilePath, targetFilePath, originalLength, targetLength);
        }

    }
}
