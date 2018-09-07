package com.treblemaker.eventchain;

import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.mixer.StereoMixer;
import com.treblemaker.mixer.interfaces.IAudioMixer;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.utils.FileStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.treblemaker.utils.*;


import java.io.File;

@Component
public class ProcessMixPanning implements IEventChain {

    @Autowired
    public AppConfigs appConfigs;

    @Autowired
    private AudioUtils audioUtils;

    @Autowired
    private IAudioMixer audioMixer;

    @Autowired
    private StereoMixer stereoMixer;

    @Value("${num_of_generated_mixes}")
    Integer numOfGeneratedMixes;

    @Override
    public QueueState set(QueueState queueState) {

        File[] sourceFiles = FileStructure.getFilesInDirectory(queueState.getQueueItem().getAudioPartFilePath());

        for (File sourceFile : sourceFiles) {

            String panCode = getPancode(sourceFile.getName().toLowerCase(), queueState);
            if (panCode != null) {
                Double[] leftRightGain = getLeftAndRightGain(panCode);
                audioMixer.changeVolume(leftRightGain[0], queueState.getQueueItem().getAudioPartFilePath() + "/" + sourceFile.getName(), queueState.getQueueItem().getMonoPartsFilePath() + "/" + sourceFile.getName().replace(".wav", "_l.wav"));
                audioMixer.changeVolume(leftRightGain[1], queueState.getQueueItem().getAudioPartFilePath() + "/" + sourceFile.getName(), queueState.getQueueItem().getMonoPartsFilePath() + "/" + sourceFile.getName().replace(".wav", "_r.wav"));
            }
        }

        stereoMixer.createStereoMixes(queueState.getQueueItem(), numOfGeneratedMixes);

        return queueState;
    }

    String getPancode(String fileName, QueueState queueState) {

        if (fileName.toLowerCase().contains("compmelodicfx") || fileName.toLowerCase().contains("compmelodic")) {
            return queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlotPanning().getCompMelody();
        }

        if (fileName.toLowerCase().contains("comphifx") || fileName.toLowerCase().contains("comphi")) {
            return queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlotPanning().getCompHi();
        }

        if (fileName.toLowerCase().contains("compalthifx") || fileName.toLowerCase().contains("compalthi")) {
            return queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlotPanning().getCompHiAlt();
        }

        if (fileName.toLowerCase().contains("compmid")) {
            return queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlotPanning().getCompMid();
        }

        if (fileName.toLowerCase().contains("compaltmid")) {
            return queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlotPanning().getCompMidAlt();
        }

        if (fileName.toLowerCase().contains("complow")) {
            return queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlotPanning().getCompLow();
        }

        if (fileName.toLowerCase().contains("compaltlow")) {
            return queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlotPanning().getCompLowAlt();
        }

        if (fileName.toLowerCase().contains("compharmonic")) {
            return queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlotPanning().getHarmonic();
        }

        if (fileName.toLowerCase().contains("compharmonicalt")) {
            return queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlotPanning().getHarmonicAlt();
        }

        if (fileName.toLowerCase().contains("comprhythm")) {
            return queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlotPanning().getBeat();
        }

        if (fileName.toLowerCase().contains("comprhythmalt")) {
            return queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlotPanning().getBeatAlt();
        }

        if (fileName.toLowerCase().contains("compambience")) {
            return queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlotPanning().getAmbient();
        }

        if (fileName.toLowerCase().contains("hit")) {
            return queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlotPanning().getHits();
        }

        if (fileName.toLowerCase().contains("fill")) {
            return queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlotPanning().getFills();
        }

        if (fileName.toLowerCase().contains("kick")) {
            return queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlotPanning().getKick();
        }

        if (fileName.toLowerCase().contains("hat")) {
            return queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlotPanning().getHat();
        }

        if (fileName.toLowerCase().contains("snare")) {
            return queueState.getStructure().get(0).getProgressionUnitBars().get(0).getCompositionTimeSlotPanning().getSnare();
        }

        //TODO LOG THIS FUCKING THING  .... .
        Application.logger.debug("LOG:  ERROR NOT TRACK MATCH -> : " + fileName);

        return null;
    }

    Double[] getLeftAndRightGain(String panCode) {
        Double[] leftAndRightGain = new Double[2];
        String[] sideAndGain = panCode.split("_");

        if (sideAndGain[0].equalsIgnoreCase("l")) {
            leftAndRightGain[0] = Double.parseDouble(sideAndGain[1]);
            leftAndRightGain[1] = 1.0 - Double.parseDouble(sideAndGain[1]);
        } else {
            leftAndRightGain[0] = 1.0 - Double.parseDouble(sideAndGain[1]);
            leftAndRightGain[1] = Double.parseDouble(sideAndGain[1]);
        }

        return leftAndRightGain;
    }
}
