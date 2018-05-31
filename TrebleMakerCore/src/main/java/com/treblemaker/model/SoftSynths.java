package com.treblemaker.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "soft_synths")
public class SoftSynths implements Serializable  {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "synth_name")
    private String synthName;

    @Column(name = "synth_file_path")
    private String synthFilePath;

    @Column(name = "mood_dark")
    private int moodDark;

    @Column(name = "mood_light")
    private int moodLight;

    @Column(name = "sixty_four_bit")
    private int sixtyFourBit;

    @Column(name = "date")
    private Date date;

    @Column(name = "users_id")
    private int usersId;

    @Column(name = "loop_transfer_complete")
    private boolean loopTransferComplete = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSynthName() {
        return synthName;
    }

    public void setSynthName(String synthName) {
        this.synthName = synthName;
    }

    public String getSynthFilePath() {
        return synthFilePath;
    }

    public void setSynthFilePath(String synthFilePath) {
        this.synthFilePath = synthFilePath;
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

    public int getSixtyFourBit() {
        return sixtyFourBit;
    }

    public void setSixtyFourBit(int sixtyFourBit) {
        this.sixtyFourBit = sixtyFourBit;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUsersId() {
        return usersId;
    }

    public void setUsersId(int usersId) {
        this.usersId = usersId;
    }

    public boolean isLoopTransferComplete() {
        return loopTransferComplete;
    }

    public void setLoopTransferComplete(boolean loopTransferComplete) {
        this.loopTransferComplete = loopTransferComplete;
    }
}