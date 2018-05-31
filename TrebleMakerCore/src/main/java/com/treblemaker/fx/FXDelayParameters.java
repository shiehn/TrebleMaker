package com.treblemaker.fx;

import java.util.Random;

public class FXDelayParameters {

    private static final int MIL_IN_SEC = 60000;

    public final static double[] VOLUME = {0.5, 0.6, 0.7, 0.8, 0.9, 1.0};

    public enum DelayType {
        TRIPLET_EIGHTH,
        TRIPLET_QUARTER,
        QUARTER,
        EIGHTH
    }

    public static DelayType getDelayTypeByNormalizedData(double normalizedDelay){

//        if(normalizedDelay ==  0.0) {
//            return DelayType.TRIPLET_EIGHTH;
//        }else if (normalizedDelay ==  0.3) {
//            return DelayType.TRIPLET_QUARTER;
//        } else if (normalizedDelay ==  0.7) {
//            return DelayType.EIGHTH;
//        } else if (normalizedDelay ==  1.0){
//            return DelayType.QUARTER;
//        }

        //TODO IN THE DATABASE THERE WAS FOUR TYPES .. REALLY THIS CHANGED TO JUST 1 or 0 now

        if(normalizedDelay ==  0.0) {
            return DelayType.EIGHTH;
        } else if (normalizedDelay ==  1.0){
            return DelayType.QUARTER;
        }

        throw new RuntimeException("getDelayTypeByNormalizedData UNEXPECTED VALUE");
    }

    public static double getNormalizedDataFromDelayType(DelayType delayType){

        switch (delayType){
            case TRIPLET_EIGHTH:
                return 0.0;
            case TRIPLET_QUARTER:
                return 0.3;
            case EIGHTH:
                return 0.7;
            case QUARTER:
                return 1.0;
        }

        throw new RuntimeException("getNormalizedDataFromDelayType UNEXPECTED VALUE");
    }

    public enum EchoVolume {
        LOW,
        MID,
        HI
    }

    public static EchoVolume getEchoVolumeFromNormalizedData(double normalizedData){

        if(normalizedData == 0.0){
            return EchoVolume.LOW;
        }else if(normalizedData == 0.5){
            return EchoVolume.MID;
        } else if(normalizedData == 1.0){
            return EchoVolume.HI;
        }

        throw new RuntimeException("getEchoVolumeFromNormalizedData UNEXPECTED VALUE");
    }

    public static double getNormalizedDataFromEchoVolume(EchoVolume echoVolume){

        switch (echoVolume){
            case LOW:
                return 0.0;
            case MID:
                return 0.5;
            case HI:
                return 1.0;
        }

        throw new RuntimeException("getNormalizedDataFromEchoVolume UNEXPECTED VALUE");
    }


    public static double getRandomVolume(){
        return VOLUME[new Random().nextInt(VOLUME.length)];
    }

    public static EchoVolume getRandomEchoVolume() {

        EchoVolume[] echoVolumes = new EchoVolume[]{
                EchoVolume.LOW,
                EchoVolume.MID,
                EchoVolume.HI
        };

        return echoVolumes[new Random().nextInt(echoVolumes.length)];
    }

    public static DelayType getRandomDelayType() {
        DelayType[] delayTypes = new DelayType[]{
                DelayType.TRIPLET_EIGHTH,
                DelayType.TRIPLET_QUARTER,
                DelayType.QUARTER,
                DelayType.EIGHTH
        };

        return delayTypes[new Random().nextInt(delayTypes.length)];
    }

    public final static double[] echosLow = {0.5, 0.3, 0.2, 0.1};
    public final static double[] echosMid = {0.7, 0.5, 0.3, 0.1};
    public final static double[] echosHigh = {0.9, 0.6, 0.4, 0.2};

    public final static double[] getEchoVolumes(EchoVolume echoVolume) {
        switch (echoVolume) {
            case LOW:
                return echosLow;
            case MID:
                return echosMid;
            case HI:
                return echosHigh;
        }

        throw new RuntimeException("Unknown EchoVolume");
    }

    public final static int[] getEchoIntervals(DelayType delayType, int bpm) {

        switch (delayType) {

            case TRIPLET_EIGHTH:
                return new int[]{
                        MIL_IN_SEC / bpm / 3,
                        MIL_IN_SEC / bpm / 3 * 2,
                        MIL_IN_SEC / bpm / 3 * 3,
                        MIL_IN_SEC / bpm / 3 * 4};

            case TRIPLET_QUARTER:
                return new int[]{
                        MIL_IN_SEC / bpm * 2 / 3,
                        MIL_IN_SEC / bpm * 2 / 3 * 2,
                        MIL_IN_SEC / bpm * 2 / 3 * 3,
                        MIL_IN_SEC / bpm * 2 / 3 * 4};

            case EIGHTH:
                return new int[]{
                        MIL_IN_SEC / bpm / 2,
                        MIL_IN_SEC / bpm / 2 * 2,
                        MIL_IN_SEC / bpm / 2 * 3,
                        MIL_IN_SEC / bpm / 2 * 4};

            case QUARTER:
                return new int[]{
                        MIL_IN_SEC / bpm,
                        MIL_IN_SEC / bpm * 2,
                        MIL_IN_SEC / bpm * 3,
                        MIL_IN_SEC / bpm * 4};
        }

        return null;
    }
}
