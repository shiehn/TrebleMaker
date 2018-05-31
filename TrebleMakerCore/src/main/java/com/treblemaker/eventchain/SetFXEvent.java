package com.treblemaker.eventchain;

import com.treblemaker.dal.interfaces.IFXArpeggioDelayDal;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.fx.FXHelper;
import com.treblemaker.model.fx.FXArpeggioDelay;
import com.treblemaker.model.fx.FXArpeggioWithRating;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.selectors.interfaces.ISynthFXSelector;
import com.treblemaker.weighters.interfaces.ISynthFXWeighter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SetFXEvent implements IEventChain {

    private IFXArpeggioDelayDal fxArpeggioDelayDal;
    private ISynthFXWeighter synthFXWeighter;
    private ISynthFXSelector synthFXSelector;

    public SetFXEvent(IFXArpeggioDelayDal fxArpeggioDelayDal, ISynthFXWeighter synthFXWeighter, ISynthFXSelector synthFXSelector) {
        this.fxArpeggioDelayDal = fxArpeggioDelayDal;
        this.synthFXWeighter = synthFXWeighter;
        this.synthFXSelector = synthFXSelector;
    }

    @Override
    public QueueState set(QueueState queueState) {

        //TODO MOVE TO HAZELCAST??
        //List<FXArpeggioDelay> fxArpeggioDelays = fxArpeggioDelayDal.findAll().stream().filter(fx -> fx.getId()==1004).collect(Collectors.toList());

        //HACKER TO ALWAY USE SPECIFIC FX TYPE!!!!!
        List<FXArpeggioDelay> fxArpeggioDelays = fxArpeggioDelayDal.findAll().stream().filter(fx -> fx.getId()==1049).collect(Collectors.toList());

        List<FXArpeggioWithRating> fxArpeggioDelaysPrimary = fxArpeggioDelays.stream().map(fxArpeggioDelay -> new FXArpeggioWithRating(fxArpeggioDelay)).collect(Collectors.toList());
        List<FXArpeggioWithRating> fxArpeggioDelaysAlt = fxArpeggioDelays.stream().map(fxArpeggioDelay -> new FXArpeggioWithRating(fxArpeggioDelay)).collect(Collectors.toList());

        List<List<Integer>> primaryAndAltSynthIds = FXHelper.extractHiSynthIds(queueState.getStructure());

        List<Map<Integer, List<FXArpeggioWithRating>>> synthIdToFXOptions = new ArrayList<>();

        for (int i = 0; i < primaryAndAltSynthIds.size(); i++) {

            Map<Integer, List<FXArpeggioWithRating>> map = new HashMap<>();
            map.put(primaryAndAltSynthIds.get(i).get(0), fxArpeggioDelaysPrimary);
            //map.put(primaryAndAltSynthIds.get(i).get(1), fxArpeggioDelaysAlt);
            synthIdToFXOptions.add(map);

            Map<Integer, List<FXArpeggioWithRating>> synthIdToWeightedFxOptions = synthFXWeighter.setWeights(queueState, synthIdToFXOptions.get(i), i);

            Map<Integer, FXArpeggioWithRating> synthIdToSelectedFXOption = synthFXSelector.selectSynthFX(synthIdToWeightedFxOptions);

            for (ProgressionUnit progressionUnit : queueState.getStructure()) {
                for (ProgressionUnitBar pBar : progressionUnit.getProgressionUnitBars()) {
                    pBar.addSelectedFXMap(synthIdToSelectedFXOption);
                }
            }
        }

        return queueState;
    }
}

