package com.treblemaker.services.audiofilter;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.utils.FileStructure;
import com.treblemaker.utils.interfaces.IAudioUtils;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class NormalizeAudioTest extends TestCase {

    @Autowired
    NormalizeAudio normalizeAudio;

    @Autowired
    IAudioUtils audioUtils;

    @Autowired
    public AppConfigs appConfigs;

    private List<Path> filePaths_original = new ArrayList<>();
    private List<Path> filePaths = new ArrayList<>();

    private final String ORIGINAL_PATH = Paths.get("src","main","java","com","treblemaker","tests","Mocks","meanLoopTest").toString();

    private final String PATH = Paths.get("src","main","java","com","treblemaker","tests","Mocks","meanLoopTestTemp").toString();

    private final String file1_p = "70_A_RevPad_01_SP_processing.wav";
    private final String file2_p = "70_Am_Chords_02_SP_processing.wav";
    private final String file3_p = "70_Bm_Chords_03_SP_processing.wav";
    private final String file4_p = "70_Bm_Chords_04_SP_processing.wav";
    private final String file5_p = "70_Bm_RhodesChordsProcessed_02_SP_processing.wav";
    private final String file6_p = "70_Bm_RhodesChordsProcessed_SP_processing.wav";
    private final String file7_p = "70_Bm_UltrArp_01_SP_processing.wav";
    private final String file8_p = "70_Cm_Chords_01_SP_processing.wav";
    private final String file9_p = "70_Cs_m_MovingSynth_02_SP_processing.wav";
    private final String file10_p = "70_Dm_ProcessedChords_02_SP_processing.wav";

    private final String file1 = "70_A_RevPad_01_SP.wav";
    private final String file2 = "70_Am_Chords_02_SP.wav";
    private final String file3 = "70_Bm_Chords_03_SP.wav";
    private final String file4 = "70_Bm_Chords_04_SP.wav";
    private final String file5 = "70_Bm_RhodesChordsProcessed_02_SP.wav";
    private final String file6 = "70_Bm_RhodesChordsProcessed_SP.wav";
    private final String file7 = "70_Bm_UltrArp_01_SP.wav";
    private final String file8 = "70_Cm_Chords_01_SP.wav";
    private final String file9 = "70_Cs_m_MovingSynth_02_SP.wav";
    private final String file10 = "70_Dm_ProcessedChords_02_SP.wav";

    String originalAudioPath = Paths.get(appConfigs.getMockDataAudioDir(),"normalize_test.mp3").toString();
    String copiedAudioPath = Paths.get(appConfigs.getMockDataAudioDir(),"normalize_test_copy.mp3").toString();

    @Before
    public void setup() throws IOException {
        List<String> pathStrings_p = Arrays.asList(file1_p, file2_p, file3_p, file4_p, file5_p, file6_p, file7_p, file8_p, file9_p, file10_p);
        for (String strPath : pathStrings_p) {
            filePaths_original.add(Paths.get(PATH, strPath));
        }

        List<String> pathStrings = Arrays.asList(file1, file2, file3, file4, file5, file6, file7, file8, file9, file10);
        for (String strPath : pathStrings) {
            filePaths.add(Paths.get(PATH,strPath));
        }

        FileStructure.copyFolder(new File(ORIGINAL_PATH),new File(PATH));

        //SETUP FOR NORMALIZE MIX
        FileStructure.deleteFile(new File(copiedAudioPath.replace(".mp3", "_0_1.mp3")));
        FileStructure.deleteFile(new File(copiedAudioPath.replace(".mp3", "_0_2.mp3")));
        FileStructure.deleteFile(new File(copiedAudioPath.replace(".mp3", "_0_3.mp3")));

        FileStructure.copyFile(originalAudioPath, copiedAudioPath.replace(".mp3", "_0_1.mp3"));
        FileStructure.copyFile(originalAudioPath, copiedAudioPath.replace(".mp3", "_0_2.mp3"));
        FileStructure.copyFile(originalAudioPath, copiedAudioPath.replace(".mp3", "_0_3.mp3"));
    }

    @After
    public void tearDown(){
        FileStructure.deleteAllFilesInDirectory(PATH);
    }

    @Test
    public void shouldGetArrayOfMeans() {

        HashMap<Path, Double> means = normalizeAudio.createMeanVolumeMap(filePaths_original);
        assertThat(means).hasSize(10);
    }

    @Test
    public void shouldCalculateCorrectMean(){
        HashMap<Path, Double> means = new HashMap<>();
        means.put(new File("a").toPath(), 0.435);
        means.put(new File("b").toPath(), -13.435);
        means.put(new File("c").toPath(), -25.435);
        means.put(new File("d").toPath(), 0.135);

        Double mean = normalizeAudio.getMeanOfMeans(means);

        assertThat(mean).isEqualTo(-9.6);
    }

    @Test
    public void shouldCorrectOffsetForMeans(){
        HashMap<Path, Double> means = new HashMap<>();
        means.put(new File("a").toPath(), 0.435);
        means.put(new File("b").toPath(), -13.435);
        means.put(new File("c").toPath(), -25.435);
        means.put(new File("d").toPath(), 0.135);

        Double target = -9.6;

        HashMap<Path, Double> targetMap = normalizeAudio.calculateMeanOffset(target, means);

        assertThat(targetMap.get(new File("a").toPath())).isEqualTo(-10.0);
        assertThat(targetMap.get(new File("b").toPath())).isEqualTo(3.8);
        assertThat(targetMap.get(new File("c").toPath())).isEqualTo(15.8);
        assertThat(targetMap.get(new File("d").toPath())).isEqualTo(-9.7);
    }

    @Test
    public void shouldReturnFalseWhenNotInRange(){
        HashMap<Path, Double> means = new HashMap<>();
        means.put(new File("a").toPath(), 0.435);
        means.put(new File("b").toPath(), -13.435);
        means.put(new File("c").toPath(), -25.435);
        means.put(new File("d").toPath(), 0.135);

        Double target = -8.6;

        boolean withinRange = normalizeAudio.meansWithinTargetRange(target, means);

        assertThat(withinRange).isFalse();
    }

    @Test
    public void shouldReturnTreu_whenValueInRange(){
        HashMap<Path, Double> means = new HashMap<>();
        means.put(new File("a").toPath(), -9.5);
        means.put(new File("b").toPath(), -8.5);
        means.put(new File("c").toPath(), -9.2);
        means.put(new File("d").toPath(), -8.8);

        Double target = -9.0;

        boolean withinRange = normalizeAudio.meansWithinTargetRange(target, means);

        assertThat(withinRange).isTrue();
    }

    @Test
    public void shouldReturnTrue_whenMeanIsInRange(){
        Double mean = -9.5;
        Double target = -9.0;

        boolean withinRange = normalizeAudio.meanIsWithinTargetRange(target, mean);

        assertThat(withinRange).isTrue();
    }

    @Test
    public void shouldReturnFalse_whenMeanIsNotInRange(){
        Double mean = 9.5;

        Double target = -9.0;

        boolean withinRange = normalizeAudio.meanIsWithinTargetRange(target, mean);

        assertThat(withinRange).isFalse();
    }

    @Test
    public void shouldAdjustMeansToTarget() throws IOException {
        HashMap<Path, Double> meanMap = normalizeAudio.createMeanVolumeMap(filePaths_original);

        double targetMean = normalizeAudio.getMeanOfMeans(meanMap);

        normalizeAudio.normalize(meanMap, targetMean, PATH);

        HashMap<Path, Double> meanMapProcessed = normalizeAudio.createMeanVolumeMap(filePaths);
    }

    @Test
    public void shouldIncreaseVolumeWhenNormalizeAudioIsCalled() throws InterruptedException, IOException {
        double initialVolume = audioUtils.getMaxVolume(new File(originalAudioPath));
        normalizeAudio.normalizeMix(appConfigs.getMockDataAudioDir(), "normalize_test_copy", 1);
        double finalVolume = audioUtils.getMaxVolume(new File(copiedAudioPath.replace(".mp3", "_0_1.mp3")));

        assertThat(finalVolume).isGreaterThan(initialVolume);
        assertThat(finalVolume).isLessThanOrEqualTo(0.0);

        boolean hasBeenDeleted = Files.notExists(new File(Paths.get(appConfigs.getMockDataAudioDir(),"normalize_test_copy_0_1_processing.mp3").toString()).toPath());
        assertThat(hasBeenDeleted).isTrue();
    }
}