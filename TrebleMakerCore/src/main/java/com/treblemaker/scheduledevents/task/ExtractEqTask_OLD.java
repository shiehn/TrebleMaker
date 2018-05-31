package com.treblemaker.scheduledevents.task;

import com.treblemaker.Application;
import com.treblemaker.dal.interfaces.IParametricEqCompositionLayerDal;
import com.treblemaker.services.spectrumanalysis.FrequencyAnalysis;
import com.treblemaker.services.spectrumanalysis.model.ParametricEqCompositionLayer;
import com.treblemaker.services.spectrumanalysis.model.ParametricEqMedians;
import com.treblemaker.services.spectrumanalysis.util.EqConversion;

import java.util.concurrent.Callable;

public class ExtractEqTask_OLD implements Callable<Boolean> {

    private String audioPath;
    private int compositionId;
    private int layerType;
    private IParametricEqCompositionLayerDal parametricEqCompositionLayerDal;

    public ExtractEqTask_OLD(String audioPath, int compositionId, int layerType, IParametricEqCompositionLayerDal parametricEqCompositionLayerDal) {
        this.audioPath = audioPath;
        this.compositionId = compositionId;
        this.layerType = layerType;
        this.parametricEqCompositionLayerDal = parametricEqCompositionLayerDal;
    }

    @Override
    public Boolean call() throws Exception {

        Application.logger.debug("LOG: CURRENT THREAD : " + Thread.currentThread().getName());

        try {
            FrequencyAnalysis frequencyAnalysis = new FrequencyAnalysis();
            ParametricEqMedians eqMedians = frequencyAnalysis.extractEq(audioPath);
            ParametricEqCompositionLayer parametricEqCompositionLayer = EqConversion.convertToParametricEqCompositionLayer(eqMedians);
            parametricEqCompositionLayer.setCompositionId(compositionId);
            parametricEqCompositionLayer.setLayerType(layerType);
            parametricEqCompositionLayerDal.save(parametricEqCompositionLayer);
        } catch (Exception e) {
            Application.logger.debug("LOG:", e);
            return false;
        }

        return true;
    }
}