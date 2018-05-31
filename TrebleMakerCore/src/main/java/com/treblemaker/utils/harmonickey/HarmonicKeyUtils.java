package com.treblemaker.utils.harmonickey;

import com.treblemaker.model.HiveChord;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnitBar;

import java.util.*;

import static com.treblemaker.model.progressions.ProgressionUnit.ProgressionType;


public class HarmonicKeyUtils {

    private static final List<HiveChord> C_MAJOR = Arrays.asList(
            new HiveChord("cmaj7"),
            new HiveChord("dmin7"),
            new HiveChord("emin7"),
            new HiveChord("fmaj7"),
            new HiveChord("gdom7"),
            new HiveChord("amin7"),
            new HiveChord("bdim")
    );

    private static final List<HiveChord> CS_MAJOR = Arrays.asList(
            new HiveChord("c#maj7"),
            new HiveChord("d#min7"),
            new HiveChord("fmin7"),
            new HiveChord("f#maj7"),
            new HiveChord("g#dom7"),
            new HiveChord("a#min7"),
            new HiveChord("cdim")
    );

    private static final List<HiveChord> D_MAJOR = Arrays.asList(
            new HiveChord("dmaj7"),
            new HiveChord("emin7"),
            new HiveChord("f#min7"),
            new HiveChord("gmaj7"),
            new HiveChord("adom7"),
            new HiveChord("bmin7"),
            new HiveChord("c#dim")
    );

    private static final List<HiveChord> DS_MAJOR = Arrays.asList(
            new HiveChord("d#maj7"),
            new HiveChord("fmin7"),
            new HiveChord("gmin7"),
            new HiveChord("g#maj7"),
            new HiveChord("a#dom7"),
            new HiveChord("cmin7"),
            new HiveChord("ddim")
    );

    private static final List<HiveChord> E_MAJOR = Arrays.asList(
            new HiveChord("emaj7"),
            new HiveChord("f#min7"),
            new HiveChord("g#min7"),
            new HiveChord("amaj7"),
            new HiveChord("bdom7"),
            new HiveChord("c#min7"),
            new HiveChord("d#dim")
    );

    private static final List<HiveChord> F_MAJOR = Arrays.asList(
            new HiveChord("fmaj7"),
            new HiveChord("gmin7"),
            new HiveChord("amin7"),
            new HiveChord("a#maj7"),
            new HiveChord("cdom7"),
            new HiveChord("dmin7"),
            new HiveChord("edim")
    );

    private static final List<HiveChord> FS_MAJOR = Arrays.asList(
            new HiveChord("f#maj7"),
            new HiveChord("g#min7"),
            new HiveChord("a#min7"),
            new HiveChord("bmaj7"),
            new HiveChord("c#dom7"),
            new HiveChord("d#min7"),
            new HiveChord("fdim")
    );

    private static final List<HiveChord> G_MAJOR = Arrays.asList(
            new HiveChord("gmaj7"),
            new HiveChord("amin7"),
            new HiveChord("bmin7"),
            new HiveChord("cmaj7"),
            new HiveChord("ddom7"),
            new HiveChord("emin7"),
            new HiveChord("f#dim")
    );

    private static final List<HiveChord> GS_MAJOR = Arrays.asList(
            new HiveChord("g#maj7"),
            new HiveChord("a#min7"),
            new HiveChord("cmin7"),
            new HiveChord("c#maj7"),
            new HiveChord("d#dom7"),
            new HiveChord("fmin7"),
            new HiveChord("gdim")
    );

    private static final List<HiveChord> A_MAJOR = Arrays.asList(
            new HiveChord("amaj7"),
            new HiveChord("bmin7"),
            new HiveChord("c#min7"),
            new HiveChord("dmaj7"),
            new HiveChord("edom7"),
            new HiveChord("f#min7"),
            new HiveChord("g#dim")
    );

    private static final List<HiveChord> AS_MAJOR = Arrays.asList(
            new HiveChord("a#maj7"),
            new HiveChord("cmin7"),
            new HiveChord("dmin7"),
            new HiveChord("d#maj7"),
            new HiveChord("fdom7"),
            new HiveChord("gmin7"),
            new HiveChord("adim")
    );

    private static final List<HiveChord> B_MAJOR = Arrays.asList(
            new HiveChord("bmaj7"),
            new HiveChord("c#min7"),
            new HiveChord("d#min7"),
            new HiveChord("emaj7"),
            new HiveChord("f#dom7"),
            new HiveChord("g#min7"),
            new HiveChord("a#dim")
    );

    public static HashMap<String, List<HiveChord>> KEYS = new HashMap<String, List<HiveChord>>() {{
        put(KeyNames.C_MAJOR, C_MAJOR);
        put(KeyNames.CS_MAJOR, CS_MAJOR);
        put(KeyNames.D_MAJOR, D_MAJOR);
        put(KeyNames.DS_MAJOR, DS_MAJOR);
        put(KeyNames.E_MAJOR, E_MAJOR);
        put(KeyNames.F_MAJOR, F_MAJOR);
        put(KeyNames.FS_MAJOR, FS_MAJOR);
        put(KeyNames.G_MAJOR, G_MAJOR);
        put(KeyNames.GS_MAJOR, GS_MAJOR);
        put(KeyNames.A_MAJOR, A_MAJOR);
        put(KeyNames.AS_MAJOR, AS_MAJOR);
        put(KeyNames.B_MAJOR, B_MAJOR);
    }};


    public static List<String> findCompatibleKeys(List<HiveChord> hiveChords) {

        List<String> compatibleKeys = new ArrayList<>();

        for (Map.Entry<String, List<HiveChord>> chordList : KEYS.entrySet()) {
            boolean allMatch = true;
            for (HiveChord chordInList : hiveChords) {
                if (!isChordInKey(chordInList, chordList.getValue())) {
                    allMatch = false;
                }
            }

            if (allMatch) {
                compatibleKeys.add(chordList.getKey());
            }
        }

        return compatibleKeys;
    }

    private static boolean isChordInKey(HiveChord chord, List<HiveChord> chordsInKey) {

        boolean allMatch = false;
        for (HiveChord chordInKey : chordsInKey) {
            if (chordInKey.isEqualOrTriadMatch(chord)) {
                allMatch = true;
            }
        }

        return allMatch;
    }

    public static Map<ProgressionUnit.ProgressionType, List<String>> convertProgressionsToChordLists(List<ProgressionUnit> progressionUnits) {

        Map<ProgressionUnit.ProgressionType, List<String>> chordMap = new HashMap<>();

        for (ProgressionUnit progressionUnit : progressionUnits) {

            ProgressionType currentProgressType = progressionUnit.getType();
            List<String> chordsInBar = new ArrayList<>();
            for (ProgressionUnitBar progressionUnitBar : progressionUnit.getProgressionUnitBars()) {
                chordsInBar.add(progressionUnitBar.getChord().getChordName());
            }

            if (chordMap.get(currentProgressType) != null) {
                chordMap.put(currentProgressType, chordsInBar);
            }
        }

        return chordMap;
    }
}
