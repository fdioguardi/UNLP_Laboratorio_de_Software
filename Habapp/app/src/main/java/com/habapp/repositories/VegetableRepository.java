package com.habapp.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.habapp.daos.VegetableDao;
import com.habapp.dbs.HabappRoomDatabase;
import com.habapp.models.Vegetable;

import java.util.List;

public class VegetableRepository {

    private final VegetableDao mVegetableDao;
    private final LiveData<List<Vegetable>> mAllVegetables;

    public VegetableRepository(Application application) {
        HabappRoomDatabase db = HabappRoomDatabase.getDatabase(application);
        mVegetableDao = db.vegetableDao();
        mAllVegetables = mVegetableDao.getAlphabetically();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Vegetable>> getAllVegetables() {
        return mAllVegetables;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Vegetable vegetable) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mVegetableDao.insert(vegetable));
    }

    public void update(Vegetable vegetable) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mVegetableDao.update(vegetable));
    }

    public void delete(Vegetable vegetable) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mVegetableDao.delete(vegetable));
    }

    public LiveData<Vegetable> findByVegetableName(String name) {
        return mVegetableDao.findByVegetableName(name);
    }

    public LiveData<Vegetable> findByVegetableID(long id) {
        return mVegetableDao.findByVegetableID(id);
    }

    public void removeVegetableReferences(long vegetableId) {
        HabappRoomDatabase.databaseWriteExecutor.execute(() -> mVegetableDao.removeVegetableReferences(vegetableId));
    }
}