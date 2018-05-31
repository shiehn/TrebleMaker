package com.treblemaker.integration;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.dal.interfaces.ISynthTemplateDal;
import com.treblemaker.model.SynthTemplate;
import com.treblemaker.renderers.SoundFountRender;
import com.treblemaker.services.audiofilter.NormalizeAudio;
import com.treblemaker.utils.FileStructure;
import com.treblemaker.utils.interfaces.IAudioUtils;
import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

import static com.treblemaker.renderers.interfaces.IAudioRender.Spectrum;
import static com.treblemaker.renderers.interfaces.IAudioRender.Spectrum.*;
import static org.assertj.core.api.Assertions.assertThat;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"return_queue_early_for_tests=true","queue_scheduled_interval=8999999","queue_scheduled_start_delay=8999999","spring.datasource.url=jdbc:mysql://localhost:3306/hivecomposedb","spring.datasource.username=root","spring.datasource.password=redrobes79D"})
public class SoundfontRenderTests {

    private SoundFountRender soundFountRender;

    public SoundfontRenderTests() {
        soundFountRender = new SoundFountRender();
    }

    @Autowired
    private ISynthTemplateDal synthTemplateDal;

    @Autowired
    private IAudioUtils audioUtils;

    @Autowired
    private NormalizeAudio normalizeAudio;

    @Autowired
    public AppConfigs appConfigs;

    private final String testpath = "C:\\Capricious\\CapriciousEngine\\src\\main\\java\\com\\treblemaker\\tests\\Mocks\\soundfonttestfiles\\";

    private final int BPM = 80;

    @Test
    public void shouldRendSoundfont() throws Exception {

        int[] HI_OCTAVES = {4, 5};
        int[] MID_OCTAVES = {4};

        Spectrum[] testSpectrums = {HI, ALT_HI, MID, ALT_MID, LOW, ALT_LOW};
        Iterable<SynthTemplate> synthTemplates = synthTemplateDal.findAll();

        for (SynthTemplate synthTemplate : synthTemplates) {
            for (Spectrum spectrum : testSpectrums) {

                int[] OCTAVES = {};

                switch (spectrum) {
                    case HI:
                        OCTAVES = HI_OCTAVES;
                        break;
                    case ALT_HI:
                        OCTAVES = HI_OCTAVES;
                        break;
                    case MID:
                        OCTAVES = MID_OCTAVES;
                        break;
                    case ALT_MID:
                        OCTAVES = MID_OCTAVES;
                        break;
                    case LOW:
                        OCTAVES = MID_OCTAVES;
                        break;
                    case ALT_LOW:
                        OCTAVES = MID_OCTAVES;
                        break;
                    default:
                        throw new RuntimeException();
                }

                for (int octave : OCTAVES) {

                    cleanUpFiles();

                    String pattern = "C"
                            + octave + "h D"
                            + octave + "h E"
                            + octave + "h F"
                            + octave + "h G"
                            + octave + "h A"
                            + octave + "h B"
                            + octave + "h C"
                            + octave + "h D"
                            + octave + "h E"
                            + octave + "h F"
                            + octave + "h G"
                            + octave + "h A"
                            + octave + "h B"
                            + octave + "h C"
                            + octave + "h";

                    //CREATE NOTES ..
                    Pattern jPattern = new Pattern(pattern);

                    //CREATE MIDI FILE ..
                    MidiFileManager.save(new Player().getSequence(jPattern), new File(testpath + "/" + appConfigs.COMP_HI_FILENAME));

                    //RENDER SHIT
                    soundFountRender.renderPart(testpath + appConfigs.COMP_HI_FILENAME,
                            testpath + appConfigs.COMP_HI_AUDIO_FILENAME,
                            spectrum,
                            synthTemplate, BPM);

                    Thread.sleep(3500);

                    renderEmptyWav(spectrum, synthTemplate, octave);

                    Thread.sleep(3500);

                    normalizeAudio.normalizeFilesInADirectory(testpath);

                    Thread.sleep(3500);

                    double eqMean = Math.abs(audioUtils.getMeanVolume(new File(testpath + "/" + appConfigs.COMP_HI_FILENAME)));

                    double emptyEqMean = Math.abs(audioUtils.getMeanVolume(new File(testpath + "/" + appConfigs.EMPTY_TEST_WAV)));

                    System.out.println("TESTING SYNTH TEMPLATE ID " + synthTemplate.getId());
                    System.out.println("TESTING OCTAVE " + octave);
                    System.out.println("TESTING SPECTRUM " + spectrum);
                    System.out.println("EQ MEAN : " + eqMean);
                    System.out.println("EQ MEAN : " + eqMean);
                    System.out.println("Empty EQ MEAN : " + emptyEqMean);
                    System.out.println("Empty EQ MEAN : " + emptyEqMean);

                    double eqDiff = Math.abs(eqMean - emptyEqMean);

                    assertThat(eqDiff).isLessThan(15.0);
                }
            }
        }
    }

    public void cleanUpFiles() {
        FileStructure.deleteSingleFile(testpath + appConfigs.COMP_HI_FILENAME);
        FileStructure.deleteSingleFile(testpath + appConfigs.COMP_HI_AUDIO_FILENAME);
        FileStructure.deleteSingleFile(testpath + appConfigs.EMPTY_TEST_WAV);
    }

    private void renderEmptyWav(Spectrum spectrum, SynthTemplate synthTemplate, int octave) throws Exception {

        // CREATE NOTES ..
         Pattern jPattern = new Pattern("G" + octave + "q Rq Rh Rw Rw Rw Rw Rw Rw Rw Rw G" + octave + "q Rq Rh");

        //CREATE MIDI FILE ..
        MidiFileManager.save(new Player().getSequence(jPattern), new File(testpath + "/" + appConfigs.EMPTY_TEST_MIDI));

        //RENDER SHIT
        soundFountRender.renderPart(testpath + "/" + appConfigs.EMPTY_TEST_MIDI,
                testpath + "/" + appConfigs.EMPTY_TEST_WAV,
                spectrum,
                synthTemplate, BPM);
    }
}
