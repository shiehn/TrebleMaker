package com.treblemaker.extractors;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.beatroot.BeatRootOnsetEventHandler;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;
import be.tarsos.dsp.onsets.OnsetHandler;
import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.dal.interfaces.IBeatLoopsDal;
import com.treblemaker.dal.interfaces.IHarmonicLoopsDal;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.RhythmicAccents;
import com.treblemaker.utils.AudioUtils;
import com.treblemaker.utils.LoopUtils;
import com.treblemaker.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RhythmicExtractionUtil implements OnsetHandler {

    private List<Double> onsets;

    private IBeatLoopsDal beatLoopsDal;

    private IHarmonicLoopsDal harmonicLoopsDal;

    @Autowired
    public AppConfigs appConfigs;

    @Autowired
    public RhythmicExtractionUtil(IBeatLoopsDal beatLoopsDal,IHarmonicLoopsDal harmonicLoopsDal) {
        this.beatLoopsDal = beatLoopsDal;
        this.harmonicLoopsDal = harmonicLoopsDal;
    }

    public void performBeatExtraction() throws Exception {

        //first delete all rhythmic accents
//        List<BeatLoop> beatLoops = beatLoopsDal.findAll();
//        for(BeatLoop bLoop : beatLoops){
//            bLoop.setRhythmicAccents(null);
//            beatLoopsDal.save(bLoop);
//        }

        List<BeatLoop> beatLoopsNoAccents = getBeatLoopsWithoutRhytmicAccents();

        if (!beatLoopsNoAccents.isEmpty()) {
            for (BeatLoop beatLoop : beatLoopsNoAccents) {

                onsets = new ArrayList<>();

                String fullAudioPath = appConfigs.getBeatLoopfullPath(beatLoop);

                AudioUtils audioUtils = new AudioUtils();
                float audioLength = audioUtils.getAudioLength(fullAudioPath);

                File audioFile = new File(fullAudioPath);
                int size = 512;
                int overlap = 256;
                AudioDispatcher dispatcher = AudioDispatcherFactory.fromFile(audioFile, size, overlap);

                ComplexOnsetDetector detector = new ComplexOnsetDetector(size);
                BeatRootOnsetEventHandler handler = new BeatRootOnsetEventHandler();
                detector.setHandler(handler);

                dispatcher.addAudioProcessor(detector);
                dispatcher.run();

                handler.trackBeats(this);

                Application.logger.debug("LOG: Waiting for beat Extraction Callback");
                Thread.sleep(1500);
                Application.logger.debug("LOG: Finished waiting for beat Extraction Callback");

                List<Integer[]> onsetIndexes = getOnsetIndexes(onsets.toArray(new Double[onsets.size()]), beatLoop.getBarCount(), LoopUtils.getSecondsInBar(beatLoop.getBpm()));

                //convert the onsetIndexes to list of rhytmic accents
                List<RhythmicAccents> rhythmicAccents = new ArrayList<>();

                // then add to database ..
                for (Integer[] indexes : onsetIndexes) {

                    RhythmicAccents accent = new RhythmicAccents();
                    accent.setFromArray(indexes);

                    rhythmicAccents.add(accent);
                }

                beatLoop.setRhythmicAccents(rhythmicAccents);
                beatLoopsDal.save(beatLoop);
            }
        }
    }

    public void performHarmonicExtraction() throws Exception {

        //first delete all rhythmic accents
//        List<HarmonicLoop> harmonicLoops = harmonicLoopsDal.findAll();
//        for(HarmonicLoop hLoop : harmonicLoops){
//            hLoop.setRhythmicAccents(null);
//            harmonicLoopsDal.save(hLoop);
//        }

        List<HarmonicLoop> harmonicLoopsNoAccents = getHarmonicLoopsWithoutRhytmicAccents();

        if (!harmonicLoopsNoAccents.isEmpty()) {
            for (HarmonicLoop harmonicLoop : harmonicLoopsNoAccents) {

                onsets = new ArrayList<>();

                String fullAudioPath = appConfigs.getHarmonicLoopsFullPath(harmonicLoop);

                float audioLength = (new AudioUtils()).getAudioLength(fullAudioPath);

                File audioFile = new File(fullAudioPath);
                int size = 512;
                int overlap = 256;
                AudioDispatcher dispatcher = AudioDispatcherFactory.fromFile(audioFile, size, overlap);

                ComplexOnsetDetector detector = new ComplexOnsetDetector(size);
                BeatRootOnsetEventHandler handler = new BeatRootOnsetEventHandler();
                detector.setHandler(handler);

                dispatcher.addAudioProcessor(detector);
                dispatcher.run();

                handler.trackBeats(this);

                Application.logger.debug("LOG: Waiting for harmonic Extraction Callback");
                Thread.sleep(2000);
                Application.logger.debug("LOG: Finished waiting for harmonic Extraction Callback");

                List<Integer[]> onsetIndexes = getOnsetIndexes(onsets.toArray(new Double[onsets.size()]), harmonicLoop.getBarCount(), LoopUtils.getSecondsInBar(harmonicLoop.getBpm()));

                //convert the onsetIndexes to list of rhytmic accents
                List<RhythmicAccents> rhythmicAccents = new ArrayList<>();

                // then add to database ..
                for (Integer[] indexes : onsetIndexes) {

                    RhythmicAccents accent = new RhythmicAccents();
                    accent.setFromArray(indexes);

                    rhythmicAccents.add(accent);
                }

                harmonicLoop.setRhythmicAccents(rhythmicAccents);
                harmonicLoopsDal.save(harmonicLoop);
            }
        }
    }

    public List<BeatLoop> getBeatLoopsWithoutRhytmicAccents() {

        List<BeatLoop> beatLoops = beatLoopsDal.findAll();

        List<BeatLoop> loopsWithAccents = beatLoops.stream().filter(beatLoop -> beatLoop.getRhythmicAccents().isEmpty()).collect(Collectors.toList());

        return loopsWithAccents;
    }

    public List<HarmonicLoop> getHarmonicLoopsWithoutRhytmicAccents() {

        List<HarmonicLoop> harmonicLoopsLoops = harmonicLoopsDal.findAll();

        List<HarmonicLoop> loopsWithAccents = harmonicLoopsLoops.stream().filter(harmonicLoop -> harmonicLoop.getRhythmicAccents().isEmpty()).collect(Collectors.toList());

        return loopsWithAccents;
    }

    @Override
    public void handleOnset(double timeOfOnset, double v1) {
        onsets.add(timeOfOnset);
        Application.logger.debug("LOG: BEAT ONSET : " + timeOfOnset);
    }


    public List<Integer[]> getOnsetIndexes(Double[] onsetTimes, int barCount, float barInSec) {

        List<Integer[]> onsetIndexCollection = new ArrayList<>();

        for (int bc = 0; bc < barCount; bc++) {
            Integer[] onsetIndexes = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

            double sixteenthInterval = 0.1875;
            double intervalWindow = sixteenthInterval / 2;

            for (int i = 0; i < onsetIndexes.length; i++) {

                for (double onsetTime : onsetTimes) {

                    if (i == 0) {
                        if (onsetTime - ((float) bc * barInSec) > (sixteenthInterval * i) - intervalWindow &&
                                onsetTime - ((float) bc * barInSec) <= (sixteenthInterval * i) + intervalWindow) {
                            onsetIndexes[i] = 1;
                        }
                    } else {
                        if (onsetTime - ((float) bc * barInSec) > (sixteenthInterval * i) - intervalWindow &&
                                onsetTime - ((float) bc * barInSec) <= (sixteenthInterval * i) + intervalWindow) {
                            onsetIndexes[i] = 1;
                        }
                    }
                }
            }

            onsetIndexCollection.add(onsetIndexes);
        }

        return onsetIndexCollection;
    }
}
