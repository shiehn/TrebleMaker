package com.treblemaker.model.stations;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "station")
public class Station {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private int status;

    @Column(name = "bpm")
    private int bpm;

    @OneToMany(targetEntity=StationTrack.class, mappedBy = "station", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<StationTrack> stationTracks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Set<StationTrack> getStationTracks() {
        return stationTracks;
    }

    public void setStationTracks(Set<StationTrack> stationTracks) {
        this.stationTracks = stationTracks;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }
}
