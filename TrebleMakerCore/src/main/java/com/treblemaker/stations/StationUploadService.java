package com.treblemaker.stations;

import com.amazonaws.AmazonClientException;
import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.dal.interfaces.IStationTrackDal;
import com.treblemaker.model.stations.StationTrack;
import com.treblemaker.services.AudioTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

@Component
public class StationUploadService {

    @Autowired
    AudioTransferService audioTransferService;

    @Autowired
    IStationTrackDal stationTrackDal;

    @Autowired
    public AppConfigs appConfigs;

    @Autowired
    public StationUploadService(AudioTransferService audioTransferService, IStationTrackDal stationTrackDal) {
        this.audioTransferService = audioTransferService;
        this.stationTrackDal = stationTrackDal;
    }

    public void fetchAndUploadTrack() {
        Application.logger.debug("LOG: UPLOADING: STARTED *************************");
        StationTrack stationTrack = fetchStationTrack();

        uploadTrack(appConfigs.getAwsBucketName(), stationTrack);
    }

    public StationTrack fetchStationTrack() {

        List<StationTrack> stationTracks = stationTrackDal.findAll();

        for (StationTrack stationTrack : stationTracks) {

            if (stationTrack.getAddToStation() == 1 && stationTrack.getUploaded() == 0) {
                return stationTrack;
            }
        }

        return null;
    }

    //String stationTrackName, String stationTrackPath
    public void uploadTrack(String s3Bin, StationTrack stationTrack) {
        Application.logger.debug("LOG: UPLOADING: BUCKET: " + s3Bin);
        
        if (s3Bin == null || stationTrack == null) {
            Application.logger.debug("LOG: UPLOADING: ABORTING!!!!!");
            return;
        }

        Application.logger.debug("LOG: UPLOADING: STATION TRACK: " + stationTrack.getFile());

        try {
            //stationTrack.getFile() + ".wav", FINAL_MIX_OUTPUT_WITH_ESCAPE_QUOTES + stationTrackName
            //audioTransferService.uploadAudioFile(s3Bin, "3652c2c7-1d28-4b9d-8997-5c49f0e450c5.wav", "C:\\Program Files\\Apache Software Foundation\\Tomcat 8.0\\webapps\\audio\\3652c2c7-1d28-4b9d-8997-5c49f0e450c5.wav");

            File[] outputFiles = getFilesInOutputDirectory(appConfigs.getFinalMixOutput());
            Application.logger.debug("LOG: UPLOADING: NUM OF OUTPUT FILES: " + outputFiles.length);

            for (File file : outputFiles) {
                if (file.getName().contains(stationTrack.getFile()) && (file.getName().contains("_0_1.mp3"))) {
                    //upload mp3
                    audioTransferService.uploadAudioFile(s3Bin, file.getName(), appConfigs.getFinalMixOutput() + "/" + file.getName());

                    //upload tar package
                    String itemId = file.getName().replace("_0_1.mp3", "");
                    String tarSource = Paths.get(appConfigs.getTarPackage(), itemId + ".tar").toString();
                    audioTransferService.uploadAudioFile(s3Bin, itemId + ".tar",
                            tarSource);

                    //upload untared package
                    audioTransferService.uploadAudioDirectory(s3Bin, itemId, Paths.get(appConfigs.getTarPackage(), itemId).toFile());
                }
            }

            stationTrack.setUploaded(1);
            stationTrackDal.save(stationTrack);
        } catch (AmazonClientException amazonClientException) {
            Application.logger.debug("LOG: Unable to upload file, upload was aborted.");
            Application.logger.debug("LOG: UPLOADING: " + amazonClientException);
        } catch (InterruptedException e) {
            Application.logger.debug("LOG:" + e);
        }
    }

    private File[] getFilesInOutputDirectory(String outputDirectoryPath) {
        File folder = new File(outputDirectoryPath);
        File[] listOfFiles = folder.listFiles();

        if(listOfFiles == null){
            return new File[]{};
        }

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                Application.logger.debug("LOG: File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                Application.logger.debug("LOG: Directory " + listOfFiles[i].getName());
            }
        }
        return listOfFiles;
    }
}
