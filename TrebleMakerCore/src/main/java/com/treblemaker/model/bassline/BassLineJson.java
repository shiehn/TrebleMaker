package com.treblemaker.model.bassline;

import java.io.Serializable;

public class BassLineJson implements Serializable {

    private String oneAInterval;
    private String oneADuration;
    private String oneBInterval;
    private String oneBDuration;
    private String oneCInterval;
    private String oneCDuration;
    private String oneDInterval;
    private String oneDDuration;

    private String twoAInterval;
    private String twoADuration;
    private String twoBInterval;
    private String twoBDuration;
    private String twoCInterval;
    private String twoCDuration;
    private String twoDInterval;
    private String twoDDuration;

    private String threeAInterval;
    private String threeADuration;
    private String threeBInterval;
    private String threeBDuration;
    private String threeCInterval;
    private String threeCDuration;
    private String threeDInterval;
    private String threeDDuration;

    private String fourAInterval;
    private String fourADuration;
    private String fourBInterval;
    private String fourBDuration;
    private String fourCInterval;
    private String fourCDuration;
    private String fourDInterval;
    private String fourDDuration;

    private String fiveAInterval;
    private String fiveADuration;
    private String fiveBInterval;
    private String fiveBDuration;
    private String fiveCInterval;
    private String fiveCDuration;
    private String fiveDInterval;
    private String fiveDDuration;

    private String sixAInterval;
    private String sixADuration;
    private String sixBInterval;
    private String sixBDuration;
    private String sixCInterval;
    private String sixCDuration;
    private String sixDInterval;
    private String sixDDuration;

    private String sevenAInterval;
    private String sevenADuration;
    private String sevenBInterval;
    private String sevenBDuration;
    private String sevenCInterval;
    private String sevenCDuration;
    private String sevenDInterval;
    private String sevenDDuration;

    private String eightAInterval;
    private String eightADuration;
    private String eightBInterval;
    private String eightBDuration;
    private String eightCInterval;
    private String eightCDuration;
    private String eightDInterval;
    private String eightDDuration;
     
    public void setIntervalByPosition(int position, String interval){

        switch (position){

            case 0:
                oneAInterval = interval;
                break;
            case 1:
                oneBInterval = interval;
                break;
            case 2:
                oneCInterval = interval;
                break;
            case 3:
                oneDInterval = interval;
                break;
            case 4:
                twoAInterval = interval;
                break;
            case 5:
                twoBInterval = interval;
                break;
            case 6:
                twoCInterval = interval;
                break;
            case 7:
                twoDInterval = interval;
                break;
            case 8:
                threeAInterval = interval;
                break;
            case 9:
                threeBInterval = interval;
                break;
            case 10:
                threeCInterval = interval;
                break;
            case 11:
                threeDInterval = interval;
                break; 
            case 12:
                fourAInterval = interval;
                break;
            case 13:
                fourBInterval = interval;
                break;
            case 14:
                fourCInterval = interval;
                break;
            case 15:
                fourDInterval = interval;
                break; 
            case 16:
                fiveAInterval = interval;
                break;
            case 17:
                fiveBInterval = interval;
                break;
            case 18:
                fiveCInterval = interval;
                break;
            case 19:
                fiveDInterval = interval;
                break;
            case 20:
                sixAInterval = interval;
                break;
            case 21:
                sixBInterval = interval;
                break;
            case 22:
                sixCInterval = interval;
                break;
            case 23:
                sixDInterval = interval;
                break;
            case 24:
                sevenAInterval = interval;
                break;
            case 25:
                sevenBInterval = interval;
                break;
            case 26:
                sevenCInterval = interval;
                break;
            case 27:
                sevenDInterval = interval;
                break;
            case 28:
                eightAInterval = interval;
                break;
            case 29:
                eightBInterval = interval;
                break;
            case 30:
                eightCInterval = interval;
                break;
            case 31:
                eightDInterval = interval;
                break;
        } 
    }

    public void setDurationByPosition(int position, String duration){

        switch (position){

            case 0:
                oneADuration = duration;
                break;
            case 1:
                oneBDuration = duration;
                break;
            case 2:
                oneCDuration = duration;
                break;
            case 3:
                oneDDuration = duration;
                break;
            case 4:
                twoADuration = duration;
                break;
            case 5:
                twoBDuration = duration;
                break;
            case 6:
                twoCDuration = duration;
                break;
            case 7:
                twoDDuration = duration;
                break;
            case 8:
                threeADuration = duration;
                break;
            case 9:
                threeBDuration = duration;
                break;
            case 10:
                threeCDuration = duration;
                break;
            case 11:
                threeDDuration = duration;
                break;
            case 12:
                fourADuration = duration;
                break;
            case 13:
                fourBDuration = duration;
                break;
            case 14:
                fourCDuration = duration;
                break;
            case 15:
                fourDDuration = duration;
                break;
            case 16:
                fiveADuration = duration;
                break;
            case 17:
                fiveBDuration = duration;
                break;
            case 18:
                fiveCDuration = duration;
                break;
            case 19:
                fiveDDuration = duration;
                break;
            case 20:
                sixADuration = duration;
                break;
            case 21:
                sixBDuration = duration;
                break;
            case 22:
                sixCDuration = duration;
                break;
            case 23:
                sixDDuration = duration;
                break;
            case 24:
                sevenADuration = duration;
                break;
            case 25:
                sevenBDuration = duration;
                break;
            case 26:
                sevenCDuration = duration;
                break;
            case 27:
                sevenDDuration = duration;
                break;
            case 28:
                eightADuration = duration;
                break;
            case 29:
                eightBDuration = duration;
                break;
            case 30:
                eightCDuration = duration;
                break;
            case 31:
                eightDDuration = duration;
                break;
        }
    }

    public String getIntervalByPosition(int position){

        switch (position){

            case 0:
                return oneAInterval;
            case 1:
                return oneBInterval;
            case 2:
                return oneCInterval;
            case 3:
                return oneDInterval;
            case 4:
                return twoAInterval;
            case 5:
                return twoBInterval;
            case 6:
                return twoCInterval;
            case 7:
                return twoDInterval;
            case 8:
                return threeAInterval;
            case 9:
                return threeBInterval;
            case 10:
                return threeCInterval;
            case 11:
                return threeDInterval;
            case 12:
                return fourAInterval;
            case 13:
                return fourBInterval;
            case 14:
                return fourCInterval;
            case 15:
                return fourDInterval;
            case 16:
                return fiveAInterval;
            case 17:
                return fiveBInterval;
            case 18:
                return fiveCInterval;
            case 19:
                return fiveDInterval;
            case 20:
                return sixAInterval;
            case 21:
               return  sixBInterval;
            case 22:
                return sixCInterval;
            case 23:
                return sixDInterval;
            case 24:
                return sevenAInterval;
            case 25:
                return sevenBInterval;
            case 26:
                return sevenCInterval;
            case 27:
                return sevenDInterval;
            case 28:
                return eightAInterval;
            case 29:
                return eightBInterval;
            case 30:
                return eightCInterval;
            case 31:
                return eightDInterval;
        }

        throw new RuntimeException("out of bounds");
    }

    public String getDurationByPosition(int position){

        switch (position){
            case 0:
                return oneADuration;
            case 1:
                return oneBDuration;
            case 2:
                return oneCDuration;
            case 3:
                return oneDDuration;
            case 4:
                return twoADuration;
            case 5:
                return twoBDuration;
            case 6:
                return twoCDuration;
            case 7:
                return twoDDuration;
            case 8:
                return threeADuration;
            case 9:
                return threeBDuration;
            case 10:
                return threeCDuration;
            case 11:
                return threeDDuration;
            case 12:
               return  fourADuration;
            case 13:
               return  fourBDuration;
            case 14:
                return fourCDuration;
            case 15:
                return fourDDuration;
            case 16:
                return fiveADuration;
            case 17:
                return fiveBDuration;
            case 18:
               return  fiveCDuration;
            case 19:
               return  fiveDDuration;
            case 20:
               return  sixADuration;
            case 21:
               return  sixBDuration;
            case 22:
               return  sixCDuration;
            case 23:
               return  sixDDuration;
            case 24:
               return  sevenADuration;
            case 25:
               return  sevenBDuration;
            case 26:
               return  sevenCDuration;
            case 27:
                return sevenDDuration;
            case 28:
               return  eightADuration;
            case 29:
               return  eightBDuration;
            case 30:
               return  eightCDuration;
            case 31:
               return  eightDDuration;
        }

        throw new RuntimeException("out of bounds");
    }


    public String print(){

        String output = oneAInterval + "-" + oneADuration + "-" + oneBInterval + "-" + oneBDuration + "-" + oneCInterval + "-" + oneCDuration + "-" + oneDInterval + "-" + oneDDuration + "-" + twoAInterval + "-" + twoADuration + "-" + twoBInterval + "-" + twoBDuration + "-" + twoCInterval + "-" + twoCDuration + "-" + twoDInterval + "-" + twoDDuration + "-" + threeAInterval + "-" + threeADuration + "-" + threeBInterval + "-" + threeBDuration + "-" + threeCInterval + "-" + threeCDuration + "-" + threeDInterval + "-" + threeDDuration + "-" + fourAInterval + "-" + fourADuration + "-" + fourBInterval + "-" + fourBDuration + "-" + fourCInterval + "-" + fourCDuration + "-" + fourDInterval + "-" + fourDDuration + "-" + fiveAInterval + "-" + fiveADuration + "-" + fiveBInterval + "-" + fiveBDuration + "-" + fiveCInterval + "-" + fiveCDuration + "-" + fiveDInterval + "-" + fiveDDuration + "-" + sixAInterval + "-" + sixADuration + "-" + sixBInterval + "-" + sixBDuration + "-" + sixCInterval + "-" + sixCDuration + "-" + sixDInterval + "-" + sixDDuration + "-" + sevenAInterval + "-" + sevenADuration + "-" + sevenBInterval + "-" + sevenBDuration + "-" + sevenCInterval + "-" + sevenCDuration + "-" + sevenDInterval + "-" + sevenDDuration + "-" + eightAInterval + "-" + eightADuration + "-" + eightBInterval + "-" + eightBDuration + "-" + eightCInterval + "-" + eightCDuration + "-" + eightDInterval + "-" + eightDDuration;

        return output;
    }

    public String getOneAInterval() {
        return oneAInterval;
    }

    public void setOneAInterval(String oneAInterval) {
        this.oneAInterval = oneAInterval;
    }

    public String getOneADuration() {
        return oneADuration;
    }

    public void setOneADuration(String oneADuration) {
        this.oneADuration = oneADuration;
    }

    public String getOneBInterval() {
        return oneBInterval;
    }

    public void setOneBInterval(String oneBInterval) {
        this.oneBInterval = oneBInterval;
    }

    public String getOneBDuration() {
        return oneBDuration;
    }

    public void setOneBDuration(String oneBDuration) {
        this.oneBDuration = oneBDuration;
    }

    public String getOneCInterval() {
        return oneCInterval;
    }

    public void setOneCInterval(String oneCInterval) {
        this.oneCInterval = oneCInterval;
    }

    public String getOneCDuration() {
        return oneCDuration;
    }

    public void setOneCDuration(String oneCDuration) {
        this.oneCDuration = oneCDuration;
    }

    public String getOneDInterval() {
        return oneDInterval;
    }

    public void setOneDInterval(String oneDInterval) {
        this.oneDInterval = oneDInterval;
    }

    public String getOneDDuration() {
        return oneDDuration;
    }

    public void setOneDDuration(String oneDDuration) {
        this.oneDDuration = oneDDuration;
    }

    public String getTwoAInterval() {
        return twoAInterval;
    }

    public void setTwoAInterval(String twoAInterval) {
        this.twoAInterval = twoAInterval;
    }

    public String getTwoADuration() {
        return twoADuration;
    }

    public void setTwoADuration(String twoADuration) {
        this.twoADuration = twoADuration;
    }

    public String getTwoBInterval() {
        return twoBInterval;
    }

    public void setTwoBInterval(String twoBInterval) {
        this.twoBInterval = twoBInterval;
    }

    public String getTwoBDuration() {
        return twoBDuration;
    }

    public void setTwoBDuration(String twoBDuration) {
        this.twoBDuration = twoBDuration;
    }

    public String getTwoCInterval() {
        return twoCInterval;
    }

    public void setTwoCInterval(String twoCInterval) {
        this.twoCInterval = twoCInterval;
    }

    public String getTwoCDuration() {
        return twoCDuration;
    }

    public void setTwoCDuration(String twoCDuration) {
        this.twoCDuration = twoCDuration;
    }

    public String getTwoDInterval() {
        return twoDInterval;
    }

    public void setTwoDInterval(String twoDInterval) {
        this.twoDInterval = twoDInterval;
    }

    public String getTwoDDuration() {
        return twoDDuration;
    }

    public void setTwoDDuration(String twoDDuration) {
        this.twoDDuration = twoDDuration;
    }

    public String getThreeAInterval() {
        return threeAInterval;
    }

    public void setThreeAInterval(String threeAInterval) {
        this.threeAInterval = threeAInterval;
    }

    public String getThreeADuration() {
        return threeADuration;
    }

    public void setThreeADuration(String threeADuration) {
        this.threeADuration = threeADuration;
    }

    public String getThreeBInterval() {
        return threeBInterval;
    }

    public void setThreeBInterval(String threeBInterval) {
        this.threeBInterval = threeBInterval;
    }

    public String getThreeBDuration() {
        return threeBDuration;
    }

    public void setThreeBDuration(String threeBDuration) {
        this.threeBDuration = threeBDuration;
    }

    public String getThreeCInterval() {
        return threeCInterval;
    }

    public void setThreeCInterval(String threeCInterval) {
        this.threeCInterval = threeCInterval;
    }

    public String getThreeCDuration() {
        return threeCDuration;
    }

    public void setThreeCDuration(String threeCDuration) {
        this.threeCDuration = threeCDuration;
    }

    public String getThreeDInterval() {
        return threeDInterval;
    }

    public void setThreeDInterval(String threeDInterval) {
        this.threeDInterval = threeDInterval;
    }

    public String getThreeDDuration() {
        return threeDDuration;
    }

    public void setThreeDDuration(String threeDDuration) {
        this.threeDDuration = threeDDuration;
    }

    public String getFourAInterval() {
        return fourAInterval;
    }

    public void setFourAInterval(String fourAInterval) {
        this.fourAInterval = fourAInterval;
    }

    public String getFourADuration() {
        return fourADuration;
    }

    public void setFourADuration(String fourADuration) {
        this.fourADuration = fourADuration;
    }

    public String getFourBInterval() {
        return fourBInterval;
    }

    public void setFourBInterval(String fourBInterval) {
        this.fourBInterval = fourBInterval;
    }

    public String getFourBDuration() {
        return fourBDuration;
    }

    public void setFourBDuration(String fourBDuration) {
        this.fourBDuration = fourBDuration;
    }

    public String getFourCInterval() {
        return fourCInterval;
    }

    public void setFourCInterval(String fourCInterval) {
        this.fourCInterval = fourCInterval;
    }

    public String getFourCDuration() {
        return fourCDuration;
    }

    public void setFourCDuration(String fourCDuration) {
        this.fourCDuration = fourCDuration;
    }

    public String getFourDInterval() {
        return fourDInterval;
    }

    public void setFourDInterval(String fourDInterval) {
        this.fourDInterval = fourDInterval;
    }

    public String getFourDDuration() {
        return fourDDuration;
    }

    public void setFourDDuration(String fourDDuration) {
        this.fourDDuration = fourDDuration;
    }

    public String getFiveAInterval() {
        return fiveAInterval;
    }

    public void setFiveAInterval(String fiveAInterval) {
        this.fiveAInterval = fiveAInterval;
    }

    public String getFiveADuration() {
        return fiveADuration;
    }

    public void setFiveADuration(String fiveADuration) {
        this.fiveADuration = fiveADuration;
    }

    public String getFiveBInterval() {
        return fiveBInterval;
    }

    public void setFiveBInterval(String fiveBInterval) {
        this.fiveBInterval = fiveBInterval;
    }

    public String getFiveBDuration() {
        return fiveBDuration;
    }

    public void setFiveBDuration(String fiveBDuration) {
        this.fiveBDuration = fiveBDuration;
    }

    public String getFiveCInterval() {
        return fiveCInterval;
    }

    public void setFiveCInterval(String fiveCInterval) {
        this.fiveCInterval = fiveCInterval;
    }

    public String getFiveCDuration() {
        return fiveCDuration;
    }

    public void setFiveCDuration(String fiveCDuration) {
        this.fiveCDuration = fiveCDuration;
    }

    public String getFiveDInterval() {
        return fiveDInterval;
    }

    public void setFiveDInterval(String fiveDInterval) {
        this.fiveDInterval = fiveDInterval;
    }

    public String getFiveDDuration() {
        return fiveDDuration;
    }

    public void setFiveDDuration(String fiveDDuration) {
        this.fiveDDuration = fiveDDuration;
    }

    public String getSixAInterval() {
        return sixAInterval;
    }

    public void setSixAInterval(String sixAInterval) {
        this.sixAInterval = sixAInterval;
    }

    public String getSixADuration() {
        return sixADuration;
    }

    public void setSixADuration(String sixADuration) {
        this.sixADuration = sixADuration;
    }

    public String getSixBInterval() {
        return sixBInterval;
    }

    public void setSixBInterval(String sixBInterval) {
        this.sixBInterval = sixBInterval;
    }

    public String getSixBDuration() {
        return sixBDuration;
    }

    public void setSixBDuration(String sixBDuration) {
        this.sixBDuration = sixBDuration;
    }

    public String getSixCInterval() {
        return sixCInterval;
    }

    public void setSixCInterval(String sixCInterval) {
        this.sixCInterval = sixCInterval;
    }

    public String getSixCDuration() {
        return sixCDuration;
    }

    public void setSixCDuration(String sixCDuration) {
        this.sixCDuration = sixCDuration;
    }

    public String getSixDInterval() {
        return sixDInterval;
    }

    public void setSixDInterval(String sixDInterval) {
        this.sixDInterval = sixDInterval;
    }

    public String getSixDDuration() {
        return sixDDuration;
    }

    public void setSixDDuration(String sixDDuration) {
        this.sixDDuration = sixDDuration;
    }

    public String getSevenAInterval() {
        return sevenAInterval;
    }

    public void setSevenAInterval(String sevenAInterval) {
        this.sevenAInterval = sevenAInterval;
    }

    public String getSevenADuration() {
        return sevenADuration;
    }

    public void setSevenADuration(String sevenADuration) {
        this.sevenADuration = sevenADuration;
    }

    public String getSevenBInterval() {
        return sevenBInterval;
    }

    public void setSevenBInterval(String sevenBInterval) {
        this.sevenBInterval = sevenBInterval;
    }

    public String getSevenBDuration() {
        return sevenBDuration;
    }

    public void setSevenBDuration(String sevenBDuration) {
        this.sevenBDuration = sevenBDuration;
    }

    public String getSevenCInterval() {
        return sevenCInterval;
    }

    public void setSevenCInterval(String sevenCInterval) {
        this.sevenCInterval = sevenCInterval;
    }

    public String getSevenCDuration() {
        return sevenCDuration;
    }

    public void setSevenCDuration(String sevenCDuration) {
        this.sevenCDuration = sevenCDuration;
    }

    public String getSevenDInterval() {
        return sevenDInterval;
    }

    public void setSevenDInterval(String sevenDInterval) {
        this.sevenDInterval = sevenDInterval;
    }

    public String getSevenDDuration() {
        return sevenDDuration;
    }

    public void setSevenDDuration(String sevenDDuration) {
        this.sevenDDuration = sevenDDuration;
    }

    public String getEightAInterval() {
        return eightAInterval;
    }

    public void setEightAInterval(String eightAInterval) {
        this.eightAInterval = eightAInterval;
    }

    public String getEightADuration() {
        return eightADuration;
    }

    public void setEightADuration(String eightADuration) {
        this.eightADuration = eightADuration;
    }

    public String getEightBInterval() {
        return eightBInterval;
    }

    public void setEightBInterval(String eightBInterval) {
        this.eightBInterval = eightBInterval;
    }

    public String getEightBDuration() {
        return eightBDuration;
    }

    public void setEightBDuration(String eightBDuration) {
        this.eightBDuration = eightBDuration;
    }

    public String getEightCInterval() {
        return eightCInterval;
    }

    public void setEightCInterval(String eightCInterval) {
        this.eightCInterval = eightCInterval;
    }

    public String getEightCDuration() {
        return eightCDuration;
    }

    public void setEightCDuration(String eightCDuration) {
        this.eightCDuration = eightCDuration;
    }

    public String getEightDInterval() {
        return eightDInterval;
    }

    public void setEightDInterval(String eightDInterval) {
        this.eightDInterval = eightDInterval;
    }

    public String getEightDDuration() {
        return eightDDuration;
    }

    public void setEightDDuration(String eightDDuration) {
        this.eightDDuration = eightDDuration;
    }
}
