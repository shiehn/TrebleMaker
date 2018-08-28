package com.treblemaker.services;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.*;
import com.amazonaws.services.s3.transfer.internal.S3ProgressListener;
import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.dal.interfaces.*;
import com.treblemaker.model.AmbienceLoop;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.SoftSynths;
import com.treblemaker.model.queues.QueueAudioTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AudioTransferService {

    @Autowired
    private IQueueAudioTransferDal queueAudioTransferDal;

    @Autowired
    private IHarmonicLoopsDal harmonicLoopsDal;

    @Autowired
    private IAmbienceLoopsDal ambienceLoopsDal;

    @Autowired
    private IBeatLoopsDal beatLoopsDal;

    @Autowired
    private ISoftSynthsDal softSynthsDal;

    @Autowired
    private AppConfigs appConfigs;

    private void flagLoopAsUsable(int loopId, int loopType) {

        switch (loopType) {

            case QueueAudioTransfer.LOOP_TYPE_BEAT:
                BeatLoop beatLoop = beatLoopsDal.findOne(loopId);
                beatLoop.setLoopTransferComplete(true);
                beatLoopsDal.save(beatLoop);
                return;
            case QueueAudioTransfer.LOOP_TYPE_HARMONIC:
                HarmonicLoop harmonicLoop = harmonicLoopsDal.findOne(loopId);
                harmonicLoop.setLoopTransferComplete(true);
                harmonicLoopsDal.save(harmonicLoop);
                return;
            case QueueAudioTransfer.LOOP_TYPE_AMBIENT:
                AmbienceLoop ambienceLoop = ambienceLoopsDal.findOne(loopId);
                ambienceLoop.setLoopTransferComplete(true);
                ambienceLoopsDal.save(ambienceLoop);
                return;
            case QueueAudioTransfer.LOOP_TYPE_SOUND_FONT:
                SoftSynths softSynths = softSynthsDal.findOne(loopId);
                softSynths.setLoopTransferComplete(true);
                softSynthsDal.save(softSynths);
                return;
        }
    }

    //TODO THIS NEEDS TO BE MOVED into its own service!!!
    private void addQueueItem(String filePath) {

        QueueAudioTransfer queueAudioTransfer = new QueueAudioTransfer();
        queueAudioTransfer.setState(QueueAudioTransfer.PROGRESS_UNSTARTED);
        queueAudioTransfer.setFilepath(filePath);
        queueAudioTransfer.setDate(Calendar.getInstance().getTime());

        //TODO FIX THESE  NUMBERS
        queueAudioTransfer.setLoop_type(999);
        queueAudioTransfer.setLoop_type(999);

        queueAudioTransferDal.save(queueAudioTransfer);
    }

    public void downloadAudioFile() {

        QueueAudioTransfer queueItem = null;

        List<QueueAudioTransfer> queueItems = queueAudioTransferDal.findAll()
                .stream()
                .filter(q -> q.getState() == QueueAudioTransfer.PROGRESS_UNSTARTED)
                .collect(Collectors.toList());

        if (!queueItems.isEmpty()) {
            queueItem = queueItems.stream().findFirst().get();
        } else {
            Application.logger.debug("LOG:","NO TRANSFER ITEMS");
            return;
        }

        String filename = queueItem.getFilename();
        Application.logger.debug("LOG:","FOUND TRANSFER ITEM!!");

        queueItem.setState(QueueAudioTransfer.PROGRESS_STARTED);
        queueAudioTransferDal.save(queueItem);

        TransferManager tm = new TransferManager(new ProfileCredentialsProvider());
        String downloadTargetPath = getDownloadTargetPath(queueItem.getFilename(), queueItem.getLoop_type());

        ensurePathExisits(downloadTargetPath);

        Application.logger.debug("LOG:","DOWNLOADING FILE : " + queueItem.getFilename());
        Application.logger.debug("LOG:","DOWNLOADING TO : " + downloadTargetPath + queueItem.getFilename());

        Download download = tm.download(appConfigs.getAwsBucketName(), queueItem.getFilename(), new File(downloadTargetPath + queueItem.getFilename()));

//        try {
        download.addProgressListener(new S3ProgressListener() {
            @Override
            public void onPersistableTransfer(PersistableTransfer persistableTransfer) {

            }

            @Override
            public void progressChanged(ProgressEvent progressEvent) {

            }
        });
        try {
            download.waitForCompletion();
        } catch (InterruptedException e) {
            Application.logger.debug("LOG:", e);
        }

        Application.logger.debug("LOG: DOWNLOAD COMPLETE !!!!!!!!!!!!!");
        Application.logger.debug("LOG: DOWNLOAD COMPLETE !!!!!!!!!!!!!");
        Application.logger.debug("LOG: DOWNLOAD COMPLETE !!!!!!!!!!!!!");
        Application.logger.debug("LOG: DOWNLOAD COMPLETE !!!!!!!!!!!!!");

        tm.shutdownNow();

        queueItem.setState(QueueAudioTransfer.PROGRESS_COMPLETE);
        queueAudioTransferDal.save(queueItem);

        flagLoopAsUsable(queueItem.getLoopId(), queueItem.getLoop_type());
    }

    private void ensurePathExisits(String downloadTargetPath) {

        File targetFile = new File(downloadTargetPath);

        if (!Files.exists(targetFile.toPath())) {
            targetFile.mkdirs();
        }
    }

    private String getDownloadTargetPath(String fileName, int loopType) {



//        switch (loopType) {
//
//            case QueueAudioTransfer.LOOP_TYPE_BEAT:
//                return appConfigs.BEAT_LOOPS_LOCATION;
//            case QueueAudioTransfer.LOOP_TYPE_HARMONIC:
//                return appConfigs.HARMONIC_LOOPS_LOCATION;
//            case QueueAudioTransfer.LOOP_TYPE_AMBIENT:
//                return appConfigs.AMBIENCE_LOOPS_LOCATION;
//            case QueueAudioTransfer.LOOP_TYPE_SOUND_FONT:
//                return appConfigs.SOUND_FONTS_LOCATION;
//        }

        throw new RuntimeException("STEVE YOU NEED TO DEAL WITH THIS .. ");
    }

    public void uploadAudioFile(String bucketName, String key, String filePath) throws InterruptedException {
        AmazonS3 s3 = new AmazonS3Client();
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        s3.setRegion(usWest2);

        Application.logger.debug("LOG:","INIT AWS TRANSFER");
        // TransferManager processes all transfers asynchronously,
        // so this call will return immediately.
        Application.logger.debug("LOG: UPLOADING: STARTED FOR: BUCKET:" + bucketName);
        Application.logger.debug("LOG: UPLOADING: STARTED FOR: KEY:" + key);
        Application.logger.debug("LOG: UPLOADING: STARTED FOR: FILEPATH: " + filePath);

        s3.putObject(new PutObjectRequest(bucketName, key, new File(filePath)));
    }

    public void uploadAudioDirectory(String bucketName, String key, File filePath) throws InterruptedException {
        AmazonS3 s3 = new AmazonS3Client();
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        s3.setRegion(usWest2);

        TransferManager tm = new TransferManager(s3);
        MultipleFileUpload upload = tm.uploadDirectory(bucketName,
                key, filePath, true);
        upload.waitForCompletion();
    }
}
