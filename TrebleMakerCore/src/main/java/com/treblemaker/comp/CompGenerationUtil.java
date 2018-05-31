package com.treblemaker.comp;

import com.treblemaker.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CompGenerationUtil {

    public static final int[] NUM_OF_COMPS = {1, 2, 3, 4, 5, 6, 7, 8};
    public static final int[] COMP_POSITION = {1, 2, 3, 4, 5, 6, 7, 8
            , 9, 10, 11, 12, 13, 14, 15, 16
            , 17, 18, 19, 20, 21, 22, 23, 24
            , 25, 26, 27, 28, 29, 30, 31, 32};

    public enum CompDuration {
        SIXTEENTH, EIGHTH, DOTTED_EIGHTH, QUARTER, DOTTED_QUARTER, HALF, DOTTED_HALF, WHOLE;
    }

    public static final CompDuration[] compDurations = {CompDuration.SIXTEENTH, CompDuration.EIGHTH, CompDuration.DOTTED_EIGHTH, CompDuration.QUARTER, CompDuration.DOTTED_QUARTER, CompDuration.HALF, CompDuration.DOTTED_HALF, CompDuration.WHOLE};

    public static int getDurationFromJFugue(String jFugue) {

        if (jFugue.toLowerCase().contains("i.")) {
            return 3;
        } else if (jFugue.toLowerCase().contains("q.")) {
            return 6;
        } else if (jFugue.toLowerCase().contains("h.")) {
            return 12;
        } else if (jFugue.toLowerCase().contains("s")) {
            return 1;
        } else if (jFugue.toLowerCase().contains("i")) {
            return 2;
        } else if (jFugue.toLowerCase().contains("q")) {
            return 4;
        } else if (jFugue.toLowerCase().contains("h")) {
            return 8;
        } else if (jFugue.toLowerCase().contains("w")) {
            return 16;
        }

        Application.logger.debug("LOG: parse attemp: " + jFugue);
        throw new RuntimeException("Error: DURATION NOT FOUND for JFUGUE!!");
    }

    public static String getJFugueDuration(int duration) {

        switch (duration) {
            case 1:
                return "s";
            case 2:
                return "i";
            case 3:
                return "i.";
            case 4:
                return "q";
            case 6:
                return "q.";
            case 8:
                return "h";
            case 12:
                return "h.";
            case 16:
                return "w";
        }

        throw new RuntimeException("Error: UNEXPECTED JFUGUE DURATION");
    }

    public static int getCompDurationLength(CompDuration compDuration) {

        switch (compDuration) {
            case SIXTEENTH:
                return 1;
            case EIGHTH:
                return 2;
            case DOTTED_EIGHTH:
                return 3;
            case QUARTER:
                return 4;
            case DOTTED_QUARTER:
                return 6;
            case HALF:
                return 8;
            case DOTTED_HALF:
                return 12;
            case WHOLE:
                return 16;
        }

        throw new RuntimeException("Error: Unknown CompDuration");
    }

    public static String[] generateInitialCompPositionsWithDurations() {

        String[] initialCompPositions = generateInitialCompPositions();
        String[] initialCompPositionsWithDurations = initialCompPositions.clone();

        for (int i = 0; i < initialCompPositions.length; i++) {

            String accent = initialCompPositions[i];
            if (accent.equalsIgnoreCase("c")) {
                //get max positions available until next accent ..
                int maxPositions = 1;
                for (int j = i + 1; j < initialCompPositions.length; j++) {
                    if (initialCompPositions[j].equalsIgnoreCase("x")) {
                        maxPositions++;
                    } else {
                        break;
                    }
                }

                //select a duration up to maxPositions
                int compDurationLength = 0;
                while (true) {
                    CompDuration compDuration = compDurations[new Random().nextInt(compDurations.length)];
                    compDurationLength = getCompDurationLength(compDuration);
                    if (compDurationLength <= maxPositions && doesNotBleed(i, compDurationLength)) {
                        break;
                    }
                }

                for (int k = 0; k < compDurationLength; k++) {
                    if (k == 0) {
                        initialCompPositionsWithDurations[i + k] = getJFugueDuration(compDurationLength);
                    } else {
                        //drone
                        initialCompPositionsWithDurations[i + k] = "d";
                    }
                }
            }
        }

        return initialCompPositionsWithDurations;
    }

    private static boolean doesNotBleed(int i, int compDurationLength) {

        int maxDurationRemaining = 0;
        if (i < 16) {
            maxDurationRemaining = 16 - i;
        } else {
            maxDurationRemaining = 32 - i;
        }

        return compDurationLength <= maxDurationRemaining;
    }

    public static String[] generateInitialCompPositions() {

        String[] initialCompPositions = {
                "x", "x", "x", "x", "x", "x", "x", "x",
                "x", "x", "x", "x", "x", "x", "x", "x",
                "x", "x", "x", "x", "x", "x", "x", "x",
                "x", "x", "x", "x", "x", "x", "x", "x"};

        //select number of comps
        int numOfComps = NUM_OF_COMPS[new Random().nextInt(NUM_OF_COMPS.length)];

        //set comp positions
        int count = 0;
        while (count < numOfComps) {

            int randPosition = new Random().nextInt(initialCompPositions.length);

            if (initialCompPositions[randPosition].equalsIgnoreCase("x")) {
                initialCompPositions[randPosition] = "c";
                count++;
            }
        }

        return initialCompPositions;
    }

    public static String compPositionsToJFugueSequence(String[] compPositions, String chordRootOne, String chordRootTwo) {

        List<String> jfugueArraySequence = new ArrayList<>();

        for (int i = 0; i < compPositions.length; i++) {

            String element = compPositions[i];

            //replace all rest with jfugue rests
            if (element.equalsIgnoreCase("x")) {
                jfugueArraySequence.add("Rs");
            }

            if (!element.equalsIgnoreCase("x") && !element.equalsIgnoreCase("d")) {
                jfugueArraySequence.add(element);
            }
        }

        int currentDurationTotal = 0;
        List<String> chordAppendedList = new ArrayList<>();

        for (int i = 0; i < jfugueArraySequence.size(); i++) {

            String element = jfugueArraySequence.get(i);

            if (element.equalsIgnoreCase("Rs")) {
                chordAppendedList.add(element);
            } else {
                if (currentDurationTotal < 16) {
                    chordAppendedList.add(chordRootOne + element);
                } else {
                    chordAppendedList.add(chordRootTwo + element);
                }
            }

            currentDurationTotal = currentDurationTotal + getDurationFromJFugue(element);
        }

        String jfugueStringSequence = String.join(" ", chordAppendedList);

        return jfugueStringSequence;
    }
}
