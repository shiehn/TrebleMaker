package com.treblemaker.eventchain;

import com.treblemaker.configs.*;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.renderers.EqFilterRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.treblemaker.configs.AppConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EqFilterEvent implements IEventChain {

    @Autowired
    private EqFilterRenderer eqFilterRenderer;

    @Autowired
    public AppConfigs appConfigs;

    @Value("${num_of_generated_mixes}")
    int numOfGeneratedMixes;

    @Value("${num_of_alt_melodies}")
    int numOfAltMelodies;

    @Override
    public QueueState set(QueueState queueState) {

        final Integer CUTOFF_FREQUENCY = 1000;

        for (int i = 0; i < numOfGeneratedMixes; i++) {
            eqFilterRenderer.render(queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_HI_FX_AUDIO_NO_EQ_FILENAME,
                    queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_HI_FX_AUDIO_FILENAME, CUTOFF_FREQUENCY);

            eqFilterRenderer.render(queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_ALT_HI_FX_NO_EQ_AUDIO_FILENAME,
                    queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_ALT_HI_FX_AUDIO_FILENAME, CUTOFF_FREQUENCY);

            for(int j=0; j<numOfAltMelodies; j++) {
                eqFilterRenderer.render(queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_MELODIC_AUDIO_FILENAME_FX_NO_EQ.replace(".wav", "_"+j+".wav"),
                        queueState.getQueueItem().getAudioPartFilePath() + "/" + i + appConfigs.COMP_MELODIC_AUDIO_FILENAME_FX.replace(".wav", "_"+j+".wav"), CUTOFF_FREQUENCY);
            }
        }

        return queueState;
    }
}
