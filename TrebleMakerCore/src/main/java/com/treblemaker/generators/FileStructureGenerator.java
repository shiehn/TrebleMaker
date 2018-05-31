package com.treblemaker.generators;

import com.treblemaker.model.queues.QueueItem;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.stereotype.Component;
import com.treblemaker.configs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Component
public class FileStructureGenerator {

    @Autowired
    private AppConfigs appConfigs;

    public String CreateFolderStructureEvent(QueueItem queueItem) {
        //TODO THESE THREE LINES DON'T HAVE TO BE HERE ..
        String songName = queueItem.getQueueItemId();
        queueItem.setMidiFilePath(appConfigs.getCompositionOutput() + "/midioutput/" + songName);
        queueItem.setAudioPartFilePath(appConfigs.getCompositionOutput() + "/audioparts/" + songName);
        queueItem.setMonoPartsFilePath(appConfigs.getCompositionOutput() + "/monoparts/" + songName);
        queueItem.setStereoPartsFilePath(appConfigs.getCompositionOutput() + "/stereoparts/" + songName);

        if (Files.notExists(Paths.get(appConfigs.getCompositionOutput() + "/audioparts"))) {
            (new File(appConfigs.getCompositionOutput() + "/audioparts")).mkdirs();
        }

        if (Files.notExists(Paths.get(appConfigs.getCompositionOutput() + "/audioparts/" + songName))) {
            (new File(appConfigs.getCompositionOutput() + "/audioparts/" + songName)).mkdirs();
        }

        if (Files.notExists(Paths.get(appConfigs.getCompositionOutput() + "/monoparts"))) {
            (new File(appConfigs.getCompositionOutput() + "/monoparts")).mkdirs();
        }

        if (Files.notExists(Paths.get(appConfigs.getCompositionOutput() + "/monoparts/" + songName))) {
            (new File(appConfigs.getCompositionOutput() + "/monoparts/" + songName)).mkdirs();
        }

        if (Files.notExists(Paths.get(appConfigs.getCompositionOutput() + "/stereoparts"))) {
            (new File(appConfigs.getCompositionOutput() + "/stereoparts")).mkdirs();
        }

        if (Files.notExists(Paths.get(appConfigs.getCompositionOutput() + "/stereoparts/" + songName))) {
            (new File(appConfigs.getCompositionOutput() + "/stereoparts/" + songName)).mkdirs();
        }
        return songName;
    }
}
