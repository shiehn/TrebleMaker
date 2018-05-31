package com.treblemaker.eventchain;

import com.treblemaker.constants.MixRoles;
import com.treblemaker.dal.interfaces.ICompositionDal;
import com.treblemaker.dal.interfaces.ICompositionTimeSlotDal;
import com.treblemaker.dal.interfaces.ICompositionTimeSlotLevelsDal;
import com.treblemaker.dal.interfaces.ICompositionTimeSlotPanningDal;
import com.treblemaker.eventchain.interfaces.IAnalyticsEvent;
import com.treblemaker.model.composition.Composition;
import com.treblemaker.model.composition.CompositionTimeSlot;
import com.treblemaker.model.composition.CompositionTimeSlotLevels;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.utils.LoopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class Analytics implements IAnalyticsEvent {

    @Value("${bypass_analytics}")
    boolean bypassAnalytics;

    @Autowired
    private ICompositionDal compositionDal;

    @Autowired
    private ICompositionTimeSlotDal compositionTimeSlotDal;

    @Autowired
    private ICompositionTimeSlotPanningDal compositionTimeSlotPanningDal;

    @Autowired
    private ICompositionTimeSlotLevelsDal compositionTimeSlotLevelsDal;

    @Override
    public QueueState initAnalytics(QueueState queueState) {

        int bpm = queueState.getQueueItem().getBpm();
        int mSecInBar = (int) LoopUtils.getMilliSecondsInBar(bpm);
        int currentBar = 0;

        List<ProgressionUnit> units = queueState.getStructure();

        for (int i = 0; i < units.size(); i++) {

            List<ProgressionUnitBar> progressionBars = units.get(i).getProgressionUnitBars();

            for (int j = 0; j < progressionBars.size(); j++) {

                progressionBars.get(j).getCompositionTimeSlot().setMsecStart(currentBar * mSecInBar);
                progressionBars.get(j).getCompositionTimeSlot().setMsecStop((currentBar + 1) * mSecInBar);
                currentBar++;
            }
        }

        return queueState;
    }

    @Override
    public Integer saveAnalytics(QueueState queueState) {

        if (bypassAnalytics) {
            return -1;
        }

        Composition composition = new Composition();
        composition.setCompositionUid(queueState.getQueueItem().getQueueItemId());
        composition.setDate(new SimpleDateFormat().format(new Date()));

        compositionDal.save(composition);

        List<CompositionTimeSlot> timeSlots = new ArrayList<>();
        for (int i = 0; i < queueState.getStructure().size(); i++) {

            for (ProgressionUnitBar pBar : queueState.getStructure().get(i).getProgressionUnitBars()) {

                pBar.getCompositionTimeSlot().setCompositionId(composition.getId());

                //TODO WTF >> DEAL WITH >>
                //TODO WTF >> DEAL WITH >>
                //TODO WTF >> DEAL WITH >>
                //TODO WTF >> DEAL WITH >>

                //timeSlot.setBeatLoopAltId(timeSlot.getBeatLoopId());
                //timeSlot.setAmbientLoopAltId(timeSlot.getAmbientLoopId());
                //timeSlot.setHarmonicLoopAltId(timeSlot.getHarmonicLoopId());

                compositionTimeSlotDal.save(pBar.getCompositionTimeSlot());

                pBar.getCompositionTimeSlotPanning().setCompositionId(composition.getId());
                pBar.getCompositionTimeSlotPanning().setCompositionTimeSlotId(pBar.getCompositionTimeSlot().getId());

                compositionTimeSlotPanningDal.save(pBar.getCompositionTimeSlotPanning());
            }
        }

        composition.setCompositionTimeSlots(timeSlots);

        saveMixLevels(queueState.getQueueItem().getFinalVolumeLevels(), queueState.getQueueItem().getFinalVolumeTargetMean(), composition.getId());

        return composition.getId();
    }

    private void saveMixLevels(List<Map<String, Double>> finalLevels, double targetVolumeMean, int compositionId) {

        CompositionTimeSlotLevels tsLevels = new CompositionTimeSlotLevels();
        tsLevels.setCompositionId(compositionId);

        tsLevels.setCompHi(finalLevels.get(0).get(MixRoles.COMP_HI_FX));
        tsLevels.setCompHiAlt(finalLevels.get(0).get(MixRoles.COMP_HI_ALT_FX));
        tsLevels.setCompMid(finalLevels.get(0).get(MixRoles.COMP_MID));
        tsLevels.setCompMidAlt(finalLevels.get(0).get(MixRoles.COMP_MID_ALT));
        tsLevels.setCompLow(finalLevels.get(0).get(MixRoles.COMP_LOW));
        tsLevels.setCompLowAlt(finalLevels.get(0).get(MixRoles.COMP_LOW_ALT));
        tsLevels.setBeat(finalLevels.get(0).get(MixRoles.COMP_RHYTHM));
        tsLevels.setBeatAlt(finalLevels.get(0).get(MixRoles.COMP_RHYTHM_ALT));
        tsLevels.setAmbient(finalLevels.get(0).get(MixRoles.COMP_AMBIENCE));
        tsLevels.setHarmonic(finalLevels.get(0).get(MixRoles.COMP_HARMONIC));
        tsLevels.setHarmonicAlt(finalLevels.get(0).get(MixRoles.COMP_HARMONIC_ALT));
        tsLevels.setHits(finalLevels.get(0).get(MixRoles.HITS));
        tsLevels.setFills(finalLevels.get(0).get(MixRoles.FILLS));
        tsLevels.setLevelBeforeMixed(targetVolumeMean);

        compositionTimeSlotLevelsDal.save(tsLevels);
    }
}

