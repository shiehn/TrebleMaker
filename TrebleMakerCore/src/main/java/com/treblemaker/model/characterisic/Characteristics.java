package com.treblemaker.model.characterisic;

import com.treblemaker.model.interfaces.IInfluenceable;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "characteristics")
public class Characteristics implements IInfluenceable, Serializable {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "name")
    private String name;

    public Characteristics() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
