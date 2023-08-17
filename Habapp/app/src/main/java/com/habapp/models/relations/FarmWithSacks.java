package com.habapp.models.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.habapp.models.Farm;
import com.habapp.models.Sack;

import java.util.List;

public class FarmWithSacks {
    @Embedded
    private Farm farm;

    @Relation(parentColumn = "farmId", entityColumn = "farmId")
    private List<Sack> sacks;

    public Farm getFarm() {
        return farm;
    }

    public void setFarm(Farm farm) {
        this.farm = farm;
    }

    public List<Sack> getSacks() {
        return sacks;
    }

    public void setSacks(List<Sack> sack) {
        this.sacks = sacks;
    }
}
