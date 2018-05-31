package com.treblemaker.eventchain.analytics;

import com.treblemaker.Application;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.extractors.sentiment.SentimentCharacteristicsExtraction;
import com.treblemaker.extractors.sentiment.SentimentMixExtraction;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SetSentimentLabelsEvent implements IEventChain {

    private SentimentMixExtraction sentimentMixExtraction;
    private SentimentCharacteristicsExtraction sentimentCharacteristicsExtraction;

    @Value("${num_of_generated_mixes}")
    int numOfGeneratedMixes;

    @Value("${num_of_generated_mix_variations}")
    int numOfGeneratedMixVariations;

    @Autowired
    public SetSentimentLabelsEvent(SentimentMixExtraction sentimentMixExtraction,
                                   SentimentCharacteristicsExtraction sentimentCharacteristicsExtraction,
                                   @Value("${num_of_generated_mixes}") int numOfGeneratedMixes,
                                   @Value("${num_of_generated_mix_variations}") int numOfGeneratedMixVariations) {
        this.sentimentMixExtraction = sentimentMixExtraction;
        this.sentimentCharacteristicsExtraction = sentimentCharacteristicsExtraction;
        this.numOfGeneratedMixes = numOfGeneratedMixes;
        this.numOfGeneratedMixVariations = numOfGeneratedMixVariations;
    }

    @Override
    public QueueState set(QueueState queueState) {

        /*
        ambient    - variation
        bass       - variation
        bright     - mix charateristic (harmonic loops)
        dark       - mix charateristic (harmonic loops)
        drums      - variation
        fast       - mix characteristic
        slow       - mix characteristic
         */

        for (ProgressionUnit progressionUnit : queueState.getStructure()) {
            for (int i = 0; i < progressionUnit.getProgressionUnitBars().size(); i++) {
                if((i%2)==0){

                    Map<String, List<String>> variationToSentiment = new HashMap<>();

                    for(int mixIndex=0; mixIndex<numOfGeneratedMixes; mixIndex++){

                        for(int variationIndex=0; variationIndex<numOfGeneratedMixVariations; variationIndex++){

                            //List<String> mixExtraction = this.sentimentMixExtraction.extract();
                            List<String> characteristicExtraction = this.sentimentCharacteristicsExtraction.extract();

                            //HACK HARD CODING THE LABELS ...
                            //HACK HARD CODING THE LABELS ...
                            //HACK HARD CODING THE LABELS ...
                            String mixVariationId = generateMixVariationId(queueState.getQueueItem().getQueueItemId(), mixIndex, variationIndex);

                            String suffix= mixVariationId.substring(mixVariationId.length() - 1);


                            List<String> variationSentiment = new ArrayList<>();

                            if(suffix.equalsIgnoreCase("1")){
                                variationSentiment.add("bass");
                            }else if(suffix.equalsIgnoreCase("2")){
                                variationSentiment.add("bright");
                            }else if(suffix.equalsIgnoreCase("3")){
                                variationSentiment.add("ambient");
                            }else{
                                Application.logger.debug("LOG: CANNOT FIX MIX VARIATION SUFFIX !!!!");
                                Application.logger.debug("LOG: LAST CHARACTOR WAS: " + suffix);
                                throw new RuntimeException("CANNOT FIND MIX VARIATION SUFFIX");
                            }

                            //variationSentiment.addAll(mixExtraction);
                            //variationSentiment.addAll(characteristicExtraction);

                            //HACK HARD CODING THE LABELS ...
                            //HACK HARD CODING THE LABELS ...
                            //HACK HARD CODING THE LABELS ...

                            variationToSentiment.put(mixVariationId, variationSentiment);
                        }
                    }

                    progressionUnit.getProgressionUnitBars().get(i).setSentimentLabels(variationToSentiment);
                }
            }
        }

        return queueState;
    }

    private String generateMixVariationId(String id, int mixIndex, int variationIndex){
        return id + "_" + mixIndex + "_" + (variationIndex + 1);
    }
}