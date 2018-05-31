package com.treblemaker.selectors;

import com.treblemaker.model.fx.FXArpeggioWithRating;
import com.treblemaker.selectors.interfaces.ISynthFXSelector;
import com.treblemaker.weighters.helper.WeightDistributor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SynthFXSelector implements ISynthFXSelector {

    @Override
    public Map<Integer, FXArpeggioWithRating> selectSynthFX(Map<Integer, List<FXArpeggioWithRating>> progressionTypeListMap) {

        Map<Integer, List<FXArpeggioWithRating>> distributedLoops = new HashMap<>();

        for (Map.Entry<Integer, List<FXArpeggioWithRating>> entry : progressionTypeListMap.entrySet())
        {
            List<FXArpeggioWithRating> distributeListByWeights = WeightDistributor.distributeFXListByWeights(entry.getValue());
            distributedLoops.put(entry.getKey(), distributeListByWeights);
        }

        Map<Integer, FXArpeggioWithRating> selectedFXMap = new HashMap<>();
        //SELECT THE 2 RANDOMS
        for (Map.Entry<Integer, List<FXArpeggioWithRating>> entry : distributedLoops.entrySet())
        {
//            while(randomSelections.size() < 2){
                FXArpeggioWithRating randomSelection = distributedLoops.get(entry.getKey()).get(new Random().nextInt(distributedLoops.get(entry.getKey()).size()));

//                if(!randomSelections.contains(randomSelection)){
//                    randomSelections.add(randomSelection);
//                }
//            }

            selectedFXMap.put(entry.getKey(), randomSelection);
        }

        return selectedFXMap;
    }
}
