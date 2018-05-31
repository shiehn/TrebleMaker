package com.treblemaker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.treblemaker.model.interfaces.IWeightableLoop;
import com.treblemaker.weighters.TotalWeight;
import com.treblemaker.weighters.enums.WeightClass;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import com.treblemaker.configs.AppConfigs;

import javax.persistence.*;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@EnableAutoConfiguration
@Entity
@Cacheable(true)
@Table(name = "harmonic_loops")
public class HarmonicLoop extends ChordBase implements IWeightableLoop, Cloneable, Serializable {

    @Override
    public HarmonicLoop clone() throws CloneNotSupportedException {
        return (HarmonicLoop)super.clone();
    }

    public static final int NOT_YET_NORMALIZED = 0;
    public static final int ALREADY_NORMALIZED = 1;

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

    @Column(name = "bar_count")
    private int barCount;

    @Column(name = "associated_id")
    private String associatedId;

    @Column(name = "loop_transfer_complete")
    private boolean loopTransferComplete;

    @Column(name = "station_id")
    private int stationId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "harmonic_loop_chords",
            joinColumns = @JoinColumn(name = "harmonic_loop_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "chord_id", referencedColumnName = "id"))
    private List<HiveChord> chords = new ArrayList<HiveChord>();

    @OneToMany(fetch=FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name = "harmonic_loop_rhythmic_accents",
            joinColumns = @JoinColumn(name = "harmonic_loop_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "rhythmic_accent_id", referencedColumnName = "id"))
    @OrderColumn(name="bar_order")
    private List<RhythmicAccents> rhythmicAccents = new ArrayList<>();

    @OneToMany(fetch=FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name = "harmonic_loop_pitch_extractions",
            joinColumns = @JoinColumn(name = "harmonic_loop_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "pitch_extraction_id", referencedColumnName = "id"))
    @OrderColumn(name="bar_order")
    private List<PitchExtractions> pitchExtractions = new ArrayList<>();

    @Transient
    private WeightClass harmonicWeight;

    @Transient
    private WeightClass rhythmicWeight;

    @Transient
    private WeightClass eqWeight;

    @Transient
    private WeightClass verticalWeight;

    @Transient
    private WeightClass timeSeriesWeight;

    @Transient
    private WeightClass themeWeight;

    // If a 2 par loop is added to each beat in a bar
    // This is the position in the loop (not the bar)
    // So at the 3rd beat in a bar the current position of a 2 bar loop would be 1 and beat 4 would be 2
    @Transient
    private Integer currentBar = 0;

    @Transient
    private boolean shim;

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

    @Override
    public Integer getBarCount() {
        return this.barCount;
    }

    @Override
    public void setBarCount(Integer barCount) {
        this.barCount = barCount;
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

    @Transient
    public String getFilePath(AppConfigs appConfigs) {
        return Paths.get(appConfigs.getApplicationRoot(),"Loops", Integer.toString(getStationId()),"HarmonicLoops").toString();
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

    public String getAssociatedId() {
        return associatedId;
    }

    public void setAssociatedId(String associatedId) {
        this.associatedId = associatedId;
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

    public List<HiveChord> getChords() {
        return chords;
    }

    public void setChords(List<HiveChord> chords) {
        this.chords = chords;
    }

    public List<HiveChord> getValidChords() {

        List<HiveChord> compatibleChords = new ArrayList<>();

        if (this.getChords().size() > 0) {
            HiveChord chordOne = this.getChords().get(0);
            if (chordOne != null && !chordOne.getChordName().equalsIgnoreCase("") && !chordOne.getChordName().equalsIgnoreCase("error")) {
                compatibleChords.add(chordOne);
            }else{
                new RuntimeException("INVALID CHORD!!");
            }
        }

        if (this.getChords().size() > 1) {
            HiveChord chordTwo = this.getChords().get(1);
            if (chordTwo != null && !chordTwo.getChordName().equalsIgnoreCase("") && !chordTwo.getChordName().equalsIgnoreCase("error")) {
                compatibleChords.add(chordTwo);
            }else{
                new RuntimeException("INVALID CHORD!!");
            }
        }

        if (this.getChords().size() > 2) {
            HiveChord chordThree = this.getChords().get(2);
            if (chordThree != null && !chordThree.getChordName().equalsIgnoreCase("") && !chordThree.getChordName().equalsIgnoreCase("error")) {
                compatibleChords.add(chordThree);
            }else{
                new RuntimeException("INVALID CHORD!!");
            }
        }

        if (this.getChords().size() > 3) {
            HiveChord chordFour = this.getChords().get(3);
            if (chordFour != null && !chordFour.getChordName().equalsIgnoreCase("") && !chordFour.getChordName().equalsIgnoreCase("error")) {
                compatibleChords.add(chordFour);
            }else{
                new RuntimeException("INVALID CHORD!!");
            }
        }

        return compatibleChords;
    }

    public HiveChord getRootOne() {

        List<HiveChord> validChords = getValidChords();

        if (validChords.size() > 0) {
            return validChords.get(0);
        }

        return null;
    }

    public HiveChord getRootTwo() {

        List<HiveChord> validChords = getValidChords();

        if (validChords.size() > 1) {
            return validChords.get(1);
        }

        return null;
    }

    public HiveChord getRootThree() {

        List<HiveChord> validChords = getValidChords();

        if (validChords.size() > 2) {
            return validChords.get(2);
        }

        return null;
    }

    public HiveChord getRootFour() {

        List<HiveChord> validChords = getValidChords();

        if (validChords.size() > 3) {
            return validChords.get(3);
        }

        return null;
    }

    public List<RhythmicAccents> getRhythmicAccents() {

        List<RhythmicAccents> accents = new ArrayList<>();
        rhythmicAccents.forEach(rAccent -> {
            if(rAccent != null){
                accents.add(rAccent);
            }
        });

        return accents;
    }

    public void setRhythmicAccents(List<RhythmicAccents> rhythmicAccents) {
        this.rhythmicAccents = rhythmicAccents;
    }

    public List<PitchExtractions> getPitchExtractions() {

        if(pitchExtractions == null){
            return new ArrayList<>();
        }

        List<PitchExtractions> pExtractions = new ArrayList<>();
        pitchExtractions.forEach(pitchExtraction -> {
            if(pitchExtraction != null){
                pExtractions.add(pitchExtraction);
            }
        });

        return pExtractions;
    }

    public void setPitchExtractions(List<PitchExtractions> pitchExtractions) {
        this.pitchExtractions = pitchExtractions;
    }

    @Override
    public boolean isShim() {
        return shim;
    }

    @Override
    public void setIsShim(boolean isShim) {
        this.shim = shim;
    }

    public void setShim(boolean shim) {
        this.shim = shim;
    }

    // If a 2 par loop is added to each beat in a bar
    // This is the position in the loop (not the bar)
    // So at the 3rd beat in a bar the current position of a 2 bar loop would be 1 and beat 4 would be 2
    public int getCurrentBar() {
        return currentBar;
    }


    // If a 2 par loop is added to each beat in a bar
    // This is the position in the loop (not the bar)
    // So at the 3rd beat in a bar the current position of a 2 bar loop would be 1 and beat 4 would be 2
    public void setCurrentBar(int currentBar) {
        this.currentBar = currentBar;
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
    public WeightClass getTimeseriesWeight() {
        return this.timeSeriesWeight;
    }

    @Override
    public void setTimeseriesWeight(WeightClass weight) {
        this.timeSeriesWeight = weight;
    }

    @Override
    public WeightClass getThemeWeight() {
        return this.themeWeight;
    }

    @Override
    public void setThemeWeight(WeightClass weight) {
        this.themeWeight = weight;
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

    public WeightClass getHarmonicWeight() {
        return harmonicWeight;
    }

    public void setHarmonicWeight(WeightClass harmonicWeight) {
        this.harmonicWeight = harmonicWeight;
    }

    @Override
    public Integer getTotalWeight() {

        return TotalWeight.calculateTotal(
                eqWeight,
                rhythmicWeight,
                verticalWeight,
                timeSeriesWeight,
                themeWeight);
    }
}
