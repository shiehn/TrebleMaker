package com.treblemaker.renderers;

import com.treblemaker.configs.*;
import com.treblemaker.Application;
import com.treblemaker.dal.interfaces.IHitDal;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.extractors.PatternSetter;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.renderers.helper.PatternConsolidationUtil;
import com.treblemaker.utils.FileStructure;
import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.treblemaker.configs.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class MidiRender implements IEventChain {

    @Autowired
    private PatternSetter midiExtractor;

    @Autowired
    private AppConfigs appConfigs;

    @Autowired
    private FileStructure fileStructure;

    @Value("${num_of_generated_mixes}")
    Integer numOfGeneratedMixes;

    //TODO THIS IS JUST FOR TESTING ...
    @Autowired
    private IHitDal basslineDal;

    @Override
    public QueueState set(QueueState queueState) {
        String songName = queueState.getQueueItem().getQueueItemId();
        fileStructure.createDirectoryStructure(songName);

        String MIDI_FILE_PATH = appConfigs.getCompositionOutput() + "/midioutput/" + songName + "/";

        List<Pattern> jPatternHi = PatternConsolidationUtil.consolidateHiPatterns(queueState.getStructure(), queueState.getQueueItem().getBpm());

        for (int i = 0; i < numOfGeneratedMixes; i++) {
            Application.logger.debug("LOG: jPatternHi : " + jPatternHi.get(i).toString());
            try {
                MidiFileManager.save(new Player().getSequence(jPatternHi.get(i).setTempo(queueState.getQueueItem().getBpm())), new File(MIDI_FILE_PATH + "/" + i + appConfigs.COMP_HI_FILENAME));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        List<Pattern> jPatternAltHi = PatternConsolidationUtil.consolidateHiAltPatterns(queueState.getStructure(), queueState.getQueueItem().getBpm());

        for (int i = 0; i < numOfGeneratedMixes; i++) {
            Application.logger.debug("LOG: jPatternAltHi : " + jPatternAltHi.get(i).toString());
            try {
                MidiFileManager.save(new Player().getSequence(jPatternAltHi.get(i).setTempo(queueState.getQueueItem().getBpm())), new File(MIDI_FILE_PATH + "/" + i + appConfigs.COMP_ALT_HI_FILENAME));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        List<Pattern> jPatternMid = PatternConsolidationUtil.consolidateMidPatterns(queueState.getStructure(), queueState.getQueueItem().getBpm());
        for (int i = 0; i < numOfGeneratedMixes; i++) {
            Application.logger.debug("LOG: jPatternMid : " + jPatternMid.get(i).toString());
            try {
                System.out.println(MIDI_FILE_PATH + i + appConfigs.COMP_MID_FILENAME);
                MidiFileManager.save(new Player().getSequence(jPatternMid.get(i).setTempo(queueState.getQueueItem().getBpm())), new File(MIDI_FILE_PATH + "/" + i + appConfigs.COMP_MID_FILENAME));
                System.out.println(MIDI_FILE_PATH + i + appConfigs.COMP_MID_FILENAME);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        List<Pattern> jPatternAltMid = PatternConsolidationUtil.consolidateMidAltPatterns(queueState.getStructure(), queueState.getQueueItem().getBpm());
        for (int i = 0; i < numOfGeneratedMixes; i++) {
            Application.logger.debug("LOG: jPatternAltMid : " + jPatternAltMid.get(i).toString());
            try {
                MidiFileManager.save(new Player().getSequence(jPatternAltMid.get(i).setTempo(queueState.getQueueItem().getBpm())), new File(MIDI_FILE_PATH + "/" + i + appConfigs.COMP_ALT_MID_FILENAME));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        List<Pattern> jPatternLow = PatternConsolidationUtil.consolidateLowPatterns(queueState.getStructure(), queueState.getQueueItem().getBpm());
        for (int i = 0; i < numOfGeneratedMixes; i++) {
            Application.logger.debug("LOG: jPatternLow : " + jPatternLow.get(i).toString());
            try {
                MidiFileManager.save(new Player().getSequence(jPatternLow.get(i).setTempo(queueState.getQueueItem().getBpm())), new File(MIDI_FILE_PATH + "/" + i + appConfigs.COMP_LOW_FILENAME));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        List<Pattern> jPatternLowAlt = PatternConsolidationUtil.consolidateLowAltPatterns(queueState.getStructure(), queueState.getQueueItem().getBpm());
        for (int i = 0; i < numOfGeneratedMixes; i++) {
            Application.logger.debug("LOG: jPatternLowAlt : " + jPatternLowAlt.get(i).toString());
            try {
                MidiFileManager.save(new Player().getSequence(jPatternLowAlt.get(i).setTempo(queueState.getQueueItem().getBpm())), new File(MIDI_FILE_PATH + "/" + i + appConfigs.COMP_ALT_LOW_FILENAME));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        List<Pattern> jPatternKick = PatternConsolidationUtil.consolidateKickMidiPatterns(queueState.getStructure(), queueState.getQueueItem().getBpm());
        for (int i = 0; i < numOfGeneratedMixes; i++) {
            Application.logger.debug("LOG: jPatternKick : " + jPatternKick.get(i).toString());
            try {
                MidiFileManager.save(new Player().getSequence(jPatternKick.get(i).setTempo(queueState.getQueueItem().getBpm())), new File(MIDI_FILE_PATH + "/" + i + appConfigs.COMP_KICK_FILENAME));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        List<Pattern> jPatternSnare = PatternConsolidationUtil.consolidateSnareMidiPatterns(queueState.getStructure(), queueState.getQueueItem().getBpm());
        for (int i = 0; i < numOfGeneratedMixes; i++) {
            Application.logger.debug("LOG: jPatternSnare : " + jPatternSnare.get(i).toString());
            try {
                MidiFileManager.save(new Player().getSequence(jPatternSnare.get(i).setTempo(queueState.getQueueItem().getBpm())), new File(MIDI_FILE_PATH + "/" + i + appConfigs.COMP_SNARE_FILENAME));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        List<Pattern> jPatternHats = PatternConsolidationUtil.consolidateHatMidiPatterns(queueState.getStructure(), queueState.getQueueItem().getBpm());
        for (int i = 0; i < numOfGeneratedMixes; i++) {
            Application.logger.debug("LOG: jPatternHats : " + jPatternHats.get(i).toString());
            try {
                MidiFileManager.save(new Player().getSequence(jPatternHats.get(i).setTempo(queueState.getQueueItem().getBpm())), new File(MIDI_FILE_PATH + "/" + i + appConfigs.COMP_HATS_FILENAME));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        //RENDER MELODIC
        //String tempoPrefix = "T" + queueState.getQueueItem().getBpm() + " ";
        String melodicString = "";
        for (ProgressionUnit progressionUnit : queueState.getStructure()) {
            if(!progressionUnit.getMelody().isEmpty()) {
                melodicString = melodicString + progressionUnit.getMelody() + " ";
            }
        }

        //2)RENDER PATTERN
        Application.logger.debug("LOG: jPatternMelodic : " + melodicString);
        for (int i = 0; i < numOfGeneratedMixes; i++) {
            try {
                Pattern melodicPattern = new Pattern(melodicString);
                MidiFileManager.save(new Player().getSequence(melodicPattern.setTempo(queueState.getQueueItem().getBpm())), new File(MIDI_FILE_PATH + "/" + i + appConfigs.COMP_MELODIC_FILENAME));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return queueState;
    }
}
