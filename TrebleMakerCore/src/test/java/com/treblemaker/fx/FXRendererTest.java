package com.treblemaker.fx;

import com.treblemaker.SpringConfiguration;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "return_queue_early_for_tests=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999", "spring.datasource.url=jdbc:mysql://localhost:3306/hivecomposedb", "spring.datasource.username=root", "spring.datasource.password=redrobes79D"})
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