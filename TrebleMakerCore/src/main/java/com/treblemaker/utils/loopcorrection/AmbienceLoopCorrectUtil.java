package com.treblemaker.utils.loopcorrection;

import com.treblemaker.Application;
import com.treblemaker.configs.AppConfigs;
import com.treblemaker.dal.interfaces.IStationDal;
import com.treblemaker.scheduledevents.GlobalState;
import com.treblemaker.services.audiofilter.NormalizeAudio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class AmbienceLoopCorrectUtil extends LoopCorrectionBase {

    @Autowired
    public AppConfigs appConfigs;

    @Autowired
    private NormalizeAudio normalizeAudio;

    @Autowired
    private IStationDal stationDal;

    public void correctLoopVolumes() throws IOException {
        if(GlobalState.getInstance().isLoopCorrectionInProgress()) {
            return;
        }

        GlobalState.getInstance().setLoopCorrectionInProgress(true);

        stationDal.findAll().stream().filter(station -> station.getStatus() == 1).collect(Collectors.toList()).forEach(station -> {
            try {
                normalizeAudio.normalizeLoopFiles(appConfigs.getAmbienceLocation(station.getId()));
            } catch (IOException e) {
                Application.logger.debug("LOG:", e);
            }
        });

        GlobalState.getInstance().setLoopCorrectionInProgress(false);
    }
}
