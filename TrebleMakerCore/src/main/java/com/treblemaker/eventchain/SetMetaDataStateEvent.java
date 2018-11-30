package com.treblemaker.eventchain;

import com.treblemaker.dal.interfaces.IMetaDataChordInfoDal;
import com.treblemaker.dal.interfaces.IMetaDataTrackInfoDal;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.metadata.MetaDataChordInfo;
import com.treblemaker.model.metadata.MetaDataTrackInfo;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SetMetaDataStateEvent implements IEventChain {

    private IMetaDataChordInfoDal metaDataChordInfoDal;
    private IMetaDataTrackInfoDal metaDataTrackInfoDal;
    private String API_VERSION;

    @Autowired
    public SetMetaDataStateEvent(IMetaDataChordInfoDal metaDataChordInfoDal, IMetaDataTrackInfoDal metaDataTrackInfoDal, @Value("${api.version}") String api_version){
        this.metaDataChordInfoDal = metaDataChordInfoDal;
        this.metaDataTrackInfoDal = metaDataTrackInfoDal;
        this.API_VERSION = api_version;
    }

    @Override
    public QueueState set(QueueState queueState) {

        List<MetaDataTrackInfo> metaDataTrackInfos = getMetaDataTrackInfo(queueState);
        List<MetaDataChordInfo> metaDataChordInfos = getMetaDataChordInfo(queueState);

        for (MetaDataTrackInfo trackInfo : metaDataTrackInfos) {
            metaDataTrackInfoDal.save(trackInfo);
        }

        for (MetaDataChordInfo chordInfos : metaDataChordInfos){
            metaDataChordInfoDal.save(chordInfos);
        }

        return queueState;
    }

    public List<MetaDataTrackInfo> getMetaDataTrackInfo(QueueState queueState){
        List<String> trackTypes = new ArrayList<>();
        trackTypes.add("comphats");
        trackTypes.add("comphi");
        trackTypes.add("compkick");
        trackTypes.add("complow");
        trackTypes.add("compmelodic");
        trackTypes.add("compmid");
        trackTypes.add("compsnare");

        List<MetaDataTrackInfo> trackInfoList = new ArrayList<>();

        for(String type : trackTypes){
            MetaDataTrackInfo metaDataTrackInfo = new MetaDataTrackInfo();
            metaDataTrackInfo.setTrackId(queueState.getQueueItem().getQueueItemId());
            metaDataTrackInfo.setTrackType(type);
            metaDataTrackInfo.setVersion(API_VERSION);

            trackInfoList.add(metaDataTrackInfo);
        }

        return trackInfoList;
    }

    public List<MetaDataChordInfo> getMetaDataChordInfo(QueueState queueState){

        List<ProgressionUnit.ProgressionType> finishedTypes = new ArrayList<>();
        List<MetaDataChordInfo> metaData = new ArrayList<>();

        for (ProgressionUnit pUnit : queueState.getStructure()) {
            if(finishedTypes.contains(pUnit.getType())){
                continue;
            }

            MetaDataChordInfo metaDataChordInfo = new MetaDataChordInfo();
            metaDataChordInfo.setTrackId(queueState.getQueueItem().getQueueItemId());
            metaDataChordInfo.setPartType(pUnit.getType().toString());
            metaDataChordInfo.setPartKey(pUnit.getKey());
            metaDataChordInfo.setTrackKey("error");
            for (ProgressionUnitBar pBar : pUnit.getProgressionUnitBars()) {
                String chords = metaDataChordInfo.getPartChords();

                if(chords == null || chords.equals("")){
                    metaDataChordInfo.setPartChords(pBar.getChord().getRawChordName());
                }else{
                    metaDataChordInfo.setPartChords(metaDataChordInfo.getPartChords() + "-" + pBar.getChord().getRawChordName());
                }

            }

            finishedTypes.add(pUnit.getType());
            metaData.add(metaDataChordInfo);
        }

        return metaData;
    }
}
