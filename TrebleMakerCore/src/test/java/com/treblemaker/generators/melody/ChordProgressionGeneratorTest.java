package com.treblemaker.generators.melody;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.generators.ChordProgressionGenerator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class ChordProgressionGeneratorTest {
    ChordProgressionGenerator chordProgressionGenerator;

    @Value("${tm.app.root}")
    String appRoot;

    @Before
    public void setup(){
        chordProgressionGenerator = new ChordProgressionGenerator(appRoot);
    }

    @Test
    public void returnsArrayOfLegitChords(){

        String key = "31";
        String chordOne = "313";
        String chordTwo = "313";

        List<List<String>> progressionList = chordProgressionGenerator.getChords(key, chordOne, chordTwo);

        assertThat(progressionList).isNotEmpty();

        for(List<String> progressions : progressionList){
            System.out.println(progressions);
            assertThat(progressions).hasSize(4);
            for(String chord : progressions){
                assertThat(chord).isNotEmpty();
                assertThat(isNumeric(chord)).isFalse();
            }
        }
    }

    @Test
    public void shouldFormatPrefix(){
        String key = "31";
        String chordOne = "413";
        String chordTwo = "513";

        String prefix = chordProgressionGenerator.formatPrefix(key,chordOne,chordTwo);

        assertThat(prefix).isEqualToIgnoringCase("#31:413-513%");
    }

    private boolean isNumeric(String input){
        return input.matches("[0-9]+");
    }
}