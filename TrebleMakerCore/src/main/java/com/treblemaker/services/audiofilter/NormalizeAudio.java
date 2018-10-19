package com.treblemaker.services.audiofilter;

import com.treblemaker.Application;
import com.treblemaker.utils.FileStructure;
import com.treblemaker.utils.interfaces.IAudioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.treblemaker.configs.AppConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NormalizeAudio {

    @Autowired
    private IAudioUtils audioUtils;

    @Autowired
    public AppConfigs appConfigs;

    @Value("${num_of_alt_melodies}")
    Integer numOfAltMelodies;

    private final double TARGET_RANGE = 0.5;

    private final double TARGET_VOLUME = 0.0;

    public double normalizeLoopFiles(String directory) throws IOException {

        List<Path> paths = Files.walk(Paths.get(directory)).filter(
                f -> !f.toFile().isDirectory() &&
                        f.toFile().isFile() &&
                        f.getFileName().toString().toLowerCase().contains(".wav"))
                .collect(Collectors.toList());

        HashMap<Path, Double> filteredMeanVolumeMap = createMeanVolumeMap(paths);

        double targetMean = getMeanOfMeans(filteredMeanVolumeMap);

        normalize(createMeanVolumeMap(paths), targetMean, directory);

        return targetMean;
    }

    public double normalizeFilesInADirectory(String directory) throws IOException {

        List<Path> paths = Files.walk(Paths.get(directory)).filter(
                f -> !f.toFile().isDirectory() &&
                        f.toFile().isFile() &&
                        f.getFileName().toString().toLowerCase().contains(".wav"))
                .collect(Collectors.toList());

        //FILTER OUT ANYTHING THAT IS NOT SONG VARIATION ONE ..
        List<Path> filteredPaths = new ArrayList<>();
        for (Path path : paths) {
            String firstLetter = String.valueOf(path.getFileName().toString().charAt(0));
            if (firstLetter.matches("^-?\\d+$")) {
                if (Integer.parseInt(firstLetter) == 0) {
                    filteredPaths.add(path);
                }
            } else {
                filteredPaths.add(path);
            }
        }

        for (Path p:filteredPaths) {
            Application.logger.debug("LOG: FILTERED PATH: " + p.toString());
        }

        HashMap<Path, Double> filteredMeanVolumeMap = createMeanVolumeMap(filteredPaths);

        double targetMean = getMeanOfMeans(filteredMeanVolumeMap);

        normalize(createMeanVolumeMap(paths), targetMean, directory);

        return targetMean;
    }

    public void cleanup(String directoryPath) throws IOException {

        List<Path> wavPaths = Files.walk(Paths.get(directoryPath)).filter(
                f -> !f.toFile().isDirectory() &&
                        f.toFile().isFile() &&
                        f.getFileName().toString().toLowerCase().contains("_processing.wav"))
                .collect(Collectors.toList());

        wavPaths.forEach(w -> {
            try {
                Application.logger.debug("LOG: ABOUT TO DELETE: " + w.toString());
                FileStructure.deleteFile(w.toFile());
            } catch (IOException e) {
                Application.logger.debug("LOG:", e);
            }
        });
    }

    public List<Path> renameToProcessing(String directoryPath) throws IOException {

        List<Path> wavPaths = Files.walk(Paths.get(directoryPath)).filter(
                f -> !f.toFile().isDirectory() &&
                        f.toFile().isFile() &&
                        f.getFileName().toString().toLowerCase().contains(".wav"))
                .collect(Collectors.toList());

        System.out.print("");
        System.out.print("PATH COUNT ******** " + wavPaths.size());
        System.out.print("");

        System.out.print("");

        //TODO JUST FOR LOG >> PLEASEE REMOVE
        final int[] count = {0};

        wavPaths.forEach(wp -> {
            if (!wp.toFile().getAbsolutePath().contains("_processing")) {
                String newName = wp.toFile().getAbsolutePath().replace(".wav", "_processing.wav");
                wp.toFile().renameTo(new File(newName));
                count[0]++;
            }
        });

        System.out.print("RENAME COUNT ******** " + count[0]);
        System.out.print("");

        List<Path> paths = Files.walk(Paths.get(directoryPath)).filter(
                f -> !f.toFile().isDirectory() &&
                        f.toFile().isFile() &&
                        f.getFileName().toString().toLowerCase().contains("_processing.wav"))
                .collect(Collectors.toList());

        return paths;
    }

    public List<Path> getWavPathsFromDirectory(String directoryPath) throws IOException {

        List<Path> wavPaths = Files.walk(Paths.get(directoryPath)).filter(
                f -> !f.toFile().isDirectory() &&
                        f.toFile().isFile() &&
                        f.getFileName().toString().toLowerCase().contains(".wav"))
                .collect(Collectors.toList());

        return wavPaths;
    }

    public List<Path> getProcessingPathsFromWavPaths(List<Path> wavPaths) throws IOException {

        List<Path> processingPaths = new ArrayList<>();

        for (Path wavPath : wavPaths) {
            File file = new File(wavPath.toString().replace(".wav", "_processing.wav"));

            if (!file.exists()) {
                throw new RuntimeException("PROCESSING FILE DOES NOT EXIST!!!");
            }

            processingPaths.add(file.toPath());
        }

        return processingPaths;


//        List<Path> paths = Files.walk(Paths.get(directoryPath)).filter(
//                f -> !f.toFile().isDirectory() &&
//                        f.toFile().isFile() &&
//                        f.getFileName().toString().toLowerCase().contains("_processing.wav"))
//                .collect(Collectors.toList());
//
//        return paths;
    }

    public Map<Path, Double> renameToProcessing(Map<Path, Double> wavPaths, List<Path> alreadyRenamed) throws IOException {

        System.out.print("");
        System.out.print("PATH COUNT ******** " + wavPaths.size());
        System.out.print("");

        System.out.print("");

        Map<Path, Double> updatedMap = new HashMap<>();

        for (Map.Entry<Path, Double> wav : wavPaths.entrySet()) {
            if (!wav.getKey().toFile().getAbsolutePath().contains("_processing") && !alreadyRenamed.contains(wav.getKey())) {
                String newName = wav.getKey().toFile().getAbsolutePath().replace(".wav", "_processing.wav");
                wav.getKey().toFile().renameTo(new File(newName));

                updatedMap.put(new File(newName).toPath(), wav.getValue());
                alreadyRenamed.add(wav.getKey());
            }
        }

        return updatedMap;
    }

    public HashMap<Path, Double> createMeanVolumeMap(List<Path> paths) {

        HashMap<Path, Double> meanVolumeMap = new HashMap<>();

        paths.forEach(p -> {
            try {
                meanVolumeMap.put(p, audioUtils.getMeanVolume(p.toFile()));
            } catch (InterruptedException e) {
                Application.logger.debug("LOG:", e);
            }
        });

        return meanVolumeMap;
    }

    public void normalize(Map<Path, Double> meanVolumeMap, double targetMean, String directory) throws IOException {

        //check if
        boolean shouldAdjustLevels = !meansWithinTargetRange(targetMean, meanVolumeMap);

        Application.logger.debug("LOG: SHOULD ADJUST LEVELS : " + shouldAdjustLevels);
        Application.logger.debug("LOG: SHOULD ADJUST LEVELS : " + shouldAdjustLevels);
        Application.logger.debug("LOG: SHOULD ADJUST LEVELS : " + shouldAdjustLevels);
        Application.logger.debug("LOG: SHOULD ADJUST LEVELS : " + shouldAdjustLevels);
        Application.logger.debug("LOG: SHOULD ADJUST LEVELS : " + shouldAdjustLevels);
        Application.logger.debug("LOG: SHOULD ADJUST LEVELS : " + shouldAdjustLevels);

        if (shouldAdjustLevels) {

            List<Path> paths = renameToProcessing(directory);


            for (Path p:paths) {
                Application.logger.debug("LOG: RENAMED PATH: " + p.toString());
            }

            //update mean volume maps with new paths
            meanVolumeMap = createMeanVolumeMap(paths);

            //////////////////////////////////////////////
            //RENAMING FILES !!!!!!!!!!
            for (Map.Entry<Path, Double> meanEntry : meanVolumeMap.entrySet()) {

                if (!meanEntry.getKey().toString().contains("_processing")) {
                    // RENAME ALL FILES  ./.....
                    File oldfile = new File(meanEntry.getKey().toString());
                    File newfile = new File(meanEntry.getKey().toString().replace(".wav", "_processing.wav"));

                    if (oldfile.renameTo(newfile)) {
                        Application.logger.debug("LOG:","Rename succesful");
                    } else {
                        Application.logger.debug("LOG:","Rename failed");
                    }
                }
            }
            /// /////////////////////////////////////

            HashMap<Path, Double> meanOffSets = calculateMeanOffset(targetMean, meanVolumeMap);

            for (Map.Entry<Path, Double> offset : meanOffSets.entrySet()) {

                StringBuilder strBuild = new StringBuilder();

                Application.logger.debug("LOG:","k : " + offset.getKey().getFileName() + " : " + offset.getValue());

                String vol = "volume=" + offset.getValue() + "dB";

                Process processDuration = null;

                try {
                    processDuration = new ProcessBuilder(appConfigs.getFFMPEGLocation(),
                            "-i",
                            offset.getKey().toFile().getAbsolutePath(),
                            "-b",
                            "705k",
                            "-minrate",
                            "705k",
                            "-maxrate",
                            "705k",
                            "-bufsize",
                            "705k",
                            "-ac",
                            "1",
                            "-af",
                            vol,
                            offset.getKey().toFile().getAbsolutePath().replace("_processing.wav", ".wav")
                    ).redirectErrorStream(true).start();

                    try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(processDuration.getInputStream(), Charset.defaultCharset()))) {
                        String line;
                        while ((line = processOutputReader.readLine()) != null) {

                            strBuild.append(line + System.lineSeparator());
                        }
                        processDuration.waitFor();
                    } catch (Exception e) {
                        Application.logger.debug("LOG:", e);
                    }
                } catch (Exception e) {
                    Application.logger.debug("LOG:", e);
                }

                String outputJson = strBuild.toString().trim();

                Application.logger.debug("LOG:","OUT >>> " + outputJson);
            }

            paths.forEach(path -> {
                try {
                    cleanup(path.toString());
                } catch (IOException e) {
                    Application.logger.debug("LOG:", e);
                }
            });
        }
    }

    public void normalizeToTarget(Map<Path, Double> meandddOffSets, List<Path> alreadyAdjusted, List<Path> alreadyRenamed) throws IOException {

        Map<Path, Double> processingMap = renameToProcessing(meandddOffSets, alreadyRenamed);

        for (Map.Entry<Path, Double> offset : processingMap.entrySet()) {

            if (!alreadyAdjusted.contains(offset.getKey())) {
                StringBuilder strBuild = new StringBuilder();

                Application.logger.debug("LOG:","k : " + offset.getKey().getFileName() + " : " + offset.getValue());

                String vol = "volume=" + offset.getValue() + "dB";

                Process processDuration = null;

                try {
                    processDuration = new ProcessBuilder(appConfigs.getFFMPEGLocation(),
                            "-i",
                            offset.getKey().toFile().getAbsolutePath(),
                            "-b",
                            "705k",
                            "-minrate",
                            "705k",
                            "-maxrate",
                            "705k",
                            "-bufsize",
                            "705k",
                            "-ac",
                            "1",
                            "-af",
                            vol,
                            offset.getKey().toFile().getAbsolutePath().replace("_processing.wav", ".wav")
                    ).redirectErrorStream(true).start();

                    try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(processDuration.getInputStream(), Charset.defaultCharset()))) {
                        String line;
                        while ((line = processOutputReader.readLine()) != null) {

                            strBuild.append(line + System.lineSeparator());
                        }
                        processDuration.waitFor();
                    } catch (Exception e) {
                        Application.logger.debug("LOG:", e);
                    }
                } catch (Exception e) {
                    Application.logger.debug("LOG:", e);
                }

                String outputJson = strBuild.toString().trim();
                Application.logger.debug("LOG: OUT >>> " + outputJson);

                alreadyAdjusted.add(offset.getKey());
            } else {
                Application.logger.debug("LOG: ALREADY NORMALIZED >>> " + offset.getKey().getFileName());
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Application.logger.debug("LOG:", e);
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Application.logger.debug("LOG:", e);
            }
        }

        for (Map.Entry<Path, Double> processedFile : processingMap.entrySet()) {

            try {
                cleanup(processedFile.getKey().toString());
            } catch (IOException e) {
                Application.logger.debug("LOG:", e);
            }
        }
    }

    public Double getMeanOfMeans(Map<Path, Double> meanVolumeMap) {

        int count = 0;
        Double meansum = 0.0;

        for (Double mean : meanVolumeMap.values()) {
            meansum = meansum + mean;
            count = count + 1;
        }

        Double unRoundedNumber = meansum / (double) count;

        return (double) Math.round(unRoundedNumber * 10) / 10;
    }

    public HashMap<Path, Double> calculateMeanOffset(Double target, Map<Path, Double> meanVolumeMap) {

        HashMap<Path, Double> meanOffsets = new HashMap<>();

        for (Map.Entry<Path, Double> entry : meanVolumeMap.entrySet()) {
            Double unRoundedNumberOffset = target - entry.getValue();
            Double roundedNumberOffset = (double) Math.round(unRoundedNumberOffset * 10) / 10;
            meanOffsets.put(entry.getKey(), roundedNumberOffset);
        }

        return meanOffsets;
    }

    public boolean meansWithinTargetRange(Double target, Map<Path, Double> means) {

        for (Map.Entry<Path, Double> entry : means.entrySet()) {

            Double unRoundedNumberOffset = target - entry.getValue();
            Double roundedNumberOffset = (double) Math.round(unRoundedNumberOffset * 10) / 10;

            if (Math.abs(roundedNumberOffset) > TARGET_RANGE) {
                return false;
            }
        }

        return true;
    }

    public boolean meanIsWithinTargetRange(Double target, Double mean) {

        Double unRoundedNumberOffset = target - mean;
        Double roundedNumberOffset = (double) Math.round(unRoundedNumberOffset * 10) / 10;

        if (Math.abs(roundedNumberOffset) > TARGET_RANGE) {
            return false;
        }

        return true;
    }

    public void normalizeMix(String mixFilePath, String mixFileName, int numOfGeneratedMixes) throws InterruptedException, IOException {
        int numberOfTrackVariations = 3;

        for (int i = 0; i < numOfGeneratedMixes; i++) {
            //for (int j = 1; j < numberOfTrackVariations + 1; j++) {
                    for(int k=1; k<numOfAltMelodies + 1; k++) {
                        String mixFileNameProcessing = mixFileName + "_" + i + "_" + k + "_processing.mp3";
                        String mixFileNameCopy = mixFileName + "_" + i + "_" + k + ".mp3";
                        performNormalize(mixFilePath, mixFileNameProcessing, mixFileNameCopy);
                    }
            //}
        }
    }

    private void performNormalize(String mixFilePath, String mixFileNameProcessing, String mixFileNameCopy) throws InterruptedException {
        new File(mixFilePath + "/" + mixFileNameCopy).renameTo(new File(mixFilePath + "/" + mixFileNameProcessing));

        double maxVolume = audioUtils.getMaxVolume(new File(mixFilePath + "/" + mixFileNameProcessing));

        double difference = getDifference(maxVolume);

        adjustVolumeLevel(mixFilePath + "/" + mixFileNameProcessing, mixFilePath + "/" + mixFileNameCopy, difference);

        File fileToDelete = new File(mixFilePath + "/" + mixFileNameProcessing);
        try {
            FileStructure.deleteFile(fileToDelete);
        } catch (Exception e) {
            Application.logger.debug("LOG:", "ERROR!! NOT ABLE TO DELETE: " + fileToDelete.getAbsolutePath());
        }
    }

    private double getDifference(double maxVolume) {

        if (maxVolume < TARGET_VOLUME) {
            Double unRoundedNumberOffset = TARGET_VOLUME - maxVolume;
            Double roundedNumberOffset = (double) Math.round(unRoundedNumberOffset * 10) / 10;
            return roundedNumberOffset;
        }

        return TARGET_VOLUME;
    }

    private void adjustVolumeLevel(String sourcePath, String destinationPath, double deciblesToAdjust) {
        StringBuilder strBuild = new StringBuilder();

        Application.logger.debug("LOG:","volume adjust : " + sourcePath + " : " + deciblesToAdjust);

        String vol = "volume=" + deciblesToAdjust + "dB";

        Process processDuration = null;

        try {
            processDuration = new ProcessBuilder(appConfigs.getFFMPEGLocation(),
                    "-i",
                    sourcePath,
                    "-b",
                    "705k",
                    "-minrate",
                    "705k",
                    "-maxrate",
                    "705k",
                    "-bufsize",
                    "705k",
                    "-ac",
                    "1",
                    "-af",
                    vol,
                    destinationPath
            ).redirectErrorStream(true).start();

            try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(processDuration.getInputStream(), Charset.defaultCharset()))) {
                String line;
                while ((line = processOutputReader.readLine()) != null) {

                    strBuild.append(line + System.lineSeparator());
                }
                processDuration.waitFor();
            } catch (Exception e) {
                Application.logger.debug("LOG:", e);
            }
        } catch (Exception e) {
            Application.logger.debug("LOG:", e);
        }

        String outputJson = strBuild.toString().trim();

        Application.logger.debug("LOG: OUT >>> " + outputJson);
    }
}
