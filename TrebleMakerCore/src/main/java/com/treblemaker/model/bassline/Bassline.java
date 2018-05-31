package com.treblemaker.model.bassline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.treblemaker.Application;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;

@Entity
@Table(name = "generated_basslines")
public class Bassline implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "arpeggio_per_bar")
    private int arpeggioPerBar;

    @Column(name = "chordtype_one")
    private int chordTypeOne;

    @Column(name = "chordtype_two")
    private int chordTypeTwo;

    @Column(name = "bassline_json")
    private String basslineString;

    @Transient
    private BassLineJson bassline;

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

    public String getBasslineString() {
        return basslineString;
    }

    public void setBasslineString(String basslineString) {
        this.basslineString = basslineString;
    }

    public BassLineJson getBassline() {

        if(this.bassline != null){
            return this.bassline;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            this.bassline = mapper.readValue(basslineString, BassLineJson.class);
        } catch (IOException e) {
            Application.logger.debug("LOG: EXCEPTION ", e);
        }

        return bassline;
    }

    public void setBassline(BassLineJson bassline) {
        this.bassline = bassline;
    }
}
