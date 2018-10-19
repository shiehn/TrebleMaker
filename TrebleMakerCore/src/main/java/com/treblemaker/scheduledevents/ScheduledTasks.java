package com.treblemaker.scheduledevents;

import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.dal.interfaces.ICompositionDal;
import com.treblemaker.dal.interfaces.IQueueItemCustomDal;
import com.treblemaker.extractors.RhythmicExtractionUtil;
import com.treblemaker.extractors.pitchextraction.PitchExtractionUtil;
import com.treblemaker.healthmonitor.HealthMonitor;
import com.treblemaker.loadbalance.LoadBalancer;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.scheduledevents.interfaces.IQueueDigester;
import com.treblemaker.services.AudioTransferService;
import com.treblemaker.services.FeedBackFileSync;
import com.treblemaker.services.queue.QueueService;
import com.treblemaker.stations.StationUploadService;
import com.treblemaker.utils.loopcorrection.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@EnableAutoConfiguration
@Component
@ComponentScan({"com.treblemaker", "com.treblemaker.dal", "com.treblemaker.generators", "com.treblemaker.extractors", "com.treblemaker.scheduledevents", "com.treblemaker.utils",
        "com.treblemaker.machinelearning", "com.treblemaker.weighters", "com.treblemaker.zzz", "com.treblemaker.machinelearning.services"})
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private IQueueDigester queueDigester;
    private IQueueItemCustomDal queueItemCustomDal;
    private BeatLoopCorrectionUtil beatLoopCorrectionUtil;
    private HarmonicLoopCorrectionUtil harmonicLoopCorrectionUtil;
    private FillsLoopCorrectionUtil fillsLoopCorrectionUtil;
    private HitsLoopCorrectionUtil hitsLoopCorrectionUtil;
    private AmbienceLoopCorrectUtil ambienceLoopCorrectUtil;
    private ICompositionDal compositionDal;
    private AudioTransferService audioTransferService;
    private RhythmicExtractionUtil rhythmicExtractionUtil;
    private PitchExtractionUtil pitchExtractionUtil;
    private QueueService queueService;
    private HealthMonitor healthMonitor;
    private final String[] mlEndpoints;
    private final boolean useOnlyFirstMachinelearnEndpoint;

    @Autowired
    private StationUploadService stationUploadService;

    @Autowired
    private FeedBackFileSync feedBackFileSync;

    @Autowired
    private AppConfigs appConfigs;

    @Autowired
    public ScheduledTasks(IQueueDigester queueDigester, IQueueItemCustomDal queueItemCustomDal, BeatLoopCorrectionUtil beatLoopCorrectionUtil, HarmonicLoopCorrectionUtil harmonicLoopCorrectionUtil, FillsLoopCorrectionUtil fillsLoopCorrectionUtil, HitsLoopCorrectionUtil hitsLoopCorrectionUtil, AmbienceLoopCorrectUtil ambienceLoopCorrectUtil,
                          ICompositionDal compositionDal, AudioTransferService audioTransferService, RhythmicExtractionUtil rhythmicExtractionUtil,
                          PitchExtractionUtil pitchExtractionUtil,
                          QueueService queueService,
                          HealthMonitor healthMonitor,
                          @Value("${machinelearning_endpoints}") String[] mlEndpoints,
                          @Value("${use_only_first_machinelearn_endpoint}") boolean useOnlyFirstMachinelearnEndpoint) {
        this.queueDigester = queueDigester;
        this.queueItemCustomDal = queueItemCustomDal;
        this.beatLoopCorrectionUtil = beatLoopCorrectionUtil;
        this.harmonicLoopCorrectionUtil = harmonicLoopCorrectionUtil;
        this.fillsLoopCorrectionUtil = fillsLoopCorrectionUtil;
        this.hitsLoopCorrectionUtil = hitsLoopCorrectionUtil;
        this.ambienceLoopCorrectUtil = ambienceLoopCorrectUtil;
        this.compositionDal = compositionDal;
        this.audioTransferService = audioTransferService;
        this.rhythmicExtractionUtil = rhythmicExtractionUtil;
        this.pitchExtractionUtil = pitchExtractionUtil;
        this.queueService = queueService;
        this.mlEndpoints = mlEndpoints;
        this.useOnlyFirstMachinelearnEndpoint = useOnlyFirstMachinelearnEndpoint;
        this.healthMonitor = healthMonitor;

        //ONE TIME INSTANTIATION OF LOAD BALANCER ..
        // ONE TIME INSTANTIATION OF LOAD BALANCER ..
        LoadBalancer.getInstance().initLoadBalancer(useOnlyFirstMachinelearnEndpoint, mlEndpoints);
    }

    @Scheduled(fixedRateString = "${queue_scheduled_interval}", initialDelayString = "${queue_scheduled_start_delay}")
    public void startQueuedEvent() throws Exception {
        Application.logger.debug("LOG: ******************************************");
        Application.logger.debug("LOG: QUEUE EVENT FIRED ************************");
        Application.logger.debug("LOG: ******************************************");

        //UPLOAD any completed tracks to stations
        //ALSO UPLOADS TAR FILES
        stationUploadService.fetchAndUploadTrack();

        //SYNC COMPLETED TRACKS WITH FEED BACK INTERFACE ...
        File folder = new File(appConfigs.getFinalMixOutput());
        if(folder.exists()) {
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                if (file.isFile()) {
                    feedBackFileSync.sync(file);
                }
            }
        }else{
            Application.logger.debug("LOG: FINAL MIX DIRECTORY DOES NOT EXIST");
        }

        if (GlobalState.getInstance().isHealthMonitorInProgress()) {
            Application.logger.debug("LOG: HEALTH MONITOR STILL ACTIVE !!");
            return;
        }

        try {
            QueueItem queueItem = queueItemCustomDal.getQueueItem();

            if (queueItem != null && !GlobalState.getInstance().isQueueItemInprogress() && healthMonitor.mLServicesHealthCheckOk()) {

                Application.logger.debug("LOG: QUEUE_EVENT STARTED: " + dateFormat.format(new Date()));

                GlobalState.getInstance().setQueueItemInprogress(true);
                queueItemCustomDal.setQueueItemProcessing(queueItem.getQueueItemId());
                queueDigester.digest(queueItem);
            } else if (queueItem == null && !GlobalState.getInstance().isQueueItemInprogress() && healthMonitor.mLServicesHealthCheckOk()) {

                if (queueService.isAtRatedSongCapacity()) {
                    //fix this for local testing
                    queueService.deleteRatedCompositions();
                }
                //fix this for local testing
                queueService.deleteOrphanedCompositions();

                if (queueService.isAtUnratedSongCompacity()) {
                    Application.logger.debug("LOG: AT MAXIMUM UNRATED SONG CAPACITY .. NOT ADDING QUEUE ITEM");
                } else {
                    queueService.addQueueItem();
                    Application.logger.debug("LOG: QUEUE_EVENT ADDING NEW QUEUE ITEM: " + dateFormat.format(new Date()));
                }
            }
        } catch (Exception e) {
            Application.logger.debug("LOG: FUCKING BIG ERROR", e);

            GlobalState.getInstance().setQueueItemInprogress(false);
        }
    }
}
