package com.treblemaker.extractors;

import com.treblemaker.extractors.interfaces.IPatternSetter;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import org.jfugue.pattern.Pattern;
import org.springframework.stereotype.Component;

@Component
public class PatternSetter implements IPatternSetter {

	public void setHiPattern(ProgressionUnitBar progressionUnitBar, Integer bpm) {

		for(int i=0; i<progressionUnitBar.getSynthTemplates().size(); i++) {

		    //BOTH PATTERNS (NORMAL ANd ALT) are already set .. so this unsets either Hi or HiAlt
			if (progressionUnitBar.getHiSynthId().get(i) != progressionUnitBar.getSynthTemplates().get(i).getHiSynthId()) {
				progressionUnitBar.getPatternHi().set(i, generateRests(2, bpm).setTempo(bpm));
			}
		}
	}
	
	public void setAltHiPattern(ProgressionUnitBar progressionUnitBar, Integer bpm) {

		for(int i=0; i<progressionUnitBar.getSynthTemplates().size(); i++) {
            //BOTH PATTERNS (NORMAL ANd ALT) are already set .. so this unsets either Hi or HiAlt
			if (progressionUnitBar.getHiSynthId().get(i) != progressionUnitBar.getSynthTemplates().get(i).getHiSynthIdAlt()) {
				progressionUnitBar.getPatternHiAlt().set(i, generateRests(2, bpm).setTempo(bpm));
			}
		}
	}

	public void setMidPattern(ProgressionUnitBar progressionUnitBar, Integer bpm) {

		for(int i=0; i<progressionUnitBar.getSynthTemplates().size(); i++) {
            //BOTH PATTERNS (NORMAL ANd ALT) are already set .. so this unsets either Hi or HiAlt
			if (progressionUnitBar.getMidSynthId().get(i) != progressionUnitBar.getSynthTemplates().get(i).getMidSynthId()) {
				progressionUnitBar.getPatternMid().set(i, generateRests(2, bpm).setTempo(bpm));
			}
		}
	}
	
	public void setAltMidPattern(ProgressionUnitBar progressionUnitBar, Integer bpm) {

		for(int i=0; i<progressionUnitBar.getSynthTemplates().size(); i++) {
            //BOTH PATTERNS (NORMAL ANd ALT) are already set .. so this unsets either Hi or HiAlt
			if (progressionUnitBar.getMidSynthId().get(i) != progressionUnitBar.getSynthTemplates().get(i).getMidSynthIdAlt()) {
				progressionUnitBar.getPatternMidAlt().set(i, generateRests(2, bpm).setTempo(bpm));
			}
		}
	}

	public void setLowPattern(ProgressionUnitBar progressionUnitBar, Integer bpm) {

		for(int i=0; i<progressionUnitBar.getSynthTemplates().size(); i++) {
            //BOTH PATTERNS (NORMAL ANd ALT) are already set .. so this unsets either Hi or HiAlt
			if (progressionUnitBar.getLowSynthId().get(i) != progressionUnitBar.getSynthTemplates().get(i).getLowSynthId()) {
				progressionUnitBar.getPatternLow().set(i, generateRests(2, bpm).setTempo(bpm));
			}
		}
	}
	
	public void setAltLowPattern(ProgressionUnitBar progressionUnitBar, Integer bpm) {

		for(int i=0; i<progressionUnitBar.getSynthTemplates().size(); i++) {
            //BOTH PATTERNS (NORMAL ANd ALT) are already set .. so this unsets either Hi or HiAlt
			if (progressionUnitBar.getLowSynthId().get(i) != progressionUnitBar.getSynthTemplates().get(i).getLowSynthIdAlt()) {
				progressionUnitBar.getPatternLowAlt().set(i, generateRests(2, bpm).setTempo(bpm));
			}
		}
	}

	public Pattern generateBarRest(Integer bpm){
		return generateRests(1, bpm);
	}

	public Pattern generateRests(int barCount, Integer bpm){
		
		Pattern pattern = new Pattern();
		pattern.setTempo(bpm);
		
		for(int i= 0; i<barCount; i++){
			pattern.add(new Pattern("Rw"));
		}
		
		return pattern;
	}
}
