package com.habapp.models.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.habapp.models.Plot;
import com.habapp.models.Vegetable;

import java.util.List;

// used to access the Plot and the Vegetables associated to it. It's dao methods are in `PlotDao`.
public class PlotWithVegetables {
    @Embedded
    private Plot plot;

    @Relation(parentColumn = "plotId", entityColumn = "vegetableId", associateBy = @Junction(PlotVegetableCrossRef.class))
    private List<Vegetable> vegetables;

    public PlotWithVegetables(Plot plot, List<Vegetable> vegetables) {
        this.plot = plot;
        this.vegetables = vegetables;
    }

    public Plot getPlot() {
        return plot;
    }

    public void setPlot(Plot plot) {
        this.plot = plot;
    }

    public List<Vegetable> getVegetables() {
        return vegetables;
    }

    public void setVegetables(List<Vegetable> vegetables) {
        this.vegetables = vegetables;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        PlotWithVegetables plotWithVegetables = (PlotWithVegetables) o;

        return this.getPlot().equals(plotWithVegetables.getPlot());
    }
}