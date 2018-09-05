package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.fx.FXHelper;
import com.treblemaker.fx.IFXRenderer;
import com.treblemaker.model.queues.QueueState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.treblemaker.configs.*;

import java.util.List;

@Component
public class RenderFXEvent implements IEventChain {

    @Autowired
    private IFXRenderer fxRenderer;

    @Autowired
    private AppConfigs appConfigs;

    @Value("${num_of_alt_melodies}")
    int numOfAltMelodies;

    @Override
    public QueueState set(QueueState queueState) {

        List<List<Integer>> hiSynthIds = FXHelper.extractHiSynthIds(queueState.getStructure());

        for(int i=0; i<hiSynthIds.size(); i++) {

            //TODO SHOULD THERE BE DIFFERENT FX SELECTED FOR EACH SYNTH TEMPLATE ??? ..
            /*
            Map<Integer, FXArpeggioWithRating> selectedHiSynthFX = queueState.getStructure().get(0).getProgressionUnitBars().get(0).getSelectedFXMap(i);//TODO THIS is hard coded to index 1!!

            fxRenderer.renderSfx(queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_HI_AUDIO_FILENAME,
                    queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_HI_FX_AUDIO_NO_EQ_FILENAME,
                    selectedHiSynthFX.get(hiSynthIds.get(i).get(0)).getFxArpeggioDelay(),
                    queueState.getQueueItem().getBpm());

            fxRenderer.renderSfx(queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_ALT_HI_AUDIO_FILENAME,
                    queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_ALT_HI_FX_NO_EQ_AUDIO_FILENAME,
                    selectedHiSynthFX.get(hiSynthIds.get(i).get(0)).getFxArpeggioDelay(),
                    queueState.getQueueItem().getBpm());
            //TODO this line was changed to because mid tracks are no longer split into norm + alt selectedHiSynthFX.get(hiSynthIds.get(i).get(1)).getFxArpeggioDelay(),
            */

            fxRenderer.renderReverbFx(queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_HI_AUDIO_FILENAME,
                    queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_HI_FX_AUDIO_NO_EQ_FILENAME);

            fxRenderer.renderReverbFx(queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_ALT_HI_AUDIO_FILENAME,
                    queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_ALT_HI_FX_NO_EQ_AUDIO_FILENAME);

            for(int j=0; j<numOfAltMelodies; j++) {
                fxRenderer.renderReverbFx(queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_MELODIC_AUDIO_FILENAME_NO_FX.replace(".wav", "_"+j+".wav"),
                        queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_MELODIC_AUDIO_FILENAME_FX_NO_EQ.replace(".wav", "_"+j+".wav"));
            }
        }



        return queueState;
    }
}
