package com.treblemaker.model.stations;

import javax.persistence.*;

@Entity
@Table(name = "station_track")
public class StationTrack {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "file")
    private String file;

    @Column(name = "uploaded")
    private int uploaded;

    @Column(name = "add_to_station")
    private int addToStation;

    @Column(name = "station_id")
    private int stationId;

    @Column(name = "num_of_versions")
    private int numOfVersions;

    @Column(name = "num_of_version_variations")
    private int numOfVersionVariations;

    @ManyToOne
    @JoinColumn(name = "station_id", insertable = false, updatable = false)
    private Station station;

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

    public int getUploaded() {
        return uploaded;
    }

    public void setUploaded(int uploaded) {
        this.uploaded = uploaded;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getAddToStation() {
        return addToStation;
    }

    public void setAddToStation(int addToStation) {
        this.addToStation = addToStation;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getNumOfVersions() {
        return numOfVersions;
    }

    public void setNumOfVersions(int numOfVersions) {
        this.numOfVersions = numOfVersions;
    }

    public int getNumOfVersionVariations() {
        return numOfVersionVariations;
    }

    public void setNumOfVersionVariations(int numOfVersionVariations) {
        this.numOfVersionVariations = numOfVersionVariations;
    }
}
