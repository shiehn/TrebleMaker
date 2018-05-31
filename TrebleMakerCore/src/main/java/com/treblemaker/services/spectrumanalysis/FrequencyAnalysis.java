package com.treblemaker.services.spectrumanalysis;

import com.treblemaker.Application;
import com.treblemaker.services.spectrumanalysis.model.ParametricEqArrays;
import com.treblemaker.services.spectrumanalysis.model.ParametricEqMedians;
import ddf.minim.AudioSample;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class FrequencyAnalysis {

    public ParametricEqMedians extractEq(String audioFile){

//        audioFile = "c:\\upload_test.wav";

//        ParametricEqMedians parametricEqMedians = new ParametricEqMedians();

        ParametricEqMedians eqLeft = extractEqFromChannel(audioFile, AudioSample.LEFT);
//        ParametricEqMedians eqRight = extractEqFromChannel(audioFile, AudioSample.RIGHT);
//
//        parametricEqMedians.setFreq20(    (eqLeft.getFreq20()    +    eqRight.getFreq20()   )/2  );
//        parametricEqMedians.setFreq25(    (eqLeft.getFreq25()    +    eqRight.getFreq25()   )/2  );
//        parametricEqMedians.setFreq31(    (eqLeft.getFreq31()    +    eqRight.getFreq31()   )/2  );
//        parametricEqMedians.setFreq40(    (eqLeft.getFreq40()    +    eqRight.getFreq40()   )/2  );
//        parametricEqMedians.setFreq50(    (eqLeft.getFreq50()    +    eqRight.getFreq50()   )/2  );
//        parametricEqMedians.setFreq63(    (eqLeft.getFreq63()    +    eqRight.getFreq63()   )/2  );
//        parametricEqMedians.setFreq80(    (eqLeft.getFreq80()    +    eqRight.getFreq80()   )/2  );
//        parametricEqMedians.setFreq100(   (eqLeft.getFreq100()   +    eqRight.getFreq100()  )/2    );
//        parametricEqMedians.setFreq125(   (eqLeft.getFreq125()   +    eqRight.getFreq125()  )/2    );
//        parametricEqMedians.setFreq160(   (eqLeft.getFreq160()   +    eqRight.getFreq160()  )/2    );
//        parametricEqMedians.setFreq200(   (eqLeft.getFreq200()   +    eqRight.getFreq200()  )/2    );
//        parametricEqMedians.setFreq250(   (eqLeft.getFreq250()   +    eqRight.getFreq250()  )/2    );
//        parametricEqMedians.setFreq315(   (eqLeft.getFreq315()   +    eqRight.getFreq315()  )/2    );
//        parametricEqMedians.setFreq400(   (eqLeft.getFreq400()   +    eqRight.getFreq400()  )/2    );
//        parametricEqMedians.setFreq500(   (eqLeft.getFreq500()   +    eqRight.getFreq500()  )/2    );
//        parametricEqMedians.setFreq630(   (eqLeft.getFreq630()   +    eqRight.getFreq630()  )/2    );
//        parametricEqMedians.setFreq800(   (eqLeft.getFreq800()   +    eqRight.getFreq800()  )/2    );
//        parametricEqMedians.setFreq1000(  (eqLeft.getFreq1000()  +    eqRight.getFreq1000() )/2     );
//        parametricEqMedians.setFreq1250(  (eqLeft.getFreq1250()  +    eqRight.getFreq1250() )/2     );
//        parametricEqMedians.setFreq1600(  (eqLeft.getFreq1600()  +    eqRight.getFreq1600() )/2     );
//        parametricEqMedians.setFreq2000(  (eqLeft.getFreq2000()  +    eqRight.getFreq2000() )/2     );
//        parametricEqMedians.setFreq2500(  (eqLeft.getFreq2500()  +    eqRight.getFreq2500() )/2     );
//        parametricEqMedians.setFreq3150(  (eqLeft.getFreq3150()  +    eqRight.getFreq3150() )/2     );
//        parametricEqMedians.setFreq4000(  (eqLeft.getFreq4000()  +    eqRight.getFreq4000() )/2     );
//        parametricEqMedians.setFreq5000(  (eqLeft.getFreq5000()  +    eqRight.getFreq5000() )/2     );
//        parametricEqMedians.setFreq6300(  (eqLeft.getFreq6300()  +    eqRight.getFreq6300() )/2     );
//        parametricEqMedians.setFreq8000(  (eqLeft.getFreq8000()  +    eqRight.getFreq8000() )/2     );
//        parametricEqMedians.setFreq10000( (eqLeft.getFreq10000() +    eqRight.getFreq10000())/2      );
//        parametricEqMedians.setFreq12500( (eqLeft.getFreq12500() +    eqRight.getFreq12500())/2      );
//        parametricEqMedians.setFreq16000( (eqLeft.getFreq16000() +    eqRight.getFreq16000())/2      );
//        parametricEqMedians.setFreq20000( (eqLeft.getFreq20000() +    eqRight.getFreq20000())/2      );


//        Application.logger.debug("LOG:","############### FINAL ######################");
//
//        Application.logger.debug("LOG:","AVG FREQ 20 : " +    eqLeft.getFreq20()   );
//        Application.logger.debug("LOG:","AVG FREQ 25 : " +    eqLeft.getFreq25()   );
//        Application.logger.debug("LOG:","AVG FREQ 31 : " +    eqLeft.getFreq31()   );
//        Application.logger.debug("LOG:","AVG FREQ 40 : " +    eqLeft.getFreq40()   );
//        Application.logger.debug("LOG:","AVG FREQ 50 : " +    eqLeft.getFreq50()   );
//        Application.logger.debug("LOG:","AVG FREQ 63 : " +    eqLeft.getFreq63()   );
//        Application.logger.debug("LOG:","AVG FREQ 80 : " +    eqLeft.getFreq80()   );
//        Application.logger.debug("LOG:","AVG FREQ 100 : " +   eqLeft.getFreq100()  );
//        Application.logger.debug("LOG:","AVG FREQ 125 : " +   eqLeft.getFreq125()  );
//        Application.logger.debug("LOG:","AVG FREQ 160 : " +   eqLeft.getFreq160()  );
//        Application.logger.debug("LOG:","AVG FREQ 200 : " +   eqLeft.getFreq200()  );
//        Application.logger.debug("LOG:","AVG FREQ 250 : " +   eqLeft.getFreq250()  );
//        Application.logger.debug("LOG:","AVG FREQ 315 : " +   eqLeft.getFreq315()  );
//        Application.logger.debug("LOG:","AVG FREQ 400 : " +   eqLeft.getFreq400()  );
//        Application.logger.debug("LOG:","AVG FREQ 500 : " +   eqLeft.getFreq500()  );
//        Application.logger.debug("LOG:","AVG FREQ 630 : " +   eqLeft.getFreq630()  );
//        Application.logger.debug("LOG:","AVG FREQ 800 : " +   eqLeft.getFreq800()  );
//        Application.logger.debug("LOG:","AVG FREQ 1000 : " +  eqLeft.getFreq1000() );
//        Application.logger.debug("LOG:","AVG FREQ 1250 : " +  eqLeft.getFreq1250() );
//        Application.logger.debug("LOG:","AVG FREQ 1600 : " +  eqLeft.getFreq1600() );
//        Application.logger.debug("LOG:","AVG FREQ 2000 : " +  eqLeft.getFreq2000() );
//        Application.logger.debug("LOG:","AVG FREQ 2500 : " +  eqLeft.getFreq2500() );
//        Application.logger.debug("LOG:","AVG FREQ 3150 : " +  eqLeft.getFreq3150() );
//        Application.logger.debug("LOG:","AVG FREQ 4000 : " +  eqLeft.getFreq4000() );
//        Application.logger.debug("LOG:","AVG FREQ 5000 : " +  eqLeft.getFreq5000() );
//        Application.logger.debug("LOG:","AVG FREQ 6300 : " +  eqLeft.getFreq6300() );
//        Application.logger.debug("LOG:","AVG FREQ 8000 : " +  eqLeft.getFreq8000() );
//        Application.logger.debug("LOG:","AVG FREQ 10000 : " + eqLeft.getFreq10000());
//        Application.logger.debug("LOG:","AVG FREQ 12500 : " + eqLeft.getFreq12500());
//        Application.logger.debug("LOG:","AVG FREQ 16000 : " + eqLeft.getFreq16000());
//        Application.logger.debug("LOG:","AVG FREQ 20000 : " + eqLeft.getFreq20000());
//
//        Application.logger.debug("LOG:","###################################################");

        return eqLeft;
    }

    private ParametricEqMedians extractEqFromChannel(String audioFile, int channel){

        Minim minim = new Minim(this);

        AudioSample audioSample = minim.loadSample(audioFile);

        // get the left channel of the audio as a float array
        // getChannel is defined in the interface BuffereAudio,
        // which also defines two constants to use as an argument
        // BufferedAudio.LEFT and BufferedAudio.RIGHT
        float[] leftChannel = audioSample.getChannel(channel);

        // then we create an array we'll copy sample data into for the FFT object
        // this should be as large as you want your FFT to be. generally speaking, 1024 is probably fine.
        int fftSize = 1024;
        float[] fftSamples = new float[fftSize];
        FFT fft = new FFT( fftSize, audioSample.sampleRate() );

        // now we'll analyze the samples in chunks
        int totalChunks = (leftChannel.length / fftSize) + 1;

        int index = 0;
        int arraySize = totalChunks * 512;

        ParametricEqArrays parametricEq = new ParametricEqArrays(arraySize);

        // allocate a 2-dimentional array that will hold all of the spectrum data for all of the chunks.
        // the second dimension if fftSize/2 because the spectrum size is always half the number of samples analyzed.
        double[][] spectra = new double[totalChunks][fftSize/2];

        for(int chunkIdx = 0; chunkIdx < totalChunks; ++chunkIdx)
        {
            int chunkStartIndex = chunkIdx * fftSize;

            // the chunk size will always be fftSize, except for the
            // last chunk, which will be however many samples are left in source
            int chunkSize = java.lang.Math.min( leftChannel.length - chunkStartIndex, fftSize );

            // copy first chunk into our analysis array
            System.arraycopy( leftChannel, // source of the copy
                    chunkStartIndex, // index to start in the source
                    fftSamples, // destination of the copy
                    0, // index to copy to
                    chunkSize // how many samples to copy
            );

            // if the chunk was smaller than the fftSize, we need to pad the analysis buffer with zeroes
            if ( chunkSize < fftSize )
            {
                // we use a system call for this
                java.util.Arrays.fill( fftSamples, chunkSize, fftSamples.length - 1, 0.0f );
            }

            // now analyze this buffer
            fft.forward( fftSamples );

            // and copy the resulting spectrum into our spectra array
            for(int i = 0; i < 512; ++i)
            {
                spectra[chunkIdx][i] = fft.getBand(i);

                parametricEq.getFreq20()[index] = fft.getFreq(20);
                parametricEq.getFreq25()[index] = fft.getFreq(25);
                parametricEq.getFreq31()[index] = fft.getFreq(31);
                parametricEq.getFreq40()[index] = fft.getFreq(40);
                parametricEq.getFreq50()[index] = fft.getFreq(50);
                parametricEq.getFreq63()[index] = fft.getFreq(63);
                parametricEq.getFreq80()[index] = fft.getFreq(80);
                parametricEq.getFreq100()[index] = fft.getFreq(100);
                parametricEq.getFreq125()[index] = fft.getFreq(125);
                parametricEq.getFreq160()[index] = fft.getFreq(160);
                parametricEq.getFreq200()[index] = fft.getFreq(200);
                parametricEq.getFreq250()[index] = fft.getFreq(250);
                parametricEq.getFreq315()[index] = fft.getFreq(315);
                parametricEq.getFreq400()[index] = fft.getFreq(400);
                parametricEq.getFreq500()[index] = fft.getFreq(500);
                parametricEq.getFreq630()[index] = fft.getFreq(630);
                parametricEq.getFreq800()[index] = fft.getFreq(800);
                parametricEq.getFreq1000()[index] = fft.getFreq(1000);
                parametricEq.getFreq1250()[index] = fft.getFreq(1250);
                parametricEq.getFreq1600()[index] = fft.getFreq(1600);
                parametricEq.getFreq2000()[index] = fft.getFreq(2000);
                parametricEq.getFreq2500()[index] = fft.getFreq(2500);
                parametricEq.getFreq3150()[index] = fft.getFreq(3150);
                parametricEq.getFreq4000()[index] = fft.getFreq(4000);
                parametricEq.getFreq5000()[index] = fft.getFreq(5000);
                parametricEq.getFreq6300()[index] = fft.getFreq(6300);
                parametricEq.getFreq8000()[index] = fft.getFreq(8000);
                parametricEq.getFreq10000()[index] = fft.getFreq(10000);
                parametricEq.getFreq12500()[index] = fft.getFreq(12500);
                parametricEq.getFreq16000()[index] = fft.getFreq(16000);
                parametricEq.getFreq20000()[index] = fft.getFreq(20000);

                index++;
            }
        }

        audioSample.close();

        ParametricEqMedians parametricEqMedians = new ParametricEqMedians();

        parametricEqMedians.setFreq20(   calculateMedian(parametricEq.getFreq20()));
        parametricEq.setFreq20(null);
        parametricEqMedians.setFreq25(   calculateMedian(parametricEq.getFreq25()));
        parametricEq.setFreq25(null);
        parametricEqMedians.setFreq31(   calculateMedian(parametricEq.getFreq31()));
        parametricEq.setFreq31(null);
        parametricEqMedians.setFreq40(   calculateMedian(parametricEq.getFreq40()));
        parametricEq.setFreq40(null);
        parametricEqMedians.setFreq50(   calculateMedian(parametricEq.getFreq50()));
        parametricEq.setFreq50(null);
        parametricEqMedians.setFreq63(   calculateMedian(parametricEq.getFreq63()));
        parametricEq.setFreq63(null);
        parametricEqMedians.setFreq80(   calculateMedian(parametricEq.getFreq80()));
        parametricEq.setFreq80(null);
        parametricEqMedians.setFreq100(  calculateMedian(parametricEq.getFreq100()));
        parametricEq.setFreq100(null);
        parametricEqMedians.setFreq125(  calculateMedian(parametricEq.getFreq125()));
        parametricEq.setFreq125(null);
        parametricEqMedians.setFreq160(  calculateMedian(parametricEq.getFreq160()));
        parametricEq.setFreq160(null);
        parametricEqMedians.setFreq200(  calculateMedian(parametricEq.getFreq200()));
        parametricEq.setFreq200(null);
        parametricEqMedians.setFreq250(  calculateMedian(parametricEq.getFreq250()));
        parametricEq.setFreq250(null);
        parametricEqMedians.setFreq315(  calculateMedian(parametricEq.getFreq315()));
        parametricEq.setFreq315(null);
        parametricEqMedians.setFreq400(  calculateMedian(parametricEq.getFreq400()));
        parametricEq.setFreq400(null);
        parametricEqMedians.setFreq500(  calculateMedian(parametricEq.getFreq500()));
        parametricEq.setFreq500(null);
        parametricEqMedians.setFreq630(  calculateMedian(parametricEq.getFreq630()));
        parametricEq.setFreq630(null);
        parametricEqMedians.setFreq800(  calculateMedian(parametricEq.getFreq800()));
        parametricEq.setFreq800(null);
        parametricEqMedians.setFreq1000( calculateMedian(parametricEq.getFreq1000()));
        parametricEq.setFreq1000(null);
        parametricEqMedians.setFreq1250( calculateMedian(parametricEq.getFreq1250()));
        parametricEq.setFreq1250(null);
        parametricEqMedians.setFreq1600( calculateMedian(parametricEq.getFreq1600()));
        parametricEq.setFreq1600(null);
        parametricEqMedians.setFreq2000( calculateMedian(parametricEq.getFreq2000()));
        parametricEq.setFreq2000(null);
        parametricEqMedians.setFreq2500( calculateMedian(parametricEq.getFreq2500()));
        parametricEq.setFreq2500(null);
        parametricEqMedians.setFreq3150( calculateMedian(parametricEq.getFreq3150()));
        parametricEq.setFreq3150(null);
        parametricEqMedians.setFreq4000( calculateMedian(parametricEq.getFreq4000()));
        parametricEq.setFreq4000(null);
        parametricEqMedians.setFreq5000( calculateMedian(parametricEq.getFreq5000()));
        parametricEq.setFreq5000(null);
        parametricEqMedians.setFreq6300( calculateMedian(parametricEq.getFreq6300()));
        parametricEq.setFreq6300(null);
        parametricEqMedians.setFreq8000( calculateMedian(parametricEq.getFreq8000()));
        parametricEq.setFreq8000(null);
        parametricEqMedians.setFreq10000(calculateMedian(parametricEq.getFreq10000()));
        parametricEq.setFreq10000(null);
        parametricEqMedians.setFreq12500(calculateMedian(parametricEq.getFreq12500()));
        parametricEq.setFreq12500(null);
        parametricEqMedians.setFreq16000(calculateMedian(parametricEq.getFreq16000()));
        parametricEq.setFreq16000(null);
        parametricEqMedians.setFreq20000(calculateMedian(parametricEq.getFreq20000()));
        parametricEq.setFreq20000(null);

        //HINT TO THE GC TO COLLECT THIS
        parametricEq = null;

        //FIND THE HIGHEST VALUE :
        double highest = highestValue(parametricEqMedians.getAllBands());

        parametricEqMedians.setFreq20(     parametricEqMedians.getFreq20()    / highest * 100);
        parametricEqMedians.setFreq25(     parametricEqMedians.getFreq25()    / highest * 100);
        parametricEqMedians.setFreq31(     parametricEqMedians.getFreq31()    / highest * 100);
        parametricEqMedians.setFreq40(     parametricEqMedians.getFreq40()    / highest * 100);
        parametricEqMedians.setFreq50(     parametricEqMedians.getFreq50()    / highest * 100);
        parametricEqMedians.setFreq63(     parametricEqMedians.getFreq63()    / highest * 100);
        parametricEqMedians.setFreq80(     parametricEqMedians.getFreq80()    / highest * 100);
        parametricEqMedians.setFreq100(    parametricEqMedians.getFreq100()   / highest * 100);
        parametricEqMedians.setFreq125(    parametricEqMedians.getFreq125()   / highest * 100);
        parametricEqMedians.setFreq160(    parametricEqMedians.getFreq160()   / highest * 100);
        parametricEqMedians.setFreq200(    parametricEqMedians.getFreq200()   / highest * 100);
        parametricEqMedians.setFreq250(    parametricEqMedians.getFreq250()   / highest * 100);
        parametricEqMedians.setFreq315(    parametricEqMedians.getFreq315()   / highest * 100);
        parametricEqMedians.setFreq400(    parametricEqMedians.getFreq400()   / highest * 100);
        parametricEqMedians.setFreq500(    parametricEqMedians.getFreq500()   / highest * 100);
        parametricEqMedians.setFreq630(    parametricEqMedians.getFreq630()   / highest * 100);
        parametricEqMedians.setFreq800(    parametricEqMedians.getFreq800()   / highest * 100);
        parametricEqMedians.setFreq1000(   parametricEqMedians.getFreq1000()  / highest * 100);
        parametricEqMedians.setFreq1250(   parametricEqMedians.getFreq1250()  / highest * 100);
        parametricEqMedians.setFreq1600(   parametricEqMedians.getFreq1600()  / highest * 100);
        parametricEqMedians.setFreq2000(   parametricEqMedians.getFreq2000()  / highest * 100);
        parametricEqMedians.setFreq2500(   parametricEqMedians.getFreq2500()  / highest * 100);
        parametricEqMedians.setFreq3150(   parametricEqMedians.getFreq3150()  / highest * 100);
        parametricEqMedians.setFreq4000(   parametricEqMedians.getFreq4000()  / highest * 100);
        parametricEqMedians.setFreq5000(   parametricEqMedians.getFreq5000()  / highest * 100);
        parametricEqMedians.setFreq6300(   parametricEqMedians.getFreq6300()  / highest * 100);
        parametricEqMedians.setFreq8000(   parametricEqMedians.getFreq8000()  / highest * 100);
        parametricEqMedians.setFreq10000(  parametricEqMedians.getFreq10000() / highest * 100);
        parametricEqMedians.setFreq12500(  parametricEqMedians.getFreq12500() / highest * 100);
        parametricEqMedians.setFreq16000(  parametricEqMedians.getFreq16000() / highest * 100);
        parametricEqMedians.setFreq20000(  parametricEqMedians.getFreq20000() / highest * 100);

        Application.logger.debug("LOG: ###################################################");

        Application.logger.debug("LOG: AVG FREQ 20 : " +    parametricEqMedians.getFreq20()   );
        Application.logger.debug("LOG: AVG FREQ 25 : " +    parametricEqMedians.getFreq25()   );
        Application.logger.debug("LOG: AVG FREQ 31 : " +    parametricEqMedians.getFreq31()   );
        Application.logger.debug("LOG: AVG FREQ 40 : " +    parametricEqMedians.getFreq40()   );
        Application.logger.debug("LOG: AVG FREQ 50 : " +    parametricEqMedians.getFreq50()   );
        Application.logger.debug("LOG: AVG FREQ 63 : " +    parametricEqMedians.getFreq63()   );
        Application.logger.debug("LOG: AVG FREQ 80 : " +    parametricEqMedians.getFreq80()   );
        Application.logger.debug("LOG: AVG FREQ 100 : " +   parametricEqMedians.getFreq100()  );
        Application.logger.debug("LOG: AVG FREQ 125 : " +   parametricEqMedians.getFreq125()  );
        Application.logger.debug("LOG: AVG FREQ 160 : " +   parametricEqMedians.getFreq160()  );
        Application.logger.debug("LOG: AVG FREQ 200 : " +   parametricEqMedians.getFreq200()  );
        Application.logger.debug("LOG: AVG FREQ 250 : " +   parametricEqMedians.getFreq250()  );
        Application.logger.debug("LOG: AVG FREQ 315 : " +   parametricEqMedians.getFreq315()  );
        Application.logger.debug("LOG: AVG FREQ 400 : " +   parametricEqMedians.getFreq400()  );
        Application.logger.debug("LOG: AVG FREQ 500 : " +   parametricEqMedians.getFreq500()  );
        Application.logger.debug("LOG: AVG FREQ 630 : " +   parametricEqMedians.getFreq630()  );
        Application.logger.debug("LOG: AVG FREQ 800 : " +   parametricEqMedians.getFreq800()  );
        Application.logger.debug("LOG: AVG FREQ 1000 : " +  parametricEqMedians.getFreq1000() );
        Application.logger.debug("LOG: AVG FREQ 1250 : " +  parametricEqMedians.getFreq1250() );
        Application.logger.debug("LOG: AVG FREQ 1600 : " +  parametricEqMedians.getFreq1600() );
        Application.logger.debug("LOG: AVG FREQ 2000 : " +  parametricEqMedians.getFreq2000() );
        Application.logger.debug("LOG: AVG FREQ 2500 : " +  parametricEqMedians.getFreq2500() );
        Application.logger.debug("LOG: AVG FREQ 3150 : " +  parametricEqMedians.getFreq3150() );
        Application.logger.debug("LOG: AVG FREQ 4000 : " +  parametricEqMedians.getFreq4000() );
        Application.logger.debug("LOG: AVG FREQ 5000 : " +  parametricEqMedians.getFreq5000() );
        Application.logger.debug("LOG: AVG FREQ 6300 : " +  parametricEqMedians.getFreq6300() );
        Application.logger.debug("LOG: AVG FREQ 8000 : " +  parametricEqMedians.getFreq8000() );
        Application.logger.debug("LOG: AVG FREQ 10000 : " + parametricEqMedians.getFreq10000());
        Application.logger.debug("LOG: AVG FREQ 12500 : " + parametricEqMedians.getFreq12500());
        Application.logger.debug("LOG: AVG FREQ 16000 : " + parametricEqMedians.getFreq16000());
        Application.logger.debug("LOG: AVG FREQ 20000 : " + parametricEqMedians.getFreq20000());

        Application.logger.debug("LOG: ###################################################");

        return parametricEqMedians;
    }

    private double highestValue(double[] allBands){

        double highestValue = 0.0000001;

        for(int i=0; i<allBands.length; i++){

            if(allBands[i] >  highestValue){
                highestValue = allBands[i];
            }
        }

        return highestValue;
    }

    //MATH UTIL ...
    private double calculateMedian(double[] values){

        int size = 0;
        for(double value : values){
            if(value != 0.0){
                size++;
            }
        }

        double[] scores = new double[size];
        int j=0;
        for(int i=0; i<values.length; i++){
            if(values[i] != 0.0){
                scores[j] = values[i];
                j++;
            }
        }

        Arrays.sort(scores);
//	    System.out.print("Sorted Scores: ");
//	    for (double x : scores) {
//	      System.out.print(x + " ");
//	    }
//	    Application.logger.debug("LOG:","");

        // Calculate median (middle number)
        double median = 0;
        double pos1 = Math.floor((scores.length - 1.0) / 2.0);
        double pos2 = Math.ceil((scores.length - 1.0) / 2.0);
        if (pos1 == pos2 ) {
            median = scores[(int)pos1];
        } else {
            median = (scores[(int)pos1] + scores[(int)pos2]) / 2.0 ;
        }

        //Application.logger.debug("LOG:","Median: " + median);

        return median;
    }

    public String sketchPath(String fileName){

        Application.logger.debug("LOG: ^^^^^^^^^^^^^ sketchPath ^^^^^^^^^^^^");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^ sketchPath ^^^^^^^^^^^^");
        return fileName;
    }

    public InputStream createInput(String fileName){

        Application.logger.debug("LOG: ^^^^^^^^^^^^^ createInput ^^^^^^^^^^^^");
        Application.logger.debug("LOG: ^^^^^^^^^^^^^ createInput ^^^^^^^^^^^^");

        Application.logger.debug("LOG: ^^^^^^^^^^^^^ createInput FILE NAME : " + fileName);

        Path path = Paths.get(fileName);

        System.out.print("CREATE INPUT EXISTS : " + path);

        byte[] data = null;
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            Application.logger.debug("LOG:", e);
        }

        InputStream inputStream = new ByteArrayInputStream(data);

        return inputStream;
    }
}
