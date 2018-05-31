package com.treblemaker.selectors;

import com.treblemaker.Application;
import com.treblemaker.dal.interfaces.ISynthTemplateDal;
import com.treblemaker.model.SynthTemplate;
import com.treblemaker.selectors.interfaces.ITemplateSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class TemplateSelector implements ITemplateSelector {

    @Autowired
    private ISynthTemplateDal synthTemplateDal;

    @Override
    public SynthTemplate chooseRandom() throws Exception {

        SynthTemplate synthTemplate = null;
        List<SynthTemplate> templateList = new ArrayList<>();
        Iterable<SynthTemplate> synthTemplates = synthTemplateDal.findAll();

        //TODO this is temp code to pick a random synth ..
        synthTemplates.iterator().forEachRemaining(templateList::add);

        int randomIndex = (new Random()).nextInt(templateList.size());
        synthTemplate = templateList.get(randomIndex);

        return synthTemplate;
    }

    @Override
    public SynthTemplate chooseUniqueRandom(List<Integer> ids) throws Exception {

        SynthTemplate selection = null;

        while (selection == null) {
            System.out.print("searching for unique template");
            SynthTemplate tempSelection = chooseRandom();
            if (!ids.contains(tempSelection.getId())) {
                selection = tempSelection;
            }
        }

        return selection;
    }

    @Override
    public List<SynthTemplate> chooseRandomList(int size) throws Exception {

        List<SynthTemplate> selections = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            selections.add(chooseUniqueRandom(selections.stream().map(synthTemplate -> synthTemplate.getId()).collect(Collectors.toList())));
        }

        return selections;
    }

    @Override
    public SynthTemplate chooseSpecific(Integer templateId) throws Exception {

        SynthTemplate synthTemplate = null;
        try {
            synthTemplate = synthTemplateDal.findOne(templateId);
        } catch (Exception e) {
            Application.logger.debug("LOG:",e);
        }

        return synthTemplate;
    }
}
