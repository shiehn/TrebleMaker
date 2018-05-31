package com.treblemaker.extractors.pitchextraction;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchProcessor;
import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.dal.interfaces.IHarmonicLoopsDal;
import com.treblemaker.model.*;
import com.treblemaker.model.PitchExtractions;
import com.treblemaker.utils.LoopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;
import static com.treblemaker.model.BeatLoop.ALREADY_NORMALIZED;

@Component
public class PitchExtractionUtil {

    private IHarmonicLoopsDal harmonicLoopsDal;

    @Autowired
    public AppConfigs appConfigs;

    @Autowired
    public PitchExtractionUtil(IHarmonicLoopsDal harmonicLoopsDal) {
        this.harmonicLoopsDal = harmonicLoopsDal;
    }

    public void extractPitchsAndUpdateDatabase() throws IOException, UnsupportedAudioFileException, InterruptedException {

        //MAYBE I SHOULD'nt FETCH THESE EVERY TIME??
        List<HarmonicLoop> harmonicLoops = harmonicLoopsDal.findAll();

        List<HarmonicLoop> filteredLoops = findLoopsWithoutPitchExtractions(harmonicLoops);

        List<PitchExtractions> extractedPitchs;

        for (HarmonicLoop harmonicLoop : filteredLoops) {
            if (harmonicLoop.getNormalizedLength() == ALREADY_NORMALIZED) {
                extractedPitchs = extractPitchs(harmonicLoop, new File(appConfigs.getHarmonicLoopsFullPath(harmonicLoop)));
                if(extractedPitchs != null || !extractedPitchs.isEmpty()){
                    harmonicLoop.setPitchExtractions(extractedPitchs);
                    harmonicLoopsDal.save(harmonicLoop);
                }
            } else {
                Application.logger.debug("LOG: ERROR: CANNOT EXTRACT PITCH FROM NON-NORMALIZED HARMONIC LOOPS");
            }
        }
    }

    public List<HarmonicLoop> findLoopsWithoutPitchExtractions(List<HarmonicLoop> harmonicLoops) {
        return harmonicLoops.stream().filter(harmonicLoop -> harmonicLoop.getPitchExtractions() == null || harmonicLoop.getPitchExtractions().isEmpty()).collect(Collectors.toList());
    }

    public List<PitchExtractions> extractPitchs(HarmonicLoop harmonicLoop, File audioFile) throws IOException, UnsupportedAudioFileException, InterruptedException {

        int bufferSize = 1024;
        int overlap = 768;
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromFile(audioFile, bufferSize, overlap);

        PitchDetectionHandlerImp pitchDetectionHandler = new PitchDetectionHandlerImp();
        dispatcher.addAudioProcessor(new PitchProcessor(PitchEstimationAlgorithm.MPM, dispatcher.getFormat().getSampleRate(), bufferSize, pitchDetectionHandler));

        new Thread(dispatcher, "Audio dispatching").start();

        Thread.sleep(10000);

        return pitchDetectionHandler.getPitchExtractions((double) LoopUtils.getSecondsInBar(harmonicLoop.getBpm()), harmonicLoop.getBarCount());
    }
}
