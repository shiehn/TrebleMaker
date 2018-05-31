package com.treblemaker.model;

import com.treblemaker.Application;
import com.treblemaker.model.arpeggio.Arpeggio;
import com.treblemaker.model.bassline.Bassline;
import com.treblemaker.model.characterisic.HarmonicLoopCharacteristic;
import com.treblemaker.model.hat.HatPattern;
import com.treblemaker.model.hat.HatSample;
import com.treblemaker.model.hitsandfills.Fill;
import com.treblemaker.model.hitsandfills.Hit;
import com.treblemaker.model.kick.KickPattern;
import com.treblemaker.model.kick.KickSample;
import com.treblemaker.model.snare.SnarePattern;
import com.treblemaker.model.snare.SnareSample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SourceData implements Serializable {

    private List<HiveChord> hiveChordInDatabase;
    private List<HarmonicLoop> harmonicLoops;
    private List<BeatLoop> beatLoops;
    private List<HarmonicLoopCharacteristic> harmonicLoopCharacteristics;
    private List<Bassline> basslines;
    private List<Hit> hits;
    private List<Fill> fills;
    private List<Arpeggio> arpeggios;

    private List<KickPattern> kickPatterns;
    private List<KickSample> kickSamples;
    private List<SnarePattern> snarePatterns;
    private List<SnareSample> snareSamples;
    private List<HatPattern> hatPatterns;
    private List<HatSample> hatSamples;

    public List<HiveChord> getHiveChordInDatabase() {
        return hiveChordInDatabase;
    }

    public void setHiveChordInDatabase(List<HiveChord> hiveChordInDatabase) {
        this.hiveChordInDatabase = hiveChordInDatabase;
    }

    public List<HarmonicLoop> getHarmonicLoops(int stationId) {
        return harmonicLoops.stream().filter(loop -> loop.getStationId() == stationId).collect(Collectors.toList());
    }

    public void setHarmonicLoops(Iterable<HarmonicLoop> harmonicLoops) {

        this.harmonicLoops = filterHarmonicCollection(harmonicLoops);
    }

    public List<HarmonicLoopCharacteristic> getHarmonicLoopCharacteristics() {
        return harmonicLoopCharacteristics;
    }

    public void setHarmonicLoopCharacteristics(List<HarmonicLoopCharacteristic> harmonicLoopCharacteristics) {
        this.harmonicLoopCharacteristics = harmonicLoopCharacteristics;
    }

    private List<HarmonicLoop> filterHarmonicCollection(Iterable<HarmonicLoop> iter) {
        List<HarmonicLoop> list = new ArrayList<HarmonicLoop>();
        for (HarmonicLoop item : iter) {

            //ENSURE ALL LOOPS ARE NOT NULL && HAVE RYTHMIC ACCENTS ..
            if(item != null && item.getRhythmicAccents() != null && item.getRhythmicAccents().size() > 0){

                //filter basslines out
//                boolean isBeatLoop = false;
//                for (HarmonicLoopCharacteristic hChar : harmonicLoopCharacteristics) {
//                    if(item.getId().equals(hChar.getHarmonicLoopId()) && hChar.getCharacteristicId() == 8){
//                        //TODO create enum for characteristics ids
//                        isBeatLoop = true;
//                    }
//                }
//
//                if(!isBeatLoop){
                    list.add(item);
//                }
            }else{
                Application.logger.debug("LOG: ERROR : NULL or MISSING RHTHMIC ACCENTS IN DATABASE!!!");
            }
        }

        return list;
    }



    public List<BeatLoop> getBeatLoops(int stationId) {
        return beatLoops.stream().filter(beatLoop -> beatLoop.getStationId() == stationId).collect(Collectors.toList());
    }

    public void setBeatLoops(List<BeatLoop> beatLoops) {

        this.beatLoops = filterBeatCollection(beatLoops);
    }

    private List<BeatLoop> filterBeatCollection(List<BeatLoop> iter){

        List<BeatLoop> list = new ArrayList<BeatLoop>();
        for (BeatLoop item : iter) {

            //ENSURE ALL LOOPS ARE NOT NULL && HAVE RYTHMIC ACCENTS ..
            if(item != null && item.getRhythmicAccents() != null && item.getRhythmicAccents().size() > 0){
                list.add(item);
            }else{
                Application.logger.debug("LOG: ERROR : NULL or MISSING RHTHMIC ACCENTS IN DATABASE!!!");
            }
        }

        return list;
    }

    public List<Bassline> getBasslines() {
        return basslines;
    }

    public void setBasslines(List<Bassline> basslines) {
        this.basslines = basslines;
    }

    public List<Arpeggio> getArpeggios() {
        return arpeggios;
    }

    public void setArpeggios(List<Arpeggio> arpeggios) {
        this.arpeggios = arpeggios;
    }

    public List<Hit> getHits(int stationId) {
        return hits.stream().filter(hit -> hit.getStationId() == stationId).collect(Collectors.toList());
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

    public List<Fill> getFills(int stationId) {
        return fills.stream().filter(fill -> fill.getStationId() == stationId).collect(Collectors.toList());
    }

    public void setFills(List<Fill> fills) {
        this.fills = fills;
    }

    public List<KickPattern> getKickPatterns() {
        return kickPatterns;
    }

    public void setKickPatterns(List<KickPattern> kickPatterns) {
        this.kickPatterns = kickPatterns;
    }

    public List<KickSample> getKickSamples() {
        return kickSamples;
    }

    public void setKickSamples(List<KickSample> kickSamples) {
        this.kickSamples = kickSamples;
    }

    public List<SnareSample> getSnareSamples() {
        return snareSamples;
    }

    public void setSnareSamples(List<SnareSample> snareSamples) {
        this.snareSamples = snareSamples;
    }

    public List<HatSample> getHatSamples() {
        return hatSamples;
    }

    public void setHatSamples(List<HatSample> hatSamples) {
        this.hatSamples = hatSamples;
    }

    public List<SnarePattern> getSnarePatterns() {
        return snarePatterns;
    }

    public void setSnarePatterns(List<SnarePattern> snarePatterns) {
        this.snarePatterns = snarePatterns;
    }

    public List<HatPattern> getHatPatterns() {
        return hatPatterns;
    }

    public void setHatPatterns(List<HatPattern> hatPatterns) {
        this.hatPatterns = hatPatterns;
    }
}
