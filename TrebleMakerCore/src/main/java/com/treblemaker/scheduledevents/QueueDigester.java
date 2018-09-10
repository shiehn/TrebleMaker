package com.treblemaker.scheduledevents;

// region imports

import com.treblemaker.Application;
import com.treblemaker.dal.interfaces.IParametricEqCompositionLayerDal;
import com.treblemaker.dal.interfaces.IQueueItemCustomDal;
import com.treblemaker.dal.interfaces.IQueueItemsDal;
import com.treblemaker.eventchain.SetStationTrackEvent;
import com.treblemaker.eventchain.analytics.SentimentEvent;
import com.treblemaker.eventchain.interfaces.IAnalyticsEvent;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.factory.ExecutorPoolFactory;
import com.treblemaker.generators.*;
import com.treblemaker.mixer.interfaces.IAudioMixer;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.ProcessingState;
import com.treblemaker.model.SynthTemplate;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.rendertransports.BeatLoopRenderTransport;
import com.treblemaker.model.rendertransports.HarmonicLoopRenderTransport;
import com.treblemaker.model.types.ProcessingPattern;
import com.treblemaker.options.interfaces.IHarmonicLoopOptions;
import com.treblemaker.renderers.AudioRender;
import com.treblemaker.renderers.HatPatternRenderer;
import com.treblemaker.renderers.KickPatternRenderer;
import com.treblemaker.renderers.SnarePatternRenderer;
import com.treblemaker.renderers.interfaces.*;
import com.treblemaker.scheduledevents.interfaces.IQueueDigester;
import com.treblemaker.scheduledevents.interfaces.IQueueHelpers;
import com.treblemaker.scheduledevents.task.ExtractEqTask_OLD;
import com.treblemaker.services.audiofilter.NormalizeAudio;
import com.treblemaker.stations.StationSaveService;
import com.treblemaker.utils.FileStructure;
import com.treblemaker.utils.LoopUtils;
import com.treblemaker.utils.TAR;
import com.treblemaker.utils.interfaces.IAudioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.treblemaker.configs.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

//endregion

@Component
public class QueueDigester implements IQueueDigester {

    @Autowired
    private FileStructure fileStructure;

    @Override
    public QueueItem digest(QueueItem queueItem) throws Exception {

        //SHOULD THIS FOLDER BE BASED ON THE STATION ID
        //SHOULD THIS FOLDER BE BASED ON THE STATION ID
        //SHOULD THIS FOLDER BE BASED ON THE STATION ID
        //SHOULD THIS FOLDER BE BASED ON THE STATION ID
        fileStructure.createDirectoryStructure(queueItem.getQueueItemId());

        //TODO THIS SHOULD BE A EVENT NOT A GENERATOR!!
        String songName = fileStructureGenerator.CreateFolderStructureEvent(queueItem);

        Application.logger.debug("LOG: THREAD A THREAD A THREAD A THREAD A THREAD A THREAD A THREAD A THREAD A THREAD A ");

        QueueState queueState = new QueueState();
        queueState.setQueueItem(queueItem);
        queueState.setDataSource(queueHelpers.getSourceData());

        ProcessingState processingState = new ProcessingState();
        processingState.setCurrentState(ProcessingState.ProcessingStates.C1_L1);

        // THIS REALLY NEED TO BE THOUGHT THROUGH?
        // THIS MEANS IF TEMPO IS LESS THAN 120 it WILL NEVER HAVE A 4 BAR LOOP ????
        if(queueItem.getBpm() < 120){
            processingState.setProcessingPattern(ProcessingPattern.TWO_BAR);
        }else {
            processingState.setProcessingPattern(ProcessingPattern.FOUR_BAR);
        }

        queueState.setProcessingState(processingState);
        queueState = analyticsEvent.initAnalytics(queueState);

        // region Beat Loops
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setBeatLoopsEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = setBeatLoopsEvent.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setBeatLoopAnalyticsEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = setBeatLoopAnalyticsEvent.set(queueState);

        //endregion Beat Loops

        // region Chord Structure


        // NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW
        // NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW
        // NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW
        // NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW
        queueState = setChordProgressionEvent.set(queueState);

        //int maxBarCount = 4;
        List<HarmonicLoop> allLoops = queueState.getDataSource().getHarmonicLoops(queueState.getQueueItem().getStationId());
        HarmonicLoop shimLoop = allLoops.stream().filter(hl -> hl.getFileName().contains("shim.wav")).findFirst().get();
        for(ProgressionUnit progressionUnit : queueState.getStructure()){
            boolean barOne = true;
            for (ProgressionUnitBar progressionUnitBar : progressionUnit.getProgressionUnitBars()){
                HarmonicLoop harmonicLoop = shimLoop.clone();
                if(barOne){
                    harmonicLoop.setCurrentBar(1);
                }

                progressionUnitBar.setHarmonicLoop(harmonicLoop);
                barOne = !barOne;
            }
        }

        // NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW
        // NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW
        // NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW
        // NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW NEW




        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setChordStructureEvent & setHarmonicLoopsEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        Application.logger.debug("LOG: $$$ THREAD main " + Thread.currentThread().getId());

//        queueState = setChordStructureAndHarmonicLoops.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setChordStructureAnalyticsEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

//        queueState = setChordStructureAnalyticsEvent.set(queueState);

        // endregion Chord Structure



        // region Set Key

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setKeyEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = setKeyEvent.set(queueState);

        // endregion Set Key


        // region Harmonic Loops

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setHarmonicLoopsAnalyticsEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = setHarmonicLoopsAnalyticsEvent.set(queueState);

        // endregion Harmonic Loops

        // region JFugue Chord Appender

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : jChordAppenderEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = jChordAppenderEvent.set(queueState);

        // endregion JFugue Chord Appender

        // region Low Synths

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : arpeggioLowTemplateAppenderEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = arpeggioLowTemplateAppenderEvent.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : bassline analytics");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = setBasslineAnalyticsEvent.set(queueState);

        // endregion Low Synths

        // region Hi Synths

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : arpeggioHiTemplateAppenderEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = arpeggioHiTemplateAppenderEvent.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : arpeggio analytics");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = setArpeggioAnalyticsEvent.set(queueState);

        // endregion Hi Synths

        // region Mid Synths

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : arpeggioMidTemplateAppenderEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = arpeggioMidTemplateAppenderEvent.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : synthTemplateAppenderEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = synthTemplateAppenderEvent.set(queueState);

        // endregion Mid Synths

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE  : setAlternatesEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = setSynthTemplateAlternatesEvent.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setSynthTemplateAnalytics");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = setSynthTemplateAnalytics.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setHarmonicLoopsAltEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = setHarmonicLoopsAltEvent.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setHarmonicLoopsAltAnalyticsEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = setHarmonicLoopsAltAnalyticsEvent.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setBeatLoopsAltEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = setBeatLoopsAltEvent.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setBeatLoopAltAnalyticsEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = setBeatLoopAltAnalyticsEvent.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setAmbienceLoopsEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = setAmbienceLoopsEvent.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setAmbienceLoopsAnalyticsEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = setAmbienceLoopsAnalyticsEvent.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setAmbienceLoopsAltEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

//        queueState = setAmbienceLoopsAltEvent.set(queueState);
//
//        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//        Application.logger.debug("LOG: PHASE : setAmbienceLoopsAnalyticsEvent");
//        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        //queueState = setAmbienceLoopsAnalyticsEvent.setHarmonicLoops(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setMelodicSynthEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = setMelodicSynthEvent.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setMidiPatternEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = setMidiPatternEvent.set(queueState);

        Application.logger.debug("LOG: SET KICK PATTERN EVENT");
        queueState = setKickPatternEvent.set(queueState);

        Application.logger.debug("LOG: SET KICK MIDI PATTERN");
        queueState = setKickMidiPatternEvent.set(queueState);

        Application.logger.debug("LOG: SET KICK PATTERN ANALYTICS");
        queueState = setKickPatternAnalyticsEvent.set(queueState);

        Application.logger.debug("LOG: RENDER KICK AUDIO PATTERN");
        kickPatternRenderer.render(queueState);

        Application.logger.debug("LOG: SET HAT PATTERN EVENT");
        queueState = setHatPatternEvent.set(queueState);

        Application.logger.debug("LOG: SET HAT MIDI PATTERN EVENT");
        queueState = setHatMidiPatternEvent.set(queueState);

        Application.logger.debug("LOG: SET HAT PATTERN ANALYTICS");
        queueState = setHatPatternAnalyticsEvent.set(queueState);

        Application.logger.debug("LOG: RENDER HAT PATTERN");
        hatPatternRenderer.render(queueState);

        Application.logger.debug("LOG: SET SNARE PATTERN EVENT");
        queueState = setSnarePatternEvent.set(queueState);

        Application.logger.debug("LOG: SET SNARE MIDI PATTERN EVENT");
        queueState = setSnareMidiPatternEvent.set(queueState);

        Application.logger.debug("LOG: SET SNARE PATTERN ANALYTICS");
        queueState = setSnarePatternAnalyticsEvent.set(queueState);

        Application.logger.debug("LOG: RENDER SNARE PATTERN");
        snarePatternRenderer.render(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : midiRender");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = midiRender.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setHits");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        queueState = setHits.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setHitsAnalytics");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        queueState = setHitsAnalyticsEvent.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setFills");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        queueState = setFills.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setFillsAnalytics");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        queueState = setFillsAnalyticsEvent.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : ensure file exists");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");



        Application.logger.debug("LOG: Rendering Mix from audio path : " + songName);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : audioRender.renderPart");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        Collection<RenderTask> collection = new ArrayList<>();

        Application.logger.debug("LOG: numOfGeneratedMixes : " + numOfGeneratedMixes);
        Application.logger.debug("LOG: numOfGeneratedMixes : " + numOfGeneratedMixes);
        Application.logger.debug("LOG: numOfGeneratedMixes : " + numOfGeneratedMixes);
        Application.logger.debug("LOG: numOfGeneratedMixes : " + numOfGeneratedMixes);
        Application.logger.debug("LOG: numOfGeneratedMixes : " + numOfGeneratedMixes);
        Application.logger.debug("LOG: numOfGeneratedMixes : " + numOfGeneratedMixes);
        Application.logger.debug("LOG: numOfGeneratedMixes : " + numOfGeneratedMixes);
        Application.logger.debug("LOG: numOfGeneratedMixes : " + numOfGeneratedMixes);

        for (int i = 0; i < numOfGeneratedMixes; i++) { //TODO THIS IS GOING TO BE THE NUM_OF_VERSIONS ...

            SynthTemplate selectedSynthTemplate = queueItem.getProgression().getStructure().get(0).getProgressionUnitBars().get(0).getSynthTemplates().get(i);

            for(int j=0; j<numOfAltMelodies; j++) {
                RenderTask renderMelodicTask = new RenderTask(queueItem.getMidiFilePath() + "/" + i + appConfigs.COMP_MELODIC_FILENAME.replace(".mid", "_"+j+".mid"), queueItem.getAudioPartFilePath() + "/" + i + appConfigs.COMP_MELODIC_AUDIO_FILENAME_NO_FX.replace(".wav", "_"+j+".wav"), AudioRender.Spectrum.MELODIC, selectedSynthTemplate, queueItem.getBpm());
                collection.add(renderMelodicTask);
            }

            RenderTask renderHiTask = new RenderTask(queueItem.getMidiFilePath() + "/" + i + appConfigs.COMP_HI_FILENAME, queueItem.getAudioPartFilePath() + "/" + i + appConfigs.COMP_HI_AUDIO_FILENAME, AudioRender.Spectrum.HI, selectedSynthTemplate, queueItem.getBpm());
            RenderTask renderHiAltTask = new RenderTask(queueItem.getMidiFilePath() + "/" + i + appConfigs.COMP_ALT_HI_FILENAME, queueItem.getAudioPartFilePath() + "/" + i + appConfigs.COMP_ALT_HI_AUDIO_FILENAME, AudioRender.Spectrum.ALT_HI, selectedSynthTemplate, queueItem.getBpm());
            RenderTask renderMidTask = new RenderTask(queueItem.getMidiFilePath() + "/" + i + appConfigs.COMP_MID_FILENAME, queueItem.getAudioPartFilePath() + "/" + i + appConfigs.COMP_MID_AUDIO_FILENAME, AudioRender.Spectrum.MID, selectedSynthTemplate, queueItem.getBpm());
            RenderTask renderMidAltTask = new RenderTask(queueItem.getMidiFilePath() + "/" + i + appConfigs.COMP_ALT_MID_FILENAME, queueItem.getAudioPartFilePath() + "/" + i + appConfigs.COMP_ALT_MID_AUDIO_FILENAME, AudioRender.Spectrum.ALT_MID, selectedSynthTemplate, queueItem.getBpm());
            RenderTask renderLowTask = new RenderTask(queueItem.getMidiFilePath() + "/" + i + appConfigs.COMP_LOW_FILENAME, queueItem.getAudioPartFilePath() + "/" + i + appConfigs.COMP_LOW_AUDIO_FILENAME, AudioRender.Spectrum.LOW, selectedSynthTemplate, queueItem.getBpm());
            RenderTask renderMidAltLowTask = new RenderTask(queueItem.getMidiFilePath() + "/" + i + appConfigs.COMP_ALT_LOW_FILENAME, queueItem.getAudioPartFilePath() + "/" + i + appConfigs.COMP_ALT_LOW_AUDIO_FILENAME, AudioRender.Spectrum.ALT_LOW, selectedSynthTemplate, queueItem.getBpm());

            collection.add(renderHiTask);
            collection.add(renderHiAltTask);
            collection.add(renderMidTask);
            collection.add(renderMidAltTask);
            collection.add(renderLowTask);
            collection.add(renderMidAltLowTask);
        }

        ExecutorService executorPool = ExecutorPoolFactory.getPool();
        List<Future<Boolean>> tasks = executorPool.invokeAll(collection);
        tasks.forEach(task -> {
            try {
                Application.logger.debug("LOG: Render MIDI Task completed : " + task.get());
            } catch (Exception e) {
                Application.logger.debug("LOG:",e);
            }
        });

        //ADDING THIS JUST FOR TESTING !!!
        //ADDING THIS JUST FOR TESTING !!!
        //ADDING THIS JUST FOR TESTING !!!
        if (returnQueueEarlyForTests) {
            return queueItem;
        }
        //ADDING THIS JUST FOR TESTING !!!
        //ADDING THIS JUST FOR TESTING !!!
        //ADDING THIS JUST FOR TESTING !!!

        System.out.print("SLEEPING AFTER MIDI RENDER ..");
        Thread.sleep(10000);
        System.out.print("SLEEPING MIDI COMPLETE");

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : RENDER BEATS, HITS, FILLS, HARMONICS");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        //extract loops to render ..
        Application.logger.debug("LOG: BEAT LOOP EXTRACT");
        BeatLoopRenderTransport beatLoopRenderTransport = beatLoopRenderer.extractLoopsToRender(queueItem);
        Application.logger.debug("LOG: END BEAT LOOP EXTRACT");
        // render beat loops to disk...
        Application.logger.debug("LOG: BEAT LOOP RENDER");
        beatLoopRenderer.renderRhythm(queueState.getQueueItem().getQueueItemId(), queueState.getQueueItem().getAudioPartFilePath(), appConfigs.COMP_RHYTHM_FILENAME, beatLoopRenderTransport);
        Application.logger.debug("LOG: END BEAT LOOP RENDER");

        //extract loops to render ..
        Application.logger.debug("LOG: BEAT LOOP ALT EXTRACT");
        BeatLoopRenderTransport beatLoopRenderTransportAlt = beatLoopAltRenderer.extractLoopsToRender(queueItem);
        Application.logger.debug("LOG: END BEAT LOOP ALT EXTRACT");

        // render beat loops to disk...
        Application.logger.debug("LOG: BEAT LOOP ALT RENDER");
        beatLoopAltRenderer.renderRhythm(queueState.getQueueItem().getQueueItemId(), queueState.getQueueItem().getAudioPartFilePath(), appConfigs.COMP_RHYTHM_ALT_FILENAME, beatLoopRenderTransportAlt);
        Application.logger.debug("LOG: END BEAT LOOP ALT RENDER");

        //TODO FOLLOW SUIT ON AMBIENCE LOOPS >>
        // render ambience loops to disk ..
        Application.logger.debug("LOG: AMBIENCE LOOP RENDER");
        queueItem = ambienceLoopRenderer.renderAmbienceLoops(queueState.getQueueItem().getAudioPartFilePath(), appConfigs.COMP_AMBIENCE_FILENAME, queueItem);
        Application.logger.debug("LOG: END AMBIENCE LOOP RENDER");

        //extract loops to render ..
        Application.logger.debug("LOG: HARMONIC LOOP EXTRACT");
        HarmonicLoopRenderTransport harmonicLoopRenderTransport = harmonicLoopRenderer.extractLoopsToRender(queueItem);
        Application.logger.debug("LOG: END HARMONIC LOOP EXTRACT");

        //render harmonic loops
        Application.logger.debug("LOG: HARMONIC LOOP RENDER");
        harmonicLoopRenderer.renderHarmonicLoops(queueState.getQueueItem().getAudioPartFilePath(), appConfigs.COMP_HARMONIC_FILENAME, harmonicLoopRenderTransport);
        Application.logger.debug("LOG: END HARMONIC LOOP RENDER");

        //TODO : REMOVE FOR TESTING :
        final int[] totalHLBars = {0};
        harmonicLoopRenderTransport.getHarmonicLoops().forEach(hl -> {
            totalHLBars[0] = totalHLBars[0] + hl.getBarCount();
        });

        Application.logger.debug("LOG: TOTAL HL BAR COUNT : " + totalHLBars[0]);

        //extract loops to render ..
        Application.logger.debug("LOG: HARMONIC ALT LOOP EXTRACT");
        HarmonicLoopRenderTransport harmonicLoopAltRenderTransport = harmonicLoopAltRenderer.extractLoopsToRender(queueItem);
        Application.logger.debug("LOG: END HARMONIC ALT LOOP EXTRACT");

        //render harmonic loops
        Application.logger.debug("LOG: HARMONIC ALT LOOP RENDER");
        harmonicLoopAltRenderer.renderHarmonicLoops(queueState.getQueueItem().getAudioPartFilePath(), appConfigs.COMP_HARMONIC_ALT_FILENAME, harmonicLoopAltRenderTransport);
        Application.logger.debug("LOG: END HARMONIC ALT LOOP RENDER");

        Application.logger.debug("LOG: HIT LOOP RENDER");
        hitRenderer.render(queueState);
        Application.logger.debug("LOG: END HIT LOOP RENDER");

        Application.logger.debug("LOG: FILL LOOP RENDER");
        fillsRenderer.render(queueState);
        Application.logger.debug("LOG: END FILL LOOP RENDER");

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : NORMALIZE AUDIO");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        double targetVolumeMean = normalizeAudio.normalizeFilesInADirectory(queueState.getQueueItem().getAudioPartFilePath());

        Application.logger.debug("LOG: setFinalVolumeTargetMean");
        queueState.getQueueItem().setFinalVolumeTargetMean(targetVolumeMean);

        //ADD DELAY FX AND MIX SYNTHS

        Thread.sleep(10000);

        //extract an array of HiSynthIds from pBars

        //rate each against HiSynthId vertial attributes ..

        Application.logger.debug("LOG: setFXEvent");
        queueState = setFXEvent.set(queueState);

        queueState = setArpeggioFXAnalyticsEvent.set(queueState);

        queueState = renderFXEvent.set(queueState);

        //add EQFILTERS ..
        queueState = eqFilterEvent.set(queueState);

        Thread.sleep(5000);

        float maxLength = LoopUtils.getSecondsInBars(queueItem.getBpm(), 32);

        for (int i = 0; i < numOfGeneratedMixes; i++) {
            if (audioUtils.getAudioLength(queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_HI_FX_AUDIO_FILENAME) > maxLength) {
                audioUtils.trimAudio(queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_HI_FX_AUDIO_FILENAME, queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_HI_FX_AUDIO_FILENAME, 0, LoopUtils.getSecondsInBars(queueItem.getBpm(), 32));
            }

            if (audioUtils.getAudioLength(queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_ALT_HI_FX_AUDIO_FILENAME) > maxLength) {
                audioUtils.trimAudio(queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_ALT_HI_FX_AUDIO_FILENAME, queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_ALT_HI_FX_AUDIO_FILENAME, 0, LoopUtils.getSecondsInBars(queueItem.getBpm(), 32));
            }
        }

        Thread.sleep(10000);

        songName = songName + ".wav";

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : setPanningEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = setPanningEvent.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : processPanningEvent");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = processMixPanning.set(queueState);

        //UPDATE MIX VOLUMES ...
        //UPDATE MIX VOLUMES ...
        //UPDATE MIX VOLUMES ...
        //UPDATE MIX VOLUMES ...
        //UPDATE MIX VOLUMES ...
        //UPDATE MIX VOLUMES ...

        //UPDATE MIX VOLUMES ...
        //UPDATE MIX VOLUMES ...

        queueState = setTargetVolumeMixEvent.set(queueState);

        //UPDATE MIX VOLUMES ...
        //UPDATE MIX VOLUMES ...
        //UPDATE MIX VOLUMES ...
        //UPDATE MIX VOLUMES ...
        //UPDATE MIX VOLUMES ...
        //UPDATE MIX VOLUMES ...
        //UPDATE MIX VOLUMES ...
        //UPDATE MIX VOLUMES ...

        queueState = renderAndSetFinalVolumeMixEvent.set(queueState);

        final String output = appConfigs.getFinalMixOutput() + "/" + songName;

        for (int i = 0; i < numOfGeneratedMixes; i++) {
            String melodyAlt = "";
            if(numOfAltMelodies > 1){
                int indexOfAltMel = 1; //TODO THIS IS BULL SHIT- ITS HARD CODED TO ONE ALT MELODY - IT SHOULD INTERATE THE NUMBER OF ALT MELODIES
                melodyAlt = queueState.getQueueItem().getStereoPartsFilePath() + "/" + i + appConfigs.COMP_MELODIC_AUDIO_FILENAME_FX.replace(".wav", "_" + indexOfAltMel + ".wav");
            }

            audioMixer.createMixes(
                    queueState.getQueueItem().getStereoPartsFilePath() + "/" + i + appConfigs.COMP_MELODIC_AUDIO_FILENAME_FX.replace(".wav","_0.wav"),
                    melodyAlt,
                    queueState.getQueueItem().getStereoPartsFilePath() + "/" + i + appConfigs.COMP_HI_FX_AUDIO_FILENAME,
                    queueState.getQueueItem().getStereoPartsFilePath() + "/" + i + appConfigs.COMP_ALT_HI_FX_AUDIO_FILENAME,
                    queueState.getQueueItem().getStereoPartsFilePath() + "/" + i + appConfigs.COMP_MID_AUDIO_FILENAME,
                    queueState.getQueueItem().getStereoPartsFilePath() + "/" + i + appConfigs.COMP_ALT_MID_AUDIO_FILENAME,
                    queueState.getQueueItem().getStereoPartsFilePath() + "/" + i + appConfigs.COMP_LOW_AUDIO_FILENAME,
                    queueState.getQueueItem().getStereoPartsFilePath() + "/" + i + appConfigs.COMP_ALT_LOW_AUDIO_FILENAME,
                    queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.COMP_RHYTHM_FILENAME,
                    queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.COMP_RHYTHM_ALT_FILENAME,
                    queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.COMP_AMBIENCE_FILENAME,
                    queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.COMP_HARMONIC_FILENAME,
                    queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.COMP_HARMONIC_ALT_FILENAME,
                    queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.HITS_FILENAME,
                    queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.FILLS_FILENAME,
                    queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.KICK_FILENAME,
                    queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.SNARE_FILENAME,
                    queueState.getQueueItem().getStereoPartsFilePath() + "/" + appConfigs.HAT_FILENAME,
                    i, output);
        }

        Application.logger.debug("LOG: FINAL MIX : " + output);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : normalizeFinalMix");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        normalizeAudio.normalizeMix(appConfigs.getFinalMixOutput(), queueItem.getQueueItemId(), numOfGeneratedMixes);

        QueueItem queueItemDb = queueItemCustomDal.getQueueItemById(Long.toString(queueItem.getId()));

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : CREATE METADATA");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        createMetaDataFile.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : TAR PACKAGING");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        packagingEvent.set(queueState);

        stationSaveService.save(queueItem.getQueueItemId());

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: SAVING ANALYTICS . ...");

        Application.logger.debug("LOG:" + queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.COMP_ALT_HI_AUDIO_FILENAME);
        Application.logger.debug("LOG:" + queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.COMP_HI_AUDIO_FILENAME);
        Application.logger.debug("LOG:" + queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.COMP_ALT_HI_AUDIO_FILENAME);
        Application.logger.debug("LOG:" + queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.COMP_MID_AUDIO_FILENAME);
        Application.logger.debug("LOG:" + queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.COMP_ALT_MID_AUDIO_FILENAME);
        Application.logger.debug("LOG:" + queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.COMP_LOW_AUDIO_FILENAME);
        Application.logger.debug("LOG:" + queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.COMP_ALT_LOW_AUDIO_FILENAME);
        Application.logger.debug("LOG:" + queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.COMP_RHYTHM_FILENAME);
        Application.logger.debug("LOG:" + queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.COMP_RHYTHM_ALT_FILENAME);
        Application.logger.debug("LOG:" + queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.COMP_AMBIENCE_FILENAME);
        Application.logger.debug("LOG:" + queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.COMP_HARMONIC_FILENAME);
        Application.logger.debug("LOG:" + queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.COMP_HARMONIC_ALT_FILENAME);
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        /*
        if (bypassEqAnalytics) {
            Application.logger.debug("LOG: EQ ANALYTICS BYPASSED");
            Application.logger.debug("LOG: EQ ANALYTICS BYPASSED");
            Application.logger.debug("LOG: EQ ANALYTICS BYPASSED");
            Application.logger.debug("LOG: EQ ANALYTICS BYPASSED");
            executorPool.shutdown();

            GlobalState.getInstance().setQueueItemInprogress(false);

            return queueItem;
        }
        */

        Integer compositionId = analyticsEvent.saveAnalytics(queueState);
        setStationTrackEvent.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : SET SENTIMENT");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        queueState = setSentimentLabelsEvent.set(queueState);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : SAVE SENTIMENT");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        sentimentEvent.saveSenimentLabels(queueState);


//        saveMixVolumeAnalytics(). ..  ..
//        queueState = null;
/*
        System.gc();

        Collection<ExtractEqTask_OLD> eqTasks = new ArrayList<>();

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : frequencyAnalysis.extractEq(AUDIO_PART_FILE_PATH + appConfigs.COMP_HI_AUDIO_FILENAME)");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        ExtractEqTask_OLD extractHiEqTask = new ExtractEqTask_OLD(queueState.getQueueItem().getAudioPartFilePath() + "/" + "0" + appConfigs.COMP_HI_AUDIO_FILENAME, compositionId, ParametricEqCompositionLayer.LAYER_TYPE_SYNTH_HI, parametricEqCompositionLayerDal);

        eqTasks.add(extractHiEqTask);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : frequencyAnalysis.extractEq(AUDIO_PART_FILE_PATH + appConfigs.COMP_ALT_HI_AUDIO_FILENAME)");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        ExtractEqTask_OLD extractHiAltEqTask = new ExtractEqTask_OLD(queueState.getQueueItem().getAudioPartFilePath() + "/" + "0" +  appConfigs.COMP_ALT_HI_AUDIO_FILENAME, compositionId, ParametricEqCompositionLayer.LAYER_TYPE_SYNTH_HI_ALT, parametricEqCompositionLayerDal);
        eqTasks.add(extractHiAltEqTask);

        eqTaskDigester(executorPool, eqTasks);
        eqTasks = new ArrayList<>();

        System.gc();

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : frequencyAnalysis.extractEq(AUDIO_PART_FILE_PATH + appConfigs.COMP_MID_AUDIO_FILENAME)");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        ExtractEqTask_OLD extractMidEqTask = new ExtractEqTask_OLD(queueState.getQueueItem().getAudioPartFilePath() + "/" + "0" +  appConfigs.COMP_MID_AUDIO_FILENAME, compositionId, ParametricEqCompositionLayer.LAYER_TYPE_SYNTH_MID, parametricEqCompositionLayerDal);
        eqTasks.add(extractMidEqTask);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : frequencyAnalysis.extractEq(AUDIO_PART_FILE_PATH + appConfigs.COMP_ALT_MID_AUDIO_FILENAME)");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        ExtractEqTask_OLD extractMidAltEqTask = new ExtractEqTask_OLD(queueState.getQueueItem().getAudioPartFilePath() + "/" +"0" +  appConfigs.COMP_ALT_MID_AUDIO_FILENAME, compositionId, ParametricEqCompositionLayer.LAYER_TYPE_SYNTH_MID_ALT, parametricEqCompositionLayerDal);
        eqTasks.add(extractMidAltEqTask);

        eqTaskDigester(executorPool, eqTasks);
        eqTasks = new ArrayList<>();

        System.gc();

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : frequencyAnalysis.extractEq(AUDIO_PART_FILE_PATH + appConfigs.COMP_LOW_AUDIO_FILENAME)");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        ExtractEqTask_OLD extractLowEqTask = new ExtractEqTask_OLD(queueState.getQueueItem().getAudioPartFilePath() + "/" +"0" +  appConfigs.COMP_LOW_AUDIO_FILENAME, compositionId, ParametricEqCompositionLayer.LAYER_TYPE_SYNTH_LOW, parametricEqCompositionLayerDal);
        eqTasks.add(extractLowEqTask);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : frequencyAnalysis.extractEq(AUDIO_PART_FILE_PATH + appConfigs.COMP_ALT_LOW_AUDIO_FILENAME)");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        ExtractEqTask_OLD extractLowAltEqTask = new ExtractEqTask_OLD(queueState.getQueueItem().getAudioPartFilePath() + "/" + "0" +  appConfigs.COMP_ALT_LOW_AUDIO_FILENAME, compositionId, ParametricEqCompositionLayer.LAYER_TYPE_SYNTH_LOW_ALT, parametricEqCompositionLayerDal);
        eqTasks.add(extractLowAltEqTask);

        eqTaskDigester(executorPool, eqTasks);
        eqTasks = new ArrayList<>();

        System.gc();

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : frequencyAnalysis.extractEq(AUDIO_PART_FILE_PATH + appConfigs.COMP_RHYTHM_FILENAME)");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        ExtractEqTask_OLD extractRhythmEqTask = new ExtractEqTask_OLD(queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.COMP_RHYTHM_FILENAME, compositionId, ParametricEqCompositionLayer.LAYER_TYPE_BEAT_LOOP, parametricEqCompositionLayerDal);
        eqTasks.add(extractRhythmEqTask);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : frequencyAnalysis.extractEq(AUDIO_PART_FILE_PATH + appConfigs.COMP_RHYTHM_ALT_FILENAME)");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        ExtractEqTask_OLD extractRhythmAltEqTask = new ExtractEqTask_OLD(queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.COMP_RHYTHM_ALT_FILENAME, compositionId, ParametricEqCompositionLayer.LAYER_TYPE_BEAT_LOOP_ALT, parametricEqCompositionLayerDal);
        eqTasks.add(extractRhythmAltEqTask);

        eqTaskDigester(executorPool, eqTasks);
        eqTasks = new ArrayList<>();

        System.gc();

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : frequencyAnalysis.extractEq(AUDIO_PART_FILE_PATH + appConfigs.COMP_AMBIENCE_FILENAME)");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        ExtractEqTask_OLD extractAmbiEqTask = new ExtractEqTask_OLD(queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.COMP_AMBIENCE_FILENAME, compositionId, ParametricEqCompositionLayer.LAYER_TYPE_AMBIENCE, parametricEqCompositionLayerDal);
        eqTasks.add(extractAmbiEqTask);

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : frequencyAnalysis.extractEq(AUDIO_PART_FILE_PATH + appConfigs.COMP_HARMONIC_FILENAME)");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        ExtractEqTask_OLD extractHarmonicEqTask = new ExtractEqTask_OLD(queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.COMP_HARMONIC_FILENAME, compositionId, ParametricEqCompositionLayer.LAYER_TYPE_HARMONIC_LOOP, parametricEqCompositionLayerDal);
        eqTasks.add(extractHarmonicEqTask);

        eqTaskDigester(executorPool, eqTasks);
        eqTasks = new ArrayList<>();

        System.gc();

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE : frequencyAnalysis.extractEq(AUDIO_PART_FILE_PATH + appConfigs.COMP_HARMONIC_ALT_FILENAME)");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        ExtractEqTask_OLD extractHarmonicAltEqTask = new ExtractEqTask_OLD(queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.COMP_HARMONIC_ALT_FILENAME, compositionId, ParametricEqCompositionLayer.LAYER_TYPE_HARMONIC_LOOP_ALT, parametricEqCompositionLayerDal);
        eqTasks.add(extractHarmonicAltEqTask);

        eqTaskDigester(executorPool, eqTasks);

        executorPool.shutdown();
        */

        //TODO WHY THE FUCK IS THIS HERE???
        queueItemCustomDal.setQueueItemComplete(Long.toString(queueItemDb.getId()), songName);
        //TODO WHY THE FUCK IS THIS HERE???

        Application.logger.debug("LOG: *** COMPLETELY FINISHED :) ***");

        GlobalState.getInstance().setQueueItemInprogress(false);

        return queueItem;
    }

    private void eqTaskDigester(ExecutorService executorPool, Collection<ExtractEqTask_OLD> eqTasks) throws InterruptedException {
        List<Future<Boolean>> eqTaskList = executorPool.invokeAll(eqTasks);
        eqTaskList.forEach(task -> {
            try {
                Application.logger.debug("LOG: EqTask completed : " + task.get());
            } catch (Exception e) {
                Application.logger.debug("LOG:", e);
            }
        });
    }

    class RenderTask implements Callable<Boolean> {

        private String targetPath;
        private AudioRender.Spectrum spectrum;
        private SynthTemplate synthTemplate;
        private String midiPath;
        private Integer bpm;

        public RenderTask(String midiPath, String targetPath, AudioRender.Spectrum spectrum, SynthTemplate synthTemplate, Integer bpm) {
            this.targetPath = targetPath;
            this.midiPath = midiPath;
            this.spectrum = spectrum;
            this.synthTemplate = synthTemplate;
            this.bpm = bpm;
        }

        @Override
        public Boolean call() throws Exception {

            Application.logger.debug("LOG: CURRENT THREAD : " + Thread.currentThread().getName());

            try {
                audioRender.renderPart(midiPath, targetPath, spectrum, synthTemplate, bpm);
            } catch (Exception e) {
                Application.logger.debug("LOG:",e);
                return false;
            }

            return true;
        }
    }

    //region AUTOWIRED DI

    @Autowired
    private IEventChain setChordStructureAnalyticsEvent;

    @Autowired
    private IEventChain jChordAppenderEvent;

    @Autowired
    private IEventChain setSynthTemplateAlternatesEvent;

    @Autowired
    private IEventChain arpeggioHiTemplateAppenderEvent;

    @Autowired
    private IEventChain setArpeggioAnalyticsEvent;

    @Autowired
    private IEventChain arpeggioMidTemplateAppenderEvent;

    @Autowired
    private IEventChain arpeggioLowTemplateAppenderEvent;

    @Autowired
    private IEventChain setBasslineAnalyticsEvent;

    @Autowired
    private IEventChain synthTemplateAppenderEvent;

    @Autowired
    private IEventChain setKeyEvent;

    @Autowired
    private IEventChain setBeatLoopsEvent;

    @Autowired
    private IEventChain setBeatLoopsAltEvent;

    @Autowired
    private IEventChain setBeatLoopAnalyticsEvent;

    @Autowired
    private IEventChain setBeatLoopAltAnalyticsEvent;

    @Autowired
    private IEventChain setAmbienceLoopsEvent;

    @Autowired
    private IEventChain setAmbienceLoopsAltEvent;

    @Autowired
    private IEventChain setAmbienceLoopsAnalyticsEvent;

    @Autowired
    private IEventChain setHarmonicLoopsAltEvent;

    @Autowired
    private IEventChain setHarmonicLoopsAnalyticsEvent;

    @Autowired
    private IEventChain setHarmonicLoopsAltAnalyticsEvent;

    @Autowired
    private IEventChain setSynthTemplateAnalytics;

    @Autowired
    private IEventChain setFillsAnalyticsEvent;

    @Autowired
    private IEventChain setHitsAnalyticsEvent;

    @Autowired
    private IEventChain setMidiPatternEvent;

    @Autowired
    private IEventChain setChordProgressionEvent;

    @Autowired
    private IEventChain setChordStructureAndHarmonicLoops;

    @Autowired
    private IEventChain setFXEvent;

    @Autowired
    private IEventChain setArpeggioFXAnalyticsEvent;

    @Autowired
    private IEventChain renderFXEvent;

    @Autowired
    private IEventChain setKickMidiPatternEvent;

    @Autowired
    private IEventChain setSnareMidiPatternEvent;

    @Autowired
    private IEventChain setHatMidiPatternEvent;

    @Autowired
    private IEventChain midiRender;

    @Autowired
    private IEventChain setPanningEvent;

    @Autowired
    private IEventChain processMixPanning;

    @Autowired
    private IEventChain setTargetVolumeMixEvent;

    @Autowired
    private IEventChain renderAndSetFinalVolumeMixEvent;

    @Autowired
    private IEventChain setHits;

    @Autowired
    private IEventChain setFills;

    @Autowired
    private IEventChain setMelodicSynthEvent;

    @Autowired
    private IEventChain setKickPatternEvent;

    @Autowired
    private IEventChain setHatPatternEvent;

    @Autowired
    private IEventChain setSnarePatternEvent;

    @Autowired
    private IEventChain setKickPatternAnalyticsEvent;

    @Autowired
    private IEventChain setHatPatternAnalyticsEvent;

    @Autowired
    private IEventChain setSnarePatternAnalyticsEvent;

    @Autowired
    private IEventChain eqFilterEvent;

    @Autowired
    private IEventChain setSentimentLabelsEvent;

    @Autowired
    private IEventChain createMetaDataFile;

    @Autowired
    private IEventChain packagingEvent;

    @Autowired
    private IBeatLoopRenderer beatLoopRenderer;

    @Autowired
    private IBeatLoopRenderer beatLoopAltRenderer;

    @Autowired
    private IAmbienceLoopRenderer ambienceLoopRenderer;

    @Autowired
    private IHarmonicLoopRenderer harmonicLoopRenderer;

    @Autowired
    private IHarmonicLoopRenderer harmonicLoopAltRenderer;

    @Autowired
    private IHitRenderer hitRenderer;

    @Autowired
    private IFillsRenderer fillsRenderer;

    @Autowired
    private KickPatternRenderer kickPatternRenderer;

    @Autowired
    private HatPatternRenderer hatPatternRenderer;

    @Autowired
    private SnarePatternRenderer snarePatternRenderer;

    @Autowired
    private IAudioRender audioRender;

    @Autowired
    private IAudioMixer audioMixer;

    @Autowired
    private IQueueItemsDal queueItemsDal;

    @Autowired
    private IQueueHelpers queueHelpers;

    @Autowired
    private IAnalyticsEvent analyticsEvent;

    @Autowired
    private IHarmonicLoopOptions harmonicLoopOptions;

    @Autowired
    private IQueueItemCustomDal queueItemCustomDal;

    @Autowired
    private NormalizeAudio normalizeAudio;

    @Autowired
    private IParametricEqCompositionLayerDal parametricEqCompositionLayerDal;

    @Autowired
    private IAudioUtils audioUtils;

    @Autowired
    private SetStationTrackEvent setStationTrackEvent;

    @Autowired
    private FileStructureGenerator fileStructureGenerator;

    @Autowired
    private SentimentEvent sentimentEvent;

    @Autowired
    private StationSaveService stationSaveService;

    @Value("${bypass_eqanalytics}")
    boolean bypassEqAnalytics;

    @Value("${return_queue_early_for_tests}")
    boolean returnQueueEarlyForTests;

    @Value("${machinelearning_endpoints}")
    String[] machineLearningEndpoints;

    @Value("${num_of_generated_mixes}")
    int numOfGeneratedMixes;

    @Value("${num_of_alt_melodies}")
    int numOfAltMelodies;

    @Autowired
    private AppConfigs appConfigs;

    //endregion
}
