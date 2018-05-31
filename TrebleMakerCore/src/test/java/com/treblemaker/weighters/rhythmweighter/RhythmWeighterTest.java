package com.treblemaker.weighters.rhythmweighter;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.RhythmicAccents;
import com.treblemaker.weighters.interfaces.IRhythmWeighter;
import com.treblemaker.weighters.models.NormalizedAccents;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999"})
public class RhythmWeighterTest extends TestCase {

    @Autowired
    private IRhythmWeighter rhythmWeighter;

    /*
        RULES:
        1) matching notes get +2
        2) lone notes (no neighbours)get a +1
        3) lone notes (with matching neighbours) +0
        4) lone notes (with NON-matching neighbours) -1
     */

    @Test
    public void ShouldWeightNonMatchNoNeighbours() {

        //static int ACCENT_NON_MATCH_NO_NEIGHBOUR = 1;

        //REFERENCE LOOP ..
        RhythmicAccents accentsA = new RhythmicAccents();
        accentsA.setTwoOne("1");

        RhythmicAccents accentsB = new RhythmicAccents();
        accentsB.setThreeOne("1");

        List<RhythmicAccents> refAccents = new ArrayList<>();
        refAccents.add(accentsA);
        refAccents.add(accentsB);

        //LOOP ..
        RhythmicAccents accents1 = new RhythmicAccents();
        accents1.setTwoTwo("1");

        RhythmicAccents accents2 = new RhythmicAccents();
        accents2.setThreeThree("1");

        List<RhythmicAccents> accents = new ArrayList<>();
        accents.add(accents1);
        accents.add(accents2);

        HarmonicLoop referenceLoop = new HarmonicLoop();
        referenceLoop.setRhythmicAccents(refAccents);

        HarmonicLoop loop = new HarmonicLoop();
        loop.setRhythmicAccents(accents);

        NormalizedAccents normalizedAccents = rhythmWeighter.normalizeAccentLengths(referenceLoop, loop);

        //int matchingNoteWeights = rhythmWeighter.weightMatchNoNeighbours(normalizedAccents.getNormalizedReferenceAccents(), normalizedAccents.getNormalizedAccents());

        int loneNoteWeights = rhythmWeighter.weightNonMatchNoNeighbours(normalizedAccents.getNormalizedReferenceAccents(), normalizedAccents.getNormalizedAccents());

//        Assert.assertEquals(0, matchingNoteWeights);
        Assert.assertEquals(2, loneNoteWeights);
    }

    @Test
    public void ShouldWeightNonMatchNonMatchingNeighour() {

        //   1100 0001 0000 0100
        //   0100 0010 0000 1000
        //static int ACCENT_NON_MATCH_NEIGHBOUR_NON_MATCHING = -1;

        //REFERENCE LOOP ..
        RhythmicAccents accentsA = new RhythmicAccents();
        accentsA.setOneOne("1");
        accentsA.setOneTwo("1");
        accentsA.setTwoFour("1");
        accentsA.setFourTwo("1");

        RhythmicAccents accentsB = new RhythmicAccents();
        accentsB.setOneOne("1");
        accentsB.setOneTwo("1");
        accentsB.setTwoFour("1");
        accentsB.setFourTwo("1");

        List<RhythmicAccents> refAccents = new ArrayList<>();
        refAccents.add(accentsA);
        refAccents.add(accentsB);

        //LOOP ..
        RhythmicAccents accents1 = new RhythmicAccents();
        accents1.setTwoTwo("1");
        accents1.setFourOne("1");

        RhythmicAccents accents2 = new RhythmicAccents();
        accents2.setTwoTwo("1");
        accents2.setFourOne("1");

        List<RhythmicAccents> accents = new ArrayList<>();
        accents.add(accents1);
        accents.add(accents2);

        HarmonicLoop referenceLoop = new HarmonicLoop();
        referenceLoop.setRhythmicAccents(refAccents);

        HarmonicLoop loop = new HarmonicLoop();
        loop.setRhythmicAccents(accents);

        NormalizedAccents normalizedAccents = rhythmWeighter.normalizeAccentLengths(referenceLoop, loop);

        int loneNoteWeights = rhythmWeighter.weightNonMatchNonMatchingNeighour(normalizedAccents.getNormalizedReferenceAccents(), normalizedAccents.getNormalizedAccents());

        Assert.assertEquals(-8, loneNoteWeights);
    }

    @Test
    public void ShouldWeightMatchNoNeighbours() {

        //   1000 0010 0000 1000
        //   0001 0010 0000 1000
        //static int ACCENT_MATCH_WEIGHT = 2;

        //REFERENCE LOOP ..
        RhythmicAccents accentsA = new RhythmicAccents();
        accentsA.setOneOne("1");
        accentsA.setTwoThree("1");
        accentsA.setFourOne("1");

        RhythmicAccents accentsB = new RhythmicAccents();
        accentsB.setOneOne("1");
        accentsB.setTwoThree("1");
        accentsB.setFourOne("1");

        List<RhythmicAccents> refAccents = new ArrayList<>();
        refAccents.add(accentsA);
        refAccents.add(accentsB);

        //LOOP ..
        RhythmicAccents accents1 = new RhythmicAccents();
        accents1.setOneFour("1");
        accents1.setTwoThree("1");
        accents1.setFourOne("1");

        List<RhythmicAccents> accents = new ArrayList<>();
        accents.add(accents1);

        HarmonicLoop referenceLoop = new HarmonicLoop();
        referenceLoop.setRhythmicAccents(refAccents);

        HarmonicLoop loop = new HarmonicLoop();
        loop.setRhythmicAccents(accents);

        NormalizedAccents normalizedAccents = rhythmWeighter.normalizeAccentLengths(referenceLoop, loop);

        int loneNoteWeights = rhythmWeighter.weightMatchNoNeighbours(normalizedAccents.getNormalizedReferenceAccents(), normalizedAccents.getNormalizedAccents());

        Assert.assertEquals(8, loneNoteWeights);
    }


    @Test
    public void ShouldWeightNonMatchWithMatchingNeighour() {

        //   0000 0001 0000 0100
        //   0000 0011 0000 1100
        //static int ACCENT_NON_MATCH_NEIGHBOUR_MATCHING = 0;

        //REFERENCE LOOP ..
        RhythmicAccents accentsA = new RhythmicAccents();
        accentsA.setTwoFour("1");
        accentsA.setFourTwo("1");

        RhythmicAccents accentsB = new RhythmicAccents();
        accentsB.setTwoFour("1");
        accentsB.setFourTwo("1");

        List<RhythmicAccents> refAccents = new ArrayList<>();
        refAccents.add(accentsA);
        refAccents.add(accentsB);

        //LOOP ..
        RhythmicAccents accents1 = new RhythmicAccents();
        accents1.setTwoThree("1");
        accents1.setTwoFour("1");

        RhythmicAccents accents2 = new RhythmicAccents();
        accents2.setFourOne("1");
        accents2.setFourTwo("1");

        List<RhythmicAccents> accents = new ArrayList<>();
        accents.add(accents1);
        accents.add(accents2);

        HarmonicLoop referenceLoop = new HarmonicLoop();
        referenceLoop.setRhythmicAccents(refAccents);

        HarmonicLoop loop = new HarmonicLoop();
        loop.setRhythmicAccents(accents);

        NormalizedAccents normalizedAccents = rhythmWeighter.normalizeAccentLengths(referenceLoop, loop);

        int loneNoteWeights = rhythmWeighter.weightNonMatchWithMatchingNeighour(normalizedAccents.getNormalizedReferenceAccents(), normalizedAccents.getNormalizedAccents());

        Assert.assertEquals(0, loneNoteWeights);
    }

    @Test
    public void ShouldWeightMatchNonMatchingNeighour() {

        //   0000 0011 0000 1100
        //   0000 0010 0000 1000
        //static int ACCENT_MATCH_WEIGHT = 2;

        //REFERENCE LOOP ..
        RhythmicAccents accentsA = new RhythmicAccents();
        accentsA.setTwoThree("1");
        accentsA.setTwoFour("1");

        accentsA.setFourOne("1");
        accentsA.setFourTwo("1");

        List<RhythmicAccents> refAccents = new ArrayList<>();
        refAccents.add(accentsA);

        //LOOP ..
        RhythmicAccents accents1 = new RhythmicAccents();
        accents1.setTwoThree("1");
        accents1.setFourOne("1");

        List<RhythmicAccents> accents = new ArrayList<>();
        accents.add(accents1);

        HarmonicLoop referenceLoop = new HarmonicLoop();
        referenceLoop.setRhythmicAccents(refAccents);

        HarmonicLoop loop = new HarmonicLoop();
        loop.setRhythmicAccents(accents);

        NormalizedAccents normalizedAccents = rhythmWeighter.normalizeAccentLengths(referenceLoop, loop);

        //int matchingNoteWeights = rhythmWeighter.weightMatchNoNeighbours(normalizedAccents.getNormalizedReferenceAccents(), normalizedAccents.getNormalizedAccents());

        int weight = rhythmWeighter.weightMatchNonMatchingNeighour(normalizedAccents.getNormalizedReferenceAccents(), normalizedAccents.getNormalizedAccents());

        Assert.assertEquals(4, weight);
    }

    @Test
    public void ShouldWeightMatchWithMatchingNeighour() {

        //   0000 0011 0000 1100
        //   0000 0011 0000 1100
        //static int ACCENT_MATCH_WEIGHT = 2;

        //REFERENCE LOOP ..
        RhythmicAccents accentsA = new RhythmicAccents();
        accentsA.setTwoThree("1");
        accentsA.setTwoFour("1");

        accentsA.setFourOne("1");
        accentsA.setFourTwo("1");

        List<RhythmicAccents> refAccents = new ArrayList<>();
        refAccents.add(accentsA);

        //LOOP ..
        RhythmicAccents accents1 = new RhythmicAccents();
        accents1.setTwoThree("1");
        accents1.setTwoFour("1");

        accents1.setFourOne("1");
        accents1.setFourTwo("1");

        List<RhythmicAccents> accents = new ArrayList<>();
        accents.add(accents1);

        HarmonicLoop referenceLoop = new HarmonicLoop();
        referenceLoop.setRhythmicAccents(refAccents);

        HarmonicLoop loop = new HarmonicLoop();
        loop.setRhythmicAccents(accents);

        NormalizedAccents normalizedAccents = rhythmWeighter.normalizeAccentLengths(referenceLoop, loop);

        //int matchingNoteWeights = rhythmWeighter.weightMatchNoNeighbours(normalizedAccents.getNormalizedReferenceAccents(), normalizedAccents.getNormalizedAccents());

        int weight = rhythmWeighter.weightMatchWithMatchingNeighour(normalizedAccents.getNormalizedReferenceAccents(), normalizedAccents.getNormalizedAccents());

        Assert.assertEquals(8, weight);
    }

















































//    weightNonMatchNonMatchingNeighour







    @Test
    public void ShouldWeightMatchingNotes() {

        //   0000 0010 0000 1000
        //   0000 0010 0000 1000
        //static int ACCENT_MATCH_WEIGHT = 2;

        //REFERENCE LOOP ..
        RhythmicAccents accentsA = new RhythmicAccents();
        accentsA.setTwoThree("1");
        accentsA.setFourOne("1");

        List<RhythmicAccents> refAccents = new ArrayList<>();
        refAccents.add(accentsA);

        //LOOP ..
        RhythmicAccents accents1 = new RhythmicAccents();
        accents1.setTwoThree("1");
        accents1.setFourOne("1");

        List<RhythmicAccents> accents = new ArrayList<>();
        accents.add(accents1);

        HarmonicLoop referenceLoop = new HarmonicLoop();
        referenceLoop.setRhythmicAccents(refAccents);

        HarmonicLoop loop = new HarmonicLoop();
        loop.setRhythmicAccents(accents);

        NormalizedAccents normalizedAccents = rhythmWeighter.normalizeAccentLengths(referenceLoop, loop);

        int weight = rhythmWeighter.weightMatchNoNeighbours(normalizedAccents.getNormalizedReferenceAccents(), normalizedAccents.getNormalizedAccents());

        Assert.assertEquals(4, weight);
    }

    @Test
    public void ShouldMatchTemplateLengthsWhenReferenceIsGreater() {

        //REFERENCE LOOP
        List<RhythmicAccents> rAccents = new ArrayList<RhythmicAccents>(){{
            add(new RhythmicAccents());
            add(new RhythmicAccents());
            add(new RhythmicAccents());
            add(new RhythmicAccents());
        }};

        HarmonicLoop referenceLoop = new HarmonicLoop();
        referenceLoop.setRhythmicAccents(rAccents);

        //LOOP
        List<RhythmicAccents> accents = new ArrayList<RhythmicAccents>(){{
            add(new RhythmicAccents());
        }};

        HarmonicLoop loop = new HarmonicLoop();
        loop.setRhythmicAccents(accents);

        NormalizedAccents normalizedAccents = rhythmWeighter.normalizeAccentLengths(referenceLoop,loop);

        Assert.assertNotNull(normalizedAccents);
        Assert.assertNotNull(normalizedAccents.getAccentsList());
        Assert.assertNotNull(normalizedAccents.getReferenceAccentList());
        Assert.assertEquals(4, normalizedAccents.getAccentsList().size());
        Assert.assertEquals(normalizedAccents.getNormalizedAccents().size(),normalizedAccents.getNormalizedReferenceAccents().size());
        Assert.assertEquals(normalizedAccents.getReferenceAccentList().size(), normalizedAccents.getAccentsList().size());
    }

    @Test
    public void ShouldMatchTemplateLengthsWhenNonReferenceIsGreater() {

        //REFERENCE LOOP
        List<RhythmicAccents> rAccents = new ArrayList<RhythmicAccents>(){{
            add(new RhythmicAccents());
        }};

        HarmonicLoop referenceLoop = new HarmonicLoop();
        referenceLoop.setRhythmicAccents(rAccents);

        //LOOP
        List<RhythmicAccents> accents = new ArrayList<RhythmicAccents>(){{
            add(new RhythmicAccents());
            add(new RhythmicAccents());
            add(new RhythmicAccents());
            add(new RhythmicAccents());
        }};

        HarmonicLoop loop = new HarmonicLoop();
        loop.setRhythmicAccents(accents);

        NormalizedAccents normalizedAccents = rhythmWeighter.normalizeAccentLengths(referenceLoop,loop);

        Assert.assertNotNull(normalizedAccents);
        Assert.assertNotNull(normalizedAccents.getAccentsList());
        Assert.assertNotNull(normalizedAccents.getReferenceAccentList());
        Assert.assertEquals(4, normalizedAccents.getAccentsList().size());
        Assert.assertEquals(4, normalizedAccents.getReferenceAccentList().size());
        Assert.assertEquals(normalizedAccents.getNormalizedAccents().size(),normalizedAccents.getNormalizedReferenceAccents().size());
        Assert.assertEquals(normalizedAccents.getReferenceAccentList().size(), normalizedAccents.getAccentsList().size());
    }

    @Test
    public void ShouldRepeatMatchingTemplateLengths() {

        //REFERENCE LOOP
        List<RhythmicAccents> rAccents = new ArrayList<RhythmicAccents>(){{
            add(new RhythmicAccents());
            add(new RhythmicAccents());
            add(new RhythmicAccents());
            add(new RhythmicAccents());
        }};

        HarmonicLoop referenceLoop = new HarmonicLoop();
        referenceLoop.setRhythmicAccents(rAccents);

        //LOOP
        List<RhythmicAccents> accents = new ArrayList<RhythmicAccents>(){{
            add(new RhythmicAccents());
            add(new RhythmicAccents());
            add(new RhythmicAccents());
            add(new RhythmicAccents());
        }};

        HarmonicLoop loop = new HarmonicLoop();
        loop.setRhythmicAccents(accents);

        NormalizedAccents normalizedAccents = rhythmWeighter.normalizeAccentLengths(referenceLoop,loop);

        Assert.assertNotNull(normalizedAccents);
        Assert.assertNotNull(normalizedAccents.getAccentsList());
        Assert.assertNotNull(normalizedAccents.getReferenceAccentList());
        Assert.assertEquals(4, normalizedAccents.getReferenceAccentList().size());
        Assert.assertEquals(4, normalizedAccents.getAccentsList().size());
        Assert.assertEquals(normalizedAccents.getNormalizedAccents().size(),normalizedAccents.getNormalizedReferenceAccents().size());
        Assert.assertEquals(normalizedAccents.getReferenceAccentList().size(), normalizedAccents.getAccentsList().size());
    }


}
