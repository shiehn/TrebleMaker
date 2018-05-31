package com.treblemaker.extractors.pitchextraction;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.PitchExtractions;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class PitchExtractionUtilTest extends TestCase {

    @Autowired
    PitchExtractionUtil pitchExtractionUtil;

    @Test
    public void shouldFindLoopsWherePitchExtractionAreNullOrEmpty(){

        HarmonicLoop harmonicLoopOne = new HarmonicLoop();
        harmonicLoopOne.setId(1);

        HarmonicLoop harmonicLoopTwo = new HarmonicLoop();
        harmonicLoopTwo.setId(2);
        harmonicLoopTwo.setPitchExtractions(Arrays.asList(new PitchExtractions()));

        HarmonicLoop harmonicLoopThree = new HarmonicLoop();
        harmonicLoopThree.setId(3);
        harmonicLoopThree.setPitchExtractions(null);

        List<HarmonicLoop> filteredLoops = pitchExtractionUtil.findLoopsWithoutPitchExtractions(Arrays.asList(harmonicLoopOne, harmonicLoopTwo, harmonicLoopThree));

        assertThat(filteredLoops).hasSize(1);
        assertThat(filteredLoops.get(0).getId()).isEqualTo(2);
    }

    @Test
    public void shouldExtractCorrectPitchesFromHarmonicLoops120() throws IOException, UnsupportedAudioFileException, InterruptedException {

        HarmonicLoop hlOneBarEightyBpm = new HarmonicLoop();
        hlOneBarEightyBpm.setId(1);
        hlOneBarEightyBpm.setBarCount(1);
        hlOneBarEightyBpm.setAudioLength(2.0f);
        hlOneBarEightyBpm.setBpm(120);
        hlOneBarEightyBpm.setFileName(Paths.get("src","main","java","com","treblemaker","tests","Mocks","pitchDetectionTest","pitch_test_1b_120.wav").toString());

        List<PitchExtractions> extractedPitchs = pitchExtractionUtil.extractPitchs(hlOneBarEightyBpm, new File(hlOneBarEightyBpm.getFileName()));

        int score = 0;

        score = updateScore(score, extractedPitchs.get(0).getOneOne(), "c", "");
        score = updateScore(score, extractedPitchs.get(0).getOneTwo(), "", "");
        score = updateScore(score, extractedPitchs.get(0).getOneThree(), "c", "");
        score = updateScore(score, extractedPitchs.get(0).getOneFour(), "c", "");

        score = updateScore(score, extractedPitchs.get(0).getTwoOne(), "c", "d");
        score = updateScore(score, extractedPitchs.get(0).getTwoTwo(), "", "");
        score = updateScore(score, extractedPitchs.get(0).getTwoThree(), "c", "");
        score = updateScore(score, extractedPitchs.get(0).getTwoFour(), "", "");

        score = updateScore(score, extractedPitchs.get(0).getThreeOne(), "cs", "d");
        score = updateScore(score, extractedPitchs.get(0).getThreeTwo(), "cs", "");
        score = updateScore(score, extractedPitchs.get(0).getThreeThree(), "cs", "ds");
        score = updateScore(score, extractedPitchs.get(0).getThreeFour(), "cs", "");

        score = updateScore(score, extractedPitchs.get(0).getFourOne(),   "f",   "e");
        score = updateScore(score, extractedPitchs.get(0).getFourTwo(),   "fs",   "e");
        score = updateScore(score, extractedPitchs.get(0).getFourThree(), "",   "");
        score = updateScore(score, extractedPitchs.get(0).getFourFour(),  "g",   "gs");

        System.out.println("FINAL SCORE *** " + score);
        System.out.println("FINAL SCORE *** " + score);
        System.out.println("FINAL SCORE *** " + score);
        System.out.println("FINAL SCORE *** " + score);

        assertThat(extractedPitchs).hasSize(1);
        assertThat(score).isGreaterThan(7);
    }

    @Test
    public void shouldExtractCorrectPitchesFromHarmonicLoops120_2bars() throws IOException, UnsupportedAudioFileException, InterruptedException {

        HarmonicLoop hlOneBarEightyBpm = new HarmonicLoop();
        hlOneBarEightyBpm.setId(1);
        hlOneBarEightyBpm.setBarCount(2);
        hlOneBarEightyBpm.setAudioLength(4.0f);
        hlOneBarEightyBpm.setBpm(120);
        hlOneBarEightyBpm.setFileName(Paths.get("src","main","java","com","treblemaker","tests","Mocks","pitchDetectionTest","pitch_test_2b_120.wav").toString());

        List<PitchExtractions> extractedPitchs = pitchExtractionUtil.extractPitchs(hlOneBarEightyBpm, new File(hlOneBarEightyBpm.getFileName()));

        assertThat(extractedPitchs).hasSize(2);
    }

    @Test
    public void shouldExtractCorrectPitchesFromHarmonicLoops120_4bars() throws IOException, UnsupportedAudioFileException, InterruptedException {

        HarmonicLoop hlOneBarEightyBpm = new HarmonicLoop();
        hlOneBarEightyBpm.setId(1);
        hlOneBarEightyBpm.setBarCount(4);
        hlOneBarEightyBpm.setAudioLength(8.0f);
        hlOneBarEightyBpm.setBpm(120);
        hlOneBarEightyBpm.setFileName(Paths.get("src","main","java","com","treblemaker","tests","Mocks","pitchDetectionTest","pitch_test_4b_120.wav").toString());

        List<PitchExtractions> extractedPitchs = pitchExtractionUtil.extractPitchs(hlOneBarEightyBpm, new File(hlOneBarEightyBpm.getFileName()));

        assertThat(extractedPitchs).hasSize(4);
    }

    @Test
    public void shouldExtractCorrectPitchesFromHarmonicLoops80() throws IOException, UnsupportedAudioFileException, InterruptedException {

        HarmonicLoop hlOneBarEightyBpm = new HarmonicLoop();
        hlOneBarEightyBpm.setId(1);
        hlOneBarEightyBpm.setBarCount(1);
        hlOneBarEightyBpm.setAudioLength(3.0f);
        hlOneBarEightyBpm.setBpm(80);
        hlOneBarEightyBpm.setFileName(Paths.get("src","main","java","com","treblemaker","tests","Mocks","pitchDetectionTest","pitch_test_1b_80.wav").toString());

        List<PitchExtractions> extractedPitchs = pitchExtractionUtil.extractPitchs(hlOneBarEightyBpm, new File(hlOneBarEightyBpm.getFileName()));

        int score = 0;

        score = updateScore(score, extractedPitchs.get(0).getOneOne(), "c", "");
        score = updateScore(score, extractedPitchs.get(0).getOneTwo(), "", "");
        score = updateScore(score, extractedPitchs.get(0).getOneThree(), "c", "");
        score = updateScore(score, extractedPitchs.get(0).getOneFour(), "c", "");

        score = updateScore(score, extractedPitchs.get(0).getTwoOne(), "c", "d");
        score = updateScore(score, extractedPitchs.get(0).getTwoTwo(), "", "");
        score = updateScore(score, extractedPitchs.get(0).getTwoThree(), "c", "");
        score = updateScore(score, extractedPitchs.get(0).getTwoFour(), "", "");

        score = updateScore(score, extractedPitchs.get(0).getThreeOne(), "cs", "d");
        score = updateScore(score, extractedPitchs.get(0).getThreeTwo(), "cs", "");
        score = updateScore(score, extractedPitchs.get(0).getThreeThree(), "cs", "ds");
        score = updateScore(score, extractedPitchs.get(0).getThreeFour(), "cs", "");

        score = updateScore(score, extractedPitchs.get(0).getFourOne(),   "f",   "e");
        score = updateScore(score, extractedPitchs.get(0).getFourTwo(),   "fs",   "e");
        score = updateScore(score, extractedPitchs.get(0).getFourThree(), "",   "");
        score = updateScore(score, extractedPitchs.get(0).getFourFour(),  "g",   "gs");

        System.out.println("FINAL SCORE *** " + score);
        System.out.println("FINAL SCORE *** " + score);
        System.out.println("FINAL SCORE *** " + score);
        System.out.println("FINAL SCORE *** " + score);

        assertThat(extractedPitchs).hasSize(1);
        assertThat(score).isGreaterThan(7);
    }

    @Test
    public void shouldExtractCorrectPitchesFromHarmonicLoops80_2bars() throws IOException, UnsupportedAudioFileException, InterruptedException {

        HarmonicLoop hlOneBarEightyBpm = new HarmonicLoop();
        hlOneBarEightyBpm.setId(1);
        hlOneBarEightyBpm.setBarCount(2);
        hlOneBarEightyBpm.setAudioLength(6.0f);
        hlOneBarEightyBpm.setBpm(80);
        hlOneBarEightyBpm.setFileName(Paths.get("src","main","java","com","treblemaker","tests","Mocks","pitchDetectionTest","pitch_test_2b_80.wav").toString());

        List<PitchExtractions> extractedPitchs = pitchExtractionUtil.extractPitchs(hlOneBarEightyBpm, new File(hlOneBarEightyBpm.getFileName()));

        assertThat(extractedPitchs).hasSize(2);
    }

    @Test
    public void shouldExtractCorrectPitchesFromHarmonicLoops90_4bars() throws IOException, UnsupportedAudioFileException, InterruptedException {

        HarmonicLoop hlOneBarEightyBpm = new HarmonicLoop();
        hlOneBarEightyBpm.setId(1);
        hlOneBarEightyBpm.setBarCount(4);
        hlOneBarEightyBpm.setAudioLength(12.0f);
        hlOneBarEightyBpm.setBpm(80);
        hlOneBarEightyBpm.setFileName(Paths.get("src","main","java","com","treblemaker","tests","Mocks","pitchDetectionTest","pitch_test_4b_80.wav").toString());

        List<PitchExtractions> extractedPitchs = pitchExtractionUtil.extractPitchs(hlOneBarEightyBpm, new File(hlOneBarEightyBpm.getFileName()));

        assertThat(extractedPitchs).hasSize(4);
    }

    public int updateScore(int currentScore, String[] notes, String targetA, String targetB){

        if(targetA == null || targetA.equalsIgnoreCase("")){
            return currentScore;
        }

        for(String ePitch : notes){
            if(!ePitch.equalsIgnoreCase("")&& !ePitch.equalsIgnoreCase(targetB)){
                if(ePitch.equalsIgnoreCase(targetA)){
                    currentScore++;
                }else{
                    currentScore--;
                }
            }
        }

        if(targetB == null || targetB.equalsIgnoreCase("")){
            return currentScore;
        }

        for(String ePitch : notes){
            if(!ePitch.equalsIgnoreCase("") && !ePitch.equalsIgnoreCase(targetA)){
                if(ePitch.equalsIgnoreCase(targetB)){
                    currentScore++;
                }else{
                    currentScore--;
                }
            }
        }

        return currentScore;
    }



}