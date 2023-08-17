package com.habapp.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.Month;

@Entity(tableName = "vegetable", indices = {@Index(value = {"name"}, unique = true)})
public class Vegetable {

    @PrimaryKey(autoGenerate = true)
    private long vegetableId;

    @NonNull
    private String name;

    @NonNull
    private String description;
    private Month sowing_month;
    private Month harvest_month;

    public Vegetable(@NonNull String name, @NonNull String description, Month sowing_month,
                     Month harvest_month) {
        this.name = name;
        this.description = description;
        this.sowing_month = sowing_month;
        this.harvest_month = harvest_month;
    }

    public long getVegetableId() {
        return vegetableId;
    }

    public void setVegetableId(long vegetableId) {
        this.vegetableId = vegetableId;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getDescription() {
        return this.description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public Month getSowing_month() {
        return this.sowing_month;
    }

    public void setSowing_month(Month sowing_month) {
        this.sowing_month = sowing_month;
    }

    public Month getHarvest_month() {
        return this.harvest_month;
    }

    public void setHarvest_month(Month harvest_month) {
        this.harvest_month = harvest_month;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        Vegetable vegetable = (Vegetable) o;

        return this.getName().equals(vegetable.getName()) &&
                this.getDescription().equals(vegetable.getDescription()) &&
                this.getHarvest_month().equals(vegetable.getSowing_month()) &&
                this.getSowing_month().equals(vegetable.getHarvest_month());
    }
}
