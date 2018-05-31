package com.treblemaker.generators;

import com.treblemaker.generators.interfaces.ISynthBassGenerator;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.bassline.BassLineJson;
import com.treblemaker.model.bassline.Bassline;
import com.treblemaker.model.bassline.BasslineWithRating;
import com.treblemaker.model.bassline.Intervals;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.utils.JFugueConvertionUtil;
import com.treblemaker.weighters.basslineweighter.IBasslineWeighter;
import org.jfugue.pattern.Pattern;
import org.jfugue.theory.Chord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SynthBassOptions implements ISynthBassGenerator {

    public static final int BASS_OCTAVE = 3;

    private IBasslineWeighter basslineWeighter;
    private boolean bypassBasslineVerticalRating;
    private Integer numOfGeneratedMixes;

    @Autowired
    public SynthBassOptions(IBasslineWeighter basslineWeighter,
                            @Value("${bypass_bassline_vertical_rating}") boolean bypassBasslineVerticalRating,
                            @Value("${num_of_generated_mixes}") Integer numOfGeneratedMixes
    ) {
        this.basslineWeighter = basslineWeighter;
        this.bypassBasslineVerticalRating = bypassBasslineVerticalRating;
        this.numOfGeneratedMixes = numOfGeneratedMixes;
    }

    public void generateSynthBassOptions(ProgressionUnit progressionUnit, List<Bassline> basslines, HashMap<Integer, List<BasslineWithRating>> basslineMap) {

        boolean skipBar = false;
        for (int i = 0; i < progressionUnit.getProgressionUnitBars().size(); i++) {

            if (!skipBar) {

                HiveChord hiveChordOne = progressionUnit.getProgressionUnitBars().get(i).getChord();
                HiveChord hiveChordTwo = progressionUnit.getProgressionUnitBars().get(i + 1).getChord();

//                String transposedChordNameOne = hiveChordOne.getRawChordName().replace("_", Integer.toString(BASS_OCTAVE)); //TODO THE BASS OCTAVE SHOULD BE CONFIGURABLE (i.e 2)
//                String transposedChordNameTwo = hiveChordTwo.getRawChordName().replace("_", Integer.toString(BASS_OCTAVE)); //TODO THE BASS OCTAVE SHOULD BE CONFIGURABLE

                String transposedChordNameOne = transposeChord(hiveChordOne.getRawChordName(), BASS_OCTAVE);
                String transposedChordNameTwo = transposeChord(hiveChordTwo.getRawChordName(), BASS_OCTAVE);


                Chord chordOne = new Chord(transposedChordNameOne);
                Chord chordTwo = new Chord(transposedChordNameTwo);

                int bassCode = getBassCode(chordOne, chordTwo, BASS_OCTAVE);

                populateBasslinesByCode(basslines, bassCode, basslineMap);
            }
            skipBar = !skipBar;
        }

        skipBar = false;
        for (int i = 0; i < progressionUnit.getProgressionUnitBars().size(); i++) {

            if (!skipBar) {

                HiveChord hiveChordOne = progressionUnit.getProgressionUnitBars().get(i).getChord();
                HiveChord hiveChordTwo = progressionUnit.getProgressionUnitBars().get(i + 1).getChord();

//                String transposedChordNameOne = hiveChordOne.getRawChordName().replace("_", Integer.toString(BASS_OCTAVE)); //TODO THE BASS OCTAVE SHOULD BE CONFIGURABLE (i.e 2)
//                String transposedChordNameTwo = hiveChordTwo.getRawChordName().replace("_", Integer.toString(BASS_OCTAVE)); //TODO THE BASS OCTAVE SHOULD BE CONFIGURABLE

                String transposedChordNameOne = transposeChord(hiveChordOne.getRawChordName(), BASS_OCTAVE);
                String transposedChordNameTwo = transposeChord(hiveChordTwo.getRawChordName(), BASS_OCTAVE);

                Chord chordOne = new Chord(transposedChordNameOne);
                Chord chordTwo = new Chord(transposedChordNameTwo);

                int bassCode = getBassCode(chordOne, chordTwo, BASS_OCTAVE);

                List<BasslineWithRating> basslineWithRatings = basslineWeighter.rateBasslines(bypassBasslineVerticalRating, basslineMap.get(bassCode), progressionUnit.getProgressionUnitBars().get(i), progressionUnit.getProgressionUnitBars().get(i + 1));

                basslineMap.put(bassCode, basslineWithRatings);
            }
            skipBar = !skipBar;
        }
    }

    @Override
    public List<BasslineWithRating> distributeByWeights(List<BasslineWithRating> basslineWithRatings) {

        List<BasslineWithRating> distributedBasslines = new ArrayList<>();

        for (BasslineWithRating basslineWithRating : basslineWithRatings) {

            for (int i = 0; i < basslineWithRating.getTotalWeight(); i++) {
                distributedBasslines.add(basslineWithRating);
            }
        }

        return distributedBasslines;
    }

    private BasslineWithRating selectFromDistributed(List<BasslineWithRating> basslineWithRatings) {
        return basslineWithRatings.get(new Random().nextInt(basslineWithRatings.size()));
    }

    @Override
    public void setSynthBassOptions(ProgressionUnit progressionUnit, List<Bassline> basslines, HashMap<Integer, List<BasslineWithRating>> basslineMap, Integer bpm) {

        HashMap<Integer, Bassline> selectedBasslines = new HashMap<>();

        for (Map.Entry<Integer, List<BasslineWithRating>> mapEntry : basslineMap.entrySet()) {

            int bassCode = mapEntry.getKey();

            List<BasslineWithRating> distrubutedByWeight = distributeByWeights(mapEntry.getValue());

            BasslineWithRating selectedBassline = selectFromDistributed(distrubutedByWeight);

            selectedBasslines.put(bassCode, selectedBassline.getBassline());
        }

        boolean skipBar = false;
        for (int i = 0; i < progressionUnit.getProgressionUnitBars().size(); i++) {

            if (!skipBar) {

                HiveChord hiveChordOne = progressionUnit.getProgressionUnitBars().get(i).getChord();
                HiveChord hiveChordTwo = progressionUnit.getProgressionUnitBars().get(i + 1).getChord();

                String transposedChordNameOne = transposeChord(hiveChordOne.getRawChordName(), BASS_OCTAVE);
                String transposedChordNameTwo = transposeChord(hiveChordTwo.getRawChordName(), BASS_OCTAVE);

                Chord chordOne = new Chord(transposedChordNameOne);
                Chord chordTwo = new Chord(transposedChordNameTwo);

                int bassCode = getBassCode(chordOne, chordTwo, BASS_OCTAVE);

                Bassline selectedBassline = selectedBasslines.get(bassCode);

                Intervals intervalsA = JFugueConvertionUtil.populateIntervals(chordOne, BASS_OCTAVE);
                Intervals intervalsB = JFugueConvertionUtil.populateIntervals(chordTwo, BASS_OCTAVE);

                int[] beats = generateBeatArrayFromBassline(selectedBassline);

                Pattern twoBarBassline = generateBasslinePattern(intervalsA, intervalsB, beats, BASS_OCTAVE, bpm);

                for (int p = 0; p < numOfGeneratedMixes; p++) {
                    progressionUnit.getProgressionUnitBars().get(i).addPatternLow(twoBarBassline);
                    progressionUnit.getProgressionUnitBars().get(i).addPatternLowAlt(twoBarBassline);
                }

                progressionUnit.getProgressionUnitBars().get(i).setBasslineId(selectedBassline.getId());
                progressionUnit.getProgressionUnitBars().get(i + 1).setBasslineId(selectedBassline.getId());
                progressionUnit.getProgressionUnitBars().get(i).setSelectedBassline(selectedBassline);

                //TODO THE MELODIC STUFF IS JUST TACKED ON HERE
                //TODO IT SHOULD BE MOVED TO ANOTHER METHOD
                //SET NOTE MAP USED MELODIC EXTRACTION ..
                List<String> noteMap = convertToNoteMap(beats, intervalsA, intervalsB);
                progressionUnit.getProgressionUnitBars().get(i).setArpeggioLowPositions(noteMap.subList(0, 16));
                progressionUnit.getProgressionUnitBars().get(i + 1).setArpeggioLowPositions(noteMap.subList(16, 32));
            }
            skipBar = !skipBar;
        }
    }


    public int getBassCode(Chord chordOne, Chord chordTwo, int bassOctave) {

        Intervals intervalsA = JFugueConvertionUtil.populateIntervals(chordOne, bassOctave);
        Intervals intervalsB = JFugueConvertionUtil.populateIntervals(chordTwo, bassOctave);

        int bassCode = JFugueConvertionUtil.getBasslineChordsCode(intervalsA, intervalsB);

        return bassCode;
    }


    public void populateBasslinesByCode(List<Bassline> basslineSource, int bassCode, HashMap<Integer, List<BasslineWithRating>> basslineMap) {

        List<BasslineWithRating> basslinesByCode = basslineMap.get(bassCode);

        if (basslinesByCode == null) {
            List<BasslineWithRating> basslineWithRatings = new ArrayList<>();

            basslineSource.forEach(bLine -> {
                if (bLine.getArpeggioPerBar() == bassCode) {
                    BasslineWithRating basslineWithRating = new BasslineWithRating();
                    basslineWithRating.setBassline(bLine);
                    basslineWithRatings.add(basslineWithRating);
                }
            });

            basslineMap.put(bassCode, basslineWithRatings);
        }
    }

    public int[] generateBeatArrayFromBassline(Bassline bassline) {

        BassLineJson bass = bassline.getBassline();

        int[] beats = new int[32];

        for (int i = 0; i < 32; i++) {
            beats[i] = Integer.parseInt(bass.getIntervalByPosition(i));
        }

        return beats;
    }

    public Pattern generateBasslinePattern(Intervals intervalsA, Intervals intervalsB, int[] beats, int bassOctave, Integer bpm) {

        List<String> durations = JFugueConvertionUtil.getDurations(beats);
        List<String> notes = JFugueConvertionUtil.getNotes(beats, intervalsA, intervalsB);


        /*TEST*/
        List<String> map = convertToNoteMap(beats, intervalsA, intervalsB);
        /*END TEST*/


        Pattern jPattern = new Pattern();
        jPattern.setTempo(bpm);
        for (int i = 0; i < notes.size(); i++) {
            jPattern.add(JFugueConvertionUtil.generatePattern(notes.get(i), durations.get(i)));
        }

        return jPattern;
    }

    public List<String> convertToNoteMap(int[] arpeggioList, Intervals intervalsA, Intervals intervalsB) {

        List<String> noteMap = new ArrayList<>();

        List<String> notes = JFugueConvertionUtil.getNotes(arpeggioList, intervalsA, intervalsB);

        List<String> filteredNotes = notes.stream().filter(n -> !n.equalsIgnoreCase("R")).collect(Collectors.toList());

        int noteIndex = 0;
        for (int i = 0; i < arpeggioList.length; i++) {
            if (arpeggioList[i] != 0) {
                if (i > 0) {
                    if (arpeggioList[i] != arpeggioList[i - 1]) {
                        noteMap.add(filteredNotes.get(noteIndex));
                        noteIndex++;
                    } else {
                        noteMap.add("R");
                    }
                } else {
                    noteMap.add(filteredNotes.get(noteIndex));
                    noteIndex++;
                }
            } else {
                noteMap.add("R");
            }
        }

        return noteMap;
    }

    @Override
    public String transposeChord(String chord, int bassOctave) {

        if ((Character.isDigit(chord.charAt(2)) && chord.charAt(1) == '#')
                || (Character.isDigit(chord.charAt(2)) && chord.charAt(1) == 'b')
                || (Character.isDigit(chord.charAt(2)) && chord.charAt(1) == 'B')) {

            char[] charArray = chord.toCharArray();
            charArray[2] = Integer.toString(bassOctave).charAt(0);
            return new String(charArray);

        } else if (chord.charAt(1) == '#' || chord.charAt(1) == 'b' || chord.charAt(1) == 'B') {
            return new StringBuffer(chord).insert(2, bassOctave).toString();
        } else if (!Character.isDigit(chord.charAt(1))) {
            return new StringBuffer(chord).insert(1, bassOctave).toString();
        } else if (Character.isDigit(chord.charAt(1))) {
            char[] charArray = chord.toCharArray();
            charArray[1] = Integer.toString(bassOctave).charAt(0);
            return new String(charArray);
        }

        throw new RuntimeException("UNABLE TO TRANSPOSE CHORD");
    }
}
