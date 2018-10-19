package com.treblemaker.mixer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.treblemaker.mixer.MixPartsAndVariations.MixTypes.*;

public class MixPartsAndVariations {

    public enum MixParts {
        audioPartMelodic1,
        audioPartMelodic2,
        audioPartMelodic3,
        audioPartMelodic4,
        audioPartMelodic5,
        audioPartRhythm,
        audioPartRhythmAlt,
        audioPartAmbience,
        audioPartHarmonic,
        audioPartHarmonicAlt,
        audioPartHi,
        audioPartAltHi,
        audioPartMid,
        audioPartAltMid,
        audioPartLow,
        audioPartAltLow,
        audioPartHits,
        audioPartFills,
        audioPartKick,
        audioPartSnare,
        audioPartHat
    }

    public enum MixTypes {
        fullMix1,
        fullMix2,
        fullMix3,
        fullMix4,
        fullMix5,
    }

    public static final Map<MixTypes, String> mixTypeExtentions;
    static {
        mixTypeExtentions = new HashMap<>();
        mixTypeExtentions.put(fullMix1, "_1.mp3");
        mixTypeExtentions.put(fullMix2, "_2.mp3");
        mixTypeExtentions.put(fullMix3, "_3.mp3");
        mixTypeExtentions.put(fullMix4, "_4.mp3");
        mixTypeExtentions.put(fullMix5, "_5.mp3");
    }

    public static final Map<String, List<String>> mixVariations;
    static {
        mixVariations = new HashMap<>();
        mixVariations.put("fullMix1", Arrays.asList(
                MixParts.audioPartMelodic1.toString(),
                MixParts.audioPartRhythm.toString(),
                MixParts.audioPartRhythmAlt.toString(),
                MixParts.audioPartAmbience.toString(),
                MixParts.audioPartHarmonic.toString(),
                MixParts.audioPartHarmonicAlt.toString(),
                MixParts.audioPartHi.toString(),
                MixParts.audioPartAltHi.toString(),
                MixParts.audioPartMid.toString(),
                MixParts.audioPartAltMid.toString(),
                MixParts.audioPartLow.toString(),
                MixParts.audioPartAltLow.toString(),
                MixParts.audioPartHits.toString(),
                MixParts.audioPartFills.toString(),
                MixParts.audioPartKick.toString(),
                MixParts.audioPartSnare.toString(),
                MixParts.audioPartHat.toString()
        ));

        mixVariations.put("fullMix2", Arrays.asList(
                MixParts.audioPartMelodic2.toString(),
                MixParts.audioPartRhythm.toString(),
                MixParts.audioPartRhythmAlt.toString(),
                MixParts.audioPartAmbience.toString(),
                MixParts.audioPartHarmonic.toString(),
                MixParts.audioPartHarmonicAlt.toString(),
                MixParts.audioPartHi.toString(),
                MixParts.audioPartAltHi.toString(),
                MixParts.audioPartMid.toString(),
                MixParts.audioPartAltMid.toString(),
                MixParts.audioPartLow.toString(),
                MixParts.audioPartAltLow.toString(),
                MixParts.audioPartHits.toString(),
                MixParts.audioPartFills.toString(),
                MixParts.audioPartKick.toString(),
                MixParts.audioPartSnare.toString(),
                MixParts.audioPartHat.toString()
        ));

        mixVariations.put("fullMix3", Arrays.asList(
                MixParts.audioPartMelodic3.toString(),
                MixParts.audioPartRhythm.toString(),
                MixParts.audioPartRhythmAlt.toString(),
                MixParts.audioPartAmbience.toString(),
                MixParts.audioPartHarmonic.toString(),
                MixParts.audioPartHarmonicAlt.toString(),
                MixParts.audioPartHi.toString(),
                MixParts.audioPartAltHi.toString(),
                MixParts.audioPartMid.toString(),
                MixParts.audioPartAltMid.toString(),
                MixParts.audioPartLow.toString(),
                MixParts.audioPartAltLow.toString(),
                MixParts.audioPartHits.toString(),
                MixParts.audioPartFills.toString(),
                MixParts.audioPartKick.toString(),
                MixParts.audioPartSnare.toString(),
                MixParts.audioPartHat.toString()
        ));

        mixVariations.put("fullMix4", Arrays.asList(
                MixParts.audioPartMelodic4.toString(),
                MixParts.audioPartRhythm.toString(),
                MixParts.audioPartRhythmAlt.toString(),
                MixParts.audioPartAmbience.toString(),
                MixParts.audioPartHarmonic.toString(),
                MixParts.audioPartHarmonicAlt.toString(),
                MixParts.audioPartHi.toString(),
                MixParts.audioPartAltHi.toString(),
                MixParts.audioPartMid.toString(),
                MixParts.audioPartAltMid.toString(),
                MixParts.audioPartLow.toString(),
                MixParts.audioPartAltLow.toString(),
                MixParts.audioPartHits.toString(),
                MixParts.audioPartFills.toString(),
                MixParts.audioPartKick.toString(),
                MixParts.audioPartSnare.toString(),
                MixParts.audioPartHat.toString()
        ));

        mixVariations.put("fullMix5", Arrays.asList(
                MixParts.audioPartMelodic5.toString(),
                MixParts.audioPartRhythm.toString(),
                MixParts.audioPartRhythmAlt.toString(),
                MixParts.audioPartAmbience.toString(),
                MixParts.audioPartHarmonic.toString(),
                MixParts.audioPartHarmonicAlt.toString(),
                MixParts.audioPartHi.toString(),
                MixParts.audioPartAltHi.toString(),
                MixParts.audioPartMid.toString(),
                MixParts.audioPartAltMid.toString(),
                MixParts.audioPartLow.toString(),
                MixParts.audioPartAltLow.toString(),
                MixParts.audioPartHits.toString(),
                MixParts.audioPartFills.toString(),
                MixParts.audioPartKick.toString(),
                MixParts.audioPartSnare.toString(),
                MixParts.audioPartHat.toString()
        ));
    }
}
