package com.habapp.models.relations;

import androidx.room.Entity;

@Entity(primaryKeys = {"vegetableId", "sackId"}, tableName = "sack_vegetable_cross_ref")
public class SackVegetableCrossRef {
    private long sackId;
    private long vegetableId;

    public SackVegetableCrossRef(long sackId, long vegetableId) {
        this.sackId = sackId;
        this.vegetableId = vegetableId;
    }

    public long getVegetableId() {
        return vegetableId;
    }

    public void setVegetableId(int vegetableId) {
        this.vegetableId = vegetableId;
    }

    public long getSackId() {
        return sackId;
    }

    public void setSackId(int sackId) {
        this.sackId = sackId;
    }
}
