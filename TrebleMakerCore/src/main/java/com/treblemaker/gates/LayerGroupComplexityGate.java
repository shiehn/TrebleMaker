package com.treblemaker.gates;

import com.treblemaker.gates.interfaces.IComplexityThreshold;
import com.treblemaker.model.types.Composition;

public class LayerGroupComplexityGate implements IComplexityThreshold {

    @Override
    public boolean shouldGateLayer(Composition.Layer layer, int unitComplexity) {
        return false;
    }

    @Override
    public int getLayerGroupComplexityTotal(Composition.LayerGroup layGroup) {
        return 0;
    }

    @Override
    public int getLayerGroupComplexityThreshold(Composition.LayerGroup layGroup) {
        return 0;
    }
}
