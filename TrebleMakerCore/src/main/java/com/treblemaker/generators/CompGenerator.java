package com.treblemaker.generators;

import com.treblemaker.generators.interfaces.ICompGenerator;
import com.treblemaker.model.comp.CompRhythm;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.weighters.enums.WeightClass;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.treblemaker.model.progressions.ProgressionUnit.*;

@Component
public class CompGenerator implements ICompGenerator {

    @Override
    public Map<ProgressionType, List<CompRhythm>> weightCompOptions(List<ProgressionUnit> progressionUnits, Map<ProgressionType, List<CompRhythm>> typeAndCompOptions) {

        Map<ProgressionType, List<CompRhythm>> weightedCompOptions = new HashMap<>();

        Iterator iterator = typeAndCompOptions.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry)iterator.next();

            List<CompRhythm> weightedComps = new ArrayList<>();

            List<CompRhythm> unWeightedComps = (List<CompRhythm>)pair.getValue();
            for(CompRhythm compRhythm : unWeightedComps){

                compRhythm.setWeight(WeightClass.OK);
                weightedComps.add(compRhythm);
            }

            weightedCompOptions.put((ProgressionType)pair.getKey(), unWeightedComps);

            iterator.remove(); // avoids a ConcurrentModificationException
        }

        return weightedCompOptions;
    }

    @Override
    public Map<ProgressionType, CompRhythm> selectCompOptionsByWeight(Map<ProgressionType, List<CompRhythm>> typeAndCompOptions) {

        Map<ProgressionType, CompRhythm> selectedCompOptions = new HashMap<>();

        Iterator iterator = typeAndCompOptions.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry)iterator.next();

            List<CompRhythm> unWeightedComps = (List<CompRhythm>)pair.getValue();

            //TODO THIS IS TEMP HACK FIX ME
            CompRhythm selectedComp = unWeightedComps.get(new Random().nextInt(unWeightedComps.size()));

            selectedCompOptions.put((ProgressionType)pair.getKey(), selectedComp);

            iterator.remove(); // avoids a ConcurrentModificationException
        }

        return selectedCompOptions;
    }

    private List<CompRhythm> weightCompRythms(List<CompRhythm> optionsToBeWeighted, List<ProgressionUnit> progressionUnits){

        for (CompRhythm compRythm : optionsToBeWeighted) {

        }

        return null;
    }
}
