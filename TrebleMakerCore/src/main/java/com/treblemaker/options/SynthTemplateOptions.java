package com.treblemaker.options;

import com.treblemaker.model.SynthTemplate;
import com.treblemaker.model.SynthTemplateOption;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.options.interfaces.ISynthTemplateOptions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SynthTemplateOptions implements ISynthTemplateOptions {

    @Override
    public void setHiSynthOptions(ProgressionUnitBar progressionUnitBar) {

        List<List<SynthTemplateOption>> allHiOptions = new ArrayList<>();

        for (SynthTemplate synthTemplate : progressionUnitBar.getSynthTemplates()) {
            SynthTemplateOption synthHi = new SynthTemplateOption(synthTemplate.getHiSynthId(),
                    synthTemplate.getHiSynthName(),
                    SynthTemplateOption.SynthRoll.SYNTH_HI);

            SynthTemplateOption synthHiAlt = new SynthTemplateOption(synthTemplate.getHiSynthIdAlt(),
                    synthTemplate.getHiSynthNameAlt(),
                    SynthTemplateOption.SynthRoll.SYNTH_HI);

            List<SynthTemplateOption> hiOptions = new ArrayList<>();
            hiOptions.add(synthHi);
            hiOptions.add(synthHiAlt);

            allHiOptions.add(hiOptions);
        }

        progressionUnitBar.setSynthTemplateHiOptions(allHiOptions);
    }

    @Override
    public void setMidSynthOptions(ProgressionUnitBar progressionUnitBar) {

        List<List<SynthTemplateOption>> allMidOptions = new ArrayList<>();

        for (SynthTemplate synthTemplate : progressionUnitBar.getSynthTemplates()) {
            SynthTemplateOption synthMid = new SynthTemplateOption(synthTemplate.getMidSynthId(),
                    synthTemplate.getMidSynthName(),
                    SynthTemplateOption.SynthRoll.SYNTH_MID);

            SynthTemplateOption synthMidAlt = new SynthTemplateOption(synthTemplate.getMidSynthIdAlt(),
                    synthTemplate.getMidSynthNameAlt(),
                    SynthTemplateOption.SynthRoll.SYNTH_MID);

            List<SynthTemplateOption> midOptions = new ArrayList<>();
            midOptions.add(synthMid);
            midOptions.add(synthMidAlt);

            allMidOptions.add(midOptions);
        }

        progressionUnitBar.setSynthTemplateMidOptions(allMidOptions);
    }

    @Override
    public void setLowSynthOptions(ProgressionUnitBar progressionUnitBar) {

        List<List<SynthTemplateOption>> allLowOptions = new ArrayList<>();

        for (SynthTemplate synthTemplate : progressionUnitBar.getSynthTemplates()) {
            SynthTemplateOption synthLow = new SynthTemplateOption(synthTemplate.getLowSynthId(),
                    synthTemplate.getLowSynthName(),
                    SynthTemplateOption.SynthRoll.SYNTH_LOW);

            SynthTemplateOption synthLowAlt = new SynthTemplateOption(synthTemplate.getLowSynthIdAlt(),
                    synthTemplate.getLowSynthNameAlt(),
                    SynthTemplateOption.SynthRoll.SYNTH_LOW);

            List<SynthTemplateOption> lowOptions = new ArrayList<>();
            lowOptions.add(synthLow);
            lowOptions.add(synthLowAlt);

            allLowOptions.add(lowOptions);
        }

        progressionUnitBar.setSynthTemplateLowOptions(allLowOptions);
    }
}
