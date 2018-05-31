package com.treblemaker.model.bassline;

import com.treblemaker.utils.ChordClass;
import org.jfugue.theory.Note;

public class Intervals {

    private Note noteOne;
    private Note noteTwo;
    private Note noteThree;
    private Note noteFour;
    private Note noteFive;

    private ChordClass intervalClass;

    public Note getByPosition(int position){
        switch (position){
            case 1:
                return this.noteOne;
            case 2:
                return this.noteTwo;
            case 3:
                return this.noteThree;
            case 4:
                return this.noteFour;
            case 5:
                return this.noteFive;
        }

        throw new RuntimeException("out of bounds");
    }

    public void setNoteOne(Note noteOne) {
        this.noteOne = noteOne;
    }

    public void setNoteTwo(Note noteTwo) {
        this.noteTwo = noteTwo;
    }

    public void setNoteThree(Note noteThree) {
        this.noteThree = noteThree;
    }

    public void setNoteFour(Note noteFour) {
        this.noteFour = noteFour;
    }

    public void setNoteFive(Note noteFive) {
        this.noteFive = noteFive;
    }

    public ChordClass getIntervalClass() {
        return intervalClass;
    }

    public void setIntervalClass(ChordClass intervalClass) {
        this.intervalClass = intervalClass;
    }
}
