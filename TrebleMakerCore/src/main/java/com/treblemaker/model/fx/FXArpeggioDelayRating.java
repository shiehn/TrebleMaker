package com.treblemaker.model.fx;

import javax.persistence.*;

@Entity
@Table(name = "fx_arpeggio_delay_rating")
public class FXArpeggioDelayRating {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "arpeggio_id")
    private int arpeggioId;

    @Column(name = "rating")
    private int rating;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArpeggioId() {
        return arpeggioId;
    }

    public void setArpeggioId(int arpeggioId) {
        this.arpeggioId = arpeggioId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
