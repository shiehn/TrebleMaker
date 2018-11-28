package com.treblemaker.services;

import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.stations.StationTrack;
import com.treblemaker.utils.TAR;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PackagingService {

    AppConfigs appConfigs;
    StationTrack stationTrack;
    Integer numOfMelodies;

    public PackagingService(AppConfigs appConfigs, StationTrack stationTrack, Integer numOfMelodies){
        this.appConfigs = appConfigs;
        this.stationTrack = stationTrack;
        this.numOfMelodies = numOfMelodies;
    }

    public void tar(){
        File src_melody = (Paths.get(appConfigs.getCompositionOutput(), "midioutput", stationTrack.getFile(), "0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_"+(stationTrack.getSelectedMelody()-1)+".mid"))).toFile();

        File src_metadata = new File(appConfigs.getMetadataPath(stationTrack.getFile()));

        File src_hi = (Paths.get(appConfigs.getCompositionOutput(), "midioutput", stationTrack.getFile(), "0" + appConfigs.COMP_HI_FILENAME)).toFile();
        File src_mid = (Paths.get(appConfigs.getCompositionOutput(), "midioutput", stationTrack.getFile(), "0" + appConfigs.COMP_MID_FILENAME)).toFile();
        File src_low = (Paths.get(appConfigs.getCompositionOutput(), "midioutput", stationTrack.getFile(), "0" + appConfigs.COMP_LOW_FILENAME)).toFile();
        File src_kick_midi  = (Paths.get(appConfigs.getCompositionOutput(), "midioutput", stationTrack.getFile(), "0" + appConfigs.COMP_KICK_FILENAME)).toFile();
        File src_snare_midi  = (Paths.get(appConfigs.getCompositionOutput(), "midioutput", stationTrack.getFile(), "0" + appConfigs.COMP_SNARE_FILENAME)).toFile();
        File src_hat_midi  = (Paths.get(appConfigs.getCompositionOutput(), "midioutput", stationTrack.getFile(), "0" + appConfigs.COMP_HATS_FILENAME)).toFile();

        String stereoPartsPath = appConfigs.getCompositionOutput() + "/stereoparts/" + stationTrack.getFile();
        File src_kick = (Paths.get(stereoPartsPath, appConfigs.KICK_FILENAME)).toFile();
        File src_snare = (Paths.get(stereoPartsPath, appConfigs.SNARE_FILENAME)).toFile();
        File src_hat = (Paths.get(stereoPartsPath, appConfigs.HAT_FILENAME)).toFile();

        List<File> filesToTar = new ArrayList<>();
        filesToTar.add(src_metadata);
        filesToTar.add(src_hi);
        filesToTar.add(src_mid);
        filesToTar.add(src_low);
        filesToTar.add(src_kick_midi);
        filesToTar.add(src_snare_midi);
        filesToTar.add(src_hat_midi);
        filesToTar.add(src_kick);
        filesToTar.add(src_snare);
        filesToTar.add(src_hat);
        filesToTar.add(src_melody);

        //MAKE UNTARED PACKAGE
        Path unTaredDir = Paths.get(appConfigs.getTarPackage(), stationTrack.getFile());
        unTaredDir.toFile().mkdirs();

        for(File source: filesToTar) {
            File dest = Paths.get(appConfigs.getTarPackage(), stationTrack.getFile(), source.getName()).toFile();
            try {
                FileUtils.copyFile(source, dest);
            } catch (IOException e) {
                Application.logger.debug("LOG: ERROR : FAILED TO CREATE UNTARED PACKAGE: " + dest);
            }
        }

        //TAR THE PACKAGE
        String tarTarget = Paths.get(appConfigs.getTarPackage(), stationTrack.getFile() + ".tar").toString();

        try {
            TAR.compress(tarTarget,filesToTar.toArray(new File[filesToTar.size()]));
        } catch (IOException e) {
            Application.logger.debug("LOG: ERROR : FAILED TO PACKAGE TAR !!! " + e);
        }
    }

    public void removeUnusedFiles(StationTrack stationTrack, File tmpTar) {
        List<String> filesToDelete = new ArrayList<>();

        if(stationTrack.getSelectedMelody() == 1) {
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_1.mid"));
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_2.mid"));
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_3.mid"));
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_4.mid"));
        }else if(stationTrack.getSelectedMelody() == 2){
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_0.mid"));
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_2.mid"));
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_3.mid"));
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_4.mid"));
        }else if(stationTrack.getSelectedMelody() == 3){
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_0.mid"));
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_1.mid"));
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_3.mid"));
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_4.mid"));
        }else if(stationTrack.getSelectedMelody() == 4){
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_0.mid"));
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_1.mid"));
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_2.mid"));
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_4.mid"));
        }else if(stationTrack.getSelectedMelody() == 5){
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_0.mid"));
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_1.mid"));
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_2.mid"));
            filesToDelete.add("./0" + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_3.mid"));
        }else{
            throw new RuntimeException("unexpected selected melody - cannot remove file");
        }

        for(String fileToDelete : filesToDelete) {
            String command = "tar --delete --file=" + tmpTar.getAbsolutePath() + " " + fileToDelete;
            Process p = null;
            try {
                p = Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int exitStatus = p.exitValue();

            if (exitStatus != 0) {
                throw new RuntimeException("error removing file from tar");
            }
        }

        return;
    }
}
