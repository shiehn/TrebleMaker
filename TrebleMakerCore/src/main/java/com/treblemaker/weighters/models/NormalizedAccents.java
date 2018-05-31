package com.treblemaker.weighters.models;

import com.treblemaker.model.RhythmicAccents;

import java.util.ArrayList;
import java.util.List;

public class NormalizedAccents {

    private List<RhythmicAccents> referenceAccentList;
    private List<RhythmicAccents> accentsList;

    private List<String> normalizedReferenceAccents;
    private List<String> normalizedAccents;

    public List<RhythmicAccents> getReferenceAccentList() {
        return referenceAccentList;
    }

    public void setReferenceAccentList(List<RhythmicAccents> referenceAccentList) {
        this.referenceAccentList = referenceAccentList;
    }

    public List<RhythmicAccents> getAccentsList() {
        return accentsList;
    }

    public void setAccentsList(List<RhythmicAccents> accentsList) {
        this.accentsList = accentsList;
    }

    public List<String> getNormalizedReferenceAccents() {

        normalizedReferenceAccents = new ArrayList<>();

        this.getReferenceAccentList().forEach(refAccents -> {

            normalizedReferenceAccents.add(refAccents.getOneOne());
            normalizedReferenceAccents.add(refAccents.getOneTwo());
            normalizedReferenceAccents.add(refAccents.getOneThree());
            normalizedReferenceAccents.add(refAccents.getOneFour());

            normalizedReferenceAccents.add(refAccents.getTwoOne());
            normalizedReferenceAccents.add(refAccents.getTwoTwo());
            normalizedReferenceAccents.add(refAccents.getTwoThree());
            normalizedReferenceAccents.add(refAccents.getTwoFour());

            normalizedReferenceAccents.add(refAccents.getThreeOne());
            normalizedReferenceAccents.add(refAccents.getThreeTwo());
            normalizedReferenceAccents.add(refAccents.getThreeThree());
            normalizedReferenceAccents.add(refAccents.getThreeFour());

            normalizedReferenceAccents.add(refAccents.getFourOne());
            normalizedReferenceAccents.add(refAccents.getFourTwo());
            normalizedReferenceAccents.add(refAccents.getFourThree());
            normalizedReferenceAccents.add(refAccents.getFourFour());
        });

        return normalizedReferenceAccents;
    }

    public void setNormalizedReferenceAccents(List<String> normalizedReferenceAccents) {
        this.normalizedReferenceAccents = normalizedReferenceAccents;
    }

    public List<String> getNormalizedAccents() {

        normalizedAccents = new ArrayList<>();

        this.getAccentsList().forEach(refAccents -> {

            normalizedAccents.add(refAccents.getOneOne());
            normalizedAccents.add(refAccents.getOneTwo());
            normalizedAccents.add(refAccents.getOneThree());
            normalizedAccents.add(refAccents.getOneFour());

            normalizedAccents.add(refAccents.getTwoOne());
            normalizedAccents.add(refAccents.getTwoTwo());
            normalizedAccents.add(refAccents.getTwoThree());
            normalizedAccents.add(refAccents.getTwoFour());

            normalizedAccents.add(refAccents.getThreeOne());
            normalizedAccents.add(refAccents.getThreeTwo());
            normalizedAccents.add(refAccents.getThreeThree());
            normalizedAccents.add(refAccents.getThreeFour());

            normalizedAccents.add(refAccents.getFourOne());
            normalizedAccents.add(refAccents.getFourTwo());
            normalizedAccents.add(refAccents.getFourThree());
            normalizedAccents.add(refAccents.getFourFour());
        });

        return normalizedAccents;
    }

    public void setNormalizedAccents(List<String> normalizedAccents) {
        this.normalizedAccents = normalizedAccents;
    }
}
