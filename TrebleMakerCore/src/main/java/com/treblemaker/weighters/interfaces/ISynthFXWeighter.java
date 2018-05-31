package com.treblemaker.weighters.interfaces;

import com.treblemaker.model.fx.FXArpeggioWithRating;
import com.treblemaker.model.queues.QueueState;

import java.util.List;
import java.util.Map;

public interface ISynthFXWeighter {

    Map<Integer, List<FXArpeggioWithRating>> setWeights(QueueState queueState, Map<Integer, List<FXArpeggioWithRating>> synthIdToFXOptions, int variationIndex);
}
