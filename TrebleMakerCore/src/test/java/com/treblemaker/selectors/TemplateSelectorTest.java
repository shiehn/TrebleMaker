package com.treblemaker.selectors;

import com.treblemaker.SpringConfiguration;
import com.treblemaker.model.SynthTemplate;
import com.treblemaker.selectors.interfaces.ITemplateSelector;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class TemplateSelectorTest extends TestCase {

    @Autowired
    private ITemplateSelector templateSelector;

    @Test
    public void shouldSetTemplate() throws Exception {

        SynthTemplate synthTemplate = templateSelector.chooseRandom();

        Assert.assertTrue(synthTemplate.getHiSynthId() != 0);
        Assert.assertTrue(!synthTemplate.getHiSynthName().equalsIgnoreCase(""));

        Assert.assertTrue(synthTemplate.getHiSynthIdAlt() != 0);
        Assert.assertTrue(!synthTemplate.getHiSynthNameAlt().equalsIgnoreCase(""));

        Assert.assertTrue(synthTemplate.getMidSynthId() != 0);
        Assert.assertTrue(!synthTemplate.getMidSynthName().equalsIgnoreCase(""));

        Assert.assertTrue(synthTemplate.getMidSynthIdAlt() != 0);
        Assert.assertTrue(!synthTemplate.getMidSynthNameAlt().equalsIgnoreCase(""));

        Assert.assertTrue(synthTemplate.getLowSynthId() != 0);
        Assert.assertTrue(!synthTemplate.getLowSynthName().equalsIgnoreCase(""));

        Assert.assertTrue(synthTemplate.getLowSynthIdAlt() != 0);
        Assert.assertTrue(!synthTemplate.getLowSynthNameAlt().equalsIgnoreCase(""));
    }

    @Test
    public void shouldReturnUniqueTemplates() throws Exception {

          List<SynthTemplate> templates = templateSelector.chooseRandomList(3);

          assertThat(templates).hasSize(3);

          for(int i=0; i<templates.size(); i++){
              int matches = 0;
              for(int j=0; j<templates.size(); j++){
                  if(templates.get(i).getId().equals(templates.get(j))){
                      matches++;
                  }
              }

              if(matches>1){
                  assertThat(true).isFalse();
              }
          }
    }
}