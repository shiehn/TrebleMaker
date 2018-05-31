package com.treblemaker.extractors.model;

import java.util.ArrayList;
import java.util.List;

public class HarmonyExtraction {

    String chordname;
    String bassNote;
    List<String> harmonies;

    public String getChordname() {
        return chordname;
    }

    public void setChordname(String chordname) {
        this.chordname = chordname;
    }

    public String getBassNote() {
        return bassNote;
    }

    public void setBassNote(String bassNote) {
        this.bassNote = bassNote;
    }

    public List<String> getHarmonies() {
        return harmonies;
    }

    public void setHarmonies(List<String> harmonies) {
        this.harmonies = harmonies;
    }

    public void addHarmony(String note) {
        if (harmonies == null) {
            harmonies = new ArrayList<>();
        }

        harmonies.add(note);
    }
}
