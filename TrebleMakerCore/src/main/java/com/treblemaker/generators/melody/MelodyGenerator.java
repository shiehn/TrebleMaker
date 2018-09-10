package com.treblemaker.generators.melody;

import com.treblemaker.extractors.KeyExtractor;
import com.treblemaker.model.HiveChord;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MelodyGenerator {

    String savedLocation = "/TrebleMaker/TrebleMakerCore/trained-melody-model.zip";

    MultiLayerNetwork restored = null;
    Config config;
    CharacterIterator characterIterator = null;

    public MelodyGenerator(Config config) {

        this.config = config;
        try {
            restored = ModelSerializer.restoreMultiLayerNetwork(savedLocation);
            System.out.println("MODEL RESTORED!: " + savedLocation);
        } catch (Exception e) {
            System.out.println("COULD NOT LOAD SAVED MODEL");
        }
    }

    private String encodeChordTypes(String chordType){

        if(chordType.equalsIgnoreCase("maj")){
            return "0";
        }else if (chordType.equalsIgnoreCase("min")){
            return "1";
        }else if (chordType.equalsIgnoreCase("dim")){
            return "2";
        }else if (chordType.equalsIgnoreCase("maj7")){
            return "3";
        }else if (chordType.equalsIgnoreCase("min7")){
            return "4";
        }else if (chordType.equalsIgnoreCase("dom7")){
            return "5";
        }else if (chordType.equalsIgnoreCase("min7b5")) {
            return "6";
        }else {
            throw new RuntimeException("COULD NOT ENCODE CHORDTYPE: " + chordType);
        }
    }

    private String encodeRootNotes(String rootNote){

            switch (rootNote) {
                case "r":
                    return "00";
                case "ab":
                    return "10";
                case "a":
                    return "11";
                case "a#":
                    return "12";
                case "bb":
                    return "20";
                case "b":
                    return "21";
                case "cb":
                    return "30";
                case "c":
                    return "31";
                case "c#":
                    return "32";
                case "db":
                    return "40";
                case "d":
                    return "41";
                case "d#":
                    return "42";
                case "eb":
                    return "50";
                case "e":
                    return "51";
                case "fb":
                    return "60";
                case "f":
                    return "61";
                case "f#":
                    return "62";
                case "gb":
                    return "70";
                case "g":
                    return "71";
                case "g#":
                    return "72";
            }

            throw new RuntimeException("cannot decode ROOTNOTE: " + rootNote);
    }

    public List<String> generate(List<HiveChord> chords, int numOfAltMelodies) {

        String keyAndChords = "";

        List<String> chordStrs = new ArrayList<>();
        for (HiveChord chord : chords) {
            chordStrs.add(chord.getChordName());
        }

        String key = (new KeyExtractor()).extract(chordStrs);

        keyAndChords = encodeRootNotes(key) + "^";

        for (HiveChord chord : chords) {
            String chordRoot = chord.extractRoot(chord.getChordName());
            String chordType = chord.getChordName().replaceFirst(chordRoot, "");

            System.out.println("************************");
            System.out.println("KEY: " + key);
            System.out.println("CHORD ROOT: " + chordRoot);
            System.out.println("ENCODE ROOT: " + encodeRootNotes(chordRoot));
            System.out.println("CHORD TYPE: " + chordType);
            System.out.println("ENCODE TYPE: " + encodeChordTypes(chordType));
            System.out.println("************************");

            keyAndChords = keyAndChords + encodeRootNotes(chordRoot) + encodeChordTypes(chordType) + "*";
            keyAndChords = keyAndChords + encodeRootNotes(chordRoot) + encodeChordTypes(chordType) + "*";
        }

        System.out.println("KEYS AND CHORDS: " + keyAndChords);


        /*
        CHORD TYPES
0 | maj
1 | min
2 | dim
3 | maj7
4 | min7
5 | dom7
6 | min7b5
         */

        //String keyAndChords = "31^313*313*313*313*613*613*715*715*";


        int miniBatchSize = 32;             //Size of mini batch to use when  training
        int exampleLength = 355;
        Random rng = new Random(12345);
        int nCharactersToSample = 320;
        int nSamplesToGenerate = 30;

        try {
            if(characterIterator == null) {
                characterIterator = (new DataIngestor()).getMelodyCharacterIterator(miniBatchSize, exampleLength);
            }
        }catch (Exception e){
            throw new RuntimeException("ERROR with getCharacterIterator");
        }

        String[] samples = sampleCharactersFromNetwork(keyAndChords, null, restored, characterIterator, rng, nCharactersToSample, nSamplesToGenerate);

        ChordMelodyDecoder decoder = new ChordMelodyDecoder(config);

        List<String> validSamples = new ArrayList<>();

        String validString = "";
        for(int i=0; i<samples.length; i++){
            validString = decoder.extractValidMelody(samples[i]);
            if(!validString.contains("invalid")){
                validSamples.add(validString);
            }
        }

        List<String> jFugueStrs = new ArrayList<>();

        for (int i=0; i<numOfAltMelodies; i++) {
            validString = validSamples.get(new Random().nextInt(validSamples.size()));
            String jFugueStr = decoder.decodeMelody(validString, decoder.getKey(validString));
            jFugueStrs.add(jFugueStr);
        }

        return jFugueStrs;
    }

    private static String[] sampleCharactersFromNetwork(String keyAndChords, String initialization, MultiLayerNetwork net,
                                                        CharacterIterator iter, Random rng, int charactersToSample, int numSamples) {
        //Set up initialization. If no initialization: use a random character
        if (initialization == null) {
            //initialization = String.valueOf(characterIterator.getRandomCharacter());
            initialization = keyAndChords;
        }

        //Create input for initialization
        INDArray initializationInput = Nd4j.zeros(numSamples, iter.inputColumns(), initialization.length());
        char[] init = initialization.toCharArray();
        for (int i = 0; i < init.length; i++) {
            int idx = iter.convertCharacterToIndex(init[i]);
            for (int j = 0; j < numSamples; j++) {
                initializationInput.putScalar(new int[]{j, idx, i}, 1.0f);
            }
        }

        StringBuilder[] sb = new StringBuilder[numSamples];
        for (int i = 0; i < numSamples; i++) sb[i] = new StringBuilder(initialization);

        //Sample from network (and feed samples back into input) one character at a time (for all samples)
        //Sampling is done in parallel here
        net.rnnClearPreviousState();
        INDArray output = net.rnnTimeStep(initializationInput);
        output = output.tensorAlongDimension(output.size(2) - 1, 1, 0);    //Gets the last time step output

        for (int i = 0; i < charactersToSample; i++) {
            //Set up next input (single time step) by sampling from previous output
            INDArray nextInput = Nd4j.zeros(numSamples, iter.inputColumns());
            //Output is a probability distribution. Sample from this for each example we want to generate, and add it to the new input
            for (int s = 0; s < numSamples; s++) {
                double[] outputProbDistribution = new double[iter.totalOutcomes()];
                for (int j = 0; j < outputProbDistribution.length; j++)
                    outputProbDistribution[j] = output.getDouble(s, j);
                int sampledCharacterIdx = sampleFromDistribution(outputProbDistribution, rng);

                nextInput.putScalar(new int[]{s, sampledCharacterIdx}, 1.0f);        //Prepare next time step input
                sb[s].append(iter.convertIndexToCharacter(sampledCharacterIdx));    //Add sampled character to StringBuilder (human readable output)
            }

            output = net.rnnTimeStep(nextInput);    //Do one time step of forward pass
        }

        String[] out = new String[numSamples];
        for (int i = 0; i < numSamples; i++) out[i] = sb[i].toString();
        return out;
    }

    public static int sampleFromDistribution(double[] distribution, Random rng) {
        double d = 0.0;
        double sum = 0.0;
        for (int t = 0; t < 10; t++) {
            d = rng.nextDouble();
            sum = 0.0;
            for (int i = 0; i < distribution.length; i++) {
                sum += distribution[i];
                if (d <= sum) return i;
            }
            //If we haven't found the right index yet, maybe the sum is slightly
            //lower than 1 due to rounding error, so try again.
        }
        //Should be extremely unlikely to happen if distribution is a valid probability distribution
        throw new IllegalArgumentException("Distribution is invalid? d=" + d + ", sum=" + sum);
    }
}
