package com.treblemaker.renderers;

import com.treblemaker.SpringConfiguration;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class EqFilterRendererTest extends TestCase {

    @Autowired
    private EqFilterRenderer eqFilterRenderer;

    String MOCKS = Paths.get("src","main","java","com","treblemaker","tests","Mocks").toString();
    String testSourceFile = Paths.get(MOCKS,"fxrendersourcefile","noHighPass.wav").toString();
    String sourcePath = Paths.get(MOCKS, "fxrenderfiles","noHighPass.wav").toString();
    String targetPath = Paths.get(MOCKS, "fxrenderfiles","highPass.wav").toString();

    @Before
    public void setup() throws IOException {
        cleanup();
        //MOVE TEST SOURCE TO SOURCE ..
        Files.copy(new File(testSourceFile).toPath(), new File(sourcePath).toPath());
    }

    @Test
    public void shouldRenderMelodyWithReverb(){
        final Integer frequencyCutoff = 1000;
        eqFilterRenderer.render(sourcePath, targetPath, frequencyCutoff);
        assertThat(new File(sourcePath).exists()).isFalse();
        assertThat(new File(targetPath).exists()).isTrue();
    }

    public void cleanup() {
        File target = new File(targetPath);
        if(target.exists()){
            target.delete();
        }

        File source = new File(sourcePath);
        if(source.exists()){
            source.delete();
        }
    }
}