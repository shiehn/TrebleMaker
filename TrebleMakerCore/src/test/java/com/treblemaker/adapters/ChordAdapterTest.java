package com.treblemaker.adapters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.treblemaker.Application;
import com.treblemaker.SpringConfiguration;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.eventchain.interfaces.IEventChain;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.queues.QueueItem;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.jfugue.theory.Chord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class ChordAdapterTest extends TestCase {

    @Autowired
    @Qualifier(value = "jChordAppenderEvent")
    private IEventChain jChordAppenderEvent;

    @Autowired
    public AppConfigs appConfigs;

//	@Autowired
//	@Qualifier("queueHelpers")
//	private IQueueHelpers queueHelpers;

    private QueueItem mQueueItem;

    private void setupTest() {

        mQueueItem = new QueueItem();

        ObjectMapper mapper = new ObjectMapper();
        ProgressionDTO progression = new ProgressionDTO();

        try {
            progression = mapper.readValue(new File(appConfigs.getApplicationRoot() + "SoullessEngine\\src\\main\\java\\Tests\\Mocks\\MocksOne.json"), ProgressionDTO.class);

            //mQueueItem.setQueueItem(progression);
        } catch (Exception e) {
        }
    }

    @Test
    public void NumberOfChordsShouldMatchNumberOfJChords() {

        List<String> chordNames = new ArrayList<>();
        chordNames.add("aMAJ");
        chordNames.add("aMAJ6");
        chordNames.add("aMAJ7");
        chordNames.add("aMAJ9");
        chordNames.add("aMIN");
        chordNames.add("aMIN6");
        chordNames.add("aMIN7");
        chordNames.add("aMIN9");
        chordNames.add("aDOM7");
        chordNames.add("aDOM9");
        chordNames.add("aAUG");
        chordNames.add("aDIM");
        chordNames.add("aSUS4");
        chordNames.add("aSUS2");
        chordNames.add("aMIN7B5");

        chordNames.add("a#MAJ");
        chordNames.add("a#MAJ6");
        chordNames.add("a#MAJ7");
        chordNames.add("a#MAJ9");
        chordNames.add("a#MIN");
        chordNames.add("a#MIN6");
        chordNames.add("a#MIN7");
        chordNames.add("a#MIN9");
        chordNames.add("a#DOM7");
        chordNames.add("a#DOM9");
        chordNames.add("a#AUG");
        chordNames.add("a#DIM");
        chordNames.add("a#SUS4");
        chordNames.add("a#SUS2");
        chordNames.add("a#MIN7B5");

        chordNames.add("abMAJ");
        chordNames.add("abMAJ6");
        chordNames.add("abMAJ7");
        chordNames.add("abMAJ9");
        chordNames.add("abMIN");
        chordNames.add("abMIN6");
        chordNames.add("abMIN7");
        chordNames.add("abMIN9");
        chordNames.add("abDOM7");
        chordNames.add("abDOM9");
        chordNames.add("abAUG");
        chordNames.add("abDIM");
        chordNames.add("abSUS4");
        chordNames.add("abSUS2");
        chordNames.add("abMIN7B5");


        List<Chord> jfugeChords = new ArrayList<>();
        for(String chordName : chordNames){
            try{
                Chord jChord = new Chord(chordName);
                jfugeChords.add(jChord);
            }catch (Exception e){
                Application.logger.debug("LOG:", e);
            }
        }

        Assert.assertEquals(jfugeChords.size(), chordNames.size());
    }

}