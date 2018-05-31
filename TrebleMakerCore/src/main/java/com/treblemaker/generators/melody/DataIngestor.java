package com.treblemaker.generators.melody;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Random;

public class DataIngestor {

    public CharacterIterator getMelodyCharacterIterator(int miniBatchSize, int sequenceLength) throws Exception {
        String url = "https://s3-us-west-2.amazonaws.com/sastrainingdata/chord-melody-data.txt?cache=" + DateTime.now();
        String fileLocation = "chord-melody-data.txt";    //Storage location from downloaded file
        File f = new File(fileLocation);
        if (!f.exists()) {
            FileUtils.copyURLToFile(new URL(url), f);
            System.out.println("File downloaded to " + f.getAbsolutePath());
        } else {
            System.out.println("Using existing text file at " + f.getAbsolutePath());
        }

        if (!f.exists()) throw new IOException("File does not exist: " + fileLocation);    //Download problem?

        char[] validCharacters = {'^', '+', '#', '*', '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        //char[] validCharacters = CharacterIterator.getMinimalCharacterSet();	//Which characters are allowed? Others will be removed
        return new CharacterIterator(fileLocation, Charset.forName("UTF-8"),
                miniBatchSize, sequenceLength, validCharacters, new Random(12345));
    }

    public CharacterIterator getChordCharacterIterator(int miniBatchSize, int sequenceLength) throws Exception {
        String url = "https://s3-us-west-2.amazonaws.com/sastrainingdata/chord-data.txt?cache=" + DateTime.now();
        String fileLocation = "chord-data.txt";    //Storage location from downloaded file
        File f = new File(fileLocation);
        if (!f.exists()) {
            FileUtils.copyURLToFile(new URL(url), f);
            System.out.println("File downloaded to " + f.getAbsolutePath());
        } else {
            System.out.println("Using existing text file at " + f.getAbsolutePath());
        }

        if (!f.exists()) throw new IOException("File does not exist: " + fileLocation);    //Download problem?

        char[] validCharacters = {'#', '*', '-', '%', ':', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        //char[] validCharacters = CharacterIterator.getMinimalCharacterSet();	//Which characters are allowed? Others will be removed
        return new CharacterIterator(fileLocation, Charset.forName("UTF-8"),
                miniBatchSize, sequenceLength, validCharacters, new Random(12345));
    }
}