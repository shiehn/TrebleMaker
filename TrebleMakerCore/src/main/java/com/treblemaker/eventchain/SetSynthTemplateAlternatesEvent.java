package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.options.interfaces.ISynthTemplateOptions;
import com.treblemaker.selectors.interfaces.ISynthTemplatePrimeOrAltSelector;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.weighters.interfaces.ISynthWeighter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.treblemaker.model.SynthTemplateOption.*;

@Component
public class SetSynthTemplateAlternatesEvent implements IEventChain {

	private ISynthTemplatePrimeOrAltSelector primaryOrAlternativeSelector;

	private ISynthTemplateOptions synthTemplateOptions;

	private ISynthWeighter synthWeighter;

	@Autowired
	public void SetSynthTemplateAlternatesEvent(ISynthTemplatePrimeOrAltSelector primaryOrAlternativeSelector, ISynthTemplateOptions synthTemplateOptions, ISynthWeighter synthWeighter){
		this.primaryOrAlternativeSelector = primaryOrAlternativeSelector;
		this.synthTemplateOptions = synthTemplateOptions;
		this.synthWeighter = synthWeighter;
	}

	@Override
	public QueueState set(QueueState queueState ) {
		queueState.getStructure().forEach(pUnit -> pUnit.getProgressionUnitBars().forEach(pBar -> {

			//SET TEMPLATE OPTIONS:
			synthTemplateOptions.setHiSynthOptions(pBar);

			//WEIGHT OPTIONS:
			synthWeighter.setWeights(pBar, SynthRoll.SYNTH_HI);

			//SELECT HIGHEST FROM WEIGHTED OPTIONS .
			primaryOrAlternativeSelector.setHiSynths(pBar);
		}));


		queueState.getStructure().forEach(pUnit -> pUnit.getProgressionUnitBars().forEach(pBar -> {

			//SET TEMPLATE OPTIONS:
			synthTemplateOptions.setMidSynthOptions(pBar);

			//WEIGHT OPTIONS:
			synthWeighter.setWeights(pBar, SynthRoll.SYNTH_MID);

			//SELECT HIGHEST FROM WEIGHTED OPTIONS .
			primaryOrAlternativeSelector.setMidSynths(pBar);
		}));


		queueState.getStructure().forEach(pUnit -> pUnit.getProgressionUnitBars().forEach(pBar -> {

			//SET TEMPLATE OPTIONS:
			synthTemplateOptions.setLowSynthOptions(pBar);

			//WEIGHT OPTIONS:
			synthWeighter.setWeights(pBar, SynthRoll.SYNTH_LOW);

			//SELECT HIGHEST FROM WEIGHTED OPTIONS .
			primaryOrAlternativeSelector.setLowSynths(pBar);
		}));

		queueState = primaryOrAlternativeSelector.setRhythm(queueState);

		return queueState;
	}
}
