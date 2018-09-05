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
    Integer numOfMelodies;

    public PackagingService(AppConfigs appConfigs, QueueItem queueItem, Integer numOfMelodies){
        this.appConfigs = appConfigs;
        this.queueItem = queueItem;
        this.numOfMelodies = numOfMelodies;
    }

    public void tar(){
        List<File> melodies = new ArrayList<>();
        for(int i=0; i<numOfMelodies; i++){
            File src_melody = (Paths.get(appConfigs.getCompositionOutput(), "midioutput", queueItem.getQueueItemId(), "0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_"+i+".mid"))).toFile();
            melodies.add(src_melody);
        }

        File src_metadata = new File(appConfigs.getMetadataPath(queueItem.getQueueItemId()));

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

        for (File melody:melodies) {
            filesToTar.add(melody);
        }

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
            TAR.compress(tarTarget,filesToTar.toArray(new File[filesToTar.size()]));
        } catch (IOException e) {
            Application.logger.debug("LOG: ERROR : FAILED TO PACKAGE TAR !!!");
        }
    }
}
