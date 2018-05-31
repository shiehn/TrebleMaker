package com.treblemaker.renderers;

import com.treblemaker.Application;
import com.treblemaker.generators.interfaces.IShimGenerator;
import com.treblemaker.model.hitsandfills.Fill;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;
import com.treblemaker.renderers.interfaces.IFillsRenderer;
import com.treblemaker.utils.FileStructure;
import com.treblemaker.utils.LoopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.treblemaker.configs.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FillsRenderer implements IFillsRenderer {

    @Autowired
    private IShimGenerator shimGenerator;

    @Autowired
    BaseRenderer baseRenderer;

    @Autowired
    private AppConfigs appConfigs;

    @Autowired
    private FileStructure fileStructure;

    @Override
    public void render(QueueState queueState) throws Exception {

        List<Fill> fillsToRender = createFillsToRender(queueState.getStructure());

        List<String> fillFilesToRender = new ArrayList<>();

        for (Fill fill : fillsToRender) {
            if (fill == null) {
                fillFilesToRender.add(createFillShim(queueState).getAbsolutePath());
            } else {
                fillFilesToRender.add(appConfigs.getFillsFullPath(fill));
            }
        }

        if (!fillFilesToRender.isEmpty()) {
            baseRenderer.concatenateFiles(fillFilesToRender, queueState.getQueueItem().getAudioPartFilePath() + "/" +  appConfigs.FILLS_FILENAME);
        }
    }

    @Override
    public List<Fill> createFillsToRender(List<ProgressionUnit> progressionUnits) {

        List<Fill> fills = new ArrayList<>();

        for (ProgressionUnit pUnit : progressionUnits) {
            for (Integer i = 0; i < pUnit.getProgressionUnitBars().size(); i++) {
                if (i != 0 && pUnit.getProgressionUnitBars().get(i - 1).getFill() != null &&
                        !pUnit.getProgressionUnitBars().get(i - 1).getFill().getBarCount().equals(2)) {
                    fills.add(null);
                } else if (i != 0 && pUnit.getProgressionUnitBars().get(i - 1).getFill() != null &&
                        pUnit.getProgressionUnitBars().get(i - 1).getFill().getBarCount().equals(2)) {
                    //skipp
                } else if (pUnit.getProgressionUnitBars().get(i).getFill() != null) {
                    fills.add(pUnit.getProgressionUnitBars().get(i).getFill());
                } else {
                    fills.add(null);
                }
            }
        }

        return fills;
    }

    @Override
    public File createFillShim(QueueState queueState) {
        String filePath = fileStructure.createShimsDirectory(queueState.getQueueItem().getQueueItemId());

        String shimName = createNameForFillShim();

        String shimPath = filePath + "/" + shimName;

        if (new File(shimPath).exists()) {
            try {
                FileStructure.deleteFile(new File(shimPath));
            } catch (IOException e) {
                Application.logger.debug("LOG:", e);
            }
        }

        shimGenerator.generateSilence(LoopUtils.getBeatsInSeconds(queueState.getQueueItem().getBpm(), 4), shimPath, 1);

        return new File(shimPath);
    }

    public String createNameForFillShim() {

        //TODO IS THE NAME UNIQUE ENOUGH?? WHAT IF MULTIPLE SHIMS HAVE THE SAME LENGTH??

//        String shimLength = Float.toString(ambienceLoop.getShimLength());
//        String shimLengthString = shimLength.replaceAll("\\.", "");
        String shimName = "onebarfill.wav";

        return shimName;
    }
}