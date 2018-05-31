package com.treblemaker.extractors.pitchextraction;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import com.treblemaker.Application;
import com.treblemaker.extractors.model.DetectionResult;
import com.treblemaker.model.PitchExtractions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PitchDetectionHandlerImp implements PitchDetectionHandler {

    private static final double CONFIDENCE_CUT_OFF = 0.95;

    private List<DetectionResult> detectionResults = new ArrayList<>();

    private FrequencyToPitchConversion frequencyToPitchConversion = new FrequencyToPitchConversion();

    public List<PitchExtractions> getPitchExtractions(double secondsInBar, int barCount) {

        double sixteenthWindow = secondsInBar / 16;
        int numOfPos = 16 * barCount;

        List<List<String>> windowMatrix = getWindowMarix(barCount);

        for (int i = 1; i < numOfPos + 1; i++) {

            double windowStart = (i - 1) * sixteenthWindow;
            double windowEnd = i * sixteenthWindow;

            Application.logger.debug("LOG: Window" + i + " " + (i - 1) * sixteenthWindow + ":" + i * sixteenthWindow);

            List<DetectionResult> detectionResultsForCurrentWindow = detectionResults.stream().filter(detectionResult -> (detectionResult.getTimeStamp() >= windowStart && detectionResult.getTimeStamp() < windowEnd) && detectionResult.getProbability() >= CONFIDENCE_CUT_OFF).collect(Collectors.toList());

            for (DetectionResult currentDetection : detectionResultsForCurrentWindow) {
                String pitch = frequencyToPitchConversion.getPitchName(currentDetection.getPitch());
                if (!windowMatrix.get(i - 1).contains(pitch)) {
                    windowMatrix.get(i - 1).add(pitch);
                }
            }
        }

        List<PitchExtractions> pitchExtractions = convertToPitchExtractions(barCount, windowMatrix);

        return pitchExtractions;
    }

    public List<PitchExtractions> convertToPitchExtractions(int barCount, List<List<String>> windowMatrix) {
        List<PitchExtractions> pitchExtractions = new ArrayList<>();

        if (barCount == 1) {
            PitchExtractions barOne = new PitchExtractions();
            barOne.setOneOne(String.join("-", windowMatrix.get(0)));
            barOne.setOneTwo(String.join("-", windowMatrix.get(1)));
            barOne.setOneThree(String.join("-", windowMatrix.get(2)));
            barOne.setOneFour(String.join("-", windowMatrix.get(3)));

            barOne.setTwoOne(String.join("-", windowMatrix.get(4)));
            barOne.setTwoTwo(String.join("-", windowMatrix.get(5)));
            barOne.setTwoThree(String.join("-", windowMatrix.get(6)));
            barOne.setTwoFour(String.join("-", windowMatrix.get(7)));

            barOne.setThreeOne(String.join("-", windowMatrix.get(8)));
            barOne.setThreeTwo(String.join("-", windowMatrix.get(9)));
            barOne.setThreeThree(String.join("-", windowMatrix.get(10)));
            barOne.setThreeFour(String.join("-", windowMatrix.get(11)));

            barOne.setFourOne(String.join("-", windowMatrix.get(12)));
            barOne.setFourTwo(String.join("-", windowMatrix.get(13)));
            barOne.setFourThree(String.join("-", windowMatrix.get(14)));
            barOne.setFourFour(String.join("-", windowMatrix.get(15)));

            pitchExtractions.add(barOne);
        } else if (barCount == 2) {
            PitchExtractions barOne = new PitchExtractions();
            barOne.setOneOne(String.join("-", windowMatrix.get(0)));
            barOne.setOneTwo(String.join("-", windowMatrix.get(1)));
            barOne.setOneThree(String.join("-", windowMatrix.get(2)));
            barOne.setOneFour(String.join("-", windowMatrix.get(3)));

            barOne.setTwoOne(String.join("-", windowMatrix.get(4)));
            barOne.setTwoTwo(String.join("-", windowMatrix.get(5)));
            barOne.setTwoThree(String.join("-", windowMatrix.get(6)));
            barOne.setTwoFour(String.join("-", windowMatrix.get(7)));

            barOne.setThreeOne(String.join("-", windowMatrix.get(8)));
            barOne.setThreeTwo(String.join("-", windowMatrix.get(9)));
            barOne.setThreeThree(String.join("-", windowMatrix.get(10)));
            barOne.setThreeFour(String.join("-", windowMatrix.get(11)));

            barOne.setFourOne(String.join("-", windowMatrix.get(12)));
            barOne.setFourTwo(String.join("-", windowMatrix.get(13)));
            barOne.setFourThree(String.join("-", windowMatrix.get(14)));
            barOne.setFourFour(String.join("-", windowMatrix.get(15)));

            PitchExtractions barTwo = new PitchExtractions();
            barTwo.setOneOne(String.join("-", windowMatrix.get(16)));
            barTwo.setOneTwo(String.join("-", windowMatrix.get(17)));
            barTwo.setOneThree(String.join("-", windowMatrix.get(18)));
            barTwo.setOneFour(String.join("-", windowMatrix.get(19)));

            barTwo.setTwoOne(String.join("-", windowMatrix.get(20)));
            barTwo.setTwoTwo(String.join("-", windowMatrix.get(21)));
            barTwo.setTwoThree(String.join("-", windowMatrix.get(22)));
            barTwo.setTwoFour(String.join("-", windowMatrix.get(23)));

            barTwo.setThreeOne(String.join("-", windowMatrix.get(24)));
            barTwo.setThreeTwo(String.join("-", windowMatrix.get(25)));
            barTwo.setThreeThree(String.join("-", windowMatrix.get(26)));
            barTwo.setThreeFour(String.join("-", windowMatrix.get(27)));

            barTwo.setFourOne(String.join("-", windowMatrix.get(28)));
            barTwo.setFourTwo(String.join("-", windowMatrix.get(29)));
            barTwo.setFourThree(String.join("-", windowMatrix.get(30)));
            barTwo.setFourFour(String.join("-", windowMatrix.get(31)));

            pitchExtractions.add(barOne);
            pitchExtractions.add(barTwo);

        } else if (barCount == 4) {

            PitchExtractions barOne = new PitchExtractions();
            barOne.setOneOne(String.join("-", windowMatrix.get(0)));
            barOne.setOneTwo(String.join("-", windowMatrix.get(1)));
            barOne.setOneThree(String.join("-", windowMatrix.get(2)));
            barOne.setOneFour(String.join("-", windowMatrix.get(3)));

            barOne.setTwoOne(String.join("-", windowMatrix.get(4)));
            barOne.setTwoTwo(String.join("-", windowMatrix.get(5)));
            barOne.setTwoThree(String.join("-", windowMatrix.get(6)));
            barOne.setTwoFour(String.join("-", windowMatrix.get(7)));

            barOne.setThreeOne(String.join("-", windowMatrix.get(8)));
            barOne.setThreeTwo(String.join("-", windowMatrix.get(9)));
            barOne.setThreeThree(String.join("-", windowMatrix.get(10)));
            barOne.setThreeFour(String.join("-", windowMatrix.get(11)));

            barOne.setFourOne(String.join("-", windowMatrix.get(12)));
            barOne.setFourTwo(String.join("-", windowMatrix.get(13)));
            barOne.setFourThree(String.join("-", windowMatrix.get(14)));
            barOne.setFourFour(String.join("-", windowMatrix.get(15)));

            PitchExtractions barTwo = new PitchExtractions();
            barTwo.setOneOne(String.join("-", windowMatrix.get(16)));
            barTwo.setOneTwo(String.join("-", windowMatrix.get(17)));
            barTwo.setOneThree(String.join("-", windowMatrix.get(18)));
            barTwo.setOneFour(String.join("-", windowMatrix.get(19)));

            barTwo.setTwoOne(String.join("-", windowMatrix.get(20)));
            barTwo.setTwoTwo(String.join("-", windowMatrix.get(21)));
            barTwo.setTwoThree(String.join("-", windowMatrix.get(22)));
            barTwo.setTwoFour(String.join("-", windowMatrix.get(23)));

            barTwo.setThreeOne(String.join("-", windowMatrix.get(24)));
            barTwo.setThreeTwo(String.join("-", windowMatrix.get(25)));
            barTwo.setThreeThree(String.join("-", windowMatrix.get(26)));
            barTwo.setThreeFour(String.join("-", windowMatrix.get(27)));

            barTwo.setFourOne(String.join("-", windowMatrix.get(28)));
            barTwo.setFourTwo(String.join("-", windowMatrix.get(29)));
            barTwo.setFourThree(String.join("-", windowMatrix.get(30)));
            barTwo.setFourFour(String.join("-", windowMatrix.get(31)));

            PitchExtractions barThree = new PitchExtractions();
            barThree.setOneOne(String.join("-", windowMatrix.get(32)));
            barThree.setOneTwo(String.join("-", windowMatrix.get(33)));
            barThree.setOneThree(String.join("-", windowMatrix.get(34)));
            barThree.setOneFour(String.join("-", windowMatrix.get(35)));

            barThree.setTwoOne(String.join("-", windowMatrix.get(36)));
            barThree.setTwoTwo(String.join("-", windowMatrix.get(37)));
            barThree.setTwoThree(String.join("-", windowMatrix.get(38)));
            barThree.setTwoFour(String.join("-", windowMatrix.get(39)));

            barThree.setThreeOne(String.join("-", windowMatrix.get(40)));
            barThree.setThreeTwo(String.join("-", windowMatrix.get(41)));
            barThree.setThreeThree(String.join("-", windowMatrix.get(42)));
            barThree.setThreeFour(String.join("-", windowMatrix.get(43)));

            barThree.setFourOne(String.join("-", windowMatrix.get(44)));
            barThree.setFourTwo(String.join("-", windowMatrix.get(45)));
            barThree.setFourThree(String.join("-", windowMatrix.get(46)));
            barThree.setFourFour(String.join("-", windowMatrix.get(47)));

            PitchExtractions barFour = new PitchExtractions();
            barFour.setOneOne(String.join("-", windowMatrix.get(48)));
            barFour.setOneTwo(String.join("-", windowMatrix.get(49)));
            barFour.setOneThree(String.join("-", windowMatrix.get(50)));
            barFour.setOneFour(String.join("-", windowMatrix.get(51)));

            barFour.setTwoOne(String.join("-", windowMatrix.get(52)));
            barFour.setTwoTwo(String.join("-", windowMatrix.get(53)));
            barFour.setTwoThree(String.join("-", windowMatrix.get(54)));
            barFour.setTwoFour(String.join("-", windowMatrix.get(55)));

            barFour.setThreeOne(String.join("-", windowMatrix.get(56)));
            barFour.setThreeTwo(String.join("-", windowMatrix.get(57)));
            barFour.setThreeThree(String.join("-", windowMatrix.get(58)));
            barFour.setThreeFour(String.join("-", windowMatrix.get(59)));

            barFour.setFourOne(String.join("-", windowMatrix.get(60)));
            barFour.setFourTwo(String.join("-", windowMatrix.get(61)));
            barFour.setFourThree(String.join("-", windowMatrix.get(62)));
            barFour.setFourFour(String.join("-", windowMatrix.get(63)));

            pitchExtractions.add(barOne);
            pitchExtractions.add(barTwo);
            pitchExtractions.add(barThree);
            pitchExtractions.add(barFour);
        }
        return pitchExtractions;
    }

    @Override
    public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
        if (pitchDetectionResult.getPitch() != -1) {
            double timeStamp = audioEvent.getTimeStamp();
            float pitch = pitchDetectionResult.getPitch();
            float probability = pitchDetectionResult.getProbability();
            double rms = audioEvent.getRMS() * 100;

            DetectionResult detectionResult = new DetectionResult();
            detectionResult.setTimeStamp(timeStamp);
            detectionResult.setPitch(pitch);
            detectionResult.setProbability(probability);
            detectionResult.setRms(rms);

            detectionResults.add(detectionResult);

            String message = String.format("Pitch detected at %.2fs: %.2fHz ( %.2f probability, RMS: %.5f )\n", timeStamp, pitch, probability, rms);
            Application.logger.debug("LOG:" +  message);
        }
    }

    // /////////// THESE  SHOULD BE IN A UTIL ...

//    public List<String>[] insertIntoWindowMatrix(List<String>[] windowMatrix) {
//        return windowMatrix;
//    }

    public List<List<String>> getWindowMarix(int barCount) {

        List<List<String>> windows = new ArrayList<>();

        if (barCount == 1) {
            for (int i = 0; i < 16; i++) {
                windows.add(new ArrayList<>());
            }
        } else if (barCount == 2) {
            for (int i = 0; i < 32; i++) {
                windows.add(new ArrayList<>());
            }
        } else if (barCount == 4) {
            for (int i = 0; i < 64; i++) {
                windows.add(new ArrayList<>());
            }
        } else {
            throw new RuntimeException("unknown bar count");
        }

        return windows;
    }

    // ////////// UTIL NO?


}