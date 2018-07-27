package com.treblemaker.utils.shims;

import com.treblemaker.configs.AppConfigs;
import com.treblemaker.generators.ShimGenerator;
import com.treblemaker.utils.LoopUtils;

import java.io.File;

public class ShimGenerationUtil {

    public static void generateShims(AppConfigs appConfigs) {
        ShimGenerator shimGenerator = new ShimGenerator();
        LoopUtils loopUtils = new LoopUtils();

        File beatShimDir = new File(appConfigs.getBeatShimsDir());

        if(!beatShimDir.exists()){
            beatShimDir.mkdir();
        }

        for(int supportedBpm : appConfigs.SUPPORTED_BPM){

            String beatShimsPath = String.format(appConfigs.getBeatShimsDir() + appConfigs.BEAT_SHIM_FILE_NAME, supportedBpm);

            File beatShimsFilePath = new File(beatShimsPath);

            if(!beatShimsFilePath.exists()){

                float secondsInBar = loopUtils.getSecondsInBar(supportedBpm);
                float secondsInTwoBars = secondsInBar * 2;

                shimGenerator.generateSilence(secondsInTwoBars,beatShimsPath,1);
            }
        }
    }
}