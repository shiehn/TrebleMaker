package com.treblemaker.model.analytics;

import javax.persistence.*;

@Entity
@Table(name = "analytics_vertical")
public class AnalyticsVertical {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "time_slot_id")
    private int timeSlotId;

    @Column(name = "rating")
    private int rating;

    @Column(name = "eq_rating")
    private int eqRating;

    @Column(name = "levels_rating")
    private Integer levelsRating;

    @Column(name = "panning_rating")
    private Integer panningRating;

    @Column(name = "arpeggio_rating")
    private int arpeggioRating;

    @Column(name = "comp_rating")
    private Integer compRating;

    @Column(name = "bassline_rating")
    private int basslineRating;

    @Column(name = "kick_rating")
    private Integer kickRating;

    @Column(name = "hat_rating")
    private Integer hatRating;

    @Column(name = "snare_rating")
    private Integer snareRating;

    @Column(name = "rated")
    private int rated = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(int timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getEqRating() {
        return eqRating;
    }

    public void setEqRating(int eqRating) {
        this.eqRating = eqRating;
    }

    public Integer getLevelsRating() {
        return levelsRating;
    }

    public void setLevelsRating(Integer levelsRating) {
        this.levelsRating = levelsRating;
    }

    public Integer getPanningRating() {
        return panningRating;
    }

    public void setPanningRating(Integer panningRating) {
        this.panningRating = panningRating;
    }

    public int getArpeggioRating() {
        return arpeggioRating;
    }

    public void setArpeggioRating(int arpeggioRating) {
        this.arpeggioRating = arpeggioRating;
    }

    public Integer getCompRating() {
        return compRating;
    }

    public void setCompRating(Integer compRating) {
        this.compRating = compRating;
    }

    public int getBasslineRating() {
        return basslineRating;
    }

    public void setBasslineRating(int basslineRating) {
        this.basslineRating = basslineRating;
    }

    public Integer getKickRating() {
        return kickRating;
    }

    public void setKickRating(Integer kickRating) {
        this.kickRating = kickRating;
    }

    public Integer getHatRating() {
        return hatRating;
    }

    public void setHatRating(Integer hatRating) {
        this.hatRating = hatRating;
    }

    public Integer getSnareRating() {
        return snareRating;
    }

    public void setSnareRating(Integer snareRating) {
        this.snareRating = snareRating;
    }

    public int getRated() {
        return rated;
    }

    public void setRated(int rated) {
        this.rated = rated;
    }
}
