package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import org.jfugue.pattern.Pattern;

public class SetHatMidiPatternEvent implements IEventChain {

    public int HAT_OCTAVE = 5;

    @Override
    public QueueState set(QueueState queueState) {

        for(ProgressionUnit pUnit : queueState.getStructure()) {
            String jfugueStr = "T" + queueState.getQueueItem().getBpm() + " ";

            for (ProgressionUnitBar pBar : pUnit.getProgressionUnitBars()) {
                for (Integer k : pBar.getHatPattern().getAsIntegerArray()) {
                    jfugueStr += intToDuration(pUnit.getKey(), k) + " ";
                }

                pBar.setHatMidiPattern(new Pattern(jfugueStr));
            }
        }

        return queueState;
    }

    private String intToDuration(String key, int intgr) {
        final String duration = "s";
        if (intgr == 0) {
            return "r" + duration;
        } else if (intgr == 1) {
            return key + HAT_OCTAVE + duration;
        }

        throw new RuntimeException("UNEXPECTED Hat PATTERN DURATION");
    }
}
