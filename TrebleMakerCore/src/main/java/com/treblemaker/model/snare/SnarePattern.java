package com.treblemaker.model.snare;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "generated_snare_patterns")
public class SnarePattern implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "one_one")
    private int oneOne = 0;

    @Column(name = "one_two")
    private int oneTwo = 0;

    @Column(name = "one_three")
    private int oneThree = 0;

    @Column(name = "one_four")
    private int oneFour = 0;

    @Column(name = "two_one")
    private int twoOne = 0;

    @Column(name = "two_two")
    private int twoTwo = 0;

    @Column(name = "two_three")
    private int twoThree = 0;

    @Column(name = "two_four")
    private int twoFour = 0;

    @Column(name = "three_one")
    private int threeOne = 0;

    @Column(name = "three_two")
    private int threeTwo = 0;

    @Column(name = "three_three")
    private int threeThree = 0;

    @Column(name = "three_four")
    private int threeFour = 0;

    @Column(name = "four_one")
    private int fourOne = 0;

    @Column(name = "four_two")
    private int fourTwo = 0;

    @Column(name = "four_three")
    private int fourThree = 0;

    @Column(name = "four_four")
    private int fourFour = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getOneOne() {
        return oneOne;
    }

    public void setOneOne(int oneOne) {
        this.oneOne = oneOne;
    }

    public int getOneTwo() {
        return oneTwo;
    }

    public void setOneTwo(int oneTwo) {
        this.oneTwo = oneTwo;
    }

    public int getOneThree() {
        return oneThree;
    }

    public void setOneThree(int oneThree) {
        this.oneThree = oneThree;
    }

    public int getOneFour() {
        return oneFour;
    }

    public void setOneFour(int oneFour) {
        this.oneFour = oneFour;
    }

    public int getTwoOne() {
        return twoOne;
    }

    public void setTwoOne(int twoOne) {
        this.twoOne = twoOne;
    }

    public int getTwoTwo() {
        return twoTwo;
    }

    public void setTwoTwo(int twoTwo) {
        this.twoTwo = twoTwo;
    }

    public int getTwoThree() {
        return twoThree;
    }

    public void setTwoThree(int twoThree) {
        this.twoThree = twoThree;
    }

    public int getTwoFour() {
        return twoFour;
    }

    public void setTwoFour(int twoFour) {
        this.twoFour = twoFour;
    }

    public int getThreeOne() {
        return threeOne;
    }

    public void setThreeOne(int threeOne) {
        this.threeOne = threeOne;
    }

    public int getThreeTwo() {
        return threeTwo;
    }

    public void setThreeTwo(int threeTwo) {
        this.threeTwo = threeTwo;
    }

    public int getThreeThree() {
        return threeThree;
    }

    public void setThreeThree(int threeThree) {
        this.threeThree = threeThree;
    }

    public int getThreeFour() {
        return threeFour;
    }

    public void setThreeFour(int threeFour) {
        this.threeFour = threeFour;
    }

    public int getFourOne() {
        return fourOne;
    }

    public void setFourOne(int fourOne) {
        this.fourOne = fourOne;
    }

    public int getFourTwo() {
        return fourTwo;
    }

    public void setFourTwo(int fourTwo) {
        this.fourTwo = fourTwo;
    }

    public int getFourThree() {
        return fourThree;
    }

    public void setFourThree(int fourThree) {
        this.fourThree = fourThree;
    }

    public int getFourFour() {
        return fourFour;
    }

    public void setFourFour(int fourFour) {
        this.fourFour = fourFour;
    }

    @Transient
    public void setFromArray(int[] intArray) {
        setOneOne(intArray[0]);
        setOneTwo(intArray[1]);
        setOneThree(intArray[2]);
        setOneFour(intArray[3]);
        setTwoOne(intArray[4]);
        setTwoTwo(intArray[5]);
        setTwoThree(intArray[6]);
        setTwoFour(intArray[7]);
        setThreeOne(intArray[8]);
        setThreeTwo(intArray[9]);
        setThreeThree(intArray[10]);
        setThreeFour(intArray[11]);
        setFourOne(intArray[12]);
        setFourTwo(intArray[13]);
        setFourThree(intArray[14]);
        setFourFour(intArray[15]);
    }

    @Transient
    public int[] getAsIntegerArray() {

        int[] output = new int[]{
                getOneOne(),
                getOneTwo(),
                getOneThree(),
                getOneFour(),
                getTwoOne(),
                getTwoTwo(),
                getTwoThree(),
                getTwoFour(),
                getThreeOne(),
                getThreeTwo(),
                getThreeThree(),
                getThreeFour(),
                getFourOne(),
                getFourTwo(),
                getFourThree(),
                getFourFour()
        };

        return output;
    }
}
