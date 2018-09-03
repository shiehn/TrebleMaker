package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.configs.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateMetaDataFile implements IEventChain {

    @Autowired
    private AppConfigs appConfigs;

    @Override
    public QueueState set(QueueState queueState) {

        String content = "API_VERSION: 0.2 \n";
        content += "Chord Progression: \n";

        for (ProgressionUnit pUnit : queueState.getStructure()) {
            content = content + "\n";
            content = content + pUnit.getType() + ":\n";
            for (ProgressionUnitBar pBar : pUnit.getProgressionUnitBars()) {
                content = content + pBar.getChord().getRawChordName() + " - ";
            }
        }

        BufferedWriter output = null;

        try {
            File file = new File(appConfigs.getMetadataPath(queueState.getQueueItem().getQueueItemId()));
            output = new BufferedWriter(new FileWriter(file));
            output.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return queueState;
    }
}
