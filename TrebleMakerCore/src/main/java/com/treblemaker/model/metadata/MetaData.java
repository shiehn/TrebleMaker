package com.treblemaker.model.metadata;

import org.nd4j.shade.jackson.annotation.JsonIgnoreProperties;
import org.nd4j.shade.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaData {

    @JsonProperty("version")
    String version;

    @JsonProperty("key")
    String key;

    @JsonProperty("tracks")
    List<String> tracks;

    @JsonProperty("chords")
    Map<String, MetaDataChords> chords;

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    @JsonProperty("tracks")
    public List<String> getTracks() {
        return tracks;
    }

    @JsonProperty("tracks")
    public void setTracks(List<String> tracks) {
        this.tracks = tracks;
    }

    public void addTrack(String track) {
        if(this.tracks == null){
            this.tracks = new ArrayList<>();
        }

        this.tracks.add(track);
    }

    @JsonProperty("chords")
    public Map<String, MetaDataChords> getChords() {
        return chords;
    }

    @JsonProperty("chords")
    public void setChords(Map<String, MetaDataChords> chords) {
        this.chords = chords;
    }

    public void addChords(String trackPart, MetaDataChords chords){
        if(this.chords == null){
            this.chords = new HashMap<>();
        }

        this.chords.put(trackPart, chords);
    }
}
