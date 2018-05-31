package com.treblemaker.renderers;

import com.treblemaker.Application;
import com.treblemaker.configs.*;
import com.treblemaker.model.kick.KickPattern;
import com.treblemaker.model.kick.KickSample;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import com.treblemaker.configs.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Component
public class KickPatternRenderer extends BaseRenderer {

    @Autowired
    private AppConfigs appConfigs;

    private static String PATH_TO_SHIM = "";

    public void render(QueueState queueState) {

        // create a list of all the patterns ...
        List<KickPattern> patternsToRender = getPatternsToRender(queueState);

        // create a list of all the sounds ...
        List<KickSample> kickSamples = queueState.getDataSource().getKickSamples().stream().filter(k -> Integer.parseInt(k.getStationId()) == queueState.getQueueItem().getStationId()).collect(Collectors.toList());
        KickSample kickSample = kickSamples.get(new Random().nextInt(kickSamples.size()));

        //CREATE SHIM ..
        // create a list of all the beat sample paths
        List<String> kickPatternFilePaths = getKickPatternFilePaths(patternsToRender, kickSample, queueState.getQueueItem().getBpm());

        //targetPath + fileName
        String targetPath = queueState.getQueueItem().getAudioPartFilePath() + "/" + appConfigs.KICK_FILENAME;

        try {
            concatenateFiles(kickPatternFilePaths, targetPath);
        } catch (Exception e) {
            Application.logger.debug("LOG:", e);
        }

        return;
    }

    public List<KickPattern> getPatternsToRender(QueueState queueState) {

        List<KickPattern> patternsToRender = new ArrayList<>();

        for (ProgressionUnit progressionUnit : queueState.getStructure()) {
            for (int i = 0; i < progressionUnit.getProgressionUnitBars().size(); i++) {
//                if (i == 0 || i == 2) {
                    patternsToRender.add(progressionUnit.getProgressionUnitBars().get(i).getKickPattern());
//                }
            }
        }

        return patternsToRender;
    }

    public List<String> getKickPatternFilePaths(List<KickPattern> patternsToRender, KickSample kickSample, int bpm) {

        List<String> kickPatternFilePaths = new ArrayList<>();

        for (KickPattern kickPatternFilePath : patternsToRender) {
            for (int kickHit : kickPatternFilePath.getAsIntegerArray()) {
                if (kickHit == 0) {
                    kickPatternFilePaths.add(appConfigs.getKickShimFullPath(kickSample, bpm));
                } else if (kickHit == 1) {
                    kickPatternFilePaths.add(appConfigs.getKickFullPath(kickSample));
                } else {
                    throw new RuntimeException("Unexpected Kick Value");
                }
            }
        }

        return kickPatternFilePaths;
    }

    /*
    	public void concatenateAndFinalizeRendering(String targetPath, String fileName, List<BeatLoop> beatLoops) {
		try {

			Application.logger.debug("LOG:","Start Process BEAT");

			List<String> sourceFilesList = new ArrayList<String>();

			for (BeatLoop beat : beatLoops) {
				sourceFilesList.add(appConfigs.getBeatLoopfullPath(beat));
			}

			concatenateFiles(sourceFilesList, targetPath + fileName);

		} catch (Exception e) {
			//TODO handle exceptions .
			Application.logger.debug("LOG:","Process BEAT EXCEPTION : " + e.getLocalizedMessage());
		}
	}
     */
}
