package com.treblemaker.selectors;

import com.treblemaker.model.SynthTemplateOption;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.selectors.interfaces.ISynthTemplatePrimeOrAltSelector;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.model.progressions.ProgressionUnit;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SynthTemplatePrimeOrAltSelector implements ISynthTemplatePrimeOrAltSelector {

	public void setHiSynths(ProgressionUnitBar progressionUnitBar){

		for(int i=0; i<progressionUnitBar.getSynthTemplateHiOptions().size(); i++){
			SynthTemplateOption hiSynth = progressionUnitBar.getSynthTemplateHiOptions().get(i).get(0);
			SynthTemplateOption hiAltSynth = progressionUnitBar.getSynthTemplateHiOptions().get(i).get(1);

			//SELECT HIGHEST FROM WEIGHTED OPTIONS ..
			if(hiSynth.getWeightClass().getValue() > hiAltSynth.getWeightClass().getValue()){
				progressionUnitBar.addHiSynthId(hiSynth.getId());
			} else if(hiAltSynth.getWeightClass().getValue() > hiSynth.getWeightClass().getValue()){
				progressionUnitBar.addHiSynthId(hiAltSynth.getId());
			} else {
				//TODO: THIS IS WHERE HI & ALT CAN BE TOGGLED!!!
				//TODO: THIS IS WHERE HI & ALT CAN BE TOGGLED!!!
				//if (new Random().nextBoolean()) {
					progressionUnitBar.addHiSynthId(hiSynth.getId());
				//} else {
				//	progressionUnitBar.addHiSynthId(hiAltSynth.getId());
				//}
			}
		}
	}
	
	public void setMidSynths(ProgressionUnitBar progressionUnitBar){

		for(int i=0; i<progressionUnitBar.getSynthTemplateMidOptions().size(); i++) {
			SynthTemplateOption midSynth = progressionUnitBar.getSynthTemplateMidOptions().get(i).get(0);
			SynthTemplateOption midAltSynth = progressionUnitBar.getSynthTemplateMidOptions().get(i).get(1);

			//SELECT HIGHEST FROM WEIGHTED OPTIONS ..
			if (midSynth.getWeightClass().getValue() > midAltSynth.getWeightClass().getValue()) {
				progressionUnitBar.addMidSynthId(midSynth.getId());
			} else if (midAltSynth.getWeightClass().getValue() > midSynth.getWeightClass().getValue()) {
				progressionUnitBar.addMidSynthId(midAltSynth.getId());
			} else {
				//TODO: THIS IS WHERE HI & ALT CAN BE TOGGLED!!!
				//TODO: THIS IS WHERE HI & ALT CAN BE TOGGLED!!!
				//if (new Random().nextBoolean()) {
					progressionUnitBar.addMidSynthId(midSynth.getId());
				//} else {
				//	progressionUnitBar.addMidSynthId(midAltSynth.getId());
				//}
			}
		}
	}
	
	public void setLowSynths(ProgressionUnitBar progressionUnitBar){

		for(int i=0; i<progressionUnitBar.getSynthTemplateLowOptions().size(); i++) {
			SynthTemplateOption lowSynth = progressionUnitBar.getSynthTemplateLowOptions().get(i).get(0);
			SynthTemplateOption lowAltSynth = progressionUnitBar.getSynthTemplateLowOptions().get(i).get(1);

			//SELECT HIGHEST FROM WEIGHTED OPTIONS ..
			if (lowSynth.getWeightClass().getValue() > lowAltSynth.getWeightClass().getValue()) {
				progressionUnitBar.addLowSynthId(lowSynth.getId());
			} else if (lowAltSynth.getWeightClass().getValue() > lowSynth.getWeightClass().getValue()) {
				progressionUnitBar.addLowSynthId(lowAltSynth.getId());
			} else {
				//TODO: THIS IS WHERE HI & ALT CAN BE TOGGLED!!!
				//TODO: THIS IS WHERE HI & ALT CAN BE TOGGLED!!!
				//if (new Random().nextBoolean()) {
					progressionUnitBar.addLowSynthId(lowSynth.getId());
				//} else {
				//	progressionUnitBar.addLowSynthId(lowAltSynth.getId());
				//}
			}
		}
	}
	
	public QueueState setRhythm(QueueState queueState){
		
		for(ProgressionUnit progressionUnit : queueState.getStructure()){

            boolean selection = randomSelection();

            progressionUnit.getProgressionUnitBars().forEach(pBar -> {
                pBar.setAlternativeRhythm(selection);
            });
		}
		
		return queueState;
	} 
	
	public boolean randomSelection(){
	    return new Random().nextBoolean();
	}
}
