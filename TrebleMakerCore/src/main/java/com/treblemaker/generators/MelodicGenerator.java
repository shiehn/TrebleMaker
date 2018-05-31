package com.treblemaker.generators;

import com.treblemaker.extractors.model.HarmonyExtraction;
import com.treblemaker.model.RhythmicAccents;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.progressions.ProgressionUnit.ProgressionType;
import com.treblemaker.model.progressions.ProgressionUnitBar;
import com.treblemaker.model.queues.QueueState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MelodicGenerator {

    public QueueState setMelodicPositions(QueueState queueState) {

        for (ProgressionUnit pUnit : queueState.getStructure()) {

            for (int barPosition = 0; barPosition < pUnit.getProgressionUnitBars().size(); barPosition++) {

                ProgressionUnitBar pBar = pUnit.getProgressionUnitBars().get(barPosition);

                RhythmicAccents beatAccents = getBeatLoopBarAccents(pBar, barPosition);

                RhythmicAccents beatAltAccents = getBeatLoopAltBarAccents(pBar, barPosition);

                RhythmicAccents harmAccents = getHarmonicLoopBarAccents(pBar, barPosition);

                RhythmicAccents harmAltAccents = getHarmonicLoopAltBarAccents(pBar, barPosition);

                List<RhythmicAccents> accents = new ArrayList<>();

                if (beatAccents != null) {
                    accents.add(beatAccents);
                }

                if (beatAltAccents != null) {
                    accents.add(beatAltAccents);
                }

                if (harmAccents != null) {
                    accents.add(harmAccents);
                }

                if (harmAltAccents != null) {
                    accents.add(harmAltAccents);
                }

                RhythmicAccents rhythmicAccents = sumRhythmicAccents(accents);

                Integer meanFromAccents = extractMeanfromAccents(rhythmicAccents);

                RhythmicAccents accentsAboveMean = setAccentsAboveMean(rhythmicAccents, meanFromAccents);

                pUnit.getProgressionUnitBars().get(barPosition).setMelodicPositions(accentsAboveMean);
            }
        }

        return queueState;
    }

    public RhythmicAccents getHarmonicLoopAltBarAccents(ProgressionUnitBar pBar, int barPosition) {
        if (pBar.getHarmonicLoopAlt() != null
                && pBar.getHarmonicLoopAlt().getRhythmicAccents() != null) {

            switch (barPosition) {
                case 0:
                    switch (pBar.getHarmonicLoopAlt().getBarCount()) {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            return pBar.getHarmonicLoopAlt().getRhythmicAccents().get(0);
                        default:
                            throw new RuntimeException("UNSUPPORTED BAR COUNT");
                    }
                case 1:
                    switch (pBar.getHarmonicLoopAlt().getBarCount()) {
                        case 1:
                            return  pBar.getHarmonicLoopAlt().getRhythmicAccents().get(0);
                        case 2:
                        case 3:
                        case 4:
                            return  pBar.getHarmonicLoopAlt().getRhythmicAccents().get(1);
                        default:
                            throw new RuntimeException("UNSUPPORTED BAR COUNT");
                    }
                case 2:
                    switch (pBar.getHarmonicLoopAlt().getBarCount()) {
                        case 1:
                        case 2:
                            return pBar.getHarmonicLoopAlt().getRhythmicAccents().get(0);
                        case 3:
                        case 4:
                            return pBar.getHarmonicLoopAlt().getRhythmicAccents().get(2);
                        default:
                            //bar length cannot be greater than 2 on bar 3
                            throw new RuntimeException("UNSUPPORTED BAR COUNT");
                    }
                case 3:
                    switch (pBar.getHarmonicLoopAlt().getBarCount()) {
                        case 1:
                            return pBar.getHarmonicLoopAlt().getRhythmicAccents().get(0);
                        case 2:
                            return pBar.getHarmonicLoopAlt().getRhythmicAccents().get(1);
                        case 3:
                        case 4:
                            return pBar.getHarmonicLoopAlt().getRhythmicAccents().get(3);
                        default:
                            throw new RuntimeException("UNSUPPORTED BAR COUNT");
                    }
                default:
                    throw new RuntimeException("UNSUPPORTED BAR INDEX");
            }
        }
        return null;
    }

    public RhythmicAccents getHarmonicLoopBarAccents(ProgressionUnitBar pBar, int barPosition) {
        if (pBar.getHarmonicLoop() != null
                && pBar.getHarmonicLoop().getRhythmicAccents() != null) {

            switch (barPosition) {
                case 0:
                    switch (pBar.getHarmonicLoop().getBarCount()) {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            return pBar.getHarmonicLoop().getRhythmicAccents().get(0);
                        default:
                            throw new RuntimeException("UNSUPPORTED BAR COUNT");
                    }
                case 1:
                    switch (pBar.getHarmonicLoop().getBarCount()) {
                        case 1:
                            return  pBar.getHarmonicLoop().getRhythmicAccents().get(0);
                        case 2:
                        case 3:
                        case 4:
                            return  pBar.getHarmonicLoop().getRhythmicAccents().get(1);
                        default:
                            throw new RuntimeException("UNSUPPORTED BAR COUNT");
                    }
                case 2:
                    switch (pBar.getHarmonicLoop().getBarCount()) {
                        case 1:
                        case 2:
                            return pBar.getHarmonicLoop().getRhythmicAccents().get(0);
                        case 3:
                        case 4:
                            return pBar.getHarmonicLoop().getRhythmicAccents().get(2);
                        default:
                            //bar length cannot be greater than 2 on bar 3
                            throw new RuntimeException("UNSUPPORTED BAR COUNT");
                    }
                case 3:
                    switch (pBar.getHarmonicLoop().getBarCount()) {
                        case 1:
                            return pBar.getHarmonicLoop().getRhythmicAccents().get(0);
                        case 2:
                            return pBar.getHarmonicLoop().getRhythmicAccents().get(1);
                        case 3:
                        case 4:
                            return pBar.getHarmonicLoop().getRhythmicAccents().get(3);
                        default:
                            throw new RuntimeException("UNSUPPORTED BAR COUNT");
                    }
                default:
                    throw new RuntimeException("UNSUPPORTED BAR INDEX");
            }
        }
        return null;
    }

    public RhythmicAccents getBeatLoopAltBarAccents(ProgressionUnitBar pBar, int barPosition) {
        if (pBar.getBeatLoopAlt() != null
                && pBar.getBeatLoopAlt().getRhythmicAccents() != null) {

            switch (barPosition) {
                case 0:
                    switch (pBar.getBeatLoopAlt().getBarCount()) {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            return pBar.getBeatLoopAlt().getRhythmicAccents().get(0);
                        default:
                            throw new RuntimeException("UNSUPPORTED BAR COUNT");
                    }
                case 1:
                    switch (pBar.getBeatLoopAlt().getBarCount()) {
                        case 1:
                            return  pBar.getBeatLoopAlt().getRhythmicAccents().get(0);
                        case 2:
                        case 3:
                        case 4:
                            return  pBar.getBeatLoopAlt().getRhythmicAccents().get(1);
                        default:
                            throw new RuntimeException("UNSUPPORTED BAR COUNT");
                    }
                case 2:
                    switch (pBar.getBeatLoopAlt().getBarCount()) {
                        case 1:
                        case 2:
                            return pBar.getBeatLoopAlt().getRhythmicAccents().get(0);
                        case 3:
                        case 4:
                            return pBar.getBeatLoopAlt().getRhythmicAccents().get(2);
                        default:
                            //bar length cannot be greater than 2 on bar 3
                            throw new RuntimeException("UNSUPPORTED BAR COUNT");
                    }
                case 3:
                    switch (pBar.getBeatLoopAlt().getBarCount()) {
                        case 1:
                            return pBar.getBeatLoopAlt().getRhythmicAccents().get(0);
                        case 2:
                            return pBar.getBeatLoopAlt().getRhythmicAccents().get(1);
                        case 3:
                        case 4:
                            return pBar.getBeatLoopAlt().getRhythmicAccents().get(3);
                        default:
                            throw new RuntimeException("UNSUPPORTED BAR COUNT");
                    }
                default:
                    throw new RuntimeException("UNSUPPORTED BAR INDEX");
            }
        }
        return null;
    }

    public RhythmicAccents getBeatLoopBarAccents(ProgressionUnitBar pBar, int barPosition) {
        if (pBar.getBeatLoop() != null
                && pBar.getBeatLoop().getRhythmicAccents() != null) {

            switch (barPosition) {
                case 0:
                    switch (pBar.getBeatLoop().getBarCount()) {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            return pBar.getBeatLoop().getRhythmicAccents().get(0);
                        default:
                            throw new RuntimeException("UNSUPPORTED BAR COUNT");
                    }
                case 1:
                    switch (pBar.getBeatLoop().getBarCount()) {
                        case 1:
                            return  pBar.getBeatLoop().getRhythmicAccents().get(0);
                        case 2:
                        case 3:
                        case 4:
                            return  pBar.getBeatLoop().getRhythmicAccents().get(1);
                        default:
                            throw new RuntimeException("UNSUPPORTED BAR COUNT");
                    }
                case 2:
                    switch (pBar.getBeatLoop().getBarCount()) {
                        case 1:
                        case 2:
                            return pBar.getBeatLoop().getRhythmicAccents().get(0);
                        case 3:
                        case 4:
                            return pBar.getBeatLoop().getRhythmicAccents().get(2);
                        default:
                            //bar length cannot be greater than 2 on bar 3
                            throw new RuntimeException("UNSUPPORTED BAR COUNT");
                    }
                case 3:
                    switch (pBar.getBeatLoop().getBarCount()) {
                        case 1:
                            return pBar.getBeatLoop().getRhythmicAccents().get(0);
                        case 2:
                            return pBar.getBeatLoop().getRhythmicAccents().get(1);
                        case 3:
                        case 4:
                            return pBar.getBeatLoop().getRhythmicAccents().get(3);
                        default:
                            throw new RuntimeException("UNSUPPORTED BAR COUNT");
                    }
                default:
                    throw new RuntimeException("UNSUPPORTED BAR INDEX");
            }
        }
        return null;
    }

    public RhythmicAccents sumRhythmicAccents(List<RhythmicAccents> accents) {

        RhythmicAccents accentSum = new RhythmicAccents();

        for (RhythmicAccents accent : accents) {
            accentSum.setOneOne(Integer.toString(Integer.parseInt(accentSum.getOneOne()) + Integer.parseInt(accent.getOneOne())));
            accentSum.setOneTwo(Integer.toString(Integer.parseInt(accentSum.getOneTwo()) + Integer.parseInt(accent.getOneTwo())));
            accentSum.setOneThree(Integer.toString(Integer.parseInt(accentSum.getOneThree()) + Integer.parseInt(accent.getOneThree())));
            accentSum.setOneFour(Integer.toString(Integer.parseInt(accentSum.getOneFour()) + Integer.parseInt(accent.getOneFour())));

            accentSum.setTwoOne(Integer.toString(Integer.parseInt(accentSum.getTwoOne()) + Integer.parseInt(accent.getTwoOne())));
            accentSum.setTwoTwo(Integer.toString(Integer.parseInt(accentSum.getTwoTwo()) + Integer.parseInt(accent.getTwoTwo())));
            accentSum.setTwoThree(Integer.toString(Integer.parseInt(accentSum.getTwoThree()) + Integer.parseInt(accent.getTwoThree())));
            accentSum.setTwoFour(Integer.toString(Integer.parseInt(accentSum.getTwoFour()) + Integer.parseInt(accent.getTwoFour())));

            accentSum.setThreeOne(Integer.toString(Integer.parseInt(accentSum.getThreeOne()) + Integer.parseInt(accent.getThreeOne())));
            accentSum.setThreeTwo(Integer.toString(Integer.parseInt(accentSum.getThreeTwo()) + Integer.parseInt(accent.getThreeTwo())));
            accentSum.setThreeThree(Integer.toString(Integer.parseInt(accentSum.getThreeThree()) + Integer.parseInt(accent.getThreeThree())));
            accentSum.setThreeFour(Integer.toString(Integer.parseInt(accentSum.getThreeFour()) + Integer.parseInt(accent.getThreeFour())));

            accentSum.setFourOne(Integer.toString(Integer.parseInt(accentSum.getFourOne()) + Integer.parseInt(accent.getFourOne())));
            accentSum.setFourTwo(Integer.toString(Integer.parseInt(accentSum.getFourTwo()) + Integer.parseInt(accent.getFourTwo())));
            accentSum.setFourThree(Integer.toString(Integer.parseInt(accentSum.getFourThree()) + Integer.parseInt(accent.getFourThree())));
            accentSum.setFourFour(Integer.toString(Integer.parseInt(accentSum.getFourFour()) + Integer.parseInt(accent.getFourFour())));
        }

        return accentSum;
    }

    public RhythmicAccents setAccentsAboveMean(RhythmicAccents rhythmicAccents, int mean) {

        RhythmicAccents accentsAboveMean = new RhythmicAccents();

        //one
        if (Integer.parseInt(rhythmicAccents.getOneOne()) > mean) {
            accentsAboveMean.setOneOne("1");
        } else {
            accentsAboveMean.setOneOne("0");
        }

        if (Integer.parseInt(rhythmicAccents.getOneTwo()) > mean) {
            accentsAboveMean.setOneTwo("1");
        } else {
            accentsAboveMean.setOneTwo("0");
        }

        if (Integer.parseInt(rhythmicAccents.getOneThree()) > mean) {
            accentsAboveMean.setOneThree("1");
        } else {
            accentsAboveMean.setOneThree("0");
        }

        if (Integer.parseInt(rhythmicAccents.getOneFour()) > mean) {
            accentsAboveMean.setOneFour("1");
        } else {
            accentsAboveMean.setOneFour("0");
        }

        //two
        if (Integer.parseInt(rhythmicAccents.getTwoOne()) > mean) {
            accentsAboveMean.setTwoOne("1");
        } else {
            accentsAboveMean.setTwoOne("0");
        }

        if (Integer.parseInt(rhythmicAccents.getTwoTwo()) > mean) {
            accentsAboveMean.setTwoTwo("1");
        } else {
            accentsAboveMean.setTwoTwo("0");
        }

        if (Integer.parseInt(rhythmicAccents.getTwoThree()) > mean) {
            accentsAboveMean.setTwoThree("1");
        } else {
            accentsAboveMean.setTwoThree("0");
        }

        if (Integer.parseInt(rhythmicAccents.getTwoFour()) > mean) {
            accentsAboveMean.setTwoFour("1");
        } else {
            accentsAboveMean.setTwoFour("0");
        }

        //three
        if (Integer.parseInt(rhythmicAccents.getThreeOne()) > mean) {
            accentsAboveMean.setThreeOne("1");
        } else {
            accentsAboveMean.setThreeOne("0");
        }

        if (Integer.parseInt(rhythmicAccents.getThreeTwo()) > mean) {
            accentsAboveMean.setThreeTwo("1");
        } else {
            accentsAboveMean.setThreeTwo("0");
        }

        if (Integer.parseInt(rhythmicAccents.getThreeThree()) > mean) {
            accentsAboveMean.setThreeThree("1");
        } else {
            accentsAboveMean.setThreeThree("0");
        }

        if (Integer.parseInt(rhythmicAccents.getThreeFour()) > mean) {
            accentsAboveMean.setThreeFour("1");
        } else {
            accentsAboveMean.setThreeFour("0");
        }

        //four
        if (Integer.parseInt(rhythmicAccents.getFourOne()) > mean) {
            accentsAboveMean.setFourOne("1");
        } else {
            accentsAboveMean.setFourOne("0");
        }

        if (Integer.parseInt(rhythmicAccents.getFourTwo()) > mean) {
            accentsAboveMean.setFourTwo("1");
        } else {
            accentsAboveMean.setFourTwo("0");
        }

        if (Integer.parseInt(rhythmicAccents.getFourThree()) > mean) {
            accentsAboveMean.setFourThree("1");
        } else {
            accentsAboveMean.setFourThree("0");
        }

        if (Integer.parseInt(rhythmicAccents.getFourFour()) > mean) {
            accentsAboveMean.setFourFour("1");
        } else {
            accentsAboveMean.setFourFour("0");
        }

        return accentsAboveMean;
    }

    public Integer extractMeanfromAccents(RhythmicAccents rhythmicAccents) {

        Integer total = Integer.parseInt(rhythmicAccents.getOneOne());
        total = total + Integer.parseInt(rhythmicAccents.getOneTwo());
        total = total + Integer.parseInt(rhythmicAccents.getOneThree());
        total = total + Integer.parseInt(rhythmicAccents.getOneFour());

        total = total + Integer.parseInt(rhythmicAccents.getTwoOne());
        total = total + Integer.parseInt(rhythmicAccents.getTwoTwo());
        total = total + Integer.parseInt(rhythmicAccents.getTwoThree());
        total = total + Integer.parseInt(rhythmicAccents.getTwoFour());

        total = total + Integer.parseInt(rhythmicAccents.getThreeOne());
        total = total + Integer.parseInt(rhythmicAccents.getThreeTwo());
        total = total + Integer.parseInt(rhythmicAccents.getThreeThree());
        total = total + Integer.parseInt(rhythmicAccents.getThreeFour());

        total = total + Integer.parseInt(rhythmicAccents.getFourOne());
        total = total + Integer.parseInt(rhythmicAccents.getFourTwo());
        total = total + Integer.parseInt(rhythmicAccents.getFourThree());
        total = total + Integer.parseInt(rhythmicAccents.getFourFour());

        return total / 16;
    }

    public Map<ProgressionType, List<HarmonyExtraction>> createTypeToExtractionMap(QueueState queueState) {
        Map<ProgressionType, List<HarmonyExtraction>> typeToHarmonicExtractionMap = new HashMap<>();

        for (ProgressionUnit progressionUnit : queueState.getStructure()) {
            if (typeToHarmonicExtractionMap.get(progressionUnit.getType()) == null) {
                List<HarmonyExtraction> extractions = new ArrayList<>();
                for (ProgressionUnitBar progressionUnitBar : progressionUnit.getProgressionUnitBars()) {
                    extractions.addAll(progressionUnitBar.getHarmonicExtractions());
                }

                typeToHarmonicExtractionMap.put(progressionUnit.getType(), extractions);
            }
        }

        return typeToHarmonicExtractionMap;
    }
}
