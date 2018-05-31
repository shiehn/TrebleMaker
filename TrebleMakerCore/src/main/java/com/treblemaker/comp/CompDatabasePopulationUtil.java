package com.treblemaker.comp;

import com.treblemaker.dal.interfaces.ICompRhythmDal;
import com.treblemaker.model.comp.CompRhythm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class CompDatabasePopulationUtil {

    @Autowired
    private ICompRhythmDal compRhythmDal;

    public void populateDatabase() {

        int numOfRecordsToPopulate = 500;

        for (int i = 0; i < numOfRecordsToPopulate; i++) {

            String[] compWithDurations = CompGenerationUtil.generateInitialCompPositionsWithDurations();
            String compAsString = String.join(",", compWithDurations);

            CompRhythm compRhythm = new CompRhythm();
            compRhythm.setComp(compAsString);

            compRhythmDal.save(compRhythm);
        }
    }
}
