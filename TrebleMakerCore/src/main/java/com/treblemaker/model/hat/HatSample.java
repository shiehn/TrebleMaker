package com.treblemaker.model.hat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "hat_samples")
public class HatSample implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "complexity")
    private int complexity = 0;

    @Column(name = "mood_dark")
    private int moodDark = 0;

    @Column(name = "mood_light")
    private int moodLight = 0;

    @Column(name = "audio_length")
    private float audioLength = 0;

    @Column(name = "file_name")
    private String fileName = "";

    @Column(name = "date")
    private String date = "";

    @Column(name = "station_id")
    private String stationId = "";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getComplexity() {
        return complexity;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
    }

    public int getMoodDark() {
        return moodDark;
    }

    public void setMoodDark(int moodDark) {
        this.moodDark = moodDark;
    }

    public int getMoodLight() {
        return moodLight;
    }

    public void setMoodLight(int moodLight) {
        this.moodLight = moodLight;
    }

    public float getAudioLength() {
        return audioLength;
    }

    public void setAudioLength(float audioLength) {
        this.audioLength = audioLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
}
