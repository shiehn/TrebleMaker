package com.treblemaker.model.melodic;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bach_chorales")
public class BachChorale implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "row_id")
    private String rowId;

    @Column(name = "event_id")
    private String eventId;

    @Column(name = "c")
    private boolean hasCNote;

    @Column(name = "cs")
    private boolean hasCSNote;

    @Column(name = "d")
    private boolean hasDNote;

    @Column(name = "ds")
    private boolean hasDSNote;

    @Column(name = "e")
    private boolean hasENote;

    @Column(name = "f")
    private boolean hasFNote;

    @Column(name = "fs")
    private boolean hasFSNote;

    @Column(name = "g")
    private boolean hasGNote;

    @Column(name = "gs")
    private boolean hasGSNote;

    @Column(name = "a")
    private boolean hasANote;

    @Column(name = "as")
    private boolean hasASNote;

    @Column(name = "b")
    private boolean hasBNote;

    @Column(name = "bass_note")
    private String bassNote;

    @Column(name = "accent")
    private Integer accent;

    @Column(name = "chord")
    private String chord;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public boolean isHasCNote() {
        return hasCNote;
    }

    public void setHasCNote(boolean hasCNote) {
        this.hasCNote = hasCNote;
    }

    public boolean isHasCSNote() {
        return hasCSNote;
    }

    public void setHasCSNote(boolean hasCSNote) {
        this.hasCSNote = hasCSNote;
    }

    public boolean isHasDNote() {
        return hasDNote;
    }

    public void setHasDNote(boolean hasDNote) {
        this.hasDNote = hasDNote;
    }

    public boolean isHasDSNote() {
        return hasDSNote;
    }

    public void setHasDSNote(boolean hasDSNote) {
        this.hasDSNote = hasDSNote;
    }

    public boolean isHasENote() {
        return hasENote;
    }

    public void setHasENote(boolean hasENote) {
        this.hasENote = hasENote;
    }

    public boolean isHasFNote() {
        return hasFNote;
    }

    public void setHasFNote(boolean hasFNote) {
        this.hasFNote = hasFNote;
    }

    public boolean isHasFSNote() {
        return hasFSNote;
    }

    public void setHasFSNote(boolean hasFSNote) {
        this.hasFSNote = hasFSNote;
    }

    public boolean isHasGNote() {
        return hasGNote;
    }

    public void setHasGNote(boolean hasGNote) {
        this.hasGNote = hasGNote;
    }

    public boolean isHasGSNote() {
        return hasGSNote;
    }

    public void setHasGSNote(boolean hasGSNote) {
        this.hasGSNote = hasGSNote;
    }

    public boolean isHasANote() {
        return hasANote;
    }

    public void setHasANote(boolean hasANote) {
        this.hasANote = hasANote;
    }

    public boolean isHasASNote() {
        return hasASNote;
    }

    public void setHasASNote(boolean hasASNote) {
        this.hasASNote = hasASNote;
    }

    public boolean isHasBNote() {
        return hasBNote;
    }

    public void setHasBNote(boolean hasBNote) {
        this.hasBNote = hasBNote;
    }

    public String getBassNote() {
        return bassNote;
    }

    public void setBassNote(String bassNote) {
        this.bassNote = bassNote;
    }

    public Integer getAccent() {
        return accent;
    }

    public void setAccent(Integer accent) {
        this.accent = accent;
    }

    public String getChord() {
        return chord;
    }

    public void setChord(String chord) {
        this.chord = chord;
    }

    public List<String> getAvailableNotes() {
        List<String> availableNotes = new ArrayList<>();

        if (hasCNote) {
            availableNotes.add("c");
        }

        if (hasCSNote) {
            availableNotes.add("c#");
        }

        if (hasDNote) {
            availableNotes.add("d");
        }

        if (hasDSNote) {
            availableNotes.add("d#");
        }

        if (hasENote) {
            availableNotes.add("e");
        }

        if (hasFNote) {
            availableNotes.add("f");
        }

        if (hasFSNote) {
            availableNotes.add("f#");
        }

        if (hasGNote) {
            availableNotes.add("g");
        }

        if (hasGSNote) {
            availableNotes.add("g#");
        }

        if (hasANote) {
            availableNotes.add("a");
        }

        if (hasASNote) {
            availableNotes.add("a#");
        }

        if (hasBNote) {
            availableNotes.add("b");
        }

        return availableNotes;
    }
}
