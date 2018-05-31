package com.treblemaker.model.composition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "composition_time_slots")
public class CompositionTimeSlot implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "composition_id")
    private Integer compositionId;

    @Column(name = "msec_start")
    private Integer msecStart;

    @Column(name = "msec_stop")
    private Integer msecStop;

    @Column(name = "beat_loop_id")
    private Integer beatLoopId;

    @Column(name = "beat_loop_alt_id")
    private Integer beatLoopAltId;

    @Column(name = "fill_id")
    private Integer fillId;

    @Column(name = "hit_id")
    private Integer hitId;

    @Column(name = "harmonic_loop_id")
    private Integer harmonicLoopId;

    @Column(name = "harmonic_loop_alt_id")
    private Integer harmonicLoopAltId;

    @Column(name = "ambient_loop_id")
    private Integer ambientLoopId;

    @Column(name = "ambient_loop_alt_id")
    private Integer ambientLoopAltId;

    @Column(name = "chord_id")
    private Integer chordId;

    @Column(name = "synth_template_id")
    private Integer synthTemplateId;

    @Column(name = "synth_template_hi_id")
    private Integer synthTemplateHiId;

    @Column(name = "synth_template_mid_id")
    private Integer synthTemplateMidId;

    @Column(name = "synth_template_low_id")
    private Integer synthTemplateLowId;

    @Column(name = "arpeggio_id")
    private Integer arpeggioId;

    @Column(name = "fx_arpeggio_delay_id")
    private Integer fxArpeggioDelayId;

    @Column(name = "bassline_id")
    private Integer basslineId;

    @Column(name = "kick_pattern_id")
    private Integer kickPatternId;

    @Column(name = "snare_pattern_id")
    private Integer snarePatternId;

    @Column(name = "hat_pattern_id")
    private Integer hatPatternId;

    @Column(name = "rated")
    private int rated = 0;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("composition_id")
    public Integer getCompositionId() {
        return compositionId;
    }

    @JsonProperty("composition_id")
    public void setCompositionId(Integer compositionId) {
        this.compositionId = compositionId;
    }

    @JsonProperty("msec_start")
    public Integer getMsecStart() {
        return msecStart;
    }

    @JsonProperty("msec_start")
    public void setMsecStart(Integer msecStart) {
        this.msecStart = msecStart;
    }

    @JsonProperty("msec_stop")
    public Integer getMsecStop() {
        return msecStop;
    }

    @JsonProperty("msec_stop")
    public void setMsecStop(Integer msecStop) {
        this.msecStop = msecStop;
    }

    @JsonProperty("beat_loop_id")
    public Integer getBeatLoopId() {
        return beatLoopId;
    }

    @JsonProperty("beat_loop_id")
    public void setBeatLoopId(Integer beatLoopId) {
        this.beatLoopId = beatLoopId;
    }

    @JsonProperty("beat_loop_alt_id")
    public Integer getBeatLoopAltId() {
        return beatLoopAltId;
    }

    @JsonProperty("beat_loop_alt_id")
    public void setBeatLoopAltId(Integer beatLoopAltId) {
        this.beatLoopAltId = beatLoopAltId;
    }

    @JsonProperty("fill_id")
    public Integer getFillId() {
        return fillId;
    }

    @JsonProperty("fill_id")
    public void setFillId(Integer fillId) {
        this.fillId = fillId;
    }

    @JsonProperty("hit_id")
    public Integer getHitId() {
        return hitId;
    }

    @JsonProperty("hit_id")
    public void setHitId(Integer hitId) {
        this.hitId = hitId;
    }

    @JsonProperty("harmonic_loop_id")
    public Integer getHarmonicLoopId() {
        return harmonicLoopId;
    }

    @JsonProperty("harmonic_loop_id")
    public void setHarmonicLoopId(Integer harmonicLoopId) {
        this.harmonicLoopId = harmonicLoopId;
    }

    @JsonProperty("harmonic_loop_alt_id")
    public Integer getHarmonicLoopAltId() {
        return harmonicLoopAltId;
    }

    @JsonProperty("harmonic_loop_alt_id")
    public void setHarmonicLoopAltId(Integer harmonicLoopAltId) {
        this.harmonicLoopAltId = harmonicLoopAltId;
    }

    @JsonProperty("ambient_loop_id")
    public Integer getAmbientLoopId() {
        return ambientLoopId;
    }

    @JsonProperty("ambient_loop_id")
    public void setAmbientLoopId(Integer ambientLoopId) {
        this.ambientLoopId = ambientLoopId;
    }

    @JsonProperty("ambient_loop_alt_id")
    public Integer getAmbientLoopAltId() {
        return ambientLoopAltId;
    }

    @JsonProperty("ambient_loop_alt_id")
    public void setAmbientLoopAltId(Integer ambientLoopAltId) {
        this.ambientLoopAltId = ambientLoopAltId;
    }

    @JsonProperty("chord_id")
    public Integer getChordId() {
        return chordId;
    }

    @JsonProperty("chord_id")
    public void setChordId(Integer chordId) {
        this.chordId = chordId;
    }

    @JsonProperty("synth_template_id")
    public Integer getSynthTemplateId() {
        return synthTemplateId;
    }

    @JsonProperty("synth_template_id")
    public void setSynthTemplateId(Integer synthTemplateId) {
        this.synthTemplateId = synthTemplateId;
    }

    @JsonProperty("synth_template_hi_id")
    public Integer  getSynthTemplateHiId() {
        return this.synthTemplateHiId;
    }

    @JsonProperty("synth_template_hi_id")
    public void setSynthTemplateHiId(Integer synthTemplateHiId) {
        this.synthTemplateHiId = synthTemplateHiId;
    }

    @JsonProperty("synth_template_mid_id")
    public Integer getSynthTemplateMidId() {
        return synthTemplateMidId;
    }

    @JsonProperty("synth_template_mid_id")
    public void setSynthTemplateMidId(Integer synthTemplateMidId) {
        this.synthTemplateMidId = synthTemplateMidId;
    }

    @JsonProperty("synth_template_low_id")
    public Integer getSynthTemplateLowId() {
        return synthTemplateLowId;
    }

    @JsonProperty("synth_template_low_id")
    public void setSynthTemplateLowId(Integer synthTemplateLowId) {
        this.synthTemplateLowId = synthTemplateLowId;
    }

    @JsonProperty("arpeggio_id")
    public Integer getArpeggioId() {
        return arpeggioId;
    }

    @JsonProperty("arpeggio_id")
    public void setArpeggioId(Integer arpeggioId) {
        this.arpeggioId = arpeggioId;
    }

    @Column(name = "fx_arpeggio_delay_id")
    public Integer getFxArpeggioDelayId() {
        return fxArpeggioDelayId;
    }

    @Column(name = "fx_arpeggio_delay_id")
    public void setFxArpeggioDelayId(Integer fxArpeggioDelayId) {
        this.fxArpeggioDelayId = fxArpeggioDelayId;
    }

    @JsonProperty("bassline_id")
    public Integer getBasslineId() {
        return basslineId;
    }

    @JsonProperty("bassline_id")
    public void setBasslineId(Integer basslineId) {
        this.basslineId = basslineId;
    }

    @JsonProperty("rated")
    public int getRated() {
        return rated;
    }

    @JsonProperty("rated")
    public void setRated(int rated) {
        this.rated = rated;
    }

    @JsonProperty("kick_pattern_id")
    public Integer getKickPatternId() {
        return kickPatternId;
    }

    @JsonProperty("kick_pattern_id")
    public void setKickPatternId(Integer kickPatternId) {
        this.kickPatternId = kickPatternId;
    }

    @JsonProperty("snare_pattern_id")
    public Integer getSnarePatternId() {
        return snarePatternId;
    }

    @JsonProperty("snare_pattern_id")
    public void setSnarePatternId(Integer snarePatternId) {
        this.snarePatternId = snarePatternId;
    }

    @JsonProperty("hat_pattern_id")
    public Integer getHatPatternId() {
        return hatPatternId;
    }

    @JsonProperty("hat_pattern_id")
    public void setHatPatternId(Integer hatPatternId) {
        this.hatPatternId = hatPatternId;
    }
}
