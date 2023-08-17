package com.habapp.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.habapp.daos.PlotDao;
import com.habapp.dbs.HabappRoomDatabase;
import com.habapp.models.Plot;
import com.habapp.models.Vegetable;
import com.habapp.models.relations.PlotVegetableCrossRef;
import com.habapp.models.relations.PlotWithVegetables;

import java.util.List;

public class PlotRepository {

    private final PlotDao mPlotDao;

    public PlotRepository(Application application) {
        HabappRoomDatabase db = HabappRoomDatabase.getDatabase(application);
        this.mPlotDao = db.plotDao();
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Plot... plots) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mPlotDao.insert(plots));
    }

    public void update(Plot... plots) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mPlotDao.update(plots));
    }

    public void delete(Plot... plots) {
        for (Plot plot : plots) {
            this.removePlotVegetableCrossRefs(plot.getPlotId());
        }
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mPlotDao.delete(plots));
    }

    public LiveData<Plot> findByPlotID(long plotId) {
        return mPlotDao.findByPlotId(plotId);
    }

    /* Plot - Vegetable methods*/

    public void insert(PlotVegetableCrossRef... plotVegetableCrossRefs) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mPlotDao.insert(plotVegetableCrossRefs));
    }

    public void removePlotVegetableCrossRefs(long plotId) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mPlotDao.removePlotVegetableCrossRefs(plotId));
    }

    public void removeVegetableFromPlot(long plotId, long vegetableId) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mPlotDao.removeVegetableFromPlot(plotId, vegetableId));
    }

    public LiveData<PlotWithVegetables> getPlotWithVegetables(long plotId) {
        return mPlotDao.getPlotWithVegetables(plotId);
    }

    public void insert(PlotWithVegetables plotWithVegetables) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mPlotDao.insert(plotWithVegetables));
    }

    public void newVegetablesForPlot(Plot plot, List<Vegetable> vegetables) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mPlotDao.newVegetablesForPlot(plot, vegetables));
    }
}