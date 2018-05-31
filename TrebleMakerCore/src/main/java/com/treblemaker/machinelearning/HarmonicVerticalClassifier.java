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
public class HarmonicVerticalClassifier implements IVerticalClassifier {

    private boolean bypassRatings;

    @Autowired
    public HarmonicVerticalClassifier(@Value("${bypass_harmonic_loop_vertical_ratings}") boolean bypassRatings,
                                      ICompositionTimeSlotDal compositionTimeSlotDal ){
        this.bypassRatings = bypassRatings;
    }

    @Override
    public WeightClass classify(ProgressionUnitBar progressionUnitBar, IWeightable weightable) {

        if(bypassRatings){
            return WeightClass.OK;
        }

        System.out.print("HARMONIC ALT classify");

        String payload = DeployRTransportHelper.createHarmonicVerticalPayload(progressionUnitBar, weightable);


        //"/classify/harmalt/{beat_loop_id}/{harmonic_loop_id}/{harmonic_loop_alt_id}/{synth_template_hi_id}/{synth_template_mid_id}/{synth_template_low_id}"

        String url = LoadBalancer.getInstance().getUrl() + "/classify/harmalt" + payload;
        HttpUtils httpUtils = new HttpUtils();
        String classResult = httpUtils.sendGet(url);

        Application.logger.debug("LOG: RATING_HARM_L_ALT as : " + classResult);

        return WeightClassificationUtils.verticalWeightToClass(classResult);
    }

    @Override
    public WeightClass classify(String neuralRequest) {
        return null;
    }
}
