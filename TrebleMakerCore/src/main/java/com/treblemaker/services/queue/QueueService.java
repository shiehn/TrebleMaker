package com.treblemaker.services.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.treblemaker.Application;
import com.treblemaker.configs.*;
import com.treblemaker.dal.interfaces.ICompositionDal;
import com.treblemaker.dal.interfaces.ICompositionTimeSlotDal;
import com.treblemaker.dal.interfaces.IQueueItemsDal;
import com.treblemaker.dal.interfaces.IStationDal;
import com.treblemaker.model.composition.Composition;
import com.treblemaker.model.composition.CompositionTimeSlot;
import com.treblemaker.model.progressions.ProgressionDTO;
import com.treblemaker.model.progressions.ProgressionUnit;
import com.treblemaker.model.queues.QueueItem;
import com.treblemaker.model.stations.Station;
import com.treblemaker.utils.FileStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.treblemaker.constants.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.treblemaker.model.progressions.ProgressionUnit.BarCount.FOUR;

@Service
public class QueueService {

    @Autowired
    private IQueueItemsDal queueItemsDal;

    @Autowired
    private ICompositionDal compositionDal;

    @Autowired
    private ICompositionTimeSlotDal compositionTimeSlotDal;

    @Autowired
    private IStationDal stationDal;

    @Autowired
    private StationIdService stationIdService;

    @Autowired
    private AppConfigs appConfigs;

    @Autowired
    @Value("${max_rated_songs_on_disk}") int maxRatedSongsOnDisk;

    @Autowired
    @Value("${max_unrated_songs_on_disk}") int maxUnratedSongsOnDisk;

    private Station getNextStation(){

        int stationId = stationIdService.getStationId();

        List<Station> stations = stationDal.findAll().stream().filter(station -> station.getStatus().equals(1) && station.getId() == stationId).collect(Collectors.toList());

        Station station = stations.get(new Random().nextInt(stations.size()));
        return station;
    }

    public long addQueueItem() throws IOException {
        String queueId = UUID.randomUUID().toString();

        Station station = getNextStation();

        QueueItem queueItem = generateQueueItem();
        queueItem.setQueueItemId(queueId);
        queueItem.setJobStatus(0);
        queueItem.setOutputPath("");
        queueItem.setBpm(station.getBpm());
        queueItem.setUsers_id(1);
        queueItem.setStationId(station.getId());
        queueItem.setDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));

        try {
            queueItemsDal.save(queueItem);
        } catch (Exception e) {
            Application.logger.debug("LOG:", e);
        }

        Application.logger.debug("LOG: QueueItem : " + queueId + " was successfully added");

        return queueItem.getId();
    }

    private QueueItem generateQueueItem() throws IOException {

        List<ProgressionUnit> structure = new ArrayList<>();
        structure.add(generateProgressionUnitVerse());
        structure.add(generateProgressionUnitVerse());
        structure.add(generateProgressionUnitChorus());
        structure.add(generateProgressionUnitVerse());
        structure.add(generateProgressionUnitVerse());
        structure.add(generateProgressionUnitChorus());
        structure.add(generateProgressionUnitBridge());
        structure.add(generateProgressionUnitChorus());

        ProgressionDTO progressionDTO = new ProgressionDTO();

        progressionDTO.setStructure(structure);

        QueueItem queueItem = new QueueItem();
        queueItem.setUsers_id(1); //hard code to only user ..

        ObjectMapper mapper = new ObjectMapper();
        String progressionDTOString = mapper.writeValueAsString(progressionDTO);

        queueItem.setQueueItem(progressionDTOString);

        return queueItem;
    }

    private ProgressionUnit generateProgressionUnitVerse() {

        ProgressionUnit progressionUnit = null;

        progressionUnit = new ProgressionUnit();

        progressionUnit.setType(ProgressionUnit.ProgressionType.VERSE);
        progressionUnit.setComplexity(70);
        progressionUnit.setMinorToMajor(5);
        progressionUnit.setBarCount( FOUR.getValue());

        return progressionUnit;
    }

    private ProgressionUnit generateProgressionUnitBridge() {

        ProgressionUnit progressionUnit = new ProgressionUnit();

        progressionUnit.setType(ProgressionUnit.ProgressionType.BRIDGE);
        progressionUnit.setComplexity(100);
        progressionUnit.setMinorToMajor(5);
        progressionUnit.setBarCount(FOUR.getValue());

        return progressionUnit;
    }

    private ProgressionUnit generateProgressionUnitChorus() {

        ProgressionUnit progressionUnit = new ProgressionUnit();

        progressionUnit.setType(ProgressionUnit.ProgressionType.CHORUS);
        progressionUnit.setComplexity(70);
        progressionUnit.setMinorToMajor(5);
        progressionUnit.setBarCount( FOUR.getValue());

        return progressionUnit;
    }


    private void deleteRatedAudioparts(String compositionId){

        String deleteDirectory = appConfigs.getCompositionOutput() + "/audioparts/" + compositionId;

        Application.logger.debug("LOG:","deleteDirectory : " + deleteDirectory);

        FileStructure.deleteAllFilesInDirectory(deleteDirectory);
    }

    private void deleteRatedAudioshims(String compositionId){

        String deleteDirectory = appConfigs.getCompositionOutput() + "/audioshims/" + compositionId;

        Application.logger.debug("LOG:","deleteDirectory : " + deleteDirectory);

        FileStructure.deleteAllFilesInDirectory(deleteDirectory);
    }

    private void deleteRatedMidioutput(String compositionId){

        String deleteDirectory = appConfigs.getCompositionOutput() + "/midioutput/" + compositionId;

        Application.logger.debug("LOG:","deleteDirectory : " + deleteDirectory);

        FileStructure.deleteAllFilesInDirectory(deleteDirectory);
    }

    public void deleteRatedCompositions() {

        Application.logger.debug("LOG:","Directory to deleteFile  : " + appConfigs.getFinalMixOutput());
        File[] audioFiles = FileStructure.getFilesInDirectory(appConfigs.getFinalMixOutput());

        for (File file : audioFiles) {

            Application.logger.debug("LOG:","file NAME : " + file.getName().replace(".wav", ""));
            Composition composition = compositionDal.findByCompositionUid(file.getName().replace(".wav", ""));
            if (composition != null) {
                List<CompositionTimeSlot> timeSlots = composition.getCompositionTimeSlots();
                if (timeSlots != null && !timeSlots.isEmpty() && timeSlots.get(0).getRated() == RATED) {
                    Application.logger.debug("LOG:","RATED SONG TO DELETE : " + appConfigs.getFinalMixOutput() + "/" + file.getName());
                    FileStructure.deleteSingleFile(appConfigs.getFinalMixOutput() + "/" + file.getName());

                    //DELETE OTHER SONG FILES i.e. audioparts & midi files
                    deleteRatedAudioparts(file.getName().replace(".wav", ""));
                    deleteRatedAudioshims(file.getName().replace(".wav", ""));
                    deleteRatedMidioutput(file.getName().replace(".wav", ""));
                }
            }
        }
    }

    public boolean isAtRatedSongCapacity(){
        int ratedSongs = countRatedSongs();
        Application.logger.debug("LOG:","ratedSongs : " + ratedSongs);
        Application.logger.debug("LOG:","maxRatedSongsOnDisk : " + maxRatedSongsOnDisk);
        return countRatedSongs() > maxRatedSongsOnDisk;
    }


    public boolean isAtUnratedSongCompacity(){

        int unratedSongs = countUnratedSongs();
        Application.logger.debug("LOG:","ratedSongs : " + unratedSongs);
        Application.logger.debug("LOG:","maxUnratedSongsOnDisk : " + maxUnratedSongsOnDisk);
        return countUnratedSongs() >  maxUnratedSongsOnDisk;
    }

    public void deleteOrphanedCompositions(){

        Application.logger.debug("LOG:","deleteOrphanedCompositions ENTERED");
        Application.logger.debug("LOG:","Directory to deleteFile  : " + appConfigs.getFinalMixOutput());
        File[] audioFiles = FileStructure.getFilesInDirectory(appConfigs.getFinalMixOutput());

        Map<String, String> compositionsDelete = new HashMap<>();

        List<Composition> compositions = compositionDal.findAll();

        for (Composition composition : compositions) {

            List<CompositionTimeSlot> timeslots = composition.getCompositionTimeSlots();

            boolean shouldDeleteOrphan = true;
            boolean isNotRated = false;

            for(CompositionTimeSlot ts : timeslots){
                if(ts.getRated() == UNRATED){
                    if(ts.getCompositionId().equals(composition.getId())) {
                        isNotRated = true;
                        if (exisitsOnDisk(composition.getCompositionUid(), audioFiles)) {
                            shouldDeleteOrphan = false;
                            break;
                        }
                    }
                }
            }

            if(shouldDeleteOrphan && isNotRated){

                Application.logger.debug("LOG:","ORPHAN TO DELETE !!! : " + composition.getCompositionUid());
                Application.logger.debug("LOG:","ORPHAN TO DELETE !!! : " + composition.getCompositionUid());
                Application.logger.debug("LOG:","ORPHAN TO DELETE !!! : " + composition.getCompositionUid());
                Application.logger.debug("LOG:","ORPHAN TO DELETE !!! : " + composition.getCompositionUid());
                removeOrphanRecordsFromDatabase(composition.getCompositionUid());
            }
        }
    }

    private void removeOrphanRecordsFromDatabase(String compositionUid){

        Composition composition = compositionDal.findByCompositionUid(compositionUid);

        List<CompositionTimeSlot> timeSlots = composition.getCompositionTimeSlots();

        for (CompositionTimeSlot timeSlot : timeSlots) {
            Application.logger.debug("LOG: atemping timeslot deleteFile");
            try {
                compositionTimeSlotDal.delete(timeSlot);
            }catch(Exception e){
                Application.logger.debug("LOG:", e);
            }
            Application.logger.debug("LOG: successfull timeslot deleteFile");
        }

        Application.logger.debug("LOG: attemping composition deleteFile 0f " + composition.getCompositionUid());
        try {
            compositionDal.delete(composition);
        }catch(Exception e){
            Application.logger.debug("LOG:", e);
        }
        Application.logger.debug("LOG: successfull composition deleteFile !!!");
    }

    private boolean exisitsOnDisk(String compId, File[] audioFiles){

        if(audioFiles == null){
            return false;
        }

        for(File file : audioFiles){
            if(file.getName().toLowerCase().contains(compId.toLowerCase())){
                Application.logger.debug("LOG: " + file.getName() + " EXISITS ON DISK");
                return true;
            }
        }

        return false;
    }

    private final int UNRATED = 0;
    private final int RATED = 1;

    public int countRatedSongs(){

        int ratedSongs = 0;

        File[] audioFiles = FileStructure.getFilesInDirectory(appConfigs.getFinalMixOutput());

        if(audioFiles == null){
            return ratedSongs;
        }

        for (File audioFile : audioFiles) {

            Composition composition = compositionDal.findByCompositionUid(audioFile.getName().replace(".wav",""));
            if(composition != null){
                List<CompositionTimeSlot> timeSlots = composition.getCompositionTimeSlots();
                if(timeSlots != null && !timeSlots.isEmpty()){
                    if(timeSlots.get(0).getRated() == RATED){
                        ratedSongs++;
                    }
                }
            }
        }

        return ratedSongs;
    }

    public int countUnratedSongs(){

        int unrated = 0;

        File[] audioFiles = FileStructure.getFilesInDirectory(appConfigs.getFinalMixOutput());

        if(audioFiles == null){
            return unrated;
        }

        for (File audioFile : audioFiles) {

            Composition composition = compositionDal.findByCompositionUid(audioFile.getName().replace(".mp3",""));
            if(composition != null){
                List<CompositionTimeSlot> timeSlots = composition.getCompositionTimeSlots();
                if(timeSlots != null && !timeSlots.isEmpty()){
                    if(timeSlots.get(0).getRated() == UNRATED){
                        unrated++;
                    }
                }
            }
        }

        return unrated;
    }
}
