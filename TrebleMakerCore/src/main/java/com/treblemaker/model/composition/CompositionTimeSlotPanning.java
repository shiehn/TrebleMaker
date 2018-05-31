package com.treblemaker.model.composition;

import javax.persistence.*;

@Entity
@Table(name = "composition_time_slot_panning")
public class CompositionTimeSlotPanning implements Cloneable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "composition_time_slot_id")
    private Integer compositionTimeSlotId;

    @Column(name = "composition_id")
    private Integer compositionId;

    @Column(name = "comp_melody")
    private String compMelody;

    @Column(name = "comp_hi")
    private String compHi;

    @Column(name = "comp_hi_alt")
    private String compHiAlt;

    @Column(name = "comp_mid")
    private String compMid;

    @Column(name = "comp_mid_alt")
    private String compMidAlt;

    @Column(name = "comp_low")
    private String compLow;

    @Column(name = "comp_low_alt")
    private String compLowAlt;

    @Column(name = "beat")
    private String beat;

    @Column(name = "beat_alt")
    private String beatAlt;

    @Column(name = "harmonic")
    private String harmonic;

    @Column(name = "harmonic_alt")
    private String harmonicAlt;

    @Column(name = "ambient")
    private String ambient;

    @Column(name = "fills")
    private String fills;

    @Column(name = "hits")
    private String hits;

    @Column(name = "kick")
    private String kick;

    @Column(name = "hat")
    private String hat;

    @Column(name = "snare")
    private String snare;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompositionTimeSlotId() {
        return compositionTimeSlotId;
    }

    public void setCompositionTimeSlotId(Integer compositionTimeSlotId) {
        this.compositionTimeSlotId = compositionTimeSlotId;
    }

    public Integer getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(Integer compositionId) {
        this.compositionId = compositionId;
    }

    public String getCompMelody() {
        return compMelody;
    }

    public void setCompMelody(String compMelody) {
        this.compMelody = compMelody;
    }

    public String getCompHi() {
        return compHi;
    }

    public void setCompHi(String compHi) {
        this.compHi = compHi;
    }

    public String getCompHiAlt() {
        return compHiAlt;
    }

    public void setCompHiAlt(String compHiAlt) {
        this.compHiAlt = compHiAlt;
    }

    public String getCompMid() {
        return compMid;
    }

    public void setCompMid(String compMid) {
        this.compMid = compMid;
    }

    public String getCompMidAlt() {
        return compMidAlt;
    }

    public void setCompMidAlt(String compMidAlt) {
        this.compMidAlt = compMidAlt;
    }

    public String getCompLow() {
        return compLow;
    }

    public void setCompLow(String compLow) {
        this.compLow = compLow;
    }

    public String getCompLowAlt() {
        return compLowAlt;
    }

    public void setCompLowAlt(String compLowAlt) {
        this.compLowAlt = compLowAlt;
    }

    public String getBeat() {
        return beat;
    }

    public void setBeat(String beat) {
        this.beat = beat;
    }

    public String getBeatAlt() {
        return beatAlt;
    }

    public void setBeatAlt(String beatAlt) {
        this.beatAlt = beatAlt;
    }

    public String getHarmonic() {
        return harmonic;
    }

    public void setHarmonic(String hamonic) {
        this.harmonic = hamonic;
    }

    public String getHarmonicAlt() {
        return harmonicAlt;
    }

    public void setHarmonicAlt(String hamonicAlt) {
        this.harmonicAlt = hamonicAlt;
    }

    public String getAmbient() {
        return ambient;
    }

    public void setAmbient(String ambient) {
        this.ambient = ambient;
    }

    public String getFills() {
        return fills;
    }

    public void setFills(String fills) {
        this.fills = fills;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public String getKick() {
        return kick;
    }

    public void setKick(String kick) {
        this.kick = kick;
    }

    public String getHat() {
        return hat;
    }

    public void setHat(String hat) {
        this.hat = hat;
    }

    public String getSnare() {
        return snare;
    }

    public void setSnare(String snare) {
        this.snare = snare;
    }

    @Override
    public CompositionTimeSlotPanning clone() throws CloneNotSupportedException {
        return (CompositionTimeSlotPanning) super.clone();
    }
}
