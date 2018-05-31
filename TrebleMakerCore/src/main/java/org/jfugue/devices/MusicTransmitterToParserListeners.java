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

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;

import org.jfugue.parser.ParserListener;

/**
 * Represents a device that will send music. For example, you can attach this
 * to your external MIDI keyboard and play music on the keyboard, which is then recorded here.
 */
public class MusicTransmitterToParserListeners 
{
    private MidiDevice device;
    private boolean isInitiated;
    private Transmitter transmitter;
    private MidiParserReceiver mrftd;
    private List<ParserListener> listeners;
    
    public MusicTransmitterToParserListeners(MidiDevice device) throws MidiUnavailableException {
        this.device = device;
        this.isInitiated = false;
        this.listeners = new ArrayList<ParserListener>();
        this.mrftd = new MidiParserReceiver();
    }
    
    private void init() throws MidiUnavailableException {
        if (!isInitiated) {
            try  {
                if (!(device.isOpen())) {
                  device.open();
                }
                this.transmitter = device.getTransmitter();
            } catch (MidiUnavailableException e) {
                device.close();
                throw e;
            }
        }
        
        this.mrftd.getParser().clearParserListeners();
        for (ParserListener listener : listeners) {
            this.mrftd.getParser().addParserListener(listener);
        }
    }
    
    public void addParserListener(ParserListener l) {
        this.listeners.add(l);
    }
    
    public List<ParserListener> getParserListeners() { 
        return this.listeners;
    }
    
    public Transmitter getTransmitter() {
        return this.transmitter;
    }
    
    public MidiParserReceiver getMidiParserReceiver() {
        return this.mrftd;
    }
    
    public void startListening() throws MidiUnavailableException {
        init();
        mrftd.getParser().startParser();
        transmitter.setReceiver(this.mrftd);
    }
    
    public void stopListening() {
        mrftd.getParser().stopParser();
        close();
    }

    /**
     * Used instead of startListening() and stopListening() - listens for a pre-defined amount of time.
     * 
     * @param millis
     * @throws MidiUnavailableException
     * @throws InterruptedException
     */
    public void listenForMillis(long millis) throws MidiUnavailableException, InterruptedException {
        startListening();
        Thread.sleep(millis);
        stopListening();
    }
    
    public void close() {
        transmitter.close();
        device.close();
    }
}