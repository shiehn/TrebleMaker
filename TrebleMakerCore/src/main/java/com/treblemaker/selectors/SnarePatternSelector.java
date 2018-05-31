package com.treblemaker.selectors;

import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.snare.SnarePattern;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SnarePatternSelector {

    public QueueState setSelectedPatterns(Map<ProgressionUnit.ProgressionType, SnarePattern> typeToSelectedPatten, QueueState queueState) {

        for (ProgressionUnit pUnit : queueState.getStructure()) {
            SnarePattern snarePattern = typeToSelectedPatten.get(pUnit.getType());

            for (int i = 0; i < pUnit.getProgressionUnitBars().size(); i++) {
                pUnit.getProgressionUnitBars().get(i).setSnarePattern(snarePattern);
            }
        }

        return queueState;
    }
}
