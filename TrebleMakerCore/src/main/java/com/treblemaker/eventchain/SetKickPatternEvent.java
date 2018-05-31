package com.treblemaker.eventchain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.treblemaker.Application;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.kick.KickPattern;
import com.treblemaker.model.kick.KickSample;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.options.KickSampleOptions;
import com.treblemaker.selectors.KickPatternSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

import static com.treblemaker.model.progressions.ProgressionUnit.ProgressionType;

@Component
public class SetKickPatternEvent implements IEventChain {

    private KickSampleOptions kickPatternOptions;
    private ObjectMapper objectMapper;
    private KickPatternSelector kickPatternSelector;

    @Autowired
    public SetKickPatternEvent(KickSampleOptions kickPatternOptions, ObjectMapper objectMapper, KickPatternSelector kickPatternSelector) {
        this.kickPatternOptions = kickPatternOptions;
        this.objectMapper = objectMapper;
        this.kickPatternSelector = kickPatternSelector;
    }

    @Override
    public QueueState set(QueueState queueState) {

        //generate kick sample options ..
        List<KickSample> kickSampleOptions = kickPatternOptions.getKickSampleOptions(queueState.getQueueItem().getStationId(), queueState.getDataSource());

        //generate kick pattern options ..
        List<KickPattern> kickPatternOptions = queueState.getDataSource().getKickPatterns();

        //create a type to Pattern option map
        Map<ProgressionType, List<KickPattern>> typeToPatternOptions = new HashMap<>();
        //TODO COPY ARRAY !!!!!
        queueState.getStructure().stream().filter(progressionUnit -> typeToPatternOptions.get(progressionUnit.getType()) == null).forEach(progressionUnit -> {
            //TODO COPY ARRAY !!!!!
            List<KickPattern> clonedKickPatterns = deepCloneKickPatterns(kickPatternOptions);
            typeToPatternOptions.put(progressionUnit.getType(), clonedKickPatterns);
        });

        //*******HACK TEMP HACK********
        //randomly select one pattern per progression type
        //randomly select one pattern per progression type
        //randomly select one pattern per progression type
        Map<ProgressionType, KickPattern> typeToSelectedPatten = new HashMap<>();
        for (Map.Entry<ProgressionType, List<KickPattern>> entry : typeToPatternOptions.entrySet()) {
            typeToSelectedPatten.put(entry.getKey(), entry.getValue().get(new Random().nextInt(entry.getValue().size())));
        }

        //ADD THE SELECTED PATTERN TO THE PROGRESSION UNIT BARS ..
        queueState = kickPatternSelector.setSelectedPatterns(typeToSelectedPatten, queueState); //NEEDS TEST

        return queueState;
    }

    private List<KickPattern> deepCloneKickPatterns(List<KickPattern> kickPatternOptions) {

        List<KickPattern> deepCopies = new ArrayList<>();

        for (KickPattern kickPattern : kickPatternOptions) {
            try {
                String patternAsString = objectMapper.writeValueAsString(kickPattern);
                KickPattern deepCopy = objectMapper.readValue(patternAsString, KickPattern.class);
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
