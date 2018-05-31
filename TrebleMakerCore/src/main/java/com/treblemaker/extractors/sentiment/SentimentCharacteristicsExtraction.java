package com.treblemaker.extractors.sentiment;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SentimentCharacteristicsExtraction {

    public List<String> extract() {
        return Arrays.asList("ambient","fast");
    }
}
