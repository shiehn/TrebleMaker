package com.treblemaker.model.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "metadata_chordinfo")
public class MetaDataChordInfo implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "track_id")
    private String trackId;

    @Column(name = "track_key")
    private String trackKey;

    @Column(name = "part_type")
    private String partType;

    @Column(name = "part_chords")
    private String partChords;

    @Column(name = "part_key")
    private String partKey;

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getTrackKey() {
        return trackKey;
    }

    public void setTrackKey(String trackKey) {
        this.trackKey = trackKey;
    }

    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    public String getPartChords() {
        return partChords;
    }

    public void setPartChords(String partChords) {
        this.partChords = partChords;
    }

    public String getPartKey() {
        return partKey;
    }

    public void setPartKey(String partKey) {
        this.partKey = partKey;
    }
}