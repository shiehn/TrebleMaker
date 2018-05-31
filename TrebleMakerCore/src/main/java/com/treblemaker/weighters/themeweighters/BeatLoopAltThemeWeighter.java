package com.treblemaker.weighters.themeweighters;

import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.weighters.enums.WeightClass;
import org.springframework.stereotype.Component;

@Component
public class BeatLoopAltThemeWeighter {

    public WeightClass calculateThemeWeight(BeatLoop optionToWeight, ProgressionUnitBar oneBarPrior, ProgressionUnitBar secondBarPrior){

        WeightClass weightClass = WeightClass.BAD;

        if(secondBarPrior != null &&
                secondBarPrior.getBeatLoopAlt() != null &&
                optionToWeight.getId().equals(secondBarPrior.getBeatLoopAlt().getId())){
            weightClass = WeightClass.OK;
        }

        if(oneBarPrior != null &&
                oneBarPrior.getBeatLoopAlt() != null &&
                optionToWeight.getId().equals(oneBarPrior.getBeatLoopAlt().getId())){
            weightClass = WeightClass.GOOD;
        }

        return weightClass;
    }
}
