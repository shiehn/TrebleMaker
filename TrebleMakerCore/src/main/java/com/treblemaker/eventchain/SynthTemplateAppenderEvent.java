package com.treblemaker.eventchain;

import com.treblemaker.Application;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.SynthTemplate;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.selectors.interfaces.ITemplateSelector;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SynthTemplateAppenderEvent implements IEventChain {

    private ITemplateSelector templateSelector;
    private Integer numOfGeneratedMixes;


    public void init(ITemplateSelector templateSelector, Integer numOfGeneratedMixes) {
        this.templateSelector = templateSelector;
        this.numOfGeneratedMixes = numOfGeneratedMixes;
    }

    @Override
    public QueueState set(QueueState queueState) {
        try {
            //TODO SELECT RANDOM !!! WTF IS THAT
            //TODO THIS NEEDS A WEIGHTER ..

            //List<SynthTemplate> synthTemplates = templateSelector.chooseRandomList(numOfGeneratedMixes);

            //hard code specific template
            SynthTemplate synthTemplate = templateSelector.chooseSpecific(11);

            queueState.getQueueItem().getProgression().getStructure().forEach(pUnit -> {
                pUnit.getProgressionUnitBars().forEach(pBar -> {
                    pBar.setSynthTemplates(Arrays.asList(synthTemplate));
                });
            });

        } catch (Exception e) {
            Application.logger.debug("LOG:", e);
        }

        return queueState;
    }
}
