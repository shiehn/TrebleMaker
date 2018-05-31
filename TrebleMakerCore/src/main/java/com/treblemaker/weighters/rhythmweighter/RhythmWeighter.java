package com.treblemaker.weighters.rhythmweighter;

import com.treblemaker.weighters.enums.WeightClass;
import com.treblemaker.weighters.interfaces.IRhythmWeighter;

import com.treblemaker.weighters.models.NormalizedAccents;
import com.treblemaker.model.RhythmicAccents;
import com.treblemaker.model.interfaces.IRhythmicLoop;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RhythmWeighter implements IRhythmWeighter {

    @Value("${bypass_rhythm_ratings}")
    private boolean bypassRatings;

    private boolean isAccent(String referenceAccent, String accent){
        return (referenceAccent.equalsIgnoreCase("1") || accent.equalsIgnoreCase("1"));
    }

    private boolean isAccentMatch(String referenceAccent, String accent){
        return (referenceAccent.equalsIgnoreCase("1") && accent.equalsIgnoreCase("1"));
    }

    private boolean isLoneAccent(String referenceAccent, String accent){

        if((referenceAccent.equalsIgnoreCase("1") && !accent.equalsIgnoreCase("1")) || (!referenceAccent.equalsIgnoreCase("1") && accent.equalsIgnoreCase("1"))){
            return true;
        }

        return false;
    }

    private boolean hasNoNeighbours(List<String> referenceAccents, List<String> accents, int index){

        if(index>0){
            if(isAccent(referenceAccents.get(index - 1), accents.get(index - 1))){
                return false;
            }
        }

        //check greaterThan neighbour
        if(index<(referenceAccents.size()-1)){
            if(isAccent(referenceAccents.get(index + 1), accents.get(index + 1))){
                return false;
            }
        }

        return true;
    }

    private boolean hasMatchingNeighbour(List<String> referenceAccents, List<String> accents, int index){

        //REFERENCE ACCENT
        //check lessThan neighbour
        if(index>0){
            if(isAccentMatch(referenceAccents.get(index - 1), accents.get(index - 1))){
                return true;
            }
        }

        //check greaterThan neighbour
        if(index<(referenceAccents.size()-1)){
            if(isAccentMatch(referenceAccents.get(index + 1), accents.get(index + 1))){
                return true;
            }
        }

        return false;
    }

    private boolean hasNonMatchingNeighbour(List<String> referenceAccents, List<String> accents, int index){

        //REFERENCE ACCENT
        //check lessThan neighbour

        if(index>0){
            if(isLoneAccent(referenceAccents.get(index - 1), accents.get(index - 1))){
                return true;
            }
        }

        //check greaterThan neighbour
        if(index<(referenceAccents.size()-1)){
            if(isLoneAccent(referenceAccents.get(index + 1), accents.get(index + 1))){
                return true;
            }
        }

        return false;
    }

    @Override
    public int weightNonMatchNoNeighbours(List<String> referenceAccents, List<String> accents) {

        //static int ACCENT_NON_MATCH_NO_NEIGHBOUR = 1;

        int weight = 0;

        for(int i=0; i<referenceAccents.size();i++){

            if(isLoneAccent(referenceAccents.get(i), accents.get(i))){
                if(hasNoNeighbours(referenceAccents,accents,i)){
                    weight = weight + IRhythmWeighter.ACCENT_NON_MATCH_NO_NEIGHBOUR;
                }
            }
        }

        return weight;
    }

    @Override
    public int weightNonMatchNonMatchingNeighour(List<String> referenceAccents, List<String> accents) {

        ////static int ACCENT_NON_MATCH_NEIGHBOUR_NON_MATCHING = -1;

        int weight = 0;

        for(int i=0; i<referenceAccents.size();i++){

            if(isLoneAccent(referenceAccents.get(i), accents.get(i))){

                //TODO HERE ..
                if(hasNonMatchingNeighbour(referenceAccents, accents, i)){
                    weight = weight + IRhythmWeighter.ACCENT_NON_MATCH_NEIGHBOUR_NON_MATCHING;
                }
            }
        }

        return weight;
    }

    @Override
    public int weightNonMatchWithMatchingNeighour(List<String> referenceAccents, List<String> accents) {

        //   0000 0001 0000 0100
        //   0000 0011 0000 1100
        //static int ACCENT_NON_MATCH_NEIGHBOUR_MATCHING = 0;

        int weight = 0;

        for(int i=0; i<referenceAccents.size(); i++){

            if(isLoneAccent(referenceAccents.get(i), accents.get(i))){

                if(hasMatchingNeighbour(referenceAccents, accents, i)){
                    weight = weight + IRhythmWeighter.ACCENT_NON_MATCH_NEIGHBOUR_MATCHING;
                }
            }
        }

        return weight;
    }

    @Override
    public int weightMatchNoNeighbours(List<String> referenceAccents, List<String> accents) {

        //   0000 0010 0000 1000
        //   0000 0010 0000 1000
        //static int ACCENT_MATCH_WEIGHT = 2;

        int weight = 0;

        for(int i=0; i<referenceAccents.size(); i++){

            if(isAccentMatch(referenceAccents.get(i), accents.get(i))){

                if(hasNoNeighbours(referenceAccents, accents, i)){
                    weight = weight + IRhythmWeighter.ACCENT_MATCH_WEIGHT;
                }
            }
        }

        return weight;
    }

    @Override
    public int weightMatchNonMatchingNeighour(List<String> referenceAccents, List<String> accents) {

        //   0000 0011 0000 1100
        //   0000 0010 0000 1000
        //static int ACCENT_MATCH_WEIGHT = 2;

        int weight = 0;

        for(int i=0; i<referenceAccents.size(); i++){

            if(isAccentMatch(referenceAccents.get(i), accents.get(i))){

                if(hasNonMatchingNeighbour(referenceAccents, accents, i)){
                    weight = weight + IRhythmWeighter.ACCENT_MATCH_WEIGHT;
                }
            }
        }

        return weight;
    }

    @Override
    public int weightMatchWithMatchingNeighour(List<String> referenceAccents, List<String> accents) {

        //   0000 0011 0000 1100
        //   0000 0011 0000 1100
        //static int ACCENT_MATCH_WEIGHT = 2;

        int weight = 0;

        for(int i=0; i<referenceAccents.size(); i++){

            if(isAccentMatch(referenceAccents.get(i), accents.get(i))){

                if(hasMatchingNeighbour(referenceAccents, accents, i)){
                    weight = weight + IRhythmWeighter.ACCENT_MATCH_WEIGHT;
                }
            }
        }

        return weight;
    }

    @Override
    public WeightClass calculateRhythmicWeight(IRhythmicLoop referenceLoop, IRhythmicLoop loop) {

        if(bypassRatings){
            return WeightClass.OK;
        }

        if(referenceLoop == null || loop == null){
            return WeightClass.OK;
        }

        NormalizedAccents normalizedAccents = normalizeAccentLengths(referenceLoop, loop);

        int weight = 0;

        weight = weight + weightNonMatchNoNeighbours(normalizedAccents.getNormalizedReferenceAccents(), normalizedAccents.getNormalizedAccents());

        weight = weight + weightNonMatchNonMatchingNeighour(normalizedAccents.getNormalizedReferenceAccents(), normalizedAccents.getNormalizedAccents());

        weight = weight + weightNonMatchWithMatchingNeighour(normalizedAccents.getNormalizedReferenceAccents(), normalizedAccents.getNormalizedAccents());

        weight = weight + weightMatchNoNeighbours(normalizedAccents.getNormalizedReferenceAccents(), normalizedAccents.getNormalizedAccents());

        weight = weight + weightMatchNonMatchingNeighour(normalizedAccents.getNormalizedReferenceAccents(), normalizedAccents.getNormalizedAccents());

        weight = weight + weightMatchWithMatchingNeighour(normalizedAccents.getNormalizedReferenceAccents(), normalizedAccents.getNormalizedAccents());

        if(weight < 0){
            return WeightClass.BAD;
        }else if (weight == 0){
            return WeightClass.OK;
        }else{
            return  WeightClass.GOOD;
        }
    }

    @Override
    public NormalizedAccents normalizeAccentLengths(IRhythmicLoop referenceLoop, IRhythmicLoop loop) {

        NormalizedAccents normalizedAccents = new NormalizedAccents();

        int refAccentsLength = referenceLoop.getRhythmicAccents().size();

        RhythmicAccents ra1 = referenceLoop.getRhythmicAccents().get(0);

        int accentsLength = loop.getRhythmicAccents().size();

        RhythmicAccents ra2 = loop.getRhythmicAccents().get(0);

        if(refAccentsLength == accentsLength){
            normalizedAccents.setReferenceAccentList(referenceLoop.getRhythmicAccents());
            normalizedAccents.setAccentsList(loop.getRhythmicAccents());
        }else if(refAccentsLength > accentsLength){
            normalizedAccents.setReferenceAccentList(referenceLoop.getRhythmicAccents());

            int loopsRequired = 0;
            loopsRequired = (int)(refAccentsLength / accentsLength);

            List<RhythmicAccents> newAccents = new ArrayList<>();

            for(Integer i=0; i < loopsRequired; i++) {

                loop.getRhythmicAccents().forEach(accent -> {
                    newAccents.add(accent);
                });
            }

            normalizedAccents.setAccentsList(newAccents);
        }else if(accentsLength > refAccentsLength){
//            if(accentsLength % refAccentsLength != 0){
//                //TODO throw exception ..
//                //NOT DIVISIBLE THROUGH EXCEPTION ..
//            }

            normalizedAccents.setAccentsList(loop.getRhythmicAccents());

            int loopsRequired = 0;
            loopsRequired = accentsLength / refAccentsLength;

            List<RhythmicAccents> newAccents = new ArrayList<>();

            for(Integer i=0; i < loopsRequired; i++) {
                referenceLoop.getRhythmicAccents().forEach(accent -> {
                    newAccents.add(accent);
                });
            }

            normalizedAccents.setReferenceAccentList(newAccents);
        }

        return normalizedAccents;
    }
}
