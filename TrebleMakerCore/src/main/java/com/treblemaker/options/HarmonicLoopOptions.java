package com.treblemaker.options;

import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.ProcessingState;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.options.interfaces.IHarmonicLoopOptions;
import com.treblemaker.utils.CloneUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HarmonicLoopOptions implements IHarmonicLoopOptions {

    private OptionsFilter optionsFilter;

    @Autowired
    public HarmonicLoopOptions(OptionsFilter optionsFilter){
        this.optionsFilter = optionsFilter;
    }

    @Override
    public void setHarmonicLoopAltOptions(ProgressionUnitBar progressionUnitBar, List<HarmonicLoop> harmonicLoopOptions) {

        List<HarmonicLoop> harmonicLoopAltOptions = getLoopOptionsFromAChord(progressionUnitBar.getChord(), harmonicLoopOptions);

        if(harmonicLoopAltOptions.size() == 0){
            //if there is no options just add a shim
            harmonicLoopAltOptions.add(harmonicLoopOptions.stream().filter(hl -> hl.getFileName().contains("shim.wav")).findFirst().get());
        }

        List<HarmonicLoop> clonedOptions = CloneUtils.cloneHarmonicLoops(harmonicLoopAltOptions);

        progressionUnitBar.setHarmonicLoopAltOptions(clonedOptions);
    }

    @Override
    public void setHarmonicLoopOptions(ProgressionUnitBar progressionUnitBar, List<HarmonicLoop> harmonicLoopOptions, ProcessingState processingState) {

        List<HarmonicLoop> options = getLoopOptionsFromAChord(progressionUnitBar.getChord(), harmonicLoopOptions);

        //FILTER OPTIONS BY PHASE
        options = optionsFilter.filterByProcessingState(options, processingState);

        List<HarmonicLoop> clonedOptions = CloneUtils.cloneHarmonicLoops(options);

        progressionUnitBar.setHarmonicLoopOptions(clonedOptions);
    }

    @Override
    public List<HarmonicLoop> getLoopOptionsFromHarmonicLoopAndChord(HarmonicLoop harmonicLoop, HiveChord chord, List<HarmonicLoop> harmonicLoopOptions) {

        List<HarmonicLoop> options = new ArrayList<>();

        List<HarmonicLoop> optionsFromHarmonicLoop = getLoopOptionsFromHarmonicLoop(harmonicLoop, harmonicLoopOptions);

        List<HarmonicLoop> optionsFromChords = getLoopOptionsFromAChord(chord, harmonicLoopOptions);

        options.addAll(optionsFromHarmonicLoop);

        for (HarmonicLoop option : optionsFromChords) {

            boolean exisits = optionsFromHarmonicLoop.stream().anyMatch(l -> l.getId().equals(option.getId()));

            if (!exisits) {
                options.add(option);
            }
        }

        List<HarmonicLoop> clonedOptions = CloneUtils.cloneHarmonicLoops(options);

        return clonedOptions;
    }

    @Override
    public List<HarmonicLoop> getLoopOptionsFromHarmonicLoop(HarmonicLoop harmonicLoop, List<HarmonicLoop> harmonicLoopOptions) {

        List<HarmonicLoop> loopOptions = new ArrayList<>();

        if (harmonicLoop.getValidChords() != null && harmonicLoop.getValidChords().size() > 0) {

            for (HiveChord chord : harmonicLoop.getValidChords()) {

                for (HarmonicLoop loop : harmonicLoopOptions) {

                    boolean exisits = loopOptions.stream().anyMatch(l -> l.getId().equals(loop.getId()));

                    if (chord.hasMatchingRoot(loop) && !loopOptions.contains(loop)) {
                        loopOptions.add(loop);
                    }
                }
            }
        }

        List<HarmonicLoop> clonedOptions = CloneUtils.cloneHarmonicLoops(loopOptions);

        return clonedOptions;
    }

    @Override
    public List<HarmonicLoop> getLoopOptionsFromAChord(HiveChord chord, List<HarmonicLoop> harmonicLoopOptions) {

        List<HarmonicLoop> harmonicLoops = new ArrayList<>();

        for (HarmonicLoop harmonicLoop : harmonicLoopOptions) {

            boolean idExists = harmonicLoops.stream().anyMatch(t -> t.getId().equals(harmonicLoop.getId()));

            if (chord.hasMatchingRoot(harmonicLoop) && !idExists) {
                harmonicLoops.add(harmonicLoop);
            }
        }

        List<HarmonicLoop> clonedOptions = CloneUtils.cloneHarmonicLoops(harmonicLoops);

        return clonedOptions;
    }
}
