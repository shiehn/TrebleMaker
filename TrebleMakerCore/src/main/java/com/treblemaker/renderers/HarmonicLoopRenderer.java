package com.treblemaker.renderers;

import com.treblemaker.Application;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.rendertransports.HarmonicLoopRenderTransport;
import com.treblemaker.renderers.interfaces.IHarmonicLoopRenderer;
import org.springframework.stereotype.Component;
import com.treblemaker.configs.AppConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HarmonicLoopRenderer extends BaseRenderer implements IHarmonicLoopRenderer {

    @Autowired
    public AppConfigs appConfigs;

    @Override
    public void renderHarmonicLoops(String harmonicLoopsTargetPath, String harmonicLoopFileName, HarmonicLoopRenderTransport harmonicLoopRenderTransport) throws Exception {

        //TODO YOU MIGHT HAVE TO THINK ABOUT SHIMS!!

        //TODO RIGHT NOW A HARMONIC LOOP IS MADITORY AT ALL TIMES ..

        Application.logger.debug("LOG: Start Concatinating HARMONIC loops ..");

        List<String> sourceFilesList = new ArrayList<String>();

        for (HarmonicLoop harmonicLoop : harmonicLoopRenderTransport.getHarmonicLoops()) {
            if(harmonicLoop != null){
                sourceFilesList.add(appConfigs.getHarmonicLoopsFullPath(harmonicLoop));
            }
            //sourceFilesList.add(appConfigs.COMPOSITION_OUTPUT + "audioshims//" + queueItem.getQueueItemId() + "//" + createShimNameForAmbienceLoops(ambienceLoop));
        }

        concatenateFiles(sourceFilesList, harmonicLoopsTargetPath + "/" + harmonicLoopFileName);
    }

    @Override
    public HarmonicLoopRenderTransport extractLoopsToRender(QueueItem queueItem) {

        List<HarmonicLoop> harmonicLoops = new ArrayList<>();

        queueItem.getProgression().getStructure().forEach(pUnit -> {
            pUnit.getProgressionUnitBars().forEach(pBar -> {
                if(pBar.getHarmonicLoop().getCurrentBar() == 1){
                    harmonicLoops.add(pBar.getHarmonicLoop());
                }
            });
        });

        HarmonicLoopRenderTransport harmonicLoopRenderTransport = new HarmonicLoopRenderTransport();
        harmonicLoopRenderTransport.setHarmonicLoops(harmonicLoops);
        return harmonicLoopRenderTransport;
    }
}
