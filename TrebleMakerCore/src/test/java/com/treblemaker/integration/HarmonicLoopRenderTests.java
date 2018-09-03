package com.treblemaker.integration;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.dal.interfaces.IHarmonicLoopsDal;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.rendertransports.HarmonicLoopRenderTransport;
import com.treblemaker.renderers.interfaces.IHarmonicLoopRenderer;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Ignore
@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class HarmonicLoopRenderTests {

    private static final int NUMBER_OF_ITERATIONS = 500;
    private static final int EXPECTED_SECONDS = 96;
    private static final int MAX_BAR_COUNT = 32;
    private static final String TARGET_FILE_NAME = "render_harmonic_test.wav";
    private static final String TARGET_FILE_PATH = "C:\\Capricious\\CapriciousEngine\\src\\main\\java\\com\\treblemaker\\tests\\Mocks\\render\\";

    @Autowired
    private IHarmonicLoopsDal harmonicLoopsDal;

    @Autowired
    private IHarmonicLoopRenderer harmonicLoopRenderer;

    @Autowired
    private IAudioUtils audioUtils;

    @Test
    public void thirtyTwoBarsShouldEqual96Seconds() throws Exception {

        List<HarmonicLoop> harmonicLoops = harmonicLoopsDal.findAll().stream().filter(harmonicLoop -> harmonicLoop.getBpm() == 80).collect(Collectors.toList());

        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {

            FileStructure.deleteFile(new File(TARGET_FILE_PATH + TARGET_FILE_NAME));

            int barLength = 0;

            List<HarmonicLoop> testHarmonicLoops = new ArrayList<>();

            while (barLength < MAX_BAR_COUNT) {

                HarmonicLoop selectedLoop = harmonicLoops.get(new Random().nextInt(harmonicLoops.size()));
                if (barLength <= (MAX_BAR_COUNT - selectedLoop.getBarCount())) {
                    barLength = barLength + selectedLoop.getBarCount();
                    testHarmonicLoops.add(selectedLoop);
                }
            }

            int newTotal = 0;
            String ids = "";
            String names = "";
            for (HarmonicLoop loop : testHarmonicLoops) {
                newTotal = newTotal + loop.getBarCount();
                ids = ids + "," + loop.getId();
                names= names + ","+ loop.getFileName();
            }

            assertThat(newTotal).isEqualTo(MAX_BAR_COUNT);
            System.out.println("SIZE = " + newTotal);

            HarmonicLoopRenderTransport harmonicLoopRenderTransport = new HarmonicLoopRenderTransport();
            harmonicLoopRenderTransport.setHarmonicLoops(testHarmonicLoops);

            harmonicLoopRenderer.renderHarmonicLoops(TARGET_FILE_PATH, TARGET_FILE_NAME, harmonicLoopRenderTransport);

            float audioLengthInSeconds = audioUtils.getAudioLength(TARGET_FILE_PATH + TARGET_FILE_NAME);

            System.out.println("TESTing HArmonic Render IDS : " + ids);
            System.out.println("TESTing HArmonic Render names : " + names);
            System.out.println("audioLengthInSeconds = " + audioLengthInSeconds);

            assertThat(audioLengthInSeconds).isGreaterThan(EXPECTED_SECONDS - 1).isLessThan(EXPECTED_SECONDS + 1);
        }
    }
}
