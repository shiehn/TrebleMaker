package com.treblemaker;

import com.treblemaker.dal.interfaces.IKickSampleDal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
public class KickToDBTransferUtil {

    @Autowired
    IKickSampleDal kickSampleDal;

    final int BPM = 120;
    final String SAMPLE_DIRECTORY_PATH = "C:\\Capricious\\resampletest\\756aadec-7aac-441c-af40-f5221babc15e\\";

    public List<String> filePathsInDirectory(String directoryPath) {
        List<String> results = new ArrayList<String>();

        File[] files = new File(directoryPath).listFiles();

        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }

        return results;
    }
}
