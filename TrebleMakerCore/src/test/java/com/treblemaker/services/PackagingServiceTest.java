//package com.treblemaker.services;
//
//import com.treblemaker.configs.AppConfigs;
//import com.treblemaker.model.queues.QueueItem;
//import com.treblemaker.model.stations.StationTrack;
//import com.treblemaker.utils.FileStructure;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//public class PackagingServiceTest {
//
//    private AppConfigs appConfigs;
//    private QueueItem queueItem;
//    private PackagingService packagingService;
//    private String tempDir;
//
//    @Before
//    public void setup(){
//        queueItem = new QueueItem();
//        queueItem.setQueueItemId("xxx");
//
//        appConfigs = new AppConfigs();
//        tempDir = Paths.get("packagetest").toString();
//
//        appConfigs.setApplicationRoot(tempDir);
//        appConfigs.appConfigs = appConfigs;
//        (new File(tempDir)).mkdirs();
//        (new File(appConfigs.getCompositionOutput())).mkdirs();
//        (new File(appConfigs.getTarPackage())).mkdirs();
//
//        packagingService = new PackagingService(appConfigs, queueItem.getQueueItemId(), 2);
//    }
//
//    @Test
//    public void shouldTarPackage(){
//        assertThat(appConfigs).isNotNull();
//        assertThat(new File(tempDir)).isDirectory();
//        packagingService.tar();
//        Path tarTarget = Paths.get(appConfigs.getTarPackage(), queueItem.getQueueItemId() + ".tar");
//        assertThat(tarTarget.toFile()).isFile();
//
//        //TODO THIS SHOULD UNTAR THE FILE AND CONFIRM IT CONTAINS EVERYTHING!!
//        //TODO THIS SHOULD UNTAR THE FILE AND CONFIRM IT CONTAINS EVERYTHING!!
//        //TODO THIS SHOULD UNTAR THE FILE AND CONFIRM IT CONTAINS EVERYTHING!!
//        //TODO THIS SHOULD UNTAR THE FILE AND CONFIRM IT CONTAINS EVERYTHING!!
//        //TODO THIS SHOULD UNTAR THE FILE AND CONFIRM IT CONTAINS EVERYTHING!!
//        //TODO THIS SHOULD UNTAR THE FILE AND CONFIRM IT CONTAINS EVERYTHING!!
//
//        //TODO these files should exist
//        /*
//        0compmelodic.mid is not supported
//        0comphi.mid is not supported
//        0compmid.mid is not supported
//        0complow.mid is not supported
//        0compkick.mid is not supported
//        0compsnare.mid is not supported
//        0comphats.mid is not supported
//        kick.wav is not supported
//        snare.wav is not supported
//        hat.wav is not supported
//        metadata.txt is not supported
//        */
//    }
//
//    @Test
//    public void shouldRemoveUnusedMelody() throws IOException {
//        Path assetPath = Paths.get("src", "main", "java", "com", "treblemaker", "tests", "Mocks");
//        File sourceTar = Paths.get(assetPath.toString(), "testasset.tar").toFile();
//        assertThat(sourceTar).exists();
//
//       //duplicate the file
//        File tmpTar = Paths.get(assetPath.toString(), "tmp.tar").toFile();
//        cleanUpTar(tmpTar);
//        Files.copy(sourceTar.toPath(), tmpTar.toPath());
//        assertThat(tmpTar).exists();
//
//        //confirm it contains 0compmelodic_0.mid & 0compmelodic_1.mid
//        assertThat(tarContainsFile("0compmelodic_0.mid",tmpTar)).isTrue();
//        assertThat(tarContainsFile("0compmelodic_1.mid",tmpTar)).isTrue();
//
//        StationTrack stationTrack = new StationTrack();
//        stationTrack.setSelectedMelody(1);
//        packagingService.removeUnusedFiles(stationTrack , tmpTar);
//
//        //confirm it no LONGER contains 0compmelodic_0.mid
//        assertThat(tarContainsFile("0compmelodic_0.mid",tmpTar)).isFalse();
//        assertThat(tarContainsFile("0compmelodic_1.mid",tmpTar)).isTrue();
//
//        cleanUpTar(tmpTar);
//    }
//
//    private boolean tarContainsFile(String fileName, File tar){
//        String command = "tar tf " + tar.getAbsolutePath() + " ./" + fileName;
//        Process p = null;
//        try {
//            p = Runtime.getRuntime().exec(command);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            p.waitFor();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        int exitStatus = p.exitValue();
//        return exitStatus == 0;
//    }
//
//    /*
//    String command = "diff "+mp.get("directory")+"/"+fileName+" "+mp.get("outdir")+"/"+fileName;
//Process p = Runtime.getRuntime().exec(command);
//p.waitFor();
//int exitStatus = p.exitValue();
//     */
//
//
//
//
//    private void cleanUpTar(File tarFile){
//        try {
//            Files.deleteIfExists(tarFile.toPath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @After
//    public void tearDown(){
//        FileStructure.deleteAllFilesInDirectory(tempDir);
//        assertThat(new File(tempDir)).doesNotExist();
//    }
//
////    public void createMidFiles(){
////
////        System.out.println("APP ROOT: " + appConfigs.getCompositionOutput());
////
////        File src_metadata = new File(appConfigs.getMetadataPath(queueItem.getQueueItemId()));
////        (Paths.get(appConfigs.getCompositionOutput(), "midioutput", queueItem.getQueueItemId(), "0" + appConfigs.COMP_MELODIC_FILENAME)).toFile().mkdirs();
////        (Paths.get(appConfigs.getCompositionOutput(), "midioutput", queueItem.getQueueItemId(), "0" + appConfigs.COMP_HI_FILENAME)).toFile().mkdirs();
////        (Paths.get(appConfigs.getCompositionOutput(), "midioutput", queueItem.getQueueItemId(), "0" + appConfigs.COMP_MID_FILENAME)).toFile().mkdirs();
////        (Paths.get(appConfigs.getCompositionOutput(), "midioutput", queueItem.getQueueItemId(), "0" + appConfigs.COMP_LOW_FILENAME)).toFile().mkdirs();
////        (Paths.get(appConfigs.getCompositionOutput(), "midioutput", queueItem.getQueueItemId(), "0" + appConfigs.COMP_KICK_FILENAME)).toFile().mkdirs();
////        (Paths.get(appConfigs.getCompositionOutput(), "midioutput", queueItem.getQueueItemId(), "0" + appConfigs.COMP_SNARE_FILENAME)).toFile().mkdirs();
////        (Paths.get(appConfigs.getCompositionOutput(), "midioutput", queueItem.getQueueItemId(), "0" + appConfigs.COMP_HATS_FILENAME)).toFile().mkdirs();
////
////        (Paths.get(queueItem.getStereoPartsFilePath(), appConfigs.KICK_FILENAME)).toFile().mkdirs();
////        (Paths.get(queueItem.getStereoPartsFilePath(), appConfigs.SNARE_FILENAME)).toFile().mkdirs();
////        (Paths.get(queueItem.getStereoPartsFilePath(), appConfigs.HAT_FILENAME)).toFile().mkdirs();
////    }
//}