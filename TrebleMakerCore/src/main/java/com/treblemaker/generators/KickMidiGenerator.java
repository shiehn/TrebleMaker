package com.treblemaker.generators;

import com.treblemaker.extractors.KeyExtractor;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueItem;
import org.jfugue.pattern.Pattern;

public class KickMidiGenerator {

    public QueueItem setMidi(QueueItem queueItem){
        for (ProgressionUnit pUnit : queueItem.getProgression().getStructure()) {


            //get the key
            String key = (new KeyExtractor()).extract(chordStrs);







            String jfugueStr = "T" + queueItem.getBpm() + " ";
            for(ProgressionUnitBar pBar: pUnit.getProgressionUnitBars()){
                for (Integer k: pBar.getKickPattern().getAsIntegerArray()){
                    //get the key
                    jfugueStr += intToDuration("F",k) + " ";
                }

                pBar.setKickMidiPattern(new Pattern(jfugueStr));
            }
        }

        return queueItem;
    }

    private String intToDuration(String key, int intgr){
        return key + Integer.toString(intgr);
    }
}
