package com.treblemaker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.model.*;
import com.treblemaker.model.characterisic.HarmonicLoopCharacteristic;
import com.treblemaker.model.hitsandfills.Hit;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.*;

import static com.treblemaker.model.progressions.ProgressionUnit.BarCount.FOUR;

@Ignore
public class TestBase extends TestCase {

    @Autowired
    public AppConfigs appConfigs;

    public QueueItem getQueueItem(List<ProgressionUnit.ProgressionType> progressionTypes) {

        HashMap<ProgressionUnit.ProgressionType, List<HiveChord>> chordsByType = new HashMap<ProgressionUnit.ProgressionType, List<HiveChord>>() {{
            put(ProgressionUnit.ProgressionType.VERSE, Arrays.asList(new HiveChord("dmin7"), new HiveChord("bdom7"), new HiveChord("a#maj7"), new HiveChord("cmaj7")));
            put(ProgressionUnit.ProgressionType.CHORUS, Arrays.asList(new HiveChord("amin7"), new HiveChord("bdom7"), new HiveChord("amaj7"), new HiveChord("emaj7")));
            put(ProgressionUnit.ProgressionType.BRIDGE, Arrays.asList(new HiveChord("dmaj7"), new HiveChord("bmaj7"), new HiveChord("cmaj7"), new HiveChord("amaj7")));
        }};

        QueueItem queueItem = new QueueItem();
        queueItem.setBpm(80);
        ProgressionDTO progressionDTO = new ProgressionDTO();

        for (ProgressionUnit.ProgressionType progressionType : progressionTypes) {

            ProgressionUnit pUnit = new ProgressionUnit();
            pUnit.initBars(FOUR.getValue());
            pUnit.setType(progressionType);

            for (Integer i = 0; i < chordsByType.get(progressionType).size(); i++) {
                pUnit.getProgressionUnitBars().get(i).setChord(chordsByType.get(progressionType).get(i));
            }

            progressionDTO.getStructure().add(pUnit);
        }

        queueItem.setProgression(progressionDTO);

        return queueItem;
    }

    public QueueItem getQueueItemWithAmbiLoops(List<ProgressionUnit.ProgressionType> progressionTypes) {

        QueueItem queueItem = getQueueItem(progressionTypes);
        queueItem.setQueueItemId(UUID.randomUUID().toString());

        //ADD DEFAULT BEATLOOPS ..
        queueItem.getProgression().getStructure().forEach(pUnit -> {
            pUnit.getProgressionUnitBars().forEach(pBar -> {
                if (pUnit.getType() == ProgressionUnit.ProgressionType.VERSE) {
                    AmbienceLoop loop = new AmbienceLoop();
                    loop.setFileName("vAmbiLoop.wav");
                    loop.setAudioLength(2);
                    loop.setShimLength(1);
                    loop.setFilePath(appConfigs.MOCK_AUDIO_PATH);
                    pBar.setAmbienceLoop(loop);
                } else if (pUnit.getType() == ProgressionUnit.ProgressionType.BRIDGE) {
                    AmbienceLoop loop = new AmbienceLoop();
                    loop.setFileName("bAmbiLoop.wav");
                    loop.setAudioLength(2);
                    loop.setShimLength(1);
                    loop.setFilePath(appConfigs.MOCK_AUDIO_PATH);
                    pBar.setAmbienceLoop(loop);
                } else if (pUnit.getType() == ProgressionUnit.ProgressionType.CHORUS) {
                    AmbienceLoop loop = new AmbienceLoop();
                    loop.setFileName("cAmbiLoop.wav");
                    loop.setAudioLength(2);
                    loop.setShimLength(1);
                    loop.setFilePath(appConfigs.MOCK_AUDIO_PATH);
                    pBar.setAmbienceLoop(loop);
                }
            });
        });

        return queueItem;
    }


    public QueueItem getQueueItemWithProgressionsAndChords(List<ProgressionUnit.ProgressionType> progressionTypes, HashMap<ProgressionUnit.ProgressionType, List<HiveChord>> chordsByType) {

        QueueItem queueItem = new QueueItem();
        ProgressionDTO progressionDTO = new ProgressionDTO();

        for (ProgressionUnit.ProgressionType progressionType : progressionTypes) {

            ProgressionUnit pUnit = new ProgressionUnit();
            pUnit.setType(progressionType);

            for (Integer i = 0; i < chordsByType.get(progressionType).size(); i++) {
                pUnit.getProgressionUnitBars().get(i).setChord(chordsByType.get(progressionType).get(i));
            }

            progressionDTO.getStructure().add(pUnit);
        }

        queueItem.setProgression(progressionDTO);

        return queueItem;
    }


    public SourceData createSourceData() {

        SourceData sourceData = new SourceData();

        List<HiveChord> chords = new ArrayList<HiveChord>() {{
            add(new HiveChord("amin7"));
            add(new HiveChord("amin7"));
            add(new HiveChord("amin7"));
            add(new HiveChord("amin7"));
        }};

        sourceData.setHiveChordInDatabase(chords);

        List<RhythmicAccents> accents = new ArrayList<>();
        accents.add(new RhythmicAccents());

        List<HarmonicLoopCharacteristic> harmonicLoopCharacteristics = new ArrayList<>();
        HarmonicLoopCharacteristic harmonicLoopCharacteristic = new HarmonicLoopCharacteristic();
        harmonicLoopCharacteristic.setHarmonicLoopId(1);
        harmonicLoopCharacteristic.setCharacteristicId(6);
        harmonicLoopCharacteristics.add(harmonicLoopCharacteristic);
        sourceData.setHarmonicLoopCharacteristics(harmonicLoopCharacteristics);

        HarmonicLoop loopA = new HarmonicLoop();
        loopA.setId(1);
        loopA.setFileName("loopOneBar");
        loopA.setBarCount(1);
        loopA.setRhythmicAccents(accents);
        loopA.setChords(new ArrayList<HiveChord>() {{
            add(new HiveChord("amin"));
        }});

        HarmonicLoop loopB = new HarmonicLoop();
        loopB.setId(2);
        loopB.setFileName("loopTwoBar");
        loopB.setBarCount(2);
        loopB.setRhythmicAccents(accents);
        loopB.setChords(new ArrayList<HiveChord>() {{
            add(new HiveChord("amin"));
        }});

        HarmonicLoop loopC = new HarmonicLoop();
        loopC.setId(3);
        loopC.setFileName("loopFourBar");
        loopC.setBarCount(4);
        loopC.setRhythmicAccents(accents);
        loopC.setChords(new ArrayList<HiveChord>() {{
            add(new HiveChord("amin7"));
        }});

        List<HarmonicLoop> harmonicLoops = new ArrayList<>();
        harmonicLoops.add(loopA);
        harmonicLoops.add(loopB);
        harmonicLoops.add(loopC);
        sourceData.setHarmonicLoops(harmonicLoops);

        return sourceData;
    }

    public void deleteFolderAndContents(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolderAndContents(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public QueueState getQueueStateWithProgression() throws JsonProcessingException {

        HiveChord chordOne = new HiveChord();
        chordOne.setId(1);
        chordOne.setChordName("amin7");

        HiveChord chordTwo = new HiveChord();
        chordTwo.setId(2);
        chordTwo.setChordName("cmaj");

        HiveChord chordThree = new HiveChord();
        chordThree.setId(3);
        chordThree.setChordName("fdom7");

        ProgressionUnitBar barOne = new ProgressionUnitBar();
        barOne.setChord(chordOne);
        ProgressionUnitBar barTwo = new ProgressionUnitBar();
        barTwo.setChord(chordTwo);
        ProgressionUnitBar barThree = new ProgressionUnitBar();
        barThree.setChord(chordThree);

        ProgressionUnitBar barFour = new ProgressionUnitBar();
        barFour.setChord(chordThree);

        List<ProgressionUnitBar> progressionUnitBars = Arrays.asList(barOne, barTwo, barThree, barFour);

        ProgressionUnit progressionUnit = new ProgressionUnit();
        progressionUnit.setBarCount(4);

        progressionUnit.setProgressionUnitBars(progressionUnitBars);

        ProgressionDTO progressionDTO = new ProgressionDTO();
        progressionDTO.setStructure(Arrays.asList(progressionUnit));

        QueueItem queueItem = new QueueItem();
        queueItem.setProgression(progressionDTO);

        Hit optionOne = new Hit();
        optionOne.setId(1);
        optionOne.setCompatibleChord(chordOne);

        Hit optionTwo = new Hit();
        optionTwo.setId(2);
        optionTwo.setCompatibleChord(chordTwo);

        Hit optionThree = new Hit();
        optionThree.setId(3);

        SourceData dataSource = new SourceData();
        dataSource.setHits(Arrays.asList(optionOne, optionTwo, optionThree));

        QueueState queueState = new QueueState();
        queueState.setDataSource(dataSource);
        queueState.setQueueItem(queueItem);

        return queueState;
    }
}

