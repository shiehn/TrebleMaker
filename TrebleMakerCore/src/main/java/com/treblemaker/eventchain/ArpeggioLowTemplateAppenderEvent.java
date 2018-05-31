package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.generators.interfaces.ISynthBassGenerator;
import com.treblemaker.model.bassline.BasslineWithRating;
import com.treblemaker.model.queues.QueueState;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public class ArpeggioLowTemplateAppenderEvent implements IEventChain {

    @Autowired
    private ISynthBassGenerator synthBassGenerator;

    @Override
    public QueueState set(QueueState queueState) {

        HashMap<Integer, List<BasslineWithRating>> basslineMap = new HashMap<>();

        queueState.getStructure().forEach(progressionUnit -> {
            synthBassGenerator.generateSynthBassOptions(progressionUnit, queueState.getDataSource().getBasslines(), basslineMap);
        });

        queueState.getStructure().forEach(progressionUnit -> {
            synthBassGenerator.setSynthBassOptions(progressionUnit, queueState.getDataSource().getBasslines(), basslineMap, queueState.getQueueItem().getBpm());
        });

        return queueState;
    }
}
