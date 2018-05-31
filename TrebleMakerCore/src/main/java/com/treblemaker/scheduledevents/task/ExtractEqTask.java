package com.treblemaker.scheduledevents.task;

import com.treblemaker.Application;
import com.treblemaker.services.spectrumanalysis.FrequencyAnalysis;
import com.treblemaker.services.spectrumanalysis.model.ParametricEqCompositionLayer;
import com.treblemaker.services.spectrumanalysis.model.ParametricEqMedians;
import com.treblemaker.services.spectrumanalysis.util.EqConversion;

import java.util.concurrent.Callable;

public class ExtractEqTask implements Callable<ParametricEqCompositionLayer> {

    private String audioPath;
    private int compositionId;
    private int layerType;

    public ExtractEqTask(String audioPath, int compositionId, int layerType) {
        this.audioPath = audioPath;
        this.compositionId = compositionId;
        this.layerType = layerType;
    }

    @Override
    public ParametricEqCompositionLayer call() throws Exception {

        Application.logger.debug("LOG: CURRENT THREAD : " + Thread.currentThread().getName());

        try {
            FrequencyAnalysis frequencyAnalysis = new FrequencyAnalysis();
            ParametricEqMedians eqMedians = frequencyAnalysis.extractEq(audioPath);
            ParametricEqCompositionLayer parametricEqCompositionLayer = EqConversion.convertToParametricEqCompositionLayer(eqMedians);
            parametricEqCompositionLayer.setCompositionId(compositionId);
            parametricEqCompositionLayer.setLayerType(layerType);

            return parametricEqCompositionLayer;
        } catch (Exception e) {
            Application.logger.debug("LOG:", e);
            return null;
        }
    }
}