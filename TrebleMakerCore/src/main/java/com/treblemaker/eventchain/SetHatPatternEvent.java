package com.treblemaker.eventchain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.treblemaker.Application;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.hat.HatPattern;
import com.treblemaker.model.hat.HatSample;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.options.HatSampleOptions;
import com.treblemaker.selectors.HatPatternSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class SetHatPatternEvent implements IEventChain {

    private HatSampleOptions hatPatternOptions;
    private ObjectMapper objectMapper;
    private HatPatternSelector hatPatternSelector;

    @Autowired
    public SetHatPatternEvent(HatSampleOptions hatPatternOptions, ObjectMapper objectMapper, HatPatternSelector hatPatternSelector) {
        this.hatPatternOptions = hatPatternOptions;
        this.objectMapper = objectMapper;
        this.hatPatternSelector = hatPatternSelector;
    }

    @Override
    public QueueState set(QueueState queueState) {

        //generate hat sample options ..
        List<HatSample> hatSampleOptions = hatPatternOptions.getHatSampleOptions(queueState.getQueueItem().getStationId(), queueState.getDataSource());

        //generate hat pattern options ..
        List<HatPattern> hatPatternOptions = queueState.getDataSource().getHatPatterns();

        //create a type to Pattern option map
        Map<ProgressionUnit.ProgressionType, List<HatPattern>> typeToPatternOptions = new HashMap<>();
        //TODO COPY ARRAY !!!!!
        queueState.getStructure().stream().filter(progressionUnit -> typeToPatternOptions.get(progressionUnit.getType()) == null).forEach(progressionUnit -> {
            //TODO COPY ARRAY !!!!!
            List<HatPattern> clonedHatPatterns = deepCloneHatPatterns(hatPatternOptions);
            typeToPatternOptions.put(progressionUnit.getType(), clonedHatPatterns);
        });

        //*******HACK TEMP HACK********
        //randomly select one pattern per progression type
        //randomly select one pattern per progression type
        //randomly select one pattern per progression type
        Map<ProgressionUnit.ProgressionType, HatPattern> typeToSelectedPatten = new HashMap<>();
        for (Map.Entry<ProgressionUnit.ProgressionType, List<HatPattern>> entry : typeToPatternOptions.entrySet()) {
            typeToSelectedPatten.put(entry.getKey(), entry.getValue().get(new Random().nextInt(entry.getValue().size())));
        }

        //ADD THE SELECTED PATTERN TO THE PROGRESSION UNIT BARS ..
        queueState = hatPatternSelector.setSelectedPatterns(typeToSelectedPatten, queueState); //NEEDS TEST

        return queueState;
    }

    private List<HatPattern> deepCloneHatPatterns(List<HatPattern> hatPatternOptions) {

        List<HatPattern> deepCopies = new ArrayList<>();

        for (HatPattern hatPattern : hatPatternOptions) {
            try {
                String patternAsString = objectMapper.writeValueAsString(hatPattern);
                HatPattern deepCopy = objectMapper.readValue(patternAsString, HatPattern.class);
                deepCopies.add(deepCopy);
            } catch (JsonProcessingException e) {
                Application.logger.debug("LOG:", e);
            } catch (IOException e) {
                Application.logger.debug("LOG:", e);
            }
        }

        return deepCopies;
    }
}
