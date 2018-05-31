package com.treblemaker.weighters.helper;

import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;

public class WeightHelper {

    public static CurrentPriorBar createCurrentPriorBar(int currentBarIndex, ProgressionUnit currentProgressionUnit, ProgressionUnit priorProgressionUnit){



        ProgressionUnitBar currentBar = currentProgressionUnit.getProgressionUnitBars().get(currentBarIndex);
        ProgressionUnitBar oneBarPrior = null;
        ProgressionUnitBar secondBarPrior = null;

        if(currentBarIndex == 0 && priorProgressionUnit != null){
            oneBarPrior = priorProgressionUnit.getProgressionUnitBars().get(3);
            secondBarPrior = priorProgressionUnit.getProgressionUnitBars().get(2);
        } else if(currentBarIndex == 1 & priorProgressionUnit != null){
            oneBarPrior = currentProgressionUnit.getProgressionUnitBars().get(0);
            secondBarPrior = priorProgressionUnit.getProgressionUnitBars().get(3);
        } else if (currentBarIndex == 2){
            oneBarPrior = currentProgressionUnit.getProgressionUnitBars().get(1);
            secondBarPrior = currentProgressionUnit.getProgressionUnitBars().get(0);
        } else if (currentBarIndex == 3){
            oneBarPrior = currentProgressionUnit.getProgressionUnitBars().get(2);
            secondBarPrior = currentProgressionUnit.getProgressionUnitBars().get(1);
        }

        //handle prior nulls
        else if(currentBarIndex == 0 && priorProgressionUnit == null){
            oneBarPrior = null;
            secondBarPrior = null;
        } else if(currentBarIndex == 1 & priorProgressionUnit == null){
            oneBarPrior = currentProgressionUnit.getProgressionUnitBars().get(0);
            secondBarPrior = null;
        }


        CurrentPriorBar currentPriorBar = new CurrentPriorBar();
        currentPriorBar.setCurrentBar(currentBar);
        currentPriorBar.setOneBarPrior(oneBarPrior);
        currentPriorBar.setSecondBarPrior(secondBarPrior);

        return currentPriorBar;
    }
}
