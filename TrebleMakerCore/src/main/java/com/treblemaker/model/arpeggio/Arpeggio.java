package com.treblemaker.model.arpeggio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.treblemaker.Application;
import com.treblemaker.weighters.enums.WeightClass;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;

@Entity
@Table(name = "generated_arpeggios")
public class Arpeggio implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "arpeggio_per_bar")
    private int arpeggioPerBar;

    @Column(name = "chordtype_one")
    private int chordTypeOne;

    @Column(name = "chordtype_two")
    private int chordTypeTwo;

    @Column(name = "arpeggio_json")
    private String arpeggioJson;

    @Transient
    private int totalWeight = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArpeggioPerBar() {
        return arpeggioPerBar;
    }

    public void setArpeggioPerBar(int arpeggioPerBar) {
        this.arpeggioPerBar = arpeggioPerBar;
    }

    public int getChordTypeOne() {
        return chordTypeOne;
    }

    public void setChordTypeOne(int chordTypeOne) {
        this.chordTypeOne = chordTypeOne;
    }

    public int getChordTypeTwo() {
        return chordTypeTwo;
    }

    public void setChordTypeTwo(int chordTypeTwo) {
        this.chordTypeTwo = chordTypeTwo;
    }

    public ArpeggioJson getArpeggioJson() {

        ObjectMapper mapper = new ObjectMapper();
        ArpeggioJson arpeggioJson = null;
        try {
            arpeggioJson = mapper.readValue(this.arpeggioJson, ArpeggioJson.class);
        } catch (IOException e) {
            Application.logger.debug("LOG:", e);
            throw new RuntimeException("Arrpeggio String not found");
        }

        return arpeggioJson;
    }

    public void setArpeggioJson(String arpeggioJson) {
        this.arpeggioJson = arpeggioJson;
    }

    public void incrementWeight(WeightClass weightClass){
        totalWeight = totalWeight + weightClass.getValue();
    }

    public int getTotalWeight(){
        return this.totalWeight;
    }
}
