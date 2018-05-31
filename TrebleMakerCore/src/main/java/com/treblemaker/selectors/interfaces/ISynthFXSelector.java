package com.treblemaker.selectors.interfaces;
import com.treblemaker.model.fx.FXArpeggioWithRating;

import java.util.List;
import java.util.Map;

public interface ISynthFXSelector {

    Map<Integer, FXArpeggioWithRating> selectSynthFX(Map<Integer, List<FXArpeggioWithRating>> progressionTypeListMap);
}
