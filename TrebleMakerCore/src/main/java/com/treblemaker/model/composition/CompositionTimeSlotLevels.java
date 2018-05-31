package com.treblemaker.model.composition;

import javax.persistence.*;

@Entity
@Table(name = "composition_time_slot_levels")
public class CompositionTimeSlotLevels {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "composition_id")
    private Integer compositionId;

    @Column(name = "comp_hi")
    private Double compHi;

    @Column(name = "comp_hi_alt")
    private Double compHiAlt;

    @Column(name = "comp_mid")
    private Double compMid;

    @Column(name = "comp_mid_alt")
    private Double compMidAlt;

    @Column(name = "comp_low")
    private Double compLow;

    @Column(name = "comp_low_alt")
    private Double compLowAlt;

    @Column(name = "beat")
    private Double beat;

    @Column(name = "beat_alt")
    private Double beatAlt;

    @Column(name = "harmonic")
    private Double harmonic;

    @Column(name = "harmonic_alt")
    private Double harmonicAlt;

    @Column(name = "ambient")
    private Double ambient;

    @Column(name = "fills")
    private Double fills;

    @Column(name = "hits")
    private Double hits;

    @Column(name = "level_before_mixed")
    private Double levelBeforeMixed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(Integer compositionId) {
        this.compositionId = compositionId;
    }

    public Double getCompHi() {
        return compHi;
    }

    public void setCompHi(Double compHi) {
        this.compHi = compHi;
    }

    public Double getCompHiAlt() {
        return compHiAlt;
    }

    public void setCompHiAlt(Double compHiAlt) {
        this.compHiAlt = compHiAlt;
    }

    public Double getCompMid() {
        return compMid;
    }

    public void setCompMid(Double compMid) {
        this.compMid = compMid;
    }

    public Double getCompMidAlt() {
        return compMidAlt;
    }

    public void setCompMidAlt(Double compMidAlt) {
        this.compMidAlt = compMidAlt;
    }

    public Double getCompLow() {
        return compLow;
    }

    public void setCompLow(Double compLow) {
        this.compLow = compLow;
    }

    public Double getCompLowAlt() {
        return compLowAlt;
    }

    public void setCompLowAlt(Double compLowAlt) {
        this.compLowAlt = compLowAlt;
    }

    public Double getBeat() {
        return beat;
    }

    public void setBeat(Double beat) {
        this.beat = beat;
    }

    public Double getBeatAlt() {
        return beatAlt;
    }

    public void setBeatAlt(Double beatAlt) {
        this.beatAlt = beatAlt;
    }

    public Double getHarmonic() {
        return harmonic;
    }

    public void setHarmonic(Double harmonic) {
        this.harmonic = harmonic;
    }

    public Double getHarmonicAlt() {
        return harmonicAlt;
    }

    public void setHarmonicAlt(Double harmonicAlt) {
        this.harmonicAlt = harmonicAlt;
    }

    public Double getAmbient() {
        return ambient;
    }

    public void setAmbient(Double ambient) {
        this.ambient = ambient;
    }

    public Double getFills() {
        return fills;
    }

    public void setFills(Double fills) {
        this.fills = fills;
    }

    public Double getHits() {
        return hits;
    }

    public void setHits(Double hits) {
        this.hits = hits;
    }

    public Double getLevelBeforeMixed() {
        return levelBeforeMixed;
    }

    public void setLevelBeforeMixed(Double levelBeforeMixed) {
        this.levelBeforeMixed = levelBeforeMixed;
    }
}
