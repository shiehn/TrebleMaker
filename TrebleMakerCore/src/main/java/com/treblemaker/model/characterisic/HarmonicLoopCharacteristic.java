package com.treblemaker.model.characterisic;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "harmonic_loop_characteristics")
public class HarmonicLoopCharacteristic implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "harmonic_loop_id")
    private int harmonicLoopId;

    @Column(name = "characteristic_id")
    private int characteristicId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHarmonicLoopId() {
        return harmonicLoopId;
    }

    public void setHarmonicLoopId(int harmonicLoopId) {
        this.harmonicLoopId = harmonicLoopId;
    }

    public int getCharacteristicId() {
        return characteristicId;
    }

    public void setCharacteristicId(int characteristicId) {
        this.characteristicId = characteristicId;
    }
}
