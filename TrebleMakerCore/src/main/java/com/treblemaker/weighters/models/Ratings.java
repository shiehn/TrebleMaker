package com.treblemaker.weighters.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ratings {

    public static final String BAD = "0";
    public static final String OK = "1";
    public static final String GOOD = "2";

    String rating;
    String freq;

    public Ratings(){}

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }
}