package com.treblemaker.model;

import com.treblemaker.model.interfaces.IInfluenceable;

import javax.persistence.*;

@Entity
@Table(name = "instruments")
public class Instruments implements IInfluenceable {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "name")
    private String name;

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
}