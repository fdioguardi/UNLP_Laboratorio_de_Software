package com.habapp.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.habapp.models.Farm;
import com.habapp.models.Sack;
import com.habapp.models.Sack;
import com.habapp.models.Vegetable;
import com.habapp.models.relations.SackVegetableCrossRef;
import com.habapp.models.relations.SackWithVegetables;

import java.util.List;

@Dao
public abstract class SackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insert(Sack... sacks);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(Sack... sacks);

    @Delete
    public abstract void delete(Sack... sacks);

    @Query("DELETE FROM sack")
    public abstract void deleteAll();

    @Query("SELECT * FROM sack ORDER BY creationDate ASC")
    public abstract LiveData<List<Sack>> getChronologically();

    @Query("SELECT * FROM sack WHERE sackId = :sackId")
    public abstract LiveData<Sack> findBySackId(long sackId);

    /* Sack - Vegetable methods*/

    // insert a SackVegetableCrossRef, linking a Sack and a Vegetable
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insert(SackVegetableCrossRef... sackVegetableCrossRefs);

    // remove the relationship between a Sack and it's Vegetables.
    // Used in the SackRepository before removing a Sack.
    @Query("DELETE FROM sack_vegetable_cross_ref WHERE sackId = :sackId")
    public abstract void removeSackVegetableCrossRefs(long sackId);

    // remove the relationship between a Sack and a single Vegetable.
    @Query("DELETE FROM sack_vegetable_cross_ref WHERE sackId = :sackId AND vegetableId = :vegetableId")
    public abstract void removeVegetableFromSack(long sackId, long vegetableId);

    // Return the SackWithVegetables that contains:
    //   the Sack with id = sackID
    //   and the list of vegetables related to it.
    @Transaction
    @Query("SELECT * FROM sack WHERE sackId = :sackId")
    public abstract LiveData<SackWithVegetables> getSackWithVegetables(long sackId);

    @Transaction
    public void insert(SackWithVegetables sackWithVegetables) {
        Sack sack = sackWithVegetables.getSack();
        sack.setFarmId(sack.getFarmId());
        long sackId = insert(sack)[0];
        if (sackWithVegetables.getVegetables() == null) {
            return;
        }
        for (Vegetable v: sackWithVegetables.getVegetables()) {
            insert(new SackVegetableCrossRef(sackId, v.getVegetableId()));
        }
    }

    @Transaction
    public void newVegetablesForSack(Sack sack, List<Vegetable> vegetables) {
        removeSackVegetableCrossRefs(sack.getSackId());
        for (Vegetable v: vegetables) {
            insert(new SackVegetableCrossRef(sack.getSackId(), v.getVegetableId()));
        }
    }
}
