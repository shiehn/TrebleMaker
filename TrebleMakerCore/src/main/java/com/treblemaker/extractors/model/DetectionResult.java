package com.treblemaker.extractors.model;

public class DetectionResult {
        double timeStamp;
        float pitch;
        float probability;
        double rms;

        public double getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(double timeStamp) {
            this.timeStamp = timeStamp;
        }

        public float getPitch() {
            return pitch;
        }

        public void setPitch(float pitch) {
            this.pitch = pitch;
        }

        public float getProbability() {
            return probability;
        }

        public void setProbability(float probability) {
            this.probability = probability;
        }

        public double getRms() {
            return rms;
        }

        public void setRms(double rms) {
            this.rms = rms;
        }
    }