package com.treblemaker.generators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.model.metadata.MetaData;
import com.treblemaker.model.metadata.MetaDataChordInfo;
import com.treblemaker.model.metadata.MetaDataChords;
import com.treblemaker.model.metadata.MetaDataTrackInfo;
import com.treblemaker.model.stations.StationTrack;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

public class MetaDataGenerator {

    private AppConfigs appConfigs;
    private StationTrack stationTrack;
    private List<MetaDataTrackInfo> metaDataTrackInfos;
    private List<MetaDataChordInfo> metaDataChordInfos;

    public MetaDataGenerator(AppConfigs appConfigs,
                             StationTrack stationTrack,
                             List<MetaDataTrackInfo> metaDataTrackInfos,
                             List<MetaDataChordInfo> metaDataChordInfos){
        this.appConfigs = appConfigs;
        this.stationTrack = stationTrack;
        this.metaDataChordInfos = metaDataChordInfos;
        this.metaDataTrackInfos = metaDataTrackInfos;
    }

    public void generate(String destination){
        MetaData metadata = new MetaData();

        for (MetaDataTrackInfo trackInfo : metaDataTrackInfos) {
            if(trackInfo.getTrackId().equalsIgnoreCase(stationTrack.getFile())) {
                if (trackInfo.getTrackType().contains("melodic")) {
                    metadata.addTrack("0" + trackInfo.getTrackType()+"_"+(stationTrack.getSelectedMelody()-1) + ".mid");
                } else {
                    metadata.addTrack("0" + trackInfo.getTrackType() + ".mid");
                }

                metadata.setVersion(trackInfo.getVersion());
            }
        }

        for(MetaDataChordInfo chordInfo : metaDataChordInfos){
            if(chordInfo.getTrackId().equalsIgnoreCase(stationTrack.getFile())){
                String[] chords = chordInfo.getPartChords().split("-");

                MetaDataChords metaDataChords = new MetaDataChords();
                metaDataChords.setChords(Arrays.asList(chords));
                metaDataChords.setPartKey(chordInfo.getPartKey());
                metaDataChords.setTrackId(stationTrack.getFile());

                metadata.addChords(chordInfo.getPartType(), metaDataChords);
                metadata.setKey(chordInfo.getTrackKey());

            }
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            String json = mapper.writeValueAsString(metadata);
            System.out.println(json);

            FileOutputStream fileOutputStream = new FileOutputStream(destination);
            mapper.writeValue(fileOutputStream, metadata);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}