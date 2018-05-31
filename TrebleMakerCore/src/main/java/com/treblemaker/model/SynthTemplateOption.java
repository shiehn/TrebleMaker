package com.treblemaker.model;

import com.treblemaker.weighters.enums.WeightClass;

public class SynthTemplateOption implements Cloneable {

    public SynthTemplateOption(int id, String name, SynthRoll synthRoll){
        this.id = id;
        this.name = name;
        this.synthRoll = synthRoll;
    }

    public enum SynthRoll {
        SYNTH_HI,
        SYNTH_MID,
        SYNTH_LOW,
    }

    private int id;
    private String name;
    private SynthRoll synthRoll;
    private WeightClass weightClass;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SynthRoll getSynthRoll() {
        return synthRoll;
    }

    public void setSynthRoll(SynthRoll synthRoll) {
        this.synthRoll = synthRoll;
    }

    public WeightClass getWeightClass() {
        return weightClass;
    }

    public void setWeightClass(WeightClass weightClass) {
        this.weightClass = weightClass;
    }

    @Override
    public SynthTemplateOption clone() throws CloneNotSupportedException {
        return (SynthTemplateOption) super.clone();
    }
}
