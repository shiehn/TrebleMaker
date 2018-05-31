package com.treblemaker.selectors;

import com.treblemaker.weighters.volumeweighter.VolumeWeighterTaskResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class VolumeLevelSelector {

    public Map<String, Double> selectFromWeightedMixes(List<VolumeWeighterTaskResponse> weightedMixes){

        List<Map<String, Double>> distributedWeights = distributeWeights(weightedMixes);

        return distributedWeights.get(new Random().nextInt(distributedWeights.size()));
    }

    public List<Map<String, Double>> distributeWeights(List<VolumeWeighterTaskResponse> weightedMixes){

        List<Map<String, Double>> distibutedMixes = new ArrayList<>();

        for(VolumeWeighterTaskResponse mix : weightedMixes){

            for(int i=0; i<mix.getWeightClass().ordinal()+1; i++){
                distibutedMixes.add(mix.getPotentialMix());
            }
        }

        return distibutedMixes;
    }
}
