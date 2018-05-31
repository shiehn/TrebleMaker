package com.treblemaker.utils.harmonickey;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "return_queue_early_for_tests=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999", "spring.datasource.url=jdbc:mysql://localhost:3306/hivecomposedb", "spring.datasource.username=root", "spring.datasource.password=redrobes79D"})
public class HarmonicKeyUtilsTest extends TestCase {

    @Test
    public void shouldMatchCMajor() {

        List<HiveChord> hiveChords = new ArrayList<>();
        hiveChords.add(new HiveChord("cmaj7"));
        hiveChords.add(new HiveChord("dmin"));
        hiveChords.add(new HiveChord("gdom7"));

        List<String> compatibleKeys = HarmonicKeyUtils.findCompatibleKeys(hiveChords);

        assertThat(compatibleKeys).contains(KeyNames.C_MAJOR);
        assertThat(compatibleKeys).hasSize(1);
    }

    @Test
    public void shouldFindThreeMatchingKeys() {

        List<HiveChord> hiveChords = new ArrayList<>();
        hiveChords.add(new HiveChord("dmaj"));

        List<String> compatibleKeys = HarmonicKeyUtils.findCompatibleKeys(hiveChords);

        assertThat(compatibleKeys).hasSize(3);
        assertThat(compatibleKeys).contains(KeyNames.G_MAJOR, KeyNames.D_MAJOR, KeyNames.A_MAJOR);
    }

    @Test
    public void shouldFindGmajWithDimTriads() {

        List<HiveChord> hiveChords = new ArrayList<>();
        hiveChords.add(new HiveChord("f#dim"));

        List<String> compatibleKeys = HarmonicKeyUtils.findCompatibleKeys(hiveChords);

        assertThat(compatibleKeys).hasSize(1);
        assertThat(compatibleKeys).contains(KeyNames.G_MAJOR);
    }

    @Test
    public void shouldFindGSharpWhenPassedDimTriad() {
        List<HiveChord> hiveChords = Arrays.asList(new HiveChord("gdim"));
        List<String> compatibleKeys = HarmonicKeyUtils.findCompatibleKeys(hiveChords);

        assertThat(compatibleKeys).hasSize(1);
        assertThat(compatibleKeys).contains(KeyNames.GS_MAJOR);
    }

    @Test
    public void shouldFindCorrectKeysForVerse() {
        assertThat(true).isFalse();
    }

    @Test
    public void shouldFindCorrectKeysForChorus() {
        assertThat(true).isFalse();
    }

    @Test
    public void shouldFindCorrectKeysForBridge() {
        assertThat(true).isFalse();
    }

    @Test
    public void shouldSelectCommonKeyForTrack() {
        assertThat(true).isFalse();
    }

    @Test
    public void shouldSelectAnAltKeyAFithAwayWhenNoCommonKeyFound() {
        //if the verse was compatible with C and the chorus F & G //select the the key in priority
    }

    @Test
    public void shouldFindCompatibleMinorKey() {
        assertThat(true).isFalse();
    }

    @Test
    public void shouldFindCompatibleMajorKey() {
        assertThat(true).isFalse();
    }

    @Test
    public void shouldSelectSelectBackUpKey() {
        assertThat(true).isFalse();
    }

    @Test
    public void shouldConvertChordProgressionsToMapOfLists() {

        ProgressionUnitBar pBarC1 = new ProgressionUnitBar();
        pBarC1.setChord(new HiveChord("cmaj"));
        ProgressionUnitBar pBarC2 = new ProgressionUnitBar();
        pBarC2.setChord(new HiveChord("dmin"));
        ProgressionUnitBar pBarC3 = new ProgressionUnitBar();
        pBarC3.setChord(new HiveChord("emin"));
        ProgressionUnitBar pBarC4 = new ProgressionUnitBar();
        pBarC4.setChord(new HiveChord("fmaj"));

        ProgressionUnitBar pBarV1 = new ProgressionUnitBar();
        pBarV1.setChord(new HiveChord("gmaj"));
        ProgressionUnitBar pBarV2 = new ProgressionUnitBar();
        pBarV2.setChord(new HiveChord("amin"));
        ProgressionUnitBar pBarV3 = new ProgressionUnitBar();
        pBarV3.setChord(new HiveChord("bmin"));
        ProgressionUnitBar pBarV4 = new ProgressionUnitBar();
        pBarV4.setChord(new HiveChord("cmaj"));

        ProgressionUnitBar pBarB1 = new ProgressionUnitBar();
        pBarB1.setChord(new HiveChord("fmaj"));
        ProgressionUnitBar pBarB2 = new ProgressionUnitBar();
        pBarB2.setChord(new HiveChord("gmin"));
        ProgressionUnitBar pBarB3 = new ProgressionUnitBar();
        pBarB3.setChord(new HiveChord("amin"));
        ProgressionUnitBar pBarB4 = new ProgressionUnitBar();
        pBarB4.setChord(new HiveChord("a#maj"));

        List<ProgressionUnitBar> pBarsChorus = Arrays.asList(pBarC1, pBarC2, pBarC3, pBarC4);
        List<ProgressionUnitBar> pBarsVerse = Arrays.asList(pBarV1, pBarV2, pBarV3, pBarV4);
        List<ProgressionUnitBar> pBarsBridge = Arrays.asList(pBarB1, pBarB2, pBarB3, pBarB4);

        ProgressionUnit chorus = new ProgressionUnit();
        chorus.initBars(4);
        chorus.setProgressionUnitBars(pBarsChorus);

        ProgressionUnit verse = new ProgressionUnit();
        verse.initBars(4);
        verse.setProgressionUnitBars(pBarsVerse);

        ProgressionUnit bridge = new ProgressionUnit();
        bridge.initBars(4);
        verse.setProgressionUnitBars(pBarsBridge);

        List<ProgressionUnit> progressionUnits = new ArrayList<>();
        progressionUnits.add(chorus);
        progressionUnits.add(verse);
        progressionUnits.add(bridge);

        assertThat(true).isFalse();
//        convertProgressionsToChordLists
    }
}