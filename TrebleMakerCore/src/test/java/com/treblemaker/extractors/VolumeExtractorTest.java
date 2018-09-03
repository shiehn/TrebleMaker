package com.treblemaker.extractors;

import com.treblemaker.SpringConfiguration;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class VolumeExtractorTest extends TestCase {
    @Autowired
    VolumeExtractor volumeExtractor;

    Map<String, File> roleToFileMap;

    private final String ORIGINAL_PATH = Paths.get(
            "src",
            "main",
            "java",
            "com",
            "treblemaker",
            "tests",
            "Mocks",
            "meanLoopTest").toString();

    private final String keyOne = "keyone";
    private final String keyTwo = "keytwo";
    private final String keyThree = "keythree";
    private final String keyFour = "keyfour";
    private final String keyFive = "keyfive";

    private final String fileOne =   Paths.get(ORIGINAL_PATH,"70_A_RevPad_01_SP_processing.wav").toString();
    private final String fileTwo =   Paths.get(ORIGINAL_PATH , "70_Am_Chords_02_SP_processing.wav").toString();
    private final String fileThree = Paths.get(ORIGINAL_PATH , "70_Bm_Chords_03_SP_processing.wav").toString();
    private final String fileFour =  Paths.get(ORIGINAL_PATH , "70_Bm_Chords_04_SP_processing.wav").toString();
    private final String fileFive =  Paths.get(ORIGINAL_PATH , "70_Bm_RhodesChordsProcessed_02_SP_processing.wav").toString();

    @Before
    public void setup(){

        roleToFileMap = new HashMap<>();

        roleToFileMap.put(keyOne, new File(fileOne));
        roleToFileMap.put(keyTwo, new File(fileTwo));
        roleToFileMap.put(keyThree, new File(fileThree));
        roleToFileMap.put(keyFour, new File(fileFour));
        roleToFileMap.put(keyFive, new File(fileFive));
    }

    @Test
    public void shouldCreateMapOfMeans() throws InterruptedException {

        assertThat(new File(ORIGINAL_PATH)).exists();

        List<Map<String, Double>> volumeMeans = volumeExtractor.getVolumeMeans(Arrays.asList(roleToFileMap));

        assertThat(volumeMeans.get(0).get(keyOne)).isEqualTo(-25.4);
        assertThat(volumeMeans.get(0).get(keyTwo)).isEqualTo(-34.0);
        assertThat(volumeMeans.get(0).get(keyThree)).isEqualTo(-25.1);
        assertThat(volumeMeans.get(0).get(keyFour)).isEqualTo(-24.9);
        assertThat(volumeMeans.get(0).get(keyFive)).isEqualTo(-24.4);
    }
}