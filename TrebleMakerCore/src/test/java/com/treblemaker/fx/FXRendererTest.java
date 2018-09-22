package com.treblemaker.fx;

import com.treblemaker.SpringConfiguration;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@ComponentScan({"com.treblemaker"})
@SpringBootTest(classes = SpringConfiguration.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class FXRendererTest extends TestCase {

    @Autowired
    private IFXRenderer fxRenderer;

    private final String TEST_SOURCE_FILE = Paths.get("src","main","java","com","treblemaker","tests","Mocks","fxrendersourcefile","noReverb.wav").toString();
    private final String SOURCE_PATH = Paths.get("src","main","java","com","treblemaker","tests","Mocks","fxrenderfiles","noReverb.wav").toString();
    private final String TARGETPATH = Paths.get("src","main","java","com","treblemaker","tests","Mocks","fxrenderfiles","withReverb.wav").toString();

    @Before
    public void setup() throws IOException {

        cleanup();

        //MOVE TEST SOURCE TO SOURCE ..
        Files.copy(new File(TEST_SOURCE_FILE).toPath(), new File(SOURCE_PATH).toPath());
    }

    @Test
    public void shouldRenderMelodyWithReverb(){
        fxRenderer.renderReverbFx(SOURCE_PATH, TARGETPATH);
        assertThat(new File(SOURCE_PATH).exists()).isFalse();
        assertThat(new File(TARGETPATH).exists()).isTrue();
    }

    @After
    public void tearDown(){
        cleanup();
    }

    public void cleanup() {
        File target = new File(TARGETPATH);
        if(target.exists()){
            target.delete();
        }

        File source = new File(SOURCE_PATH);
        if(source.exists()){
            source.delete();
        }
    }
}