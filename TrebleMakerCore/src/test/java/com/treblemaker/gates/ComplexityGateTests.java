package com.treblemaker.gates;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.gates.interfaces.IComplexityThreshold;
import com.treblemaker.gates.interfaces.IGateSetter;
import com.treblemaker.model.types.Composition;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class ComplexityGateTests extends TestCase {

    @Autowired
    private IGateSetter complexityGateSetter;

    @Autowired
    @Qualifier(value = "templateGroupComplexityGate")
    private IComplexityThreshold templateGroupComplexityGate;

    @Test
    public void ShouldReturnTierOne() {

        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.AMBIENCE_LOOP, 21));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.AMBIENCE_LOOP_ALT, 20));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.HARMONIC_LOOP, 5));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.HARMONIC_LOOP_ALT, 16));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.SYNTH_MID, 2));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.SYNTH_MID_ALT, 9));
        Assert.assertTrue(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.BASS_LOOP, 3));
    }

    @Test
    public void ShouldReturnTierTwo() {

        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.AMBIENCE_LOOP, 35));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.AMBIENCE_LOOP_ALT, 35));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.HARMONIC_LOOP, 35));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.HARMONIC_LOOP_ALT, 35));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.SYNTH_MID, 35));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.SYNTH_MID_ALT, 35));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.SYNTH_LOW, 35));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.SYNTH_LOW_ALT, 35));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.BASS_LOOP, 35));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.BEAT_LOOP, 35));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.BEAT_LOOP_ALT, 35));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.FILL_LOOP, 35));
    }

    @Test
    public void ShouldReturnTierThree() {

        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.AMBIENCE_LOOP, 67));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.AMBIENCE_LOOP_ALT, 98));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.HARMONIC_LOOP, 67));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.HARMONIC_LOOP_ALT, 70));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.SYNTH_MID, 75));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.SYNTH_MID_ALT, 84));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.SYNTH_LOW, 77));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.SYNTH_LOW_ALT, 99));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.BASS_LOOP, 91));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.BEAT_LOOP, 66));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.BEAT_LOOP_ALT, 69));
        Assert.assertFalse(templateGroupComplexityGate.shouldGateLayer(Composition.Layer.FILL_LOOP, 89));
    }
}
