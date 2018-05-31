package com.treblemaker.options;

import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.ProcessingState;
import com.treblemaker.model.types.ProcessingPattern;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OptionsFilter {

    public List<HarmonicLoop> filterByProcessingState(List<HarmonicLoop> harmonicLoops, ProcessingState processingState) {

        if (processingState.getProcessingPattern() == ProcessingPattern.TWO_BAR) {

            switch (processingState.getCurrentState()) {
                case C1_L3:
                    return harmonicLoops.stream().filter(harmonicLoop -> !harmonicLoop.getBarCount().equals(4)).collect(Collectors.toList());
                case C2_L4:
                    return harmonicLoops.stream().filter(harmonicLoop -> harmonicLoop.getBarCount().equals(1)).collect(Collectors.toList());
                case C4_L2:
                    return harmonicLoops.stream().filter(harmonicLoop -> harmonicLoop.getBarCount().equals(1)).collect(Collectors.toList());
                default:
                    return harmonicLoops;
            }
        } else if (processingState.getProcessingPattern() == ProcessingPattern.FOUR_BAR) {
            switch (processingState.getCurrentState()) {
                case C1_L1:
                    return harmonicLoops.stream().filter(harmonicLoop -> harmonicLoop.getBarCount().equals(4)).collect(Collectors.toList());
                default:
                    return harmonicLoops;
            }
        }

        throw new RuntimeException("No Processing State set!");
    }
}
