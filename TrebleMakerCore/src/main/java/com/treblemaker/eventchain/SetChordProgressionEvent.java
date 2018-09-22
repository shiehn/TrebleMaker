package com.treblemaker.eventchain;

import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.extractors.KeyExtractor;
import com.treblemaker.generators.ChordProgressionGenerator;
import com.treblemaker.generators.melody.ChordMelodyDecoder;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SetChordProgressionEvent implements IEventChain {

    //ChordProgressionGenerator chordProgressionGenerator = new ChordProgressionGenerator();

    @Override
    public QueueState set(QueueState queueState) {
        ChordMelodyDecoder decoder = new ChordMelodyDecoder(null);
        KeyExtractor keyExtractor = new KeyExtractor();

        String encodedKey = getRandomKey();
        String decodedKey = decoder.getNoteName(encodedKey);

        List<HiveChord> chords = keyExtractor.getChordsInKey(decodedKey);

        HiveChord chordOne = chords.get((new Random().nextInt(chords.size())));
        HiveChord chordTwo = chords.get((new Random().nextInt(chords.size())));

        List<String> verseChords = getChordForProgressionUnit(encodedKey, chordOne, chordTwo, queueState.getAppRoot());
        List<HiveChord> verseHiveChords = verseChords.stream().map(c -> new HiveChord(c)).collect(Collectors.toList());

        List<String> chorusChords = getChordForProgressionUnit(encodedKey, new HiveChord(verseChords.get(2)), new HiveChord(verseChords.get(3)), queueState.getAppRoot());
        List<HiveChord> chorusHiveChords = chorusChords.stream().map(c -> new HiveChord(c)).collect(Collectors.toList());

        List<String> bridgeChords = getChordForProgressionUnit(encodedKey, new HiveChord(chorusChords.get(2)), new HiveChord(chorusChords.get(3)), queueState.getAppRoot());
        List<HiveChord> bridgeHiveChords = bridgeChords.stream().map(c -> new HiveChord(c)).collect(Collectors.toList());

        for(ProgressionUnit progressionUnit : queueState.getStructure()){
            ProgressionUnit.ProgressionType type = progressionUnit.getType();

            switch (type) {
                case VERSE:
                    for (int i = 0; i < progressionUnit.getProgressionUnitBars().size(); i++) {
                        progressionUnit.getProgressionUnitBars().get(i).setChord(verseHiveChords.get(i));
                    }
                    break;
                case CHORUS:
                    for (int i = 0; i < progressionUnit.getProgressionUnitBars().size(); i++) {
                        progressionUnit.getProgressionUnitBars().get(i).setChord(chorusHiveChords.get(i));
                    }
                    break;
                case BRIDGE:
                    for (int i = 0; i < progressionUnit.getProgressionUnitBars().size(); i++) {
                        progressionUnit.getProgressionUnitBars().get(i).setChord(bridgeHiveChords.get(i));
                    }
                    break;
                default:
                    throw new RuntimeException("unexpected progression unit type");
            }
        }

        return queueState;
    }

    public List<String> getChordForProgressionUnit(String key, HiveChord chordOne, HiveChord chordTwo, String appRoot){
        ChordMelodyDecoder decoder = new ChordMelodyDecoder(null);
        KeyExtractor keyExtractor = new KeyExtractor();

        String chordRootOne = decoder.encodeNoteName(chordOne.extractRoot(chordOne.getChordName()));
        String chordTypeOne = chordOne.getChordName().replace(chordOne.extractRoot(chordOne.getChordName()), "");
        String chordTypeOneEncoded = decoder.encodeChordType(chordTypeOne);

        String chordRootTwo = decoder.encodeNoteName(chordTwo.extractRoot(chordTwo.getChordName()));
        String chordTypeTwo = chordTwo.getChordName().replace(chordTwo.extractRoot(chordTwo.getChordName()), "");
        String chordTypeTwoEncoded = decoder.encodeChordType(chordTypeTwo);

        ChordProgressionGenerator chordProgressionGenerator = new ChordProgressionGenerator(appRoot);
        List<List<String>> progressions = chordProgressionGenerator.getChords(key,
                (chordRootOne + chordTypeOneEncoded),
                (chordRootTwo + chordTypeTwoEncoded));

        List<String> selectedProgressionChords = progressions.get((new Random()).nextInt(progressions.size()));

        return selectedProgressionChords;
    }

    public String getRandomKey() {
        List<String> keys = Arrays.asList("a","a#", "b", "c","c#","d","d#","e","f","f#","g","g#");
        ChordMelodyDecoder decoder = new ChordMelodyDecoder(null);

        String key = keys.get((new Random()).nextInt(keys.size()));
        return decoder.encodeNoteName(key);
    }

    public int getRandomChordFromKey(int key){
        return 9999999;
    }
}
