package com.treblemaker.options;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.TestBase;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.ProcessingState;
import com.treblemaker.model.SourceData;
import com.treblemaker.model.types.ProcessingPattern;
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
public class OptionsFilterTest extends TestBase {

    @Autowired
    OptionsFilter optionsFilter;

    @Test
    public void shouldFilter4BarOptions_whenProcessingStateIsL3(){

        SourceData sourceData = createSourceData();

        ProcessingState processingState = new ProcessingState();
        processingState.setProcessingPattern(ProcessingPattern.TWO_BAR);
        processingState.setCurrentState(ProcessingState.ProcessingStates.C1_L3);

        List<HarmonicLoop> harmonicLoops = optionsFilter.filterByProcessingState(sourceData.getHarmonicLoops(0), processingState);

        assertThat(harmonicLoops).hasSize(2);
    }

    @Test
    public void shouldFilterAllBut4BarOptions_whenProcessingStateIsC1_L3(){

        SourceData sourceData = createSourceData();

        ProcessingState processingState = new ProcessingState();
        processingState.setProcessingPattern(ProcessingPattern.FOUR_BAR);
        processingState.setCurrentState(ProcessingState.ProcessingStates.C1_L1);

        List<HarmonicLoop> harmonicLoops = optionsFilter.filterByProcessingState(sourceData.getHarmonicLoops(0), processingState);

        assertThat(harmonicLoops).hasSize(1);
        assertThat(harmonicLoops.get(0).getFileName()).isEqualToIgnoringCase("loopFourBar");
    }
}