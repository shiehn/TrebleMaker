package com.treblemaker.model.arpeggio;

import java.io.Serializable;

public class ArpeggioJson implements Serializable {

    private int[] arpeggio;

    public int[] getArpeggio() {
        return arpeggio;
    }

    public void setArpeggio(int[] arpeggio) {
        this.arpeggio = arpeggio;
    }
}
