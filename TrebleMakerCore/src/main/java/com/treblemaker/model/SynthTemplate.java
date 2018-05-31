package com.treblemaker.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "synth_templates")
public class SynthTemplate implements Cloneable, Serializable {

	@Id
	@GeneratedValue
	private Integer id;

	@Column(name = "complexity")
	private int complexity;

	@Column(name = "hi_preset")
	private String hiPreset = "empty";

	@Column(name = "hi_synth_name")
	private String hiSynthName;

	@Column(name = "hi_synth_id")
	private int hiSynthId;

	@Column(name = "mid_preset")
	private String midPreset = "empty";

	@Column(name = "mid_synth_name")
	private String midSynthName;

	@Column(name = "mid_synth_id")
	private int midSynthId;

	@Column(name = "low_preset")
	private String lowPreset = "empty";

	@Column(name = "low_synth_name")
	private String lowSynthName;

	@Column(name = "low_synth_id")
	private int lowSynthId;

	@Column(name = "has_been_validated")
	private boolean hasBeenValidated;

	@Column(name = "hi_preset_alt")
	private String hiPresetAlt  = "empty";

	@Column(name = "hi_synth_name_alt")
	private String hiSynthNameAlt;

	@Column(name = "hi_synth_id_alt")
	private int hiSynthIdAlt;

	@Column(name = "mid_preset_alt")
	private String midPresetAlt = "empty";

	@Column(name = "mid_synth_name_alt")
	private String midSynthNameAlt;

	@Column(name = "mid_synth_id_alt")
	private int midSynthIdAlt;

	@Column(name = "low_preset_alt")
	private String lowPresetAlt = "empty";

	@Column(name = "low_synth_name_alt")
	private String lowSynthNameAlt;

	@Column(name = "low_synth_id_alt")
	private int lowSynthIdAlt;

	@Column(name = "mood_dark_level")
	private int moodDarkLevel;

	@Column(name = "mood_light_level")
	private int moodLightLevel;

	@Column(name = "hi_quarter_note_compatibility")
	private int hiQuarterNoteCompatibility;

	@Column(name = "hi_eigth_note_compatibility")
	private int hiEigthNoteCompatibility;

	@Column(name = "hi_sixteenth_note_compatibility")
	private int hiSixteenthNoteCompatibility;

	@Column(name = "hi_quarter_note_compatibility_alt")
	private int hiQuarterNoteCompatibilityAlt;

	@Column(name = "hi_eigth_note_compatibility_alt")
	private int hiEigthNoteCompatibilityAlt;

	@Column(name = "hi_sixteenth_note_compatibility_alt")
	private int hiSixteenthNoteCompatibilityAlt;

	@Column(name = "date")
	private Date date = new Date();

	@Column(name = "users_id")
	private int usersId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getComplexity() {
		return complexity;
	}

	public void setComplexity(int complexity) {
		this.complexity = complexity;
	}

	public String getHiPreset() {
		return hiPreset;
	}

	public void setHiPreset(String hiPreset) {
		this.hiPreset = hiPreset;
	}

	public String getHiSynthName() {
		return hiSynthName;
	}

	public void setHiSynthName(String hiSynthName) {
		this.hiSynthName = hiSynthName;
	}

	public int getHiSynthId() {
		return hiSynthId;
	}

	public void setHiSynthId(int hiSynthId) {
		this.hiSynthId = hiSynthId;
	}

	public String getMidPreset() {
		return midPreset;
	}

	public void setMidPreset(String midPreset) {
		this.midPreset = midPreset;
	}

	public String getMidSynthName() {
		return midSynthName;
	}

	public void setMidSynthName(String midSynthName) {
		this.midSynthName = midSynthName;
	}

	public int getMidSynthId() {
		return midSynthId;
	}

	public void setMidSynthId(int midSynthId) {
		this.midSynthId = midSynthId;
	}

	public String getLowPreset() {
		return lowPreset;
	}

	public void setLowPreset(String lowPreset) {
		this.lowPreset = lowPreset;
	}

	public String getLowSynthName() {
		return lowSynthName;
	}

	public void setLowSynthName(String lowSynthName) {
		this.lowSynthName = lowSynthName;
	}

	public int getLowSynthId() {
		return lowSynthId;
	}

	public void setLowSynthId(int lowSynthId) {
		this.lowSynthId = lowSynthId;
	}

	public boolean isHasBeenValidated() {
		return hasBeenValidated;
	}

	public void setHasBeenValidated(boolean hasBeenValidated) {
		this.hasBeenValidated = hasBeenValidated;
	}

	public String getHiPresetAlt() {
		return hiPresetAlt;
	}

	public void setHiPresetAlt(String hiPresetAlt) {
		this.hiPresetAlt = hiPresetAlt;
	}

	public String getHiSynthNameAlt() {
		return hiSynthNameAlt;
	}

	public void setHiSynthNameAlt(String hiSynthNameAlt) {
		this.hiSynthNameAlt = hiSynthNameAlt;
	}

	public int getHiSynthIdAlt() {
		return hiSynthIdAlt;
	}

	public void setHiSynthIdAlt(int hiSynthIdAlt) {
		this.hiSynthIdAlt = hiSynthIdAlt;
	}

	public String getMidPresetAlt() {
		return midPresetAlt;
	}

	public void setMidPresetAlt(String midPresetAlt) {
		this.midPresetAlt = midPresetAlt;
	}

	public String getMidSynthNameAlt() {
		return midSynthNameAlt;
	}

	public void setMidSynthNameAlt(String midSynthNameAlt) {
		this.midSynthNameAlt = midSynthNameAlt;
	}

	public int getMidSynthIdAlt() {
		return midSynthIdAlt;
	}

	public void setMidSynthIdAlt(int midSynthIdAlt) {
		this.midSynthIdAlt = midSynthIdAlt;
	}

	public String getLowPresetAlt() {
		return lowPresetAlt;
	}

	public void setLowPresetAlt(String lowPresetAlt) {
		this.lowPresetAlt = lowPresetAlt;
	}

	public String getLowSynthNameAlt() {
		return lowSynthNameAlt;
	}

	public void setLowSynthNameAlt(String lowSynthNameAlt) {
		this.lowSynthNameAlt = lowSynthNameAlt;
	}

	public int getLowSynthIdAlt() {
		return lowSynthIdAlt;
	}

	public void setLowSynthIdAlt(int lowSynthIdAlt) {
		this.lowSynthIdAlt = lowSynthIdAlt;
	}

	public int getMoodDarkLevel() {
		return moodDarkLevel;
	}

	public void setMoodDarkLevel(int moodDarkLevel) {
		this.moodDarkLevel = moodDarkLevel;
	}

	public int getMoodLightLevel() {
		return moodLightLevel;
	}

	public void setMoodLightLevel(int moodLightLevel) {
		this.moodLightLevel = moodLightLevel;
	}

	public int getHiQuarterNoteCompatibility() {
		return hiQuarterNoteCompatibility;
	}

	public void setHiQuarterNoteCompatibility(int hiQuarterNoteCompatibility) {
		this.hiQuarterNoteCompatibility = hiQuarterNoteCompatibility;
	}

	public int getHiEigthNoteCompatibility() {
		return hiEigthNoteCompatibility;
	}

	public void setHiEigthNoteCompatibility(int hiEigthNoteCompatibility) {
		this.hiEigthNoteCompatibility = hiEigthNoteCompatibility;
	}

	public int getHiSixteenthNoteCompatibility() {
		return hiSixteenthNoteCompatibility;
	}

	public void setHiSixteenthNoteCompatibility(int hiSixteenthNoteCompatibility) {
		this.hiSixteenthNoteCompatibility = hiSixteenthNoteCompatibility;
	}

	public int getHiQuarterNoteCompatibilityAlt() {
		return hiQuarterNoteCompatibilityAlt;
	}

	public void setHiQuarterNoteCompatibilityAlt(int hiQuarterNoteCompatibilityAlt) {
		this.hiQuarterNoteCompatibilityAlt = hiQuarterNoteCompatibilityAlt;
	}

	public int getHiEigthNoteCompatibilityAlt() {
		return hiEigthNoteCompatibilityAlt;
	}

	public void setHiEigthNoteCompatibilityAlt(int hiEigthNoteCompatibilityAlt) {
		this.hiEigthNoteCompatibilityAlt = hiEigthNoteCompatibilityAlt;
	}

	public int getHiSixteenthNoteCompatibilityAlt() {
		return hiSixteenthNoteCompatibilityAlt;
	}

	public void setHiSixteenthNoteCompatibilityAlt(int hiSixteenthNoteCompatibilityAlt) {
		this.hiSixteenthNoteCompatibilityAlt = hiSixteenthNoteCompatibilityAlt;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getUsersId() {
		return usersId;
	}

	public void setUsersId(int usersId) {
		this.usersId = usersId;
	}

	@Override
	public SynthTemplate clone() throws CloneNotSupportedException {
		return (SynthTemplate) super.clone();
	}
}
