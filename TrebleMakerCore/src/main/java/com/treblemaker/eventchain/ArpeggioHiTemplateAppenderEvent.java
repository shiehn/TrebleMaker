package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.generators.interfaces.IArpeggioGenerator;
import com.treblemaker.model.arpeggio.Arpeggio;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.treblemaker.model.progressions.ProgressionUnit.ProgressionType.*;

@Component
public class ArpeggioHiTemplateAppenderEvent implements IEventChain {

    private IArpeggioGenerator arpeggioGenerator;

    @Autowired
    public ArpeggioHiTemplateAppenderEvent(IArpeggioGenerator arpeggioGenerator) {
        this.arpeggioGenerator = arpeggioGenerator;
    }

    public List<Integer> arpeggioTemplateVerse;
    public List<Integer> arpeggioTemplateBridge;
    public List<Integer> arpeggioTemplateChorus;

    @Override
    public QueueState set(QueueState queueState ) {

        Map<ProgressionUnit.ProgressionType, Map<Integer, List<Arpeggio>>> arpeggioOptions = new HashMap<>();

        //GET OPTIONS ..
        Map<Integer, List<Arpeggio>> arpeggioVerseOptions = arpeggioGenerator.getArpeggioOptions(queueState.getStructure(), VERSE, queueState.getDataSource());
        arpeggioOptions.put(VERSE, arpeggioVerseOptions);

        Map<Integer, List<Arpeggio>> arpeggioChorusOptions = arpeggioGenerator.getArpeggioOptions(queueState.getStructure(), CHORUS, queueState.getDataSource());
        arpeggioOptions.put(CHORUS, arpeggioChorusOptions);

        Map<Integer, List<Arpeggio>> arpeggioBridgeOptions = arpeggioGenerator.getArpeggioOptions(queueState.getStructure(), BRIDGE, queueState.getDataSource());
        arpeggioOptions.put(BRIDGE, arpeggioBridgeOptions);

        //RATE OPTIONS ..
        arpeggioGenerator.weightArpeggioOptions(queueState.getStructure(), VERSE, arpeggioVerseOptions);
        arpeggioGenerator.weightArpeggioOptions(queueState.getStructure(), CHORUS, arpeggioChorusOptions);
        arpeggioGenerator.weightArpeggioOptions(queueState.getStructure(), BRIDGE, arpeggioBridgeOptions);

        Map<ProgressionUnit.ProgressionType, Map<Integer, Arpeggio>> selectedArpeggios = new HashMap<>();

        selectedArpeggios.put(VERSE, arpeggioGenerator.selectFromARateDistributedList(arpeggioVerseOptions));
        selectedArpeggios.put(CHORUS, arpeggioGenerator.selectFromARateDistributedList(arpeggioChorusOptions));
        selectedArpeggios.put(BRIDGE, arpeggioGenerator.selectFromARateDistributedList(arpeggioBridgeOptions));

        //SET OPTIONS ..
        arpeggioGenerator.setPatternWithTemplate(queueState.getStructure(), VERSE,  selectedArpeggios.get(VERSE), queueState.getDataSource(), queueState.getQueueItem().getBpm());
        arpeggioGenerator.setPatternWithTemplate(queueState.getStructure(), CHORUS, selectedArpeggios.get(CHORUS), queueState.getDataSource(), queueState.getQueueItem().getBpm());
        arpeggioGenerator.setPatternWithTemplate(queueState.getStructure(), BRIDGE, selectedArpeggios.get(BRIDGE), queueState.getDataSource(), queueState.getQueueItem().getBpm());

        return queueState;
    }
}
