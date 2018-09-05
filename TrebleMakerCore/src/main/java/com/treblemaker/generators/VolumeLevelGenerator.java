package com.treblemaker.generators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.*;
import com.treblemaker.constants.*;

@Component
public class VolumeLevelGenerator {

    public final int DB_RANGE = 1;
    public final int NUM_OF_MIXES = 1;

    @Value("${num_of_generated_mixes}")
    int numOfGeneratedMixes;

    @Value("${num_of_alt_melodies}")
    int numOfAltMelodies;

    public List<Map<String, Double>> generatePotentialMixes(){

        List<Map<String, Double>> potentialMixes = new ArrayList<>();

        for(int i=0; i<numOfGeneratedMixes; i++){
            potentialMixes.add(generatePotentialMix());
        }

        return potentialMixes;
    }

    public Map<String, Double> generatePotentialMix(){

        Map<String, Double> potenialMix = new HashMap<>();
        potenialMix.put(MixRoles.COMP_HI_FX, 18.0);
        potenialMix.put(MixRoles.COMP_HI_ALT_FX, 18.0);
        potenialMix.put(MixRoles.COMP_MID, 10.0);
        potenialMix.put(MixRoles.COMP_MID_ALT, 10.0);
        potenialMix.put(MixRoles.COMP_LOW, 14.0);
        potenialMix.put(MixRoles.COMP_LOW_ALT, 14.0);
        potenialMix.put(MixRoles.COMP_RHYTHM, 14.0);
        potenialMix.put(MixRoles.COMP_RHYTHM_ALT, 14.0);
        potenialMix.put(MixRoles.COMP_AMBIENCE, 9.0);
        potenialMix.put(MixRoles.COMP_HARMONIC, 5.5);
        potenialMix.put(MixRoles.COMP_HARMONIC_ALT, 5.0);
        potenialMix.put(MixRoles.HITS, 14.0);
        potenialMix.put(MixRoles.FILLS, 14.0);
        potenialMix.put(MixRoles.KICK, 17.0);
        potenialMix.put(MixRoles.SNARE, 14.0);
        potenialMix.put(MixRoles.HAT, 14.0);

        for(int i=0; i<numOfAltMelodies; i++){
            potenialMix.put(MixRoles.COMP_MELODY+i, 23.0);
        }

        return potenialMix;
    }

    public double generateRandomDecibleLevel(int min, int max){

        if(min < 1){
            min = 1;
        }

        if(max > DB_RANGE){
            max = DB_RANGE;
        }

        if(min > max){
            max = min;
        }

        Random random = new Random();
        return 1.0 * (double)random.nextInt(max - min + 1) + min;
    }
}
