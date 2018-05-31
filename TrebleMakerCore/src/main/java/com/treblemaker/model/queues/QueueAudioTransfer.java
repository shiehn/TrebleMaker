package com.treblemaker.model.queues;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "queue_audio_transfer")
public class QueueAudioTransfer {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "state")
    private int state;

    @Column(name = "filepath")
    private String filepath;

    @Column(name = "filename")
    private String filename;

    @Column(name = "date")
    private Date date;

    @Column(name = "loop_id")
    private int loopId;

    @Column(name = "loop_type")
    private int loop_type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getLoopId() {
        return loopId;
    }

    public void setLoopId(int loopId) {
        this.loopId = loopId;
    }

    public int getLoop_type() {
        return loop_type;
    }

    public void setLoop_type(int loop_type) {
        this.loop_type = loop_type;
    }

    @Transient
    public static final int PROGRESS_UNSTARTED = 0;

    @Transient
    public static final int PROGRESS_STARTED = 1;

    @Transient
    public static final int PROGRESS_COMPLETE = 2;

    @Transient
    public static final int LOOP_TYPE_BEAT = 0;

    @Transient
    public static final int LOOP_TYPE_HARMONIC = 1;

    @Transient
    public static final int LOOP_TYPE_AMBIENT = 2;

    @Transient
    public static final int LOOP_TYPE_SOUND_FONT = 3;
}
