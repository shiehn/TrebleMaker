package com.treblemaker.eventchain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.treblemaker.Application;
import com.treblemaker.SpringConfiguration;
import com.treblemaker.TestBase;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.utils.AudioUtils;
import com.treblemaker.utils.FileStructure;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Ignore
@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class ProcessMixPanningTest extends TestBase {

    @Autowired
    private IEventChain processMixPanning;

    @Autowired
    private IEventChain setPanningEvent;

    @Autowired
    private AudioUtils audioUtils;

    @Autowired
    public AppConfigs appConfigs;

    String[] targetFilesArray = new String[]{
            "0compalthi.wav",
            "0compalthifx.wav",
            "0compaltlow.wav",
            "0compaltmid.wav",
            "0comphi.wav",
            "0comphifx.wav",
            "0complow.wav",
            "0compmelodic.wav",
            "0compmid.wav",
            "compambience.wav",
            "compharmonic.wav",
            "compharmonicalt.wav",
            "comprhythm.wav",
            "comprhythmalt.wav",
            "fill.wav",
            "hat.wav",
            "hit.wav",
            "kick.wav",
            "snare.wav"
    };

    QueueState queueState = null;
    QueueItem queueItem = null;

    /*
        if (Files.notExists(Paths.get(appConfigs.COMPOSITION_OUTPUT + "audioparts"))) {
            (new File(appConfigs.COMPOSITION_OUTPUT + "audioparts")).mkdir();
        }

        if (Files.notExists(Paths.get(appConfigs.COMPOSITION_OUTPUT + "audioparts"))) {
            (new File(appConfigs.COMPOSITION_OUTPUT + "monoparts")).mkdir();
        }

        if (Files.notExists(Paths.get(appConfigs.COMPOSITION_OUTPUT + "audioparts"))) {
            (new File(appConfigs.COMPOSITION_OUTPUT + "stereoparts")).mkdir();
        }
    */

    private final String TRACK_GUID = "3F7SAD7F88SADF";

    String AUDIO_PART_FILE_PATH;

    //C:\HiveComposeWeb\CapriciousEngine\src\main\java\com\treblemaker\tests\Mocks\mockAudio\monoProcessPanningTest.wav

    private void setup() {

        AUDIO_PART_FILE_PATH = appConfigs.getMockDataAudioDir();

        //FIRST TRY TO DELETE EVERYTHING ..

        FileStructure.deleteAllFilesInDirectory(Paths.get(AUDIO_PART_FILE_PATH,"audioparts",TRACK_GUID).toString());
        FileStructure.deleteAllFilesInDirectory(Paths.get(AUDIO_PART_FILE_PATH,"monoparts",TRACK_GUID).toString());
        FileStructure.deleteAllFilesInDirectory(Paths.get(AUDIO_PART_FILE_PATH,"stereoparts",TRACK_GUID).toString());

        //RECREATE FOLDER ..

        if (Files.notExists(Paths.get(AUDIO_PART_FILE_PATH,"audioparts"))) {
            (new File(Paths.get(AUDIO_PART_FILE_PATH,"audioparts").toString())).mkdir();
        }

        if (Files.notExists(Paths.get(AUDIO_PART_FILE_PATH,"audioparts",TRACK_GUID))) {
            (new File(Paths.get(AUDIO_PART_FILE_PATH,"audioparts",TRACK_GUID).toString())).mkdir();
        }

        if (Files.notExists(Paths.get(AUDIO_PART_FILE_PATH,"monoparts"))) {
            (new File(Paths.get(AUDIO_PART_FILE_PATH,"monoparts").toString())).mkdir();
        }

        if (Files.notExists(Paths.get(AUDIO_PART_FILE_PATH ,"monoparts",TRACK_GUID))) {
            (new File(Paths.get(AUDIO_PART_FILE_PATH ,"monoparts",TRACK_GUID).toString())).mkdir();
        }

        if (Files.notExists(Paths.get(AUDIO_PART_FILE_PATH,"stereoparts"))) {
            (new File(Paths.get(AUDIO_PART_FILE_PATH,"stereoparts").toString())).mkdir();
        }

        if (Files.notExists(Paths.get(AUDIO_PART_FILE_PATH,"stereoparts", TRACK_GUID))) {
            (new File(Paths.get(AUDIO_PART_FILE_PATH,"stereoparts",TRACK_GUID).toString())).mkdir();
        }

        //MAKE mono wave files ..

        List<String> targetFiles = Arrays.asList(targetFilesArray);

        for (String target : targetFiles) {
            File sourceFile = new File(Paths.get(appConfigs.getMockDataAudioDir(),"monoProcessPanningTest.wav").toString());
            File targetFile = new File(Paths.get(appConfigs.getMockDataAudioDir(),"audioparts", TRACK_GUID, target).toString());
            try {
                audioUtils.copyFile(sourceFile, targetFile);
            } catch (IOException e) {
                Application.logger.debug("LOG:", e);
            }
        }
    }

    @Test
    public void shouldCreateLeftAndRightMonoFiles() throws InterruptedException, JsonProcessingException {

        setup();

        File[] audioFiles = FileStructure.getFilesInDirectory(Paths.get(appConfigs.getMockDataAudioDir(),"audioparts",TRACK_GUID).toString());
        assertThat(audioFiles).isNotEmpty();

        QueueState queueState = getQueueStateWithProgression();
//        QueueItem queueItem = new QueueItem();
        queueState.getQueueItem().setQueueItemId(TRACK_GUID);
        queueState.getQueueItem().setAudioPartFilePath(Paths.get(appConfigs.getMockDataAudioDir(),"audioparts",TRACK_GUID + "/").toString());
        queueState.getQueueItem().setMonoPartsFilePath(Paths.get(appConfigs.getMockDataAudioDir(),"monoparts", TRACK_GUID + "/").toString());
        queueState.getQueueItem().setStereoPartsFilePath(Paths.get(appConfigs.getMockDataAudioDir(),"stereoparts",TRACK_GUID + "/").toString());
//        queueState.setQueueItem(queueItem);

        queueState = setPanningEvent.set(queueState);

        queueState = processMixPanning.set(queueState);

        File[] monoFiles = FileStructure.getFilesInDirectory(Paths.get(appConfigs.getMockDataAudioDir(),"monoparts",TRACK_GUID).toString());
        assertThat(monoFiles.length).isEqualTo(audioFiles.length * 2);

        for (File monoFileName : monoFiles) {
            boolean contains = monoFileName.getName().contains("_r.wav") || monoFileName.getName().contains("_l.wav");
            assertThat(contains).isTrue();
        }

        File[] stereoFiles = FileStructure.getFilesInDirectory(Paths.get(appConfigs.getMockDataAudioDir(),"stereoparts",TRACK_GUID).toString());
        //assert # of stereofiles is 1/2 the # of monoFiles
        assertThat(stereoFiles.length).isEqualTo((monoFiles.length / 2) - 2); //-2 is because hi_comp is dropped for hi_fx_comp
        assertThat(stereoFiles.length).isEqualTo(audioFiles.length - 2); //-2 is because hi_comp is dropped for hi_fx_comp

        for (File stereoFile : stereoFiles) {
            boolean doesNotContain = !stereoFile.getName().contains("_r.wav") && !stereoFile.getName().contains("_l.wav");
            assertThat(doesNotContain).isTrue();
        }
    }
}