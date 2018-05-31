package com.treblemaker.generators.melody;

public class Config {

    private boolean forceCorrectPitches;

    public Config(boolean forceCorrectPitches) {
        this.forceCorrectPitches = forceCorrectPitches;
    }

    public boolean shouldForceCorrectPitches() {
        return forceCorrectPitches;
    }
}