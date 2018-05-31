package com.treblemaker.machinelearning.helper;


import com.treblemaker.weighters.enums.WeightClass;

public class SequenceClassiferUtils {

    private final static int BAD = 0;
    private final static int OK = 1;
    private final static int GOOD = 2;

    public static int incrementBigramRating(int subjectRating, int ratingToIncrement){

        //RATE BIGRAMS  ..
        switch (subjectRating){
            case BAD:
                ratingToIncrement = ratingToIncrement -2;
                break;
            case OK:
                ratingToIncrement = ratingToIncrement +1;
                break;
            case GOOD:
                ratingToIncrement = ratingToIncrement +2;
                break;
        }

        return ratingToIncrement;
    }

    public static int incrementUnigramRating(int subjectRating, int ratingToIncrement){

        //RATE UNIGRAMS ...
        switch (subjectRating){
            case BAD:
                ratingToIncrement = ratingToIncrement -1;
                break;
            case OK:
                ratingToIncrement = ratingToIncrement +0;
                break;
            case GOOD:
                ratingToIncrement = ratingToIncrement +1;
                break;
        }

        return ratingToIncrement;
    }

    public static WeightClass ratingToWeightClass(int rating){

        if(rating < 0){
            return WeightClass.BAD;
        } else if (rating == 0){
            return WeightClass.OK;
        } else if (rating > 0){
            return WeightClass.GOOD;
        }

        throw new RuntimeException("could not classify harmonic alt");
    }
}
