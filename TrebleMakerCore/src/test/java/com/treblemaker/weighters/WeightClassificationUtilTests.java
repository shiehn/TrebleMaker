package com.treblemaker.weighters;

import com.treblemaker.weighters.enums.WeightClass;
import com.treblemaker.weighters.models.EqWeightResponse;
import com.treblemaker.weighters.models.Ratings;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WeightClassificationUtilTests {

    @Test
    public void shouldConvertToCorrectWeightClass(){

        WeightClass bad = WeightClassificationUtils.verticalWeightToClass("0");
        WeightClass ok = WeightClassificationUtils.verticalWeightToClass("1");
        WeightClass good = WeightClassificationUtils.verticalWeightToClass("2");

        assertThat(bad).isEqualTo(WeightClass.BAD);
        assertThat(ok).isEqualTo(WeightClass.OK);
        assertThat(good).isEqualTo(WeightClass.GOOD);
    }

    private List<Ratings> generateBands(){

        List<Ratings> eqBands = new ArrayList<>();

        //LOW
        eqBands.add(new Ratings(){{setFreq("FREQ_20"); setRating(Ratings.BAD);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_25"); setRating(Ratings.BAD);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_31"); setRating(Ratings.BAD);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_40"); setRating(Ratings.BAD);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_50"); setRating(Ratings.BAD);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_63"); setRating(Ratings.BAD);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_80"); setRating(Ratings.OK);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_100");setRating(Ratings.OK);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_125");setRating(Ratings.OK);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_160");setRating(Ratings.GOOD);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_200");setRating(Ratings.GOOD);}});

        //MID
        eqBands.add(new Ratings(){{setFreq("FREQ_250"); setRating(Ratings.GOOD);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_315"); setRating(Ratings.GOOD);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_400"); setRating(Ratings.GOOD);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_500"); setRating(Ratings.GOOD);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_630"); setRating(Ratings.GOOD);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_800"); setRating(Ratings.GOOD);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_1000");setRating(Ratings.OK);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_1250");setRating(Ratings.OK);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_1600");setRating(Ratings.OK);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_2000");setRating(Ratings.BAD);}});

        //HI
        eqBands.add(new Ratings(){{setFreq("FREQ_2500"); setRating(Ratings.GOOD);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_3150"); setRating(Ratings.GOOD);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_4000"); setRating(Ratings.GOOD);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_5000"); setRating(Ratings.GOOD);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_6300"); setRating(Ratings.OK);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_8000"); setRating(Ratings.OK);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_10000");setRating(Ratings.OK);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_12500");setRating(Ratings.OK);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_16000");setRating(Ratings.BAD);}});
        eqBands.add(new Ratings(){{setFreq("FREQ_20000");setRating(Ratings.BAD);}});

        return eqBands;
    }

    @Test
    public void shouldClassifyAsOk(){

        EqWeightResponse eqWeightResponse = new EqWeightResponse();
        eqWeightResponse.setEqbands(generateBands());

        WeightClass weightClass = WeightClassificationUtils.weightHiEqRegister(eqWeightResponse);

        assertThat(weightClass).isEqualTo(WeightClass.OK);
    }

    @Test
    public void shouldClassifyAsGood(){

        EqWeightResponse eqWeightResponse = new EqWeightResponse();
        eqWeightResponse.setEqbands(generateBands());

        WeightClass weightClass = WeightClassificationUtils.weightMidEqRegister(eqWeightResponse);

        assertThat(weightClass).isEqualTo(WeightClass.GOOD);
    }

    @Test
    public void shouldClassifyAsBad(){

        EqWeightResponse eqWeightResponse = new EqWeightResponse();
        eqWeightResponse.setEqbands(generateBands());

        WeightClass weightClass = WeightClassificationUtils.weightLowEqRegister(eqWeightResponse);

        assertThat(weightClass).isEqualTo(WeightClass.BAD);
    }

    @Test
    public void shouldClassifyOveralOk(){

        EqWeightResponse eqWeightResponse = new EqWeightResponse();
        eqWeightResponse.setEqbands(generateBands());

        WeightClass weightClass = WeightClassificationUtils.eqResponseToWeightClass(eqWeightResponse);

        assertThat(weightClass).isEqualTo(WeightClass.OK);
    }
}