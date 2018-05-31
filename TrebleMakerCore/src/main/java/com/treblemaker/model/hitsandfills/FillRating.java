package com.treblemaker.model.hitsandfills;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "fills_ratings")
public class FillRating implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "fill_id")
    private Integer fillId;

    @Column(name = "composition_id")
    private Integer compositionId;

    @Column(name = "rating")
    private Integer rating;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFillId() {
        return fillId;
    }

    public void setFillId(Integer fillId) {
        this.fillId = fillId;
    }

    public Integer getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(Integer compositionId) {
        this.compositionId = compositionId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}