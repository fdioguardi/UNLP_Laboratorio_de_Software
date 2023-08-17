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
import com.habapp.models.Plot;
import com.habapp.models.Vegetable;
import com.habapp.models.relations.FarmWithPlots;
import com.habapp.models.relations.FarmWithVisits;
import com.habapp.models.relations.PlotVegetableCrossRef;
import com.habapp.models.relations.PlotWithVegetables;

import java.util.List;

@Dao
public abstract class FarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insert(Farm... farms);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(Farm... farms);

    @Delete
    public abstract void delete(Farm... farms);

    @Query("DELETE FROM farm")
    public abstract void deleteAll();

    @Query("SELECT * FROM farm ORDER BY name ASC")
    public abstract LiveData<List<Farm>> getAlphabetically();

    @Query("SELECT * FROM farm WHERE farmId = :farmId")
    public abstract LiveData<Farm> findByFarmId(long farmId);

    /* Farm - Plot methods*/

    // Return the FarmWithPlots that contains:
    //   the Farm with id = farmId
    //   and the list of plots related to it.
    @Transaction
    @Query("SELECT * FROM farm WHERE farmId = :farmId")
    public abstract LiveData<FarmWithPlots> getFarmWithPlots(long farmId);

    // Delete all plots related to the farm with id = farmId.
    @Query("DELETE FROM plot WHERE farmId = :farmId")
    public abstract void deletePlots(long farmId);

    @Transaction
    public void insert(Farm farm, List<PlotWithVegetables> plots) {
        long farmId = insert(farm)[0];
        for (PlotWithVegetables plotWV : plots) {
            Plot plot = plotWV.getPlot();
            plot.setFarmId(farmId);
            long plotId = insert(plot)[0];
            for (Vegetable v: plotWV.getVegetables()) {
                insert(new PlotVegetableCrossRef(plotId, v.getVegetableId()));
            }
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insert(Plot... plots);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insert(PlotVegetableCrossRef... plotVegetableCrossRefs);

    @Transaction
    @Query("SELECT * FROM farm WHERE farmId = :farmId")
    public abstract LiveData<FarmWithVisits> getFarmWithVisits(long farmId);
}
