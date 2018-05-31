package com.treblemaker.model.analytics;


public class PredictorVertical {
    /*
    beat_loop_id
beat_loop_alt_id
fill_id
harmonic_loop_id
harmonic_loop_alt_id
ambient_loop_id
ambient_loop_alt_id
chord_id
synth_template_id
synth_template_hi_id
synth_template_mid_id
synth_template_low_id
   */

    private int rating;
    private String beatLoopId;
    private String beatLoopAltId;
    private String fillId;
    private String harmonicLoopId;
    private String harmonicLoopAltId;
    private String ambientLoopId;
    private String ambientLoopAltId;
    private String chordId;
    private String synthTemplateId;
    private String synthTemplateHiId;
    private String synthTemplateMidId;
    private String synthTemplateLowId;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getBeatLoopId() {
        return beatLoopId;
    }

    public void setBeatLoopId(String beatLoopId) {
        this.beatLoopId = beatLoopId;
    }

    public String getBeatLoopAltId() {
        return beatLoopAltId;
    }

    public void setBeatLoopAltId(String beatLoopAltId) {
        this.beatLoopAltId = beatLoopAltId;
    }

    public String getFillId() {
        return fillId;
    }

    public void setFillId(String fillId) {
        this.fillId = fillId;
    }

    public String getHarmonicLoopId() {
        return harmonicLoopId;
    }

    public void setHarmonicLoopId(String harmonicLoopId) {
        this.harmonicLoopId = harmonicLoopId;
    }

    public String getHarmonicLoopAltId() {
        return harmonicLoopAltId;
    }

    public void setHarmonicLoopAltId(String harmonicLoopAltId) {
        this.harmonicLoopAltId = harmonicLoopAltId;
    }

    public String getAmbientLoopId() {
        return ambientLoopId;
    }

    public void setAmbientLoopId(String ambientLoopId) {
        this.ambientLoopId = ambientLoopId;
    }

    public String getAmbientLoopAltId() {
        return ambientLoopAltId;
    }

    public void setAmbientLoopAltId(String ambientLoopAltId) {
        this.ambientLoopAltId = ambientLoopAltId;
    }

    public String getChordId() {
        return chordId;
    }

    public void setChordId(String chordId) {
        this.chordId = chordId;
    }

    public String getSynthTemplateId() {
        return synthTemplateId;
    }

    public void setSynthTemplateId(String synthTemplateId) {
        this.synthTemplateId = synthTemplateId;
    }

    public String getSynthTemplateHiId() {
        return synthTemplateHiId;
    }

    public void setSynthTemplateHiId(String synthTemplateHiId) {
        this.synthTemplateHiId = synthTemplateHiId;
    }

    public String getSynthTemplateMidId() {
        return synthTemplateMidId;
    }

    public void setSynthTemplateMidId(String synthTemplateMidId) {
        this.synthTemplateMidId = synthTemplateMidId;
    }

    public String getSynthTemplateLowId() {
        return synthTemplateLowId;
    }

    public void setSynthTemplateLowId(String synthTemplateLowId) {
        this.synthTemplateLowId = synthTemplateLowId;
    }
}
