package com.treblemaker.services.spectrumanalysis.util;

import com.treblemaker.services.spectrumanalysis.model.IParametricEq;
import com.treblemaker.services.spectrumanalysis.model.ParametricEqBeatLoop;
import com.treblemaker.services.spectrumanalysis.model.ParametricEqCompositionLayer;
import com.treblemaker.services.spectrumanalysis.model.ParametricEqHarmonicLoop;

public class EqConversion {

    public static ParametricEqBeatLoop convertToParametricEqBeatLoop(IParametricEq parametricEq){
        
        ParametricEqBeatLoop eqBeatLoop = new ParametricEqBeatLoop();

        eqBeatLoop.setFreq20(parametricEq.getFreq20());
        eqBeatLoop.setFreq25(parametricEq.getFreq25());
        eqBeatLoop.setFreq31(parametricEq.getFreq31());
        eqBeatLoop.setFreq40(parametricEq.getFreq40());
        eqBeatLoop.setFreq50(parametricEq.getFreq50());
        eqBeatLoop.setFreq63(parametricEq.getFreq63());
        eqBeatLoop.setFreq80(parametricEq.getFreq80());
        eqBeatLoop.setFreq100(parametricEq.getFreq100());
        eqBeatLoop.setFreq125(parametricEq.getFreq125());
        eqBeatLoop.setFreq160(parametricEq.getFreq160());
        eqBeatLoop.setFreq200(parametricEq.getFreq200());
        eqBeatLoop.setFreq250(parametricEq.getFreq250());
        eqBeatLoop.setFreq315(parametricEq.getFreq315());
        eqBeatLoop.setFreq400(parametricEq.getFreq400());
        eqBeatLoop.setFreq500(parametricEq.getFreq500());
        eqBeatLoop.setFreq630(parametricEq.getFreq630());
        eqBeatLoop.setFreq800(parametricEq.getFreq800());
        eqBeatLoop.setFreq1000(parametricEq.getFreq1000());
        eqBeatLoop.setFreq1250(parametricEq.getFreq1250());
        eqBeatLoop.setFreq1600(parametricEq.getFreq1600());
        eqBeatLoop.setFreq2000(parametricEq.getFreq2000());
        eqBeatLoop.setFreq2500(parametricEq.getFreq2500());
        eqBeatLoop.setFreq3150(parametricEq.getFreq3150());
        eqBeatLoop.setFreq4000(parametricEq.getFreq4000());
        eqBeatLoop.setFreq5000(parametricEq.getFreq5000());
        eqBeatLoop.setFreq6300(parametricEq.getFreq6300());
        eqBeatLoop.setFreq8000(parametricEq.getFreq8000());
        eqBeatLoop.setFreq10000(parametricEq.getFreq10000());
        eqBeatLoop.setFreq12500(parametricEq.getFreq12500());
        eqBeatLoop.setFreq16000(parametricEq.getFreq16000());
        eqBeatLoop.setFreq20000(parametricEq.getFreq20000());

        return eqBeatLoop;
    }

    public static ParametricEqHarmonicLoop convertToParametricEqHarmonicLoop(IParametricEq parametricEq){

        ParametricEqHarmonicLoop eqBeatLoop = new ParametricEqHarmonicLoop();

        eqBeatLoop.setFreq20(parametricEq.getFreq20());
        eqBeatLoop.setFreq25(parametricEq.getFreq25());
        eqBeatLoop.setFreq31(parametricEq.getFreq31());
        eqBeatLoop.setFreq40(parametricEq.getFreq40());
        eqBeatLoop.setFreq50(parametricEq.getFreq50());
        eqBeatLoop.setFreq63(parametricEq.getFreq63());
        eqBeatLoop.setFreq80(parametricEq.getFreq80());
        eqBeatLoop.setFreq100(parametricEq.getFreq100());
        eqBeatLoop.setFreq125(parametricEq.getFreq125());
        eqBeatLoop.setFreq160(parametricEq.getFreq160());
        eqBeatLoop.setFreq200(parametricEq.getFreq200());
        eqBeatLoop.setFreq250(parametricEq.getFreq250());
        eqBeatLoop.setFreq315(parametricEq.getFreq315());
        eqBeatLoop.setFreq400(parametricEq.getFreq400());
        eqBeatLoop.setFreq500(parametricEq.getFreq500());
        eqBeatLoop.setFreq630(parametricEq.getFreq630());
        eqBeatLoop.setFreq800(parametricEq.getFreq800());
        eqBeatLoop.setFreq1000(parametricEq.getFreq1000());
        eqBeatLoop.setFreq1250(parametricEq.getFreq1250());
        eqBeatLoop.setFreq1600(parametricEq.getFreq1600());
        eqBeatLoop.setFreq2000(parametricEq.getFreq2000());
        eqBeatLoop.setFreq2500(parametricEq.getFreq2500());
        eqBeatLoop.setFreq3150(parametricEq.getFreq3150());
        eqBeatLoop.setFreq4000(parametricEq.getFreq4000());
        eqBeatLoop.setFreq5000(parametricEq.getFreq5000());
        eqBeatLoop.setFreq6300(parametricEq.getFreq6300());
        eqBeatLoop.setFreq8000(parametricEq.getFreq8000());
        eqBeatLoop.setFreq10000(parametricEq.getFreq10000());
        eqBeatLoop.setFreq12500(parametricEq.getFreq12500());
        eqBeatLoop.setFreq16000(parametricEq.getFreq16000());
        eqBeatLoop.setFreq20000(parametricEq.getFreq20000());

        return eqBeatLoop;
    }

    public static ParametricEqCompositionLayer convertToParametricEqCompositionLayer(IParametricEq parametricEq){

        ParametricEqCompositionLayer eqBeatLoop = new ParametricEqCompositionLayer();

        eqBeatLoop.setFreq20(parametricEq.getFreq20());
        eqBeatLoop.setFreq25(parametricEq.getFreq25());
        eqBeatLoop.setFreq31(parametricEq.getFreq31());
        eqBeatLoop.setFreq40(parametricEq.getFreq40());
        eqBeatLoop.setFreq50(parametricEq.getFreq50());
        eqBeatLoop.setFreq63(parametricEq.getFreq63());
        eqBeatLoop.setFreq80(parametricEq.getFreq80());
        eqBeatLoop.setFreq100(parametricEq.getFreq100());
        eqBeatLoop.setFreq125(parametricEq.getFreq125());
        eqBeatLoop.setFreq160(parametricEq.getFreq160());
        eqBeatLoop.setFreq200(parametricEq.getFreq200());
        eqBeatLoop.setFreq250(parametricEq.getFreq250());
        eqBeatLoop.setFreq315(parametricEq.getFreq315());
        eqBeatLoop.setFreq400(parametricEq.getFreq400());
        eqBeatLoop.setFreq500(parametricEq.getFreq500());
        eqBeatLoop.setFreq630(parametricEq.getFreq630());
        eqBeatLoop.setFreq800(parametricEq.getFreq800());
        eqBeatLoop.setFreq1000(parametricEq.getFreq1000());
        eqBeatLoop.setFreq1250(parametricEq.getFreq1250());
        eqBeatLoop.setFreq1600(parametricEq.getFreq1600());
        eqBeatLoop.setFreq2000(parametricEq.getFreq2000());
        eqBeatLoop.setFreq2500(parametricEq.getFreq2500());
        eqBeatLoop.setFreq3150(parametricEq.getFreq3150());
        eqBeatLoop.setFreq4000(parametricEq.getFreq4000());
        eqBeatLoop.setFreq5000(parametricEq.getFreq5000());
        eqBeatLoop.setFreq6300(parametricEq.getFreq6300());
        eqBeatLoop.setFreq8000(parametricEq.getFreq8000());
        eqBeatLoop.setFreq10000(parametricEq.getFreq10000());
        eqBeatLoop.setFreq12500(parametricEq.getFreq12500());
        eqBeatLoop.setFreq16000(parametricEq.getFreq16000());
        eqBeatLoop.setFreq20000(parametricEq.getFreq20000());

        return eqBeatLoop;
    }
}
