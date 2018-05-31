package com.treblemaker.fx;

import com.treblemaker.dal.interfaces.IFXArpeggioDelayDal;
import com.treblemaker.model.fx.FXArpeggioDelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.treblemaker.fx.FXDelayParameters.*;

@Component
public class FXGenerationUtil {

    private IFXArpeggioDelayDal fxArpeggioDelayDal;

    @Autowired
    public FXGenerationUtil(IFXArpeggioDelayDal fxArpeggioDelayDal) {
        this.fxArpeggioDelayDal = fxArpeggioDelayDal;
    }

    private static final int RECORDS_TO_CREATE = 500;

    public static List<FXArpeggioDelay> generateFxDatabaseRecords(){

        List<FXArpeggioDelay> fxArpeggioDelays = new ArrayList<>();

        for(int i=0; i<RECORDS_TO_CREATE; i++){
            FXArpeggioDelay fxArpeggioDelay = new FXArpeggioDelay();

            double delayType = FXDelayParameters.getNormalizedDataFromDelayType(generateDelayType());
            fxArpeggioDelay.setDelayType(delayType);

            double delayVolume = FXDelayParameters.getNormalizedDataFromEchoVolume(generateDelayVolume());
            fxArpeggioDelay.setDelayVolume(delayVolume);

            fxArpeggioDelay.setMasterVolume(generateMasterVolume());

            fxArpeggioDelays.add(fxArpeggioDelay);
        }

        return fxArpeggioDelays;
    }

    /*
    THIS IS ONLY MEANT TO BE RUN ONCE TO POPULATE THE DATABASE
     */
    public void populateFxDelayDataBase(){

        List<FXArpeggioDelay> fxArpeggioDelayList = generateFxDatabaseRecords();

        fxArpeggioDelayList.forEach(fxArpeggioDelay -> {
            fxArpeggioDelayDal.save(fxArpeggioDelay);
        });
    }

    private static double generateMasterVolume() {
        return FXDelayParameters.VOLUME[new Random().nextInt(FXDelayParameters.VOLUME.length)];
    }

    private static DelayType generateDelayType(){

        List<DelayType> delayTypes = Arrays.asList(new DelayType[]{DelayType.TRIPLET_EIGHTH, DelayType.TRIPLET_QUARTER, DelayType.QUARTER, DelayType.EIGHTH});
        return delayTypes.get(new Random().nextInt(delayTypes.size()));
    }

    private static EchoVolume generateDelayVolume() {

        List<EchoVolume> echoVolumes = Arrays.asList(new EchoVolume[]{EchoVolume.LOW, EchoVolume.MID, EchoVolume.HI});
        return echoVolumes.get(new Random().nextInt(echoVolumes.size()));
    }
}
