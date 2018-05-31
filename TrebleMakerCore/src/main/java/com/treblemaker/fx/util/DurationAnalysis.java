package com.treblemaker.fx.util;

import org.springframework.stereotype.Component;

@Component
public class DurationAnalysis {

    public double extractSixteenthFrequency(int[] arpeggioDurations){

        int totalSixteenths = 32;
        int count = 0;

        for(int i=0; i<arpeggioDurations.length; i++){

            if(i==31 && arpeggioDurations[i] != 0){
                count++;
            }else if(arpeggioDurations[i] != 0 && arpeggioDurations[i+1] != 0){
                count++;
            }
        }

        double percentSixteenths = (double)count/(double)totalSixteenths;

        double output = Math.round(percentSixteenths * 10.0) / 10.0; // => 1.23

        if(output > 1.0){
            output = 1.0;
        }

        return output;
    }

    public double extractEightFrequency(int[] arpeggioDurations){

        int noteLength = 2;

        double totalEighths = (double)arpeggioDurations.length/(double)noteLength;
        int count = 0;

        for(int i=0; i<arpeggioDurations.length; i++) {

            if(i == arpeggioDurations.length - noteLength && arpeggioDurations[i] != 0
                    && arpeggioDurations[i+1] == 0) {
                count++;
            }else if(i < arpeggioDurations.length - noteLength && arpeggioDurations[i] != 0
                    && arpeggioDurations[i+1] == 0
                    && arpeggioDurations[i+noteLength] != 0){
                count++;
            }
        }

        double percentSixteenths = (double)count/(double)totalEighths;

        double output = Math.round(percentSixteenths * 10.0) / 10.0; // => 1.23

        if(output > 1.0){
            output = 1.0;
        }

        return output;
    }

    public double extractQuarterFrequency(int[] arpeggioDurations){
        int noteLength = 4;

        double totalEighths = (double)arpeggioDurations.length/(double)noteLength;
        int count = 0;

        for(int i=0; i<arpeggioDurations.length; i++) {

            if(i == arpeggioDurations.length - noteLength && arpeggioDurations[i] != 0
                    && arpeggioDurations[i+1] == 0
                    && arpeggioDurations[i+2] == 0
                    && arpeggioDurations[i+3] == 0) {
                count++;
            }else if(i < arpeggioDurations.length - noteLength && arpeggioDurations[i] != 0
                    && arpeggioDurations[i+1] == 0
                    && arpeggioDurations[i+2] == 0
                    && arpeggioDurations[i+3] == 0
                    && arpeggioDurations[i+noteLength] != 0){
                count++;
            }
        }

        double percentSixteenths = (double)count/(double)totalEighths;

        double output = Math.round(percentSixteenths * 10.0) / 10.0; // => 1.23

        if(output > 1.0){
            output = 1.0;
        }

        return output;
    }

    public double extractDottedQuarterFrequency(int[] arpeggioDurations){
        int noteLength = 6;

        double totalEighths = (double)arpeggioDurations.length/(double)noteLength;
        int count = 0;

        for(int i=0; i<arpeggioDurations.length; i++) {

            if(i == arpeggioDurations.length - noteLength && arpeggioDurations[i] != 0
                    && arpeggioDurations[i+1] == 0
                    && arpeggioDurations[i+2] == 0
                    && arpeggioDurations[i+3] == 0
                    && arpeggioDurations[i+4] == 0
                    && arpeggioDurations[i+5] == 0) {
                count++;
            }else if(i < arpeggioDurations.length - noteLength && arpeggioDurations[i] != 0
                    && arpeggioDurations[i+1] == 0
                    && arpeggioDurations[i+2] == 0
                    && arpeggioDurations[i+3] == 0
                    && arpeggioDurations[i+4] == 0
                    && arpeggioDurations[i+5] == 0
                    && arpeggioDurations[i+noteLength] != 0){
                count++;
            }
        }

        double percentSixteenths = (double)count/(double)totalEighths;

        double output = Math.round(percentSixteenths * 10.0) / 10.0; // => 1.23

        if(output > 1.0){
            output = 1.0;
        }

        return output;
    }

    public double extractHalfFrequency(int[] arpeggioDurations){

        int noteLength = 8;

        double totalEighths = (double)arpeggioDurations.length/(double)noteLength;
        int count = 0;

        for(int i=0; i<arpeggioDurations.length; i++) {

            if(i == arpeggioDurations.length - noteLength && arpeggioDurations[i] != 0 && arpeggioDurations[i+1] == 0
                    && arpeggioDurations[i+2] == 0
                    && arpeggioDurations[i+3] == 0
                    && arpeggioDurations[i+4] == 0
                    && arpeggioDurations[i+5] == 0
                    && arpeggioDurations[i+6] == 0
                    && arpeggioDurations[i+7] == 0) {
                count++;
            }else if(i < arpeggioDurations.length - noteLength && arpeggioDurations[i] != 0 &&
                    arpeggioDurations[i+1] == 0 &&
                    arpeggioDurations[i+2] == 0 &&
                    arpeggioDurations[i+3] == 0 &&
                    arpeggioDurations[i+4] == 0 &&
                    arpeggioDurations[i+5] == 0 &&
                    arpeggioDurations[i+6] == 0 &&
                    arpeggioDurations[i+7] == 0 &&
                    arpeggioDurations[i+noteLength] != 0){
                count++;
            }
        }

        double percentSixteenths = (double)count/(double)totalEighths;

        double output = Math.round(percentSixteenths * 10.0) / 10.0; // => 1.23

        if(output > 1.0){
            output = 1.0;
        }

        return output;
    }
}
