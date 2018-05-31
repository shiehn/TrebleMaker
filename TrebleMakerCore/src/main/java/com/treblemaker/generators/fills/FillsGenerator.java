package com.treblemaker.generators.fills;

import com.treblemaker.generators.interfaces.IFillsGenerator;
import com.treblemaker.model.hitsandfills.Fill;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;

import java.util.ArrayList;
import java.util.List;

import static com.treblemaker.model.progressions.ProgressionUnit.*;

public class FillsGenerator implements IFillsGenerator {

    @Override
    public QueueState setFillsOptions(QueueState queueState) {

        for(Integer i=0; i<queueState.getStructure().size(); i++){

            ProgressionType currentType = queueState.getStructure().get(i).getType();

            if(nextUnitIsNull(queueState.getStructure(),i) || !queueState.getStructure().get(i+1).getType().equals(currentType)){

                //ensure fills are 2 bars in length..
                List<Fill> filteredFills= new ArrayList<>();
                for(Fill fill: queueState.getDataSource().getFills(queueState.getQueueItem().getStationId())){
                    if(fill.getBarCount().equals(2)) {
                        filteredFills.add(fill);
                    }
                }

                //ADD THE FILL OPTIONS HERE ..
                queueState.getStructure().get(i).getProgressionUnitBars().get(2).setFillOptions(filteredFills);
            }
        }

        return queueState;
    }

    private boolean nextUnitIsNull(List<ProgressionUnit> pUnits, int i){
        return i+1 >= pUnits.size();
    }
}
