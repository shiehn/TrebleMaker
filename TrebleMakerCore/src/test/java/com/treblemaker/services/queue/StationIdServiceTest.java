package com.treblemaker.services.queue;

import com.treblemaker.SpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringConfiguration.class, properties ={"connect_to_cache=true", "queue_scheduled_interval=8999999", "queue_scheduled_start_delay=8999999", "station_ids=1,2,3"})
public class StationIdServiceTest {

    @Autowired
    public StationIdService stationIdService;

    @Test
    public void shouldRotateStationIds(){

        int valueOne = stationIdService.getStationId();
        int valueTwo = stationIdService.getStationId();
        int valueThree = stationIdService.getStationId();
        int valueFour = stationIdService.getStationId();
        int valueFive = stationIdService.getStationId();
        int valueSix = stationIdService.getStationId();

        assertThat(valueOne).isEqualTo(1);
        assertThat(valueTwo).isEqualTo(2);
        assertThat(valueThree).isEqualTo(3);
        assertThat(valueFour).isEqualTo(1);
        assertThat(valueFive).isEqualTo(2);
        assertThat(valueSix).isEqualTo(3);
    }
}