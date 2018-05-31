package com.treblemaker.eventchain;

import com.treblemaker.Application;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.composition.CompositionTimeSlotPanning;
import com.treblemaker.model.queues.QueueState;
import org.springframework.stereotype.Component;

@Component
public class SetPanningEvent implements IEventChain {

    @Override
    public QueueState set(QueueState queueState) {

        final CompositionTimeSlotPanning compositionTimeSlotPanning = assignPanning(new CompositionTimeSlotPanning());

        queueState.getStructure().forEach(pUnit -> {
            pUnit.getProgressionUnitBars().forEach(pBar -> {

                CompositionTimeSlotPanning ctsPanning = null;

                try {
                    ctsPanning = compositionTimeSlotPanning.clone();
                } catch (CloneNotSupportedException e) {
                    Application.logger.debug("LOG:", e);
                }

                ctsPanning.setCompositionTimeSlotId(pBar.getCompositionTimeSlot().getId());
                ctsPanning.setCompositionId(pBar.getCompositionTimeSlot().getCompositionId());

                //TODO: THIS NEEDS TO BE BUILT OUT WITH MACHINE LEARNING
                /* THIS NEEDS TO BE BUILT OUT WITH MACHINE LEARNING !!!!!!! */

                pBar.setCompositionTimeSlotPanning(ctsPanning);
            });
        });

        return queueState;
    }

    private enum PanPosition {
        LEFT, RIGHT, LEFT_OR_RIGHT
    }

    private CompositionTimeSlotPanning assignPanning(CompositionTimeSlotPanning ctsPanning) {

        ctsPanning.setCompMelody("r_0.1");
        ctsPanning.setCompHi("l_0.3");
        ctsPanning.setCompHiAlt("r_0.3");
        ctsPanning.setCompMid("l_0.3");
        ctsPanning.setCompMidAlt("r_0.3");
        ctsPanning.setCompLow("l_0.3");
        ctsPanning.setCompLowAlt("r_0.3");
        ctsPanning.setBeat("l_0.3");
        ctsPanning.setBeatAlt("r_0.3");
        ctsPanning.setHarmonic("l_0.3");
        ctsPanning.setHarmonicAlt("r_0.3");
        ctsPanning.setAmbient("l_0.3");
        ctsPanning.setFills("r_0.3");
        ctsPanning.setHits("l_0.3");
        ctsPanning.setKick("r_0.3");
        ctsPanning.setHat("l_0.3");
        ctsPanning.setSnare("r_0.3");

        return ctsPanning;
    }

    /*
    private String generatePanCode(PanPosition panPosition) {

        int randomPan = new Random().nextInt(10);

        if (panPosition.equals(PanPosition.LEFT)) {
            return "l_" + ((double) randomPan / 10.0);
        } else if (panPosition.equals(PanPosition.RIGHT)) {
            return "r_" + ((double) randomPan / 10.0);
        } else {

            String position = "";
            if (new Random().nextBoolean()) {
                position = "l_";
            } else {
                position = "r_";
            }

            return position + ((double) randomPan / 10.0);
        }
    }
    */
}
