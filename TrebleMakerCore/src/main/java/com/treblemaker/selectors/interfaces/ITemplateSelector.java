package com.treblemaker.selectors.interfaces;

import com.treblemaker.model.SynthTemplate;

import java.util.List;

public interface ITemplateSelector {

	List<SynthTemplate> chooseRandomList(int size) throws Exception;

	SynthTemplate chooseRandom() throws Exception;

    SynthTemplate chooseUniqueRandom(List<Integer> ids) throws Exception;

	SynthTemplate chooseSpecific(Integer templateId) throws Exception;
}
