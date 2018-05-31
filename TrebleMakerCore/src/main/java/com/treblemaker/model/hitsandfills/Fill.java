package com.treblemaker.model.hitsandfills;

import com.treblemaker.model.HiveChord;
import com.treblemaker.model.interfaces.IWeightable;
import com.treblemaker.weighters.TotalWeight;
import com.treblemaker.weighters.enums.WeightClass;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "fills")
public class Fill implements Serializable, Cloneable, IWeightable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "complexity")
    private Integer complexity;

    @Column(name = "mood_dark")
    private Integer moodDark;

    @Column(name = "mood_light")
    private Integer moodLight;

    @Column(name = "audio_length")
    private Float audioLength;

    @Column(name = "bpm")
    private Integer bpm;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "date")
    private String date;

    @Column(name = "normalized_length")
    private Integer normalizedLength;

    @Column(name = "bar_count")
    private Integer barCount;

    @Column(name = "loop_transfer_complete")
    private Integer loopTransferComplete;

    @Column(name = "station_id")
    private int stationId;

    @ManyToOne()
    @JoinColumn(name="chord_id")
    private HiveChord chordId;

    @Transient
    private WeightClass verticalWeight;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComplexity() {
        return complexity;
    }

    public void setComplexity(Integer complexity) {
        this.complexity = complexity;
    }

    public Integer getMoodDark() {
        return moodDark;
    }

    public void setMoodDark(Integer moodDark) {
        this.moodDark = moodDark;
    }

    public Integer getMoodLight() {
        return moodLight;
    }

    public void setMoodLight(Integer moodLight) {
        this.moodLight = moodLight;
    }

    public Float getAudioLength() {
        return audioLength;
    }

    public void setAudioLength(Float audioLength) {
        this.audioLength = audioLength;
    }

    public Integer getBpm() {
        return bpm;
    }

    public void setBpm(Integer bpm) {
        this.bpm = bpm;
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

    public Integer getNormalizedLength() {
        return normalizedLength;
    }

    public void setNormalizedLength(Integer normalizedLength) {
        this.normalizedLength = normalizedLength;
    }

    public Integer getBarCount() {
        return barCount;
    }

    public void setBarCount(Integer barCount) {
        this.barCount = barCount;
    }

    public Integer getLoopTransferComplete() {
        return loopTransferComplete;
    }

    public void setLoopTransferComplete(Integer loopTransferComplete) {
        this.loopTransferComplete = loopTransferComplete;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public HiveChord getCompatibleChord(){
        return chordId;
    }

    public void setCompatibleChord(HiveChord hiveChord){
        this.chordId = hiveChord;
    }

    @Override
    public Hit clone() throws CloneNotSupportedException {
        return (Hit) super.clone();
    }

    @Override
    public WeightClass getVerticalWeight() {
        return verticalWeight;
    }

    @Override
    public void setVerticalWeight(WeightClass weight) {
        this.verticalWeight = weight;
    }

    @Override
    public WeightClass getTimeseriesWeight() {
        return null;
    }

    @Override
    public void setTimeseriesWeight(WeightClass weight) {

    }

    @Override
    public WeightClass getThemeWeight() {
        return null;
    }

    @Override
    public void setThemeWeight(WeightClass weight) {

    }

    @Override
    public void setRhythmicWeight(WeightClass weight) {

    }

    @Override
    public WeightClass getRhythmicWeight() {
        return null;
    }

    @Override
    public WeightClass getEqWeight() {
        return null;
    }

    @Override
    public void setEqWeight(WeightClass weight) {

    }

    @Override
    public Integer getTotalWeight() {
        return TotalWeight.calculateTotal(
                verticalWeight);
    }

    public HiveChord getChordId() {
        return chordId;
    }

    public void setChordId(HiveChord chordId) {
        this.chordId = chordId;
    }
}
