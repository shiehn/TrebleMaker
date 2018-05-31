/*
 * JFugue, an Application Programming Interface (API) for Music Programming
 * http://www.jfugue.org
 *
 * Copyright (C) 2003-2014 David Koelle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jfugue.devices;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;

import org.jfugue.midi.MidiDefaults;
import org.jfugue.player.SequencerManager;

/**
 * Represents a device that will send music. For example, you can attach this
 * to your external MIDI keyboard and play music on the keyboard, which is then recorded here.
 */
public class MusicTransmitterToSequence
{
    private MidiDevice device;
    private boolean isInitiated;
    private Sequencer sequencer;
    
    public MusicTransmitterToSequence(MidiDevice device) throws MidiUnavailableException {
        this.device = device;
        this.isInitiated = false;
    }
    
    private void init() throws MidiUnavailableException, InvalidMidiDataException {
        if (!isInitiated) {
            if (!(device.isOpen())) {
              device.open();
            }
            sequencer = SequencerManager.getInstance().getSequencer();
            sequencer.open();
            
            Transmitter transmitter = device.getTransmitter();
            Receiver receiver = sequencer.getReceiver();
            transmitter.setReceiver(receiver);

            Sequence sequence = new Sequence(Sequence.PPQ, 24);
            for (int i=0; i < MidiDefaults.TRACKS; i++) {
            	Track track = sequence.createTrack();
            	sequencer.recordEnable(track, i);
            }
            sequencer.setSequence(sequence);
            sequencer.setTickPosition(0);
        }
    }
    
    public void startListening() throws MidiUnavailableException, InvalidMidiDataException {
        init();
        sequencer.startRecording();
    }
    
    public void stopListening() {
        sequencer.stopRecording();
        close();
    }

    /**
     * Used instead of startListening() and stopListening() - listens for a pre-defined amount of time.
     * 
     * @param millis
     * @throws MidiUnavailableException
     * @throws InterruptedException
     */
    public void listenForMillis(long millis) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        startListening();
        Thread.sleep(millis);
        stopListening();
    }
    
    public void close() {
        sequencer.close();
        device.close();
    }
    
    public Sequence getSequence() {
    	return sequencer.getSequence();
    }
}