package com.treblemaker.utils.rhythmic;

import com.treblemaker.model.RhythmicAccents;

import java.util.ArrayList;
import java.util.List;

public class RhythmicAccentUtil {

    public static List<RhythmicAccents> normalizeAccentsToTwoBars(List<RhythmicAccents> rhythmicAccents){

        List<RhythmicAccents> normalizedAccents = new ArrayList<>();

        if(rhythmicAccents.size() < 2){
            normalizedAccents.add(rhythmicAccents.get(0));
            normalizedAccents.add(rhythmicAccents.get(0));
        }else{
            normalizedAccents.add(rhythmicAccents.get(0));
            normalizedAccents.add(rhythmicAccents.get(1));
        }

        return normalizedAccents;
    }

    public static int[] extractAccentsAsArray(List<RhythmicAccents> rhythmicAccents){

        rhythmicAccents = normalizeAccentsToTwoBars(rhythmicAccents);

        int[] accents = new int[32];

        int index = 0;
        for (RhythmicAccents rAccents : rhythmicAccents) {
            int[] accentsAsInts = rAccents.getAsIntegerArray();
            for (int a : accentsAsInts) {
                accents[index] = a;
                index++;
            }
        }

        return accents;
    }
}
