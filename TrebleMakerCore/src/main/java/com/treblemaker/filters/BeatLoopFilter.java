package com.treblemaker.filters;

import com.treblemaker.filters.interfaces.IBeatLoopFilter;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.HiveChord;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BeatLoopFilter implements IBeatLoopFilter {

    @Override
    public List<BeatLoop> filterByChords(List<BeatLoop> beatOptions, List<HiveChord> chords) {
        return null;
    }
}
