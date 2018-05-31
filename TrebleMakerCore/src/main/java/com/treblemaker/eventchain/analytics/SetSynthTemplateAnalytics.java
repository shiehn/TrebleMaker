package com.treblemaker.eventchain.analytics;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;

import java.util.List;

public class SetSynthTemplateAnalytics implements IEventChain {

    @Override
    public QueueState set(QueueState queueState) {

        List<ProgressionUnit> progressionUnits = queueState.getStructure();

        for (ProgressionUnit unit : progressionUnits) {

            //SET TEMPLATE ID
            for (int i = 0; i < unit.getBarCount(); i++) {

                Integer templateId = unit.getProgressionUnitBars().get(0).getSynthTemplates().get(0).getId();
                unit.getProgressionUnitBars().get(i).getCompositionTimeSlot().setSynthTemplateId(templateId);
            }

            //SET HI
            unit.getProgressionUnitBars().forEach(pBar -> {
                //NOTE: ALL THE ANALYTICS ARE RECORED TO THE FIRST SYNTH TEMPLATE !!!!
                pBar.getCompositionTimeSlot().setSynthTemplateHiId(pBar.getHiSynthId().get(0));
            });

            //SET MID
            unit.getProgressionUnitBars().forEach(pBar -> {
                //NOTE: ALL THE ANALYTICS ARE RECORED TO THE FIRST SYNTH TEMPLATE !!!!
                pBar.getCompositionTimeSlot().setSynthTemplateMidId(pBar.getMidSynthId().get(0));
            });

            //SET LOW
            unit.getProgressionUnitBars().forEach(pBar -> {
                //NOTE: ALL THE ANALYTICS ARE RECORED TO THE FIRST SYNTH TEMPLATE !!!!
                pBar.getCompositionTimeSlot().setSynthTemplateLowId(pBar.getLowSynthId().get(0));
            });
        }

        return queueState;
    }
}
