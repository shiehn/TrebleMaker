package com.treblemaker.generators;

import com.treblemaker.generators.melody.CharacterIterator;
import com.treblemaker.generators.melody.ChordMelodyDecoder;
import com.treblemaker.generators.melody.DataIngestor;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChordProgressionGenerator {
    String savedLocation = "/TrebleMaker/TrebleMakerCore/trained-chords-model.zip";
    MultiLayerNetwork restored = null;
    CharacterIterator characterIterator = null;

    public ChordProgressionGenerator() {
        try {
            restored = ModelSerializer.restoreMultiLayerNetwork(savedLocation);
            System.out.println("MODEL RESTORED!: " + savedLocation);
        } catch (Exception e) {
            System.out.println("COULD NOT LOAD SAVED MODEL");
        }
    }

    public List<List<String>> getChords(String key, String chordOne, String chordTwo){
        List<List<String>> chordLists = new ArrayList<>();

        int miniBatchSize = 32;             //Size of mini batch to use when  training
        int exampleLength = 27;//100 per row         //Length of each training example sequence to use. This could certainly be increased
        int nCharactersToSample = 15;                //Length of each sample to generate
        int nSamplesToGenerate = 20;                    //Number of samples to generate after each training epoch
        Random rng = new Random(12345);

        try {
            if(characterIterator == null) {
                characterIterator = (new DataIngestor()).getChordCharacterIterator(miniBatchSize, exampleLength);
            }
        }catch (Exception e){
            throw new RuntimeException("ERROR with getCharacterIterator");
        }

        String prefix = formatPrefix(key,chordOne,chordTwo);
        String[] samples = sampleCharactersFromNetwork(prefix, null, restored, characterIterator, rng, nCharactersToSample, nSamplesToGenerate);

        ChordMelodyDecoder decoder = new ChordMelodyDecoder(null);

        String validString = "";
        for(int i=0; i<samples.length; i++){
            validString = decoder.extractValidChords(samples[i]);
            if(!validString.contains("invalid")){
               List<String> chords = decoder.extractChordPredictionToArray(validString);
               chordLists.add(chords);
            }
        }

        return chordLists;
    }

    private String[] sampleCharactersFromNetwork(String keyAndChords, String initialization, MultiLayerNetwork net,
                                                        CharacterIterator iter, Random rng, int charactersToSample, int numSamples) {
        //Set up initialization. If no initialization: use a random character
        if (initialization == null) {
            //initialization = String.valueOf(iter.getRandomCharacter());
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

    public int sampleFromDistribution(double[] distribution, Random rng) {
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

    public String formatPrefix(String key, String chordOne, String chordTwo){

        //#31:313-313%
        return "#" + key + ":" + chordOne + "-" + chordTwo + "%";
    }
}
