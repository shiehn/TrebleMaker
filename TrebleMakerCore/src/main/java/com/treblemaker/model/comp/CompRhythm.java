package com.treblemaker.model.comp;

import com.treblemaker.weighters.enums.WeightClass;

import javax.persistence.*;

@Entity
@Table(name = "comp_rhythms")
public class CompRhythm {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "comp")
    private String comp;

    @Transient
    private WeightClass weight;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public WeightClass getWeight() {
        return weight;
    }

    public void setWeight(WeightClass weight) {
        this.weight = weight;
    }
}
