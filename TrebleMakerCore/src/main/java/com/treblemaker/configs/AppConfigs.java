package com.treblemaker.configs;


import java.nio.file.Paths;

import com.treblemaker.model.*;
import com.treblemaker.model.hat.*;
import com.treblemaker.model.kick.*;
import com.treblemaker.model.snare.*;
import com.treblemaker.model.hitsandfills.*;
import com.treblemaker.configs.AppConfigs;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.treblemaker.configs.AppConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfigs {

    @Autowired
    public AppConfigs appConfigs;

    @Value("${tm.app.root}")
    private String APPLICATION_ROOT;

    public String getApplicationRoot() {
        return this.APPLICATION_ROOT;
    }

    public void setApplicationRoot(String root){
        this.APPLICATION_ROOT = root;
    }

    @Value("${tm.aws.bucket.name}")
    private String AWS_BUCKET_NAME;

    public String getAwsBucketName() {
        return AWS_BUCKET_NAME;
    }

    public final int OCTAVE = 5;

    public final String CHORD_BREAK = "_";

    public int[] SUPPORTED_BPM = {80, 120};

    public String MOCK_AUDIO_PATH = Paths.get( "TrebleMakerCore","src", "main", "java", "com", "treblemaker", "tests", "Mocks", "mockAudio").toString();

    public String getCompositionOutput() {
        return APPLICATION_ROOT + "/Compositions";
    }

    public String getFinalMixOutput() {
        return getCompositionOutput() + "/finalmix";
    }

    public String getTarPackage() {
        return getCompositionOutput() + "/tars";
    }

    public String getFFMPEGLocation() {
        return APPLICATION_ROOT + "/ffmpeg/ffmpeg";
    }

    public String getSoxLocation() {
        return APPLICATION_ROOT + "/sox1442/sox";
    }

    public String getVSTPluginDir() {
        return APPLICATION_ROOT + "Vst64BitSynths\\";
    }

    public String getMrsWatsonLocation() {
        return this.APPLICATION_ROOT + "MrsWatson\\Windows\\mrswatson64.exe";
    }

    public String getFluidSynth() {
        return APPLICATION_ROOT + "/FluidSynth";
    }

    public String getAmbienceFullPath(AmbienceLoop ambienceLoop) {
        return APPLICATION_ROOT + "/Loops/" + ambienceLoop.getStationId() + "/AmbienceLoops/" + ambienceLoop.getFileName();
    }

    public String getAmbienceLocation(int stationId) {
        return APPLICATION_ROOT + "/Loops/" + stationId + "/AmbienceLoops";
    }

    public String getBeatLoopfullPath(BeatLoop beatLoop) {
        return APPLICATION_ROOT + "/Loops/" + beatLoop.getStationId() + "/BeatLoops/" + beatLoop.getFileName();
    }

    public String getBeatShimLoopfullPath(BeatLoop beatLoop, String queueItemId) {
        return getCompositionOutput() + "/audioshims/" + queueItemId + "/" + beatLoop.getFileName();
    }

    public String getBeatLoopsLocation(int stationId) {
        return APPLICATION_ROOT + "/Loops/" + stationId + "/BeatLoops";
    }

    public String getHarmonicLoopsFullPath(HarmonicLoop harmonicLoop) {
        return APPLICATION_ROOT + "/Loops/" + harmonicLoop.getStationId() + "/HarmonicLoops/" + harmonicLoop.getFileName();
    }

    public String getHarmonicLoopsLocation(int stationId) {
        return APPLICATION_ROOT + "/Loops/" + stationId + "/HarmonicLoops";
    }

    public String getHitsFullPath(Hit hit) {
        String fileName;
        if (hit.getFileName().contains(".wav")) {
            fileName = hit.getFileName();
        } else {
            fileName = hit.getFileName() + ".wav";
        }

        return APPLICATION_ROOT + "/Loops/" + hit.getStationId() + "/Hits/" + fileName;
    }

    public String getMetadataPath(String queueItemId) {
        return Paths.get(getCompositionOutput(), "midioutput", queueItemId, appConfigs.METADATA_FILENAME).toString();
    }


    public String getHitsLocation(int stationId) {
        return APPLICATION_ROOT + "/Loops/" + stationId + "Hits";
    }

    public String getFillsFullPath(Fill fill) {
        String fileName;
        if (fill.getFileName().contains(".wav")) {
            fileName = fill.getFileName();
        } else {
            fileName = fill.getFileName() + ".wav";
        }

        return APPLICATION_ROOT + "/Loops/" + fill.getStationId() + "/Fills/" + fileName;
    }

    public String getFillsLocation(int stationId) {
        return APPLICATION_ROOT + "/Loops/" + stationId + "/Fills";
    }

    public String getKickFullPath(KickSample kick) {
        return APPLICATION_ROOT + "/Loops/" + kick.getStationId() + "/Kicks/" + kick.getFileName();
    }

    public String getKickShimFullPath(KickSample kick, int bpm) {
        return APPLICATION_ROOT + "/Loops/" + kick.getStationId() + "/Kicks/shim_kick_" + bpm + ".wav";
    }

    public String getSnareFullPath(SnareSample snareSample) {
        return APPLICATION_ROOT + "/Loops/" + snareSample.getStationId() + "/Snares/" + snareSample.getFileName();
    }

    public String getSnareShimFullPath(SnareSample snareSample, int bpm) {
        return APPLICATION_ROOT + "/Loops/" + snareSample.getStationId() + "/Snares/shim_snare_" + bpm + ".wav";
    }

    public String getHatFullPath(HatSample hatSample) {
        return APPLICATION_ROOT + "/Loops/" + hatSample.getStationId() + "/Hats/" + hatSample.getFileName();
    }

    public String getHatShimFullPath(HatSample hatSample, int bpm) {
        return APPLICATION_ROOT + "/Loops/" + hatSample.getStationId() + "/Hats/shim_hat_" + bpm + ".wav";
    }

    public String getHarmonicSamplesTest() {
        return APPLICATION_ROOT + "/Loops/HarmonicLoops";
    }

    public String getSoundFontLocation() {
        return APPLICATION_ROOT + "/SoundFonts";
    }

    public String getMidiFilePathPrefix() {
        return getCompositionOutput() + "/midioutput";
    }

    public String getBeatShimsDir() {
        return APPLICATION_ROOT + "/Loops/BeatLoops/BeatShims";
    }

    public String getHarmonicShimsDir() {
        return APPLICATION_ROOT + "/Loops/HarmonicLoops/HarmonicShims";
    }

    public String getMockDataDir() {
        return APPLICATION_ROOT + "/TrebleMakerCore/src/main/java/com/treblemaker/tests/Mocks";
    }

    public String getMockDataAudioDir() {
        return getMockDataDir() + "/mockAudio";
    }

    public String COMP_HI_FILENAME = "comphi.mid";
    public String COMP_ALT_HI_FILENAME = "compalthi.mid";
    public String COMP_MID_FILENAME = "compmid.mid";
    public String COMP_ALT_MID_FILENAME = "compaltmid.mid";
    public String COMP_LOW_FILENAME = "complow.mid";
    public String COMP_ALT_LOW_FILENAME = "compaltlow.mid";
    public String COMP_KICK_FILENAME = "compkick.mid";
    public String COMP_SNARE_FILENAME = "compsnare.mid";
    public String COMP_HATS_FILENAME = "comphats.mid";

    public String COMP_MELODIC_FILENAME = "compmelodic.mid";

    public String COMP_MELODIC_AUDIO_FILENAME_NO_FX = "compmelodic_nofx.wav";
    public String COMP_MELODIC_AUDIO_FILENAME_FX_NO_EQ = "compmelodic_noEq.wav";
    public String COMP_MELODIC_AUDIO_FILENAME_FX = "compmelodic.wav";

    public String COMP_HI_AUDIO_FILENAME = "comphi.wav";
    public String COMP_ALT_HI_AUDIO_FILENAME = "compalthi.wav";

    public String COMP_HI_FX_AUDIO_NO_EQ_FILENAME = "comphifx_noEqFilter.wav";
    public String COMP_HI_FX_AUDIO_FILENAME = "comphifx.wav";
    public String COMP_ALT_HI_FX_NO_EQ_AUDIO_FILENAME = "compalthifx_noEqFilter.wav";
    public String COMP_ALT_HI_FX_AUDIO_FILENAME = "compalthifx.wav";

    public String COMP_MID_AUDIO_FILENAME = "compmid.wav";
    public String COMP_ALT_MID_AUDIO_FILENAME = "compaltmid.wav";
    public String COMP_LOW_AUDIO_FILENAME = "complow.wav";
    public String COMP_ALT_LOW_AUDIO_FILENAME = "compaltlow.wav";

    public String COMP_RHYTHM_FILENAME = "comprhythm.wav";
    public String COMP_RHYTHM_ALT_FILENAME = "comprhythmalt.wav";
    public String COMP_AMBIENCE_FILENAME = "compambience.wav";
    public String COMP_HARMONIC_FILENAME = "compharmonic.wav";
    public String COMP_HARMONIC_ALT_FILENAME = "compharmonicalt.wav";

    public String BEAT_SHIM_FILE_NAME = "beat_shim_2_bars_%s_bpm.wav";
    public String HARMONIC_SHIM_FILE_NAME = "harmonicloop_shim_2_bars_%s_bpm.wav";

    public String HITS_FILENAME = "hit.wav";
    public String FILLS_FILENAME = "fill.wav";
    public String KICK_FILENAME = "kick.wav";
    public String HAT_FILENAME = "hat.wav";
    public String SNARE_FILENAME = "snare.wav";

    public String EMPTY_TEST_MIDI = "empty_test_midi.mid";
    public String EMPTY_TEST_WAV = "empty_test_wav.wav";

    public String METADATA_FILENAME = "metadata.txt";
}



