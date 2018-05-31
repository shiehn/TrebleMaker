package com.treblemaker.weighters;

import com.treblemaker.weighters.enums.WeightClass;
import com.treblemaker.weighters.models.EqWeightResponse;
import com.treblemaker.weighters.models.Ratings;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class WeightClassificationUtils {

    private static List<String> lowEqRange = Arrays.asList("FREQ_20", "FREQ_25", "FREQ_31", "FREQ_40", "FREQ_50", "FREQ_63", "FREQ_80", "FREQ_100", "FREQ_125", "FREQ_160", "FREQ_200");
    private static List<String> midEqRange = Arrays.asList("FREQ_250", "FREQ_315", "FREQ_400", "FREQ_500", "FREQ_630", "FREQ_800", "FREQ_1000", "FREQ_1250", "FREQ_1600", "FREQ_2000");
    private static List<String> hiEqRange = Arrays.asList("FREQ_2500", "FREQ_3150", "FREQ_4000", "FREQ_5000", "FREQ_6300", "FREQ_8000", "FREQ_10000", "FREQ_12500", "FREQ_16000", "FREQ_20000");

    public static WeightClass verticalWeightToClass(String verticalClass) {

        if (verticalClass.contains("2")) {
            return WeightClass.GOOD;
        } else if (verticalClass.contains("1")) {
            return WeightClass.OK;
        } else if (verticalClass.contains("0")) {
            return WeightClass.BAD;
        }

        throw new RuntimeException("CANNOT CAST verticalClass string to ENUM");
    }

    public static WeightClass eqResponseToWeightClass(EqWeightResponse eqWeightResponse) {

        int badCount = 0;
        int okCount = 0;
        int goodCount = 0;

        WeightClass hiClass = weightHiEqRegister(eqWeightResponse);
        WeightClass midClass = weightMidEqRegister(eqWeightResponse);
        WeightClass lowClass = weightLowEqRegister(eqWeightResponse);

        if (hiClass.equals(WeightClass.BAD)) {
            badCount++;
        }

        if (midClass.equals(WeightClass.BAD)) {
            badCount++;
        }

        if (lowClass.equals(WeightClass.BAD)) {
            badCount++;
        }

        // /////////////////

        if (hiClass.equals(WeightClass.OK)) {
            okCount++;
        }

        if (midClass.equals(WeightClass.OK)) {
            okCount++;
        }

        if (lowClass.equals(WeightClass.OK)) {
            okCount++;
        }

        // /////////////////

        if (hiClass.equals(WeightClass.GOOD)) {
            goodCount++;
        }

        if (midClass.equals(WeightClass.GOOD)) {
            goodCount++;
        }

        if (lowClass.equals(WeightClass.GOOD)) {
            goodCount++;
        }

        if (okCount >= goodCount && okCount >= badCount) {
            return WeightClass.OK;
        }

        if (badCount >= goodCount && badCount >= okCount) {
            return WeightClass.BAD;
        }

        return WeightClass.GOOD;
    }

    public static WeightClass weightLowEqRegister(EqWeightResponse eqWeightResponse) {

        int badCount = 0;
        int okCount = 0;
        int goodCount = 0;

        for (Ratings rating : eqWeightResponse.getEqbands()) {
            if (isLowEqRegister(rating)) {
                if (rating.getRating().toLowerCase().contains("0")) {
                    badCount++;
                }

                if (rating.getRating().toLowerCase().contains("1")) {
                    okCount++;
                }

                if (rating.getRating().toLowerCase().contains("2")) {
                    goodCount++;
                }
            }
        }

        if (badCount >= okCount && badCount >= goodCount) {
            return WeightClass.BAD;
        }

        if (okCount >= goodCount && okCount >= badCount) {
            return WeightClass.OK;
        }

        return WeightClass.GOOD;
    }

    public static WeightClass weightMidEqRegister(EqWeightResponse eqWeightResponse) {

        int badCount = 0;
        int okCount = 0;
        int goodCount = 0;

        for (Ratings rating : eqWeightResponse.getEqbands()) {
            if (isMidEqRegister(rating)) {
                if (rating.getRating().toLowerCase().contains("0")) {
                    badCount++;
                }

                if (rating.getRating() .toLowerCase().contains("1")) {
                    okCount++;
                }

                if (rating.getRating().toLowerCase().contains("2")) {
                    goodCount++;
                }
            }
        }

        if (badCount >= okCount && badCount >= goodCount) {
            return WeightClass.BAD;
        }

        if (okCount >= goodCount && okCount >= badCount) {
            return WeightClass.OK;
        }

        return WeightClass.GOOD;
    }

    public static WeightClass weightHiEqRegister(EqWeightResponse eqWeightResponse) {

        int badCount = 0;
        int okCount = 0;
        int goodCount = 0;

        for (Ratings rating : eqWeightResponse.getEqbands()) {
            if (isHiEqRegister(rating)) {
                if (rating.getRating().toLowerCase().contains("0")) {
                    badCount++;
                }

                if (rating.getRating().toLowerCase().contains("1")) {
                    okCount++;
                }

                if (rating.getRating().toLowerCase().contains("2")) {
                    goodCount++;
                }
            }
        }

        if (badCount >= okCount && badCount >= goodCount) {
            return WeightClass.BAD;
        }

        if (okCount >= goodCount && okCount >= badCount) {
            return WeightClass.OK;
        }

        return WeightClass.GOOD;
    }

    public static boolean isLowEqRegister(Ratings rating) {
        return lowEqRange.contains(rating.getFreq());
    }

    public static boolean isMidEqRegister(Ratings rating) {
        return midEqRange.contains(rating.getFreq());
    }

    public static boolean isHiEqRegister(Ratings rating) {
        return hiEqRange.contains(rating.getFreq());
    }


}
