package com.treblemaker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.treblemaker.adapters.ChordAdapter;
import com.treblemaker.adapters.interfaces.IChordAdapter;
import com.treblemaker.dal.interfaces.IAmbienceLoopsDal;
import com.treblemaker.dal.interfaces.IAnalyticsHorizontalDal;
import com.treblemaker.dal.interfaces.IBachChoraleDal;
import com.treblemaker.dal.interfaces.IFXArpeggioDelayDal;
import com.treblemaker.eventchain.*;
import com.treblemaker.eventchain.analytics.*;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.extractors.MelodicExtractor;
import com.treblemaker.extractors.sentiment.SentimentCharacteristicsExtraction;
import com.treblemaker.extractors.sentiment.SentimentMixExtraction;
import com.treblemaker.fx.util.DurationAnalysis;
import com.treblemaker.gates.LayerGroupComplexityGate;
import com.treblemaker.gates.TemplateComplexityGate;
import com.treblemaker.gates.interfaces.IComplexityThreshold;
import com.treblemaker.generators.*;
import com.treblemaker.generators.beats.BeatLoopAltGenerator;
import com.treblemaker.generators.beats.BeatLoopGenerator;
import com.treblemaker.generators.fills.FillsGenerator;
import com.treblemaker.generators.harmonic.HarmonicLoopAltGenerator;
import com.treblemaker.generators.harmonic.HarmonicLoopGenerator;
import com.treblemaker.generators.hits.HitGenerator;
import com.treblemaker.generators.interfaces.*;
import com.treblemaker.generators.melody.Config;
import com.treblemaker.generators.melody.MelodyGenerator;
import com.treblemaker.machinelearning.HarmonicLoopAltTimeseriesClassifier;
import com.treblemaker.machinelearning.HarmonicLoopTimeseriesClassifier;
import com.treblemaker.mixer.AudioMixer;
import com.treblemaker.mixer.interfaces.IAudioMixer;
import com.treblemaker.model.analytics.AnalyticsHorizontal;
import com.treblemaker.options.*;
import com.treblemaker.renderers.*;
import com.treblemaker.renderers.interfaces.*;
import com.treblemaker.scheduledevents.QueueDigester;
import com.treblemaker.scheduledevents.interfaces.IQueueDigester;
import com.treblemaker.selectors.*;
import com.treblemaker.selectors.interfaces.*;
import com.treblemaker.utils.interfaces.IAudioUtils;
import com.treblemaker.weighters.beatloopweighters.BeatLoopAltWeighter;
import com.treblemaker.weighters.fills.FillWeighter;
import com.treblemaker.weighters.fills.ISetFillWeighter;
import com.treblemaker.weighters.fx.SynthFXWeighter;
import com.treblemaker.weighters.harmonicloopweighters.HarmonicLoopAltWeighter;
import com.treblemaker.weighters.harmonicloopweighters.HarmonicLoopWeighter;
import com.treblemaker.weighters.hits.HitWeighter;
import com.treblemaker.weighters.hits.IHitWeighter;
import com.treblemaker.weighters.interfaces.IMidiWeighter;
import com.treblemaker.weighters.interfaces.IRhythmWeighter;
import com.treblemaker.weighters.interfaces.ISynthFXWeighter;
import com.treblemaker.weighters.interfaces.IWeighter;
import com.treblemaker.weighters.midiweighter.MidiWeighter;
import com.treblemaker.weighters.rhythmweighter.RhythmWeighter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@EnableAutoConfiguration
@Configuration
@ComponentScan({"com.treblemaker", "com.treblemaker.dal", "com.treblemaker.generators", "com.treblemaker.extractors", "com.treblemaker.scheduledevents", "com.treblemaker.utils",
        "com.treblemaker.machinelearning", "com.treblemaker.weighters", "com.treblemaker", "com.treblemaker.machinelearning"})
public class SpringConfiguration {

    @Value("${bypass_seqence_ratings}")
    boolean bypassSeqenceRatings;
    @Value("${cache_key_hive_cache}")
    String cacheKeyHiveCache;
    @Value("${cache_key_timeseries}")
    String cacheKeyTimeseries;
    @Value("${bypass_bassline_vertical_rating}")
    boolean bypassBasslineVerticalRating;
    @Value("${bypass_arpeggio_vertical_rating}")
    boolean bypassArpeggioVerticalRating;
    @Value("${machinelearning_endpoints}")
    String[] mlEndpoints;
    @Value("${use_only_first_machinelearn_endpoint}")
    boolean useOnlyFirstMachinelearnEndpoint;
    @Value("${num_of_generated_mixes}")
    Integer numOfGeneratedMixes;
    @Value("${num_of_generated_mix_variations}")
    Integer numOfGeneratedMixVariations;
    @Value("${bypass_eq_ratings}")
    boolean bypassEqRatings;
    @Value("${bypass_synthfx_rating}")
    boolean bypassSynthFXRating;

    @Autowired
    private SentimentMixExtraction sentimentMixExtraction;

    @Autowired
    private SentimentCharacteristicsExtraction sentimentCharacteristicsExtraction;

    @Autowired
    private IFXArpeggioDelayDal ifxArpeggioDelayDal;

    @Autowired
    private IAnalyticsHorizontalDal analyticsHorizontalDal;

    @Autowired
    private IAmbienceLoopsDal ambienceLoopsDal;

    @Autowired
    private DurationAnalysis durationAnalysis;

    @Autowired
    private IBachChoraleDal bachChoraleDal;

    @Autowired
    IAudioUtils audioUtils;

    @Autowired
    private MelodicGenerator melodicGenerator;

    @Autowired
    private MelodicExtractor melodicExtractor;

    @Bean(name = "queueDigester")
    public IQueueDigester queueDigester() {
        return new QueueDigester();
    }

    @Bean(name = "synthPadGenerator")
    public ISynthPadGenerator synthPadGenerator() {
        return new SynthPadGenerator();
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ EVENTS


    @Bean(name = "setKeyEvent")
    public IEventChain setKeyEvent() {
        return new SetKeyEvent();
    }

    @Bean(name = "setHarmonicStructureEvent")
    public IEventChain setHarmonicStructureEvent() {
        return new SetChordStructureEvent();
    }

    @Bean(name = "setChordStructureAnalyticsEvent")
    public IEventChain setChordStructureAnalyticsEvent() {
        return new SetChordStructureAnalyticsEvent();
    }

    @Bean(name = "jChordAppenderEvent")
    public IEventChain jChordAppenderEvent() {
        return new JChordAppenderEvent();
    }

    @Bean(name = "setSynthTemplateAlternatesEvent")
    public IEventChain setSynthTemplateAlternatesEvent() {
        return new SetSynthTemplateAlternatesEvent();
    }

    @Bean(name = "arpeggioHiTemplateAppenderEvent")
    public IEventChain arpeggioHiTemplateAppenderEvent() {
        return new ArpeggioHiTemplateAppenderEvent(arpeggioGenerator());
    }

    @Bean(name = "setArpeggioAnalyticsEvent")
    public IEventChain setArpeggioAnalyticsEvent() {
        return new SetArpeggioAnalyticsEvent();
    }

    @Bean(name = "setBasslineAnalyticsEvent")
    public IEventChain setBasslineAnalyticsEvent() {
        return new SetBasslineAnalyticsEvent();
    }

    @Bean(name = "arpeggioMidTemplateAppenderEvent")
    public IEventChain arpeggioMidTemplateAppenderEvent() {
        return new ArpeggioMidTemplateAppenderEvent();
    }

    @Bean(name = "arpeggioLowTemplateAppenderEvent")
    public IEventChain arpeggioLowTemplateAppenderEvent() {
        return new ArpeggioLowTemplateAppenderEvent();
    }

    @Bean(name = "synthTemplateAppenderEvent")
    public IEventChain synthTemplateAppenderEvent() {
        SynthTemplateAppenderEvent synthTemplateAppenderEvent = new SynthTemplateAppenderEvent();
        synthTemplateAppenderEvent.init(templateSelector(), numOfGeneratedMixes);

        return synthTemplateAppenderEvent;
    }

    @Bean(name = "setAmbienceLoopsAnalyticsEvent")
    public IEventChain setAmbienceLoopsAnalyticsEvent() {
        return new SetAmbienceLoopsAnalyticsEvent();
    }

    @Bean(name = "setAmbienceLoopsEvent")
    public IEventChain setAmbienceLoopsEvent() {
        return new SetAmbienceLoopsEvent();
    }

    @Bean(name = "setAmbienceLoopsAltEvent")
    public IEventChain setAmbienceLoopsAltEvent() {
        return new SetAmbienceLoopsAltEvent();
    }

    @Bean(name = "setHarmonicLoopsEvent")
    public IEventChain setHarmonicLoopsEvent() {
        return new SetHarmonicLoopsEvent();
    }

    @Bean(name = "setHarmonicLoopsAltEvent")
    public IEventChain SetHarmonicLoopsAltEvent() {
        return new SetHarmonicLoopsAltEvent();
    }

    @Bean(name = "setHarmonicLoopsAnalyticsEvent")
    public IEventChain setHarmonicLoopsAnalyticsEvent() {
        return new SetHarmonicLoopsAnalyticsEvent();
    }

    @Bean(name = "setHarmonicLoopsAltAnalyticsEvent")
    public IEventChain setHarmonicLoopsAltAnalyticsEvent() {
        return new SetHarmonicLoopsAltAnalyticsEvent();
    }

    @Bean(name = "setBeatLoopsEvent")
    public IEventChain setBeatLoopsEvent() {
        return new SetBeatLoopsEvent();
    }

    @Bean(name = "setBeatLoopsAltEvent")
    public IEventChain setBeatLoopsAltEvent() {
        return new SetBeatLoopsAltEvent();
    }

    @Bean(name = "setBeatLoopAnalyticsEvent")
    public IEventChain setBeatLoopAnalyticsEvent() {
        return new SetBeatLoopAnalyticsEvent();
    }

    @Bean(name = "setBeatLoopAltAnalyticsEvent")
    public IEventChain setBeatLoopAltAnalyticsEvent() {
        return new SetBeatLoopAltAnalyticsEvent();
    }

    @Bean(name = "setSynthTemplateAnalytics")
    public IEventChain setSynthTemplateAnalytics() {
        return new SetSynthTemplateAnalytics();
    }

    @Bean(name = "setFillsAnalyticsEvent")
    public IEventChain setFillsAnalyticsEvent() {
        return new SetFillsAnalyticsEvent();
    }

    @Bean(name = "setHitsAnalyticsEvent")
    public IEventChain setHitsAnalyticsEvent() {
        return new SetHitsAnalyticsEvent();
    }

    @Bean(name = "setKickMidiPatternEvent")
    public IEventChain setKickMidiPatternEvent() {
        return new SetKickMidiPatternEvent();
    }

    @Bean(name = "setSnareMidiPatternEvent")
    public IEventChain setSnareMidiPatternEvent() {
        return new SetSnareMidiPatternEvent();
    }

    @Bean(name = "setHatMidiPatternEvent")
    public IEventChain setHatMidiPatternEvent() {
        return new SetHatMidiPatternEvent();
    }

    @Bean(name = "setMidiPatternEvent")
    public IEventChain setMidiPatternEvent() {
        return new SetMidiPatternEvent();
    }

    @Bean(name = "setChordProgressionEvent")
    public IEventChain setChordProgressionEvent() {
        return new SetChordProgressionEvent();
    }

    @Bean(name = "setChordStructureAndHarmonicLoops")
    public IEventChain setChordStructureAndHarmonicLoops() {
        return new SetChordStructureAndHarmonicLoops();
    }

    @Bean(name = "setFXEvent")
    public IEventChain setFXEvent() {
        return new SetFXEvent(ifxArpeggioDelayDal, synthFXWeighter(), synthFXSelector());
    }

    @Bean(name = "setArpeggioFXAnalyticsEvent")
    public IEventChain setArpeggioFXAnalyticsEvent() {
        return new SetArpeggioFXAnalyticsEvent();
    }

    @Bean(name = "renderFXEvent")
    public IEventChain renderFXEvent() {
        return new RenderFXEvent();
    }

    @Bean(name = "processMixPanning")
    public IEventChain processMixPanning() {
        return new ProcessMixPanning();
    }

    @Bean(name = "setTargetVolumeMixEvent")
    public IEventChain setTargetVolumeMixEvent() {
        return new SetTargetVolumeMixEvent();
    }

    @Bean(name = "renderAndSetFinalVolumeMixEvent")
    public IEventChain renderAndSetFinalVolumeMixEvent() {
        return new RenderAndSetFinalVolumeMixEvent();
    }

    @Bean(name = "setPanningEvent")
    public IEventChain setPanningEvent() {
        return new SetPanningEvent();
    }

    @Bean(name = "setHits")
    public IEventChain setHits() {
        return new SetHits();
    }

    @Bean(name = "setFills")
    public IEventChain setFills() {
        return new SetFills();
    }

    @Bean(name = "setMelodicSynthEvent")
    public IEventChain setMelodicSynthEvent() {
        Config config = new Config(true);
        MelodyGenerator melodyGenerator = new MelodyGenerator(config);

        return new SetMelodicSynthEvent(melodyGenerator, melodicExtractor, bachChoraleDal);
    }

    @Bean(name = "setSentimentLabelsEvent")
    public IEventChain setSentimentLabelsEvent() {
        return new SetSentimentLabelsEvent(sentimentMixExtraction, sentimentCharacteristicsExtraction, numOfGeneratedMixes, numOfGeneratedMixVariations);
    }

    @Bean(name = "createMetaDataFile")
    public IEventChain createMetaDataFile() {
        return new CreateMetaDataFile();
    }

    @Bean(name = "packagingEvent")
    public IEventChain packagingEvent() {
        return new PackagingEvent();
    }

    @Autowired
    KickSampleOptions kickSampleOptions;

    @Autowired
    KickPatternSelector kickPatternSelector;

    @Autowired
    HatSampleOptions hatSampleOptions;

    @Autowired
    HatPatternSelector hatPatternSelector;

    @Autowired
    SnareSampleOptions snareSampleOptions;

    @Autowired
    SnarePatternSelector snarePatternSelector;

    @Bean(name = "setKickPatternEvent")
    public IEventChain setKickPatternEvent() {
        return new SetKickPatternEvent(kickSampleOptions, new ObjectMapper(), kickPatternSelector);
    }

    @Bean(name = "setHatPatternEvent")
    public IEventChain setHatPatternEvent() {
        return new SetHatPatternEvent(hatSampleOptions, new ObjectMapper(), hatPatternSelector);
    }

    @Bean(name = "setSnarePatternEvent")
    public IEventChain setSnarePatternEvent() {
        return new SetSnarePatternEvent(snareSampleOptions, new ObjectMapper(), snarePatternSelector);
    }

    @Bean(name = "setKickPatternAnalyticsEvent")
    public IEventChain setKickPatternAnalyticsEvent() {
        return new SetKickPatternAnalyticsEvent();
    }

    @Bean(name = "setHatPatternAnalyticsEvent")
    public IEventChain setHatPatternAnalyticsEvent() {
        return new SetHatPatternAnalyticsEvent();
    }

    @Bean(name = "setSnarePatternAnalyticsEvent")
    public IEventChain setSnarePatternAnalyticsEvent() {
        return new SetSnarePatternAnalyticsEvent();
    }

    @Bean(name = "eqFilterEvent")
    public IEventChain eqFilterEvent() {
        return new EqFilterEvent();
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ GENERATERS ..

    @Bean(name = "arpeggioGenerator")
    public IArpeggioGenerator arpeggioGenerator() {
        return new ArpeggioGenerator(arpeggioSelector(), midiWeighter(), numOfGeneratedMixes);
    }

    @Bean(name = "ambienceGenerator")
    public IAmbienceGenerator ambienceGenerator() {
        return new AmbienceGenerator(shimGenerator(), ambienceLoopsDal, audioUtils);
    }

    @Bean(name = "beatLoopGenerator")
    public IBeatLoopGenerator beatLoopGenerator() {
        return new BeatLoopGenerator();
    }

    @Bean(name = "beatLoopAltGenerator")
    public IBeatLoopGenerator beatLoopAltGenerator() {
        return new BeatLoopAltGenerator(bypassSeqenceRatings, cacheKeyHiveCache, cacheKeyTimeseries);
    }

    @Bean(name = "harmonicLoopGenerator")
    public IHarmonicLoopGenerator harmonicLoopGenerator() {
        return new HarmonicLoopGenerator(harmonicLoopSelector(), chordSelector(), harmonicLoopWeighter(), new HarmonicLoopOptions(optionsFilter()), optionsFilter());
    }

    @Bean(name = "harmonicLoopAltGenerator")
    public IHarmonicLoopGenerator harmonicLoopAltGenerator() {

        return new HarmonicLoopAltGenerator(new HarmonicLoopOptions(optionsFilter()), harmonicLoopAltWeighter(), harmonicLoopSelector(), cacheKeyHiveCache, cacheKeyTimeseries);
    }

    @Bean(name = "shimGenerator")
    public IShimGenerator shimGenerator() {
        return new ShimGenerator();
    }

    @Bean(name = "hitGenerator")
    public IHitGenerator hitGenerator() {
        return new HitGenerator();
    }

    @Bean(name = "fillsGenerator")
    public IFillsGenerator fillsGenerator() {
        return new FillsGenerator();
    }

    @Bean(name = "stationTrackGenerator")
    public StationTrackGenerator stationTrackGenerator() {
        return new StationTrackGenerator();
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ SELECTERS. ..
    @Bean(name = "chordSelector")
    public IChordSelector chordSelector() {
        return new ChordSelector();
    }

    @Bean(name = "templateSelector")
    public ITemplateSelector templateSelector() {
        return new TemplateSelector();
    }

    @Bean(name = "beatLoopSelector")
    public IBeatLoopSelector beatLoopSelector() {
        return new BeatLoopSelector();
    }

    @Bean(name = "harmonicLoopSelector")
    public IHarmonicLoopSelector harmonicLoopSelector() {
        return new HarmonicLoopSelector();
    }

    @Bean(name = "synthFXSelector")
    public ISynthFXSelector synthFXSelector() {
        return new SynthFXSelector();
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ WEIGHTERS ....

    @Bean(name = "midiWeighter")
    public IMidiWeighter midiWeighter() {
        return new MidiWeighter(bypassArpeggioVerticalRating);
    }

    @Bean(name = "rhythm")
    public IRhythmWeighter rhythmWeighter() {
        return new RhythmWeighter();
    }

    @Bean(name = "beatLoopAltWeighter")
    public IWeighter beatLoopAltWeighter() {
        return new BeatLoopAltWeighter();
    }

    @Bean(name = "harmonicLoopAltWeighter")
    public HarmonicLoopAltWeighter harmonicLoopAltWeighter() {
        return new HarmonicLoopAltWeighter();
    }

    @Bean(name = "harmonicLoopWeighter")
    public HarmonicLoopWeighter harmonicLoopWeighter() {
        return new HarmonicLoopWeighter(rhythmWeighter(), harmonicLoopTimeseriesClassifier(), bypassEqRatings);
    }

    @Bean(name = "harmonicLoopTimeseriesClassifier")
    public HarmonicLoopTimeseriesClassifier harmonicLoopTimeseriesClassifier() {
        Iterable<AnalyticsHorizontal> cachedData = (Iterable<AnalyticsHorizontal>) Application.client.getMap(cacheKeyHiveCache).get(cacheKeyTimeseries);
        return new HarmonicLoopTimeseriesClassifier(bypassSeqenceRatings, cacheKeyHiveCache, cacheKeyTimeseries, analyticsHorizontalDal, cachedData);
    }

    @Bean(name = "harmonicLoopAltTimeseriesClassifier")
    public HarmonicLoopAltTimeseriesClassifier harmonicLoopAltTimeseriesClassifier() {
        Iterable<AnalyticsHorizontal> cachedData = (Iterable<AnalyticsHorizontal>) Application.client.getMap(cacheKeyHiveCache).get(cacheKeyTimeseries);
        return new HarmonicLoopAltTimeseriesClassifier(bypassSeqenceRatings, cacheKeyHiveCache, cacheKeyTimeseries, analyticsHorizontalDal, cachedData);
    }

    @Bean(name = "hitWeighter")
    public IHitWeighter hitWeighter() {
        return new HitWeighter();
    }


    @Bean(name = "synthFXWeighter")
    public ISynthFXWeighter synthFXWeighter() {
        return new SynthFXWeighter(durationAnalysis, bypassSynthFXRating);
    }

    @Bean(name = "fillWeighter")
    public ISetFillWeighter fillWeighter() {
        return new FillWeighter();
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ RENDERER ....

    @Bean(name = "midiRender")
    public IEventChain midiRender() {
        return new MidiRender();
    }

    @Bean(name = "beatLoopRenderer")
    public IBeatLoopRenderer beatLoopRenderer() {
        return new BeatLoopRenderer();
    }

    @Bean(name = "ambienceLoopRenderer")
    public IAmbienceLoopRenderer ambienceLoopRenderer() {
        return new AmbienceLoopRenderer();
    }

    @Bean(name = "audioRender")
    public IAudioRender audioRender() {
        return new SoundFountRender();
    }

    @Bean(name = "arpeggioSelector")
    public IArpeggioSelector arpeggioSelector() {
        return new ArpeggioSelector();
    }

    @Bean(name = "hitSelector")
    public IHitSelector hitSelector() {
        return new HitSelector();
    }

    @Bean(name = "fillSelector")
    public IFillSelector fillSelector() {
        return new FillSelector();
    }

    @Bean(name = "harmonicLoopRenderer")
    public IHarmonicLoopRenderer harmonicLoopRenderer() {
        return new HarmonicLoopRenderer();
    }

    @Bean(name = "harmonicLoopAltRenderer")
    public IHarmonicLoopRenderer harmonicLoopAltRenderer() {
        return new HarmonicLoopAltRenderer();
    }

    @Bean(name = "hitRenderer")
    public IHitRenderer hitRenderer() {
        return new HitRenderer();
    }

    @Bean(name = "fillsRenderer")
    public IFillsRenderer fillsRenderer() {
        return new FillsRenderer();
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ MIXER..
    @Bean(name = "audioMixer")
    public IAudioMixer audioMixer() {
        return new AudioMixer();
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ DATA_ACCESS ..

    //ADAPTERS
    @Bean(name = "chordAdapter")
    public IChordAdapter chordAdapter() {
        return new ChordAdapter();
    }

    //GATES
    @Bean(name = "layerGroupComplexityGate")
    public IComplexityThreshold layerGroupComplexityGate() {
        return new LayerGroupComplexityGate();
    }

    @Bean(name = "templateGroupComplexityGate")
    public IComplexityThreshold templateComplexityGate() {
        return new TemplateComplexityGate();
    }

    @Bean(name = "fileStructureGenerator")
    public FileStructureGenerator fileStructureGenerator() {
        return new FileStructureGenerator();
    }

    @Bean(name = "optionsFilter")
    public OptionsFilter optionsFilter() {
        return new OptionsFilter();
    }


}