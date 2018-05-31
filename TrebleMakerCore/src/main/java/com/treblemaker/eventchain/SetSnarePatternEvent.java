package com.treblemaker.eventchain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.treblemaker.Application;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.snare.SnarePattern;
import com.treblemaker.model.snare.SnareSample;
import com.treblemaker.options.SnareSampleOptions;
import com.treblemaker.selectors.SnarePatternSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class SetSnarePatternEvent implements IEventChain {

    private SnareSampleOptions snarePatternOptions;
    private ObjectMapper objectMapper;
    private SnarePatternSelector snarePatternSelector;

    @Autowired
    public SetSnarePatternEvent(SnareSampleOptions snarePatternOptions, ObjectMapper objectMapper, SnarePatternSelector snarePatternSelector) {
        this.snarePatternOptions = snarePatternOptions;
        this.objectMapper = objectMapper;
        this.snarePatternSelector = snarePatternSelector;
    }

    @Override
    public QueueState set(QueueState queueState) {

        //generate kick sample options ..
        List<SnareSample> snareSampleOptions = snarePatternOptions.getSnareSampleOptions(queueState.getQueueItem().getStationId(), queueState.getDataSource());

        //generate snare pattern options ..
        List<SnarePattern> snarePatternOptions = queueState.getDataSource().getSnarePatterns();

        //create a type to Pattern option map
        Map<ProgressionUnit.ProgressionType, List<SnarePattern>> typeToPatternOptions = new HashMap<>();
        //TODO COPY ARRAY !!!!!
        queueState.getStructure().stream().filter(progressionUnit -> typeToPatternOptions.get(progressionUnit.getType()) == null).forEach(progressionUnit -> {
            //TODO COPY ARRAY !!!!!
            List<SnarePattern> clonedSnarePatterns = deepCloneSnarePatterns(snarePatternOptions);
            typeToPatternOptions.put(progressionUnit.getType(), clonedSnarePatterns);
        });

        //*******HACK TEMP HACK********
        //randomly select one pattern per progression type
        //randomly select one pattern per progression type
        //randomly select one pattern per progression type
        Map<ProgressionUnit.ProgressionType, SnarePattern> typeToSelectedPatten = new HashMap<>();
        for (Map.Entry<ProgressionUnit.ProgressionType, List<SnarePattern>> entry : typeToPatternOptions.entrySet()) {
            typeToSelectedPatten.put(entry.getKey(), entry.getValue().get(new Random().nextInt(entry.getValue().size())));
        }

        //ADD THE SELECTED PATTERN TO THE PROGRESSION UNIT BARS ..
        queueState = snarePatternSelector.setSelectedPatterns(typeToSelectedPatten, queueState); //NEEDS TEST

        return queueState;
    }

    private List<SnarePattern> deepCloneSnarePatterns(List<SnarePattern> snarePatternOptions) {

        List<SnarePattern> deepCopies = new ArrayList<>();

        for (SnarePattern snarePattern : snarePatternOptions) {
            try {
                String patternAsString = objectMapper.writeValueAsString(snarePattern);
                SnarePattern deepCopy = objectMapper.readValue(patternAsString, SnarePattern.class);
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
