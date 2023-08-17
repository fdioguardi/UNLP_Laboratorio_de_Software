package com.habapp.models.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.habapp.models.Farm;
import com.habapp.models.Visit;

import java.util.List;

// used to access the Farm it's Visits. It's dao methods are in `FarmDao`.
public class FarmWithVisits {
    @Embedded
    private Farm farm;

    @Relation(parentColumn = "farmId", entityColumn = "farmId")
    private List<Visit> visits;

    public Farm getFarm() {
        return farm;
    }

    public void setFarm(Farm farm) {
        this.farm = farm;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> Visits) {
        this.visits = Visits;
    }
}
