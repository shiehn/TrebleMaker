package com.treblemaker.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rhythmic_accents")
public class RhythmicAccents implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "one_one")
    private String oneOne = "0";

    @Column(name = "one_two")
    private String oneTwo = "0";

    @Column(name = "one_three")
    private String oneThree = "0";

    @Column(name = "one_four")
    private String oneFour = "0";

    @Column(name = "two_one")
    private String twoOne = "0";

    @Column(name = "two_two")
    private String twoTwo = "0";

    @Column(name = "two_three")
    private String twoThree = "0";

    @Column(name = "two_four")
    private String twoFour = "0";

    @Column(name = "three_one")
    private String threeOne = "0";

    @Column(name = "three_two")
    private String threeTwo = "0";

    @Column(name = "three_three")
    private String threeThree = "0";

    @Column(name = "three_four")
    private String threeFour = "0";

    @Column(name = "four_one")
    private String fourOne = "0";

    @Column(name = "four_two")
    private String fourTwo = "0";

    @Column(name = "four_three")
    private String fourThree = "0";

    @Column(name = "four_four")
    private String fourFour = "0";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOneOne() {
        return oneOne;
    }

    public void setOneOne(String oneOne) {
        this.oneOne = oneOne;
    }

    public String getOneTwo() {
        return oneTwo;
    }

    public void setOneTwo(String oneTwo) {
        this.oneTwo = oneTwo;
    }

    public String getOneThree() {
        return oneThree;
    }

    public void setOneThree(String oneThree) {
        this.oneThree = oneThree;
    }

    public String getOneFour() {
        return oneFour;
    }

    public void setOneFour(String oneFour) {
        this.oneFour = oneFour;
    }

    public String getTwoOne() {
        return twoOne;
    }

    public void setTwoOne(String twoOne) {
        this.twoOne = twoOne;
    }

    public String getTwoTwo() {
        return twoTwo;
    }

    public void setTwoTwo(String twoTwo) {
        this.twoTwo = twoTwo;
    }

    public String getTwoThree() {
        return twoThree;
    }

    public void setTwoThree(String twoThree) {
        this.twoThree = twoThree;
    }

    public String getTwoFour() {
        return twoFour;
    }

    public void setTwoFour(String twoFour) {
        this.twoFour = twoFour;
    }

    public String getThreeOne() {
        return threeOne;
    }

    public void setThreeOne(String threeOne) {
        this.threeOne = threeOne;
    }

    public String getThreeTwo() {
        return threeTwo;
    }

    public void setThreeTwo(String threeTwo) {
        this.threeTwo = threeTwo;
    }

    public String getThreeThree() {
        return threeThree;
    }

    public void setThreeThree(String threeThree) {
        this.threeThree = threeThree;
    }

    public String getThreeFour() {
        return threeFour;
    }

    public void setThreeFour(String threeFour) {
        this.threeFour = threeFour;
    }

    public String getFourOne() {
        return fourOne;
    }

    public void setFourOne(String fourOne) {
        this.fourOne = fourOne;
    }

    public String getFourTwo() {
        return fourTwo;
    }

    public void setFourTwo(String fourTwo) {
        this.fourTwo = fourTwo;
    }

    public String getFourThree() {
        return fourThree;
    }

    public void setFourThree(String fourThree) {
        this.fourThree = fourThree;
    }

    public String getFourFour() {
        return fourFour;
    }

    public void setFourFour(String fourFour) {
        this.fourFour = fourFour;
    }

    @Transient
    public void setFromArray(Integer[] intArray) {
        setOneOne(intArray[0].toString());
        setOneTwo(intArray[1].toString());
        setOneThree(intArray[2].toString());
        setOneFour(intArray[3].toString());
        setTwoOne(intArray[4].toString());
        setTwoTwo(intArray[5].toString());
        setTwoThree(intArray[6].toString());
        setTwoFour(intArray[7].toString());
        setThreeOne(intArray[8].toString());
        setThreeTwo(intArray[9].toString());
        setThreeThree(intArray[10].toString());
        setThreeFour(intArray[11].toString());
        setFourOne(intArray[12].toString());
        setFourTwo(intArray[13].toString());
        setFourThree(intArray[14].toString());
        setFourFour(intArray[15].toString());
    }

    public int[] getAsIntegerArray() {

        int[] output = new int[]{
                Integer.parseInt(getOneOne()),
                Integer.parseInt(getOneTwo()),
                Integer.parseInt(getOneThree()),
                Integer.parseInt(getOneFour()),
                Integer.parseInt(getTwoOne()),
                Integer.parseInt(getTwoTwo()),
                Integer.parseInt(getTwoThree()),
                Integer.parseInt(getTwoFour()),
                Integer.parseInt(getThreeOne()),
                Integer.parseInt(getThreeTwo()),
                Integer.parseInt(getThreeThree()),
                Integer.parseInt(getThreeFour()),
                Integer.parseInt(getFourOne()),
                Integer.parseInt(getFourTwo()),
                Integer.parseInt(getFourThree()),
                Integer.parseInt(getFourFour())
        };

        return output;
    }

    public int getActiveCount() {

        int activeCount = 0;

        for (int activeStatus : getAsIntegerArray()) {
            if (activeStatus == 1) {
                activeCount = activeCount + 1;
            }
        }

        return activeCount;
    }
}
