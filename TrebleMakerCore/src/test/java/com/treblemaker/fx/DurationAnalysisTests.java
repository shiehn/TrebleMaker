package com.treblemaker.fx;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.fx.util.DurationAnalysis;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class DurationAnalysisTests {

    @Autowired
    private DurationAnalysis durationAnalysis;

    @Test
    public void extractCorrectDurationsOne(){
        //{"arpeggio":[0,2,3,1,1,2,0,0,0,0,0,0,0,0,0,0,0,2,3,1,1,2,0,0,0,0,0,0,0,0,0,0]}

        int[] arpeggioArray = new int[]{0,2,3,1,1,2,0,0,0,0,0,0,0,0,0,0,0,2,3,1,1,2,0,0,0,0,0,0,0,0,0,0};

        assertThat(durationAnalysis.extractSixteenthFrequency(arpeggioArray)).isEqualTo(0.3); //8
        assertThat(durationAnalysis.extractEightFrequency(arpeggioArray)).isEqualTo(0.0); //0
        assertThat(durationAnalysis.extractQuarterFrequency(arpeggioArray)).isEqualTo(0.0); //0
        assertThat(durationAnalysis.extractDottedQuarterFrequency(arpeggioArray)).isEqualTo(0.0); //0
        assertThat(durationAnalysis.extractHalfFrequency(arpeggioArray)).isEqualTo(0.0); //0
    }

    @Test
    public void extractCorrectDurationsTwo(){
//        {"arpeggio":[5,1,1,1,0,1,0,0,0,0,0,0,0,0,0,0,5,1,1,1,0,1,0,0,0,0,0,0,0,0,0,0]}

        int[] arpeggioArray = new int[]{5,1,1,1,0,1,0,0,0,0,0,0,0,0,0,0,5,1,1,1,0,1,0,0,0,0,0,0,0,0,0,0};

        assertThat(durationAnalysis.extractSixteenthFrequency(arpeggioArray)).isEqualTo(0.2); //6
        assertThat(durationAnalysis.extractEightFrequency(arpeggioArray)).isEqualTo(0.1); //2
        assertThat(durationAnalysis.extractQuarterFrequency(arpeggioArray)).isEqualTo(0.0); //0
        assertThat(durationAnalysis.extractDottedQuarterFrequency(arpeggioArray)).isEqualTo(0.0); //0
        assertThat(durationAnalysis.extractHalfFrequency(arpeggioArray)).isEqualTo(0.0); //0
    }

    @Test
    public void extractCorrectDurationsThree(){
//        {"arpeggio":[0,0,0,0,5,0,0,0,0,3,1,0,0,5,0,1,0,0,0,0,5,0,0,0,0,3,1,0,0,5,0,1]}

        int[] arpeggioArray = new int[]{0,0,0,0,5,0,0,0,0,3,1,0,0,5,0,1,0,0,0,0,5,0,0,0,0,3,1,0,0,5,0,1};

        assertThat(durationAnalysis.extractSixteenthFrequency(arpeggioArray)).isEqualTo(0.1); //3
        assertThat(durationAnalysis.extractEightFrequency(arpeggioArray)).isEqualTo(0.1); //2
        assertThat(durationAnalysis.extractQuarterFrequency(arpeggioArray)).isEqualTo(0.0); //0
        assertThat(durationAnalysis.extractDottedQuarterFrequency(arpeggioArray)).isEqualTo(0.0); //0
        assertThat(durationAnalysis.extractHalfFrequency(arpeggioArray)).isEqualTo(0.0); //0
    }

    @Test
    public void extractCorrectDurationsFour(){
//        {"arpeggio":[3,5,1,0,0,2,0,1,0,3,0,5,3,0,0,0,3,5,1,0,0,2,0,1,0,3,0,5,3,0,0,0]}

        int[] arpeggioArray = new int[]{3,5,1,0,0,2,0,1,0,3,0,5,3,0,0,0,3,5,1,0,0,2,0,1,0,3,0,5,3,0,0,0};

        assertThat(durationAnalysis.extractSixteenthFrequency(arpeggioArray)).isEqualTo(0.2); //6
        assertThat(durationAnalysis.extractEightFrequency(arpeggioArray)).isEqualTo(0.4); //6
        assertThat(durationAnalysis.extractQuarterFrequency(arpeggioArray)).isEqualTo(0.3); //2
        assertThat(durationAnalysis.extractDottedQuarterFrequency(arpeggioArray)).isEqualTo(0.0); //0
        assertThat(durationAnalysis.extractHalfFrequency(arpeggioArray)).isEqualTo(0.0); //0
    }

    @Test
    public void extractCorrectDurationsFive(){
//        {"arpeggio":[0,0,3,2,1,0,0,0,5,0,3,2,0,0,2,0,0,0,3,2,1,0,0,0,5,0,3,2,0,0,2,0]}

        int[] arpeggioArray = new int[]{0,0,3,2,1,0,0,0,5,0,3,2,0,0,2,0,0,0,3,2,1,0,0,0,5,0,3,2,0,0,2,0};

        assertThat(durationAnalysis.extractSixteenthFrequency(arpeggioArray)).isEqualTo(0.2); //6
        assertThat(durationAnalysis.extractEightFrequency(arpeggioArray)).isEqualTo(0.2); //3
        assertThat(durationAnalysis.extractQuarterFrequency(arpeggioArray)).isEqualTo(0.4); //3
        assertThat(durationAnalysis.extractHalfFrequency(arpeggioArray)).isEqualTo(0.0); //0
    }

    @Test
    public void extractCorrectDurationsSix(){
//        {"arpeggio":[5,1,2,5,1,0,0,0,0,0,1,0,0,0,0,0,5,1,2,5,1,0,0,0,0,0,1,0,0,0,0,0]}

        int[] arpeggioArray = new int[]{5,1,2,5,1,0,0,0,0,0,1,0,0,0,0,0,5,1,2,5,1,0,0,0,0,0,1,0,0,0,0,0};

        assertThat(durationAnalysis.extractSixteenthFrequency(arpeggioArray)).isEqualTo(0.3); //8
        assertThat(durationAnalysis.extractEightFrequency(arpeggioArray)).isEqualTo(0.0); //0
        assertThat(durationAnalysis.extractQuarterFrequency(arpeggioArray)).isEqualTo(0.0); //3
        assertThat(durationAnalysis.extractDottedQuarterFrequency(arpeggioArray)).isEqualTo(0.8); //4
        assertThat(durationAnalysis.extractHalfFrequency(arpeggioArray)).isEqualTo(0.0); //0
    }

    @Test
    public void extractCorrectDurationsSeven(){
//        {"arpeggio":[2,1,2,3,0,0,0,0,0,0,0,0,0,0,0,0,2,1,2,3,0,0,0,0,0,0,0,0,0,0,0,0]}

        int[] arpeggioArray = new int[]{2,1,2,3,0,0,0,0,0,0,0,0,0,0,0,0,2,1,2,3,0,0,0,0,0,0,0,0,0,0,0,0};

        assertThat(durationAnalysis.extractSixteenthFrequency(arpeggioArray)).isEqualTo(0.2); //6
        assertThat(durationAnalysis.extractEightFrequency(arpeggioArray)).isEqualTo(0.0); //0
        assertThat(durationAnalysis.extractQuarterFrequency(arpeggioArray)).isEqualTo(0.0); //0
        assertThat(durationAnalysis.extractDottedQuarterFrequency(arpeggioArray)).isEqualTo(0.0); //0
        assertThat(durationAnalysis.extractHalfFrequency(arpeggioArray)).isEqualTo(0.0); //0
    }

    @Test
    public void extractCorrectDurationsEight(){
//        {"arpeggio":[5,3,0,0,0,0,0,0,0,3,4,0,0,0,0,0,5,3,0,0,0,0,0,0,0,3,1,0,0,0,0,0]}

        int[] arpeggioArray = new int[]{5,3,0,0,0,0,0,0,0,3,4,0,0,0,0,0,5,3,0,0,0,0,0,0,0,3,1,0,0,0,0,0};

        assertThat(durationAnalysis.extractSixteenthFrequency(arpeggioArray)).isEqualTo(0.1); //4
        assertThat(durationAnalysis.extractEightFrequency(arpeggioArray)).isEqualTo(0.0); //0
        assertThat(durationAnalysis.extractQuarterFrequency(arpeggioArray)).isEqualTo(0.0); //0
        assertThat(durationAnalysis.extractDottedQuarterFrequency(arpeggioArray)).isEqualTo(0.4); //2
        assertThat(durationAnalysis.extractHalfFrequency(arpeggioArray)).isEqualTo(0.5); //2
    }

    @Test
    public void extractCorrectDurationsNine(){
//        {"arpeggio":[0,0,0,0,0,0,2,2,0,4,5,2,1,2,0,2,0,0,0,0,0,0,2,2,0,4,5,2,1,2,0,2]}

        int[] arpeggioArray = new int[]{0,0,0,0,0,0,2,2,0,4,5,2,1,2,0,2,0,0,0,0,0,0,2,2,0,4,5,2,1,2,0,2};

        assertThat(durationAnalysis.extractSixteenthFrequency(arpeggioArray)).isEqualTo(0.3); //11
        assertThat(durationAnalysis.extractEightFrequency(arpeggioArray)).isEqualTo(0.3); //4
        assertThat(durationAnalysis.extractQuarterFrequency(arpeggioArray)).isEqualTo(0.0); //0
        assertThat(durationAnalysis.extractDottedQuarterFrequency(arpeggioArray)).isEqualTo(0.0); //0
        assertThat(durationAnalysis.extractHalfFrequency(arpeggioArray)).isEqualTo(0.0); //0
    }

    @Test
    public void extractCorrectDurationsTen(){
//        {"arpeggio":[0,0,0,0,1,1,3,0,1,0,0,5,1,0,0,0,0,0,0,0,1,1,3,0,1,0,0,5,1,0,0,0]}

        int[] arpeggioArray = new int[]{0,0,0,0,1,1,3,0,1,0,0,5,1,0,0,0,0,0,0,0,1,1,3,0,1,0,0,5,1,0,0,0};

        assertThat(durationAnalysis.extractSixteenthFrequency(arpeggioArray)).isEqualTo(0.2); //6
        assertThat(durationAnalysis.extractEightFrequency(arpeggioArray)).isEqualTo(0.1); //0
        assertThat(durationAnalysis.extractQuarterFrequency(arpeggioArray)).isEqualTo(0.1); //1
        assertThat(durationAnalysis.extractDottedQuarterFrequency(arpeggioArray)).isEqualTo(0.0); //0
        assertThat(durationAnalysis.extractHalfFrequency(arpeggioArray)).isEqualTo(0.3); //1
    }
}
