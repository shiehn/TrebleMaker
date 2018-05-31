package com.treblemaker.options;

import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.options.interfaces.IBeatLoopOptions;
import com.treblemaker.utils.CloneUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BeatLoopAltOptions implements IBeatLoopOptions {

    @Override
    public void setBeatLoopOptions(ProgressionUnitBar pUnitBar, List<BeatLoop> beatLoops) {

        List<BeatLoop> filteredOptions = beatLoops.stream().filter(beatLoop -> beatLoop.getBarCount() == pUnitBar.getBeatLoop().getBarCount()).collect(Collectors.toList());

        List<BeatLoop> filteredAndClonedOptions = CloneUtils.cloneBeatLoops(filteredOptions);

        pUnitBar.setBeatLoopAltOptions(filteredAndClonedOptions);
    }
}
