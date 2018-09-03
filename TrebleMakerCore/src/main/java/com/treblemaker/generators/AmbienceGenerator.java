package com.treblemaker.generators;

import com.treblemaker.Application;
import com.treblemaker.dal.interfaces.IAmbienceLoopsDal;
import com.treblemaker.generators.interfaces.IAmbienceGenerator;
import com.treblemaker.generators.interfaces.IShimGenerator;
import com.treblemaker.helpers.ShimHelper;
import com.treblemaker.model.AmbienceLoop;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.types.Composition;
import com.treblemaker.utils.LoopUtils;
import com.treblemaker.utils.interfaces.IAudioUtils;
import org.springframework.stereotype.Component;
import com.treblemaker.configs.AppConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class AmbienceGenerator implements IAmbienceGenerator {

    private IShimGenerator shimGenerator;
    private IAmbienceLoopsDal ambienceLoopsDal;
    private IAudioUtils audioUtils;
    private AppConfigs appConfigs;

    public AmbienceGenerator(IShimGenerator shimGenerator, IAmbienceLoopsDal ambienceLoopsDal, IAudioUtils audioUtils, AppConfigs appConfigs) {
        this.shimGenerator = shimGenerator;
        this.ambienceLoopsDal = ambienceLoopsDal;
        this.audioUtils = audioUtils;
        this.appConfigs = appConfigs;
    }

    public QueueState generateAndSetAmbienceLoops(QueueState queueState, Composition.Layer layerType) {

        //get bar length in seconds ..
        float secondsInBar = LoopUtils.getBeatsInSeconds(queueState.getQueueItem().getBpm(), 4);

        float maxSampleLength = secondsInBar * 4f;

        // ProgressionType.VERSE, ProgressionType.BRIDGE, ProgressionType.CHORUS
        Map<ProgressionUnit.ProgressionType, AmbienceLoop> ambienceLoopsByType = new HashMap<>();

        //List<String> choosenAmbienceLoops = new ArrayList<String>();

        for (int i = 0; i < queueState.getStructure().size(); i++) {

            ProgressionUnit progressionUnit = queueState.getStructure().get(i);

            // if dictionary does not contain an entry for that type add one ..
            if (ambienceLoopsByType.get(progressionUnit.getType()) == null) {

//                ArrayList<AmbienceLoop> ambienceLoopsForProgressionUnit = new ArrayList<AmbienceLoop>();

                AmbienceLoop ambienceLoop;

                if (progressionUnit.isLayerGated(layerType)) {
                    ambienceLoop = ShimHelper.getShimAsAmbienceLoop();
                } else {
                    ambienceLoop = getLoop(maxSampleLength, progressionUnit);
                }

                //find loop that fits in 2, 4, 0r 8 bars ..
                float barCount = LoopUtils.getBarCountForLoop(ambienceLoop.getAudioLength(), secondsInBar);
                float exactSampleLength = audioUtils.getAudioLength(appConfigs.getAmbienceFullPath(ambienceLoop));
                float newLoopLengthInSeconds = LoopUtils.getBarGroupLengthInSeconds((float) secondsInBar, (float) barCount);
                float shimLength = newLoopLengthInSeconds - exactSampleLength;
                ambienceLoop.setShimLength(shimLength);
                //int loopsRequired = (int) (progressionUnit.getBarCount() / barCount);

//                for (int j = 0; j < loopsRequired; j++) {
//                    ambienceLoopsForProgressionUnit.add(ambienceLoop);
//                }

                ambienceLoopsByType.put(progressionUnit.getType(), ambienceLoop);
            }
        }

        // ONLY SELECT A NEW BEAT IF rerender = true

        setAmbienceLoops(ambienceLoopsByType, layerType, queueState.getStructure());
    /*
        if (!queueItem.isIsRefactor()) {
			setBeatLoops(ambienceLoopsByType, queueItem.getQueueItem().getStructure());
		} else if (queueItem.isIsRefactor() && BeatLoopHelper.isBeatLoopReRender(queueItem)) {
			setBeatLoops(ambienceLoopsByType, queueItem.getQueueItem().getStructure());
		} else if (queueItem.isIsRefactor() && !BeatLoopHelper.isBeatLoopReRender(queueItem)) {
			// use the existing beatLoop ..
			// setBeatLoops(beatLoopsByType,
			// queueItem.getQueueItem().getStructure());
		}
		*/

        return queueState;
    }

    public int numberOfLoopsRequired(ProgressionUnit progression, BeatLoop beatLoop) {

        int numOfBars = progression.getBarCount();
        int beatLength = beatLoop.getBarCount();

        return numOfBars / beatLength;
    }

    public AmbienceLoop getLoop(float maxSampleLength, ProgressionUnit progressionUnit) {

        //TODO THIS NEED TO BE CACHED!! SHOULDN't HIT SQL
        List<AmbienceLoop> ambienceLoops = ambienceLoopsDal.findByAudioLengthLessThanEqual(maxSampleLength);

        List<AmbienceLoop> filteredForChords = filterForChordMatches(ambienceLoops, progressionUnit);

        if(filteredForChords != null && !filteredForChords.isEmpty()){
            return filteredForChords.get(new Random().nextInt(filteredForChords.size()));
        }

        List<AmbienceLoop> filteredForATonal = filterForATonalMatches(ambienceLoops);

        return filteredForATonal.get(new Random().nextInt(filteredForATonal.size()));
    }

    public float getSampleLength(String filePath) {

        File file = new File(filePath);

        AudioInputStream audioInputStream;
        float durationInSeconds = -1;
        try {

            audioInputStream = AudioSystem.getAudioInputStream(file);

            AudioFormat format = audioInputStream.getFormat();
            long audioFileLength = file.length();
            int frameSize = format.getFrameSize();
            float frameRate = format.getFrameRate();
            durationInSeconds = (audioFileLength / (frameSize * frameRate));

        } catch (Exception e) {
            Application.logger.debug("LOG:",e);
        }

        return durationInSeconds;
    }

    @Override
    public void setAmbienceLoops(Map<ProgressionUnit.ProgressionType, AmbienceLoop> ambienceLoopsByType, Composition.Layer layerType, List<ProgressionUnit> progressionUnits) {

        progressionUnits.forEach(pUnit -> {

            //TODO THIS MUST CHANGE!!!
            //TODO FOR NOW ALL AMBIENCE LOOPS ARE JUST GOING TO BE ON BAR ONE
            if (layerType == Composition.Layer.AMBIENCE_LOOP) {
                AmbienceLoop ambienceLoop = ambienceLoopsByType.get(pUnit.getType());
                pUnit.getProgressionUnitBars().get(0).setAmbienceLoop(ambienceLoop);
            }
        });

        /*
        for (ProgressionUnit progression : progressionUnits) {

            if(layerType == Composition.Layer.AMBIENCE_LOOP){

                AmbienceLoop ambienceByType = ambienceLoopsByType.get(progression.getType());

                for(Integer i=0; i<ambienceByType.size(); i++){
                    progression.getProgressionUnitBars().get(i).setAmbienceLoop(ambienceByType.get(i));
                }


            }else if(layerType == Composition.Layer.AMBIENCE_LOOP_ALT){

                for(Integer i=0; i<ambienceLoopsByType.get(progression.getType()).size(); i++){
                    progression.getProgressionUnitBars().get(i).setAmbienceLoopAlt(ambienceLoopsByType.get(progression.getType()).get(i));
                }
            }
        }
        */
    }

    private List<AmbienceLoop> filterForATonalMatches(List<AmbienceLoop> ambienceLoops){

        return ambienceLoops.stream().filter(ambienceLoop ->
                ambienceLoop.getChords() == null || ambienceLoop.getChords().isEmpty()).collect(Collectors.toList());
    }

    private List<AmbienceLoop> filterForChordMatches(List<AmbienceLoop> ambienceLoops, ProgressionUnit progressionUnit) {

        List<AmbienceLoop> loopsWithChordMatch = new ArrayList<>();

        for (AmbienceLoop ambienceLoop : ambienceLoops) {

            List<Boolean> matches = new ArrayList<>();

            for (ProgressionUnitBar bar : progressionUnit.getProgressionUnitBars()) {

                for (HiveChord chord : ambienceLoop.getChords()) {
                    if (bar.getChord() != null && bar.getChord().isEqualOrTriadMatch(chord)) {
                        matches.add(true);
                    }
                }
            }

            if (matches.size() == 4) {
                loopsWithChordMatch.add(ambienceLoop);
            }
        }

        return loopsWithChordMatch;
    }
}
