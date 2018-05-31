package com.treblemaker.model.fx;

import com.treblemaker.weighters.TotalWeight;
import com.treblemaker.weighters.enums.WeightClass;

import javax.persistence.*;

@Entity
@Table(name = "fx_arpeggio_delay")
public class FXArpeggioDelay {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "delay_type")
    private double delayType;

    @Column(name = "delay_volume")
    private double delayVolume;

    @Column(name = "master_volume")
    private double masterVolume;

    @Transient
    private WeightClass harmonicWeight;

    @Transient
    private WeightClass genreWeight;

    @Transient
    private WeightClass characteristicWeight;

    @Transient
    private WeightClass instrumentWeight;

    @Transient
    private WeightClass rhythmicWeight;

    @Transient
    private WeightClass eqWeight;

    @Transient
    private WeightClass verticalWeight;

    @Transient
    private WeightClass sequenceWeight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDelayType() {
        return delayType;
    }

    public void setDelayType(double delayType) {
        this.delayType = delayType;
    }

    public double getDelayVolume() {
        return delayVolume;
    }

    public void setDelayVolume(double delayVolume) {
        this.delayVolume = delayVolume;
    }

    public double getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(double masterVolume) {
        this.masterVolume = masterVolume;
    }

    public WeightClass getHarmonicWeight() {
        return harmonicWeight;
    }

    public void setHarmonicWeight(WeightClass harmonicWeight) {
        this.harmonicWeight = harmonicWeight;
    }

    public WeightClass getGenreWeight() {
        return genreWeight;
    }

    public void setGenreWeight(WeightClass genreWeight) {
        this.genreWeight = genreWeight;
    }

    public WeightClass getCharacteristicWeight() {
        return characteristicWeight;
    }

    public void setCharacteristicWeight(WeightClass characteristicWeight) {
        this.characteristicWeight = characteristicWeight;
    }

    public WeightClass getInstrumentWeight() {
        return instrumentWeight;
    }

    public void setInstrumentWeight(WeightClass instrumentWeight) {
        this.instrumentWeight = instrumentWeight;
    }

    public WeightClass getRhythmicWeight() {
        return rhythmicWeight;
    }

    public void setRhythmicWeight(WeightClass rhythmicWeight) {
        this.rhythmicWeight = rhythmicWeight;
    }

    public WeightClass getEqWeight() {
        return eqWeight;
    }

    public void setEqWeight(WeightClass eqWeight) {
        this.eqWeight = eqWeight;
    }

    public WeightClass getVerticalWeight() {
        return verticalWeight;
    }

    public void setVerticalWeight(WeightClass verticalWeight) {
        this.verticalWeight = verticalWeight;
    }

    public WeightClass getSequenceWeight() {
        return sequenceWeight;
    }

    public void setSequenceWeight(WeightClass sequenceWeight) {
        this.sequenceWeight = sequenceWeight;
    }

    public Integer getTotalWeight() {

        return TotalWeight.calculateTotal(
                harmonicWeight,
                genreWeight,
                characteristicWeight,
                instrumentWeight,
                verticalWeight,
                sequenceWeight,
                rhythmicWeight,
                eqWeight);
    }
}