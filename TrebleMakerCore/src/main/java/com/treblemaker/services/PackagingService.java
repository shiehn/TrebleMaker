package com.treblemaker.services;

import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.utils.TAR;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class PackagingService {

    AppConfigs appConfigs;
    QueueItem queueItem;

    public PackagingService(AppConfigs appConfigs, QueueItem queueItem){
        this.appConfigs = appConfigs;
        this.queueItem = queueItem;
    }

    public void tar(){
        File src_metadata = new File(appConfigs.getMetadataPath(queueItem.getQueueItemId()));
        File src_melody = (Paths.get(appConfigs.getCompositionOutput(), "midioutput", queueItem.getQueueItemId(), "0" + appConfigs.COMP_MELODIC_FILENAME)).toFile();
        File src_hi = (Paths.get(appConfigs.getCompositionOutput(), "midioutput", queueItem.getQueueItemId(), "0" + appConfigs.COMP_HI_FILENAME)).toFile();
        File src_mid = (Paths.get(appConfigs.getCompositionOutput(), "midioutput", queueItem.getQueueItemId(), "0" + appConfigs.COMP_MID_FILENAME)).toFile();
        File src_low = (Paths.get(appConfigs.getCompositionOutput(), "midioutput", queueItem.getQueueItemId(), "0" + appConfigs.COMP_LOW_FILENAME)).toFile();
        File src_kick_midi  = (Paths.get(appConfigs.getCompositionOutput(), "midioutput", queueItem.getQueueItemId(), "0" + appConfigs.COMP_KICK_FILENAME)).toFile();
        File src_snare_midi  = (Paths.get(appConfigs.getCompositionOutput(), "midioutput", queueItem.getQueueItemId(), "0" + appConfigs.COMP_SNARE_FILENAME)).toFile();
        File src_hat_midi  = (Paths.get(appConfigs.getCompositionOutput(), "midioutput", queueItem.getQueueItemId(), "0" + appConfigs.COMP_HATS_FILENAME)).toFile();

        File src_kick = (Paths.get(queueItem.getStereoPartsFilePath(), appConfigs.KICK_FILENAME)).toFile();
        File src_snare = (Paths.get(queueItem.getStereoPartsFilePath(), appConfigs.SNARE_FILENAME)).toFile();
        File src_hat = (Paths.get(queueItem.getStereoPartsFilePath(), appConfigs.HAT_FILENAME)).toFile();
        //File src_hits = (Paths.get(queueState.getQueueItem().getStereoPartsFilePath(), appConfigs.HITS_FILENAME)).toFile();
        //File src_fills = (Paths.get(queueState.getQueueItem().getStereoPartsFilePath(), appConfigs.FILLS_FILENAME)).toFile();

        //TAR THE PACKAGE
        String tarTarget = Paths.get(appConfigs.getTarPackage(), queueItem.getQueueItemId() + ".tar").toString();

        try {
            TAR.compress(tarTarget,
                    src_melody,
                    src_hi,
                    src_mid,
                    src_low,
                    src_kick_midi,
                    src_snare_midi,
                    src_hat_midi,
                    src_kick,
                    src_snare,
                    src_hat,
                    src_metadata
            );
        } catch (IOException e) {
            Application.logger.debug("LOG: ERROR : FAILED TO PACKAGE TAR !!!");
        }
    }
}
