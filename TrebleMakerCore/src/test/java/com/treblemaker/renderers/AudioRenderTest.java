package com.treblemaker.renderers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.treblemaker.SpringConfiguration;
import com.treblemaker.TestBase;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.model.SynthTemplate;
import com.treblemaker.renderers.interfaces.IAudioRender;
import com.treblemaker.utils.LoopUtils;
import com.treblemaker.utils.interfaces.IAudioUtils;
import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.theory.Chord;
import org.jfugue.theory.Note;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class AudioRenderTest extends TestBase {

    @Autowired
    private IAudioRender audioRender;

    @Autowired
    private IAudioUtils audioUtils;

    @Autowired
    public AppConfigs appConfigs;

    private final int BPM = 80;

    @Test
    public void shouldRenderSynthBassAtCorrectLength() throws IOException, InterruptedException {

        int bpm = 80;
        int barCount = 32;

        String midiGeneratedTest = appConfigs.getApplicationRoot() + "lowMidiTest.wav";
        String midiFilePath = appConfigs.getMockDataDir() + "midi\\" + "complow.mid";

        File targetFile = new File(midiGeneratedTest);
        if(targetFile != null && targetFile.exists()){
            deleteFolderAndContents(targetFile);
        }

        ObjectMapper mapper = new ObjectMapper();
        SynthTemplate synthTemplate = mapper.readValue(new File(appConfigs.getMockDataDir() + "midi\\mockSynthTemplate.json"), SynthTemplate.class);




        //GENERATE AND SAVE MIDI

        Pattern pattern = new Pattern();

        List<Chord> chords = new ArrayList<>();
        chords.add(new Chord("C"));
        chords.add(new Chord("E"));
        chords.add(new Chord("C"));
        chords.add(new Chord("E"));
        chords.add(new Chord("C"));
        chords.add(new Chord("E"));
        chords.add(new Chord("C"));
        chords.add(new Chord("E"));
        chords.add(new Chord("C"));
        chords.add(new Chord("E"));
        chords.add(new Chord("C"));
        chords.add(new Chord("E"));
        chords.add(new Chord("C"));
        chords.add(new Chord("E"));
        chords.add(new Chord("C"));
        chords.add(new Chord("E"));
        chords.add(new Chord("C"));
        chords.add(new Chord("E"));
        chords.add(new Chord("C"));
        chords.add(new Chord("E"));
        chords.add(new Chord("C"));
        chords.add(new Chord("E"));
        chords.add(new Chord("C"));
        chords.add(new Chord("E"));
        chords.add(new Chord("C"));
        chords.add(new Chord("E"));
        chords.add(new Chord("C"));
        chords.add(new Chord("E"));
        chords.add(new Chord("C"));
        chords.add(new Chord("E"));
        chords.add(new Chord("C"));
        chords.add(new Chord("E"));

        chords.forEach(c -> {

            Note rootNote = c.getRoot();

			/*
			String bassNote = rootNote.toStringWithoutDuration() + 3 + "h";

			progressionUnit.getPatternLow().setTempo(appConfigs.TEMPO).add(bassNote);
			progressionUnit.getPatternLow().setTempo(appConfigs.TEMPO).add(bassNote);
			*/

            String bassNote = rootNote.toStringWithoutDuration() + 4 + "w";
            pattern.add(bassNote);
        });

        pattern.setTempo(bpm);

        try {
            MidiFileManager.save(new Player().getSequence(pattern), new File(midiFilePath));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        //END GENERATE AND SAVE MIDI

        //compaltlow.mid
        audioRender.renderPart(midiFilePath, midiGeneratedTest, AudioRender.Spectrum.LOW, synthTemplate, BPM);

        Thread.sleep(5000);


        float audioLengthInSeconds = audioUtils.getAudioLength(midiGeneratedTest);
        float targetTrackLengthInSeconds = LoopUtils.getSecondsInBar(bpm) * barCount;

        //CLEAN UP
//        targetFile = new File(midiGeneratedTest);
//        if(targetFile != null && targetFile.exists()){
//            deleteFolderAndContents(targetFile);
//        }

        Assert.assertEquals(targetTrackLengthInSeconds,audioLengthInSeconds,0.05f);
    }
}
