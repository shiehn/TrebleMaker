package com.treblemaker.services;

import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.utils.TAR;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        List<File> filesToTar = Arrays.asList(
                src_metadata,
                src_melody,
                src_hi,
                src_mid,
                src_low,
                src_kick_midi,
                src_snare_midi,
                src_hat_midi,
                src_kick,
                src_snare,
                src_hat
        );

        //MAKE UNTARED PACKAGE
        Path unTaredDir = Paths.get(appConfigs.getTarPackage(), queueItem.getQueueItemId());
        unTaredDir.toFile().mkdirs();

        for(File source: filesToTar) {
            File dest = Paths.get(appConfigs.getTarPackage(), queueItem.getQueueItemId(), source.getName()).toFile();
            try {
                FileUtils.copyFile(source, dest);
            } catch (IOException e) {
                Application.logger.debug("LOG: ERROR : FAILED TO CREATE UNTARED PACKAGE: " + dest);
            }
        }

        //TAR THE PACKAGE
        String tarTarget = Paths.get(appConfigs.getTarPackage(), queueItem.getQueueItemId() + ".tar").toString();

        try {
            TAR.compress(tarTarget,
                    filesToTar.get(0),
                    filesToTar.get(1),
                    filesToTar.get(2),
                    filesToTar.get(3),
                    filesToTar.get(4),
                    filesToTar.get(5),
                    filesToTar.get(6),
                    filesToTar.get(7),
                    filesToTar.get(8),
                    filesToTar.get(9),
                    filesToTar.get(10)
            );
        } catch (IOException e) {
            Application.logger.debug("LOG: ERROR : FAILED TO PACKAGE TAR !!!");
        }


    }
}
