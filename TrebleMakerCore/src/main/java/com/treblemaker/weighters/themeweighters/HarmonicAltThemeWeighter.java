package com.treblemaker.weighters.themeweighters;

import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.weighters.enums.WeightClass;
import org.springframework.stereotype.Component;

@Component
public class HarmonicAltThemeWeighter {

    public WeightClass calculateThemeWeight(HarmonicLoop optionToWeight, ProgressionUnitBar oneBarPrior, ProgressionUnitBar secondBarPrior){

        WeightClass weightClass = WeightClass.BAD;

        if(secondBarPrior != null &&
                secondBarPrior.getHarmonicLoopAlt() != null &&
                optionToWeight.getId().equals(secondBarPrior.getHarmonicLoopAlt().getId())){
            weightClass = WeightClass.OK;
        }

        if(oneBarPrior != null &&
                oneBarPrior.getHarmonicLoopAlt() != null &&
                optionToWeight.getId().equals(oneBarPrior.getHarmonicLoopAlt().getId())){
            weightClass = WeightClass.GOOD;
        }

        return weightClass;
    }
}
