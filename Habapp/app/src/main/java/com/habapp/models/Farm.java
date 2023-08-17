package com.habapp.models;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.habapp.utils.Location;

@Entity(tableName = "farm")
public class Farm {
    @PrimaryKey(autoGenerate = true)
    private long farmId;

    @NonNull
    private String name;
    @NonNull
    private String address;
    private double area;
    @NonNull
    @Embedded
    private Location location;

    public Farm(@NonNull String name, @NonNull String address, double area, @NonNull Location location) {
        this.name = name;
        this.address = address;
        this.area = area;
        this.location = location;
    }

    public long getFarmId() {
        return farmId;
    }

    public void setFarmId(long farmId) {
        this.farmId = farmId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    @NonNull
    public Location getLocation() {
        return location;
    }

    public void setLocation(@NonNull Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
