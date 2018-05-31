package com.treblemaker.scheduledevents.helpers;

import com.hazelcast.core.IMap;
import com.treblemaker.Application;
import com.treblemaker.configs.*;
import com.treblemaker.dal.interfaces.*;
import com.treblemaker.configs.*;
import com.treblemaker.model.*;
import com.treblemaker.model.arpeggio.Arpeggio;
import com.treblemaker.model.bassline.Bassline;
import com.treblemaker.model.hat.HatPattern;
import com.treblemaker.model.hat.HatSample;
import com.treblemaker.model.hitsandfills.Fill;
import com.treblemaker.model.hitsandfills.Hit;
import com.treblemaker.model.kick.KickPattern;
import com.treblemaker.model.kick.KickSample;
import com.treblemaker.model.snare.SnarePattern;
import com.treblemaker.model.snare.SnareSample;
import com.treblemaker.scheduledevents.interfaces.IQueueHelpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Component
public class QueueHelpers implements IQueueHelpers {

    private IChordSequencesDal chordSequencesDal;
    private IHarmonicLoopsDal harmonicLoopsDal;
    private IBeatLoopsDal beatLoopsDal;
    private IArpeggioDal arpeggioDal;
    private IHarmonicLoopCharacteristicDal harmonicLoopCharacteristicDal;
    private IBasslineDal basslineDal;
    private IHitDal hitDal;
    private IFillDal fillDal;
    private IKickPatternDal kickPatternDal;
    private IKickSampleDal kickSampleDal;
    private IHatPatternDal hatPatternDal;
    private IHatSampleDal hatSampleDal;
    private ISnarePatternDal snarePatternDal;
    private ISnareSampleDal snareSampleDal;
    private String cacheKeyHiveCache;
    private String cacheKeyTimeseries;
    private String cacheKeyDatasource;

    @Autowired
    private AppConfigs appConfigs;

    @Autowired
    public QueueHelpers(IChordSequencesDal chordSequencesDal,
                        IHarmonicLoopsDal iHarmonicLoopsDal,
                        IBeatLoopsDal iBeatLoopsDal,
                        IHarmonicLoopCharacteristicDal iHarmonicLoopCharacteristicDal,
                        IBasslineDal iBasslineDal,
                        IArpeggioDal iArpeggioDal,
                        IHitDal hitDal,
                        IFillDal fillDal,
                        IKickPatternDal kickPatternDal,
                        IKickSampleDal kickSampleDal,
                        IHatPatternDal hatPatternDal,
                        IHatSampleDal hatSampleDal,
                        ISnarePatternDal snarePatternDal,
                        ISnareSampleDal snareSampleDal,
                        @Value("${cache_key_hive_cache}") String cacheKeyHiveCache,
                        @Value("${cache_key_timeseries}") String cacheKeyTimeseries,
                        @Value("${cache_key_datasource}") String cacheKeyDatasource) {

        this.chordSequencesDal = chordSequencesDal;
        this.harmonicLoopsDal = iHarmonicLoopsDal;
        this.beatLoopsDal = iBeatLoopsDal;
        this.cacheKeyHiveCache = cacheKeyHiveCache;
        this.cacheKeyTimeseries = cacheKeyTimeseries;
        this.cacheKeyDatasource = cacheKeyDatasource;
        this.harmonicLoopCharacteristicDal = iHarmonicLoopCharacteristicDal;
        this.basslineDal = iBasslineDal;
        this.arpeggioDal = iArpeggioDal;
        this.hitDal = hitDal;
        this.fillDal = fillDal;
        this.kickPatternDal = kickPatternDal;
        this.kickSampleDal = kickSampleDal;
        this.hatPatternDal = hatPatternDal;
        this.hatSampleDal = hatSampleDal;
        this.snarePatternDal = snarePatternDal;
        this.snareSampleDal = snareSampleDal;
    }

    @Override
    public SourceData getSourceData() {

        SourceData sourceData = new SourceData();
        IMap hiveCache = null;

        if (Application.client != null) {
            hiveCache = Application.client.getMap(cacheKeyHiveCache);
            sourceData = (SourceData) hiveCache.get(cacheKeyDatasource);
        }

        if (sourceData != null) {

            Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            Application.logger.debug("LOG: DATA-SOURCE FOUND IN CACHE !!!!");
            Application.logger.debug("LOG: DATA-SOURCE FOUND IN CACHE !!!!");
            Application.logger.debug("LOG: DATA-SOURCE FOUND IN CACHE !!!!");
            Application.logger.debug("LOG: DATA-SOURCE FOUND IN CACHE !!!!");
            Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

            return sourceData;
        }

        sourceData = new SourceData();

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : FETCHING harmonicLoops");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        List<HarmonicLoop> harmonicLoops = harmonicLoopsDal.findByNormalizedLength(HarmonicLoop.ALREADY_NORMALIZED);
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : FINIHED FETCHING harmonicLoops");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : FETCHING basslines");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        List<Bassline> basslines = basslineDal.findAll();
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : FINIHED FETCHING basslines");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : FETCHING Arpeggios");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        List<Arpeggio> arpeggios = arpeggioDal.findAll();
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : FINIHED FETCHING Arpeggios");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");



        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : FETCHING Hits");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        List<Hit> hits = hitDal.findAll();
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : FINIHED FETCHING Hits");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : FETCHING Fills");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        List<Fill> fills = fillDal.findAll();
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : FINIHED FETCHING Fills");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : FETCHING Kick Patterns");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        List<KickPattern> kickPatterns = kickPatternDal.findAll();
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : Finished FETCHING Kick Patterns");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : FETCHING Kick Samples");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        List<KickSample> kickSamples = kickSampleDal.findAll();
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : Finished FETCHING Kick Samples");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : FETCHING Hat Patterns");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        List<HatPattern> hatPatterns = hatPatternDal.findAll();
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : Finished FETCHING Hat Patterns");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : FETCHING Hat Samples");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        List<HatSample> hatSamples = hatSampleDal.findAll();
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : Finished FETCHING Kick Samples");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : FETCHING Snare Patterns");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        List<SnarePattern> snarePatterns = snarePatternDal.findAll();
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : Finished FETCHING Hat Patterns");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : FETCHING Snare Samples");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        List<SnareSample> snareSamples = snareSampleDal.findAll();
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : Finished FETCHING Snare Samples");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

        //TODO THIS SHOULD BE IN THAT DAL NOT HERE!!!!!!!!
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : FETCHING ALL CHORDS && SEQUENCES");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Iterable<ChordSequences> sequences = chordSequencesDal.findAll();
        List<HiveChord> chords = new ArrayList<>();
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Application.logger.debug("LOG: PHASE 1 : FINIHSED FETCHING ALL CHORDS && SEQUENCES");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        for (ChordSequences sequence : sequences) {
            chords.addAll(sequence.getChords());
        }

        //FILTER CHORDS ..
        List<HiveChord> filteredChords = new ArrayList<>();
        Outer:
        for (HiveChord chord : chords) {
            for (HarmonicLoop loop : harmonicLoops) {
                if (chord.hasMatchingRoot(loop)) {
                    filteredChords.add(chord);
                    continue Outer;
                }
            }

            filteredChords.add(new HiveChord(appConfigs.CHORD_BREAK));
        }

        sourceData.setBasslines(basslines);

        sourceData.setArpeggios(arpeggios);

        sourceData.setHiveChordInDatabase(filteredChords);

        sourceData.setHarmonicLoopCharacteristics(harmonicLoopCharacteristicDal.findAll());

        sourceData.setHarmonicLoops(harmonicLoopsDal.findByNormalizedLength(HarmonicLoop.ALREADY_NORMALIZED));

        sourceData.setBeatLoops(beatLoopsDal.findByNormalizedLength(BeatLoop.ALREADY_NORMALIZED));

        sourceData.setHits(hits);

        sourceData.setFills(fills);

        sourceData.setKickPatterns(kickPatterns);

        sourceData.setKickSamples(kickSamples);

        sourceData.setHatPatterns(hatPatterns);

        sourceData.setHatSamples(hatSamples);

        sourceData.setSnarePatterns(snarePatterns);

        sourceData.setSnareSamples(snareSamples);

        if (Application.client != null) {
            hiveCache.put(cacheKeyDatasource, sourceData);
        }

        return sourceData;
    }
}
