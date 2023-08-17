package com.habapp.models.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.habapp.models.Sack;
import com.habapp.models.Vegetable;

import java.util.List;

public class SackWithVegetables {
    @Embedded
    private Sack sack;

    @Relation(parentColumn = "sackId", entityColumn = "vegetableId", associateBy = @Junction(SackVegetableCrossRef.class))
    private List<Vegetable> vegetables;

    public SackWithVegetables(Sack sack, List<Vegetable> vegetables) {
        this.sack = sack;
        this.vegetables = vegetables;
    }

    public Sack getSack() {
        return sack;
    }

    public void setSack(Sack sack) {
        this.sack = sack;
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
        SackWithVegetables sackWithVegetables = (SackWithVegetables) o;

        return this.getSack().equals(sackWithVegetables.getSack());
    }
}
