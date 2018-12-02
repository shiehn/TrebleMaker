package com.treblemaker.fx;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.dal.interfaces.IFXArpeggioDelayDal;
import com.treblemaker.model.SynthTemplate;
import com.treblemaker.model.fx.FXArpeggioDelay;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.utils.FileStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class FxTests {

    @Autowired
    IFXRenderer sfxRenderer;

    @Autowired
    IFXArpeggioDelayDal fxArpeggioDelayDal;

    final int BPM = 80;

    private final String inputAudio = Paths.get("src","main","resources","delaytest.wav").toString();
    private final String outputAudio = Paths.get("src","main","resources","delaytestoutput.wav").toString();

    private List<ProgressionUnit> progressionUnits;

    @Before
    public void setup(){
        List<SynthTemplate> synthTemplates = Arrays.asList(new SynthTemplate());

        ProgressionUnitBar barA = new ProgressionUnitBar();
        barA.setHiSynthId(Arrays.asList(111));
        barA.setSynthTemplates(synthTemplates);

        ProgressionUnitBar barB = new ProgressionUnitBar();
        barB.setHiSynthId(Arrays.asList(222));
        barB.setSynthTemplates(synthTemplates);

        ProgressionUnitBar barC = new ProgressionUnitBar();
        barC.setHiSynthId(Arrays.asList(333));
        barC.setSynthTemplates(synthTemplates);

        ProgressionUnit progressionUnitA = new ProgressionUnit();
        progressionUnitA.setProgressionUnitBars(Arrays.asList(barA,barA,barC,barA));

        ProgressionUnit progressionUnitB = new ProgressionUnit();
        progressionUnitB.setProgressionUnitBars(Arrays.asList(barB,barA,barB,barA));

        ProgressionUnit progressionUnitC = new ProgressionUnit();
        progressionUnitC.setProgressionUnitBars(Arrays.asList(barB,barB,barB,barA));

        ProgressionUnit progressionUnitD = new ProgressionUnit();
        progressionUnitD.setProgressionUnitBars(Arrays.asList(barC,barC,barC,barA));

        progressionUnits = Arrays.asList(progressionUnitA, progressionUnitB, progressionUnitC, progressionUnitD);
    }

    @Test
    public void rendersAudioWithFx(){

        FileStructure.deleteSingleFile(outputAudio);

        FXArpeggioDelay fxArpeggioDelay = new FXArpeggioDelay();
        fxArpeggioDelay.setDelayType(0.0);
        fxArpeggioDelay.setDelayVolume(0.5);
        fxArpeggioDelay.setMasterVolume(0.6);

        sfxRenderer.renderSfx(inputAudio,outputAudio,fxArpeggioDelay,80);

        File audioWithFx = new File(outputAudio);

        assertThat(audioWithFx.exists()).isTrue();
    }

    @Test
    public void fxHelperShouldExtractCorrect2Ids(){

        List<List<Integer>> ids = FXHelper.extractHiSynthIds(progressionUnits);

        assertThat(ids.get(0).size()).isEqualTo(2);
        assertThat(ids.get(0).get(0)).isEqualTo(111);
        assertThat(ids.get(0).get(1)).isEqualTo(333);
    }

    @Test
    public void shouldCreateCorrectVolumeArrays(){
        assertThat(FXDelayParameters.getEchoVolumes(FXDelayParameters.EchoVolume.LOW)).isEqualTo(FXDelayParameters.echosLow);
        assertThat(FXDelayParameters.getEchoVolumes(FXDelayParameters.EchoVolume.MID)).isEqualTo(FXDelayParameters.echosMid);
        assertThat(FXDelayParameters.getEchoVolumes(FXDelayParameters.EchoVolume.HI)).isEqualTo(FXDelayParameters.echosHigh);
    }

    @Test
    public void shouldCreateCorrectIntervalArrays() {
        assertThat(FXDelayParameters.getEchoIntervals(FXDelayParameters.DelayType.TRIPLET_EIGHTH, BPM)).isEqualTo(new int[]{250,500,750,1000});
        assertThat(FXDelayParameters.getEchoIntervals(FXDelayParameters.DelayType.TRIPLET_QUARTER, BPM)).isEqualTo(new int[]{500,1000,1500,2000});
        assertThat(FXDelayParameters.getEchoIntervals(FXDelayParameters.DelayType.EIGHTH, BPM)).isEqualTo(new int[]{375,750,1125,1500});
        assertThat(FXDelayParameters.getEchoIntervals(FXDelayParameters.DelayType.QUARTER, BPM)).isEqualTo(new int[]{750,1500,2250,3000});
    }

    @Test
    public void allGeneratedDelaysAreWithinLimits(){

        List<FXArpeggioDelay> fxArpeggioDelays = FXGenerationUtil.generateFxDatabaseRecords();

        int count = 0;
        for (FXArpeggioDelay fxArpeggioDelay: fxArpeggioDelays) {
            System.out.println("testing fxArpeggio instance :  " + count++);
            assertThat(masterVolumeIsValid(fxArpeggioDelay.getMasterVolume())).isTrue();
            assertThat(echoVolumeIsValid(fxArpeggioDelay.getDelayVolume())).isTrue();
            assertThat(delayTypeIsValid(fxArpeggioDelay.getDelayType())).isTrue();
        }
    }

    @Test
    public void shouldCreateFfmpegCommandOne(){

        FXArpeggioDelay fxArpeggioDelay = new FXArpeggioDelay();
        fxArpeggioDelay.setMasterVolume(0.6);
        fxArpeggioDelay.setDelayType(1.0);
        fxArpeggioDelay.setDelayVolume(0.5);
        String command = sfxRenderer.createFfmpegEchoCommand(fxArpeggioDelay, 80);
        String expectedValue = "aecho=1.0:0.6:750|1500|2250|3000:0.7|0.5|0.3|0.1";
        assertThat(command).isEqualToIgnoringCase(expectedValue);
    }

    @Test
    public void shouldCreateFfmpegCommandTwo(){
        FXArpeggioDelay fxArpeggioDelay = new FXArpeggioDelay();
        fxArpeggioDelay.setMasterVolume(0.3);
        fxArpeggioDelay.setDelayType(1.0);
        fxArpeggioDelay.setDelayVolume(1.0);
        String command = sfxRenderer.createFfmpegEchoCommand(fxArpeggioDelay, 80);
        String expectedValue = "aecho=1.0:0.3:750|1500|2250|3000:0.9|0.6|0.4|0.2";
        assertThat(command).isEqualToIgnoringCase(expectedValue);
    }

    public boolean masterVolumeIsValid(double value){

        for(double v : FXDelayParameters.VOLUME){
            if(v == value){
                return true;
            }
        }

        return false;
    }

    public boolean echoVolumeIsValid(double value){

        double[] possibleVolumes = {0.0,0.5,1.0};

        for(double v : possibleVolumes){
            if(v == value){
                return true;
            }
        }

        return false;
    }

    public boolean delayTypeIsValid(double value){

        double[] possibleVolumes = {0.0,0.3,0.7,1.0};

        for(double v : possibleVolumes){
            if(v == value){
                return true;
            }
        }

        return false;
    }
}
