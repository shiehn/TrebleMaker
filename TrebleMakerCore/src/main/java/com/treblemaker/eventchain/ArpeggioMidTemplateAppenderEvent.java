package com.treblemaker.eventchain;

import com.treblemaker.dal.interfaces.ICompRhythmDal;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.generators.interfaces.ICompGenerator;
import com.treblemaker.generators.interfaces.ISynthPadGenerator;
import com.treblemaker.model.comp.CompRhythm;
import com.treblemaker.model.queues.QueueState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.treblemaker.model.progressions.ProgressionUnit.ProgressionType;

@Component
public class ArpeggioMidTemplateAppenderEvent implements IEventChain {

    @Autowired
    private ISynthPadGenerator synthPadGenerator;

    @Autowired
    private ICompRhythmDal compRhythmDal;

    @Autowired
    private ICompGenerator compGenerator;

    @Override
    public QueueState set(QueueState queueState) {

        Map<ProgressionType, List<CompRhythm>> typeAndCompOptions = new HashMap<>();

        typeAndCompOptions.put(ProgressionType.VERSE, compRhythmDal.findAll());
        typeAndCompOptions.put(ProgressionType.CHORUS, compRhythmDal.findAll());
        typeAndCompOptions.put(ProgressionType.BRIDGE, compRhythmDal.findAll());

        Map<ProgressionType, List<CompRhythm>> weightedCompOptions = compGenerator.weightCompOptions(queueState.getStructure(), typeAndCompOptions);

        Map<ProgressionType, CompRhythm> selectedCompOptions = compGenerator.selectCompOptionsByWeight(weightedCompOptions);

        queueState.getStructure().forEach(progressionUnit ->
        synthPadGenerator.generateAndSetSynthPad(progressionUnit, selectedCompOptions));

        return queueState;


        /*
        //this generates the comp layer

        Map<ProgressionType, List<CompRhythm>> typeAndCompOptions = new HashMap<>();

        typeAndCompOptions.put(ProgressionType.VERSE, compRhythmDal.findAll());
        typeAndCompOptions.put(ProgressionType.CHORUS, compRhythmDal.findAll());
        typeAndCompOptions.put(ProgressionType.BRIDGE, compRhythmDal.findAll());

        Map<ProgressionType, List<CompRhythm>> weightedCompOptions = compGenerator.weightCompOptions(queueState.getStructure(), typeAndCompOptions);

        Map<ProgressionType, CompRhythm> selectedCompOptions = compGenerator.selectCompOptionsByWeight(weightedCompOptions);

        queueState.getStructure().forEach(progressionUnit ->
        synthPadGenerator.generateAndSetSynthPad(progressionUnit, selectedCompOptions));

        return queueState;
         */
    }
}



