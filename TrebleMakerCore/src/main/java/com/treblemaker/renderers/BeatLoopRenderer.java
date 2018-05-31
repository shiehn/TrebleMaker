package com.treblemaker.renderers;

import com.treblemaker.Application;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.rendertransports.BeatLoopRenderTransport;
import com.treblemaker.renderers.interfaces.IBeatLoopRenderer;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.treblemaker.configs.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class BeatLoopRenderer extends BaseRenderer implements IBeatLoopRenderer {

	@Autowired
	private AppConfigs appConfigs;

	@Override
	public void renderRhythm(String queueItemId, String rhythmTargetPath, String rhythmFileName, BeatLoopRenderTransport beatLoopRenderTransport) {

		concatenateAndFinalizeRendering(queueItemId, rhythmTargetPath, rhythmFileName, beatLoopRenderTransport.getBeatLoops());
	}

	@Override
	public BeatLoopRenderTransport extractLoopsToRender(QueueItem queueItem) {

		List<BeatLoop> beatLoops = new ArrayList<BeatLoop>();

		queueItem.getProgression().getStructure().forEach(pUnit -> {
			pUnit.getProgressionUnitBars().forEach(pUnitBar ->  {
				if(pUnitBar.getBeatLoop().getCurrentBar() == 1){
					beatLoops.add(pUnitBar.getBeatLoop());
				}
			});
		});

		BeatLoopRenderTransport beatLoopRenderTransport = new BeatLoopRenderTransport();
		beatLoopRenderTransport.setBeatLoops(beatLoops);
		return beatLoopRenderTransport;
	}

	@Override
	public void concatenateAndFinalizeRendering(String queueItemId, String targetPath, String fileName, List<BeatLoop> beatLoops) {
		try {

			Application.logger.debug("LOG: Start Process BEAT");

			List<String> sourceFilesList = new ArrayList<String>();

			for (BeatLoop beat : beatLoops) {
				sourceFilesList.add(appConfigs.getBeatLoopfullPath(beat));
			}

			concatenateFiles(sourceFilesList, targetPath + "/" + fileName);

		} catch (Exception e) {
			//TODO handle exceptions .
			Application.logger.debug("LOG: Process BEAT EXCEPTION : " + e.getLocalizedMessage());
		}
	}
}
