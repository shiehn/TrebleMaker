package com.treblemaker.model;

import com.treblemaker.model.interfaces.IWeightable;
import com.treblemaker.weighters.TotalWeight;
import com.treblemaker.weighters.enums.WeightClass;
import com.treblemaker.configs.AppConfigs;

import javax.persistence.*;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ambience_loops")
public class AmbienceLoop implements IWeightable, Serializable {

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

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "date")
    private Date date;

    @Column(name = "users_id")
    private long usersId;

    @Column(name = "normalized_length")
    private int normalizedLength;

    @Column(name = "loop_transfer_complete")
    private boolean loopTransferComplete;

    @Column(name = "station_id")
    private int stationId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "ambience_loop_chords",
            joinColumns = @JoinColumn(name = "ambience_loop_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "chord_id", referencedColumnName = "id"))
    private List<HiveChord> chords = new ArrayList<HiveChord>();

    @Transient
    private WeightClass verticalWeight;

    @Transient
    private WeightClass eqWeight;

    @Transient
    private WeightClass timeseriesWeight;

    @Transient
    private WeightClass themeWeight;

    @Transient
    private WeightClass rhythmicWeight;

    @Transient
    private float shimLength;

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

    @Override
    public WeightClass getVerticalWeight() {
        return this.verticalWeight;
    }

    @Override
    public void setVerticalWeight(WeightClass weight) {
        this.verticalWeight = weight;
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
    public void setRhythmicWeight(WeightClass weight) {
        this.rhythmicWeight = weight;
    }

    @Override
    public void setEqWeight(WeightClass weight) {
        this.eqWeight = weight;
    }

    @Override
    public WeightClass getEqWeight() {
        return eqWeight;
    }

    @Override
    public WeightClass getRhythmicWeight() {
        return this.rhythmicWeight;
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

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Transient
    public String getFilePath(AppConfigs appConfigs) {
        return Paths.get(appConfigs.getApplicationRoot(),"Loops", Integer.toString(getStationId()),"AmbienceLoops").toString();
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public float getShimLength() {
        if (this.shimLength < 0.0f) {
            return 0;
        }

        return shimLength;
    }

    public void setShimLength(float shimLength) {
        this.shimLength = shimLength;
    }

    public List<HiveChord> getChords() {
        return chords;
    }

    public void setChords(List<HiveChord> chords) {
        this.chords = chords;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }
}
