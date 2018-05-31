package com.treblemaker.generators;

import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
public class StationTrackGenerator {

    public String extractTrackName(QueueState queueState){

        final List<ProgressionUnit> progressions = queueState.getStructure();

        //TODO NEED TO ADD STATION ID:
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(extractFirstChord(progressions));
        stringBuilder.append("_");
        stringBuilder.append(extractSecondChord(progressions));
        stringBuilder.append("_");
        stringBuilder.append(extractSecondLastChord(progressions));
        stringBuilder.append("_");
        stringBuilder.append(extractLastChord(progressions));
        stringBuilder.append("_");
        stringBuilder.append(queueState.getQueueItem().getBpm());
        stringBuilder.append("_");
        stringBuilder.append(extractSongKey());
        stringBuilder.append("_");
        stringBuilder.append(createTimeStamp());
        stringBuilder.append("_");
        stringBuilder.append(createUIDWithFileType());

        return stringBuilder.toString();
    }

    private String extractFirstChord(List<ProgressionUnit> progressions){
        ProgressionUnit firstProgression = progressions.get(0);
        List<ProgressionUnitBar> bars = firstProgression.getProgressionUnitBars();
        ProgressionUnitBar firstBar = bars.get(0);

        return firstBar.getChord().getChordName();
    }

    private String extractSecondChord(List<ProgressionUnit> progressions){
        ProgressionUnit firstProgression = progressions.get(0);
        List<ProgressionUnitBar> bars = firstProgression.getProgressionUnitBars();
        ProgressionUnitBar secondBar = bars.get(1);

        return secondBar.getChord().getChordName();
    }

    private String extractSecondLastChord(List<ProgressionUnit> progressions){
        ProgressionUnit lastProgression = progressions.get(progressions.size()-1);
        List<ProgressionUnitBar> bars = lastProgression.getProgressionUnitBars();
        ProgressionUnitBar secondLastBar = bars.get(2);

        return secondLastBar.getChord().getChordName();
    }

    private String extractLastChord(List<ProgressionUnit> progressions){
        ProgressionUnit lastProgression = progressions.get(progressions.size()-1);
        List<ProgressionUnitBar> bars = lastProgression.getProgressionUnitBars();
        ProgressionUnitBar secondLastBar = bars.get(3);

        return secondLastBar.getChord().getChordName();
    }

    private String extractSongKey(){

        return null;
    }

    private String createTimeStamp(){

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();

        return dateFormat.format(date).replace("/", "-");
    }

    private String createUIDWithFileType(){

        String UID = Integer.toString(new Random().nextInt(10000));
        return UID + ".wav";
    }
}
