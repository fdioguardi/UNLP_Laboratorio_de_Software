package com.habapp.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.habapp.daos.FarmDao;
import com.habapp.dbs.HabappRoomDatabase;
import com.habapp.models.Farm;
import com.habapp.models.Plot;
import com.habapp.models.relations.FarmWithPlots;
import com.habapp.models.relations.FarmWithVisits;
import com.habapp.models.relations.PlotWithVegetables;

import java.util.List;

public class FarmRepository {

    private final FarmDao mFarmDao;
    private final LiveData<List<Farm>> mAllFarms;

    public FarmRepository(Application application) {
        HabappRoomDatabase db = HabappRoomDatabase.getDatabase(application);
        this.mFarmDao = db.farmDao();
        mAllFarms = mFarmDao.getAlphabetically();
    }

    public LiveData<List<Farm>> getAllFarms() {
        return mAllFarms;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Farm... farms) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mFarmDao.insert(farms));
    }

    public void insert(Farm farm, List<PlotWithVegetables> plotsWV) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mFarmDao.insert(farm, plotsWV));
    }

    public void update(Farm... farms) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mFarmDao.update(farms));
    }

    public void delete(Farm... farms) {
        for (Farm farm : farms) {
            HabappRoomDatabase.databaseWriteExecutor.execute(() -> mFarmDao.deletePlots(farm.getFarmId()));

        }
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mFarmDao.delete(farms));
    }

    public LiveData<Farm> findByFarmId(long farmId) {
        return mFarmDao.findByFarmId(farmId);
    }

    /* Farm - Plot methods*/

    public LiveData<FarmWithPlots> getFarmWithPlots(long farmId) {
        return mFarmDao.getFarmWithPlots(farmId);
    }

    public LiveData<FarmWithVisits> getFarmWithVisits(long farmId) {
        return mFarmDao.getFarmWithVisits(farmId);
    }
}