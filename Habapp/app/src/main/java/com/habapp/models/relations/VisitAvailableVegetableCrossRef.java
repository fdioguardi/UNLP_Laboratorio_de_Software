package com.habapp.models.relations;

import androidx.room.Entity;

@Entity(primaryKeys = {"vegetableId", "visitId"}, tableName = "visit_available_vegetable_cross_ref")
public class VisitAvailableVegetableCrossRef {
    private long visitId;
    private long vegetableId;

    public VisitAvailableVegetableCrossRef(long visitId, long vegetableId) {
        this.visitId = visitId;
        this.vegetableId = vegetableId;
    }

    public long getVegetableId() {
        return vegetableId;
    }

    public void setVegetableId(int vegetableId) {
        this.vegetableId = vegetableId;
    }

    public long getVisitId() {
        return visitId;
    }

    public void setVisitId(int visitId) {
        this.visitId = visitId;
    }
}
