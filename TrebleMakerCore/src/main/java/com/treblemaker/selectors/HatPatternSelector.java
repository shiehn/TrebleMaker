package com.treblemaker.selectors;

import com.treblemaker.model.hat.HatPattern;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HatPatternSelector {

    public QueueState setSelectedPatterns(Map<ProgressionUnit.ProgressionType, HatPattern> typeToSelectedPatten, QueueState queueState) {

        for (ProgressionUnit pUnit : queueState.getStructure()) {
            HatPattern hatPattern = typeToSelectedPatten.get(pUnit.getType());

            for (int i = 0; i < pUnit.getProgressionUnitBars().size(); i++) {
                pUnit.getProgressionUnitBars().get(i).setHatPattern(hatPattern);
            }
        }

        return queueState;
    }
}
