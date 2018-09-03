package com.treblemaker.integration;

import com.treblemaker.Application;
import com.treblemaker.SpringConfiguration;
import com.treblemaker.dal.interfaces.IBachChoraleDal;
import com.treblemaker.model.HiveChord;
import com.treblemaker.model.melodic.BachChorale;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class BackChoraleDalTests {

    @Autowired
    IBachChoraleDal bachChoraleDal;

    @Test
    public void shouldConvertAllRecordsToHiveChords() {

        List<BachChorale> chorales = bachChoraleDal.findAll();

        for (BachChorale chorale : chorales) {
            try{
                HiveChord hiveChord = new HiveChord(chorale.getChord());
            }catch (Exception e){
                Application.logger.debug("LOG:", e);
                assertThat(true).isFalse();
            }
        }

        assertThat(chorales).isNotEmpty();
    }
}
