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
import com.habapp.models.relations.PlotVegetableCrossRef;
import com.habapp.models.relations.PlotWithVegetables;

import java.util.List;

@Dao
public abstract class PlotDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insert(Plot... plots);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(Plot... plots);

    @Delete
    public abstract void delete(Plot... plots);

    @Query("DELETE FROM plot")
    public abstract void deleteAll();

    @Query("SELECT * FROM plot WHERE plotId = :plotId")
    public abstract LiveData<Plot> findByPlotId(long plotId);

    /* Plot - Vegetable methods*/

    // insert a PlotVegetableCrossRef, linking a Plot and a Vegetable
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insert(PlotVegetableCrossRef... plotVegetableCrossRefs);

    // remove the relationship between a Plot and it's Vegetables.
    // Used in the PlotRepository before removing a Plot.
    @Query("DELETE FROM plot_vegetable_cross_ref WHERE plotId = :plotId")
    public abstract void removePlotVegetableCrossRefs(long plotId);

    // remove the relationship between a Plot and a single Vegetable.
    @Query("DELETE FROM plot_vegetable_cross_ref WHERE plotId = :plotId AND vegetableId = :vegetableId")
    public abstract void removeVegetableFromPlot(long plotId, long vegetableId);

    // Return the PlotWithVegetables that contains:
    //   the Plot with id = plotID
    //   and the list of vegetables related to it.
    @Transaction
    @Query("SELECT * FROM plot WHERE plotId = :plotId")
    public abstract LiveData<PlotWithVegetables> getPlotWithVegetables(long plotId);

    @Transaction
    public void insert(PlotWithVegetables plotWithVegetables) {
        Plot plot = plotWithVegetables.getPlot();
        plot.setFarmId(plot.getFarmId());
        long plotId = insert(plot)[0];
        if (plotWithVegetables.getVegetables() == null) {
            return;
        }
        for (Vegetable v: plotWithVegetables.getVegetables()) {
            insert(new PlotVegetableCrossRef(plotId, v.getVegetableId()));
        }
    }

    @Transaction
    public void newVegetablesForPlot(Plot plot, List<Vegetable> vegetables) {
        removePlotVegetableCrossRefs(plot.getPlotId());
        for (Vegetable v: vegetables) {
            insert(new PlotVegetableCrossRef(plot.getPlotId(), v.getVegetableId()));
        }
    }
}
