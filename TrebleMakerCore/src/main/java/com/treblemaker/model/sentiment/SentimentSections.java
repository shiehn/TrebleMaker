package com.treblemaker.model.sentiment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SentimentSections implements Serializable {
    private List<SentimentSection> sentimentSectionList = new ArrayList<>();

    public List<SentimentSection> getSentimentSectionList() {
        return sentimentSectionList;
    }

    public void setSentimentSectionList(List<SentimentSection> sentimentSectionList) {
        this.sentimentSectionList = sentimentSectionList;
    }

    public void addSentimentSection(SentimentSection sentimentSection){
        this.sentimentSectionList.add(sentimentSection);
    }
}
