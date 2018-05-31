package com.treblemaker.weighters;


import com.treblemaker.weighters.enums.WeightClass;

public class TotalWeight {

    public static Integer calculateTotal(WeightClass... args){

        Integer totalWeight = 1;

        Integer badCount = 0;
        Integer goodCount = 0;

        for (WeightClass weight : args) {

            if(weight != null){
                if(weight == WeightClass.BAD){
                    badCount = badCount + 1;
                }else if(weight == WeightClass.GOOD){
                    goodCount = goodCount + 1;
                }
            }
        }

        totalWeight = totalWeight + goodCount;

        totalWeight = totalWeight - badCount;

        if(totalWeight < 1){
            totalWeight = 1;
        }

        return totalWeight;
    }
}
