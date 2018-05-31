package org.jfugue.parser;

import org.jfugue.theory.Note;

public interface NoteEventListener {
    public void onNoteStarted(Note note);
    public void onNoteFinished(Note note);
}
