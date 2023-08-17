package com.habapp.models.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.habapp.models.Visit;
import com.habapp.models.Vegetable;

import java.util.List;

public class VisitWithVegetables {
    @Embedded
    private Visit visit;

    @Relation(parentColumn = "visitId", entityColumn = "vegetableId", associateBy = @Junction(VisitAvailableVegetableCrossRef.class))
    private List<Vegetable> availableVegetables;


    public VisitWithVegetables(Visit visit, List<Vegetable> availableVegetables) {
        this.visit = visit;
        this.availableVegetables = availableVegetables;
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public List<Vegetable> getAvailableVegetables() {
        return availableVegetables;
    }

    public void setAvailableVegetables(List<Vegetable> vegetables) {
        this.availableVegetables = vegetables;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        VisitWithVegetables visitWithVegetables = (VisitWithVegetables) o;

        return this.getVisit().equals(visitWithVegetables.getVisit());
    }
}
