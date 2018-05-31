package com.treblemaker.generators;

import com.treblemaker.generators.interfaces.IArpeggioGenerator;
import com.treblemaker.model.SourceData;
import com.treblemaker.model.arpeggio.Arpeggio;
import com.treblemaker.model.bassline.Intervals;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.selectors.interfaces.IArpeggioSelector;
import com.treblemaker.utils.JFugueConvertionUtil;
import com.treblemaker.weighters.enums.WeightClass;
import com.treblemaker.weighters.interfaces.IMidiWeighter;
import org.jfugue.pattern.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.treblemaker.model.progressions.ProgressionUnit.ProgressionType;

@Component
public class ArpeggioGenerator implements IArpeggioGenerator {

    private IArpeggioSelector arpeggioSelector;
    private IMidiWeighter midiWeighter;
    private Integer numOfGeneratedMixes;

    @Autowired
    public ArpeggioGenerator(IArpeggioSelector arpeggioSelector, IMidiWeighter midiWeighter, @Value("${num_of_generated_mixes}") Integer numOfGeneratedMixes) {

        this.arpeggioSelector = arpeggioSelector;
        this.midiWeighter = midiWeighter;
        this.numOfGeneratedMixes = numOfGeneratedMixes;
    }

    public List<Integer> generateTemplate(List<ProgressionUnit> progressionUnits, ProgressionType unitType) {

        List<Integer> arpeggioTemplate = new ArrayList<Integer>();

        for (ProgressionUnit progressionUnit : progressionUnits) {

            if (progressionUnit.getType() == unitType) {
                int numOfArrpeggioNotes = arpeggioSelector.SelectArpeggioSpeed();
                for (int i = 0; i < numOfArrpeggioNotes; i++) {

                    int randomNoteIndex = new Random().nextInt(4);
                    arpeggioTemplate.add(randomNoteIndex);
                }

                return arpeggioTemplate;
            }
        }

        return arpeggioTemplate;
    }

    //todo move this ..
    private List<Arpeggio> selectArpeggiosByCode(int code, List<Arpeggio> arpeggioSource) {

        List<Arpeggio> arpeggiosFiltered = new ArrayList<>();
        for (int i = 0; i < arpeggioSource.size(); i++) {

            if (arpeggioSource.get(i).getArpeggioPerBar() == code) {
                arpeggiosFiltered.add(arpeggioSource.get(i));
            }
        }

        return arpeggiosFiltered;
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

    private Pattern convertToJfuguePattern(int[] arpeggioList, Intervals intervalsA, Intervals intervalsB, Integer bpm) {

        List<String> durations = JFugueConvertionUtil.getDurations(arpeggioList);
        List<String> notes = JFugueConvertionUtil.getNotes(arpeggioList, intervalsA, intervalsB);

        if (durations.size() != notes.size()) {
            throw new RuntimeException("different lengths");
        }

        Pattern jPattern = new Pattern();
        jPattern.setTempo(bpm);
        for (int i = 0; i < notes.size(); i++) {
            jPattern.add(JFugueConvertionUtil.generatePattern(notes.get(i), durations.get(i)));
        }

        return jPattern;
    }

    //private Map<ProgressionType, Map<Integer, Arpeggio>> arpeggioOptions = new HashMap<>();

    public Map<Integer, List<Arpeggio>> getArpeggioOptions(List<ProgressionUnit> progressionUnits, ProgressionUnit.ProgressionType unitType, SourceData sourceData) {

        Map<Integer, List<Arpeggio>> codeArppeggiosMap = new HashMap<>();

        int bassOctave = 4;
        List<Integer> arpeggioCodes = new ArrayList<>();

        //first collect arpeggio codes
        for (int i = 0; i < progressionUnits.size(); i++) {

            if (progressionUnits.get(i).getType() == unitType) {

                boolean skip = false;

                for (int j = 0; j < progressionUnits.get(i).getProgressionUnitBars().size(); j++) {

                    if (!skip) {
                        if (j < progressionUnits.get(i).getProgressionUnitBars().size() - 1) {
                            Intervals intervalsA = JFugueConvertionUtil.populateIntervals(progressionUnits.get(i).getProgressionUnitBars().get(j).getjChord(), bassOctave);
                            Intervals intervalsB = JFugueConvertionUtil.populateIntervals(progressionUnits.get(i).getProgressionUnitBars().get(j + 1).getjChord(), bassOctave);

                            int arpeggioCode = JFugueConvertionUtil.getBasslineChordsCode(intervalsA, intervalsB);
                            arpeggioCodes.add(arpeggioCode);
                        }
                    }

                    skip = !skip;
                }
            }
        }

        //select arpeggios for progression_type
        for (int i = 0; i < arpeggioCodes.size(); i++) {
            List<Arpeggio> arpeggio = codeArppeggiosMap.get(arpeggioCodes.get(i));
            if (arpeggio == null) {
                codeArppeggiosMap.put(arpeggioCodes.get(i), selectArpeggiosByCode(arpeggioCodes.get(i), sourceData.getArpeggios()));
            }
        }

        return codeArppeggiosMap;
    }

    @Override
    public List<Arpeggio> distributeWeightedArpeggios(List<Arpeggio> weightedArpeggios) {

        List<Arpeggio> distributedBasslines = new ArrayList<>();

        for (Arpeggio arrpeggioWithRating : weightedArpeggios) {

            for (int i = 0; i < arrpeggioWithRating.getTotalWeight(); i++) {
                distributedBasslines.add(arrpeggioWithRating);
            }
        }

        return distributedBasslines;
    }

    @Override
    public Map<Integer, Arpeggio> selectFromARateDistributedList(Map<Integer, List<Arpeggio>> weightedArpeggios) {

        Map<Integer, Arpeggio> filtered = new HashMap<>();

        for (Entry<Integer, List<Arpeggio>> entry : weightedArpeggios.entrySet()) {

            List<Arpeggio> distributedArpeggios = distributeWeightedArpeggios(entry.getValue());

            Arpeggio selectedArpeggio = distributedArpeggios.get(new Random().nextInt(distributedArpeggios.size()));

            filtered.put(entry.getKey(), selectedArpeggio);
        }

        return filtered;
    }

    @Override
    public void weightArpeggioOptions(List<ProgressionUnit> progressionUnits, ProgressionType unitType, Map<Integer, List<Arpeggio>> arpeggioMap) {

        int bassOctave = 4;

        //first collect arpeggio codes
        for (int i = 0; i < progressionUnits.size(); i++) {

            if (progressionUnits.get(i).getType() == unitType) {

                boolean skip = false;

                for (int j = 0; j < progressionUnits.get(i).getProgressionUnitBars().size(); j++) {

                    if (!skip) {
                        if (j < progressionUnits.get(i).getProgressionUnitBars().size() - 1) {
                            Intervals intervalsA = JFugueConvertionUtil.populateIntervals(progressionUnits.get(i).getProgressionUnitBars().get(j).getjChord(), bassOctave);
                            Intervals intervalsB = JFugueConvertionUtil.populateIntervals(progressionUnits.get(i).getProgressionUnitBars().get(j + 1).getjChord(), bassOctave);

                            int arpeggioCode = JFugueConvertionUtil.getBasslineChordsCode(intervalsA, intervalsB);

                            //FOR EACH ARPEGGIO OPTION WEIGHT IT
                            for (Arpeggio arpeggioOption : arpeggioMap.get(arpeggioCode)) {

                                WeightClass weightClass = midiWeighter.getWeight(progressionUnits.get(i).getProgressionUnitBars().get(j),
                                        progressionUnits.get(i).getProgressionUnitBars().get(j + 1),
                                        progressionUnits.get(i).getProgressionUnitBars().get(j).getSelectedBassline(),
                                        arpeggioOption);

                                arpeggioOption.incrementWeight(weightClass);
                            }
                        }
                    }

                    skip = !skip;
                }
            }
        }
    }

    @Override
    public void setPatternWithTemplate(List<ProgressionUnit> progressionUnits, ProgressionType unitType, Map<Integer, Arpeggio> selectedArpeggios, SourceData sourceData, Integer bpm) {

        int bassOctave = 4;
        for (int i = 0; i < progressionUnits.size(); i++) {

            if (progressionUnits.get(i).getType() == unitType) {

                boolean skip = false;

                for (int j = 0; j < progressionUnits.get(i).getProgressionUnitBars().size(); j++) {

                    if (!skip) {
                        if (j < progressionUnits.get(i).getProgressionUnitBars().size() - 1) {

                            Intervals intervalsA = JFugueConvertionUtil.populateIntervals(progressionUnits.get(i).getProgressionUnitBars().get(j).getjChord(), bassOctave);
                            Intervals intervalsB = JFugueConvertionUtil.populateIntervals(progressionUnits.get(i).getProgressionUnitBars().get(j + 1).getjChord(), bassOctave);

                            int arpeggioCode = JFugueConvertionUtil.getBasslineChordsCode(intervalsA, intervalsB);

                            Arpeggio arpeggio = selectedArpeggios.get(arpeggioCode);

                            Pattern jPattern = convertToJfuguePattern(arpeggio.getArpeggioJson().getArpeggio(), intervalsA, intervalsB, bpm);

                            //ITS POSSIBLE THIS LOOP SHOULD WRAP THE ENTIRE FUNCTION .. THIS IS ADDING THE
                            //SAME PATTERN TO EACH INDEX
                            for(int t=0; t<numOfGeneratedMixes; t++) {
                                progressionUnits.get(i).getProgressionUnitBars().get(j).addPatternHi(jPattern);
                                progressionUnits.get(i).getProgressionUnitBars().get(j).addPatternHiAlt(jPattern);
                            }

                            //THIS IS NEEDED FOR ANALYTICS ..
                            progressionUnits.get(i).getProgressionUnitBars().get(j).setArpeggioId(arpeggio.getId());
                            progressionUnits.get(i).getProgressionUnitBars().get(j + 1).setArpeggioId(arpeggio.getId());

                            //TODO THE MELODIC STUFF IS JUST TACKED ON HERE
                            //TODO IT SHOULD BE MOVED TO ANOTHER METHOD
                            //SET NOTE MAP USED MELODIC EXTRACTION ..
                            List<String> noteMap = convertToNoteMap(arpeggio.getArpeggioJson().getArpeggio(), intervalsA, intervalsB);
                            progressionUnits.get(i).getProgressionUnitBars().get(j).setArpeggioHiPositions(noteMap.subList(0, 16));
                            progressionUnits.get(i).getProgressionUnitBars().get(j + 1).setArpeggioHiPositions(noteMap.subList(16, 32));
                        }
                    }

                    skip = !skip;
                }
            }
        }
    }
}
