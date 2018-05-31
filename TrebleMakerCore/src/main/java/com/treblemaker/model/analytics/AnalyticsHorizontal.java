package com.treblemaker.model.analytics;

import com.treblemaker.model.composition.CompositionTimeSlot;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "analytics_horizontal")
public class AnalyticsHorizontal implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "rating")
    private int rating;

    @OneToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "horizontal_timeslots",
            joinColumns = @JoinColumn(name = "analytics_horizontal_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "time_slot_id", referencedColumnName = "id"))
    //@OrderColumn(name="msec_start")
    @Column(name = "composition_time_slots")
    private List<CompositionTimeSlot> composition_time_slots;

    @Column(name = "category_harmonic")
    private boolean category_harmonic;

    @Column(name = "category_rhythmic")
    private boolean category_rhythmic;

    @Column(name = "category_ambience")
    private boolean category_ambience;

    @Column(name = "category_fill")
    private boolean category_fill;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<CompositionTimeSlot> getComposition_time_slots() {
        return composition_time_slots;
    }

    public void setComposition_time_slots(List<CompositionTimeSlot> composition_time_slots) {
        this.composition_time_slots = composition_time_slots;
    }

    public boolean isCategory_harmonic() {
        return category_harmonic;
    }

    public void setCategory_harmonic(boolean category_harmonic) {
        this.category_harmonic = category_harmonic;
    }

    public boolean isCategory_rhythmic() {
        return category_rhythmic;
    }

    public void setCategory_rhythmic(boolean category_rhythmic) {
        this.category_rhythmic = category_rhythmic;
    }

    public boolean isCategory_ambience() {
        return category_ambience;
    }

    public void setCategory_ambience(boolean category_ambience) {
        this.category_ambience = category_ambience;
    }

    public boolean isCategory_fill() {
        return category_fill;
    }

    public void setCategory_fill(boolean category_fill) {
        this.category_fill = category_fill;
    }
}
