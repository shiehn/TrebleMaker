package com.treblemaker.eventchain;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.dal.interfaces.IMetaDataChordInfoDal;
import com.treblemaker.dal.interfaces.IMetaDataTrackInfoDal;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.metadata.MetaDataChordInfo;
import com.treblemaker.model.metadata.MetaDataTrackInfo;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.queues.QueueState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SetMetaDataStateEventTest {

    IMetaDataTrackInfoDal metaDataTrackInfoDal;

    SetMetaDataStateEvent setMetaDataState;

    QueueState queueState;

    @Before
    public void setup(){
        metaDataTrackInfoDal = Mockito.mock(IMetaDataTrackInfoDal.class);
        //make sure save does nothing
        setMetaDataState = new SetMetaDataStateEvent(null, metaDataTrackInfoDal, "2.1");

        queueState = new QueueState();

        ProgressionUnitBar barA = new ProgressionUnitBar();
        barA.setHiSynthId(Arrays.asList(111));
        barA.setChord(new HiveChord("amin7"));
        ProgressionUnitBar barB = new ProgressionUnitBar();
        barB.setChord(new HiveChord("bdom7"));
        barB.setHiSynthId(Arrays.asList(222));
        ProgressionUnitBar barC = new ProgressionUnitBar();
        barC.setHiSynthId(Arrays.asList(333));
        barC.setChord(new HiveChord("cmaj7"));

        ProgressionUnit progressionUnitA = new ProgressionUnit();
        progressionUnitA.setType(ProgressionUnit.ProgressionType.VERSE);
        progressionUnitA.setProgressionUnitBars(Arrays.asList(barA,barA,barC,barA));
        progressionUnitA.setKey("gmaj");

        ProgressionUnit progressionUnitB = new ProgressionUnit();
        progressionUnitB.setType(ProgressionUnit.ProgressionType.BRIDGE);
        progressionUnitB.setProgressionUnitBars(Arrays.asList(barB,barA,barB,barA));
        progressionUnitB.setKey("cmaj");

        ProgressionUnit progressionUnitC = new ProgressionUnit();
        progressionUnitC.setType(ProgressionUnit.ProgressionType.BRIDGE);
        progressionUnitC.setProgressionUnitBars(Arrays.asList(barB,barB,barB,barA));

        ProgressionUnit progressionUnitD = new ProgressionUnit();
        progressionUnitD.setType(ProgressionUnit.ProgressionType.CHORUS);
        progressionUnitD.setProgressionUnitBars(Arrays.asList(barC,barC,barC,barA));
        progressionUnitD.setKey("amin");

        List<ProgressionUnit> progressionUnits = Arrays.asList(progressionUnitA, progressionUnitB, progressionUnitC, progressionUnitD);

        ProgressionDTO progression = new ProgressionDTO();
        progression.setStructure(progressionUnits);

        QueueItem queueItem = new QueueItem();
        queueItem.setProgression(progression);
        queueItem.setQueueItemId("some-track-id");

        queueState.setQueueItem(queueItem);
    }

    @Test
    public void shouldGenerateCorrectTrackInfoList(){

        List<MetaDataTrackInfo> trackInfos = setMetaDataState.getMetaDataTrackInfo(queueState);

        assertThat(trackInfos).hasSize(7);

        List<String> trackTypes = new ArrayList<>();
        trackTypes.add("comphats");
        trackTypes.add("comphi");
        trackTypes.add("compkick");
        trackTypes.add("complow");
        trackTypes.add("compmelodic");
        trackTypes.add("compmid");
        trackTypes.add("compsnare");

        for(MetaDataTrackInfo trackInfo : trackInfos){
            assertThat(trackInfo.getTrackId()).isEqualToIgnoringCase(queueState.getQueueItem().getQueueItemId());
            assertThat(trackTypes.contains(trackInfo.getTrackType())).isTrue();
            assertThat(trackInfo.getVersion()).isEqualToIgnoringCase("2.1");
        }
    }

    @Test
    public void shouldGenerateCorrectChordInfo(){
        List<MetaDataChordInfo> metaDataChordInfos = setMetaDataState.getMetaDataChordInfo(queueState);
        assertThat(metaDataChordInfos).hasSize(3);

        assertThat(metaDataChordInfos.get(0).getTrackId()).isEqualToIgnoringCase("some-track-id");
        assertThat(metaDataChordInfos.get(0).getPartType()).isEqualToIgnoringCase(ProgressionUnit.ProgressionType.VERSE.toString());
        assertThat(metaDataChordInfos.get(0).getPartChords()).isEqualToIgnoringCase("amin7-amin7-cmaj7-amin7");
        assertThat(metaDataChordInfos.get(0).getPartKey()).isEqualToIgnoringCase("Gmaj");

        assertThat(metaDataChordInfos.get(1).getTrackId()).isEqualToIgnoringCase("some-track-id");
        assertThat(metaDataChordInfos.get(1).getPartType()).isEqualToIgnoringCase(ProgressionUnit.ProgressionType.BRIDGE.toString());
        assertThat(metaDataChordInfos.get(1).getPartChords()).isEqualToIgnoringCase("bdom7-amin7-bdom7-amin7");
        assertThat(metaDataChordInfos.get(1).getPartKey()).isEqualToIgnoringCase("Cmaj");

        assertThat(metaDataChordInfos.get(2).getTrackId()).isEqualToIgnoringCase("some-track-id");
        assertThat(metaDataChordInfos.get(2).getPartType()).isEqualToIgnoringCase(ProgressionUnit.ProgressionType.CHORUS.toString());
        assertThat(metaDataChordInfos.get(2).getPartChords()).isEqualToIgnoringCase("cmaj7-cmaj7-cmaj7-amin7");
        assertThat(metaDataChordInfos.get(2).getPartKey()).isEqualToIgnoringCase("amin");
    }
}