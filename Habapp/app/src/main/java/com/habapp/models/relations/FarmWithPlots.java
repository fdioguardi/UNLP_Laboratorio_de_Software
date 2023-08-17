package com.habapp.models.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.habapp.models.Farm;
import com.habapp.models.Plot;

import java.util.List;

// used to access the Farm it's Plots. It's dao methods are in `FarmDao`.
public class FarmWithPlots {
    @Embedded
    private Farm farm;

    @Relation(parentColumn = "farmId", entityColumn = "farmId")
    private List<Plot> plots;

    public Farm getFarm() {
        return farm;
    }

    public void setFarm(Farm farm) {
        this.farm = farm;
    }

    public List<Plot> getPlots() {
        return plots;
    }

    public void setPlots(List<Plot> plots) {
        this.plots = plots;
    }
}