package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.generators.VolumeLevelGenerator;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.selectors.VolumeLevelSelector;
import com.treblemaker.weighters.volumeweighter.VolumeWeighter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SetTargetVolumeMixEvent implements IEventChain {

    @Autowired
    private VolumeWeighter volumeWeighter;

    @Autowired
    private VolumeLevelGenerator volumeLevelGenerator;

    @Autowired
    private VolumeLevelSelector volumeLevelSelector;

    @Override
    public QueueState set(QueueState queueState) {

        List<Map<String, Double>> potentialMixes = volumeLevelGenerator.generatePotentialMixes();

        //WEIGHT POTENTIAL MEANS ...
        //List<VolumeWeighterTaskResponse> weightedMixes = volumeWeighter.weightVolumes(potentialMixes);

        //SELECTOR ..
        //Map<String, Double> selectedMix = volumeLevelSelector.selectFromWeightedMixes(weightedMixes);

        //queueState.getQueueItem().setSelectedVolumeTargets(selectedMix);

        queueState.getQueueItem().setSelectedVolumeTargets(potentialMixes.get(0));

        return queueState;
    }
}
