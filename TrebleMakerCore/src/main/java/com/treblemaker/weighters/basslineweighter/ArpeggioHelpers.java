package com.treblemaker.weighters.basslineweighter;

import com.treblemaker.weighters.enums.WeightClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArpeggioHelpers {

    public WeightClass generateRandomWeight(){

        List<WeightClass> randomWeights = new ArrayList<>();
        randomWeights.add(WeightClass.BAD);
        randomWeights.add(WeightClass.OK);
        randomWeights.add(WeightClass.GOOD);

        Random randomizer = new Random();
        return randomWeights.get(randomizer.nextInt(randomWeights.size()));
    }

    public WeightClass stringToWeight(String stringWeight){

        if(stringWeight.equalsIgnoreCase("0")){
            return WeightClass.BAD;
        }else if(stringWeight.equalsIgnoreCase("1")){
            return WeightClass.OK;
        }else{
            return WeightClass.GOOD;
        }
    }

    public String createNeuralInput(double[] intervals, double[] durations, int[] beatLoopAccents, int[] harmonicLoopAccents){

        StringBuilder stringBuilder = new StringBuilder();

        int length=32;
        for(int i=0; i<length; i++){
            stringBuilder.append(intervals[i]);
            stringBuilder.append(",");
            stringBuilder.append(durations[i]);
            stringBuilder.append(",");
            stringBuilder.append(beatLoopAccents[i]);
            stringBuilder.append(",");
            stringBuilder.append(harmonicLoopAccents[i]);
            if(i <length-1){
                stringBuilder.append(",");
            }
        }

        return stringBuilder.toString();
    }
}
