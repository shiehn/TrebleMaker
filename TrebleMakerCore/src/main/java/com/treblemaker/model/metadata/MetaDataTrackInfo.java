package com.treblemaker.model.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "metadata_trackinfo")
public class MetaDataTrackInfo implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "track_id")
    private String trackId;

    @Column(name = "track_type")
    private String trackType;

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getTrackType() {
        return trackType;
    }

    public void setTrackType(String trackType) {
        this.trackType = trackType;
    }
}
