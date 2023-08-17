package com.habapp.models.relations;

import androidx.room.Entity;

@Entity(primaryKeys = {"vegetableId", "plotId"}, tableName = "plot_vegetable_cross_ref")
public class PlotVegetableCrossRef {
    private long plotId;
    private long vegetableId;

    public PlotVegetableCrossRef(long plotId, long vegetableId) {
        this.plotId = plotId;
        this.vegetableId = vegetableId;
    }

    public long getVegetableId() {
        return vegetableId;
    }

    public void setVegetableId(int vegetableId) {
        this.vegetableId = vegetableId;
    }

    public long getPlotId() {
        return plotId;
    }

    public void setPlotId(int plotId) {
        this.plotId = plotId;
    }
}
