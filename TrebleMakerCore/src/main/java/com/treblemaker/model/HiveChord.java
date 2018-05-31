package com.treblemaker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.treblemaker.model.interfaces.IInfluenceable;
import com.treblemaker.model.interfaces.IWeightable;
import com.treblemaker.weighters.TotalWeight;
import com.treblemaker.weighters.enums.WeightClass;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 bar one:
  l- d_dom_7
 c- c_dom_7

 options :  A

 bar two:
 l - a_maj
        b_maj
 c -  f#_min

 options :  A,c

 bar three:

 l - c#_maj
 c - g_maj

 options :  C

 bar four:

 l - c_maj, d_maj
 c -  a_maj

 options :  D, B

 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Cacheable(true)
@Table(name = "chords")
public class HiveChord extends ChordBase implements IWeightable, IInfluenceable, Serializable {

    public HiveChord(){}

    public HiveChord(String chordName){
        setChordName(chordName);
    }

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "chord_name")
    private String chordName;

//    @Column(name = "chord_catagory")
//    private String chordCatagory;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="chord_catagory")
    private ChordCategory chordCatagory;

    @Transient
    private WeightClass harmonicWeight;

    @Transient
    private WeightClass rhythmicWeight;

    @Transient
    private WeightClass eqWeight;

    @Transient
    private WeightClass verticalWeight;

    @Transient
    private WeightClass timeseriesWeight;

    @Transient
    private WeightClass themeWeight;

    public boolean isEqual(HiveChord chord){

        if(chord == null){
            return false;
        }

        return chord.getChordName().equalsIgnoreCase(this.getChordName());
    }

    public boolean isEqual(String chordName){
        HiveChord chord = new HiveChord(chordName);
        return chord.isEqual(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public void setFileName(String fileName) {

    }

    @Override
    public String getFileName() {
        return null;
    }

    public String getChordName(){

        String name = this.chordName;

        if(this.chordName.equalsIgnoreCase("_") || this.chordName.equalsIgnoreCase("")){
            return normalizeNotesToSharps(this.chordName);
        }

        try {
            if ((name.charAt(1) + "").equalsIgnoreCase("3") ||
                    (name.charAt(1) + "").equalsIgnoreCase("4") ||
                    (name.charAt(1) + "").equalsIgnoreCase("5")) {
                StringBuilder sb = new StringBuilder(this.chordName);
                name = sb.deleteCharAt(1).toString();
            }

            if (name.length() > 2) {
                if ((name.charAt(2) + "").equalsIgnoreCase("3") ||
                        (name.charAt(2) + "").equalsIgnoreCase("4") ||
                        (name.charAt(2) + "").equalsIgnoreCase("5")) {
                    StringBuilder sb = new StringBuilder(this.chordName);
                    name = sb.deleteCharAt(2).toString();
                }
            }
        }catch (Exception e){
            System.out.println("THIS IS THE PROBLEM CHORD: " + name);
            throw e;
        }


        return normalizeNotesToSharps(name);
    }

    public String getRawChordName(){
        return this.chordName;
    }

    public void setChordName(String chordName){
        this.chordName = normalizeNotesToSharps(chordName);
    }

    @Transient
    List<String> chordTypes = Arrays.asList("maj","min","dim","aug","dom","sus");

    public String extractRoot(String chordName){

        for(String type:chordTypes){
            if(chordName.contains(type)){
                String[] split = chordName.split(type);
                return split[0]
                        .replace("3", "")
                        .replace("4", "")
                        .replace("5", "");
            }
        }

        throw new RuntimeException("Could not extract chord root!!");
    }

    public boolean isEqualOrTriadMatch(HiveChord harmonicLoopChord) {

        if(this.getChordName() == null || this.getChordName().length() == 0 || this.getChordName().equalsIgnoreCase("_")){
            return false;
        }

        if(harmonicLoopChord.isEqual(this)){
            return true;
        }

        //root must be the same
        if (!extractRoot(this.getChordName()).equalsIgnoreCase(extractRoot(harmonicLoopChord.getChordName()))) {
            return false;
        }

        //DIM/MIN7B5
        if(harmonicLoopChord.getChordName().toLowerCase().contains("dim") && !harmonicLoopChord.getChordName().toLowerCase().contains("7") && !harmonicLoopChord.getChordName().toLowerCase().contains("9")){
            return this.getChordName().toLowerCase().contains("min7b5");
        }

        if(this.getChordName().toLowerCase().contains("dim") && !this.getChordName().toLowerCase().contains("7") && !this.getChordName().toLowerCase().contains("9")){
            return harmonicLoopChord.getChordName().toLowerCase().contains("min7b5");
        }

        //MIN/MIN7
        if(harmonicLoopChord.getChordName().toLowerCase().contains("min") && !harmonicLoopChord.getChordName().toLowerCase().contains("6") && !harmonicLoopChord.getChordName().toLowerCase().contains("7") && !harmonicLoopChord.getChordName().toLowerCase().contains("9")){
            return this.getChordName().toLowerCase().contains("min7") || this.getChordName().toLowerCase().contains("min6");
        }

        if(this.getChordName().toLowerCase().contains("min") && !this.getChordName().toLowerCase().contains("6") && !this.getChordName().toLowerCase().contains("7") && !this.getChordName().toLowerCase().contains("9")){
            return harmonicLoopChord.getChordName().toLowerCase().contains("min7") || harmonicLoopChord.getChordName().toLowerCase().contains("min6");
        }

        //MAJ/MAJ7
        if(harmonicLoopChord.getChordName().toLowerCase().contains("maj") && !harmonicLoopChord.getChordName().toLowerCase().contains("6") && !harmonicLoopChord.getChordName().toLowerCase().contains("7") && !harmonicLoopChord.getChordName().toLowerCase().contains("9")){
            return this.getChordName().toLowerCase().contains("maj7") || this.getChordName().toLowerCase().contains("maj6") || this.getChordName().toLowerCase().contains("dom");
        }

        if(this.getChordName().toLowerCase().contains("maj") && !this.getChordName().toLowerCase().contains("6") && !this.getChordName().toLowerCase().contains("7") && !this.getChordName().toLowerCase().contains("9")){
            return harmonicLoopChord.getChordName().toLowerCase().contains("maj7") || harmonicLoopChord.getChordName().toLowerCase().contains("maj6") || harmonicLoopChord.getChordName().toLowerCase().contains("dom");
        }

        return false;
    }

    public boolean hasMatchingRoot(HiveChord chord) {

        //1
        if ((chord != null) && this.getChordName().toLowerCase().contains(chord.getChordName().toLowerCase())) {
            return true;
        }

        if ((chord != null) && chord.getChordName().toLowerCase().contains(this.getChordName().toLowerCase())) {
            return true;
        }

        if ((chord != null) && chord.getChordName().equalsIgnoreCase(this.getChordName().toLowerCase())) {
            return true;
        }

        if ((chord != null) && isEqualOrTriadMatch(chord)) {
            return true;
        }

        return false;
    }

    public boolean hasMatchingRoot(HarmonicLoop harmonicLoop) {

        //1
        if ((harmonicLoop.getRootOne() != null) && this.getChordName().toLowerCase().contains(harmonicLoop.getRootOne().getChordName().toLowerCase())) {
            return true;
        }

        if ((harmonicLoop.getRootOne() != null) && harmonicLoop.getRootOne().getChordName().toLowerCase().contains(this.getChordName().toLowerCase())) {
            return true;
        }

        if ((harmonicLoop.getRootOne() != null) && harmonicLoop.getRootOne().getChordName().equalsIgnoreCase(this.getChordName().toLowerCase())) {
            return true;
        }

        if ((harmonicLoop.getRootOne() != null) && isEqualOrTriadMatch(harmonicLoop.getRootOne())) {
            return true;
        }

        //2
        if ((harmonicLoop.getRootTwo() != null) && this.getChordName().toLowerCase().contains(harmonicLoop.getRootTwo().getChordName().toLowerCase())) {
            return true;
        }

        if ((harmonicLoop.getRootTwo() != null) && harmonicLoop.getRootTwo().getChordName().toLowerCase().contains(this.getChordName().toLowerCase())) {
            return true;
        }

        if ((harmonicLoop.getRootTwo() != null) && harmonicLoop.getRootTwo().getChordName().equalsIgnoreCase(this.getChordName().toLowerCase())) {
            return true;
        }

        if ((harmonicLoop.getRootTwo() != null) && isEqualOrTriadMatch(harmonicLoop.getRootTwo())) {
            return true;
        }

        //3
        if ((harmonicLoop.getRootThree() != null) && this.getChordName().toLowerCase().contains(harmonicLoop.getRootThree().getChordName().toLowerCase())) {
            return true;
        }

        if ((harmonicLoop.getRootThree() != null) && harmonicLoop.getRootThree().getChordName().toLowerCase().contains(this.getChordName().toLowerCase())) {
            return true;
        }

        if ((harmonicLoop.getRootThree() != null) && harmonicLoop.getRootThree().getChordName().equalsIgnoreCase(this.getChordName().toLowerCase())) {
            return true;
        }

        if ((harmonicLoop.getRootThree() != null) && isEqualOrTriadMatch(harmonicLoop.getRootThree())) {
            return true;
        }

        //4
        if ((harmonicLoop.getRootFour() != null) && this.getChordName().toLowerCase().contains(harmonicLoop.getRootFour().getChordName().toLowerCase())) {
            return true;
        }

        if ((harmonicLoop.getRootFour() != null) && harmonicLoop.getRootFour().getChordName().toLowerCase().contains(this.getChordName().toLowerCase())) {
            return true;
        }

        if ((harmonicLoop.getRootFour() != null) && harmonicLoop.getRootFour().getChordName().equalsIgnoreCase(this.getChordName().toLowerCase())) {
            return true;
        }

        if ((harmonicLoop.getRootFour() != null) && isEqualOrTriadMatch(harmonicLoop.getRootFour())) {
            return true;
        }

        return false;
    }

    @Override
    public WeightClass getVerticalWeight() {
        return verticalWeight;
    }

    @Override
    public void setVerticalWeight( WeightClass weight) {
        verticalWeight = weight;
    }


    @Override
    public WeightClass getTimeseriesWeight() {
        return timeseriesWeight;
    }

    @Override
    public void setTimeseriesWeight(WeightClass weight) {
        timeseriesWeight = weight;
    }

    @Override
    public WeightClass getThemeWeight() {
        return themeWeight;
    }

    @Override
    public void setThemeWeight(WeightClass weight) {
        themeWeight = weight;
    }

    @Override
    public void setRhythmicWeight( WeightClass weight) {
        rhythmicWeight = weight;
    }

    @Override
    public WeightClass getRhythmicWeight() {
        return rhythmicWeight;
    }

    @Override
    public WeightClass getEqWeight() {
        return eqWeight;
    }

    @Override
    public void setEqWeight(WeightClass weight) {
        eqWeight = weight;
    }

    @Override
    public Integer getTotalWeight() {

        return TotalWeight.calculateTotal(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    public ChordCategory getChordCatagory() {
        return chordCatagory;
    }

    public void setChordCatagory(ChordCategory chordCatagory) {
        this.chordCatagory = chordCatagory;
    }

    public WeightClass getHarmonicWeight() {
        return harmonicWeight;
    }

    public void setHarmonicWeight(WeightClass harmonicWeight) {
        this.harmonicWeight = harmonicWeight;
    }

    public List<String> getChordTypes() {
        return chordTypes;
    }

    public void setChordTypes(List<String> chordTypes) {
        this.chordTypes = chordTypes;
    }
}
