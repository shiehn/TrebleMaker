package com.treblemaker.machinelearning;

import com.treblemaker.Application;
import com.treblemaker.dal.interfaces.ICompositionTimeSlotDal;
import com.treblemaker.loadbalance.LoadBalancer;
import com.treblemaker.machinelearning.helper.DeployRTransportHelper;
import com.treblemaker.machinelearning.interfaces.IVerticalClassifier;
import com.treblemaker.model.interfaces.IWeightable;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.utils.Http.HttpUtils;
import com.treblemaker.weighters.WeightClassificationUtils;
import com.treblemaker.weighters.enums.WeightClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BeatLoopVerticalClassifier implements IVerticalClassifier {

    private boolean bypassRatings;

    @Autowired
    public BeatLoopVerticalClassifier(@Value("${bypass_vertical_beat_ratings}") boolean bypassRatings,
                                      ICompositionTimeSlotDal compositionTimeSlotDal ){

        this.bypassRatings = bypassRatings;
    }

    @Override
    public WeightClass classify(ProgressionUnitBar progressionUnitBar, IWeightable weightable) {

        if(bypassRatings){
            return WeightClass.OK;
        }

        String requestParams = DeployRTransportHelper.createBeatLoopVerticalPayload(progressionUnitBar, weightable);

        String url = LoadBalancer.getInstance().getUrl() + "/classify/beatalt" + requestParams;
        HttpUtils httpUtils = new HttpUtils();
        String classResult = httpUtils.sendGet(url);

        Application.logger.debug("LOG: RATING_BEAT_L_ALT as : " + classResult);

        return WeightClassificationUtils.verticalWeightToClass(classResult);
    }

    @Override
    public WeightClass classify(String neuralRequest) {
        return null;
    }
}
