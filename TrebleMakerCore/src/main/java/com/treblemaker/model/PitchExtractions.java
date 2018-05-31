package com.treblemaker.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "pitch_extractions")
public class PitchExtractions implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "one_one")
    private String oneOne = "";

    @Column(name = "one_two")
    private String oneTwo = "";

    @Column(name = "one_three")
    private String oneThree = "";

    @Column(name = "one_four")
    private String oneFour = "";

    @Column(name = "two_one")
    private String twoOne = "";

    @Column(name = "two_two")
    private String twoTwo = "";

    @Column(name = "two_three")
    private String twoThree = "";

    @Column(name = "two_four")
    private String twoFour = "";

    @Column(name = "three_one")
    private String threeOne = "";

    @Column(name = "three_two")
    private String threeTwo = "";

    @Column(name = "three_three")
    private String threeThree = "";

    @Column(name = "three_four")
    private String threeFour = "";

    @Column(name = "four_one")
    private String fourOne = "";

    @Column(name = "four_two")
    private String fourTwo = "";

    @Column(name = "four_three")
    private String fourThree = "";

    @Column(name = "four_four")
    private String fourFour = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getOneOne() {
        return oneOne.split("-");
    }

    public void setOneOne(String oneOne) {
        this.oneOne = oneOne;
    }

    public String[] getOneTwo() {
        return oneTwo.split("-");
    }

    public void setOneTwo(String oneTwo) {
        this.oneTwo = oneTwo;
    }

    public String[] getOneThree() {
        return oneThree.split("-");
    }

    public void setOneThree(String oneThree) {
        this.oneThree = oneThree;
    }

    public String[] getOneFour() {
        return oneFour.split("-");
    }

    public void setOneFour(String oneFour) {
        this.oneFour = oneFour;
    }

    public String[] getTwoOne() {
        return twoOne.split("-");
    }

    public void setTwoOne(String twoOne) {
        this.twoOne = twoOne;
    }

    public String[] getTwoTwo() {
        return twoTwo.split("-");
    }

    public void setTwoTwo(String twoTwo) {
        this.twoTwo = twoTwo;
    }

    public String[] getTwoThree() {
        return twoThree.split("-");
    }

    public void setTwoThree(String twoThree) {
        this.twoThree = twoThree;
    }

    public String[] getTwoFour() {
        return twoFour.split("-");
    }

    public void setTwoFour(String twoFour) {
        this.twoFour = twoFour;
    }

    public String[] getThreeOne() {
        return threeOne.split("-");
    }

    public void setThreeOne(String threeOne) {
        this.threeOne = threeOne;
    }

    public String[] getThreeTwo() {
        return threeTwo.split("-");
    }

    public void setThreeTwo(String threeTwo) {
        this.threeTwo = threeTwo;
    }

    public String[] getThreeThree() {
        return threeThree.split("-");
    }

    public void setThreeThree(String threeThree) {
        this.threeThree = threeThree;
    }

    public String[] getThreeFour() {
        return threeFour.split("-");
    }

    public void setThreeFour(String threeFour) {
        this.threeFour = threeFour;
    }

    public String[] getFourOne() {
        return fourOne.split("-");
    }

    public void setFourOne(String fourOne) {
        this.fourOne = fourOne;
    }

    public String[] getFourTwo() {
        return fourTwo.split("-");
    }

    public void setFourTwo(String fourTwo) {
        this.fourTwo = fourTwo;
    }

    public String[] getFourThree() {
        return fourThree.split("-");
    }

    public void setFourThree(String fourThree) {
        this.fourThree = fourThree;
    }

    public String[] getFourFour() {
        return fourFour.split("-");
    }

    public void setFourFour(String fourFour) {
        this.fourFour = fourFour;
    }

//    @Transient
//    public void setFromArray(String[] pitchArray) {
//        setOneOne(pitchArray[0].toString());
//        setOneTwo(pitchArray[1].toString());
//        setOneThree(pitchArray[2].toString());
//        setOneFour(pitchArray[3].toString());
//        setTwoOne(pitchArray[4].toString());
//        setTwoTwo(pitchArray[5].toString());
//        setTwoThree(pitchArray[6].toString());
//        setTwoFour(pitchArray[7].toString());
//        setThreeOne(pitchArray[8].toString());
//        setThreeTwo(pitchArray[9].toString());
//        setThreeThree(pitchArray[10].toString());
//        setThreeFour(pitchArray[11].toString());
//        setFourOne(pitchArray[12].toString());
//        setFourTwo(pitchArray[13].toString());
//        setFourThree(pitchArray[14].toString());
//        setFourFour(pitchArray[15].toString());
//    }

    @Transient
    public List<String[]> getAsList() {

        List<String[]> output = Arrays.asList(
                this.getOneOne(),
                this.getOneTwo(),
                this.getOneThree(),
                this.getOneFour(),
                this.getTwoOne(),
                this.getTwoTwo(),
                this.getTwoThree(),
                this.getTwoFour(),
                this.getThreeOne(),
                this.getThreeTwo(),
                this.getThreeThree(),
                this.getThreeFour(),
                this.getFourOne(),
                this.getFourTwo(),
                this.getFourThree(),
                this.getFourFour()
        );

        return output;
    }
}
