package com.treblemaker.extractors.pitchextraction;

import java.util.ArrayList;
import java.util.List;

public class FrequencyToPitchConversion {

    class PitchFreq {
        String note;
        int octave;
        float freq;

        public PitchFreq(String note, float freq) {
            this.note = note;
            this.freq = freq;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public float getFreq() {
            return freq;
        }

        public void setFreq(float freq) {
            this.freq = freq;
        }

        public int getOctave() {
            return octave;
        }

        public void setOctave(int octave) {
            this.octave = octave;
        }
    }

    List<PitchFreq> pitchFreqs = new ArrayList<>();

    public FrequencyToPitchConversion() {
        pitchFreqs.add(new PitchFreq("c", 16.35f));
        pitchFreqs.add(new PitchFreq("cs", 17.32f));
        pitchFreqs.add(new PitchFreq("d", 18.35f));
        pitchFreqs.add(new PitchFreq("ds", 19.45f));
        pitchFreqs.add(new PitchFreq("e", 20.60f));
        pitchFreqs.add(new PitchFreq("f", 21.83f));
        pitchFreqs.add(new PitchFreq("fs", 23.12f));
        pitchFreqs.add(new PitchFreq("g", 24.50f));
        pitchFreqs.add(new PitchFreq("gs", 25.96f));
        pitchFreqs.add(new PitchFreq("a", 27.50f));
        pitchFreqs.add(new PitchFreq("as", 29.14f));
        pitchFreqs.add(new PitchFreq("b", 30.87f));
    }

    public String getPitchName(float freq){

        float closestDistance = 9999999f;
        PitchFreq bestMatch = null;

        for(PitchFreq pitchFreq:pitchFreqs) {

            int numOfOctaves = 8;
            for (int i = 0; i < numOfOctaves+1; i++) {

                double freqOctave = pitchFreq.getFreq() * Math.pow((double)2f,(double)i);

                if(Math.abs(freq-freqOctave) < closestDistance){
                    bestMatch = pitchFreq;
                    bestMatch.setOctave(i);
                    closestDistance = (float)Math.abs(freq-freqOctave);
                }
            }
        }

        return bestMatch.getNote();
    }
}
