package com.treblemaker.gates;

import com.treblemaker.Application;
import com.treblemaker.gates.interfaces.IComplexityThreshold;
import com.treblemaker.model.types.Composition;

public class TemplateComplexityGate implements IComplexityThreshold {

    int totalTemplateWeight = 70;
    int numberOfTiers = 3;

    private int getTierOne(){
        return totalTemplateWeight / numberOfTiers;
    }

    private int getTierTwo(){
        return (totalTemplateWeight / numberOfTiers) * 2;
    }

    private int getTierThree(){
        return totalTemplateWeight;
    }

    @Override
    public boolean shouldGateLayer(Composition.Layer layer, int unitComplexity) {

        int templateWeight = (int)Math.floor((totalTemplateWeight * unitComplexity)/100);

        if(templateWeight < getTierOne()){

            if(layer == Composition.Layer.AMBIENCE_LOOP ||
                    layer == Composition.Layer.AMBIENCE_LOOP_ALT ||
                    layer == Composition.Layer.HARMONIC_LOOP ||
                    layer == Composition.Layer.HARMONIC_LOOP_ALT ||
                    layer == Composition.Layer.SYNTH_MID ||
                    layer == Composition.Layer.SYNTH_MID_ALT){
                return false;
            }

            return true;

        }else if(templateWeight < getTierTwo()){

            if(layer == Composition.Layer.AMBIENCE_LOOP ||
                    layer == Composition.Layer.AMBIENCE_LOOP_ALT ||
                    layer == Composition.Layer.HARMONIC_LOOP ||
                    layer == Composition.Layer.HARMONIC_LOOP_ALT ||
                    layer == Composition.Layer.SYNTH_MID ||
                    layer == Composition.Layer.SYNTH_MID_ALT ||
                    layer == Composition.Layer.SYNTH_LOW ||
                    layer == Composition.Layer.SYNTH_LOW_ALT ||
                    layer == Composition.Layer.BASS_LOOP ||
                    layer == Composition.Layer.BEAT_LOOP ||
                    layer == Composition.Layer.BEAT_LOOP_ALT ||
                    layer == Composition.Layer.FILL_LOOP){
                return false;
            }

            return true;

        }else if(templateWeight <= getTierThree()){

            return false;
            //37-100%
            //AMBIENCE_LOOP,
            //AMBIENCE_LOOP_ALT,
            //HARMONIC_LOOP,
            //HARMONIC_LOOP_ALT,
            //SYNTH_HIGH,
            //SYNTH_HIGH_ALT,
            //SYNTH_MID,
            //SYNTH_MID_ALT,
            //SYNTH_LOW,
            //SYNTH_LOW_ALT,
            //BASS_LOOP,
            //BEAT_LOOP,
            //BEAT_LOOP_ALT,
            //FILL_LOOP
        }else{
            //ERROR???
            Application.logger.debug("LOG: ERROR : WEIGHT IS NOT FALLING WITHIN A TIER !!! WEIGHT = " + templateWeight);
            return false;
        }
    }

    @Override
    public int getLayerGroupComplexityTotal(Composition.LayerGroup layerGroup) {

        //TODO: steve at any point in time you need access to the complexity sum of any layer group
        //TODO: fill this out ..
        return 0;
    }

    @Override
    public int getLayerGroupComplexityThreshold(Composition.LayerGroup layerGroup) {

        //TODO: you may need to know what the gating threshold actually is at certain times ..
        return 0;
    }
}
