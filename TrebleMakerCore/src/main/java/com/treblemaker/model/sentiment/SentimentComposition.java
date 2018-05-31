package com.treblemaker.model.sentiment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.treblemaker.Application;
import com.treblemaker.utils.json.JsonUtils;

import javax.persistence.*;
import java.io.IOException;

@Entity
@Table(name = "sentiment_composition")
public class SentimentComposition {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "composition_id")
    private String compositionId;

    @Column(name = "sentiment_json")
    private String sentimentJson;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(String compositionId) {
        this.compositionId = compositionId;
    }

    @Transient
    public SentimentSections getSentimentSections() {

        SentimentSections sentimentSections = null;

        try {
             sentimentSections = JsonUtils.mapper.readValue(this.sentimentJson, SentimentSections.class);
        } catch (IOException e) {
            Application.logger.debug("LOG:", e);
        }
        return sentimentSections;
    }

    @Transient
    public void setSentimentJson(SentimentSections sentimentSections) {
        try {
            this.sentimentJson = JsonUtils.mapper.writeValueAsString(sentimentSections);
        } catch (JsonProcessingException e) {
            Application.logger.debug("LOG:", e);
        }
    }
}
