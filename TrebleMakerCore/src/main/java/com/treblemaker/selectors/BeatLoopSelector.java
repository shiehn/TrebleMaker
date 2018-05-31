package com.treblemaker.selectors;

import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.interfaces.IWeightableLoop;
import com.treblemaker.selectors.interfaces.IBeatLoopSelector;
import com.treblemaker.weighters.helper.WeightDistributor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class BeatLoopSelector implements IBeatLoopSelector {

    @Override
    public BeatLoop selectBeatLoop(List<BeatLoop> beatLoops) {

        //TODO you need some form of inteligence here ..
        //TODO you need some form of inteligence here ..
        //TODO you need some form of inteligence here ..
        BeatLoop beatLoop = beatLoops.get(new Random().nextInt(beatLoops.size()));

        return beatLoop;
    }

    @Override
    public IWeightableLoop selectByWeight(List<? extends IWeightableLoop> weightables) {

        List<IWeightableLoop> weightableLoops = WeightDistributor.distributeIWeightableListByWeights(weightables);

        return weightableLoops.get(new Random().nextInt(weightableLoops.size()));

//        IWeightableLoop highestWeighted = weightables.get(0);
//
//       for(int i=0; i<weightables.size(); i++){
//           if(weightables.get(i).getTotalWeight() > highestWeighted.getTotalWeight()){
//               highestWeighted = weightables.get(i);
//           }
//       }
//
//        return highestWeighted;
    }

    @Override
    public IWeightableLoop selectByWeightAndBarCount(List<? extends IWeightableLoop> weightables, int maxBarCount) {

        List<IWeightableLoop> filteredByBarCount = new ArrayList<>();

        weightables.forEach(weightable -> {

            if(weightable.getBarCount() <= maxBarCount){
                filteredByBarCount.add(weightable);
            }
        });

        return selectByWeight(filteredByBarCount);
    }
}
