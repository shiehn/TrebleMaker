package com.treblemaker.selectors;

import com.treblemaker.model.kick.KickPattern;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KickPatternSelector {

    public QueueState setSelectedPatterns(Map<ProgressionUnit.ProgressionType, KickPattern> typeToSelectedPatten, QueueState queueState) {

        for (ProgressionUnit pUnit : queueState.getStructure()) {
            KickPattern kickPattern = typeToSelectedPatten.get(pUnit.getType());

            for (int i = 0; i < pUnit.getProgressionUnitBars().size(); i++) {
                pUnit.getProgressionUnitBars().get(i).setKickPattern(kickPattern);
            }
        }

        return queueState;
    }
}
