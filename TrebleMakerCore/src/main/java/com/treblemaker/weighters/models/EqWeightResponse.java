package com.treblemaker.weighters.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EqWeightResponse {

    List<Ratings> eqbands = new ArrayList<>();

    public EqWeightResponse(){}

    public List<Ratings> getEqbands() {
        return eqbands;
    }

    public void setEqbands(List<Ratings> eqbands) {
        this.eqbands = eqbands;
    }
}



