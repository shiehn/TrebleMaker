package com.treblemaker.weighters.basslineweighter;

import com.treblemaker.model.bassline.BasslineWithRating;
import com.treblemaker.model.progressions.ProgressionUnitBar;

import java.util.List;

public interface IBasslineWeighter {

    List<BasslineWithRating> rateBasslines(boolean bypassBasslineVerticalRating, List<BasslineWithRating> basslineWithRatings, ProgressionUnitBar barOne, ProgressionUnitBar barTwo);
}
