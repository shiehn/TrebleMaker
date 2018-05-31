package com.treblemaker.utils;

import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.generators.interfaces.IShimGenerator;
import com.treblemaker.renderers.BaseRenderer;
import com.treblemaker.utils.interfaces.IAudioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Component
public class AudioUtils implements IAudioUtils {

    public static final int MONO = 1;
    public static final int STEREO = 2;

    @Autowired
    public AppConfigs appConfigs;

    @Autowired
    @Qualifier(value = "shimGenerator")
    protected IShimGenerator shimGenerator;

    enum VolumeType {
        MEAN, MAX
    }


    public void resampleToMono16Bit(String filePath) throws InterruptedException {

        File oldAudio = new File(filePath);

        String tempFilePath = filePath.replace(".wav", "_temp.wav");

        File tempAudio = new File(tempFilePath);

        if (!oldAudio.exists()) {
            Application.logger.debug("LOG: + resampleToMono16Bit ERROR: FILE DOES NOT EXIST!!");
            return;
        }

        //move original audio
        oldAudio.renameTo(tempAudio);

        Thread.sleep(1000);

        oldAudio.delete();

        Thread.sleep(1000);

        resampleWithFFMPEGTo16Bit44100Mono(tempFilePath, filePath);

        Thread.sleep(5000);

        tempAudio.delete();
    }


    //TODO MOVE TO FFMPEG WRAPPER
    //TODO MOVE TO FFMPEG WRAPPER
    //TODO MOVE TO FFMPEG WRAPPER
    //TODO MOVE TO FFMPEG WRAPPER
    //TODO MOVE TO FFMPEG WRAPPER

    public void resampleWithFFMPEGTo16Bit44100Mono(String sourcePath, String targetPath) {

        StringBuilder strBuild = new StringBuilder();
        /*NEW CODE*/

        Process processDuration = null;
        try {
            processDuration = new ProcessBuilder(appConfigs.getFFMPEGLocation(),
                    "-i",
                    sourcePath,
                    "-acodec",
                    "pcm_s16le",
                    "-ar",
                    "44100",
                    "-ac",
                    "1",
                    targetPath
            ).redirectErrorStream(true).start();

            try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(processDuration.getInputStream(), Charset.defaultCharset()))) {
                String line;
                while ((line = processOutputReader.readLine()) != null) {

                    strBuild.append(line + System.lineSeparator());
                }
                processDuration.waitFor();
            }
        } catch (IOException e) {
            Application.logger.debug("LOG:", e);
        } catch (InterruptedException e) {
            Application.logger.debug("LOG:", e);
        }

        String outputJson = strBuild.toString().trim();
    }

    //TODO MOVE TO FFMPEG WRAPPER
    //TODO MOVE TO FFMPEG WRAPPER
    //TODO MOVE TO FFMPEG WRAPPER
    //TODO MOVE TO FFMPEG WRAPPER
    //TODO MOVE TO FFMPEG WRAPPER


    public float getAudioLength(String pathToAudioFile) {

        File file = new File(pathToAudioFile);

        AudioInputStream inputStream = null;
        try {
            inputStream = AudioSystem.getAudioInputStream(file);
        } catch (UnsupportedAudioFileException e) {
            Application.logger.debug("LOG:", e);
        } catch (IOException e) {
            Application.logger.debug("LOG:", e);
        }

        AudioFormat format = inputStream.getFormat();
        long audioFileLength = file.length();
        int frameSize = format.getFrameSize();
        float frameRate = format.getFrameRate();
        float durationInSeconds = (audioFileLength / (frameSize * frameRate));

        return durationInSeconds;
    }

    public int isMonoOrStereo(String pathToAudioFile) {

        File file = new File(pathToAudioFile);

        AudioInputStream inputStream = null;
        try {
            inputStream = AudioSystem.getAudioInputStream(file);
        } catch (UnsupportedAudioFileException e) {
            Application.logger.debug("LOG:", e);
        } catch (IOException e) {
            Application.logger.debug("LOG:", e);
        }

        return inputStream.getFormat().getChannels();
    }

    public void trimAudio(String sourceFileName, String destinationFileName, int startSecond, float secondsToCopy) {
        AudioInputStream inputStream = null;
        AudioInputStream shortenedStream = null;

        File tempFile = null;

        try {
            File file = new File(sourceFileName);
            String newSourceName = sourceFileName.replaceAll(".wav", "Proccessing.wav");
            tempFile = new File(newSourceName);

            copyFile(file, tempFile);

            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(tempFile);
            AudioFormat format = fileFormat.getFormat();
            inputStream = AudioSystem.getAudioInputStream(tempFile);
            int bytesPerSecond = format.getFrameSize() * (int) format.getFrameRate();
            inputStream.skip(startSecond * bytesPerSecond);
            long framesOfAudioToCopy = (long) (secondsToCopy * format.getFrameRate());
            shortenedStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy);
            File destinationFile = new File(destinationFileName);
            AudioSystem.write(shortenedStream, fileFormat.getType(), destinationFile);

        } catch (Exception e) {
            Application.logger.debug("LOG:", e);
        } finally {
            if (inputStream != null) try {
                inputStream.close();
            } catch (Exception e) {
                Application.logger.debug("LOG:", e);
            }
            if (shortenedStream != null) try {
                shortenedStream.close();
            } catch (Exception e) {
                Application.logger.debug("LOG:", e);
            }

            if (tempFile != null) {
                System.gc();
                tempFile.setWritable(true);
                tempFile.delete();
            }
        }
    }

    public void concatenateAudioShim(String sourceFile, String targetFilePath, float loopLength, float targetLength) throws Exception {

        float acceptableLengthDiff = 0.001f;
        float lengthDiff = targetLength - loopLength;

        if (targetLength > loopLength & (lengthDiff > acceptableLengthDiff)) {

            //TODO GENERATE RANDOM STAMP ..
            String pathForShim = Paths.get(appConfigs.getApplicationRoot(),"tempShim.wav").toString();

            float shimLength = targetLength - loopLength;

            shimGenerator.generateSilence(shimLength, pathForShim, isMonoOrStereo(sourceFile));

            BaseRenderer baseRenderer = new BaseRenderer();

// -------------------
            File originalFile = new File(sourceFile);
            String newSourceName = sourceFile.replaceAll(".wav", "Proccessing.wav");
            File tempFile = new File(newSourceName);
            copyFile(originalFile, tempFile);
// -------------------


            //String newPath = sourceFile.replace(".wav", "PROCESSING.wav");

            //File processingFile = new File(targetFilePath);

            //copyFile(originalFile, processingFile);

            List<String> concateLoops = new ArrayList<String>();
            concateLoops.add(tempFile.getAbsolutePath());
            concateLoops.add(pathForShim);

            File targetFile = new File(targetFilePath);
            if (targetFile.exists()) {
                System.gc();
                targetFile.setWritable(true);
                targetFile.delete();
            }

            if (originalFile.exists()) {
                System.gc();
                originalFile.setWritable(true);
                originalFile.delete();
            }

            baseRenderer.concatenateFiles(concateLoops, targetFilePath);

            File processedFile = new File(targetFilePath);

//			File newOriginalFile = new File(sourceFile);
//			newOriginalFile.createNewFile();
//
//			copyFile(processedFile, newOriginalFile);

            //TODO SHOULD I DELETE OLD ONE ?? ..

            if (processedFile.exists()) {
                //TODO wtf .. why do I need to call the GC to get the file to deleteFile ..
                System.gc();
                tempFile.delete();
            }
        }
    }

    public void copyFile(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    @Override
    public double getMaxVolume(File sourceFile) throws InterruptedException {

        Double volumeMeasure = null;

        try {
            Process processDuration = new ProcessBuilder(appConfigs.getFFMPEGLocation(),
                    "-i",
                    sourceFile.getAbsolutePath(),
                    "-af",
                    "volumedetect",
                    "-f",
                    "null",
                    "NUL").redirectErrorStream(true).start();
            StringBuilder strBuild = new StringBuilder();

            try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(processDuration.getInputStream(), Charset.defaultCharset()))) {
                String line;
                while ((line = processOutputReader.readLine()) != null) {

                    if (volumeMeasure == null) {
                        volumeMeasure = extractVolumeMeasure(line, VolumeType.MAX);
                    }

                    strBuild.append(line + System.lineSeparator());
                }
                processDuration.waitFor();
            }
//			String outputJson = strBuild.toString().trim();

        } catch (IOException e) {
            Application.logger.debug("LOG: ERROR DURING MIX: " + e.getLocalizedMessage());
        }

        return volumeMeasure;
    }

    @Override
    public double getMeanVolume(File sourceFile) throws InterruptedException {
        Double volumeMeasure = null;

        try {
            Process processDuration = new ProcessBuilder(appConfigs.getFFMPEGLocation(),
                    "-i",
                    sourceFile.getAbsolutePath(),
                    "-af",
                    "volumedetect",
                    "-f",
                    "null",
                    "NUL").redirectErrorStream(true).start();
            StringBuilder strBuild = new StringBuilder();

            try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(processDuration.getInputStream(), Charset.defaultCharset()))) {
                String line;
                while ((line = processOutputReader.readLine()) != null) {

                    if (volumeMeasure == null) {
                        volumeMeasure = extractVolumeMeasure(line, VolumeType.MEAN);
                    }

                    strBuild.append(line + System.lineSeparator());
                }
                processDuration.waitFor();
            }
            String outputJson = strBuild.toString().trim();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Application.logger.debug("LOG:", e);
            }
        } catch (IOException e) {

            Application.logger.debug("LOG:", e);
        }

        return volumeMeasure;
    }

    public Double extractVolumeMeasure(String line, VolumeType volumeType) {

        try {
            String start = "max_volume:";
            if (volumeType == VolumeType.MEAN) {
                start = "mean_volume:";
            }

            String stop = "dB";

            int startIndex = line.indexOf(start) + start.length();
            int stopIndex = line.indexOf(stop);


            String result = line.substring(startIndex, stopIndex).trim();
            double dbLevel = Double.parseDouble(result);
//			return Math.abs(dbLevel);
            return dbLevel;
        } catch (Exception e) {
            return null;
        }
    }
}
