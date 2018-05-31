//package com.treblemaker.stations;
//
//import com.treblemaker.SpringConfiguration;
//import com.treblemaker.dal.interfaces.IStationTrackDal;
//import com.treblemaker.model.stations.StationTrack;
//import com.treblemaker.services.AudioTransferService;
//import junit.framework.TestCase;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = SpringConfiguration.class)
//public class StationUploadServiceTest extends TestCase {
//
//    StationUploadService stationUploadService;
//
//    IStationTrackDal stationTrackDal;
//
//    @Autowired
//    AudioTransferService audioTransferService;
//
//    AudioTransferService audioTransferServiceSpy;
//
//    @Before
//    public void setup() throws InterruptedException {
//
//        audioTransferServiceSpy = Mockito.spy(audioTransferService);
//
//        IStationTrackDal stationTrackDal = Mockito.mock(IStationTrackDal.class);
//        stationUploadService = Mockito.mock(StationUploadService.class);
//        AudioTransferService audioTransferService = Mockito.mock(AudioTransferService.class);
//
//        StationTrack stationTrackOne = new StationTrack();
//        stationTrackOne.setName("gmin7_gmin_emin_a#maj_80_null_2016-11-10_4783.wav");
//        stationTrackOne.setFile("7edd4faf-d8a3-4328-a8f2-11f5b187b315");
//        stationTrackOne.setUploaded(1);
//
//        StationTrack stationTrackTwo = new StationTrack();
//        stationTrackTwo.setName("dmin7_dmin_dmin_a#maj_80_null_2016-11-10_4783.wav");
//        stationTrackTwo.setFile("3652c2c7-1d28-4b9d-8997-5c49f0e450c5");
//        stationTrackTwo.setUploaded(0);
//
//        List<StationTrack> stationTracks = Arrays.asList(stationTrackOne, stationTrackTwo);
//
//        when(stationTrackDal.findAll()).thenReturn(stationTracks);
//
//        doNothing().when(audioTransferServiceSpy).uploadAudioFile(anyString(),anyString(),anyString());
//
//        stationUploadService = new StationUploadService(audioTransferServiceSpy, stationTrackDal);
//    }
//
//    @Test
//    public void should_callUploadTrack_when_fetchAndUploadTrackCalled() throws Exception {
//
//        stationUploadService.fetchAndUploadTrack();
//
//        verify(audioTransferServiceSpy).uploadAudioFile("signalsandsocery","3652c2c7-1d28-4b9d-8997-5c49f0e450c5.wav","C:/\"Program Files\"/\"Apache Software Foundation\"/\"Tomcat 8.0\"/webapps/audio/3652c2c7-1d28-4b9d-8997-5c49f0e450c5.wav");
//    }
//}