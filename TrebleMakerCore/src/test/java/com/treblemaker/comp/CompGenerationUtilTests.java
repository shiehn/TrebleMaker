package com.treblemaker.comp;

import com.treblemaker.Application;
import com.treblemaker.SpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class CompGenerationUtilTests {

    @Test
    public void shouldGenerateCorrectInitialLength() {

        int numOfTests = 1000;

        for (int i=0; i<numOfTests; i++) {
            String[] compPositions = CompGenerationUtil.generateInitialCompPositions();
            assertThat(compPositions.length).isEqualTo(32);
        }
    }

    @Test
    public void shouldAddToCorrectDuration(){

        int numOfTests = 1000;

        for (int i=0; i<numOfTests; i++) {
            String[] compPositions = CompGenerationUtil.generateInitialCompPositionsWithDurations();
            String compSequence = CompGenerationUtil.compPositionsToJFugueSequence(compPositions, "A", "B");
            String[] elements = compSequence.split(" ");

            int totalDuration = 0;
            for (String element : elements) {
                totalDuration = totalDuration + CompGenerationUtil.getDurationFromJFugue(element);
            }

            Application.logger.debug("LOG: duration check: #" + i);
            assertThat(totalDuration).isEqualTo(32);
        }
    }

    @Test
    public void shouldAssignChordsToCorrectBar(){

        int numOfTests = 1000;

        for (int i=0; i<numOfTests; i++) {
            String[] compPositions = CompGenerationUtil.generateInitialCompPositionsWithDurations();
            String compSequence = CompGenerationUtil.compPositionsToJFugueSequence(compPositions, "A", "B");
            String[] elements = compSequence.split(" ");

            Application.logger.debug("LOG: LB");
            Application.logger.debug("LOG: jfugueStringSequence : " + compSequence);
            Application.logger.debug("LOG: elements size : " + elements.length);

            int totalDuration = 0;
            for (String element : elements) {

                if(totalDuration<16){
                    assertThat(element).doesNotContain("B");
                }

                if(totalDuration>15){
                    assertThat(element).doesNotContain("A");
                }

                totalDuration = totalDuration + CompGenerationUtil.getDurationFromJFugue(element);
            }

            Application.logger.debug("LOG:","chord check: #" + i);
            assertThat(totalDuration).isEqualTo(32);
        }
    }

//    @Test
//    public void bitwiseTest(){
//
//        int one = 1;
//        int two = 2;
//
//        boolean output = false;
//
//        if(one == 1 & two == 3){
//            output = true;
//        }
//
//        assertThat(output).isTrue();
//    }

    @Test
    public void shouldNeverHaveChordsBleedIntoNextBar(){

        int numOfTests = 1000;

        for (int i=0; i<numOfTests; i++) {
            String[] compPositions = CompGenerationUtil.generateInitialCompPositionsWithDurations();
            String compSequence = CompGenerationUtil.compPositionsToJFugueSequence(compPositions, "A", "B");
            String[] elements = compSequence.split(" ");

            Application.logger.debug("LOG:","LB");
            Application.logger.debug("LOG:","jfugueStringSequence : " + compSequence);
            Application.logger.debug("LOG:","elements size : " + elements.length);

            int totalDuration = 0;
            int currentRemainder = 0;
            for (String element : elements) {

                if(totalDuration < 16){
                    currentRemainder = 16-totalDuration;
                }else{
                    currentRemainder = 32-totalDuration;
                }

                Application.logger.debug("LOG:","currentRemainder: " + currentRemainder);

                int durationOfElement = CompGenerationUtil.getDurationFromJFugue(element);
                assertThat(durationOfElement).isLessThanOrEqualTo(currentRemainder);

                totalDuration = totalDuration + durationOfElement;
            }

            Application.logger.debug("LOG:","chord check: #" + i);
        }
    }
}
