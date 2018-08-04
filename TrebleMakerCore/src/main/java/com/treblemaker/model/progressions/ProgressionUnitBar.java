package com.treblemaker.model.progressions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.treblemaker.extractors.model.HarmonyExtraction;
import com.treblemaker.model.*;
import com.treblemaker.model.bassline.Bassline;
import com.treblemaker.model.comp.CompRhythm;
import com.treblemaker.model.composition.CompositionTimeSlot;
import com.treblemaker.model.composition.CompositionTimeSlotPanning;
import com.treblemaker.model.fx.FXArpeggioWithRating;
import com.treblemaker.model.hat.HatPattern;
import com.treblemaker.model.hitsandfills.Fill;
import com.treblemaker.model.hitsandfills.Hit;
import com.treblemaker.model.kick.KickPattern;
import com.treblemaker.model.kick.KickSample;
import com.treblemaker.model.snare.SnarePattern;
import org.jfugue.pattern.Pattern;
import org.jfugue.theory.Chord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties({"jChord", "patternHi", "patternHiAlt", "patternMid", "patternMidAlt", "patternLow", "patternLowAlt", "chord", "beatLoop", "beatLoopAlt", "ambienceLoop", "harmonicLoop", "harmonicLoopAlt", "compositionTimeSlot", "AlternativeHi", "AlternativeMid", "AlternativeLow", "AlternativeRhythm"})
public class ProgressionUnitBar implements Cloneable {

    private Chord jChord;
    private HiveChord chord;
    private BeatLoop beatLoop;
    private BeatLoop beatLoopAlt;
    private List<BeatLoop> beatLoopAltOptions;
    private AmbienceLoop ambienceLoopAlt;
    private AmbienceLoop ambienceLoop;
    private List<AmbienceLoop> ambienceLoopAltOptions;
    private HarmonicLoop harmonicLoop;
    private HarmonicLoop harmonicLoopAlt;
    private List<HarmonicLoop> harmonicLoopOptions;
    private List<HarmonicLoop> harmonicLoopAltOptions;
    private Hit hit;
    private List<Hit> hitOptions;
    private Fill fill;
    private List<Fill> fillOptions;

    private Pattern kickMidiPattern;
    private KickPattern kickPattern;
    private KickSample kickSample;


    private Pattern hatMidiPattern;
    private HatPattern hatPattern;

    private Pattern snareMidiPattern;
    private SnarePattern snarePattern;

    private CompositionTimeSlot compositionTimeSlot;
    private CompositionTimeSlotPanning compositionTimeSlotPanning;


    //MELODIC PATTERN STUFF ...
    private RhythmicAccents melodicPositions;
    private List<String> arpeggioHiPositions;
    private List<String> arpeggioLowPositions;
    private List<HarmonyExtraction> harmonicExtractions;
    private List<String> melodicPhrase;
    //MELODIC PATTERN STUFF ..

    private List<SynthTemplate> synthTemplates;
    private List<List<SynthTemplateOption>> synthTemplateHiOptions;
    private List<List<SynthTemplateOption>> synthTemplateMidOptions;
    private List<List<SynthTemplateOption>> synthTemplateLowOptions;

    private List<Integer> hiSynthId;
    private List<Integer> midSynthId;
    private List<Integer> lowSynthId;

    private int arpeggioId;

    private List<Map<Integer, FXArpeggioWithRating>> selectedFXMap;

    private CompRhythm selectedCompRhythm;
    private int basslineId;
    private Bassline selectedBassline;

    //TODO THESE CAUSE PROBLEMS WITH JSON SERIALIZATION !!!!
    private List<Pattern> patternHi = new ArrayList<>();
    private List<Pattern> patternHiAlt = new ArrayList<>();
    private List<Pattern> patternMid = new ArrayList<>();
    private List<Pattern> patternMidAlt = new ArrayList<>();
    private List<Pattern> patternLow = new ArrayList<>();
    private List<Pattern> patternLowAlt = new ArrayList<>();
    //TODO THESE CAUSE PROBLEMS WITH JSON SERIALIZATION !!!!

    private boolean AlternativeRhythm;

    private Map<String, List<String>> sentimentLabels = null;

    public ProgressionUnitBar() {
        compositionTimeSlot = new CompositionTimeSlot();
        compositionTimeSlotPanning = new CompositionTimeSlotPanning();
    }

    public Chord getjChord() {
        return jChord;
    }

    public void setjChord(Chord jChord) {
        this.jChord = jChord;
    }

    public HiveChord getChord() {
        return chord;
    }

    public void setChord(HiveChord chord) {
        this.chord = chord;
    }

    public BeatLoop getBeatLoop() {
        return beatLoop;
    }

    public void setBeatLoop(BeatLoop beatLoop) {
        this.beatLoop = beatLoop;
    }

    public BeatLoop getBeatLoopAlt() {
        return beatLoopAlt;
    }

    public void setBeatLoopAlt(BeatLoop beatLoopAlt) {
        this.beatLoopAlt = beatLoopAlt;
    }

    public AmbienceLoop getAmbienceLoop() {
        return ambienceLoop;
    }

    public void setAmbienceLoop(AmbienceLoop ambienceLoop) {
        this.ambienceLoop = ambienceLoop;
    }

    public AmbienceLoop getAmbienceLoopAlt() {
        return ambienceLoopAlt;
    }

    public void setAmbienceLoopAlt(AmbienceLoop ambienceLoopAlt) {
        this.ambienceLoopAlt = ambienceLoopAlt;
    }

    public HarmonicLoop getHarmonicLoop() {
        return harmonicLoop;
    }

    public void setHarmonicLoop(HarmonicLoop harmonicLoop) {
        this.harmonicLoop = harmonicLoop;
    }

    public HarmonicLoop getHarmonicLoopAlt() {
        return harmonicLoopAlt;
    }

    public void setHarmonicLoopAlt(HarmonicLoop harmonicLoopAlt) {
        this.harmonicLoopAlt = harmonicLoopAlt;
    }

    public CompositionTimeSlot getCompositionTimeSlot() {
        return compositionTimeSlot;
    }

    public void setCompositionTimeSlot(CompositionTimeSlot compositionTimeSlot) {
        this.compositionTimeSlot = compositionTimeSlot;
    }

    public CompositionTimeSlotPanning getCompositionTimeSlotPanning() {
        return compositionTimeSlotPanning;
    }

    public void setCompositionTimeSlotPanning(CompositionTimeSlotPanning compositionTimeSlotPanning) {
        this.compositionTimeSlotPanning = compositionTimeSlotPanning;
    }

    public List<Pattern> getPatternHi() {
        return patternHi;
    }

    public void setPatternHi(List<Pattern> patternHi) {
        this.patternHi = patternHi;
    }

    public void addPatternHi(Pattern patternHi) {
        this.patternHi.add(patternHi);
    }

    public List<Pattern> getPatternHiAlt() {
        return patternHiAlt;
    }

    public void setPatternHiAlt(List<Pattern> patternHiAlt) {
        this.patternHiAlt = patternHiAlt;
    }

    public void addPatternHiAlt(Pattern patternHiAlt) {
        this.patternHiAlt.add(patternHiAlt);
    }

    public List<Pattern> getPatternMid() {
        return patternMid;
    }

    public void setPatternMid(List<Pattern> patternMid) {
        this.patternMid = patternMid;
    }

    public void addPatternMid(Pattern patternMid) {
        this.patternMid.add(patternMid);
    }

    public List<Pattern> getPatternMidAlt() {
        return patternMidAlt;
    }

    public void setPatternMidAlt(List<Pattern> patternMidAlt) {
        this.patternMidAlt = patternMidAlt;
    }

    public void addPatternMidAlt(Pattern patternMidAlt) {
        this.patternMidAlt.add(patternMidAlt);
    }

    public List<Pattern> getPatternLow() {
        return patternLow;
    }

    public void setPatternLow(List<Pattern> patternLow) {
        this.patternLow = patternLow;
    }

    public void addPatternLow(Pattern patternLow) {
        if (this.patternLow == null) {
            this.patternLow = new ArrayList<>();
        }
        this.patternLow.add(patternLow);
    }

    public List<Pattern> getPatternLowAlt() {
        return patternLowAlt;
    }

    public void setPatternLowAlt(List<Pattern> patternLowAlt) {
        this.patternLowAlt = patternLowAlt;
    }

    public void addPatternLowAlt(Pattern patternLowAlt) {
        this.patternLowAlt.add(patternLowAlt);
    }

    public List<BeatLoop> getBeatLoopAltOptions() {

        if (beatLoopAltOptions == null) {
            beatLoopAltOptions = new ArrayList<>();
        }
        return beatLoopAltOptions;
    }

    public void setBeatLoopAltOptions(List<BeatLoop> beatLoopAltOptions) {
        this.beatLoopAltOptions = beatLoopAltOptions;
    }

    public List<AmbienceLoop> getAmbienceLoopAltOptions() {

        if (ambienceLoopAltOptions == null) {
            ambienceLoopAltOptions = new ArrayList<>();
        }

        return ambienceLoopAltOptions;
    }

    public void setAmbienceLoopAltOptions(List<AmbienceLoop> ambienceLoopAltOptions) {
        this.ambienceLoopAltOptions = ambienceLoopAltOptions;
    }

    public List<HarmonicLoop> getHarmonicLoopOptions() {

        if (harmonicLoopOptions == null) {
            harmonicLoopOptions = new ArrayList<>();
        }

        return harmonicLoopOptions;
    }

    public void setHarmonicLoopOptions(List<HarmonicLoop> harmonicLoopOptions) {
        this.harmonicLoopOptions = harmonicLoopOptions;
    }

    public List<HarmonicLoop> getHarmonicLoopAltOptions() {

        if (harmonicLoopAltOptions == null) {
            harmonicLoopAltOptions = new ArrayList<>();
        }

        return harmonicLoopAltOptions;
    }

    public void setHarmonicLoopAltOptions(List<HarmonicLoop> harmonicLoopAltOptions) {
        this.harmonicLoopAltOptions = harmonicLoopAltOptions;
    }

    public Hit getHit() {
        return hit;
    }

    public void setHit(Hit hit) {
        this.hit = hit;
    }

    public List<Hit> getHitOptions() {

        if (hitOptions == null) {
            hitOptions = new ArrayList<>();
        }

        return hitOptions;
    }

    public void setHitOptions(List<Hit> hitOptions) {
        this.hitOptions = hitOptions;
    }


    public Fill getFill() {
        return fill;
    }

    public void setFill(Fill fill) {
        this.fill = fill;
    }

    public List<Fill> getFillOptions() {

        if (fillOptions == null) {
            fillOptions = new ArrayList<>();
        }

        return fillOptions;
    }

    public void setFillOptions(List<Fill> fillOptions) {
        this.fillOptions = fillOptions;
    }

    public List<SynthTemplate> getSynthTemplates() {
        return synthTemplates;
    }

    public void setSynthTemplates(List<SynthTemplate> synthTemplates) {
        this.synthTemplates = synthTemplates;
    }

    public List<List<SynthTemplateOption>> getSynthTemplateHiOptions() {
        return synthTemplateHiOptions;
    }

    public void setSynthTemplateHiOptions(List<List<SynthTemplateOption>> synthTemplateHiOptions) {
        this.synthTemplateHiOptions = synthTemplateHiOptions;
    }

    public void addSynthTemplateHiOption(List<SynthTemplateOption> synthTemplateHiOption) {
        if (this.synthTemplateHiOptions == null) {
            this.synthTemplateHiOptions = new ArrayList<>();
        }

        this.synthTemplateHiOptions.add(synthTemplateHiOption);
    }

    public List<List<SynthTemplateOption>> getSynthTemplateMidOptions() {
        return synthTemplateMidOptions;
    }

    public void setSynthTemplateMidOptions(List<List<SynthTemplateOption>> synthTemplateMidOptions) {
        this.synthTemplateMidOptions = synthTemplateMidOptions;
    }

    public void addSynthTemplateMidOption(List<SynthTemplateOption> synthTemplateMidOption) {
        if (this.synthTemplateMidOptions == null) {
            this.synthTemplateMidOptions = new ArrayList<>();
        }

        this.synthTemplateMidOptions.add(synthTemplateMidOption);
    }

    public List<List<SynthTemplateOption>> getSynthTemplateLowOptions() {
        return synthTemplateLowOptions;
    }

    public void setSynthTemplateLowOptions(List<List<SynthTemplateOption>> synthTemplateLowOptions) {
        this.synthTemplateLowOptions = synthTemplateLowOptions;
    }

    public void addSynthTemplateLowOption(List<SynthTemplateOption> synthTemplateLowOption) {
        if (this.synthTemplateLowOptions == null) {
            this.synthTemplateLowOptions = new ArrayList<>();
        }

        this.synthTemplateLowOptions.add(synthTemplateLowOption);
    }

    public List<Integer> getHiSynthId() {
        return hiSynthId;
    }

    public void setHiSynthId(List<Integer> hiSynthId) {
        this.hiSynthId = hiSynthId;
    }

    public void addHiSynthId(Integer hiSynthId) {
        if (this.hiSynthId == null) {
            this.hiSynthId = new ArrayList<>();
        }
        this.hiSynthId.add(hiSynthId);
    }

    public List<Integer> getMidSynthId() {
        return midSynthId;
    }

    public void setMidSynthId(List<Integer> midSynthId) {
        this.midSynthId = midSynthId;
    }

    public void addMidSynthId(Integer midSynthId) {
        if (this.midSynthId == null) {
            this.midSynthId = new ArrayList<>();
        }
        this.midSynthId.add(midSynthId);
    }

    public List<Integer> getLowSynthId() {
        return lowSynthId;
    }

    public void setLowSynthId(List<Integer> lowSynthId) {
        this.lowSynthId = lowSynthId;
    }

    public void addLowSynthId(Integer lowSynthId) {

        if (this.lowSynthId == null) {
            this.lowSynthId = new ArrayList<>();
        }
        this.lowSynthId.add(lowSynthId);
    }

    @JsonProperty("AlternativeRhythm")
    public boolean isAlternativeRhythm() {
        return AlternativeRhythm;
    }

    @JsonProperty("AlternativeRhythm")
    public void setAlternativeRhythm(boolean alternativeRhythm) {
        AlternativeRhythm = alternativeRhythm;
    }

    public int getArpeggioId() {
        return arpeggioId;
    }

    public void setArpeggioId(int arpeggioId) {
        this.arpeggioId = arpeggioId;
    }

    public Map<Integer, FXArpeggioWithRating> getSelectedFXMap(int index) {
        return selectedFXMap.get(index);
    }

    public Integer getSelectedFXId(int index) {
        return selectedFXMap.get(index).get(hiSynthId.get(index)).getFxArpeggioDelay().getId();
    }

    public void setSelectedFXMap(List<Map<Integer, FXArpeggioWithRating>> selectedFXMap) {
        this.selectedFXMap = selectedFXMap;
    }

    public void addSelectedFXMap(Map<Integer, FXArpeggioWithRating> selectedFXMap) {
        if (this.selectedFXMap == null) {
            this.selectedFXMap = new ArrayList<>();
        }

        this.selectedFXMap.add(selectedFXMap);
    }

    public int getBasslineId() {
        return basslineId;
    }

    public void setBasslineId(int basslineId) {
        this.basslineId = basslineId;
    }

    public Bassline getSelectedBassline() {
        return selectedBassline;
    }

    public void setSelectedBassline(Bassline selectedBassline) {
        this.selectedBassline = selectedBassline;
    }

    public CompRhythm getSelectedCompRhythm() {
        return selectedCompRhythm;
    }

    public void setSelectedCompRhythm(CompRhythm selectedCompRhythm) {
        this.selectedCompRhythm = selectedCompRhythm;
    }

    @Override
    public ProgressionUnitBar clone() throws CloneNotSupportedException {
        return (ProgressionUnitBar) super.clone();
    }

    public RhythmicAccents getMelodicPositions() {
        return melodicPositions;
    }

    public void setMelodicPositions(RhythmicAccents melodicPositions) {
        this.melodicPositions = melodicPositions;
    }

    public List<String> getArpeggioHiPositions() {
        return arpeggioHiPositions;
    }

    public void setArpeggioHiPositions(List<String> arpeggioHiPositions) {
        this.arpeggioHiPositions = arpeggioHiPositions;
    }

    public List<String> getArpeggioLowPositions() {
        return arpeggioLowPositions;
    }

    public void setArpeggioLowPositions(List<String> arpeggioLowPositions) {
        this.arpeggioLowPositions = arpeggioLowPositions;
    }

    public List<HarmonyExtraction> getHarmonicExtractions() {
        return harmonicExtractions;
    }

    public void setHarmonicExtractions(List<HarmonyExtraction> harmonicExtractions) {
        this.harmonicExtractions = harmonicExtractions;
    }

    public List<String> getMelodicPhrase() {
        return melodicPhrase;
    }

    public void setMelodicPhrase(List<String> melodicPhrase) {
        this.melodicPhrase = melodicPhrase;
    }

    public Pattern getKickMidiPattern(){
        return this.kickMidiPattern;
    }

    public void setKickMidiPattern(Pattern kickMidiPattern){
        this.kickMidiPattern = kickMidiPattern;
    }

    public KickPattern getKickPattern() {
        return kickPattern;
    }

    public void setKickPattern(KickPattern kickPattern) {
        this.kickPattern = kickPattern;
    }

    public KickSample getKickSample() {
        return kickSample;
    }

    public void setKickSample(KickSample kickSample) {
        this.kickSample = kickSample;
    }

    public Pattern getHatMidiPattern() {
        return hatMidiPattern;
    }

    public void setHatMidiPattern(Pattern hatMidiPattern) {
        this.hatMidiPattern = hatMidiPattern;
    }

    public HatPattern getHatPattern() {
        return hatPattern;
    }

    public Pattern getSnareMidiPattern() {
        return snareMidiPattern;
    }

    public void setSnarePattern(SnarePattern snarePattern) {
        this.snarePattern = snarePattern;
    }

    public void setSnareMidiPattern(Pattern snareMidiPattern) {
        this.snareMidiPattern = snareMidiPattern;
    }

    public SnarePattern getSnarePattern() {
        return snarePattern;
    }

    public void setHatPattern(HatPattern hatPattern) {
        this.hatPattern = hatPattern;
    }


    public Map<String, List<String>> getSentimentLabels() {
        return sentimentLabels;
    }

    public void setSentimentLabels(Map<String, List<String>> sentimentLabels) {
        this.sentimentLabels = sentimentLabels;
    }
}

