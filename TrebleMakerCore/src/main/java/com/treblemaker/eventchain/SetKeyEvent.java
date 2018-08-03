package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.extractors.KeyExtractor;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SetKeyEvent implements IEventChain {

    private KeyExtractor keyExtractor;

    public SetKeyEvent() {
        keyExtractor = new KeyExtractor();
    }

    @Override
    public QueueState set(QueueState queueState) {

        for (ProgressionUnit pUnit : queueState.getStructure()) {
            List<String> unitChords = new ArrayList<>();

            for(ProgressionUnitBar pBar : pUnit.getProgressionUnitBars()) {
                    unitChords.add(pBar.getChord().getRawChordName());
            }

            String key = keyExtractor.extract(unitChords);
            pUnit.setKey(key);
        }

        return queueState;
    }
}
