package com.treblemaker.machinelearning;

import com.treblemaker.Application;
import com.treblemaker.loadbalance.LoadBalancer;
import com.treblemaker.utils.Http.HttpUtils;
import com.treblemaker.weighters.WeightClassificationUtils;
import com.treblemaker.weighters.enums.WeightClass;
import com.treblemaker.constants.*;

import java.util.Map;

public class VolumeLevelClassifier {

    private boolean bypassRatings;

    public VolumeLevelClassifier(boolean bypassRatings) {
        this.bypassRatings = bypassRatings;
    }

    public WeightClass classify(Map<String, Double> potentialMix) {

        if (bypassRatings) {
            return WeightClass.OK;
        }

        StringBuilder requestParams = new StringBuilder();
        requestParams.append("/");
        requestParams.append(potentialMix.get(MixRoles.COMP_HI_FX));
        requestParams.append("/");
        requestParams.append(potentialMix.get(MixRoles.COMP_HI_ALT_FX));
        requestParams.append("/");
        requestParams.append(potentialMix.get(MixRoles.COMP_MID));
        requestParams.append("/");
        requestParams.append(potentialMix.get(MixRoles.COMP_MID_ALT));
        requestParams.append("/");
        requestParams.append(potentialMix.get(MixRoles.COMP_LOW));
        requestParams.append("/");
        requestParams.append(potentialMix.get(MixRoles.COMP_LOW_ALT));
        requestParams.append("/");
        requestParams.append(potentialMix.get(MixRoles.COMP_RHYTHM));
        requestParams.append("/");
        requestParams.append(potentialMix.get(MixRoles.COMP_RHYTHM_ALT));
        requestParams.append("/");
        requestParams.append(potentialMix.get(MixRoles.COMP_HARMONIC));
        requestParams.append("/");
        requestParams.append(potentialMix.get(MixRoles.COMP_HARMONIC_ALT));
        requestParams.append("/");
        requestParams.append(potentialMix.get(MixRoles.COMP_AMBIENCE));
        requestParams.append("/");
        requestParams.append(potentialMix.get(MixRoles.FILLS));
        requestParams.append("/");
        requestParams.append(potentialMix.get(MixRoles.HITS));
        requestParams.append("/");

        String url = LoadBalancer.getInstance().getUrl() + "/classify/volume" + requestParams.toString();
        HttpUtils httpUtils = new HttpUtils();
        String classResult = httpUtils.sendGet(url);

        Application.logger.debug("LOG: RATING_VOLUME as : " + classResult);

        return WeightClassificationUtils.verticalWeightToClass(classResult);
    }
}
