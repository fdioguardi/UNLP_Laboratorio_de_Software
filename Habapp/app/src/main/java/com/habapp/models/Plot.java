package com.habapp.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "plot")
public class Plot {

    @PrimaryKey(autoGenerate = true)
    private long plotId;
    private long farmId;
    @NonNull
    private String name;
    @NonNull
    private String description;
    private double agroecologicArea;
    private int rows;
    private boolean isCovered;

    public Plot(@NonNull String name, @NonNull String description, double agroecologicArea, int rows, boolean isCovered) {
        this.name = name;
        this.description = description;
        this.agroecologicArea = agroecologicArea;
        this.rows = rows;
        this.isCovered = isCovered;
    }

    public long getPlotId() {
        return plotId;
    }

    public void setPlotId(long plotId) {
        this.plotId = plotId;
    }

    public long getFarmId() {
        return farmId;
    }

    public void setFarmId(long farmId) {
        this.farmId = farmId;
    }

    @NonNull
    public String getDescription() {
        return this.description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public double getAgroecologicArea() {
        return this.agroecologicArea;
    }

    public void setAgroecologicArea(double agroecologicArea) {
        this.agroecologicArea = agroecologicArea;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public boolean isCovered() {
        return isCovered;
    }

    public void setCovered(boolean covered) {
        isCovered = covered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        Plot plot = (Plot) o;

        return this.getName().equals(plot.getName()) &&
                this.getDescription().equals(plot.getDescription()) &&
                this.getAgroecologicArea() == plot.getAgroecologicArea() &&
                this.getRows() == plot.getRows() &&
                this.isCovered() == plot.isCovered();
    }
}
