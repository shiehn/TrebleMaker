package com.treblemaker.model;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "chord_categories")
public class ChordCategory implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "category")
    private String category;

    public ChordCategory() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
