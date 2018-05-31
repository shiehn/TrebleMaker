package com.treblemaker.utils.loopcorrection;

import com.treblemaker.Application;
import com.treblemaker.dal.interfaces.IHarmonicLoopsDal;
import com.treblemaker.dal.interfaces.IStationDal;
import com.treblemaker.model.HarmonicLoop;
import com.treblemaker.scheduledevents.GlobalState;
import com.treblemaker.services.audiofilter.NormalizeAudio;
import com.treblemaker.utils.interfaces.IAudioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.treblemaker.configs.AppConfigs;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Component
public class HarmonicLoopCorrectionUtil extends LoopCorrectionBase {

    @Autowired
    public AppConfigs appConfigs;

    @Autowired
    private IHarmonicLoopsDal iHarmonicLoopsDal;

    @Autowired
    private IStationDal stationDal;

    @Autowired
    private IAudioUtils audioUtils;

    @Autowired
    private NormalizeAudio normalizeAudio;

    public void correctLoopLengths() {

        Iterable<HarmonicLoop> harmonicLoops = iHarmonicLoopsDal.findByNormalizedLength(HarmonicLoop.NOT_YET_NORMALIZED);

        for (HarmonicLoop harmonicLoop : harmonicLoops) {
            try {

                String fullFilePath = Paths.get(appConfigs.getApplicationRoot(),"Loops", Integer.toString(harmonicLoop.getStationId()),"HarmonicLoops", harmonicLoop.getFileName()).toString();

                audioUtils.resampleToMono16Bit(fullFilePath);

                correctLoopLength(fullFilePath, fullFilePath, harmonicLoop);

                iHarmonicLoopsDal.save(harmonicLoop);

            } catch (Exception e) {
                Application.logger.debug("LOG:", e);
            }
        }
    }

    public void correctLoopVolumes() throws IOException {
        if (GlobalState.getInstance().isLoopCorrectionInProgress()) {
            return;
        }

        GlobalState.getInstance().setLoopCorrectionInProgress(true);

        //appConfigs.getHarmonicLoopsLocation()
        //iHarmonicLoopsDal.findAll().stream().filter(loop -> loop)

        stationDal.findAll().stream().filter(station -> station.getStatus() ==  1 && station.getId() == 3).collect(Collectors.toList()).forEach(station -> {

            try {
                normalizeAudio.normalizeLoopFiles(appConfigs.getHarmonicLoopsLocation(station.getId()));
            } catch (IOException e) {
                Application.logger.debug("LOG:", e);
            }
        });

        GlobalState.getInstance().setLoopCorrectionInProgress(false);
    }
}