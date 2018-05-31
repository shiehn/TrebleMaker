package com.treblemaker.gates.interfaces;

import com.treblemaker.model.types.Composition;
import org.springframework.stereotype.Component;

/**
 * Created by Steve on 2015-09-06.
 */
@Component
public interface IComplexityThreshold {

    boolean shouldGateLayer(Composition.Layer layer, int unitComplexity);

    int getLayerGroupComplexityTotal(Composition.LayerGroup layGroup);

    int getLayerGroupComplexityThreshold(Composition.LayerGroup layGroup);
}