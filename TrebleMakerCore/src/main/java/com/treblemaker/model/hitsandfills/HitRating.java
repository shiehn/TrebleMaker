package com.treblemaker.model.hitsandfills;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "hits_ratings")
public class HitRating implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "hit_id")
    private Integer hitId;

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

    public Integer getHitId() {
        return hitId;
    }

    public void setHitId(Integer hitId) {
        this.hitId = hitId;
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