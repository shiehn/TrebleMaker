package com.treblemaker.renderers;

import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.rendertransports.HarmonicLoopRenderTransport;
import com.treblemaker.renderers.interfaces.IHarmonicLoopRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HarmonicLoopAltRenderer extends BaseRenderer implements IHarmonicLoopRenderer {

    @Autowired
    public AppConfigs appConfigs;

    @Override
    public void renderHarmonicLoops(String loopsTargetPath, String loopFileName, HarmonicLoopRenderTransport harmonicLoopRenderTransport) throws Exception {

        //TODO YOU MIGHT HAVE TO THINK ABOUT SHIMS!!

        //TODO RIGHT NOW A HARMONIC LOOP IS MADITORY AT ALL TIMES ..
        Application.logger.debug("LOG: Start Concatinating HARMONIC loops ..");

        List<String> sourceFilesList = new ArrayList<String>();

        for (HarmonicLoop harmonicLoopAlt : harmonicLoopRenderTransport.getHarmonicLoops()) {
            if(harmonicLoopAlt != null){
                sourceFilesList.add(appConfigs.getHarmonicLoopsFullPath(harmonicLoopAlt));
            }else{
                Application.logger.debug("LOG: - ERROR: STEVE YOU NEED TO HANDLE HARMONIC LOOP SHIMS");
                 Application.logger.debug("LOG: - ERROR: STEVE YOU NEED TO HANDLE HARMONIC LOOP SHIMS");
                  Application.logger.debug("LOG: - ERROR: STEVE YOU NEED TO HANDLE HARMONIC LOOP SHIMS");
                 Application.logger.debug("LOG: - ERROR: STEVE YOU NEED TO HANDLE HARMONIC LOOP SHIMS");}

            //sourceFilesList.add(appConfigs.COMPOSITION_OUTPUT + "audioshims//" + queueItem.getQueueItemId() + "//" + createShimNameForAmbienceLoops(ambienceLoop));
        }

        concatenateFiles(sourceFilesList, loopsTargetPath + "/" + loopFileName);
    }

    @Override
    public HarmonicLoopRenderTransport extractLoopsToRender(QueueItem queueItem) {

        List<HarmonicLoop> harmonicLoopsAlt = new ArrayList<>();

        queueItem.getProgression().getStructure().forEach(pUnit -> {
            pUnit.getProgressionUnitBars().forEach(pBar -> {
                if(pBar.getHarmonicLoopAlt() != null && pBar.getHarmonicLoopAlt().getCurrentBar() == 1){
                    harmonicLoopsAlt.add(pBar.getHarmonicLoopAlt());
                }
            });
        });

        HarmonicLoopRenderTransport harmonicLoopRenderTransport = new HarmonicLoopRenderTransport();
        harmonicLoopRenderTransport.setHarmonicLoops(harmonicLoopsAlt);
        return harmonicLoopRenderTransport;
    }
}
