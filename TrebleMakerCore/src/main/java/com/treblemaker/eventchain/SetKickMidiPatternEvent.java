package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import org.jfugue.pattern.Pattern;

public class SetKickMidiPatternEvent implements IEventChain {

    private String KICK_OCTAVE = "0";

    @Override
    public QueueState set(QueueState queueState) {

        for(ProgressionUnit pUnit : queueState.getStructure()) {
            String jfugueStr = "T" + queueState.getQueueItem().getBpm() + " ";

            for (ProgressionUnitBar pBar : pUnit.getProgressionUnitBars()) {
                for (Integer k : pBar.getKickPattern().getAsIntegerArray()) {
                    jfugueStr += intToDuration(pUnit.getKey(), k) + " ";
                }

                pBar.setKickMidiPattern(new Pattern(jfugueStr));
            }
        }

        return queueState;
    }

    private String intToDuration(String key, int intgr) {
        final String duration = "s";
        if (intgr == 0) {
            return "r" + duration;
        } else if (intgr == 1) {
            return key + KICK_OCTAVE + duration;
        }

        throw new RuntimeException("UNEXPECTED KICK PATTERN DURATION");
    }
}
