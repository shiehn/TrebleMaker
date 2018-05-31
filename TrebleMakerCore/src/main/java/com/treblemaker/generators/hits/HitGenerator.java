package com.treblemaker.generators.hits;

import com.treblemaker.Application;
import com.treblemaker.generators.interfaces.IHitGenerator;
import com.treblemaker.model.hitsandfills.Hit;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.selectors.interfaces.IHitSelector;
import com.treblemaker.weighters.hits.IHitWeighter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class HitGenerator implements IHitGenerator {

    @Autowired
    private IHitWeighter hitWeighter;

    @Autowired
    private IHitSelector hitSelector;

    @Override
    public QueueState setHitOptions(QueueState queueState) {

        //upto 2 hits per progression type
//        HashMap<ProgressionType, List<Hit>> pTypeToHitOptions = new HashMap<>();

        //set primary options
        //set secondary options
        for (ProgressionUnit pUnit : queueState.getStructure()) {
            for(ProgressionUnitBar pBar : pUnit.getProgressionUnitBars()){

                List<Hit> hitOptions = new ArrayList<>();

                for(Hit hitOption : queueState.getDataSource().getHits(queueState.getQueueItem().getStationId())){

                    if(hitOption.getCompatibleChord() == null || pBar.getChord().isEqualOrTriadMatch(hitOption.getCompatibleChord())){
                        try {
                            hitOptions.add(hitOption.clone());
                        } catch (CloneNotSupportedException e) {
                            Application.logger.debug("LOG:", e);
                        }
                    }
                }

                pBar.setHitOptions(hitOptions);
            }
        }

        return queueState;
    }

    @Override
    public QueueState weightHitOptions(QueueState queueState) {

        return hitWeighter.setHitWeights(queueState);
    }

    @Override
    public QueueState selectHits(QueueState queueState) {

        return hitSelector.selectHits(queueState);
    }
}
