package com.treblemaker.gates;

import com.treblemaker.gates.interfaces.IComplexityThreshold;
import com.treblemaker.gates.interfaces.IGateSetter;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.types.Composition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ComplexityGateSetter implements IGateSetter {

    @Autowired
    @Qualifier(value = "layerGroupComplexityGate")
    private IComplexityThreshold layerGroupComplexityGate;

    @Autowired
    @Qualifier(value = "templateGroupComplexityGate")
    private IComplexityThreshold templateGroupComplexityGate;

    @Override
    public QueueItem setComplextityGates(QueueItem queueItem) {

        //SET GATING FLAG FOR EACH LAYER TYPE ..
        for(ProgressionUnit pUnit : queueItem.getProgression().getStructure()){

            HashMap<Composition.Layer, Boolean> gatedLayers = new HashMap<>();

            for(Composition.Layer layer : Composition.Layer.values()){
                gatedLayers.put(layer, templateGroupComplexityGate.shouldGateLayer(layer, pUnit.getComplexity()));
            }

            pUnit.setGatedLayers(gatedLayers);
        }

        return queueItem;
    }
}
