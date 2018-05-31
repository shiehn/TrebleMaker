package com.treblemaker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.treblemaker.model.interfaces.IWeightableLoop;
import com.treblemaker.weighters.TotalWeight;
import com.treblemaker.weighters.enums.WeightClass;
import com.treblemaker.configs.AppConfigs;

import javax.persistence.*;
import java.io.Serializable;
import java.nio.file.FileSystem;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Cacheable(true)
@Table(name = "beat_loops")
public class BeatLoop implements IWeightableLoop, Cloneable, Serializable {

    public static final int NOT_YET_NORMALIZED = 0;
    public static final int ALREADY_NORMALIZED = 1;

    @Override
    public BeatLoop clone() throws CloneNotSupportedException {
        return (BeatLoop) super.clone();
    }

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "complexity")
    private int complexity;

    @Column(name = "mood_dark")
    private int moodDark;

    @Column(name = "mood_light")
    private int moodLight;

    @Column(name = "audio_length")
    private float audioLength;

    @Column(name = "bar_count")
    private Integer barCount = 0;

    @Column(name = "bpm")
    private int bpm;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "date")
    private String date;

    @Column(name = "users_id")
    private long usersId;

    @Column(name = "normalized_length")
    private int normalizedLength;

    @Column(name = "loop_transfer_complete")
    private boolean loopTransferComplete;

    @Column(name = "station_id")
    private int stationId;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name = "beat_loop_rhythmic_accents",
            joinColumns = @JoinColumn(name = "beat_loop_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "rhythmic_accents_id", referencedColumnName = "id"))
    @OrderColumn(name = "bar_order")
    private List<RhythmicAccents> rhythmicAccents = new ArrayList<>();

    @Transient
    private WeightClass verticalWeight;

    @Transient
    private WeightClass timeseriesWeight;

    @Transient
    private WeightClass themeWeight;

    @Transient
    private WeightClass rhythmicWeight;

    @Transient
    private WeightClass eqWeight;

    @Transient
    private boolean isShim;

    @Transient
    private int currentBar;

    @Override
    public boolean isShim() {
        return isShim;
    }

    @Override
    public void setIsShim(boolean isShim) {
        this.isShim = isShim;
    }

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

    public Integer getBarCount() {
        return barCount;
    }

    public void setBarCount(Integer barCount) {
        this.barCount = barCount;
    }

    public int getCurrentBar() {
        return currentBar;
    }

    public void setCurrentBar(int currentBar) {
        this.currentBar = currentBar;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath(AppConfigs appConfigs) {
        return Paths.get(appConfigs.getApplicationRoot(),"Loops", Integer.toString(getStationId()), "BeatLoops").toString();
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getUsersId() {
        return usersId;
    }

    public void setUsersId(long usersId) {
        this.usersId = usersId;
    }

    public int getNormalizedLength() {
        return normalizedLength;
    }

    public void setNormalizedLength(int normalizedLength) {
        this.normalizedLength = normalizedLength;
    }

    public boolean isLoopTransferComplete() {
        return loopTransferComplete;
    }

    public void setLoopTransferComplete(boolean loopTransferComplete) {
        this.loopTransferComplete = loopTransferComplete;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public List<RhythmicAccents> getRhythmicAccents() {

        //TODO WHY THE FUCK IS JPA GIVING ME NULLS!!!
        List<RhythmicAccents> accents = new ArrayList<>();
        rhythmicAccents.forEach(rAccent -> {
            if(rAccent != null) {
                accents.add(rAccent);
            }
        });

        return accents;
    }

    public void setRhythmicAccents(List<RhythmicAccents> rhythmicAccents) {
        this.rhythmicAccents = rhythmicAccents;
    }

    @Override
    public WeightClass getTimeseriesWeight() {
        return timeseriesWeight;
    }

    @Override
    public void setTimeseriesWeight(WeightClass weight) {
        timeseriesWeight = weight;
    }

    @Override
    public WeightClass getThemeWeight() {
        return themeWeight;
    }

    @Override
    public void setThemeWeight(WeightClass weight) {
        themeWeight = weight;
    }

    @Override
    public WeightClass getVerticalWeight() {
        return verticalWeight;
    }

    @Override
    public void setVerticalWeight(WeightClass weight) {
        verticalWeight = weight;
    }


    @Override
    public void setRhythmicWeight(WeightClass weight) {
        rhythmicWeight = weight;
    }

    @Override
    public WeightClass getRhythmicWeight() {
        return rhythmicWeight;
    }

    @Override
    public WeightClass getEqWeight() {
        return eqWeight;
    }

    @Override
    public void setEqWeight(WeightClass weight) {
        eqWeight = weight;
    }

    @Override
    public Integer getTotalWeight() {

        return TotalWeight.calculateTotal(
                verticalWeight,
                timeseriesWeight,
                themeWeight,
                rhythmicWeight,
                eqWeight);
    }
}
