package com.treblemaker.machinelearning.helper;

import com.treblemaker.model.commons.ClassificationTypes;
import com.treblemaker.model.interfaces.IWeightable;
import com.treblemaker.model.progressions.ProgressionUnitBar;

public class DeployRTransportHelper {

    public static String createHarmonicVerticalPayload(ProgressionUnitBar pUnitBar, IWeightable weightable){

        String classificationType = ClassificationTypes.HARMONIC_LOOP_ALT;

        String templateId = "null";
        if (pUnitBar != null && pUnitBar.getSynthTemplates() != null && pUnitBar.getSynthTemplates().get(0).getId() != null) {
            templateId = pUnitBar.getSynthTemplates().get(0).getId().toString();
        }

        //TODO THIS IS ONLY RATING THE FIRST SYNTH !!!!
        String synthHiId = Integer.toString(pUnitBar.getHiSynthId().get(0));
        String synthMidId = Integer.toString(pUnitBar.getMidSynthId().get(0));
        String synthLowId = Integer.toString(pUnitBar.getLowSynthId().get(0));
//{beat_loop_id}/{harmonic_loop_id}/{harmonic_loop_alt_id}/{synth_template_hi_id}/{synth_template_mid_id}/{synth_template_low_id}

        StringBuilder payload = new StringBuilder();
        payload.append("/");
        payload.append(pUnitBar.getBeatLoop().getId().toString());
        payload.append("/");
        payload.append(pUnitBar.getHarmonicLoop().getId());
        payload.append("/");
        payload.append(weightable.getId().toString());
        payload.append("/");
        payload.append(synthHiId);
        payload.append("/");
        payload.append(synthMidId);
        payload.append("/");
        payload.append(synthLowId);

        return payload.toString();
    }

    public static String createBeatLoopVerticalPayload(ProgressionUnitBar pUnitBar, IWeightable weightable) {

        String classificationType = ClassificationTypes.BEAT_LOOP_ALT;

        String templateId = "null";
        if (pUnitBar != null && pUnitBar.getSynthTemplates() != null && pUnitBar.getSynthTemplates().get(0).getId() != null) {
            templateId = pUnitBar.getSynthTemplates().get(0).getId().toString();
        }


        //TODO THIS IS ONLY RATING THE FIRST SYNTH !!!!
        String synthHiId = Integer.toString(pUnitBar.getHiSynthId().get(0));
        String synthMidId = Integer.toString(pUnitBar.getMidSynthId().get(0));
        String synthLowId = Integer.toString(pUnitBar.getLowSynthId().get(0));

        //{beat_loop_id}/{beat_loop_alt_id}/{harmonic_loop_id}/{harmonic_loop_alt_id}/{synth_template_hi_id}/{synth_template_mid_id}/{synth_template_low_id}

        StringBuilder payload = new StringBuilder();
        payload.append("/");
        payload.append(pUnitBar.getBeatLoop().getId().toString());
        payload.append("/");
        payload.append(weightable.getId().toString());
        payload.append("/");
        payload.append(pUnitBar.getHarmonicLoop().getId().toString());
        payload.append("/");
        payload.append(pUnitBar.getHarmonicLoopAlt().getId().toString());
        payload.append("/");
        payload.append(synthHiId);
        payload.append("/");
        payload.append(synthMidId);
        payload.append("/");
        payload.append(synthLowId);

        return payload.toString();
    }

    private static String createParam(String prefix, IWeightable suffix){

        String suffixString = "null";

        if(suffix != null && suffix.getId() != null){
            suffixString = suffix.getId().toString();
        }

        return prefix + suffixString;
    }

    private static String createParam(String prefix, String suffix){

        if(suffix == null){
            suffix = "null";
        }

        return prefix + suffix;
    }
}
