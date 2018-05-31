package com.treblemaker.weighters.synthweighter;

import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.weighters.enums.WeightClass;
import com.treblemaker.weighters.interfaces.ISynthWeighter;
import org.springframework.stereotype.Component;

import static com.treblemaker.model.SynthTemplateOption.SynthRoll;

@Component
public class SynthWeighter implements ISynthWeighter {

    @Override
    public void setWeights(ProgressionUnitBar progressionUnitBar, SynthRoll synthRoll) {

        //TODO DO THIS IS JUST STUBBED OUT MUST DO WEIGHTING FOR REAL!!!!!!!!
        //TODO DO THIS IS JUST STUBBED OUT MUST DO WEIGHTING FOR REAL!!!!!!!!
        //TODO DO THIS IS JUST STUBBED OUT MUST DO WEIGHTING FOR REAL!!!!!!!!
        //TODO DO THIS IS JUST STUBBED OUT MUST DO WEIGHTING FOR REAL!!!!!!!!

        switch (synthRoll) {
            case SYNTH_HI:
                progressionUnitBar.getSynthTemplateHiOptions().forEach(synthTemplateOptions -> synthTemplateOptions.forEach(s -> s.setWeightClass(WeightClass.OK)));
                break;
            case SYNTH_MID:
                progressionUnitBar.getSynthTemplateMidOptions().forEach(synthTemplateOptions -> synthTemplateOptions.forEach(s -> s.setWeightClass(WeightClass.OK)));
                break;
            case SYNTH_LOW:
                progressionUnitBar.getSynthTemplateLowOptions().forEach(synthTemplateOptions -> synthTemplateOptions.forEach(s -> s.setWeightClass(WeightClass.OK)));
                break;
        }

        //TODO DO THIS IS JUST STUBBED OUT MUST DO WEIGHTING FOR REAL!!!!!!!!
        //TODO DO THIS IS JUST STUBBED OUT MUST DO WEIGHTING FOR REAL!!!!!!!!
        //TODO DO THIS IS JUST STUBBED OUT MUST DO WEIGHTING FOR REAL!!!!!!!!
        //TODO DO THIS IS JUST STUBBED OUT MUST DO WEIGHTING FOR REAL!!!!!!!!
    }
}
