package com.treblemaker.model.composition;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "composition")
public class Composition {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "composition_uid")
    private String compositionUid;

    @Column(name = "date")
    private String date;

    @OneToMany(mappedBy = "compositionId", fetch = FetchType.EAGER)
    private List<CompositionTimeSlot> compositionTimeSlots;

    public List<CompositionTimeSlot> getCompositionTimeSlots() {

        if(this.compositionTimeSlots != null){
            return this.compositionTimeSlots;
        }

        return new ArrayList<CompositionTimeSlot>();
    }

    public void setCompositionTimeSlots(List<CompositionTimeSlot> timeSlots) {
        this.compositionTimeSlots = timeSlots;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompositionUid() {
        return compositionUid;
    }

    public void setCompositionUid(String compositionUid) {
        this.compositionUid = compositionUid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
