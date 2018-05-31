package com.treblemaker.extractors.sentiment;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SentimentMixExtraction {

    public List<String> extract() {
        return Arrays.asList("bass","bright");
    }
}
