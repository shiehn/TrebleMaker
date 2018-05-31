package com.treblemaker.utils.loopcorrection;

import com.treblemaker.Application;
import com.treblemaker.dal.interfaces.IBeatLoopsDal;
import com.treblemaker.dal.interfaces.IStationDal;
import com.treblemaker.model.BeatLoop;
import com.treblemaker.scheduledevents.GlobalState;
import com.treblemaker.services.audiofilter.NormalizeAudio;
import com.treblemaker.utils.interfaces.IAudioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BeatLoopCorrectionUtil extends LoopCorrectionBase {

    private IBeatLoopsDal beatLoopsDal;

    private IAudioUtils audioUtils;

    private NormalizeAudio normalizeAudio;

    private IStationDal stationDal;

    @Autowired
    public BeatLoopCorrectionUtil(IBeatLoopsDal beatLoopsDal, IAudioUtils audioUtils, NormalizeAudio normalizeAudio, IStationDal stationDal){

        this.beatLoopsDal = beatLoopsDal;
        this.audioUtils = audioUtils;
        this.normalizeAudio = normalizeAudio;
        this.stationDal = stationDal;
    }

    public void correctLoopLengths() {

        try {

            List<BeatLoop> beatLoops = beatLoopsDal.findByNormalizedLength(BeatLoop.NOT_YET_NORMALIZED);

            for (BeatLoop beatLoop : beatLoops) {

                String fullFilePath = Paths.get(appConfigs.getApplicationRoot(),"Loops", Integer.toString(beatLoop.getStationId()), "BeatLoops", beatLoop.getFileName()).toString();

                audioUtils.resampleToMono16Bit(fullFilePath);

                correctLoopLength(fullFilePath, fullFilePath, beatLoop);

                beatLoopsDal.save(beatLoop);
            }
        } catch (Exception e) {
            Application.logger.debug("LOG:", e);
        }
    }

    public void correctLoopVolumes() throws IOException {
        if(GlobalState.getInstance().isLoopCorrectionInProgress()) {
           return;
        }

        GlobalState.getInstance().setLoopCorrectionInProgress(true);

//        long startTime = System.nanoTime();
        stationDal.findAll().stream().filter(station -> station.getStatus() == 1 && station.getId() == 3).collect(Collectors.toList()).forEach(station -> {
            try {
                normalizeAudio.normalizeLoopFiles(appConfigs.getBeatLoopsLocation(station.getId()));
            } catch (IOException e) {
                Application.logger.debug("LOG:", e);
            }
        });
//        long endTime = System.nanoTime();
//        long duration = (endTime - startTime)/1000000;  //divide by 1000000 to get milliseconds.

        GlobalState.getInstance().setLoopCorrectionInProgress(false);
    }
}
