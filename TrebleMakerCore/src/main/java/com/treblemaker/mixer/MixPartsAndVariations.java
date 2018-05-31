package com.treblemaker.mixer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.treblemaker.mixer.MixPartsAndVariations.MixTypes.*;

public class MixPartsAndVariations {

    public enum MixParts {
        audioPartMelodic,
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
        fullMix,
        noBottomMix,
        ambientMix
    }

    public static final Map<MixTypes, String> mixTypeExtentions;
    static {
        mixTypeExtentions = new HashMap<>();
        mixTypeExtentions.put(fullMix, "_1.mp3");
        mixTypeExtentions.put(noBottomMix, "_2.mp3");
        mixTypeExtentions.put(ambientMix, "_3.mp3");
    }

    public static final Map<String, List<String>> mixVariations;
    static {
        mixVariations = new HashMap<>();
        mixVariations.put("fullMix", Arrays.asList(
                MixParts.audioPartMelodic.toString(),
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

        mixVariations.put("noBottomMix", Arrays.asList(
                MixParts.audioPartMelodic.toString(),
                MixParts.audioPartAmbience.toString(),
                MixParts.audioPartHarmonic.toString(),
                MixParts.audioPartHarmonicAlt.toString(),
                MixParts.audioPartHi.toString(),
                MixParts.audioPartAltHi.toString(),
                MixParts.audioPartMid.toString(),
                MixParts.audioPartAltMid.toString(),
                MixParts.audioPartHits.toString(),
                MixParts.audioPartFills.toString(),
                MixParts.audioPartSnare.toString(),
                MixParts.audioPartHat.toString()
        ));

        mixVariations.put("ambientMix", Arrays.asList(
                MixParts.audioPartAmbience.toString(),
                MixParts.audioPartHi.toString(),
                MixParts.audioPartAltHi.toString(),
                MixParts.audioPartMid.toString(),
                MixParts.audioPartAltMid.toString(),
                MixParts.audioPartHits.toString(),
                MixParts.audioPartFills.toString()
        ));
    }
}
