package com.treblemaker.options;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.RhythmicAccents;
import com.treblemaker.model.SourceData;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.options.interfaces.IHarmonicLoopOptions;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.ArrayList;
import java.util.List;

import static com.treblemaker.model.progressions.ProgressionUnit.BarCount.FOUR;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class HarmonicLoopOptionsTest extends TestCase {

    @Autowired
    private IHarmonicLoopOptions harmonicLoopOptions;

    private List<HarmonicLoop> harmonicLoops;
    private List<HiveChord> chordList;
    private QueueState queueState;
    private SourceData sourceData;
    private List<ProgressionUnit> progressionUnits;

    private void createHarmonicLoopListFromDb() {

        List<RhythmicAccents> accentsList = new ArrayList<>();
        accentsList.add(new RhythmicAccents());


        HarmonicLoop harmonicLoopA = new HarmonicLoop();
        harmonicLoopA.setId(1);
        harmonicLoopA.setFileName("loopA");
        harmonicLoopA.setRhythmicAccents(accentsList);
        ArrayList<HiveChord> chordListA = new ArrayList<HiveChord>() {{
            add(new HiveChord("c#_min"));
            add(new HiveChord("d_dom_7"));
            add(new HiveChord("b_maj"));
            add(new HiveChord("g_min_7"));
        }};
        harmonicLoopA.setChords(chordListA);

        HarmonicLoop harmonicLoopB = new HarmonicLoop();
        harmonicLoopB.setId(2);
        harmonicLoopB.setFileName("loopB");
        harmonicLoopB.setRhythmicAccents(accentsList);
        ArrayList<HiveChord> chordListB = new ArrayList<HiveChord>() {{
            add(new HiveChord("d#_min"));
            add(new HiveChord("a_dom_7"));
            add(new HiveChord("g#_maj_7"));
            add(new HiveChord("d_min_7"));
        }};
        harmonicLoopB.setChords(chordListB);

        HarmonicLoop harmonicLoopC = new HarmonicLoop();
        harmonicLoopC.setId(3);
        harmonicLoopC.setFileName("loopC");
        harmonicLoopC.setRhythmicAccents(accentsList);
        ArrayList<HiveChord> chordListC = new ArrayList<HiveChord>() {{
            add(new HiveChord("c#_maj"));
            add(new HiveChord("a_dom_7"));
            add(new HiveChord("f#_maj_7"));
            add(new HiveChord("c_min_7"));
        }};
        harmonicLoopC.setChords(chordListC);

        harmonicLoops = new ArrayList<HarmonicLoop>() {{
            add(harmonicLoopA);
            add(harmonicLoopB);
            add(harmonicLoopC);
        }};
    }

    private void createChordListFromDb() {

        chordList = new ArrayList<HiveChord>() {{
            add(new HiveChord("g#_maj_7"));
            add(new HiveChord("b_dom_7"));
            add(new HiveChord("c_min_7"));
            add(new HiveChord("c_dom_7"));
        }};

        String test = "";
    }

    private void createSourceData() {

        sourceData = new SourceData() {{
            setHiveChordInDatabase(chordList);
            setHarmonicLoops(harmonicLoops);
        }};
    }

    private void createProgressionUnits() {

        HarmonicLoop loopA = new HarmonicLoop();
        List<HiveChord> loopsChords = new ArrayList<HiveChord>() {{
            add(new HiveChord("d_dom_7"));
        }};
        loopA.setChords(loopsChords);

        HarmonicLoop loopB = new HarmonicLoop();
        List<HiveChord> loopsHiveChordB = new ArrayList<HiveChord>() {{
            add(new HiveChord("a_maj"));
            add(new HiveChord("b_maj"));
        }};
        loopB.setChords(loopsHiveChordB);

        HarmonicLoop loopC = new HarmonicLoop();
        List<HiveChord> loopsHiveChordC = new ArrayList<HiveChord>() {{
            add(new HiveChord("c#_maj"));
        }};
        loopC.setChords(loopsHiveChordC);

        HarmonicLoop loopD = new HarmonicLoop();
        List<HiveChord> loopsHiveChordD = new ArrayList<HiveChord>() {{
            add(new HiveChord("c_maj"));
            add(new HiveChord("d_maj"));
        }};
        loopD.setChords(loopsHiveChordD);

        List<HarmonicLoop> loops = new ArrayList<HarmonicLoop>(){{
            add(loopA);
            add(loopB);
            add(loopC);
            add(loopD);
        }};

        ProgressionUnit unitA = new ProgressionUnit();
        unitA.initBars(FOUR.getValue());
        List<HiveChord> chords = new ArrayList<HiveChord>() {{
            add(new HiveChord("c_dom_7"));
            add(new HiveChord("f#_min"));
            add(new HiveChord("g_maj"));
            add(new HiveChord("a_maj"));
        }};

        for(Integer i=0; i<chords.size(); i++){
            unitA.getProgressionUnitBars().get(i).setChord(chords.get(i));
        }

        for(Integer i=0; i<loops.size(); i++){
            unitA.getProgressionUnitBars().get(i).setHarmonicLoop(loops.get(i));
        }

        progressionUnits = new ArrayList<ProgressionUnit>(){{
            add(unitA);
        }};

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(progressionUnits);

        QueueItem queueItem = new QueueItem();
        queueItem.setProgression(progressionDTO);

        queueState = new QueueState();
        queueState.setQueueItem(queueItem);
        queueState.setDataSource(sourceData);
    }

    @Before
    public void beforeEachTest() {

        createHarmonicLoopListFromDb();
        createChordListFromDb();
        createSourceData();
        createProgressionUnits();
    }

    @Test
    public void ShouldGetOptionsFromHarmonicLoopAndChord() {

        HarmonicLoop loop = new HarmonicLoop();
        List<HiveChord> loopsChords = new ArrayList<HiveChord>() {{
            add(new HiveChord("c#_maj"));
        }};
        loop.setChords(loopsChords);

        HiveChord chord = new HiveChord("a_dom_7");

        List<HarmonicLoop> options =  harmonicLoopOptions.getLoopOptionsFromHarmonicLoopAndChord(loop, chord, sourceData.getHarmonicLoops(0));

        Assert.assertEquals(2, options.size());
        Assert.assertEquals(3, options.get(0).getId().intValue());
        Assert.assertEquals(2, options.get(1).getId().intValue());
    }

    public void ShouldGetOptionsFromAChord() {

        HiveChord chord = new HiveChord("b_maj");

        List<HarmonicLoop> options =  harmonicLoopOptions.getLoopOptionsFromAChord(chord, sourceData.getHarmonicLoops(0));

        Assert.assertEquals(2, options.size());
        Assert.assertEquals(1, options.get(0).getId().intValue());
        Assert.assertEquals(3, options.get(1).getId().intValue());
    }

    @Test
    public void ShouldGetOptionsFromHarmonicLoop() {

        HarmonicLoop loop = new HarmonicLoop();
        List<HiveChord> loopsChords = new ArrayList<HiveChord>() {{
            add(new HiveChord("c_maj"));
            add(new HiveChord("c#_maj"));
            add(new HiveChord("d_dom_7"));
        }};
        loop.setChords(loopsChords);

        List<HarmonicLoop> options =  harmonicLoopOptions.getLoopOptionsFromHarmonicLoop(loop, sourceData.getHarmonicLoops(0));

        Assert.assertEquals(2, options.size());
        Assert.assertEquals(3, options.get(0).getId().intValue());
        Assert.assertEquals(1, options.get(1).getId().intValue());
    }

    @Test
    public void ShouldSetHarmonicLoopOptions(){

        List<RhythmicAccents> accentsList = new ArrayList<>();
        accentsList.add(new RhythmicAccents());

        List<HarmonicLoop> sourceLoops = new ArrayList<>();

        HarmonicLoop loopA = new HarmonicLoop();
        loopA.setId(1111);
        loopA.setRhythmicAccents(accentsList);
        loopA.setChords(new ArrayList<HiveChord>() {{
            add(new HiveChord("a_min_7"));
        }});

        HarmonicLoop loopB = new HarmonicLoop();
        loopB.setId(2222);
        loopB.setFileName("loopB");
        loopB.setRhythmicAccents(accentsList);
        loopB.setChords(new ArrayList<HiveChord>(){{
            add(new HiveChord("c_maj_7"));
        }});

        HarmonicLoop loopC = new HarmonicLoop();
        loopC.setId(3333);
        loopC.setFileName("loopC");
        loopC.setRhythmicAccents(accentsList);
        loopC.setChords(new ArrayList<HiveChord>(){{
            add(new HiveChord("b_dom_7"));
            add(new HiveChord("c_maj_7"));
        }});

        sourceLoops.add(loopA);
        sourceLoops.add(loopB);
        sourceLoops.add(loopC);

        SourceData sourceData = new SourceData();
        sourceData.setHarmonicLoops(sourceLoops);

        ProgressionUnitBar progressionUnitBar = new ProgressionUnitBar();
        progressionUnitBar.setChord(new HiveChord("c_maj_7"));

        //setHarmonicLoops all options ..
        harmonicLoopOptions.setHarmonicLoopAltOptions(progressionUnitBar, sourceData.getHarmonicLoops(0));


        assertThat(progressionUnitBar.getHarmonicLoopAltOptions()).isNotNull();
        assertThat(progressionUnitBar.getHarmonicLoopAltOptions()).hasSize(2);
    }

}