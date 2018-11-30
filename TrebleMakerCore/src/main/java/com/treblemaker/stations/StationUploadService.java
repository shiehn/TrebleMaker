package com.treblemaker.stations;

import com.amazonaws.AmazonClientException;
import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.dal.interfaces.IMetaDataChordInfoDal;
import com.treblemaker.dal.interfaces.IMetaDataTrackInfoDal;
import com.treblemaker.dal.interfaces.IStationTrackDal;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.generators.MetaDataGenerator;
import com.treblemaker.model.stations.StationTrack;
import com.treblemaker.services.AudioTransferService;
import com.treblemaker.services.PackagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class StationUploadService {

    public AppConfigs appConfigs;
    private AudioTransferService audioTransferService;
    private IStationTrackDal stationTrackDal;
    private IMetaDataTrackInfoDal metaDataTrackInfoDal;
    private IMetaDataChordInfoDal metaDataChordInfoDal;

    @Value("${num_of_alt_melodies}")
    int numOfAltMelodies;

    @Autowired
    public StationUploadService(AppConfigs appConfigs,
                                AudioTransferService audioTransferService,
                                IStationTrackDal stationTrackDal,
                                IMetaDataTrackInfoDal metaDataTrackInfoDal,
                                IMetaDataChordInfoDal metaDataChordInfoDal) {
        this.audioTransferService = audioTransferService;
        this.stationTrackDal = stationTrackDal;
        this.metaDataTrackInfoDal = metaDataTrackInfoDal;
        this.metaDataChordInfoDal = metaDataChordInfoDal;
        this.appConfigs = appConfigs;
    }

    public void fetchAndUploadTrack() {
        Application.logger.debug("LOG: UPLOADING: STARTED *************************");
        StationTrack stationTrack = fetchStationTrackToUpload();

        if(stationTrack != null){

            //create metadata
            MetaDataGenerator metaDataGenerator = new MetaDataGenerator(appConfigs, stationTrack, metaDataTrackInfoDal.findAll(), metaDataChordInfoDal.findAll());
            metaDataGenerator.generate(appConfigs.getMetadataPath(stationTrack.getFile()));

            PackagingService packagingService = new PackagingService(appConfigs, stationTrack, numOfAltMelodies);
            packagingService.tar();
        }

        uploadTrack(appConfigs.getAwsBucketName(), stationTrack);
    }

    public StationTrack fetchStationTrackToUpload() {
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
            File[] outputFiles = getFilesInOutputDirectory(appConfigs.getFinalMixOutput());
            Application.logger.debug("LOG: UPLOADING: NUM OF OUTPUT FILES: " + outputFiles.length);

            for (File file : outputFiles) {
                if (file.getName().contains(stationTrack.getFile()) && (file.getName().contains("_0_1.mp3"))) {
                    String itemId = file.getName().replace("_0_1.mp3", "");
                    Path tarSource = Paths.get(appConfigs.getTarPackage(), itemId + ".tar");

                    //upload mp3
                    audioTransferService.uploadAudioFile(s3Bin, file.getName(), appConfigs.getFinalMixOutput() + "/" + file.getName());

                    //upload tar
                    audioTransferService.uploadAudioFile(s3Bin, itemId + ".tar",
                            tarSource.toString());

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
