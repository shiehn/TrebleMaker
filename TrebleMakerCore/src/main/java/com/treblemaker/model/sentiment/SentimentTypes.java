package com.treblemaker.model.sentiment;

import javax.persistence.*;

@Entity
@Table(name = "sentiment_types")
public class SentimentTypes {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "type")
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}