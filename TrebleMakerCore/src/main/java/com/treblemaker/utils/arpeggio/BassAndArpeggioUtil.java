package com.treblemaker.utils.arpeggio;

public class BassAndArpeggioUtil {

    public static double convertIntervalsToDouble(String duration){

        switch (duration){
            case "0":
                return 0.0;
            case "1":
                return 0.2;
            case "2":
                return 0.4;
            case "3":
                return 0.6;
            case "4":
                return 0.8;
            case "5":
                return 1.0;
        }

        throw new RuntimeException("ERROR COULD NOT MAP INTERVAL TO DOUBLE");
    }

    public static double convertDurationToInt(String duration){

        switch (duration){
            case "r":
                return 0.0;
            case "s":
                return 0.33;
            case "n":
                return 0.66;
            case "e":
                return 1.0;
        }

        throw new RuntimeException("ERROR COULD NOT MAP DURATION TO INT");
    }
}
