package com.treblemaker.integration;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.dal.interfaces.IBeatLoopsDal;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.renderers.interfaces.IBeatLoopRenderer;
import com.treblemaker.utils.FileStructure;
import com.treblemaker.utils.interfaces.IAudioUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Ignore
@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class BeatLoopRenderTests {

    private static final int NUMBER_OF_ITERATIONS = 500;
    private static final int EXPECTED_SECONDS = 96;
    private static final int MAX_BAR_COUNT = 32;
    private static final String TARGET_FILE_NAME = "render_beat_test.wav";
    private static final String TARGET_FILE_PATH = "C:\\Capricious\\CapriciousEngine\\src\\main\\java\\com\\treblemaker\\tests\\Mocks\\render\\";

    @Autowired
    private IBeatLoopsDal beatLoopsDal;

    @Autowired
    private IBeatLoopRenderer beatLoopRenderer;

    @Autowired
    private IAudioUtils audioUtils;

    @Test
    public void thirtyTwoBarsShouldEqual96Seconds() throws IOException {

        List<BeatLoop> beatLoops = beatLoopsDal.findAll().stream().filter(beatLoop -> beatLoop.getBpm() == 80).collect(Collectors.toList());


        for(int i=0; i<NUMBER_OF_ITERATIONS; i++) {

            FileStructure.deleteFile(new File(TARGET_FILE_PATH + TARGET_FILE_NAME));

            int barLength = 0;

            List<BeatLoop> testBeats = new ArrayList<>();

            while (barLength < MAX_BAR_COUNT) {

                BeatLoop selectedLoop = beatLoops.get(new Random().nextInt(beatLoops.size()));
                if(barLength <= (MAX_BAR_COUNT - selectedLoop.getBarCount())){
                    barLength =  barLength  +  selectedLoop.getBarCount();
                    testBeats.add(selectedLoop);
                }
            }

            int newTotal = 0;
            String ids = "";
            for (BeatLoop loop : testBeats) {
                newTotal = newTotal + loop.getBarCount();
                ids = ids + "," + loop.getId();
            }

            assertThat(newTotal).isEqualTo(MAX_BAR_COUNT);
            System.out.println("SIZE = " + newTotal);

            beatLoopRenderer.concatenateAndFinalizeRendering("FIX THIS!!!!", TARGET_FILE_PATH,TARGET_FILE_NAME,testBeats);

            System.out.println("FINISHED BEAT CONCATE");

            float audioLengthInSeconds = audioUtils.getAudioLength(TARGET_FILE_PATH + TARGET_FILE_NAME);

            System.out.println("TESTing BEAT Render IDS : " + ids);
            System.out.println("audioLengthInSeconds = " + audioLengthInSeconds);

            assertThat(audioLengthInSeconds).isGreaterThan(EXPECTED_SECONDS-1).isLessThan(EXPECTED_SECONDS+1);
        }
    }
}
