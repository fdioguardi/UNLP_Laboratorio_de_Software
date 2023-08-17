package com.habapp.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.habapp.models.Visit;
import com.habapp.models.Vegetable;
import com.habapp.models.Visit;
import com.habapp.models.relations.VisitWithVegetables;
import com.habapp.models.relations.VisitAvailableVegetableCrossRef;

import java.util.List;

@Dao
public abstract class VisitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insert(Visit... visits);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(Visit... visits);

    @Delete
    public abstract void delete(Visit... visits);

    @Query("DELETE FROM visit")
    public abstract void deleteAll();

    @Query("SELECT * FROM visit ORDER BY date ASC")
    public abstract LiveData<List<Visit>> getChronologically();

    @Query("SELECT * FROM visit WHERE visitId = :visitId")
    public abstract LiveData<Visit> findByVisitId(long visitId);

    /* Visit - Vegetable methods*/

    // insert a VisitAvailableVegetableCrossRef, linking a Visit and a Vegetable
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insert(VisitAvailableVegetableCrossRef... visitAvailableVegetableCrossRefs);

    // remove the relationship between a Visit and it's Available Vegetables.
    // Used in the VisitRepository before removing a Visit.
    @Query("DELETE FROM visit_available_vegetable_cross_ref WHERE visitId = :visitId")
    public abstract void removeVisitAvailableVegetableCrossRefs(long visitId);

    // remove the relationship between a Visit and a single Available Vegetable.
    @Query("DELETE FROM visit_available_vegetable_cross_ref WHERE visitId = :visitId AND vegetableId = :vegetableId")
    public abstract void removeAvailableVegetableFromVisit(long visitId, long vegetableId);

    // Return the VisitWithVegetables that contains:
    //   the Visit with id = visitID
    //   and the list of vegetables related to it.
    @Transaction
    @Query("SELECT * FROM visit WHERE visitId = :visitId")
    public abstract LiveData<VisitWithVegetables> getVisitWithVegetables(long visitId);

    @Transaction
    public void insert(VisitWithVegetables visitWithVegetables) {
        Visit visit = visitWithVegetables.getVisit();
        visit.setFarmId(visit.getFarmId());
        long visitId = insert(visit)[0];
        if (visitWithVegetables.getAvailableVegetables() == null) {
            return;
        }
        for (Vegetable v: visitWithVegetables.getAvailableVegetables()) {
            insert(new VisitAvailableVegetableCrossRef(visitId, v.getVegetableId()));
        }
    }

    @Transaction
    public void newAvailableVegetablesForVisit(Visit visit, List<Vegetable> vegetables) {
        removeVisitAvailableVegetableCrossRefs(visit.getVisitId());
        for (Vegetable v: vegetables) {
            insert(new VisitAvailableVegetableCrossRef(visit.getVisitId(), v.getVegetableId()));
        }
    }

}
