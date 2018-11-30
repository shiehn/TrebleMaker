package com.treblemaker.generators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.model.metadata.MetaData;
import com.treblemaker.model.metadata.MetaDataChordInfo;
import com.treblemaker.model.metadata.MetaDataChords;
import com.treblemaker.model.metadata.MetaDataTrackInfo;
import com.treblemaker.model.stations.StationTrack;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MetaDataGeneratorTest {

    private MetaDataGenerator metaDataGenerator;
    private File tempDir;

    @Before
    public void setup() {
        AppConfigs appConfigs = new AppConfigs();

        MetaDataChordInfo metaDataChordInfo1 = new MetaDataChordInfo();
        metaDataChordInfo1.setTrackKey("g");
        metaDataChordInfo1.setTrackId("xxx");
        metaDataChordInfo1.setPartType("verse");
        metaDataChordInfo1.setPartKey("c");
        metaDataChordInfo1.setPartChords("cmaj-dmin-dmin-gmaj");

        MetaDataChordInfo metaDataChordInfo2 = new MetaDataChordInfo();
        metaDataChordInfo2.setTrackKey("g");
        metaDataChordInfo2.setTrackId("yyy");
        metaDataChordInfo2.setPartType("chorus");
        metaDataChordInfo2.setPartKey("c");
        metaDataChordInfo2.setPartChords("fmaj-fmin-dmin-amaj");

        MetaDataChordInfo metaDataChordInfo3 = new MetaDataChordInfo();
        metaDataChordInfo3.setTrackKey("g");
        metaDataChordInfo3.setTrackId("xxx");
        metaDataChordInfo3.setPartType("chorus");
        metaDataChordInfo3.setPartKey("d");
        metaDataChordInfo3.setPartChords("g-g-dmin-dmaj");

        List<MetaDataChordInfo> metaDataChordInfos = new ArrayList<>();
        metaDataChordInfos.add(metaDataChordInfo1);
        metaDataChordInfos.add(metaDataChordInfo2);
        metaDataChordInfos.add(metaDataChordInfo3);

        MetaDataTrackInfo mdt1 = new MetaDataTrackInfo();
        mdt1.setTrackId("yyy");
        mdt1.setTrackType("melodic");
        mdt1.setVersion("2.1");

        MetaDataTrackInfo mdt2 = new MetaDataTrackInfo();
        mdt2.setTrackId("xxx");
        mdt2.setTrackType("melodic");
        mdt2.setVersion("2.1");

        MetaDataTrackInfo mdt3 = new MetaDataTrackInfo();
        mdt3.setTrackId("xxx");
        mdt3.setTrackType("low");
        mdt3.setVersion("2.1");

        List<MetaDataTrackInfo> metaDataTrackInfos = new ArrayList<>();
        metaDataTrackInfos.add(mdt1);
        metaDataTrackInfos.add(mdt3);
        metaDataTrackInfos.add(mdt2);

        StationTrack stationTrack = new StationTrack();
        stationTrack.setFile("xxx");
        stationTrack.setSelectedMelody(1);

        metaDataGenerator = new MetaDataGenerator(appConfigs, stationTrack, metaDataTrackInfos, metaDataChordInfos);
    }

    @Test
    public void shouldGenerateMetadataFile() throws IOException {
        tempDir = Files.createTempDir();
        Path metaDataDestination = Paths.get(tempDir.getAbsolutePath(), "metadata.json");
        System.out.println("PATH: " + metaDataDestination.toString());

        metaDataGenerator.generate(metaDataDestination.toString());

        assertThat(metaDataDestination.toFile().exists()).isTrue();

        MetaData metaData = new ObjectMapper().readValue(metaDataDestination.toFile(), MetaData.class);
        assertThat(metaData.getTracks()).hasSize(2);
        assertThat(metaData.getTracks()).contains("0melodic_0.mid");

        MetaDataChords verse = metaData.getChords().get("verse");
        MetaDataChords chorus = metaData.getChords().get("chorus");

        assertThat(verse.getChords()).hasSize(4);
        assertThat(verse.getChords()).contains("cmaj","dmin","dmin","gmaj");
        assertThat(verse.getPartKey()).isEqualToIgnoringCase("c");

        assertThat(chorus.getChords()).hasSize(4);
        assertThat(chorus.getChords()).contains("g", "g", "dmin", "dmaj");
        assertThat(chorus.getPartKey()).isEqualToIgnoringCase("d");

        assertThat(metaData.getVersion()).isEqualToIgnoringCase("2.1");
        assertThat(metaData.getKey()).isEqualToIgnoringCase("g");
    }
}